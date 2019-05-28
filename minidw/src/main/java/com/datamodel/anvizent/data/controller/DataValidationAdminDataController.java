package com.datamodel.anvizent.data.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.anvizent.client.data.to.csv.path.converter.ClientDBProcessor;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ContextParameter;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.DataValidation;
import com.datamodel.anvizent.service.model.DataValidationType;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.MasterDataValidation;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchemaDbVariables;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController("user_dataValidationAdminDataController")
@RequestMapping("" + Constants.AnvizentURL.MINIDW_ADMIN_BASE_URL + "/etlAdmin")
@CrossOrigin
public class DataValidationAdminDataController {
	
	protected static final Logger logger = LoggerFactory.getLogger(DataValidationAdminDataController.class);
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	
	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities adminRestUtilities;
	
	@Autowired
	@Qualifier("dataValidationServicesRestTemplateUtilities")
	private RestTemplateUtilities restTemplateUtilities;

	List<String> pathList = new ArrayList<>();
	
	@RequestMapping(value = "/viewValidationScriptByScriptId", method = RequestMethod.POST)
	public DataResponse viewValidationScriptById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("scriptId") Integer scriptId,  Locale locale,
			HttpServletRequest request){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		User user = CommonUtils.getUserDetails(request, null, null);
		try{
			LinkedMultiValueMap<String, Object> map = new  LinkedMultiValueMap<>();
			  map.add("scriptId", scriptId);
			if(scriptId != null && scriptId !=0 ){
				dataResponse = restTemplateUtilities.postRestObject(request, "/viewValidationScriptByScriptId", map, user.getUserId());
				if(dataResponse != null && dataResponse.getHasMessages()
						&& dataResponse.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				}else{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				    message.setText(messageSource.getMessage("anvizent.package.label.noscriptfound",null, locale));
				    messages.add(message);
				}
			}
		}catch(Throwable t){
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
		    message.setText(messageSource.getMessage("anvizent.message.error.text.unableToProcessYourRequest",null, locale)+t.getMessage());
		    messages.add(message);
		}
		dataResponse.addMessages(messages);;
		return dataResponse;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/executeSeletedScripts", method = RequestMethod.POST)
	public DataResponse executeDataValidationScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody List<DataValidation> scripts, Locale locale,
			HttpServletRequest request){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		User user = CommonUtils.getUserDetails(request, null, null);
		try{
			if(scripts != null && scripts.size()>0){
				MasterDataValidation  masterValidation = new MasterDataValidation();
				List<DLInfo> dlInfoList = getDlInfoList(request, user.getUserId());
				if(dlInfoList.size()<=0){
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.nodlsmapped", null, locale));
					messages.add(message);
				}
				
				for(DataValidation datavalidation:scripts){
					DataResponse validationTypeJobsandDependencyJars = restTemplateUtilities.getRestObject(request, "/getValidationTypeJobsandDependencyJars/{validationtypeId}",user.getUserId(),datavalidation.getValidationTypeId());
					DataValidationType dataValidationTypeJobs = null;
					ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					if(validationTypeJobsandDependencyJars != null && validationTypeJobsandDependencyJars.getHasMessages() 
							   && validationTypeJobsandDependencyJars.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
						LinkedHashMap<String, Object> validationTyperesponse = (LinkedHashMap<String, Object>) validationTypeJobsandDependencyJars.getObject();
						 dataValidationTypeJobs = mapper.convertValue(validationTyperesponse, new TypeReference<DataValidationType>() {
						});
					}
					datavalidation.setDataValidationType(dataValidationTypeJobs);
				}
				masterValidation.setDataValidations(scripts);
				masterValidation.setDlsInfo(dlInfoList);
				
				DataResponse runScriptsResponse = restUtilities.postRestObject(request, "/runSeletedScripts", masterValidation, clientId);
				if(runScriptsResponse != null && runScriptsResponse.getHasMessages() 
						&& runScriptsResponse.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.scriptsExecutedSuccessfully", null, locale));
					messages.add(message);
				}else{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.scriptsExecutionFailed", null, locale));
					messages.add(message);
				}
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noscriptIdFound", null, locale));
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
	
	@SuppressWarnings("unchecked")
	private List<DLInfo> getDlInfoList(HttpServletRequest request, String userId) {
		List<DLInfo> dlInfoList = new ArrayList<DLInfo>();
		DataResponse dlDataResponse = restTemplateUtilities.getRestObject(request, "/getClientMappingDLInfo", userId);
		if(dlDataResponse != null && dlDataResponse.getHasMessages() 
				&& dlDataResponse.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
			List<LinkedHashMap<String, Object>> response = (List<LinkedHashMap<String, Object>>) dlDataResponse.getObject();
			ObjectMapper mapper = new ObjectMapper();
			dlInfoList = mapper.convertValue(response, new TypeReference<List<DLInfo>>() {});
		}else{
			dlInfoList = new ArrayList<>();
		}
		return dlInfoList;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/runValidationScriptById", method = RequestMethod.POST)
	public DataResponse runDataValidationScript(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DataValidation dataValidation, Locale locale,
			HttpServletRequest request){
		DataResponse dataResponse = null;
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		User user = CommonUtils.getUserDetails(request, null, null);
		try{
			if(dataValidation != null && dataValidation.getScriptId() >0){
				MasterDataValidation  masterValidation = new MasterDataValidation();
				List<DLInfo> dlInfoList = getDlInfoList(request, user.getUserId());
				if(dlInfoList.size()<=0){
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.nodlsmapped", null, locale));
					messages.add(message);
				}
					DataResponse validationTypeJobsandDependencyJars = restTemplateUtilities.getRestObject(request, "/getValidationTypeJobsandDependencyJars/{validationtypeId}",user.getUserId(),dataValidation.getValidationTypeId());
					DataValidationType dataValidationTypeJobs = null;
					ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					if(validationTypeJobsandDependencyJars != null && validationTypeJobsandDependencyJars.getHasMessages() 
							   && validationTypeJobsandDependencyJars.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
						LinkedHashMap<String, Object> validationTyperesponse = (LinkedHashMap<String, Object>) validationTypeJobsandDependencyJars.getObject();
						 dataValidationTypeJobs = mapper.convertValue(validationTyperesponse, new TypeReference<DataValidationType>() {
						});
					}
					dataValidation.setDataValidationType(dataValidationTypeJobs);
					masterValidation.addDataValidation(dataValidation);
					masterValidation.setDlsInfo(dlInfoList);
				
				DataResponse runScriptsResponse = restUtilities.postRestObject(request, "/runSeletedScripts", masterValidation, clientId);
				if(runScriptsResponse != null && runScriptsResponse.getHasMessages() 
						&& runScriptsResponse.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.scriptsExecutedSuccessfully", null, locale));
					messages.add(message);
				}else{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.scriptsExecutionFailed", null, locale));
					messages.add(message);
				}
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noscriptIdFound", null, locale));
				messages.add(message);
			}
			
		}catch(Exception e){
			logger.error("error while runDataValidationScript: "+e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo", null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
		}
		return dataResponse;
	}
	
	@RequestMapping(value="/viewScriptValidationErrorLogs/{batchId}", method=RequestMethod.GET)
	public DataResponse getValidationScriptErrorLogs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("batchId") String batchId, Locale locale,
			HttpServletRequest request){
		   logger.info("in Model App viewErrorLogs()");
		return restUtilities.getRestObject(request, "/viewScriptValidationErrorLogs/{batchId}", clientId, batchId);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/downloadBatchResults/{batchId}/{isFileDownload}", method=RequestMethod.GET)
	public DataResponse downloadBatchResults(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, HttpServletResponse res, 
			@PathVariable("batchId") String batchId,
			@PathVariable("isFileDownload") boolean isFileDownload){
		
		   logger.info("in Model App viewErrorLogs()");
		    DataResponse dataResponse = new DataResponse();;
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			File file = null;
			
		   try{
			   if(!isFileDownload){
				   DataResponse response = restUtilities.getRestObject(request, "/downloadBatchResults/{batchId}", clientId, batchId);
				   if(response != null && response.getHasMessages() 
						   && response.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
					   
					   pathList = (List<String>) response.getObject();
					   message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					   messages.add(message);
				   }else{
					   message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					   message.setText(messageSource.getMessage("anvizent.message.error.text.noResultsFoundToDownload", null, locale));
					   messages.add(message);
				   }
			   } else {
				   if(pathList != null && pathList.size() > 0){
					   for(String filePath: pathList){
							  file = new File(filePath);
							 /* io = new FileInputStream(new File(filePath));
							  byteFile = IOUtils.toByteArray(io);
					          outStream = new FileOutputStream(file.getAbsolutePath());
					          outStream.write(byteFile);*/
							CommonUtils.sendFIleToStream(file.getAbsolutePath(), request, res);
						}
				   }else{
					   message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					   message.setText(messageSource.getMessage("anvizent.message.error.text.unabletodownloadFile", null, locale));
					   messages.add(message);
				   }
			   }
		   }catch(Exception e){
			   message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			   message.setText(messageSource.getMessage("anvizent.message.error.text.unableToProcessYourRequest", null, locale));
			   messages.add(message);
		   }
		   dataResponse.addMessages(messages);
		return dataResponse;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/executePreloadScripts", method=RequestMethod.POST)
	public DataResponse executePreloadScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DataValidation dataValidation, 
			  Locale locale, HttpServletRequest request){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		User user = CommonUtils.getUserDetails(request, null, null);
		try{
			if(dataValidation  != null){
				
				StringBuilder scriptsIds = new StringBuilder();
				dataValidation.getScriptIds().forEach(id -> {
					scriptsIds.append(",").append(id);
				});
				scriptsIds.deleteCharAt(0);
				LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
				
				map.add("scriptIds", scriptsIds.toString());
				map.add("connectionId", dataValidation.getIlConnection().getConnectionId());
				DataResponse validationResponse = restUtilities.postRestObject(request, "/getSelectedValidationByIds", map, user.getUserId());
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
				FileSettings fileSettings = (FileSettings) SessionHelper.getSesionAttribute(request, Constants.Config.FILE_SETTINGS_INFO);
				if(validationResponse != null && validationResponse.getHasMessages() 
						   && validationResponse.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
					
					List<LinkedHashMap<String, Object>> response = (List<LinkedHashMap<String, Object>>) validationResponse.getObject();
					List<DataValidation> dataValidationList = mapper.convertValue(response, new TypeReference<List<DataValidation>>() {});
					S3BucketInfo s3BucketInfo = getS3BucketInfo( request,clientId );//(S3BucketInfo) SessionHelper.getSesionAttribute(request,Constants.Config.S3_BUCKET_INFO);
					LinkedMultiValueMap<String, Object> executeParam = new LinkedMultiValueMap<>();
					ClientDBProcessor clientDBProcessor = new ClientDBProcessor(null, null, clientId, null);
					
				 for(SchemaDbVariables schemaDbVariables : dataValidation.getSchemaDbVariables()){
					DataValidationType dataValidationTypeJobs;
						for(DataValidation validation : dataValidationList){
							
							String protocal = validation.getIlConnection().getDatabase().getProtocal();
							String script  = validation.getValidationScripts();
							
							if(protocal.indexOf("sqlserver") != -1 || protocal.indexOf("odbc") != -1){
								script = script.replace(schemaDbVariables.getDbVariable(), schemaDbVariables.getDbValue());
								script = script.replace(schemaDbVariables.getSchemaVariable(), schemaDbVariables.getSchemaValue());
								validation.setValidationScripts(script);
							}else if(protocal.indexOf("mysql") != -1 || protocal.indexOf("oracle") != -1 || protocal.indexOf("db2") != -1 || protocal.indexOf("sforce") != -1 || 
									protocal.indexOf("as400") != -1  || protocal.indexOf("postgresql") != -1  || protocal.indexOf("vortex") != -1 || protocal.indexOf("openedge") != -1){
								
								script = script.replace(schemaDbVariables.getSchemaVariable(), schemaDbVariables.getDbValue());
								validation.setValidationScripts(script);
							}
							
							/**
							 * Here the dependency jars and the jobNames will be set to the validationType bean
							 */
							
							DataResponse validationTypeJobsandDependencyJars = restTemplateUtilities.getRestObject(request, "/getValidationTypeJobsandDependencyJars/{validationtypeId}",user.getUserId(),validation.getValidationTypeId());
							
							if(validationTypeJobsandDependencyJars != null && validationTypeJobsandDependencyJars.getHasMessages() 
									   && validationTypeJobsandDependencyJars.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
								LinkedHashMap<String, Object> validationTyperesponse = (LinkedHashMap<String, Object>) validationTypeJobsandDependencyJars.getObject();
								 dataValidationTypeJobs = mapper.convertValue(validationTyperesponse, new TypeReference<DataValidationType>() {
								});
								
							SourceFileInfo sourceFileInfo = uploadPreloadValidationFilesToS3(user.getClientId(), s3BucketInfo, clientDBProcessor,validation,
									                        deploymentType, fileSettings);
							executeParam.add("sourceFilePath", sourceFileInfo.getFilePath());
							executeParam.add("jobName", dataValidationTypeJobs.getJobName());
							executeParam.add("dependencyJar", dataValidationTypeJobs.getDependencyJars());
							executeParam.add("validationTypeId",dataValidationTypeJobs.getValidationTypeId());
							executeParam.add("scriptId", validation.getScriptId());
							executeParam.add("validationScript", validation.getValidationScripts());
							DataResponse executeJobsResponse = restUtilities.postRestObject(request, "/executePreLoadValidationScripts", executeParam, user.getUserId());
							 if(executeJobsResponse != null && executeJobsResponse.getHasMessages() 
									   && executeJobsResponse.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)){
								message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
								message.setText(executeJobsResponse.getMessages().get(0).getText());
							    messages.add(message);
							  }else {
								 message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
								 message.setText(executeJobsResponse.getMessages().get(0).getText());
								 messages.add(message);
							     }
							}
						}
					}
				}
			}
		   }catch(Throwable t){
		  	   message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			   message.setText(t.getMessage());
			   messages.add(message);
		    }
		   dataResponse.addMessages(messages);
		return dataResponse;
	}
	@SuppressWarnings({ "unchecked" })
	private S3BucketInfo getS3BucketInfo(HttpServletRequest request, String clientId) throws Exception
	{
		S3BucketInfo s3BucketInfo = null;
		DataResponse s3BucketInfoDataResponse = restUtilities.getRestObject(request, "/getS3BucketInfoFromClientId", clientId);
		if( s3BucketInfoDataResponse != null && s3BucketInfoDataResponse.getHasMessages() )
		{
			if( s3BucketInfoDataResponse.getMessages().get(0).getCode().equals("SUCCESS") )
			{
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) s3BucketInfoDataResponse.getObject();
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				s3BucketInfo = mapper.convertValue(map, new TypeReference<S3BucketInfo>()
				{
				});
			}
			else
			{
				throw new Exception("s3 bucket info not found for client id:" + clientId);
			}
		}
		return s3BucketInfo;
	}

	public static SourceFileInfo uploadPreloadValidationFilesToS3(String clientId, S3BucketInfo s3BucketInfo,
			ClientDBProcessor clientDBProcessor, DataValidation validation, String deploymentType, FileSettings fileSettings) {
		String tempPath = Constants.Temp.getTempFileDir();
		int recordsPerFile = 100;
		File tempFile = null;
		File localFile = null;
		SourceFileInfo sourceFileInfo = null;
		try {
			
			String dirPath = tempPath + "PreloadDataValidation/" + CommonUtils.generateUniqueIdWithTimestamp() + "/";
			/*LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String cTime = currentTime.format(formatter);
			s3FolderPath = clientId + "_Preload_DataValidation_" + cTime + "/";*/

			validation.getIlConnection().setServer(com.anvizent.encryptor.EncryptionServiceImpl.getInstance()
					.decrypt(validation.getIlConnection().getServer()));
			validation.getIlConnection().setUsername(com.anvizent.encryptor.EncryptionServiceImpl.getInstance()
					.decrypt(validation.getIlConnection().getUsername()));
			validation.getIlConnection().setPassword(com.anvizent.encryptor.EncryptionServiceImpl.getInstance()
					.decrypt(validation.getIlConnection().getPassword()));
			validation.getIlConnection().setConnectionType(com.anvizent.encryptor.EncryptionServiceImpl.getInstance()
					.decrypt(validation.getIlConnection().getConnectionType()));

			ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
			ilConnectionMapping.setiLId(validation.getIlId());
			ilConnectionMapping.setiLquery(validation.getValidationScripts());
			ilConnectionMapping.setiLConnection(validation.getIlConnection());
			ilConnectionMapping.setClientId(clientId);

			HashMap<String, Object> processedMap = clientDBProcessor.processClientDBData(tempPath, null,
					ilConnectionMapping, ilConnectionMapping.getiLConnection().getDatabase().getConnector_id(), false,
					recordsPerFile, false, recordsPerFile);

			Path tempFileName = (Path) processedMap.get("tempFile");
			tempFile = tempFileName.toFile();
			FileUtils.forceMkdir(new File(dirPath));
			localFile = new File(
					dirPath + File.separator + validation.getValidationName() + "_" + validation.getValidationId() + ".csv");
			IOUtils.copy(new FileInputStream(tempFile), new FileOutputStream(localFile));
			sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, localFile, clientId, 0, null, 0, deploymentType, "", 
					                                                false, fileSettings.getFileEncryption());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourceFileInfo;
	}
	
	@RequestMapping(value="/getDataValidationTypesByvalidationId/{id}",method=RequestMethod.GET)
	public DataResponse getDataValidationTypesByvalidationId(@PathVariable(Constants.PathVariables.CLIENT_ID)String clientId,@PathVariable("id")Integer id,HttpServletRequest request,Locale locale){
		User user = CommonUtils.getUserDetails(request, null, null);
		return restTemplateUtilities.getRestObject(request,"/getDataValidationTypesByvalidationId/{id}", user.getUserId(),id);
	}
	@RequestMapping(value="/getAllDataValidationTypes",method=RequestMethod.GET)
	public DataResponse getDataValidationTypes(@PathVariable(Constants.PathVariables.CLIENT_ID)String clientId,HttpServletRequest request,Locale locale){
		User user = CommonUtils.getUserDetails(request, null, null);
		return restTemplateUtilities.getRestObject(request,"/getAllDataValidationTypes", user.getUserId());
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/saveDataValidationTypes", method = RequestMethod.POST)
	public DataResponse saveDataValidationTypes(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("validationTypeId") Integer validationTypeId,
			@RequestParam("validationTypeName") String validationTypeName,
			@RequestParam("validationTypeDesc") String validationTypeDesc,
			@RequestParam("dataValidation_Select") int dataValidation_Select, @RequestParam("jobName") String jobName,
			@RequestParam(value = "jobFile", required = false) List<MultipartFile> jobFile,
			@RequestParam("uploadedJobFileNames") String uploadedJobFileNames, @RequestParam("active") boolean active,
			@RequestParam("contextParameters") List<String> contextParameters, HttpServletRequest request,
			Locale locale) {
		
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		boolean isUploaded = Boolean.FALSE;
		MultiValueMap<Object, Object> filesMap = new LinkedMultiValueMap<>();
		try {
			if (jobFile != null) {
				if (jobFile.size() > 0) {
					String isCommonJob = "yes";
					String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());
					for (MultipartFile file : jobFile) {
						String tempFileName = tempFolderName + file.getOriginalFilename();
						try {
							CommonUtils.createFile(tempFolderName, tempFileName, file);
						} catch (Exception e) {
							e.printStackTrace();
						}
						filesMap.add("files", new FileSystemResource(tempFileName));
						filesMap.add("isCommonJob", isCommonJob);
					}
					DataResponse uploadedFileResponse = restTemplateUtilities.postRestObject(request,
							"/uploadIlOrDlFiles", filesMap, user.getUserId());
					if (uploadedFileResponse.getHasMessages()
							&& uploadedFileResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						isUploaded = true;
					}
				}
			} else {
				isUploaded = false;
			}

		/*	if (isUploaded) {*/
				DataValidationType dataValidationType = new DataValidationType();
				dataValidationType.setActive(active);
				dataValidationType.setValidationTypeName(validationTypeName);
				dataValidationType.setValidationTypeDesc(validationTypeDesc);
				if (validationTypeId != null && validationTypeId > 0) {
					dataValidationType.setValidationTypeId(validationTypeId);
				} else {
					dataValidationType.setValidationTypeId(0);
				}
				dataValidationType.setJobName(jobName);
				dataValidationType.setDependencyJars(uploadedJobFileNames);
				dataValidationType.setValidationId(dataValidation_Select);
				dataValidationType.setContextParamList(contextParameters);
				return restTemplateUtilities.postRestObject(request, "/saveDataValidationTypes", dataValidationType,
						user.getUserId());
			/*}*/
		} catch (Exception e) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.addMessages(messages);
		}

		return dataResponse;
	}
	
	@RequestMapping(value="/getDataValidationTypeById/{id}",method=RequestMethod.GET)
	public DataResponse getDataValidationTypeById(@PathVariable(Constants.PathVariables.CLIENT_ID)String clientId,@PathVariable("id") int id,HttpServletRequest request,Locale locale){
		User user = CommonUtils.getUserDetails(request, null, null);
		return restTemplateUtilities.getRestObject(request,"/getDataValidationTypeById/{id}", user.getUserId(),id);
	}
	
	@RequestMapping(value="/getDatavalidation", method=RequestMethod.GET)
	public DataResponse getDataValidation(@PathVariable(Constants.PathVariables.CLIENT_ID)String clientId,HttpServletRequest request,HttpServletResponse response,Locale locale){
		User user = CommonUtils.getUserDetails(request, null, null);
		return restTemplateUtilities.getRestObject(request, "/getDatavalidation", user.getUserId());
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAvailableJarsList", method = RequestMethod.GET)
	public DataResponse getAvailableJarsList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request,Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		
		
		DataResponse contextParamsDataResponse = restTemplateUtilities.getRestObject(request, "/getContextParameters", user.getUserId());
		List<ContextParameter> contextParams = null;
		if(contextParamsDataResponse !=null && contextParamsDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
			
			List<LinkedHashMap<String, Object>> response = (List<LinkedHashMap<String, Object>>) contextParamsDataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			contextParams = mapper.convertValue(response, new TypeReference<List<ContextParameter>>() {
			});
		}
		
		DataResponse availableJarsDataResponse = adminRestUtilities.getRestObject(request, "/getAvailableJarsList",
				user.getUserId());
		
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		if (availableJarsDataResponse != null && availableJarsDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<Map<String, Object>> availableJarsList = (List<Map<String, Object>>) availableJarsDataResponse.getObject();
			availableJarsList = availableJarsList.stream().filter(jar -> jar.get("fileName").toString().startsWith("data"))
					.collect(Collectors.toList());
			
			DataResponse dataResponseParams = new DataResponse();
			
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponseParams.setMessages(messages);
			Map<String,Object> dataValidationTypePopupData = new LinkedHashMap<String,Object>();
			dataValidationTypePopupData.put("jarsList", availableJarsList);
			dataValidationTypePopupData.put("contextParamMap", contextParams);
			dataResponseParams.setObject(dataValidationTypePopupData);
			return dataResponseParams;
		}else{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.jarFilesAreNotAvailable", null, locale));
			messages.add(message);
			availableJarsDataResponse.setMessages(messages);
			return availableJarsDataResponse;
		}
	}
}
