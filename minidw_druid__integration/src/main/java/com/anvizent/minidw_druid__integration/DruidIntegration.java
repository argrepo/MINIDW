
package com.anvizent.minidw_druid__integration;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw_druid_integration_util.RestTemplateutil;
import com.datamodel.anvizent.common.exception.DruidException;
import com.datamodel.anvizent.errorhandler.CustomRestTemplateResponseErrorHandler;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.security.AnvizentEncryptionServiceImpl2;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.model.MiddleLevelManager;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageExecution;

public class DruidIntegration extends Thread {

	String mlmContextPath;
	String mlmWriteEndPoint;
	String mlmDeleteEndPoint;
	String mlmAddToUploadListEndPoint;
	String mlmUpgradeEndPoint;
	String mlmServicePrivateKey;
	String mlmServiceIv;
	String userID;
	List<String> derivedTablesListNew;
	String userAuthToken;
	String clientAuthToken;
	AnvizentEncryptionServiceImpl2 aes2;
	ScheduleDao scheduleDao;
	JdbcTemplate clientAppDbJdbcTemplate;
	String timeZone;
	long executionId;
    String clientid;
    Modification modification;
	private RestTemplateutil restTemplateutil = new RestTemplateutil();

	private HashMap<String, Integer> druidDataSourceMap = new HashMap<String, Integer>();
	private HashMap<String, Long> oldversionMap = new HashMap<String, Long>();
	private HashMap<String, Long> newVersionMap = new HashMap<String, Long>();

	public DruidIntegration(List<String> derivedTablesListNew, String userId, MiddleLevelManager middleLevelManager,String clientId,ScheduleDao scheduleDao,Modification modification,long executionId,String timeZone,JdbcTemplate clientAppDbJdbcTemplate) throws DruidException {

		this.mlmContextPath = middleLevelManager.getContextPath();
		this.mlmWriteEndPoint = middleLevelManager.getWriteEndPoint();
		this.mlmDeleteEndPoint = middleLevelManager.getDeleteEndPoint();
		this.mlmAddToUploadListEndPoint = middleLevelManager.getUploadListEndPoint();
		this.mlmUpgradeEndPoint = middleLevelManager.getUpgradeEndPoint();
		this.mlmServicePrivateKey = middleLevelManager.getEncryptionPrivateKey();
		this.mlmServiceIv = middleLevelManager.getEncryptionIV();
		this.userID = userId;
		this.derivedTablesListNew = derivedTablesListNew;
		this.clientAuthToken = middleLevelManager.getClientAuthToken();
		this.userAuthToken = middleLevelManager.getUserAuthToken();
		this.clientid=clientId;
		this.scheduleDao=scheduleDao;
		this.modification=modification;
		this.executionId=executionId;
		this.timeZone=timeZone;
		this.clientAppDbJdbcTemplate=clientAppDbJdbcTemplate;
		try {
			if (StringUtils.isNotBlank(mlmServicePrivateKey) && StringUtils.isNotBlank(mlmServiceIv)) {
				aes2 = new AnvizentEncryptionServiceImpl2(mlmServicePrivateKey.trim(), mlmServiceIv.trim());
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			PackageExecution packExecutionBefore = MinidwServiceUtil.getDruidStatusAndComments(executionId,Constants.ExecutionStatus.STARTED, "\n"+ TimeZoneDateHelper.getFormattedDateString() +" Druid execution started.", timeZone);
			packExecutionBefore.setModification(modification);
			scheduleDao.updateDruidStartInfo(packExecutionBefore, clientAppDbJdbcTemplate);
			
			getDruidDataSourceMap();
			updateDruidStatus(Constants.ExecutionStatus.INPROGRESS, "DataSources " + druidDataSourceMap );
		} catch (Throwable e) {
			throw new DruidException(e.getMessage());
		}

	}
	@Override
	public void run() {
		try {
			
			druidProcess();
			updateDruidStatus(Constants.ExecutionStatus.COMPLETED, "Druid execution completed.");
		} catch (Throwable e) {
			updateDruidStatus(Constants.ExecutionStatus.FAILED, e.getMessage());
		}
	}
	public String deleteDruidDataSourceOldVersion() throws DruidException {
      StringBuilder deleteStatus= new StringBuilder();
		try {
            int count = 1;
			for (Entry<String, Long> entry : oldversionMap.entrySet()) {
				/*if (StringUtils.isNotBlank(mlmUpgradeEndPoint)) {
					deleteStatus.append("\n upgrate Druid Data Source Old Version execution  started for "+entry.getKey() +" with   version id: "+entry.getValue()+" and "+ count +" of "+ oldversionMap.size());
					upgradeVersionInStagingDB(druidDataSourceMap.get(entry.getKey()));
					deleteStatus.append("\n upgrate Druid Data Source Old Version execution  completed for "+entry.getKey() +" with version id: "+entry.getValue()+" and "+ count +" of "+ oldversionMap.size());
					
				}*/
			   if(entry.getValue() != 0){
				deleteStatus.append("\n delete Druid Data Source Old Version execution  started for "+entry.getKey() +" with   version id: "+entry.getValue()+" and "+ count +" of "+ oldversionMap.size());
				callMLMDeleteEndPoint(entry.getKey(), entry.getValue());
				deleteStatus.append("\n delete Druid Data Source Old Version execution  completed for "+entry.getKey() +" with version id: "+entry.getValue()+" and "+ count +" of "+ oldversionMap.size());
				}else{
					deleteStatus.append("\n delete Druid Data Source Old Version execution failed for "+entry.getKey() +" with version id: "+entry.getValue()+" and "+ count +" of "+ oldversionMap.size());
				}
			   count++;
			}

		} catch (Exception e) {
			throw new DruidException("Unable to delete druid old version", e);
		}
      return deleteStatus.toString();
	}

	public String deleteDruidDataSourceNewVersion() throws DruidException {
		StringBuilder deleteStatus= new StringBuilder();
		try {
			int count = 1;
			for (Entry<String, Long> entry : newVersionMap.entrySet()) {
				deleteStatus.append("\n upgrate Druid Data Source new Version execution  started for "+entry.getKey() +" with   version id: "+entry.getValue()+" and "+ count +" of "+ oldversionMap.size());
				upgradeVersionInStagingDB(druidDataSourceMap.get(entry.getKey()));
				deleteStatus.append("\n upgrate Druid Data Source new Version execution  completed for "+entry.getKey() +" with version id: "+entry.getValue()+" and "+ count +" of "+ oldversionMap.size());
				
				if(entry.getValue() != 0){
				deleteStatus.append("\n delete Druid Data Source new Version execution  started for "+entry.getKey() +" with version id: "+entry.getValue()+" and "+ count +" of "+ newVersionMap.size());
				callMLMDeleteEndPoint(entry.getKey(), entry.getValue());
				deleteStatus.append("\n delete Druid Data Source new Version execution  completed for "+entry.getKey() +" with with versionid: "+entry.getValue()+" and "+ count +" of "+ newVersionMap.size());
				}else{
				deleteStatus.append("\n delete Druid Data Source new Version execution failed for "+entry.getKey() +" with version id: "+entry.getValue()+" and "+ count +" of "+ oldversionMap.size());
				}
			   count++;
			}

		} catch (Exception e) {
			throw new DruidException("Unable to delete druid new version", e);
		}
		 return deleteStatus.toString();
	}

	public void upgradeVersionInStagingDB(Integer druidDataSourceID) throws Exception {
		String endpoint = mlmContextPath + mlmUpgradeEndPoint;
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new CustomRestTemplateResponseErrorHandler());
		HttpEntity<?> entity = new HttpEntity<Object>(getHeaderMap());
		restTemplate.exchange(endpoint, HttpMethod.PUT, entity, String.class, userID,clientid,String.valueOf(druidDataSourceID));
	}

	@SuppressWarnings("unchecked")
	public void druidProcess() throws DruidException {
		 StringBuilder executionStatus = new StringBuilder();
		try {
            int rowCount = 0;
            executionStatus.append(" Total tables list " + derivedTablesListNew);
			updateDruidStatus(Constants.ExecutionStatus.INPROGRESS, executionStatus );
			for (Entry<String, Integer> entry : druidDataSourceMap.entrySet()) {
				rowCount++;
				executionStatus.append("Druid execution started for data source name: "+entry.getKey() +" with druid data source id: "+entry.getValue()+" and "+ rowCount +" of "+ druidDataSourceMap.size());
				updateDruidStatus(Constants.ExecutionStatus.INPROGRESS, executionStatus );
				ResponseEntity<String> response = callMLMProcessEndPoint(userID, entry.getValue());
				org.json.simple.JSONObject jobj = (org.json.simple.JSONObject) new JSONParser().parse(response.getBody());
				if (jobj != null && jobj.get("status").toString().equalsIgnoreCase("ok")) {
					org.json.simple.JSONObject druidVersion = (org.json.simple.JSONObject) jobj.get("data");
					Long oldversion = Long.parseLong((druidVersion.get("oldVersion")).toString());
					Long newversion = Long.parseLong((druidVersion.get("newVersion")).toString());
					if (oldversion != null && newversion != null) {
						oldversionMap.put(entry.getKey(), oldversion);
						newVersionMap.put(entry.getKey(), newversion);
					}
					executionStatus.append(druidVersion);
					updateDruidStatus(Constants.ExecutionStatus.INPROGRESS, executionStatus );
					executionStatus.append("Druid execution completed for data source name: "+entry.getKey() +" with druid data source id: "+entry.getValue()+" and "+ rowCount +" of data source total size "+ druidDataSourceMap.size()+"\n");
					updateDruidStatus(Constants.ExecutionStatus.INPROGRESS, executionStatus );
				}else if(jobj != null && jobj.get("status").toString().equalsIgnoreCase("BAD_REQUEST")){
				 List<Map<String,Object>> listOfMap = (List<Map<String, Object>>) jobj.get("messages");
				 Map<String,Object> map = listOfMap.get(0);
				 String message = (String) map.get("details");
				 executionStatus.append("Druid execution failed for data source name: "+entry.getKey() +" with druid data source id: "+entry.getValue()+" and "+ rowCount +" of data source total size "+ druidDataSourceMap.size()+"\n");
				 executionStatus.append(message);
				 updateDruidStatus(Constants.ExecutionStatus.INPROGRESS, executionStatus );
				 throw new DruidException(executionStatus.toString());
				}
				else {
					executionStatus.append("Druid execution failed for data source name: "+entry.getKey() +" with druid data source id: "+entry.getValue()+" and "+ rowCount +" of data source total size "+ druidDataSourceMap.size()+"\n");
					updateDruidStatus(Constants.ExecutionStatus.INPROGRESS, executionStatus );
					throw new DruidException(executionStatus.toString());
				}
			}
			updateDruidStatus(Constants.ExecutionStatus.INPROGRESS, executionStatus );
		} catch (Exception e) {
			throw new DruidException(executionStatus.toString() +"\n"+" "+e.getMessage());
		}
	}

	/**
	 * 
	 * @param userId
	 * @param packageId
	 * @param druid_datasource_id
	 * @return
	 * @throws Exception
	 */

	private ResponseEntity<String> callMLMProcessEndPoint(String userId, int druid_datasource_mapping_id)
			throws Exception {
		String url = mlmContextPath + mlmWriteEndPoint;
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<?> entity = new HttpEntity<Object>(getHeaderMap());
		restTemplate.setErrorHandler(new CustomRestTemplateResponseErrorHandler());
		return restTemplate.exchange(url, HttpMethod.POST, entity, String.class, userId,clientid, String.valueOf(druid_datasource_mapping_id));
		
	}

	private ResponseEntity<String> callMLMDeleteEndPoint(String dataSource, Long version) throws Exception {
		String endpoint = mlmContextPath + mlmDeleteEndPoint;

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new CustomRestTemplateResponseErrorHandler());
		HttpEntity<?> entity = new HttpEntity<Object>(getHeaderMap());
		return restTemplate.exchange(endpoint, HttpMethod.DELETE, entity, String.class, dataSource, userID,clientid,String.valueOf(version));
	}

	public void getDruidDataSourceMap() throws Exception {
		for (String tableName : derivedTablesListNew) {
			if (StringUtils.isNotBlank(mlmAddToUploadListEndPoint)) {
				Integer druidSourceID = getDruidDataSourceID(tableName);
				druidDataSourceMap.put(tableName, druidSourceID);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public Integer getDruidDataSourceID(String tableName) throws Exception {

		String endpoint = mlmContextPath + mlmAddToUploadListEndPoint;
		HttpEntity<?> entity = new HttpEntity<Object>(getHeaderMap());
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new CustomRestTemplateResponseErrorHandler());
		HashMap<String, Object> map = restTemplateutil.getMap(restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class, tableName, userID,clientid));
		String status = (String) map.get("status");
		if (status.equalsIgnoreCase("ok")) {
			return (Integer) map.get("data");
		}else if(map != null && map.get("status").toString().equalsIgnoreCase("BAD_REQUEST")){
			 List<Map<String,Object>> listOfMap = (List<Map<String, Object>>) map.get("messages");
			 Map<String,Object> detailsMap = listOfMap.get(0);
			 String message = (String) detailsMap.get("details");
			 throw new DruidException("\n Druid execution failed while getting data source of data source id: "+tableName+"\n"+message +"\n");
			}
		return null;
	}

	private MultiValueMap<String, String> getHeaderMap() throws Exception {
		MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
		String encryptedUserId = aes2.encrypt(userID);
		String encryptedClientId = aes2.encrypt(clientid);
		headerMap.add(clientAuthToken, encryptedClientId);
		headerMap.add(userAuthToken, encryptedUserId);
		return headerMap;
	}
	
	
	public void updateDruidStatus(String status, StringBuilder executionStatus) {
		updateDruidStatus(status, executionStatus.toString());
		 /* for clearing the executionStatus*/
		 executionStatus.setLength(0);
	}
	
	public void updateDruidStatus(String status, String executionStatus) {
		PackageExecution packExecutionBefore = MinidwServiceUtil.getDruidStatusAndComments(executionId,status, TimeZoneDateHelper.getFormattedDateString()+ " " + executionStatus.toString(), timeZone);
		 packExecutionBefore.setModification(modification);
		 scheduleDao.updateDruidEndInfo(packExecutionBefore, clientAppDbJdbcTemplate);
	}

}
