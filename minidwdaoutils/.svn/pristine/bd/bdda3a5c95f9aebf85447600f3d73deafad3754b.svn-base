package com.datamodel.anvizent.helper;

import static com.anvizent.minidw.service.utils.MinidwServiceUtil.getExecutionStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.client.data.to.csv.path.converter.ClientDBProcessor;
import com.anvizent.client.data.to.csv.path.converter.exception.QueryParcingException;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.helper.CommonUtils;
import com.anvizent.minidw.service.utils.helper.WebServiceCSVWriter;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.anvizent.minidw.service.utils.processor.WebServiceProcessor;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.CSVConversionException;
import com.datamodel.anvizent.common.exception.FlatFileReadingException;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.AESConverter;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.IncrementalUpdate;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;

public class MiniDwJobUtil {
	
	protected static final Log LOGGER = LogFactory.getLog(MiniDwJobUtil.class);
	
	@Autowired
	WebServiceProcessor webServiceProcessor;
	@Autowired
	CommonProcessor commonProcessor;
	 

	public static DataResponse spPackageUploadExecutor(PackageExecution packageExecution,long executionId,Package userPackage,FileDao fileDao, ScheduleDao scheduleDao,PackageDao packageDao, User user,
			ClientJdbcInstance clientJdbcInstance ,String deploymentType,String timeZone,UserDao userDao, WebServiceDao webServiceDao) {
		
		SchedulerSlave slave = new SchedulerSlave();
		slave.setId(0);
		slave.setName("N/A");
		
		return spPackageUploadExecutor(packageExecution,executionId, userPackage, fileDao, scheduleDao, packageDao, user, clientJdbcInstance, deploymentType, timeZone, userDao, webServiceDao, slave);
		
	}
	
	@SuppressWarnings("unchecked")
	public static DataResponse spPackageUploadExecutor(PackageExecution packageExecutionInfo,long executionId,Package userPackage,FileDao fileDao, ScheduleDao scheduleDao,PackageDao packageDao, User user,
			ClientJdbcInstance clientJdbcInstance, String deploymentType, String timeZone, UserDao userDao,
			WebServiceDao webServiceDao, SchedulerSlave slave) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		Set<String> totalTablesSet = new HashSet<>();
		List<PackageExecutorMappingInfo> packageExecutorMappingInfoList = null;
		PackageExecution packageExecution = null;
		StringBuilder executionMappingInfoComments = new StringBuilder();
		Set<Integer> dlIds = new HashSet<Integer>();
		Map<String, Object> finalresponseMap = new HashMap<String, Object>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Modification modification = null;
		String executionStatus = null;
		try {
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			modification.setModifiedBy(user.getUserName());
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);

			String tempPath = Constants.Temp.getTempFileDir();
			tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
			tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
			String jobFilesPath = tempPath + "JobFiles/";
			CommonUtils.createDir(jobFilesPath);

			String runType = packageExecutionInfo.getRunType();

			packageExecution =  getExecutionStatus(executionId, Constants.ExecutionStatus.STARTED,
					"\n IL & DL source files execution started  at " + slave.getId() + "-" + slave.getName(), timeZone);
			packageExecution.setModification(modification);
			scheduleDao.updatePackageExecutionStatus(packageExecution, clientAppDbJdbcTemplate);

			if (runType.equals("il") || runType.equals("all")) {

				packageExecutorMappingInfoList = packageDao.getPackageExecutorSourceMappingInfoList(executionId,
						clientAppDbJdbcTemplate);
				if (packageExecutorMappingInfoList != null && packageExecutorMappingInfoList.size() > 0) {
					LOGGER.info("Package execution started for id : " + packageExecution.getExecutionId()
							+ " dataResponse " + dataResponse);
					for (PackageExecutorMappingInfo packageExecutorMappingInfo : packageExecutorMappingInfoList) {
						DataResponse runIlAndDlDataResponse =  runIl(user, modification,
								packageExecutorMappingInfo.getPackageExecution().getTimeZone(), jobFilesPath,
								clientJdbcInstance, scheduleDao, packageDao, packageExecutorMappingInfo, deploymentType,
								userPackage, userDao, webServiceDao);
						if (runIlAndDlDataResponse != null && runIlAndDlDataResponse.getMessages() != null) {
							if (runIlAndDlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
								Map<String, Object> responseMap = (Map<String, Object>) runIlAndDlDataResponse
										.getObject();
								totalTablesSet.add((String) (responseMap.get("iLTableName")));
								dlIds.add((Integer) (responseMap.get("dlId")));
							} else {
								throw new Exception(runIlAndDlDataResponse.getMessages().get(0).getText());
							}
						} else {
							throw new Exception("\n Unable to get IL info  with excution Id :" + executionId
									+ " with package Id: " + userPackage.getPackageId());
						}
					}
				} else {
					message.setCode("ERROR");
					message.setText("Unable to get package executor mapping info list with execution Id:" + executionId
							+ " with package Id:" + userPackage.getPackageId());
					executionStatus = Constants.ExecutionStatus.FAILED;
					executionMappingInfoComments
							.append("\n Unable to get package executor mapping info list with execution Id:"
									+ executionId + " with package Id:" + userPackage.getPackageId());
					return dataResponse;
				}
			} else {
				executionStatus = Constants.ExecutionStatus.INPROGRESS;
				executionMappingInfoComments.append("Run only DL selected.");
				packageExecution = MinidwServiceUtil.getExecutionStatus(executionId, executionStatus,
						executionMappingInfoComments.toString(), timeZone);
				packageExecution.setModification(modification);
				scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);

				List<DLInfo> dlInfoList = packageDao.getILConnectionMappingDlInfoByPackageId(
						(int) userPackage.getPackageId(), user.getUserId(), clientAppDbJdbcTemplate);
				for (DLInfo dlInfo : dlInfoList) {
					dlIds.add(dlInfo.getdL_id());
				}

			}

			// dl execution start
			if (runType.equals("dl") || runType.equals("all")) {
				if (dlIds != null && dlIds.size() > 0) {
					executionMappingInfoComments.append("\n DL execution started.");
					for (int dLId : dlIds) {
						try {
							DataResponse runDlDataResponse = runDl(user, modification, timeZone, jobFilesPath,
									clientJdbcInstance, scheduleDao, packageDao, executionId, deploymentType,
									userPackage, dLId);
							if (runDlDataResponse != null && runDlDataResponse.getMessages() != null) {
								if (runDlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
									Map<String, Object> responseMap = (Map<String, Object>) runDlDataResponse
											.getObject();
									totalTablesSet.add((String) (responseMap.get("dlTableName")));
									executionMappingInfoComments
											.append((String) (responseMap.get("executionMappingInfoComments")));
								} else {
									executionMappingInfoComments
											.append(runDlDataResponse.getMessages().get(0).getText());
									message.setCode("ERROR");
									message.setText(runDlDataResponse.getMessages().get(0).getText());
								}
							} else {
								throw new Exception("\n Unable to get DL info with excution Id :" + executionId
										+ " with package Id: " + userPackage.getPackageId());
							}

						} catch (TalendJobNotFoundException | CSVConversionException | InterruptedException
								| IOException | AnvizentCorewsException | ParseException e) {
							message.setCode("ERROR");
							message.setText(MinidwServiceUtil.getErrorMessageString(e));
							executionMappingInfoComments.append(MinidwServiceUtil.getErrorMessageString(e));
						}
					}
					executionMappingInfoComments.append("\n DL execution completed.");
				} else {
					executionMappingInfoComments.append("DL id's list is empty and size is: " + dlIds.size());
					LOGGER.info("DL id's list is empty and size is: " + dlIds.size());
				}
			} else {
				executionMappingInfoComments.append("Run only IL selected " + dlIds.size());
			}
			// update package schedule status.

			ClientData clientData = new ClientData();
			userPackage.setIsScheduled(Boolean.TRUE);
			userPackage.setScheduleStatus(Constants.Status.STATUS_DONE);
			clientData.setUserPackage(userPackage);
			clientData.setModification(modification);
			clientData.setUserId(user.getUserId());
			packageDao.updatePackageScheduleStatus(clientData, clientAppDbJdbcTemplate);

			executionMappingInfoComments.append("\n IL & DL source files execution completed.");
			executionStatus = Constants.ExecutionStatus.COMPLETED;
			message.setCode("SUCCESS");
			message.setText(userPackage.getPackageName() + ": Data Processed Successfully");
			finalresponseMap.put("totalTablesSet", totalTablesSet);
			dataResponse.setObject(finalresponseMap);

		} catch (Throwable ae) {
			LOGGER.error("error while getPackageExecutorSourceMappingInfoList() ", ae);
			message.setCode("ERROR");
			message.setText(executionMappingInfoComments.toString() + MinidwServiceUtil.getErrorMessageString(ae));
			executionStatus = Constants.ExecutionStatus.FAILED;
			executionMappingInfoComments.append(ae.getMessage());
		}
		packageExecution = MinidwServiceUtil.getExecutionStatus(executionId, executionStatus,
				executionMappingInfoComments.toString(), timeZone);
		packageExecution.setModification(modification);
		scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);

		return dataResponse;
	}
	
	public static DataResponse cpPackageUploadExecutor(long executionId,Package userPackage,FileDao fileDao, ScheduleDao scheduleDao,PackageDao packageDao, User user,
			ClientJdbcInstance clientJdbcInstance ,String deploymentType,String timeZone,UserDao userDao) {
		SchedulerSlave slave = new SchedulerSlave();
		slave.setId(0);
		slave.setName("N/A");
		
		return cpPackageUploadExecutor(executionId, userPackage, fileDao, scheduleDao, packageDao, user, clientJdbcInstance, deploymentType, timeZone, userDao, slave);
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public static DataResponse cpPackageUploadExecutor(long executionId,Package userPackage,FileDao fileDao, ScheduleDao scheduleDao,PackageDao packageDao, User user,
			ClientJdbcInstance clientJdbcInstance ,String deploymentType,String timeZone,UserDao userDao,SchedulerSlave slave) { 
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<String> filePaths = null;
		Set<String> totalTablesSet = new HashSet<>();
		Map<String, Object> processedData = null;
		Map<String, Object> results = null;
		PackageExecution packageExecution = null;
		StringBuilder executionMappingInfoComments = new StringBuilder();
		String executionMappingInfoStatus = Constants.ExecutionStatus.ERROR;
		List<PackageExecutorMappingInfo> packageExecutorMappingInfoList = null;
		Map<String,Object> responseMap = new HashMap<String,Object>();
		Modification modification = null;
		int totalRecordsProcessed = 0;
		int totalRecordsFailed = 0;
		int totalDuplicateRecordsFound = 0;
		int totalNoOfRecords = 0;
		Map<String, Object>  havingSameColumsResultsMap = new HashMap<String, Object>();
		try{
			
		    modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			modification.setModifiedBy(user.getUserName());
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);
			
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
		    packageExecutorMappingInfoList = packageDao.getPackageExecutorSourceMappingInfoList(executionId, clientAppDbJdbcTemplate);
			if (packageExecutorMappingInfoList != null && packageExecutorMappingInfoList.size() > 0) {
				
				packageExecution = MinidwServiceUtil.getExecutionStatus(executionId, Constants.ExecutionStatus.STARTED, "\n Execution started for custom package.\n Source files execution started  at " + slave.getId() +"-" + slave.getName() ,timeZone);
				packageExecution.setModification(modification);
				scheduleDao.updatePackageExecutionStatus(packageExecution, clientAppDbJdbcTemplate);
				
				for (PackageExecutorMappingInfo packageExecutorMappingInfo : packageExecutorMappingInfoList) {
					DataResponse runCpDs = runCp(userPackage,fileDao,scheduleDao,packageDao,user,clientJdbcInstance ,  deploymentType,  timeZone,  userDao,  packageExecutorMappingInfo);
					if(runCpDs != null && runCpDs.getMessages() != null){
							if(!runCpDs.getMessages().get(0).getCode().equals("SUCCESS")){  
								executionMappingInfoStatus=Constants.ExecutionStatus.FAILED;
								executionMappingInfoComments.append(runCpDs.getMessages().get(0).getText());
							}else{
								if(userPackage.getFilesHavingSameColumns()){
								Map<String,Object>  recordsProcessedMap =  (HashMap<String,Object>)runCpDs.getObject();
								if(recordsProcessedMap.get("totalRecordsProcessed") != null){
									totalRecordsProcessed += (int)recordsProcessedMap.get("totalRecordsProcessed");
								}
								if(recordsProcessedMap.get("totalDuplicateRecordsFound") != null){
									totalDuplicateRecordsFound += (int)recordsProcessedMap.get("totalDuplicateRecordsFound");
								}
								if(recordsProcessedMap.get("totalRecordsFailed") != null){
									totalRecordsFailed += (int)recordsProcessedMap.get("totalRecordsFailed");
								}
								if(recordsProcessedMap.get("totalNoOfRecords") != null){
									totalNoOfRecords += (int)recordsProcessedMap.get("totalNoOfRecords");
								}
								if(recordsProcessedMap.get("targetTable") != null){
									totalTablesSet.add(recordsProcessedMap.get("targetTable").toString());
								}
								}
							}
						}
					else{
						 throw new Exception("\n Unable to get response at run custom package, with execution Id:"+executionId+" with package Id: "+userPackage.getPackageId()+".");
					  }
					} 
				if(!userPackage.getFilesHavingSameColumns()) {
					DataResponse executeCustomPackageTargetTable = executeCustomPackageTargetTable(clientJdbcInstance,userPackage,user,fileDao,
							  scheduleDao,timeZone,executionId,packageDao);
					if(executeCustomPackageTargetTable != null && executeCustomPackageTargetTable.getMessages() != null){
						if(executeCustomPackageTargetTable.getMessages().get(0).getCode().equals("SUCCESS")){  
							totalTablesSet.add(executeCustomPackageTargetTable.getObject().toString());
						}else{
							executionMappingInfoStatus=Constants.ExecutionStatus.FAILED;
							executionMappingInfoComments.append("\n"+executeCustomPackageTargetTable.getMessages().get(0).getText());
						}
					}
					else{
						throw new Exception("\n Unable to get custom package taget table response, with execution Id:"+executionId+" with package Id: "+userPackage.getPackageId()+".");
					}
				  }else{
						ClientData targetTableClientData = packageDao.getTargetTableInfoByPackage(user.getUserId(),userPackage.getPackageId(), clientAppDbJdbcTemplate);
						if(targetTableClientData != null){
							Table table = targetTableClientData.getUserPackage().getTable();
							table.setIsProcessed(Boolean.TRUE);
							table.setNoOfRecordsProcessed(totalRecordsProcessed);
							table.setNoOfRecordsFailed(totalRecordsFailed);
							table.setDuplicateRecords(totalDuplicateRecordsFound);
							table.setTotalRecords(totalNoOfRecords); 
						    updateTargetTableInfoStatus(table,packageDao,userPackage,user,modification,clientAppDbJdbcTemplate);
						}
					  
				  }
					DataResponse executeCustomPackageDerivedTables = executeCustomPackageDerivedTables(clientJdbcInstance,userPackage,user,fileDao,
							                                          scheduleDao,timeZone,executionId,packageDao);
					if(executeCustomPackageDerivedTables != null && executeCustomPackageDerivedTables.getMessages() != null){
						if(executeCustomPackageDerivedTables.getMessages().get(0).getCode().equals("SUCCESS")){
							Set<String> tableSet = (HashSet<String>) executeCustomPackageDerivedTables.getObject();
							for(String table : tableSet){
								totalTablesSet.add(table);
							}
						} else{
							executionMappingInfoStatus=Constants.ExecutionStatus.FAILED;
							executionMappingInfoComments.append("\n"+executeCustomPackageDerivedTables.getMessages().get(0).getText());
						}
					} else{
						throw new Exception("\n Unable to get custom package derived table response, with execution Id:"+executionId+" with package Id: "+userPackage.getPackageId()+".");
					}
			 
				executionMappingInfoStatus=Constants.ExecutionStatus.COMPLETED;
				message.setCode("SUCCESS");
				message.setText("Custom package executed successfully.");
				responseMap.put("totalTablesSet", totalTablesSet);
				dataResponse.setObject(responseMap);
				
	          }else{
	        		message.setCode("ERROR");
					message.setText("Package executor mapping info list not found for execution id:"+executionId+".");
					executionMappingInfoComments.append("Package executor mapping info list not found for execution id:"+executionId+".");
					executionMappingInfoStatus=Constants.ExecutionStatus.FAILED;
	          }
			}catch(Throwable e){
				message.setCode("ERROR");
				message.setText(MinidwServiceUtil.getErrorMessageString(e));
				executionMappingInfoComments.append(MinidwServiceUtil.getErrorMessageString(e));
				executionMappingInfoStatus=Constants.ExecutionStatus.FAILED;
			}
		packageExecution = MinidwServiceUtil.getExecutionStatus(packageExecution.getExecutionId(), executionMappingInfoStatus,executionMappingInfoComments.toString(),packageExecution.getTimeZone());
		packageExecution.setModification(modification);
		scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);
		
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	public static DataResponse runCustomDataSets(PackageDao packageDao,List<String> totalTablesSet,User user,JdbcTemplate jdbcTemplate,JdbcTemplate clientAppDbJdbcTemplate,FileDao fileDao,Modification modification,String runType) throws SQLException{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<String> ddlsTable = new ArrayList<>();
		dataResponse.setObject(ddlsTable);
		try{
		List<DDLayout> dDLayoutList = packageDao.getDDlayoutList(user.getClientId(),totalTablesSet, user.getUserId(), clientAppDbJdbcTemplate);
		if (dDLayoutList != null && dDLayoutList.size() > 0) {
			for (DDLayout ddlayout : dDLayoutList) {
				boolean tableExist = fileDao.isTableExists(user.getUserId(), null, ddlayout.getTableName(),jdbcTemplate);
				if (tableExist) {
					int count = 0;
					String errorMessage = "";
					try {
						count = CommonUtils.runDDlayoutTable(ddlayout,jdbcTemplate);
						ddlsTable.add(ddlayout.getTableName());
					} catch (BadSqlGrammarException e) {
						errorMessage = MinidwServiceUtil.getErrorMessageString(e);
					}
					ddlayout.setRunType(runType);
					ddlayout.setModification(modification);
					ddlayout.setInsertedCount(count);
					ddlayout.setErrorMessage(errorMessage);
					packageDao.updateDDlayoutTableAuditLogs(user.getUserId(), ddlayout, clientAppDbJdbcTemplate);
				}
			}
			message.setCode("SUCCESS");
			message.setText("DDlayout list successfully updated.");
		  }else{
			  if (dDLayoutList == null) {
				  dDLayoutList = new ArrayList<>();
			  }
			  LOGGER.error("DDL's list is empty and size is :"+dDLayoutList.size());
			  message.setCode("ERROR");
			  message.setText("DDL's list is empty and size is :"+dDLayoutList.size());
		  }
		}catch(Throwable e){
			message.setCode("ERROR");
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	public static void runAllSources(long executionId ,Package userPackage,FileDao fileDao, ScheduleDao scheduleDao,PackageDao packageDao, User user,
			ClientJdbcInstance clientJdbcInstance ,String deploymentType,String timeZone,WebServiceDao webServiceDao,S3BucketInfo s3BucketInfo,
			JdbcTemplate commonJdbcTemplate,PackageExecution packageExecution) throws PackageExecutionException{
		 
		int limit = -1;
		SourceFileInfo sourceFileInfo = null;
		PackageExecution packExecution=null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int source=0;
		try {

			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			
			List<ClientData> mappingInfoList = packageDao.getILConnectionMappingInfoByPackage(user.getUserId(),
					user.getClientId(), userPackage.getPackageId(), userPackage.getIsStandard(), clientAppDbJdbcTemplate,true);
			FileSettings fileSettings = packageDao.getFileSettingsInfo(user.getClientId(), clientAppDbJdbcTemplate);

			
			if(mappingInfoList != null && mappingInfoList.size() > 0){
				String noOfSourcesString = " No Of Sources: " + mappingInfoList.size() +"." +"\n ";
				for (ClientData mappingInfo : mappingInfoList) {
				source++;
				
				StringBuilder sourceTypes = new StringBuilder(noOfSourcesString+" "+TimeZoneDateHelper.getFormattedDateString()+" Source No "+ source +" \n Source Type: ");
				 noOfSourcesString = " ";
				 if ( !mappingInfo.getIlConnectionMapping().getIsFlatFile() && !mappingInfo.getIlConnectionMapping().getIsWebservice() ) {
					 sourceTypes.append("Database");
					 
				 } else if (mappingInfo.getIlConnectionMapping().getIsFlatFile()) {
					 sourceTypes.append("Flat file");
					 
				 } else if( mappingInfo.getIlConnectionMapping().getIsWebservice() ) {
					 sourceTypes.append("Webservices");
				 } else {
					 sourceTypes.append("unknown");
				 }
				 if ( mappingInfo.getUserPackage().getIsStandard() ) {
					 sourceTypes.append("\n IL Id: ").append(mappingInfo.getIlConnectionMapping().getiLId());
					 sourceTypes.append(" DL Id: ").append(mappingInfo.getIlConnectionMapping().getdLId());
				 }
				 
				 PackageExecution ppackExecution = MinidwServiceUtil.getUploadStatus(executionId,Constants.ExecutionStatus.INPROGRESS, sourceTypes.toString()+".",timeZone);
				 ppackExecution.setModification(modification);
				 scheduleDao.updatePackageExecutionUploadInfo(ppackExecution, clientAppDbJdbcTemplate);
				
				ILConnectionMapping ilConnectionMapping =   MinidwServiceUtil.getILConnectionMapping(mappingInfo);
				
				if (!ilConnectionMapping.getIsFlatFile() && !ilConnectionMapping.getIsWebservice()) {
					// data base start here
					if (ilConnectionMapping.getiLConnection() != null) {
						if (ilConnectionMapping.isMultipartEnabled()) {
							limit = fileSettings.getNoOfRecordsPerFile();
						}

						fileUploadByClientApp(user.getUserId(), userPackage.getPackageId(), ilConnectionMapping, limit,
								clientJdbcInstance, fileSettings, user, packageDao, timeZone, deploymentType,
								executionId, scheduleDao, s3BucketInfo);
						// data base end here
					}else {
						throw new PackageExecutionException("Connection details not found for the source no:"+source+" for connection mapping id:"+mappingInfo.getIlConnectionMapping().getConnectionMappingId()+".");
					 }
				} else if (mappingInfo.getIlConnectionMapping().getIsFlatFile()) {
					try{
					 //flat file start here
					sourceFileInfo = new SourceFileInfo();
					sourceFileInfo.setS3BucketInfo(s3BucketInfo);
					sourceFileInfo.setStorageType(mappingInfo.getIlConnectionMapping().getStorageType());
					sourceFileInfo.setFilePath(mappingInfo.getIlConnectionMapping().getFilePath());
					sourceFileInfo.setIlConnectionMappingId(mappingInfo.getIlConnectionMapping().getConnectionMappingId());
					
					sourceFileInfo.setModification(modification);
					sourceFileInfo.setSourceFileId(mappingInfo.getIlConnectionMapping().getSourceFileInfoId());
					Integer ilId = ilConnectionMapping.getiLId();

					packExecution = MinidwServiceUtil.getUploadStatus(executionId,
							Constants.ExecutionStatus.INPROGRESS, "\n\n Flat file source \n IL Id:" + ilId
									+ " \n S3/Local file path is:" + mappingInfo.getIlConnectionMapping().getFilePath()+".",
							timeZone);
					packExecution.setModification(modification);
					scheduleDao.updatePackageExecutionUploadInfo(packExecution, clientAppDbJdbcTemplate);

					LOGGER.info("IL source files upload completed.Package id : " + executionId);

					sourceFileInfo.setPackageExecution(MinidwServiceUtil.getUploadStatus(executionId,
							Constants.ExecutionStatus.COMPLETED, "Uploaded successfully.", timeZone));
					packageDao.updatePackageExecutorSourceMappingInfo(sourceFileInfo, clientAppDbJdbcTemplate);

					// flat file end here
					}catch (AmazonS3Exception e ) {
						 throw new PackageExecutionException(e.getMessage());
						
					}
				}
		}
		} else {
			throw new PackageExecutionException("Sources not found.");
		}
	  }catch(Exception e){
		throw new PackageExecutionException(e);
	  }
	}
	
	private static void fileUploadByClientApp(String encryptedUserId, Integer packageId, ILConnectionMapping iLConnectionMapping, int limit
			,ClientJdbcInstance clientJdbcInstance,FileSettings fileSettings,User user,PackageDao packageDao,String timeZone,String deploymentType,
			long executionId,ScheduleDao scheduleDao,S3BucketInfo s3BucketInfo) throws PackageExecutionException {
 
		File tempFile = null;
		String directoryPath = null;
		PackageExecution packExecution = null;
		StringBuilder uploadStatusInfo = new StringBuilder();
		String incrementalUpdateDate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {

			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String query = iLConnectionMapping.getiLquery();
			
			if (StringUtils.isBlank(query)) {
				throw new PackageExecutionException("Query Should Not be Empty Please Check The Source Queries.");
			}
			
			ClientDBProcessor clientDBProcessor = new ClientDBProcessor(null, null, encryptedUserId, packageId);
			boolean isIncrementalUpdate = iLConnectionMapping.getIsIncrementalUpdate();
			
			int iLId = iLConnectionMapping.getiLId();
			if ( isIncrementalUpdate ) {
				incrementalUpdateDate = packageDao.getILIncrementalUpdateDate(null, iLId, iLConnectionMapping.getiLConnection().getConnectionId() + "", "database", iLConnectionMapping.getConnectionMappingId(),clientJdbcInstance.getClientJdbcTemplate(true));
			} 
			boolean isMultiPartEnabled = fileSettings.getMultiPartEnabled();
			boolean isEncryptionRequired = fileSettings.getFileEncryption();
			/*if (isMultiPartEnabled) {
				limit = fileSettings.getNoOfRecordsPerFile();//isMultiPartEnabled = false;
			}*/
			String s3LogicalDirPath = "datafiles_U" + user.getClientId() + "_P" + packageId + "_M" + iLConnectionMapping.getConnectionMappingId();
			Integer ilId = iLConnectionMapping.getiLId();
			if (ilId != null && ilId != 0) {
				s3LogicalDirPath += "_IL"+ilId + "_" +"DL"+iLConnectionMapping.getdLId() ;
			}
			s3LogicalDirPath += "_" + CommonUtils.generateUniqueIdWithTimestamp();			
			HashMap<String, Object> processedMap = clientDBProcessor.processClientDBData(Constants.Temp.getTempFileDir(), incrementalUpdateDate, iLConnectionMapping, iLConnectionMapping.getiLConnection().getDatabase().getConnector_id(), isIncrementalUpdate, null, isMultiPartEnabled, fileSettings.getNoOfRecordsPerFile());
			
			Path tempDir = (Path) processedMap.get("tempDir");
			String maxDate = (String) processedMap.get("maxDate");
			iLConnectionMapping.setClientId(user.getUserId());
			
			if(iLConnectionMapping.getiLConnection() != null){
				iLConnectionMapping.getiLConnection().setWebApp(true);
			}
			File originalFile = null;
			if (isMultiPartEnabled) {
				originalFile = tempDir.toFile();
				directoryPath = originalFile.getAbsolutePath();
			} else {
				Path tempFileName = (Path) processedMap.get("tempFile");
				tempFile = tempFileName.toFile();
				directoryPath = FilenameUtils.getFullPath(tempFile.getAbsolutePath());
				s3LogicalDirPath += ".csv";
				originalFile = new File(directoryPath + s3LogicalDirPath);
				tempFile.renameTo(originalFile);
			}
			     
			    String startTime = TimeZoneDateHelper.getFormattedDateString();
				LOGGER.info("Database file uploading started " + startTime + " for IL mapping id " + iLConnectionMapping.getConnectionMappingId()+".");
				uploadStatusInfo.append("\n  Database  file uploading started " + startTime + " for IL mapping id: " + iLConnectionMapping.getConnectionMappingId()+".");
				SourceFileInfo sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, originalFile, user.getUserId(), iLConnectionMapping.getPackageId() , user.getUserName(), iLConnectionMapping.getConnectionMappingId(), deploymentType,s3LogicalDirPath ,isMultiPartEnabled,isEncryptionRequired);
				String endTime = TimeZoneDateHelper.getFormattedDateString();
				LOGGER.info("Database file uploading completed for " + endTime + " for IL Mapping Id " + iLConnectionMapping.getConnectionMappingId()+".");
				uploadStatusInfo.append("\n Database file uploading completed for " + endTime + " for IL mapping id: " + iLConnectionMapping.getConnectionMappingId()+".");
				
				// added in /fileUploadByClientsApp
				int rowCount = 0;
				if (limit == -1) {
					//rowCount = getRowCountOfQueryOrSP(iLConnectionMapping, incrementalUpdateDate);
					sourceFileInfo.setRowCount(rowCount);
					sourceFileInfo.setIncrementalUpdate(true);
					sourceFileInfo.setIncrementalDateValue(maxDate);
				}
				sourceFileInfo.setMultiPartFile(isMultiPartEnabled);
				if(s3BucketInfo != null){
					sourceFileInfo.setS3BucketInfo(s3BucketInfo);
				}else{
					S3BucketInfo s3BuckInfo = new S3BucketInfo();
					s3BuckInfo.setId(0);
					sourceFileInfo.setS3BucketInfo(s3BuckInfo);
				}
				sourceFileInfo.setStorageType(iLConnectionMapping.getStorageType());
				sourceFileInfo.setPackageExecution(MinidwServiceUtil.getUploadStatus(executionId, Constants.ExecutionStatus.COMPLETED, "Uploaded successfully.",timeZone));
				sourceFileInfo.setDelimeter(Constants.FileTypeDelimiter.CSV_DELIMITER);
				sourceFileInfo.setFileType(Constants.FileType.CSV);
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(user.getUserName());
				sourceFileInfo.setModification(modification);
				
			    int sourceFileInfoId =  packageDao.updateSourceFileInfo(sourceFileInfo, clientAppDbJdbcTemplate);
			    sourceFileInfo.setSourceFileId(sourceFileInfoId);
						 
			    packExecution = MinidwServiceUtil.getUploadStatus(executionId,Constants.ExecutionStatus.INPROGRESS, uploadStatusInfo.toString()+"\n MultiPart Enabled :"+isMultiPartEnabled+" \n\n In database source \n iLId :"+iLId+" \n s3 file path is:"+sourceFileInfo.getFilePath()+".",timeZone);
			    packExecution.setModification(modification);
			    scheduleDao.updatePackageExecutionUploadInfo(packExecution, clientAppDbJdbcTemplate);
				
			    packageDao.updatePackageExecutorSourceMappingInfo(sourceFileInfo, clientAppDbJdbcTemplate);
					 
				
		}catch (InstantiationException | IllegalAccessException | ClassNotFoundException | QueryParcingException
				| SQLException | IOException | AmazonS3Exception | OnpremFileCopyException | AnvizentCorewsException e) {
			 throw new PackageExecutionException(e.getMessage());
			
		}finally {
			if (!deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
				if (StringUtils.isNotBlank(directoryPath)) {
					try {
						FileUtils.forceDelete(new File(directoryPath));
					} catch (Exception e) {
						LOGGER.info("Unable to delete created csv for " + MinidwServiceUtil.getErrorMessageString(e));
					}
				}
			}
		}
	}
	
	public static int getRowCountOfQueryOrSP(ILConnectionMapping ilConnectionMapping, String incrementalUpdateDate)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
			QueryParcingException {
		int rowCount = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		CallableStatement cstmt = null;
		ResultSet res = null;
		try {
			conn = WebServiceProcessor.connectDatabase(ilConnectionMapping.getiLConnection());
			if (conn != null) {
				String typeOfCommand = ilConnectionMapping.getTypeOfCommand();
				boolean isQuery = ("Query".equals(typeOfCommand));
				if (isQuery) {
					String query = ilConnectionMapping.getiLquery();
					query = replaceDateValue(incrementalUpdateDate, query);

					String sql = "Select count(*) as row_count from (" + query + ") count_table";
					pstmt = conn.prepareStatement(sql);
					res = pstmt.executeQuery();
					if (res != null && res.next()) {
						rowCount = res.getInt("row_count");
					}

				} else {
					String procName = ilConnectionMapping.getiLquery();
					String procParams = ilConnectionMapping.getProcedureParameters();
					List<Map<String, String>> paramList = new ArrayList<>();

					if (StringUtils.isNotEmpty(procParams)) {
						String[] params = procParams.split("\\^");

						if (params.length > 0) {
							for (String param : params) {
								String[] p = param.split("=");
								if (p.length == 2) {
									Map<String, String> paramMap = new HashMap<>();
									paramMap.put("name", p[0]);
									paramMap.put("value", p[1]);
									paramList.add(paramMap);
								}
							}
						}
					}

					int noofparams = paramList.size();
					StringBuilder query = new StringBuilder();
					query.append("{call ").append(procName);

					if (noofparams > 0) {
						query.append("(");
						for (int i = 1; i <= noofparams; i++) {
							query.append("?");
							if (i < noofparams) {
								query.append(", ");
							}
						}
						query.append(")");
					}
					query.append("}");
					cstmt = conn.prepareCall(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

					if (noofparams > 0) {
						int index = 1;
						for (Map<String, String> paramMap : paramList) {
							cstmt.setObject(index++, paramMap.get("value"));
						}
					}
					res = cstmt.executeQuery();
					if (res != null && res.next()) {
						res.last();
						rowCount = res.getRow();
					}
				}
			}
		} finally {
			try {

				if (res != null) {
					res.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

		return rowCount;
	}
	public static String replaceDateValue(String incrementalUpdateDate, String ilQuery) throws QueryParcingException {
		if (incrementalUpdateDate == null) {
			return ilQuery;
		} else {

			int startIndex = ilQuery.indexOf("/*");
			int endIndex = 0;
			boolean dateFound = false;

			while (startIndex != -1) {
				endIndex = ilQuery.indexOf("*/", startIndex);

				if (endIndex == -1) {
					ilQuery += "*/";
					endIndex = ilQuery.length() - 2;
					break;
				}
				if (ilQuery.substring(startIndex, endIndex).contains("{date}")) {
					dateFound = true;
					break;
				}
				startIndex = ilQuery.indexOf("/*", endIndex);
			}

			if (!dateFound) {
				throw new QueryParcingException("Commented incremental query part not found.");
			}

			String commentedQuery = ilQuery.substring(startIndex + 2, endIndex).replace("{date}",
					"'" + incrementalUpdateDate + "'");
			String removeComentsAddedDate = ilQuery.substring(0, startIndex) + commentedQuery
					+ ilQuery.substring(endIndex + 2);

			return removeComentsAddedDate;
		}
	}
	
	public static String getFilePathForWsApi(WebServiceApi webServiceApi, List<LinkedHashMap<String, Object>> finalformattedApiResponse, String csvSavePath) {

		WebServiceCSVWriter writer = new WebServiceCSVWriter();
		if ( StringUtils.isBlank(csvSavePath) ) {
			csvSavePath = Constants.Temp.getTempFileDir();
		}
		String fileDir = createDir(csvSavePath + "fileMappingWithIL/");
		String newfilename = webServiceApi.getApiName().replaceAll("\\s+", "_") + "_" + generateUniqueIdWithTimestamp();
		String filePath = fileDir + newfilename + ".csv";

		if (finalformattedApiResponse != null && filePath != null) {
			try {
				writer.writeAsCSV(finalformattedApiResponse, filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}
	public static String createDir(String dirName) {

		if (StringUtils.isNotBlank(dirName)) {
			if (!new File(dirName).exists()) {
				new File(dirName).mkdirs();
				LOGGER.info("dir created:" + dirName);
			}

		}
		return dirName;
	}
	public static String generateUniqueIdWithTimestamp() {

		String op = "";

		try {
			DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss_SS");
			Date currentTime = new Date();

			op = format.format(currentTime);

		} catch (Exception e) {
			LOGGER.error("Error while creating new unique id", e);
		}

		return op;
	}
	
	
	
	
	public static DataResponse saveOrUpdateIncrementalUpdate(JdbcTemplate clientJdbcTemplate, String schemaName,List<Map<String, Object>> incremtalUpdateList,ScheduleDao scheduleDao,User user){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try{
			if(incremtalUpdateList != null && incremtalUpdateList.size() >0 ){
		for (Map<String, Object> incrementalMap : incremtalUpdateList) {
			
			ILConnectionMapping iLConnectionMapping = new ILConnectionMapping();
			ILConnection iLConnection = new ILConnection();
			Database database = new Database();
			database.setConnector_id((Integer) incrementalMap.get("wsMappingId"));
			iLConnection.setDatabase(database);
			iLConnectionMapping.setiLConnection(iLConnection);
			iLConnectionMapping.setTypeOfCommand(Constants.SourceType.WEBSERVICE);
			iLConnectionMapping.setIncrementalDateValue(incrementalMap.get("currentTime").toString());
			iLConnectionMapping.setiLId((Integer) incrementalMap.get("iLId"));

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			iLConnectionMapping.setModification(modification);

			IncrementalUpdate incrementalUpdate = scheduleDao.getIncrementalUpdate(iLConnectionMapping,clientJdbcTemplate, schemaName);

			if (StringUtils.isNotBlank(incrementalUpdate.getIncDateFromSource())) {
				scheduleDao.updateIncrementalUpdate(iLConnectionMapping,clientJdbcTemplate, schemaName);
			} else {
				scheduleDao.saveIncrementalUpdate(iLConnectionMapping,clientJdbcTemplate, schemaName);
			}
		}
		message.setCode("SUCCESS");
		message.setText("Incremtal update sist successfully updated.");
	  }else{
		  if (incremtalUpdateList == null) {
			  incremtalUpdateList = new ArrayList<>();
		  }
		  LOGGER.error("Incremtal update list is empty and size is :"+incremtalUpdateList.size());
		  message.setCode("ERROR");
		  message.setText("Incremtal update list is empty and size is :"+incremtalUpdateList.size());
	  }
	}catch(Exception e){
		message.setCode("ERROR");
		message.setText(MinidwServiceUtil.getErrorMessageString(e));
	}
	messages.add(message);
	dataResponse.setMessages(messages);
	return dataResponse;
	}
	
	public static DataResponse runIl(User user,Modification modification,String timeZone,String jobFilesPath,ClientJdbcInstance clientJdbcInstance,ScheduleDao scheduleDao,PackageDao packageDao
			,PackageExecutorMappingInfo packageExecutorMappingInfo,String deploymentType,Package userPackage,UserDao userDao, WebServiceDao webServiceDao) throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException, AnvizentCorewsException{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		ILConnectionMapping iLConnectionMapping = null;
		List<String> filePaths = null;
		JobExecutionInfo jobExecutionInfo = null;
		Map<String,Object> responseMap = new HashMap<>();
		PackageExecution packageExecution  = null;
		String executionMappingInfoStatus = Constants.ExecutionStatus.ERROR;
		StringBuilder executionMappingInfoComments = new StringBuilder();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientStagingJdbcTemplate = null;
		ClientJobExecutionParameters clientJobExecutionParameters = null;
		String datbaseTypeName="unknown";
		S3BucketInfo  s3BucketInfo = null;
		try {
			
			   clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			   iLConnectionMapping = packageDao.getIlConnectionMappingByPackageExecutorSourceMappingInfo(packageExecutorMappingInfo.getExecutionId(),packageExecutorMappingInfo.getId(), clientAppDbJdbcTemplate);
			 
			   if(iLConnectionMapping != null){
			 
			    Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
				String clientSchemaName = clientDbDetails.get("clientdb_schema").toString();
				String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();
				String databaseHost = clientDbDetails.get("region_hostname").toString();
				String databasePort = clientDbDetails.get("region_port").toString();
				String databaseUserName = clientDbDetails.get("clientdb_username").toString();
				String databasePassword = clientDbDetails.get("clientdb_password").toString();
			 
				ILInfo iLInfo = packageDao.getILByIdWithJobName(iLConnectionMapping.getiLId(), user.getClientId(), clientAppDbJdbcTemplate);
				if(iLInfo != null){
					String defaultSrcTimezone=null;
					ILConnectionMapping iLConnectionMappingInfobyConnectionFlags=packageDao.getILConnectionMappingInfoForPreview(iLConnectionMapping.getConnectionMappingId(), iLConnectionMapping.getPackageId(), user.getUserId() , clientAppDbJdbcTemplate);
					
					if (iLConnectionMappingInfobyConnectionFlags == null ) {
						message.setCode("ERROR");
						message.setText("Mapping info not found");
						return dataResponse;
					}
					
						if(iLConnectionMappingInfobyConnectionFlags.getIsWebservice() != null && iLConnectionMappingInfobyConnectionFlags.getIsWebservice()){
							WebServiceConnectionMaster webServiceConnectionMaster =webServiceDao.getWebServiceConnectionDetails(Long.valueOf(iLConnectionMappingInfobyConnectionFlags.getWsConId()), user.getClientId() , clientAppDbJdbcTemplate);
							defaultSrcTimezone = webServiceConnectionMaster.getWebServiceTemplateMaster().getTimeZone();
							datbaseTypeName = "W" + webServiceConnectionMaster.getId();
						}else{
							if(iLConnectionMappingInfobyConnectionFlags.getIsFlatFile()){
								datbaseTypeName = "F";
							}
							else{
								ILConnection iLConnection = packageDao.getILConnectionById(iLConnectionMappingInfobyConnectionFlags.getiLConnection().getConnectionId(), user.getUserId() , clientAppDbJdbcTemplate);
								defaultSrcTimezone = iLConnection.getTimeZone();
								datbaseTypeName = "D"+ iLConnectionMappingInfobyConnectionFlags.getiLConnection().getConnectionId();
							}
						}
						
						datbaseTypeName =  StringUtils.isNotBlank(iLConnectionMapping.getIlSourceName()) ? iLConnectionMapping.getIlSourceName() : datbaseTypeName;

				packageExecution =  MinidwServiceUtil.packageExecutionMappingInfo(Constants.ExecutionStatus.STARTED, "IL execution started.",packageExecutorMappingInfo.getId(),modification,timeZone);
				packageExecution.setModification(modification);
				scheduleDao.updatePackageExecutionMappingInfo(packageExecution, clientAppDbJdbcTemplate);
				
				   if(deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)){
					   s3BucketInfo = new S3BucketInfo();
					   s3BucketInfo.setId(0);
					   s3BucketInfo.setClientId(Integer.valueOf(user.getClientId()));
				   }else{
					   s3BucketInfo = userDao.getS3BucketInfoById(user.getClientId(),iLConnectionMapping.getS3BucketId(), null);
				   }
				   
				   
				   if(iLInfo.getJobExecutionType().equals('E')){
					  /* 
					   EltJobTagInfo eltJobTags = userDao.getEltJobTagInfoById(iLInfo.getJobTagId(),null);
					   EltLoadParameters loadParamsInfo = userDao.fetchLoadParametersById(iLInfo.getLoadParameterId(), null);
					   List<EltJobInfo> eltjobList = eltJobTags.getJobsList();
					   for(EltJobInfo eltJobInfo : eltjobList){
						   eltJobInfo.getDerivedComponent();
						   eltJobInfo.getConfigProp().getTagId();
						   eltJobInfo.getValuesProp().getTagId();
						   eltJobInfo.getStatsProp().getTagId();
						   EltMasterConfiguration eltmaster = userDao.getDefaultEltMasterConfiguration(null);
						   
						   List<Object> eltMastersParamsList = new ArrayList<>();
						   eltMastersParamsList.add(eltmaster.getSparkJobPath());
						   eltMastersParamsList.add(ELTPathConstants.class);
						   eltMastersParamsList.add(eltmaster.getEltClassPath());
						   eltMastersParamsList.add(ELTPathConstants.MASTER);
						   eltMastersParamsList.add(eltmaster.getMaster());
						   if(eltmaster.getMaster().equals("yarn")){
							   eltMastersParamsList.add(ELTPathConstants.DEPLOYMODE);
							   eltMastersParamsList.add(eltmaster.getDeployMode());   
						   }
						  
						   eltMastersParamsList.add(ELTPathConstants.NUMBEROFEXECUTERS);
						   eltMastersParamsList.add(loadParamsInfo.getNoOfExecutors());
						   eltMastersParamsList.add(ELTPathConstants.EXECUTORMEMORY);
						   eltMastersParamsList.add(loadParamsInfo.getExecutorMemory());
						   eltMastersParamsList.add(ELTPathConstants.TOTALEXECUTERSCORES);
						   eltMastersParamsList.add(loadParamsInfo.getExecutorCores());
						   eltMastersParamsList.add("\"/root/AnvizentELTCoreLib/trunk/lib/elt.core.spark-0.9.0.jar\"");
						   eltMastersParamsList.add(ELTPathConstants.SPARKMASTER);
						   eltMastersParamsList.add(eltmaster.getSparkMaster());
						   eltMastersParamsList.add(ELTPathConstants.APPNAME);
						   eltMastersParamsList.add(iLInfo.getiL_name());
						   
					   }
					   
					   */
				   }else{
					   if(s3BucketInfo != null){
						   filePaths = MinidwServiceUtil.downloadFilesFromS3(iLConnectionMapping.getFilePath(), user.getUserId(),deploymentType, iLConnectionMapping.isMultipartEnabled(),s3BucketInfo,iLConnectionMapping.isEncryptionRequired());
						   if(filePaths != null){
							Map<String, String> ilContextParams = packageDao.getIlContextParams(iLConnectionMapping.getiLId(), clientAppDbJdbcTemplate);
							clientJobExecutionParameters = packageDao.getClientJobExecutionParams(clientAppDbJdbcTemplate);
							
							if(clientJobExecutionParameters == null){
								clientJobExecutionParameters= new ClientJobExecutionParameters();
								clientJobExecutionParameters.setSourceTimeZone("UTC");
								clientJobExecutionParameters.setDestTimeZone("UTC");
								clientJobExecutionParameters.setInterval(12);
								clientJobExecutionParameters.setCaseSensitive(true);
								clientJobExecutionParameters.setNullReplaceValues("unknown");
							}
							if(StringUtils.isNotBlank(defaultSrcTimezone)){
								clientJobExecutionParameters.setSourceTimeZone(defaultSrcTimezone);
							}
							if(userPackage.getTrailingMonths() != 0){
								clientJobExecutionParameters.setInterval(userPackage.getTrailingMonths());
							}
							
							
							Map<String, String> contextParams = MinidwServiceUtil.getContextParams(ilContextParams,
									databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword,
									databaseHost, databasePort, clientSchemaName, databaseUserName, databasePassword,
									databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword,
									datbaseTypeName, iLInfo, user.getUserId(), userPackage.getPackageId(), jobFilesPath,
									packageExecutorMappingInfo.getExecutionId(),clientJobExecutionParameters,iLConnectionMapping.getIsIncrementalUpdate());
							if (contextParams != null) {
								executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(TimeZoneDateHelper.getFormattedDateString()).append("\t IL job started...").append(Constants.Config.NEW_LINE).append("id : " + iLInfo.getiL_id() + "; IL Name :" + iLInfo.getiL_name());
								int ilSourceIterationCount = 1;
								int ilSourceMultiPartCount = filePaths.size();
								LOGGER.info("IL job started...id : " + iLInfo.getiL_id() + "; IL Name :" + iLInfo.getiL_name()+ "; IL job Class :" + iLInfo.getJobName());
								executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(TimeZoneDateHelper.getFormattedDateString()).append("\t Execution started for the partfiles ").append(ilSourceMultiPartCount);
								executionMappingInfoComments.append("Context parameters \n").append(TimeZoneDateHelper.getFormattedDateString()).append("\t ").append(contextParams);
								
								for (String filePath : filePaths) {
									executionMappingInfoComments.append("\nFILE_SRC="+filePath);
									String statusString = executionMappingInfoComments.toString();
									packageExecution = MinidwServiceUtil.getExecutionStatus(packageExecutorMappingInfo.getExecutionId(), Constants.ExecutionStatus.INPROGRESS, statusString,timeZone);
									packageExecution.setModification(modification);
									scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);
									executionMappingInfoComments = new StringBuilder();
									jobExecutionInfo = MinidwServiceUtil.runILEtlJob(contextParams, iLInfo, user.getUserId(),userPackage.getPackageId(), filePath, modification, jobFilesPath, user);
									scheduleDao.updateJobExecutionDetails(jobExecutionInfo, clientAppDbJdbcTemplate);
									executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(jobExecutionInfo.getExecutionMessages());
									executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(TimeZoneDateHelper.getFormattedDateString()).append("\t Execution completed for the partfile ").append(ilSourceIterationCount).append(" of ").append(ilSourceMultiPartCount);
									ilSourceIterationCount++;
								}
								// incremental update
								if(iLConnectionMapping.getIsIncrementalUpdate() && StringUtils.isNotBlank(iLConnectionMapping.getIncrementalDateValue())){
									clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
									scheduleDao.saveOrUpdateIncrementalUpdate(iLConnectionMapping.getIncrementalDateValue(),iLConnectionMapping.getConnectionMappingId(),clientAppDbJdbcTemplate,clientStagingJdbcTemplate);
								}

								String statusString = executionMappingInfoComments.toString();
								packageExecution = MinidwServiceUtil.getExecutionStatus(packageExecutorMappingInfo.getExecutionId(), Constants.ExecutionStatus.INPROGRESS, statusString,timeZone);
								packageExecution.setModification(modification);
								scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);
								
								LOGGER.info("IL job completed...id : " + iLInfo.getiL_id() + "; IL Name :" + iLInfo.getiL_name());
								executionMappingInfoStatus = Constants.ExecutionStatus.COMPLETED;
							
							packageExecution =  MinidwServiceUtil.packageExecutionMappingInfo(executionMappingInfoStatus, "Il execution completed.",packageExecutorMappingInfo.getId(),modification,timeZone);
							packageExecution.setModification(modification);
							scheduleDao.updatePackageExecutionMappingInfoStatus(packageExecution, clientAppDbJdbcTemplate);
							
							message.setCode("SUCCESS");
							message.setText("Il executed successfully.");
							responseMap.put("dlId",iLConnectionMapping.getdLId());
							responseMap.put("iLTableName",iLInfo.getiL_table_name());
							dataResponse.setObject(responseMap);
							}else{
								message.setCode("ERROR");
								message.setText("Client job execution parameters not found");
							}
						} else{
							message.setCode("ERROR");
							message.setText("Files not found in s3 for buket id: "+iLConnectionMapping.getS3BucketId());
						}
						}else{
							message.setCode("ERROR");
							message.setText("S3 bucket info not found for buket id: "+iLConnectionMapping.getS3BucketId());
						}
				   }
				  
				}else{
					message.setCode("ERROR");
					message.setText("IL info not found for IL id:"+iLConnectionMapping.getiLId());
				}
		     } else {
				message.setCode("ERROR");
				message.setText("Mapping info not found for execution id:"+packageExecutorMappingInfo.getExecutionId() +" with mapping id:"+packageExecutorMappingInfo.getId());
			 
		      }
		   }catch (Throwable e) {
				message.setCode("ERROR");
				message.setText(executionMappingInfoComments.toString()+MinidwServiceUtil.getErrorMessageString(e));
			 
			}  
		
		return dataResponse;
	   }
	public static DataResponse runDl(User user,Modification modification,String timeZone,String jobFilesPath,ClientJdbcInstance clientJdbcInstance,ScheduleDao scheduleDao,PackageDao packageDao
			,long executionId,String deploymentType,Package userPackage,Integer dLId) throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException, AnvizentCorewsException, ParseException{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		
		JobExecutionInfo jobExecutionInfo = null;
		Map<String,Object> responseMap = new HashMap<>();
		StringBuilder executionMappingInfoComments = new StringBuilder();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String startDate=null;
		String endDate=null;
		ClientJobExecutionParameters clientJobExecutionParameters = null;
		boolean loadType = false;
		try {
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			    Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
				String clientSchemaName = clientDbDetails.get("clientdb_schema").toString();
				String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();
				String databaseHost = clientDbDetails.get("region_hostname").toString();
				String databasePort = clientDbDetails.get("region_port").toString();
				String databaseUserName = clientDbDetails.get("clientdb_username").toString();
				String databasePassword = clientDbDetails.get("clientdb_password").toString();
			 
			clientJobExecutionParameters =	packageDao.getClientJobExecutionParams(clientAppDbJdbcTemplate);

			if(clientJobExecutionParameters == null){
				clientJobExecutionParameters= new ClientJobExecutionParameters();
				clientJobExecutionParameters.setSourceTimeZone("UTC");
				clientJobExecutionParameters.setDestTimeZone("UTC");
				clientJobExecutionParameters.setInterval(12);
				clientJobExecutionParameters.setCaseSensitive(true);
				clientJobExecutionParameters.setNullReplaceValues("unknown");
			}
			if(userPackage.getTrailingMonths() != 0){
				clientJobExecutionParameters.setInterval(userPackage.getTrailingMonths());
			}
		    loadType = packageDao.getDlLoadType(executionId,clientAppDbJdbcTemplate);
			Map<String, String> dlParamsVals = MinidwServiceUtil.getDlParamsVals(databaseHost, databasePort,
					clientStagingSchema, databaseUserName, databasePassword, databaseHost, databasePort,
					clientSchemaName, databaseUserName, databasePassword, databaseHost, databasePort,
					clientStagingSchema, databaseUserName, databasePassword, user.getUserId(), userPackage, jobFilesPath,
					executionId,clientJobExecutionParameters,loadType);
			if(dlParamsVals != null){
			// run DL Jobs
			DLInfo dLInfo = packageDao.getDLByIdWithJobName(dLId, user.getClientId(), clientAppDbJdbcTemplate);
			if(dLInfo != null){
			Map<String, String> dlContextParams = packageDao.getDlContextParams(dLInfo.getdL_id(), clientAppDbJdbcTemplate);
			if(dlContextParams != null){
				
			startDate = TimeZoneDateHelper.getFormattedDateString();
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append("DL job started...").append("id : " + dLInfo.getdL_id() + "; DL name :" + dLInfo.getdL_name()+ "; DL job name :" + dLInfo.getJobName()).append(" date and time: ").append(startDate);
			jobExecutionInfo = MinidwServiceUtil.runDlEtlJob(dlContextParams, dlParamsVals, dLInfo, user.getUserId(),userPackage, modification, jobFilesPath, user);
			endDate = TimeZoneDateHelper.getFormattedDateString();
			
			updatePackageExecutionTargetTableInfo( dLInfo.getdL_table_name(),startDate,endDate,jobExecutionInfo,packageDao,executionId,modification,clientAppDbJdbcTemplate);
			scheduleDao.updateJobExecutionDetails(jobExecutionInfo, clientAppDbJdbcTemplate);
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append("DL job completed...").append("id : " + dLInfo.getdL_id() + "; DL Name :" + dLInfo.getdL_name()).append(" date and time: ").append(startDate);;
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(jobExecutionInfo.getExecutionMessages());
			
			List<DLInfo> anlyDlList = packageDao.getAnalyticalDLs(dLId, clientAppDbJdbcTemplate);
			for (DLInfo anly_DL : anlyDlList) {
				
				startDate = TimeZoneDateHelper.getFormattedDateString();
				executionMappingInfoComments.append(Constants.Config.NEW_LINE).append("Started date and time: ").append(startDate).append("\t Analytical DL job started...").append(Constants.Config.NEW_LINE).append("id : " + anly_DL.getdL_id() + "; DL name :" + anly_DL.getdL_name());
				jobExecutionInfo = MinidwServiceUtil.runAnalyticalDlEtlJob(dlContextParams, anly_DL, user.getUserId(),userPackage, modification, jobFilesPath, user);
				endDate = TimeZoneDateHelper.getFormattedDateString();
				updatePackageExecutionTargetTableInfo( anly_DL.getdL_table_name(),startDate,endDate,jobExecutionInfo,packageDao,executionId,modification,clientAppDbJdbcTemplate);
				
				scheduleDao.updateJobExecutionDetails(jobExecutionInfo, clientAppDbJdbcTemplate);
				executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(jobExecutionInfo.getExecutionMessages());
				executionMappingInfoComments.append(Constants.Config.NEW_LINE).append("Completed and time: ").append(endDate).append("\t Analytical DL job completed...").append(Constants.Config.NEW_LINE).append("id : " + anly_DL.getdL_id() + "; DL name :" + anly_DL.getdL_name());
			}
			message.setCode("SUCCESS");
			message.setText("Dl executed successfully.");
			responseMap.put("dlTableName", dLInfo.getdL_table_name());
			responseMap.put("executionMappingInfoComments", executionMappingInfoComments.toString());
			dataResponse.setObject(responseMap);
			
			}
			else{
				message.setCode("ERROR");
				message.setText("Client job execution parameters not found");
			}
			
		 }else{
			 message.setCode("ERROR");
			 message.setText("Dl info not found for dl id: "+dLId);
		 }
		}else{
		 message.setCode("ERROR");
		 message.setText("DL param values not found for dl id:"+dLId);
		}
		} catch (Throwable e) {
			message.setCode("ERROR");
			message.setText(executionMappingInfoComments.toString()+MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	public static DataResponse runCp(Package userPackage,FileDao fileDao, ScheduleDao scheduleDao,PackageDao packageDao, User user,
			ClientJdbcInstance clientJdbcInstance ,String deploymentType,String timeZone,UserDao userDao,PackageExecutorMappingInfo packageExecutorMappingInfo) { 
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		
		ILConnectionMapping iLConnectionMapping = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientStagingJdbcTemplate=null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<String> filePaths = null;
		Map<String, Object> processedData = null;
		Map<String, Object> results = null;
		PackageExecution packageExecution = null;
		StringBuilder executionMappingInfoComments = new StringBuilder();
		String executionMappingInfoStatus = Constants.ExecutionStatus.ERROR;
		Map<String,Object> responseMap = new HashMap<String,Object>();
		Integer successRecordsOfFile = 0;
		Integer failedRecordsOfFile = 0;
		Integer duplicateRecordsOfFile = 0;
		Integer totalRecordsOfFile = 0;
		String startDate = null;
		String endDate = null;
		try{
			
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			modification.setModifiedBy(user.getUserName());
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);
			
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			String clientSchemaName = clientDbDetails.get("clientdb_schema").toString();
			String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();
			
					iLConnectionMapping = packageDao.getIlConnectionMappingByPackageExecutorSourceMappingInfo(packageExecutorMappingInfo.getExecutionId(),packageExecutorMappingInfo.getId(), clientAppDbJdbcTemplate);
					S3BucketInfo s3BucketInfo = null;
					if(deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)){
						   s3BucketInfo = new S3BucketInfo();
						   s3BucketInfo.setId(0);
						   s3BucketInfo.setClientId(Integer.valueOf(user.getClientId()));
					   }else{
						   s3BucketInfo = userDao.getS3BucketInfoById(user.getClientId(),iLConnectionMapping.getS3BucketId(), null);
					   }
						if (iLConnectionMapping != null) {
							ClientData targetTableClientData = packageDao.getTargetTableInfoByPackage(user.getUserId(), userPackage.getPackageId(), clientAppDbJdbcTemplate);
							if (targetTableClientData != null) {
								Table table = targetTableClientData.getUserPackage().getTable();
								targetTableClientData.getUserPackage().setPackageName(userPackage.getPackageName());
								boolean isTableExist = false;
			
								isTableExist = packageDao.isTargetTableExist(user.getUserId(), table.getTableName(), clientAppDbJdbcTemplate);
								if (isTableExist)
									isTableExist = MinidwServiceUtil.isTableExists(user.getUserId(), null, table.getTableName(),clientJdbcTemplate);
								if (isTableExist) {
									table.setSchemaName(clientSchemaName);
									List<Column> columns = MinidwServiceUtil.getTableStructure(null, table.getTableName(),0, user.getUserId(), clientJdbcTemplate);
			
									// get original ,alias columns from
									// tbl_target_table_alias_names temp table
									List<String> originaCols = new ArrayList<String>();
									for (Column col : columns) {
										originaCols.add(col.getColumnName());
									}
									table.setOriginalColumnNames(originaCols);
			
									List<Map<String, Object>> targetTableAliasCols = packageDao.targetTableAliasColumns(table.getTableId(), clientAppDbJdbcTemplate);
			
									if (!targetTableAliasCols.isEmpty()) {
										for (Map<String, Object> row : targetTableAliasCols) {
											String originalColumn = (String) row.get("original_columnname");
											String aliasColumn = (String) row.get("alias_columnname");
			
											for (Column col : columns) {
												if (col.getColumnName().equals(aliasColumn))
													col.setColumnName(originalColumn);
												table.setColumns(columns);
											}
										}
									} else {
										table.setColumns(columns);
									}
								    packageExecution =  MinidwServiceUtil.packageExecutionMappingInfo(Constants.ExecutionStatus.STARTED, "Execution started For custom package.",packageExecutorMappingInfo.getId(),modification,timeZone);
									scheduleDao.updatePackageExecutionMappingInfo(packageExecution, clientAppDbJdbcTemplate); 
									
									if (userPackage.getFilesHavingSameColumns()) {
										 //File having same columns with yes option.
										executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Execution started for the File having same columns.\n");
										MinidwServiceUtil.truncateTable(clientSchemaName,targetTableClientData.getUserPackage().getTable().getTableName(),clientJdbcTemplate);
										 
										 filePaths = MinidwServiceUtil.downloadFilesFromS3(iLConnectionMapping.getFilePath(), user.getUserId(),deploymentType, iLConnectionMapping.isMultipartEnabled(),s3BucketInfo,iLConnectionMapping.isEncryptionRequired());
										 if (filePaths != null) {
												int ilSourceIterationCount = 1;
												int ilSourceMultiPartCount = filePaths.size();
												if (StringUtils.isNotBlank(clientSchemaName)) {
													for (String filePath : filePaths) {
														startDate = TimeZoneDateHelper.getFormattedDateString();
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Execution started for the partfiles ").append(ilSourceMultiPartCount).append(" date and time :").append(startDate);
														try {
															processedData = MinidwServiceUtil.processDataFromFileBatch(filePath,iLConnectionMapping.getFileType(),iLConnectionMapping.getDelimeter(), null, targetTableClientData, clientStagingSchema, clientJdbcTemplate);
														} catch (FlatFileReadingException e) {
															throw e;
														}
														if (processedData.get("successRecords") != null) {
														  successRecordsOfFile = (Integer) processedData.get("successRecords");
														}
														if (processedData.get("failedRecords") != null) {
															failedRecordsOfFile = (Integer) processedData.get("failedRecords");
															}
														if (processedData.get("duplicateRecords") != null) {
															duplicateRecordsOfFile = (Integer) processedData.get("duplicateRecords");
															}
														if (processedData.get("totalRecords") != null) {
															totalRecordsOfFile = (Integer) processedData.get("totalRecords");
															}
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Success records Of File:"+successRecordsOfFile);
														responseMap.put("totalRecordsProcessed", successRecordsOfFile);
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Failed records Of File:"+failedRecordsOfFile);
														responseMap.put("failedRecordsOfFile", failedRecordsOfFile);
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Duplicate records Of File:"+duplicateRecordsOfFile);
														responseMap.put("totalDuplicateRecordsFound", duplicateRecordsOfFile);
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Total records Of File:"+totalRecordsOfFile);
														responseMap.put("totalNoOfRecords", totalRecordsOfFile);
														responseMap.put("targetTable", table.getTableName());
														endDate = TimeZoneDateHelper.getFormattedDateString();
														executionMappingInfoComments.append(Constants.Config.NEW_LINE) .append(" Execution completed for the partfile ").append(" date and time :").append(endDate).append(ilSourceIterationCount).append(" of ").append(ilSourceMultiPartCount).append("\n\n");
													    
														ilSourceIterationCount++;
													}
													
													executionMappingInfoStatus = Constants.ExecutionStatus.COMPLETED;
												}
											}else{
												executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" File paths not found and filePaths S3 bucket info  is : " + s3BucketInfo.getBucketName() +"and bucket id is:"+s3BucketInfo.getId());
												throw new Exception("\n File paths not found and filePaths S3 bucket info  is : " + s3BucketInfo.getBucketName() +"and bucket id is:"+s3BucketInfo.getId());
											}
										 
										 executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Execution completed for the file having same columns.\n");
									  
										 JobExecutionInfo jobExecutionInfo = new JobExecutionInfo(); 
										 jobExecutionInfo.setStatusCode(executionMappingInfoStatus.equals("COMPLETED") ? 0 : 1);
										 jobExecutionInfo.setExecutionMessages(executionMappingInfoComments.toString());
										 updatePackageExecutionTargetTableInfo( table.getTableName(),startDate,endDate,jobExecutionInfo,packageDao,packageExecutorMappingInfo.getExecutionId(),modification,clientAppDbJdbcTemplate);
										 
									} else {
			                                //no option
											executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Execution started for no option.\n");
											String tempTableName = fileDao.getTempTableNameByMappingId(iLConnectionMapping.getConnectionMappingId(), clientAppDbJdbcTemplate);
											List<Column> tableColumns = MinidwServiceUtil.getTableStructure(null, tempTableName,0, user.getUserId(), clientStagingJdbcTemplate);
			  
											Table tableInfo = new Table();
											tableInfo.setTableName(tempTableName);
											tableInfo.setColumns(tableColumns);
											tableInfo.setSchemaName(clientStagingSchema);
			
											List<String> originaColumns = new ArrayList<String>();
											for (Column column : tableColumns) {
												originaColumns.add(column.getColumnName());
												tableInfo.setOriginalColumnNames(originaColumns);
											}
			
											ClientData clientInfo = new ClientData();
											clientInfo.setUserId(user.getUserId());
											userPackage.setTable(tableInfo);
											clientInfo.setUserPackage(userPackage);
											
											MinidwServiceUtil.truncateTable(tableInfo.getSchemaName(), tableInfo.getTableName(), clientStagingJdbcTemplate);
											
											filePaths = MinidwServiceUtil.downloadFilesFromS3(iLConnectionMapping.getFilePath(), user.getUserId(),deploymentType, iLConnectionMapping.isMultipartEnabled(),s3BucketInfo,iLConnectionMapping.isEncryptionRequired());
											if(filePaths != null){
												int ilSourceIterationCount = 1;
												int ilSourceMultiPartCount = filePaths.size();
												for (String filePath : filePaths) {
													try {
														results = MinidwServiceUtil.processDataFromFileBatch(filePath, iLConnectionMapping.getFileType(),iLConnectionMapping.getDelimeter(), null, clientInfo,clientStagingSchema, clientStagingJdbcTemplate);
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" ").append(TimeZoneDateHelper.getFormattedDateString()).append(" Execution started for the partfiles ").append(ilSourceMultiPartCount);
														if(results.get("successRecords") != null){
														  successRecordsOfFile = (Integer) results.get("successRecords");
														}
														if(results.get("failedRecords") != null){
															failedRecordsOfFile = (Integer) results.get("failedRecords");
															}
														if(results.get("duplicateRecords") != null){
															duplicateRecordsOfFile = (Integer) results.get("duplicateRecords");
															}
														if(results.get("totalRecords") != null){
															totalRecordsOfFile = (Integer) results.get("totalRecords");
															}
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Success records Of File:"+(Integer) results.get("successRecords"));
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Failed records Of File:"+(Integer) results.get("failedRecords"));
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" duplicate records Of File:"+(Integer) results.get("duplicateRecords"));
														executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" total records Of File:"+(Integer) results.get("totalRecords"));
			     										executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" ").append(TimeZoneDateHelper.getFormattedDateString()).append(" Execution completed for the partfile ").append(ilSourceIterationCount).append(" of ").append(ilSourceMultiPartCount).append("\n\n");
														ilSourceIterationCount++;
														
													} catch (FlatFileReadingException e) {
														throw e;
													}
													
												}
												executionMappingInfoStatus = Constants.ExecutionStatus.INPROGRESS;
												executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Execution completed for no option.\n");
											}else{
												executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" file paths not found and filePaths S3 bucket info  is : " + s3BucketInfo.getBucketName() +"and bucket id is:"+s3BucketInfo.getId());
												throw new Exception("\n File paths not found and filePaths S3 bucket info  is : " + s3BucketInfo.getBucketName() +"and bucket id is:"+s3BucketInfo.getId());
											}
										}
								
									String statusString = executionMappingInfoComments.toString();
									packageExecution = MinidwServiceUtil.getExecutionStatus(packageExecutorMappingInfo.getExecutionId(), executionMappingInfoStatus.toString(), statusString,timeZone);
									packageExecution.setModification(modification);
									scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);
									
									packageExecution =  MinidwServiceUtil.packageExecutionMappingInfo(Constants.ExecutionStatus.COMPLETED, "Execution completed For custom package.",packageExecutorMappingInfo.getId(),modification,timeZone);
									packageExecution.setModification(modification);
									scheduleDao.updatePackageExecutionMappingInfoStatus(packageExecution, clientAppDbJdbcTemplate);
									
									message.setCode("SUCCESS");
									dataResponse.setObject(responseMap);
								  } else {
			
									// if target table does not exist, delete everything
									// related to the the table.
									ClientData clientData = packageDao.getTargetTableInfoByPackage(user.getUserId(),userPackage.getPackageId(), clientAppDbJdbcTemplate);
									userPackage.setTable(clientData.getUserPackage().getTable());
									Table targetTable = clientData.getUserPackage().getTable();
									// get derived target table info
									List<TableDerivative> derivedTables = fileDao.getCustomTargetDerivativeTables(user.getUserId(),userPackage.getPackageId(), targetTable.getTableId(), clientAppDbJdbcTemplate);
									userPackage.setDerivedTables(derivedTables);
									userPackage.setModification(modification);
			
									packageDao.deleteCustomTablesBYPackageId(userPackage, user.getUserId(), clientJdbcTemplate,clientAppDbJdbcTemplate);
									executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(targetTable.getTableName() + ": Target table does not exist. ");
									
									String statusString = executionMappingInfoComments.toString();
									packageExecution = MinidwServiceUtil.getExecutionStatus(packageExecutorMappingInfo.getExecutionId(), Constants.ExecutionStatus.ERROR, statusString,timeZone);
									packageExecution.setModification(modification);
									scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);
									
									// set error message with below reson
									message.setCode("ERROR");
									message.setText(targetTable.getTableName()+": Target table does not exist. Please create a target table for the package and schedule. Package will be not be shown in schedule page without a target table.");
									throw new PackageExecutionException(targetTable.getTableName()+": Target table does not exist. Please create a target table for the package and schedule. Package will be not be shown in schedule page without a target table.");
								  }
			
							}else{
								message.setCode("ERROR");
								message.setText("Please create target tables to proceed");
								throw new PackageExecutionException("Please create target tables to proceed");
								 
							}
						}else{
							message.setCode("ERROR");
							message.setText("Package executor source mapping not found for execution id:"+packageExecutorMappingInfo.getExecutionId());
							throw new PackageExecutionException("Package executor source mapping not found for execution id:"+packageExecutorMappingInfo.getExecutionId());
			           }
			}catch(Throwable e){
				message.setCode("ERROR");
				message.setText(executionMappingInfoComments.toString()+"\n"+MinidwServiceUtil.getErrorMessageString(e));
				throw new PackageExecutionException(executionMappingInfoComments.toString()+"\n"+MinidwServiceUtil.getErrorMessageString(e));
			} finally{
			    com.datamodel.anvizent.helper.CommonUtils.closeDataSource(clientStagingJdbcTemplate);
			    com.datamodel.anvizent.helper.CommonUtils.closeDataSource(clientJdbcTemplate);
			}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	public static DataResponse executeCustomPackageTargetTable(ClientJdbcInstance clientJdbcInstance,Package userPackage,User user,FileDao fileDao,
			ScheduleDao scheduleDao,String timeZone,long executionId,PackageDao packageDao){
		 
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
 
		Map<String, Object> processedData = null;
		StringBuilder executionMappingInfoComments = new StringBuilder();
		String executionMappingInfoStatus=null;
		PackageExecution packageExecution = null;
		Modification modification =null;
		Table table = null;
		Integer successRecordsOfFile = 0;
		Integer failedRecordsOfFile = 0;
		Integer duplicateRecordsOfFile = 0;
		Integer totalRecordsOfFile = 0;
		String startDate = null;
		String endDate = null;
		try{
			
		    modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			modification.setModifiedBy(user.getUserName());
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);
			
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();	
			String clientSchemaName = clientDbDetails.get("clientdb_schema").toString();
			
			ClientData targetTableClientData = packageDao.getTargetTableInfoByPackage(user.getUserId(),userPackage.getPackageId(), clientAppDbJdbcTemplate);
			if (targetTableClientData != null) {
				 table = targetTableClientData.getUserPackage().getTable();
				targetTableClientData.getUserPackage().setPackageName(userPackage.getPackageName());
				boolean isTableExist = false;

				isTableExist = packageDao.isTargetTableExist(user.getUserId(), table.getTableName(),clientAppDbJdbcTemplate);
				if (isTableExist)
					isTableExist = fileDao.isTableExists(user.getUserId(), null, table.getTableName(),clientJdbcTemplate);
				if (isTableExist) {
					table.setSchemaName(clientSchemaName);
					List<Column> columns = packageDao.getTableStructure(null, table.getTableName(),0, user.getUserId(), clientJdbcTemplate);
					// get original ,alias columns from && tbl_target_table_alias_names temp table
					List<String> originaCols = new ArrayList<String>();
					for (Column col : columns) {
						originaCols.add(col.getColumnName());
						table.setOriginalColumnNames(originaCols);
					}
					List<Map<String, Object>> targetTableAliasCols = packageDao.targetTableAliasColumns(table.getTableId(), clientAppDbJdbcTemplate);
					if (!targetTableAliasCols.isEmpty()) {
						for (Map<String, Object> row : targetTableAliasCols) {
							String originalColumn = (String) row.get("original_columnname");
							String aliasColumn = (String) row.get("alias_columnname");

							for (Column col : columns) {
								if (col.getColumnName().equals(aliasColumn))
									col.setColumnName(originalColumn);
								table.setColumns(columns);
							}
						}
					} else {
						table.setColumns(columns);
					}
				}
			MinidwServiceUtil.truncateTable(null, table.getTableName(), clientJdbcTemplate);
			ClientData clientData = new ClientData();
			clientData.setUserId(user.getUserId());
			userPackage.setTable(table);
			clientData.setUserPackage(userPackage);
			
			startDate= TimeZoneDateHelper.getFormattedDateString();
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Execution started for target table  ").append(" date and time: ").append(startDate).append("\n");
			String customTempQuery = fileDao.getCustomTempQuery(clientData, clientAppDbJdbcTemplate);
			List<Table> tablesList = fileDao.getCustomTempTables(user.getUserId(), Integer.toString(userPackage.getPackageId()), clientAppDbJdbcTemplate);
			
			processedData = MinidwServiceUtil.processCustomTargetTableQuery(clientData, clientStagingSchema, clientJdbcTemplate,tablesList,customTempQuery);
			
			if( processedData.get("successRecords") != null){
				successRecordsOfFile = (Integer) processedData.get("successRecords");
			}
			if( processedData.get("failedRecords") != null){
				 failedRecordsOfFile = (Integer) processedData.get("failedRecords");
			}
			if( processedData.get("duplicateRecords") != null){
			 duplicateRecordsOfFile = (Integer) processedData.get("duplicateRecords");
			}
			if( processedData.get("totalRecords") != null){
				 totalRecordsOfFile = (Integer) processedData.get("totalRecords");
			}
			endDate= TimeZoneDateHelper.getFormattedDateString();
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" total Records Processed: "+successRecordsOfFile);
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" total Records Failed: "+failedRecordsOfFile);
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" total Duplicate Records Found: "+duplicateRecordsOfFile);
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" total No Of Records: "+totalRecordsOfFile).append("\n");;
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" Execution completed for target table").append(" date and time: ").append(endDate);
			executionMappingInfoStatus = Constants.ExecutionStatus.COMPLETED;
			
			table.setIsProcessed(Boolean.TRUE);
			table.setNoOfRecordsProcessed(successRecordsOfFile);
			table.setNoOfRecordsFailed(failedRecordsOfFile);
			table.setDuplicateRecords(duplicateRecordsOfFile);
			table.setTotalRecords(totalRecordsOfFile); 
			
			updateTargetTableInfoStatus(table,packageDao,userPackage, user,modification,clientAppDbJdbcTemplate);
			
			message.setCode("SUCCESS");
			dataResponse.setObject(table.getTableName());
		 }else{
				message.setCode("ERROR");
				message.setText("target Table Client Data not found for package id: "+userPackage.getPackageId());
				throw new PackageExecutionException("target Table Client Data not found for package id: "+userPackage.getPackageId());
			}
		}catch(Exception e){
			executionMappingInfoStatus=Constants.ExecutionStatus.FAILED;
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(e.getCause().getMessage());
			message.setCode("ERROR");
			message.setText(e.getCause().getMessage());
		}
		String statusString = executionMappingInfoComments.toString();
		packageExecution = MinidwServiceUtil.getExecutionStatus(executionId, executionMappingInfoStatus.toString(), statusString,timeZone);
		packageExecution.setModification(modification);
		scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);
		
	   JobExecutionInfo jobExecutionInfo = new JobExecutionInfo(); 
	   jobExecutionInfo.setStatusCode(executionMappingInfoStatus.equals("COMPLETED") ? 0 : 1);
	   jobExecutionInfo.setExecutionMessages(executionMappingInfoComments.toString());
	   updatePackageExecutionTargetTableInfo(table.getTableName(),startDate,endDate,jobExecutionInfo,packageDao,executionId,modification,clientAppDbJdbcTemplate);
	 
		
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
		 
		}
	public static DataResponse executeCustomPackageDerivedTables(ClientJdbcInstance clientJdbcInstance,Package userPackage,User user,FileDao fileDao,
			ScheduleDao scheduleDao,String timeZone,long executionId,PackageDao packageDao){
		 
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		StringBuilder executionMappingInfoComments = new StringBuilder();
		String executionMappingInfoStatus=Constants.ExecutionStatus.COMPLETED;
		PackageExecution packageExecution = null;
		Modification modification =null;
		Set<String> derivativeTablesSet = new HashSet<String>();
		Table table= null;
		String startDate = null;
		String endDate = null;
		Integer successRecordsOfFile = 0;
		Integer failedRecordsOfFile = 0;
		Integer duplicateRecordsOfFile = 0;
		Integer totalRecordsOfFile = 0;
		try{
			
		    modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			modification.setModifiedBy(user.getUserName());
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);
			
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			String clientSchemaName = clientDbDetails.get("clientdb_schema").toString();

			ClientData targetTableClientData = packageDao.getTargetTableInfoByPackage(user.getUserId(),userPackage.getPackageId(), clientAppDbJdbcTemplate);
			if (targetTableClientData != null) {
			   table = targetTableClientData.getUserPackage().getTable();
				List<TableDerivative> tableDerivatives = fileDao.getCustomTargetDerivativeTables(user.getUserId(),userPackage.getPackageId(), table.getTableId(), clientAppDbJdbcTemplate);
			if (tableDerivatives != null && tableDerivatives.size() > 0) {
				executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" table Derivatives size:"+tableDerivatives.size());
				for (TableDerivative tableDerivative : tableDerivatives) {
					derivativeTablesSet.add(tableDerivative.getTableName());
					boolean isDerivedTableExist = MinidwServiceUtil.isTableExists(user.getUserId(), null, tableDerivative.getTableName(), clientJdbcTemplate);
					if (isDerivedTableExist) {
						tableDerivative.setSchemaName(clientSchemaName);
						MinidwServiceUtil.truncateTable(tableDerivative.getSchemaName(),tableDerivative.getTableName(), clientJdbcTemplate);
						List<Column> derivedTableColumns = MinidwServiceUtil.getTableStructure(null, tableDerivative.getTableName(),0, user.getUserId(), clientJdbcTemplate);
						tableDerivative.setColumns(derivedTableColumns);
						derivativeTablesSet.add(tableDerivative.getTableName());
						tableDerivative.setPackageName(userPackage.getPackageName());
						startDate = TimeZoneDateHelper.getFormattedDateString();
						executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" execution started table Derivative: "+tableDerivative.getTableName()).append(" Date and Time: ").append(startDate).append(Constants.Config.NEW_LINE);
						
						Map<String, Object> processedResults = MinidwServiceUtil.processCustomTargetDerivativeTable(tableDerivative, table.getTableName(), clientJdbcTemplate);
					
						endDate = TimeZoneDateHelper.getFormattedDateString();
						
						if( processedResults.get("successRecords") != null){
							successRecordsOfFile = (Integer) processedResults.get("successRecords");
						}
						if( processedResults.get("failedRecords") != null){
							 failedRecordsOfFile = (Integer) processedResults.get("failedRecords");
						}
						if( processedResults.get("duplicateRecords") != null){
						 duplicateRecordsOfFile = (Integer) processedResults.get("duplicateRecords");
						}
						if( processedResults.get("totalRecords") != null){
							 totalRecordsOfFile = (Integer) processedResults.get("totalRecords");
						}
						
						executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" success Records Of File:"+successRecordsOfFile);
						executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" failed Records Of File:"+failedRecordsOfFile);
						executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" total Duplicate Records Found:"+duplicateRecordsOfFile);
						executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" total Records Of File:"+totalRecordsOfFile).append("\n");
						
						executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(" execution completed table Derivative: "+tableDerivative.getTableName()).append(" Date and Time: ").append(endDate);
						 
					    JobExecutionInfo jobExecutionInfo = new JobExecutionInfo(); 
					    jobExecutionInfo.setStatusCode(executionMappingInfoStatus.equals("COMPLETED") ? 0 : 1);
					    jobExecutionInfo.setExecutionMessages(executionMappingInfoComments.toString());
					    updatePackageExecutionTargetTableInfo(tableDerivative.getTableName(),startDate,endDate,jobExecutionInfo,packageDao,executionId,modification,clientAppDbJdbcTemplate);
						
						fileDao.updateCustomTargetDerivativeTableResults(tableDerivative, processedResults, modification, clientAppDbJdbcTemplate);
					}
				}
				message.setCode("SUCCESS");
				dataResponse.setObject(derivativeTablesSet);
			}else{
				message.setCode("ERROR");
				message.setText(" Derived tables not found for target table :"+table.getTableName());
				executionMappingInfoComments.append(" Derived tables not found for target table :"+table.getTableName());
			}
			}else{
				message.setCode("ERROR");
				message.setText(" target Table Client Data not found for package id: "+userPackage.getPackageId());
				executionMappingInfoComments.append(" target Table Client Data not found for package id: "+userPackage.getPackageId());
				throw new PackageExecutionException("target Table Client Data not found for package id: "+userPackage.getPackageId());
			}
		}catch(Exception e){
			executionMappingInfoStatus = Constants.ExecutionStatus.ERROR;
			executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(e.getCause().getMessage());
			message.setCode("ERROR");
			message.setText(e.getCause().getMessage());
		}
		
		String statusString = executionMappingInfoComments.toString();
		packageExecution = MinidwServiceUtil.getExecutionStatus(executionId, executionMappingInfoStatus.toString(), statusString,timeZone);
		packageExecution.setModification(modification);
		scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);
		
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
		}
	
	
	public static void reloadUrl(String userId,List<String> totalTablesSet,String clientIdfromHeader,String schemaName,JdbcTemplate clientAppDbJdbcTemplate,String authenticationEndPointUrl){
		RestTemplate restTemplate = new RestTemplate();
		try {
		   if (authenticationEndPointUrl != null) {
			   
				String encryptedClientId=AESConverter.encrypt(userId);
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.add("X-Auth-User-Token", encryptedClientId);	
				HttpEntity<Object> headerParamsPost = new HttpEntity<Object>(httpHeaders);

				String tableNames =String.join(",",	totalTablesSet.toArray(new String[totalTablesSet.size()]));
				ResponseEntity<?> updateStatusEntity = restTemplate.exchange(authenticationEndPointUrl + 
				"/addUserLevelTableAccessEntry/{userId}/{tableNames}/{schemaName}/{clientId}", HttpMethod.POST, headerParamsPost, Object.class, userId, tableNames, schemaName, clientIdfromHeader);
				@SuppressWarnings("unchecked")
				Map<String, Object> updateStatus =  (Map<String, Object>)updateStatusEntity.getBody();
				System.out.println("Updated url -- > " + authenticationEndPointUrl + "; updateStatus -- > " + updateStatus+".");
			} else {
				System.out.println("error occured while reloading the table level access -- > " + authenticationEndPointUrl+".");
				throw new Exception("\n unable to reload the table level access.");
			}
		} catch (Throwable t) {
			t.printStackTrace();
			MinidwServiceUtil.getErrorMessage("ERROR", t);
		}
	}
	

public static void alertsAndThreshoulds(String clientIdfromHeader, List<String> totalTablesSet, String packageName,String alertsThresholdsUrl) {
		String tableNames = String.join(",", totalTablesSet.toArray(new String[] {}));
		LOGGER.info("/************Alerts Api call initiated for package Name:" + packageName + "************************/");
		AlertsAndThreshoulds alertsAndThreshoulds = new AlertsAndThreshoulds(alertsThresholdsUrl, clientIdfromHeader,tableNames);
		alertsAndThreshoulds.start();
		LOGGER.info("/************Alerts Api call initiated Successfully package Name:" + packageName + "************************/");
	}
public static void updateTargetTableInfoStatus(Table table ,PackageDao packageDao,Package userPackage,User user,Modification modification,
		JdbcTemplate clientAppDbJdbcTemplate){
	   
        ClientData updateTargetTableInfoClientData = new ClientData();
		userPackage.setTable(table);
		updateTargetTableInfoClientData.setUserId(user.getUserId());
		updateTargetTableInfoClientData.setUserPackage(userPackage);
		updateTargetTableInfoClientData.setModification(modification);
		packageDao.updateTargetTableInfo(updateTargetTableInfoClientData, clientAppDbJdbcTemplate);
		
		// update package schedule status
		userPackage.setIsScheduled(Boolean.TRUE);
		userPackage.setScheduleStatus(Constants.Status.STATUS_DONE);
		packageDao.updatePackageScheduleStatus(updateTargetTableInfoClientData, clientAppDbJdbcTemplate);
}
public static int  updatePackageExecutionTargetTableInfo(String targetTableName,String startDate,String endDate,
		JobExecutionInfo jobExecutionInfo, PackageDao packageDao,long executionId,Modification modification,JdbcTemplate clientAppDbJdbcTemplate){
	//update package execution target table info
	PackageExecution tagetTableExecution = new PackageExecution();
	String jobExecutionStatus =  jobExecutionInfo.getStatusCode() == 0 ? "COMPLETED" : "ERROR";
	tagetTableExecution.setExecutionId(executionId);
	tagetTableExecution.setExecutionStatus(jobExecutionStatus);
	tagetTableExecution.setExecutionComments(jobExecutionInfo.getExecutionMessages());
	tagetTableExecution.setExecutionStartDate(startDate);
	tagetTableExecution.setLastExecutedDate(endDate);
	tagetTableExecution.setTagetTableName(targetTableName);
	tagetTableExecution.setModification(modification);
	return packageDao.updatePackageExecutionTargetTableInfo(tagetTableExecution,clientAppDbJdbcTemplate);
}



public static DataResponse runCustomDataSetsForDDlRefresh(PackageDao packageDao,List<String> totalTablesSet,JdbcTemplate jdbcTemplate,JdbcTemplate clientAppDbJdbcTemplate, FileDao fileDao, String runType, User
		user) throws SQLException{
	DataResponse dataResponse = new DataResponse();
	List<Message> messages = new ArrayList<>();
	Message message = new Message();
	StringBuilder detailedMessages = new StringBuilder();
	try{
	List<DDLayout> dDLayoutList = packageDao.getDDlayoutListByClientId(user.getClientId(),totalTablesSet, user.getUserId(), clientAppDbJdbcTemplate);
	if (dDLayoutList != null && dDLayoutList.size() > 0) {
		for (DDLayout ddlayout : dDLayoutList) {
			boolean tableExist = fileDao.isTableExists(user.getUserId(), null, ddlayout.getTableName(),jdbcTemplate);
			if (tableExist) {
				int count = 0;
				String errorMessage = "";
				try {
					count = CommonUtils.runDDlayoutTable(ddlayout,jdbcTemplate);
				} catch (Throwable e) {
					errorMessage = MinidwServiceUtil.getErrorMessageString(e);
					detailedMessages.append("\n").append(errorMessage);
				}
				ddlayout.setRunType(runType);
				ddlayout.setModification(user.getModification());
				ddlayout.setInsertedCount(count);
				ddlayout.setErrorMessage(errorMessage);
				packageDao.updateDDlayoutTableAuditLogs(user.getUserId(), ddlayout,  clientAppDbJdbcTemplate);
			} else {
				detailedMessages.append("\n").append("Table ").append(ddlayout.getTableName()).append(" doesn't exist at client schema" );
			}
		}
		message.setCode("SUCCESS");
		message.setText("DDlayout List successfully updated." + "\n" + detailedMessages.toString());
	  }else{
		  if (dDLayoutList == null) {
			  dDLayoutList = new ArrayList<>();
		  }
		  LOGGER.error("DDL's List is empty and size is :"+dDLayoutList.size());
		  message.setCode("ERROR");
		  message.setText("DDL's List is empty for the requested tables: "+String.join(",", totalTablesSet.toArray(new String[]{})));
	  }
	}catch(Throwable e){
		message.setCode("ERROR");
		message.setText(MinidwServiceUtil.getErrorMessageString(e));
	}
	messages.add(message);
	dataResponse.setMessages(messages);
	return dataResponse;
}


}
