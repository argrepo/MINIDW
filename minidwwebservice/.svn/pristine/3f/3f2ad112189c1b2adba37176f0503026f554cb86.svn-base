package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.EtlJobUtil;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.model.ETLjobExecutionMessages;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.service.DataValidationService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.DataValidation;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.MasterDataValidation;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;

@RestController("user_dataValidationServiceRestController")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/package")
@CrossOrigin
public class DataValidationServiceRestController {
	protected static final Logger logger = LoggerFactory.getLogger(DataValidationServiceRestController.class);
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private UserDetailsService userService;
	
	@Autowired
	private PackageService packageService;
	
	@Autowired
	private DataValidationService dataValidationService;
	
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;
	
	private EtlJobUtil etlJobUtil = new EtlJobUtil(Constants.Config.COMMON_ETL_JOBS, Constants.Config.ETL_JOBS,
			System.getProperty(Constants.Config.SYSTEM_PATH_SEPARATOR));
	
	@RequestMapping(value="/runSeletedScripts", method= RequestMethod.POST)
	public DataResponse executeMultipleScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, 
			 @RequestBody MasterDataValidation masterDataInfo, 
			 Locale locale, HttpServletRequest request){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		SourceFileInfo sourceFileInfo = null;
		try{
			String jobFilesPath = null;
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
           
			StringBuilder dlIds = new StringBuilder();
			masterDataInfo.getDlsInfo().forEach(dls ->{
				  dlIds.append(",").append(dls.getdL_id());
			  });
			 dlIds.deleteCharAt(0);
			
			StringBuilder scriptsIds = new StringBuilder();
			masterDataInfo.getDataValidations().forEach(data -> {
				scriptsIds.append(",").append(data.getScriptId());
			});
			scriptsIds.deleteCharAt(0); 
			

			ClientJdbcInstance  clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader, clientAppDbJdbcTemplate);
			FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
			boolean isEncryptionRequired = fileSettings.getFileEncryption();
			
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
		      String databaseHost = clientDbDetails.get("region_hostname").toString();
		      String databasePort= clientDbDetails.get("region_port").toString(); 
		      String schemaName= clientDbDetails.get("clientdb_schema").toString();
		      String clientdbUsername= clientDbDetails.get("clientdb_username").toString();
		      String clientDbPassword= clientDbDetails.get("clientdb_password").toString();
		      
			String tempPath = com.datamodel.anvizent.helper.minidw.Constants.Temp.getTempFileDir();
			if (tempPath.contains("\\")) {
				tempPath = tempPath.replace('\\', '/');
			}
			if (!tempPath.endsWith("/")) {
				tempPath = tempPath + "/";
			}
			
			for(DataValidation datavalidation : masterDataInfo.getDataValidations()){
				
				String dataSourceName = clientIdfromHeader + "_" + datavalidation.getScriptId();
				String jobNameAndType = "Data_Validation_Framework_"+datavalidation.getScriptId();
				String startDateTime = CommonUtils.currentDateTime();
				String s3FolderPath = clientIdfromHeader+"_Data_Validation_Framework_"+datavalidation.getScriptId()+"_"+startDateTime+"/";
				jobFilesPath = tempPath + "DataValidation/"+CommonUtils.generateUniqueIdWithTimestamp()+"/";		       
			    
				CommonUtils.createDir(jobFilesPath);
				
			     /* Map<String, String> ilContextParams = dataValidationService.getDataValidationContextParams(datavalidation.getScriptId(), clientAppDbJdbcTemplate);*/
			     /*  ilContextParams.put("SCRIPT_ID", "{script_id}");
			       ilContextParams.put("DL_ID", "{dl_id}");
			       ilContextParams.put("EMAIL_AUTH_PASSWORD", "{email_auth_password}");
			       ilContextParams.put("EMAIL_AUTH_USERNAME", "{email_auth_username}");
			       ilContextParams.put("EMAIL_BCC_LIST", "{email_bcc_list}");
			       ilContextParams.put("EMAIL_CC_LIST", "{email_cc_list}");
			       ilContextParams.put("EMAIL_FROM", "{email_from}");
			       ilContextParams.put("EMAIL_MESSAGE", "{email_message}");
			       ilContextParams.put("EMAIL_SMTP_HOST", "{email_smtp_host}");
			       ilContextParams.put("EMAIL_SMTP_PORT", "{email_smtp_port}");
			       ilContextParams.put("EMAIL_SUBJECT", "{email_subject}");
			       ilContextParams.put("EMAIL_TO_LIST", "{email_to_list}");*/
			       
			      Map<String, String> ilParamsVals = new LinkedHashMap<String, String>();
			      
			      String clientStagingSchema = schemaName+"_staging";
			      
			        // staging properties
					ilParamsVals.put("SRC_HOST", databaseHost);
					ilParamsVals.put("SRC_PORT", databasePort);
					ilParamsVals.put("SRC_DBNAME", clientStagingSchema);
					ilParamsVals.put("SRC_UN", clientdbUsername);
					ilParamsVals.put("SRC_PW", clientDbPassword);
					ilParamsVals.put("SRC", "data_validation_result");

					// target database properties
					ilParamsVals.put("TGT_HOST", databaseHost);
					ilParamsVals.put("TGT_PORT", databasePort);
					ilParamsVals.put("TGT_DBNAME", schemaName);
					ilParamsVals.put("TGT_UN", clientdbUsername);
					ilParamsVals.put("TGT_PW", clientDbPassword);
					ilParamsVals.put("TGT", "");
					
					// master database properties

					ilParamsVals.put("MASTER_HOST", databaseHost);
					ilParamsVals.put("MASTER_PORT", databasePort);
					ilParamsVals.put("MASTER_DBNAME", schemaName+"_appdb");
					ilParamsVals.put("MASTER_UN", clientdbUsername);
					ilParamsVals.put("MASTER_PW", clientDbPassword);
					
					
					// client id and package id
					ilParamsVals.put("CLIENT_ID", clientIdfromHeader);
					ilParamsVals.put("PACKAGE_ID", userId);
					
					// file path details
					ilParamsVals.put("FILE_PATH", jobFilesPath);
					ilParamsVals.put("ERROR_LOG_PATH", jobFilesPath);
					ilParamsVals.put("BULK_PATH", jobFilesPath);
					
					ilParamsVals.put("JOB_STARTDATETIME", startDateTime);
					ilParamsVals.put("JOB_NAME", jobNameAndType);
					ilParamsVals.put("JOB_TYPE", jobNameAndType);
					ilParamsVals.put("SCRIPT_ID", datavalidation.getScriptId().toString());
					ilParamsVals.put("DL_ID", dlIds.toString());
					
					//ETL_CNTRL, ETL_JOB_ERROR_LOG, ETL_JOB_LOAD_SMRY, ETL_Jobs
					
					ilParamsVals.put("ETL_JOBS", "ETL_Jobs");
					ilParamsVals.put("ETL_CONTROL_JOB", "ETL_CNTRL");
					ilParamsVals.put("ETL_JOB_ERROR_LOG", "ETL_JOB_ERROR_LOG");
					ilParamsVals.put("ETL_JOB_LOAD_SMRY", "ETL_JOB_LOAD_SMRY");
					
					//Hard coded params for email 
					ilParamsVals.put("EMAIL_AUTH_PASSWORD", "Anvizent@123");
					ilParamsVals.put("EMAIL_AUTH_USERNAME", "anvizdatavalidation@gmail.com");
					ilParamsVals.put("EMAIL_BCC_LIST", "");
					ilParamsVals.put("EMAIL_CC_LIST", "");
					ilParamsVals.put("EMAIL_FROM", "anvizdatavalidation@gmail.com");
					ilParamsVals.put("EMAIL_MESSAGE", "Hi, There,\\n  P.F.A");
					ilParamsVals.put("EMAIL_SMTP_HOST", "smtp.gmail.com");
					ilParamsVals.put("EMAIL_SMTP_PORT", "465");
					ilParamsVals.put("EMAIL_SUBJECT", "Data validations result");
					ilParamsVals.put("EMAIL_TO_LIST", "vinodkumar.basireddy@anvizent.com");

					ilParamsVals.put("DATASOURCENAME", dataSourceName);
					
					//packageService.parseContextParams(ilContextParams, ilParamsVals);
					
					String[] contextParamArry = CommonUtils.convertToContextParamsArray(ilParamsVals);
				
					if(StringUtils.isNotBlank(datavalidation.getDataValidationType().getJobName())){
						int dvstatus = -1;
						ETLjobExecutionMessages etLjobExecutionMessages = new ETLjobExecutionMessages();
						try{
							etLjobExecutionMessages = etlJobUtil.runETLjar(datavalidation.getDataValidationType().getJobName(),
									datavalidation.getDataValidationType().getDependencyJars(),contextParamArry);
							dvstatus = etLjobExecutionMessages.getStatus();
							String  executionMessages = etLjobExecutionMessages.getErrorStreamMsg() + etLjobExecutionMessages.getInputStreamMsg();
							JobExecutionInfo executionInfo = new JobExecutionInfo();
							  executionInfo.setStatusCode(dvstatus);
							  executionInfo.setJobClass(datavalidation.getDataValidationType().getJobName());
							  executionInfo.setDependencyJars(datavalidation.getDataValidationType().getDependencyJars());
							  executionInfo.setJobName(jobNameAndType);
							  executionInfo.setJobId(jobNameAndType+CommonUtils.currentDateTime());
							  executionInfo.setExecutionMessages(executionMessages);
							    Modification modification = new Modification(new Date());
								modification.setCreatedBy(userId);
							  executionInfo.setModification(modification);
							  if(dvstatus == 0){
								  File folder = new File(jobFilesPath);
								  File[] listOfFiles = folder.listFiles();
								  String fileName = null;
								  for(File file : listOfFiles){
									  if(file.isFile()){
										  fileName = file.getName();
										   if(fileName.startsWith(clientIdfromHeader+"_DataValidationResult") && fileName.endsWith(Constants.FileType.XLSX)){
										      sourceFileInfo = MinidwServiceUtil.uploadFileToS3(file, s3BucketInfo, s3FolderPath,isEncryptionRequired);
										      logger.info(sourceFileInfo.getFilePath());
										   }
									  }										  
								  }								  
								    message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
									message.setText(messageSource.getMessage("anvizent.package.success.dvJobIsExecuted", null, locale));
									messages.add(message);
									dataResponse.setMessages(messages);
							  }else{
								  message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
									message.setText(messageSource.getMessage("anvizent.package.error.dvJobExecutionFailed", null, locale));
									messages.add(message);
									dataResponse.setMessages(messages);
							  }												    
							  
						}catch (InterruptedException | IOException e) {
							throw new TalendJobNotFoundException(messageSource.getMessage("anvizent.package.error.dvJobFailed",	null, locale), dvstatus + e.getMessage(), e);
						}
					}else {
						throw new TalendJobNotFoundException(messageSource.getMessage("anvizent.package.error.jobnotfound", null, locale));
					}
			}			    
				
		}catch (AnvizentCorewsException e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			message.setText(e.getMessage());
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.package.success.unabletorundvjob", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value="/viewJobExecutionResults/{scriptId}", method=RequestMethod.GET)
	public DataResponse getJobExecutionResults(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("scriptId") Integer scriptId, 
			HttpServletRequest request, Locale locale){
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate =null;
		JdbcTemplate clientAppDbJdbcTemplate =null;
		List<JobResult> resultlist = null;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			
			resultlist = dataValidationService.getJobExecutionResults(clientIdfromHeader, scriptId, clientJdbcTemplate);
			dataResponse.setObject(resultlist);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			messages.add(message);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			messages.add(message);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value="/viewScriptValidationErrorLogs/{batchId}", method=RequestMethod.GET)
	public DataResponse getValidationScriptErrorLogs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("batchId") String batchId, Locale locale,
			HttpServletRequest request){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null;
		List<JobResult> resultlist = null;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			resultlist = dataValidationService.getValidationScriptErrorLogs(batchId, clientJdbcTemplate);
			if(resultlist != null && resultlist.size() > 0){
				dataResponse.setObject(resultlist);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noRecordsFound", null, locale));
				messages.add(message);
			}
		}catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToviewErrorLogs", null, locale));
			messages.add(message);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToviewErrorLogs", null, locale));
			messages.add(message);			
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	@RequestMapping(value="/downloadBatchResults/{batchId}", method=RequestMethod.GET)
	public DataResponse downloadBatchResultsByBatchId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("batchId") String batchId, Locale locale,
			HttpServletRequest request){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader, clientAppDbJdbcTemplate);
			//String deploymentType = request.getHeader(com.datamodel.anvizent.helper.minidw.Constants.Config.DEPLOYMENT_TYPE);
			FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
			List<String> filePathis = MinidwServiceUtil.downloadFolderFilesFromS3(batchId+"/", s3BucketInfo, false, fileSettings.getFileEncryption());
			if(filePathis !=null && filePathis.size() > 0 ){
				dataResponse.setObject(filePathis);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noResultsFoundToDownload", null, locale));
				messages.add(message);
			}
			
		}catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			messages.add(message);
			
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			messages.add(message);			
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value="/getpreloadValidationInfoByConnectorId", method=RequestMethod.POST)
	public DataResponse getpreloadValidationInfoByConnectorId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("validationTypeId") Integer validationTypeId, 
			 @RequestParam("connectionId") Integer connectionId, HttpServletRequest request, HttpServletResponse response, Locale locale){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<DataValidation> dataValidationList =null; 
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dataValidationList = dataValidationService.getPreloadValidationInfoByConnectionId(validationTypeId, connectionId, clientAppDbJdbcTemplate);
			if(dataValidationList != null && dataValidationList.size() > 0){
				dataResponse.setObject(dataValidationList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noRecordsFound", null, locale));
				messages.add(message);
			}
		}catch(Throwable t){
			logger.error("error while getAllDataValidations: "+t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo", null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value="/getSelectedValidationByIds", method=RequestMethod.POST)
	public DataResponse getSelectedValidationByIds(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			 @RequestParam("scriptIds") String scriptIds, 
			 @RequestParam("connectionId") Integer connectionId,
			 HttpServletRequest request, HttpServletResponse response, Locale locale){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<DataValidation> dataValidationList =null; 
		try{
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dataValidationList = dataValidationService.getSelectedValidationByIds(scriptIds,connectionId, clientAppDbJdbcTemplate);
			if(dataValidationList != null && dataValidationList.size() > 0){
				dataResponse.setObject(dataValidationList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noRecordsFound", null, locale));
				messages.add(message);
			}
		}catch(Exception e){
			logger.error("error while getAllDataValidations: "+e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo", null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
		}
		dataResponse.addMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value="/executePreLoadValidationScripts", method=RequestMethod.POST)
	public DataResponse executePreLoadValidationScripts(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("sourceFilePath")String  sourceFilePath,
			@RequestParam("jobName")String jobName,
			@RequestParam("dependencyJar")String dependencyJar,
			@RequestParam("validationTypeId")Integer validationTypeId,
			@RequestParam("scriptId") Integer scriptId,
			@RequestParam("validationScript")String validationScript,
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String downloadFilePath = null;
		File localFilePath = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			String databaseHost = clientDbDetails.get("region_hostname").toString();
			String databasePort = clientDbDetails.get("region_port").toString();
			String schemaName = clientDbDetails.get("clientdb_schema").toString();
			String clientdbUsername = clientDbDetails.get("clientdb_username").toString();
			String clientDbPassword = clientDbDetails.get("clientdb_password").toString();

			S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader,
					clientAppDbJdbcTemplate);
			String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
			FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
			boolean isEncryptionRequired = fileSettings.getFileEncryption();
			/*List<Map<String,String>>contextParamMap = null;
			if(validationTypeId  > 0){
				contextParamMap = dataValidationService.getContextParamNameAndValueById(validationTypeId, clientAppDbJdbcTemplate);
			}
			 */
			//To do
			downloadFilePath = MinidwServiceUtil.downloadFileFromS3(sourceFilePath, s3BucketInfo, isEncryptionRequired);
			
			downloadFilePath = (downloadFilePath.indexOf('\\') != -1) ? downloadFilePath.replaceAll("\\\\", "/") : downloadFilePath;
			String downloadFileName = StringUtils.substringAfterLast(downloadFilePath, "/");
			String tempPath = Constants.Temp.getTempFileDir();
			tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
			tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
			String jobFilesPath = tempPath + "DataValidation/"+CommonUtils.generateUniqueIdWithTimestamp()+"/";
			CommonUtils.createDir(jobFilesPath);
			String validationScriptId = String.valueOf(scriptId);

				String dataSourceName = clientIdfromHeader + "_" + validationScriptId;
				String job_type = "Data_Validation_Framework_";
				String startDateTime = CommonUtils.currentDateTime();
				String jobNameAndType =  job_type + validationScriptId;
				String s3FolderPath = clientIdfromHeader + "_Data_Validation_Framework_" + scriptId + "_" + startDateTime+"/";
				Map<String, String> ilParamsVals = new LinkedHashMap<String, String>();

				String clientStagingSchema = schemaName + "_staging";

				ilParamsVals.put("BULK_PATH", jobFilesPath);
				ilParamsVals.put("CLIENT_ID", clientIdfromHeader);
				ilParamsVals.put("Cont_File", clientIdfromHeader);
				ilParamsVals.put("DATASOURCENAME", dataSourceName);
				ilParamsVals.put("DateFormat", 
						"yyyy-MM-dd'T'HH:mm:ss'Z'@#dd,MM,yyyy' 'HH:mm:ss@#yyyy-MM-dd@#yyyy-MM-dd'T'HH:mm.ss'Z'@#yyyy-MM-dd'T'HH:mm:ss@#yyyy-MM-dd' 'HH:mm:ss@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy/MM/dd' 'HH:mm:ss@#MM/dd/yyyy' 'HH:mm:ss@#MM-dd-yyyy' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss'Z'@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy-MM-dd' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssXXX@#yyyy-dd-MM' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssSSS@#yyyy-dd-MM' 'HH:mm:ssSSS@#MM/dd/yyyy' 'HH:MM' AM'");
				ilParamsVals.put("DL_ID", "2");

				// Hard coded params for email
				ilParamsVals.put("EMAIL_AUTH_PASSWORD", "Anvizent@123");
				ilParamsVals.put("EMAIL_AUTH_USERNAME", "anvizdatavalidation@gmail.com");
				ilParamsVals.put("EMAIL_BCC_LIST", "");
				ilParamsVals.put("EMAIL_CC_LIST", "");
				ilParamsVals.put("EMAIL_FROM", "anvizdatavalidation@gmail.com");
				ilParamsVals.put("EMAIL_MESSAGE", "Hi, There,\\n  P.F.A");
				ilParamsVals.put("EMAIL_SMTP_HOST", "smtp.gmail.com");
				ilParamsVals.put("EMAIL_SMTP_PORT", "465");
				ilParamsVals.put("EMAIL_SUBJECT", "Data validations result");
				ilParamsVals.put("EMAIL_TO_LIST", "sriravali.samudrala@anvizent.com");
				
				ilParamsVals.put("ETL_JOBS", "ETL_Jobs");
				ilParamsVals.put("ETL_CONTROL_JOB", "ETL_CNTRL");
				ilParamsVals.put("ETL_JOB_ERROR_LOG", "ETL_JOB_ERROR_LOG");
				ilParamsVals.put("ETL_JOB_LOAD_SMRY", "ETL_JOB_LOAD_SMRY");

				ilParamsVals.put("FILE_PATH", jobFilesPath);
				ilParamsVals.put("FILE_SRC", downloadFilePath);

				ilParamsVals.put("JOB_DESCRIPTION", "");
				ilParamsVals.put("JOB_NAME", jobNameAndType);
				ilParamsVals.put("JOB_ORDER_SEQ", "");
				ilParamsVals.put("JOB_STARTDATETIME", startDateTime);
				ilParamsVals.put("JOB_TYPE", dataSourceName);

				// master database properties

				ilParamsVals.put("MASTER_HOST", databaseHost);
				ilParamsVals.put("MASTER_PORT", databasePort);
				ilParamsVals.put("MASTER_DBNAME", clientStagingSchema);
				ilParamsVals.put("MASTER_UN", clientdbUsername);
				ilParamsVals.put("MASTER_PW", clientDbPassword);

				ilParamsVals.put("PACKAGE_ID", clientId);
				ilParamsVals.put("SCRIPT_ID", validationScriptId);

				// staging properties
				ilParamsVals.put("SRC", "data_validation_result");
				ilParamsVals.put("SRC_DBNAME", clientStagingSchema);
				ilParamsVals.put("SRC_HOST", databaseHost);
				ilParamsVals.put("SRC_PORT", databasePort);
				ilParamsVals.put("SRC_PW", clientDbPassword);
				ilParamsVals.put("SRC_UN", clientdbUsername);

				// target database properties
				ilParamsVals.put("TGT", "");
				ilParamsVals.put("TGT_DBNAME", schemaName);
				ilParamsVals.put("TGT_HOST", databaseHost);
				ilParamsVals.put("TGT_PORT", databasePort);
				ilParamsVals.put("TGT_PW", clientDbPassword);
				ilParamsVals.put("TGT_UN", clientdbUsername);
				ilParamsVals.put("TRUNCATE_FLAG", clientdbUsername);

				String[] contextParamArry = CommonUtils.convertToContextParamsArray(ilParamsVals);
				if (StringUtils.isNotBlank(jobName)) {
					int dvstatus = -1;
					ETLjobExecutionMessages etLjobExecutionMessages = new ETLjobExecutionMessages();
					try {
						etLjobExecutionMessages = etlJobUtil.runETLjar(jobName,	dependencyJar, contextParamArry);
						dvstatus = etLjobExecutionMessages.getStatus();
						String executionMessages = etLjobExecutionMessages.getErrorStreamMsg()
								+ etLjobExecutionMessages.getInputStreamMsg();
						JobExecutionInfo executionInfo = new JobExecutionInfo();
						executionInfo.setStatusCode(dvstatus);
						executionInfo.setJobClass(jobName);
						executionInfo.setDependencyJars(dependencyJar);
						executionInfo.setJobName(jobNameAndType);
						executionInfo.setJobId(jobNameAndType + CommonUtils.currentDateTime());
						executionInfo.setExecutionMessages(executionMessages);
						Modification modification = new Modification(new Date());
						modification.setCreatedBy(clientId);
						executionInfo.setModification(modification);
						if (dvstatus == 0) {
							File folder = new File(jobFilesPath);
							File[] listOfFiles = folder.listFiles();
							String fileName = null;
							for (File file : listOfFiles) {
								if (file.isFile()) {
									fileName = file.getName();
									if (fileName.startsWith(clientIdfromHeader + "_DataValidationResult")
											&& fileName.endsWith(Constants.FileType.XLSX)) {
										SourceFileInfo sourceFileInfo = MinidwServiceUtil.uploadFileToS3(file, s3BucketInfo,
												s3FolderPath, isEncryptionRequired);
										logger.info(sourceFileInfo.getFilePath());
									}
								}
							}
							message.setCode(
									com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
							message.setText(
									messageSource.getMessage("anvizent.package.success.dvJobIsExecuted", null, locale));
							messages.add(message);
							dataResponse.setMessages(messages);
						} else {
							message.setCode(
									com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
							message.setText(messageSource.getMessage("anvizent.package.error.dvJobExecutionFailed",
									null, locale));
							messages.add(message);
							dataResponse.setMessages(messages);
						}

					} catch (InterruptedException | IOException e) {
						throw new TalendJobNotFoundException(
								messageSource.getMessage("anvizent.package.error.dvJobFailed", null, locale),
								dvstatus + e.getMessage(), e);
					}
				} else {
					throw new TalendJobNotFoundException(
							messageSource.getMessage("anvizent.package.error.jobnotfound", null, locale));
			}

			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
		} catch (Exception e) {
			logger.error("error while getAllDataValidations: " + e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo",
					null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
		}
		return dataResponse;
	}
	
}
