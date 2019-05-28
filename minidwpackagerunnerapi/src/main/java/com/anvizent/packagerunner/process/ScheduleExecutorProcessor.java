package com.anvizent.packagerunner.process;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.anvizent.minidw.service.utils.processor.DataBaseProcessor;
import com.anvizent.minidw.service.utils.processor.ETLJobProcessor;
import com.anvizent.minidw.service.utils.processor.FlatFileProcessor;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.anvizent.minidw.service.utils.processor.ParseErrorMessage;
import com.anvizent.minidw.service.utils.processor.WebServiceProcessor;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.CSVConversionException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.ExecutionProcessor;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.StandardPackageDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.datamodel.anvizent.service.model.User;

@Component
public class ScheduleExecutorProcessor {
	
	protected static final Log log = LogFactory.getLog(ScheduleExecutorProcessor.class);
	
	@Autowired
	UserDao userDao;
	@Autowired
	PackageDao packageDao;
	@Autowired
	FileDao fileDao;
	@Autowired
	ScheduleDao scheduleDao;
	@Autowired
	WebServiceDao webServiceDao;
	@Autowired
	StandardPackageDao standardPackageDao; 
	@Autowired
	FlatFileProcessor flatFileProcessor;
	@Autowired
	DataBaseProcessor dbProcessor;
	@Autowired
	WebServiceProcessor webServiceProcessor;
	@Autowired		
	CommonProcessor commonProcessor;
	@Autowired
	ExecutionProcessor executionProcessor;
	@Autowired
	ETLJobProcessor etlJobProcessor;
	@Autowired
	ParseErrorMessage parseErrorMessage;
	@Autowired
	MetaDataFetch metaDataFetch;
	 
	@SuppressWarnings("unchecked")
	public DataResponse execute(User user, Package userPackage, PackageExecution packageExecution,  String deploymentType, SchedulerSlave slave){

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		Set<String> totalTablesSet = new HashSet<>();
		List<PackageExecutorMappingInfo> packageExecutorMappingInfoList = null;
		StringBuilder executionMappingInfoComments = new StringBuilder();
		Set<Integer> dlIds = new HashSet<Integer>();
		Map<String, Object> finalresponseMap = new HashMap<String, Object>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Modification modification = null;
		String executionStatus = null;
		long executionId = packageExecution.getExecutionId(); 
		String timeZone = packageExecution.getTimeZone();
		
		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(packageExecution.getClientId()));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserId());
			modification.setModifiedBy(user.getUserId());
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);

			String tempPath = Constants.Temp.getTempFileDir();
			tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
			tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
			String jobFilesPath = tempPath + "JobFiles/";
			etlJobProcessor.createDir(jobFilesPath);

			String runType = packageExecution.getRunType();

			packageExecution = commonProcessor.getExecutionStatus(packageExecution.getExecutionId(), Constants.ExecutionStatus.STARTED,
					"\n il & dl source files execution started  at " + slave.getId() + "-" + slave.getName(), timeZone);
			packageExecution.setModification(modification);
			
			scheduleDao.updatePackageExecutionStatus(packageExecution, clientAppDbJdbcTemplate);

			if (runType.equals("il") || runType.equals("all")) {

				packageExecutorMappingInfoList = packageDao.getPackageExecutorSourceMappingInfoList(executionId,
						clientAppDbJdbcTemplate);
				if (packageExecutorMappingInfoList != null && packageExecutorMappingInfoList.size() > 0) {
					log.info("Package execution started for id : " + packageExecution.getExecutionId()
							+ " dataResponse " + dataResponse);
					for (PackageExecutorMappingInfo packageExecutorMappingInfo : packageExecutorMappingInfoList) {
						DataResponse runIlAndDlDataResponse = executionProcessor.runIl(user, modification,
								packageExecutorMappingInfo.getPackageExecution().getTimeZone(), jobFilesPath,
								clientJdbcInstance, packageExecutorMappingInfo, deploymentType,
								userPackage);
						if (runIlAndDlDataResponse != null && runIlAndDlDataResponse.getMessages() != null) {
							if (runIlAndDlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
								Map<String, Object> responseMap = (Map<String, Object>) runIlAndDlDataResponse.getObject();
								totalTablesSet.add((String) (responseMap.get("iLTableName")));
								dlIds.add((Integer) (responseMap.get("dlId")));
							} else {
								throw new Exception(runIlAndDlDataResponse.getMessages().get(0).getText());
							}
						} else {
							throw new Exception("\n unable to get IL info. with excution Id :" + executionId
									+ " with DL id: "+packageExecution.getDlId());
						}
					}
				} else {
					message.setCode("ERROR");
					message.setText("unable to get Package Executor Mapping Info List. with execution Id:" + executionId
							+ " with DL Id:" + packageExecution.getDlId());
					executionStatus = Constants.ExecutionStatus.FAILED;
					executionMappingInfoComments
							.append("\n unable to get Package Executor Mapping Info List. with execution Id:"
									+ executionId + " with DL Id:" + packageExecution.getDlId());
					return dataResponse;
				}
			} else {
				executionStatus = Constants.ExecutionStatus.INPROGRESS;
				executionMappingInfoComments.append("Run only DL selected");
				packageExecution = commonProcessor.getExecutionStatus(executionId, executionStatus,
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
					executionMappingInfoComments.append("\n Dl execution started.");
					//for (int dLId : dlIds) {
						try {
							DataResponse runDlDataResponse = executionProcessor.runDl(user, modification, timeZone, jobFilesPath,
									clientJdbcInstance, executionId, deploymentType,
									userPackage, packageExecution.getDlId());
							if (runDlDataResponse != null && runDlDataResponse.getMessages() != null) {
								if (runDlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
									Map<String, Object> responseMap = (Map<String, Object>) runDlDataResponse.getObject();
									totalTablesSet.add((String) (responseMap.get("dlTableName")));
									executionMappingInfoComments.append((String) (responseMap.get("executionMappingInfoComments")));
								} else {
									executionMappingInfoComments.append(runDlDataResponse.getMessages().get(0).getText());
									message.setCode("ERROR");
									message.setText(runDlDataResponse.getMessages().get(0).getText());
								}
							} else {
								throw new Exception("\n unable to get DL info. with excution Id :" + executionId
										+ " with DL Id: " + packageExecution.getDlId());
							}
						} catch (TalendJobNotFoundException | CSVConversionException | InterruptedException
								| IOException | AnvizentCorewsException | ParseException e) {
							message.setCode("ERROR");
							message.setText(parseErrorMessage.getErrorMessageString(e));
							executionMappingInfoComments.append(parseErrorMessage.getErrorMessageString(e));
						}
					//}
					executionMappingInfoComments.append("\n Dl execution completed.");
				} else {
					executionMappingInfoComments.append("dl Ids list is empty and size is: " + dlIds.size());
					log.info("dl Ids list is empty and size is: " + dlIds.size());
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

			executionMappingInfoComments.append("\n il & dl source files execution completed.");
			executionStatus = Constants.ExecutionStatus.COMPLETED;
			message.setCode("SUCCESS");
			message.setText(userPackage.getPackageName() + ": Data Processed Successfully");
			finalresponseMap.put("totalTablesSet", totalTablesSet);
			dataResponse.setObject(finalresponseMap);

		} catch (Throwable ae) {
			log.error("error while getPackageExecutorSourceMappingInfoList() ", ae);
			message.setCode("ERROR");
			message.setText(executionMappingInfoComments.toString() + parseErrorMessage.getErrorMessageString(ae));
			executionStatus = Constants.ExecutionStatus.FAILED;
			executionMappingInfoComments.append(ae.getMessage());
		}
		packageExecution = commonProcessor.getExecutionStatus(executionId, executionStatus,
				executionMappingInfoComments.toString(), timeZone);
		packageExecution.setModification(modification);
		scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);

		return dataResponse;
	}
	
	public DataResponse runCustomDataSets(List<String> totalTablesSet,User user,JdbcTemplate jdbcTemplate,JdbcTemplate clientAppDbJdbcTemplate, Modification modification,String runType) throws SQLException{
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
				String errorMessage = "";
				int count = 0;
				if (tableExist) {
					try {
						count = commonProcessor.runDDlayoutTable(ddlayout,jdbcTemplate);
						ddlsTable.add(ddlayout.getTableName());
					} catch (Throwable e) {
						errorMessage = parseErrorMessage.getErrorMessageString(e);
					}
				} else {
					errorMessage = "Table " + ddlayout.getTableName() + " does not exist";
				}
				ddlayout.setRunType(runType);
				ddlayout.setModification(modification);
				ddlayout.setInsertedCount(count);
				ddlayout.setErrorMessage(errorMessage);
				packageDao.updateDDlayoutTableAuditLogs(user.getUserId(), ddlayout, clientAppDbJdbcTemplate);
			}
			message.setCode("SUCCESS");
			message.setText("DDlayout List successfully updated.");
		  }else{
			  if (dDLayoutList == null) {
				  dDLayoutList = new ArrayList<>();
			  }
			  log.error("DDL's List is empty and size is :"+dDLayoutList.size());
			  message.setCode("ERROR");
			  message.setText("DDL Layout List is empty and size is :"+dDLayoutList.size());
		  }
		}catch(Throwable e){
			log.error("DLL Layout execution failed ",e);
			message.setCode("ERROR");
			message.setText(parseErrorMessage.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	

}
