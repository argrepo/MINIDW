package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.anvizent.encryptor.EncryptionUtility;
import com.anvizent.minidw.client.jdbc.utils.Constants;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.helper.ParseFileData;
import com.anvizent.minidw.service.utils.processor.S3FileProcessor;
import com.anvizent.minidw_druid__integration.DruidIntegration;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.MiniDwJobUtil;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.dao.ETLAdminDao;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.MiddleLevelManager;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController("anvizentFileUploadRestController")
@RequestMapping("/anvizentFileUpload")
@CrossOrigin
public class AnvizentFileUploadRestController {
	
private static final Log logger= LogFactory.getLog(AnvizentFileUploadRestController.class);

     private @Value("${anvizent.corews.api.url:}") String authenticationEndPointUrl;
     private @Value("${alerts.thresholds.url:}") String alertsThresholdsUrl;
     
     private static final String PRIVATE_KEY = "anvizent";
 	 private static final String IV = "AnvizentDMT IV16";
 	 

	@Autowired
	PackageService packageService;
	@Autowired
	FileDao fileDao;
	@Autowired
	ScheduleDao scheduleDao;
	@Autowired
	WebServiceDao webServiceDao;
	@Autowired
	FileService fileService;
	@Autowired
	UserDao userDao;
	@Autowired
	UserDetailsService userService;
	@Autowired
	PackageDao packageDao;
	@Autowired
	ETLAdminDao eTLAdminDao;
	
	@Autowired
	private MessageSource messageSource;
	
	AmazonS3 s3Client;
	
	@RequestMapping(value="/fileUpload", method=RequestMethod.POST, produces =  "application/json")
	public ResponseEntity<Object> fileUpload(
			   @RequestParam(value = "clientId") String clientId,
               @RequestParam(value = "tableName") String tableName,
			   @RequestParam(value = "file") MultipartFile multipartfile, 
			   @RequestParam(value= "operationType") String operationType,
			   @RequestParam(value= "charset", required=false) String charset,
			   HttpServletRequest httpServeletRequest,Locale locale){
		
		if ( StringUtils.isBlank(charset) ) {
			charset = "utf-8";
		}
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Map<String, Object> mapResponse = new LinkedHashMap<String, Object>();
		boolean isFileTypeValid=Boolean.FALSE;
		JdbcTemplate clientJdbcTemplate = new JdbcTemplate();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		File tempFile=null;
		String separatorChar=","; 
		String stringQuoteChar="\"";
		List<LinkedHashMap<String, Object>> fileDataMapList= new ArrayList<LinkedHashMap<String,Object>>();
		ParseFileData parsefileData = new ParseFileData();
		long filesize=0;
		String insertDateIntoTableName=null;
		List<String> ddlTableSet = new ArrayList<String>();
		String runType="Excel Upload";
		StringBuilder extraMessages = new StringBuilder();
		try{
			    ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				clientAppDbJdbcTemplate=clientJdbcInstance.getClientAppdbJdbcTemplate();
				
			if(multipartfile!=null && !multipartfile.isEmpty()){
				String[] ddlTables= tableName.split("\\&");
				if(ddlTables.length>0){
					insertDateIntoTableName=ddlTables[0];
					for(int i = 1; i <= ddlTables.length-1; i++){
						ddlTableSet.add(ddlTables[i]);
					}
					//clientJdbcTemplate  = getJdb();
					boolean isTableExists=fileService.isTableExists(clientId, null, insertDateIntoTableName, clientJdbcTemplate);
					if(isTableExists){
						List<Column> columns = packageService.getTableStructure(null, insertDateIntoTableName, 0, clientId, clientJdbcTemplate);
						tempFile = CommonUtils.multipartToFile(multipartfile);
						String fileName = multipartfile.getOriginalFilename();
						String filetype = FilenameUtils.getExtension(fileName);
						filesize=FileUtils.sizeOf(tempFile);
						isFileTypeValid= parsefileData.isValidFile(multipartfile);
						mapResponse.put("filename", fileName);
						mapResponse.put("filesize", FileUtils.byteCountToDisplaySize(filesize));
						if (isFileTypeValid) {
							List<String> headers = parsefileData.getHeadersFromFile(tempFile.getAbsolutePath(),
									filetype, separatorChar, stringQuoteChar, charset);
							if (headers.size() == columns.size()) {
								fileDataMapList = parsefileData.getDataFromFile(tempFile, filetype, separatorChar,
										stringQuoteChar, charset);
								if (operationType.equals("truncate_insert")) {
									fileService.truncateTable(null, insertDateIntoTableName, clientJdbcTemplate);
								}
								LinkedHashMap<String, Object> batchExecutionResponse = parsefileData
										.processFileDataMapList(clientJdbcTemplate, columns, insertDateIntoTableName,
												headers, fileDataMapList);
								int insertedCount = 0;
								if (batchExecutionResponse != null
										&& batchExecutionResponse.get("successrecords") != null) {
									insertedCount = Integer
											.parseInt(batchExecutionResponse.get("successrecords").toString());
								}
								/* ddl update in file upload */

								if (ddlTableSet.size() > 0) {

									if (insertedCount > 0) {
										User user = new User();
										user.setClientId(clientId);

										Modification modification = new Modification(new Date());
										modification.setCreatedBy(clientId);
										user.setModification(modification);

										DataResponse dataResponseDDl = MiniDwJobUtil.runCustomDataSetsForDDlRefresh(packageDao,
												ddlTableSet, clientJdbcTemplate, clientAppDbJdbcTemplate, fileDao,
												runType, user);
										mapResponse.put("ddlResponse", dataResponseDDl);
									} else {
										extraMessages.append("Success record not found to refresh DDL's");
									}

								} else {
									extraMessages.append("No DDL's processed/found");
								}

								if (batchExecutionResponse != null) {
									mapResponse.putAll(batchExecutionResponse);
									message.setCode("SUCCESS");
									message.setText(messageSource.getMessage("anvizent.message.text.parsedSuccessfully",
											null, locale));
									messages.add(0, message);
									dataResponse.setObject(mapResponse);
								} else {
									message.setCode("FAILED");
									message.setText(messageSource.getMessage("anvizent.message.text.internalError",
											null, locale));
									messages.add(message);
									dataResponse.setObject(mapResponse);
								}
							} else {
								message.setCode("FAILED");
								message.setText(messageSource.getMessage("anvizent.message.text.columnCountNotMatched",
										null, locale));
								messages.add(message);
							}
						}else{
							message.setCode("FAILED");
							message.setText("file type is not valid.");
							messages.add(message);
						}
					}else{
						message.setCode("FAILED");
						message.setText(messageSource.getMessage("anvizent.message.text.tableNotExists", null, locale));
						messages.add(message);
					}
				 
				 } else{
					    message.setCode("FAILED");
						message.setText("Table name not provided");
						messages.add(message);
				 }
				}else{
				message.setCode("FAILED");
				message.setText(messageSource.getMessage("anvizent.message.text.uploadedEmptyFile", null, locale));
				messages.add(message);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			String error= MinidwServiceUtil.getErrorMessageString(e);
			message.setCode("FAILED");
			message.setText(error);
			messages.add(message);
		}
		
		dataResponse.addMessages(messages);
		
		return new ResponseEntity<Object>(dataResponse, HttpStatus.OK);
	}
	
	public JdbcTemplate getJdb() {
		return new JdbcTemplate(createDataSource("jdbc:mysql://54.186.167.108:4475/altecdb_1009014", "altecuser", "user37"));
	}
	
	public BasicDataSource createDataSource(String url, String username, String password) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
		dataSource.setUrl(url);
		dataSource.setInitialSize(1);
		dataSource.setMaxActive(1);
		dataSource.setMinEvictableIdleTimeMillis(10);
		dataSource.setTestOnBorrow(true);
		dataSource.setTimeBetweenEvictionRunsMillis(10);
		dataSource.setMinIdle(0);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}
	
	@RequestMapping(value="/packageFileUploadandExecutor", method=RequestMethod.POST, produces =  "application/json")
	public ResponseEntity<Object> packageFileUploadandExecutor(
			@RequestParam(value="clientId") String clientId, 
			@RequestParam(value="packageId") String packageId,Locale locale){
		
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Package userPackage= null;
		Modification modification = null;
		String startDate = null;
		PackageExecution packageExecution =  new PackageExecution();
		Integer executionId = 0, scheduleId = 0;
		String deploymentType = new String();
		String status = null;
		StringBuilder uploadStatus = new StringBuilder();
		String timeZone =TimeZone.getDefault().getID();
		DataResponse spCpPackageUploadExecutorDs = null;
		User user= null;
			try{
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				clientAppDbJdbcTemplate=clientJdbcInstance.getClientAppdbJdbcTemplate();
				ClientJdbcInstance jdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails("-1"));
				JdbcTemplate commonJdbcTemplate = jdbcInstance.getClientAppdbJdbcTemplate();
				//deploymentType="cloud";
				if (deploymentType.equals(com.datamodel.anvizent.helper.minidw.Constants.Config.DEPLOYMENT_TYPE_CLOUD)) {
						// File upload start
						int convertPackageId =Integer.parseInt(packageId);
						try{
							userPackage = packageService.getPackageWithoutUserId(convertPackageId, clientAppDbJdbcTemplate);	
							
							S3BucketInfo s3BucketInfo = userDao.getS3BucketInfoByClientId(clientId, clientAppDbJdbcTemplate);
							
							 user = userDao.getUser(userPackage.getUserId(), clientAppDbJdbcTemplate);
							
							modification = new Modification(new Date());
							modification.setCreatedBy(user.getUserName());
							modification.setModifiedBy(user.getUserName());
							
							startDate = TimeZoneDateHelper.getFormattedDateString();
							
							packageExecution.setPackageId((long) convertPackageId);
							packageExecution.setScheduleId(scheduleId);
							packageExecution.setInitiatedFrom(com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.RUNNOW);
							packageExecution.setRunType("all");
							packageExecution.setUploadStatus(com.datamodel.anvizent.helper.minidw.Constants.ExecutionStatus.STARTED);
							packageExecution.setUploadStartDate(startDate);
							packageExecution.setTimeZone(timeZone);
							packageExecution.setJobExecutionRequired(true);
							packageExecution.setModification(modification);
							packageExecution.setUploadComments("Source files upload started.");
							
							executionId = scheduleDao.savePackageExectionInfo(packageExecution, clientAppDbJdbcTemplate);
							if(executionId != 0){
								packageExecution.setExecutionId(executionId);
								
								MiniDwJobUtil.runAllSources((long) executionId, userPackage, fileDao, scheduleDao, packageDao, user,
										clientJdbcInstance, deploymentType, packageExecution.getTimeZone(), webServiceDao,
										s3BucketInfo, commonJdbcTemplate,packageExecution);
							}
							status=com.datamodel.anvizent.helper.minidw.Constants.ExecutionStatus.COMPLETED;
						
					} catch (PackageExecutionException pe) {
						logger.error("error while packageUploadExecutor() ", pe);
						uploadStatus.append(MinidwServiceUtil.getErrorMessageString(pe));
						status = com.datamodel.anvizent.helper.minidw.Constants.ExecutionStatus.FAILED;
						message.setCode(status);
						message.setText(uploadStatus.toString());
						messages.add(message);
					} catch (Throwable e) {
						logger.error("error while packageUploadExecutor() ", e);
						uploadStatus.append(MinidwServiceUtil.getErrorMessageString(e));
						status = com.datamodel.anvizent.helper.minidw.Constants.ExecutionStatus.FAILED;
						message.setCode(status);
						message.setText(uploadStatus.toString());
						messages.add(message);
					}
					packageExecution = MinidwServiceUtil.getUploadStatus(executionId, status, "\n" + uploadStatus.toString() + "\n Source files upload completed.", timeZone );
					packageExecution.setModification(modification);
					scheduleDao.updatePackageExecutionUploadInfo(packageExecution, clientAppDbJdbcTemplate);
				
				// package Executor Start
				
					try{
						if (userPackage.getIsStandard()) {
							spCpPackageUploadExecutorDs = MiniDwJobUtil.spPackageUploadExecutor(packageExecution,
									packageExecution.getExecutionId(), userPackage, fileDao, scheduleDao, packageDao, user,
									clientJdbcInstance, deploymentType, packageExecution.getTimeZone(), userDao, webServiceDao);
						} else {
							spCpPackageUploadExecutorDs = MiniDwJobUtil.cpPackageUploadExecutor(
									packageExecution.getExecutionId(), userPackage, fileDao, scheduleDao, packageDao, user,
									clientJdbcInstance, deploymentType, packageExecution.getTimeZone(), userDao);
							uploadStatus.append("\n Execution completed for custom package.\n source files execution completed.");
						} 
						
						if (spCpPackageUploadExecutorDs != null && spCpPackageUploadExecutorDs.getHasMessages()) {
							if (spCpPackageUploadExecutorDs.getMessages().get(0).getCode().equals("SUCCESS")
									&& spCpPackageUploadExecutorDs.getObject() != null) {
								@SuppressWarnings("unchecked")
								Map<String, Object> responseMap = (Map<String, Object>) spCpPackageUploadExecutorDs.getObject();
								@SuppressWarnings("unchecked")
								Set<String> tableSet = (HashSet<String>) responseMap.get("totalTablesSet");
								List<String> tableList = new ArrayList<String>(tableSet);
								String runType = com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.RUNNOW;
								MiniDwJobUtil.runCustomDataSets(packageDao, tableList, user, clientJdbcTemplate,
										clientAppDbJdbcTemplate, fileDao, modification,runType);
								uploadStatus.append("\n run ddl successfully.");
			
								clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
								Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
								String schemaName = clientDbDetails.get("clientdb_schema").toString();
			
								MiniDwJobUtil.reloadUrl(user.getUserId(), tableList, user.getClientId(), schemaName,
										clientAppDbJdbcTemplate, authenticationEndPointUrl);
								uploadStatus.append("\n reload Url updated successfully.");
			
								druidIntegration(user.getClientId(), user.getUserId(), tableList, clientAppDbJdbcTemplate,
										packageExecution.getExecutionId(), packageExecution.getTimeZone(), scheduleDao, userDao,
										eTLAdminDao, commonJdbcTemplate);
								uploadStatus.append("\n druid Integration   updated successfully.");
			
								if ( StringUtils.isNotBlank(alertsThresholdsUrl)) {
									MiniDwJobUtil.alertsAndThreshoulds(user.getClientId(), tableList,
											packageExecution.getPackageId() + "", alertsThresholdsUrl);
									uploadStatus.append("\n alerts And Threshoulds updated successfully.");
								}
								status = com.datamodel.anvizent.helper.minidw.Constants.ExecutionStatus.COMPLETED;
							} else {
								status = com.datamodel.anvizent.helper.minidw.Constants.ExecutionStatus.FAILED;
							}
						}
						message.setCode(status);
						message.setText(uploadStatus.toString());
						messages.add(message);
					}catch (Throwable e) {
						Message msg = MinidwServiceUtil.getErrorMessage(e);
						uploadStatus.append(msg.getText());
						status = com.datamodel.anvizent.helper.minidw.Constants.ExecutionStatus.FAILED;
						message.setCode(status);
						message.setText(uploadStatus.toString());
						messages.add(message);
					}
				}else{
					message.setCode("FAILED");
					message.setText(messageSource.getMessage("anvizent.message.text.deploymentTypeIsNotOnCloud", null, locale));
					messages.add(message);
				}
		}catch(Exception e){
			e.printStackTrace();
			String error= MinidwServiceUtil.getErrorMessageString(e);
			message.setCode("FAILED");
			message.setText(error);
			messages.add(message);
		}
			dataResponse.addMessages(messages);
		
		return new ResponseEntity<Object>(dataResponse,HttpStatus.OK);
	}
	
	public static void druidIntegration(String clientIdfromHeader, String userId, List<String> totalTablesList,
			JdbcTemplate clientAppDbJdbcTemplate, long executionId, String timeZone, ScheduleDao scheduleDao,
			UserDao userDao, ETLAdminDao eTLAdminDao, JdbcTemplate commonJdbcTemplate) {

		Modification modification = null;
		MiddleLevelManager middleLevelManager = null;

		try {

			CloudClient cloudClient = userDao.getClientDetails(clientIdfromHeader);
			timeZone = StringUtils.isBlank(timeZone) ? TimeZone.getDefault().getID() : timeZone;

			modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			modification.setModifiedBy(userId);

			if (cloudClient.isDruidEnabled()) {

				middleLevelManager = eTLAdminDao.getMiddleLevelManagerDetailsById(commonJdbcTemplate);
				DruidIntegration druidIntegration = new DruidIntegration(totalTablesList, userId, middleLevelManager,
						clientIdfromHeader, scheduleDao, modification, executionId, timeZone, clientAppDbJdbcTemplate);
				druidIntegration.start();

			} else {
				PackageExecution packExecutionAfter = MinidwServiceUtil.getDruidStatusAndComments(executionId,
						com.datamodel.anvizent.helper.minidw.Constants.ExecutionStatus.IGNORED,
						"Druid is not not enabled this client id:" + clientIdfromHeader, timeZone);
				packExecutionAfter.setModification(modification);
				scheduleDao.updateDruidStartInfo(packExecutionAfter, clientAppDbJdbcTemplate);
			}
		} catch (Throwable t) {
			PackageExecution packExecutionAfter = MinidwServiceUtil.getDruidStatusAndComments(executionId,
					com.datamodel.anvizent.helper.minidw.Constants.ExecutionStatus.FAILED, "\n" + t.getMessage(), timeZone);
			packExecutionAfter.setModification(modification);
			scheduleDao.updateDruidEndInfo(packExecutionAfter, clientAppDbJdbcTemplate);
		}

	}
	
	@RequestMapping(value="/packageExecutorExceptionInfo", method=RequestMethod.GET, produces =  "application/json")
	public ResponseEntity<Object> packageExecutorExceptionInfo(@RequestParam(value="accessToken") String accessToken, Locale locale){
		DataResponse reponse = new DataResponse();
	    EncryptionUtility encryptionUtility;
	    JdbcTemplate clientAppDbJdbcTemplate= null;
	    Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Package userPackage = null;
		PackageExecution packageExecution = null;
		int sourceCount=0;
		int incrementalLoads =0;
		//String executionComments =null;
		try{
			System.out.println(accessToken);
			
			 encryptionUtility = new EncryptionUtility(PRIVATE_KEY, IV);
			 String decryptvalue = encryptionUtility.decrypt(accessToken);
			 logger.info(decryptvalue);
			 ObjectMapper mapper = new ObjectMapper();
			 Map<String, Object> data = new HashMap<>();
			 data= mapper.readValue(decryptvalue, new TypeReference<HashMap<String,Object>>() {});
			 
			 String clientId=(String) data.get("clientId");
			 String packageId=(String) data.get("packageId");
			 String executionId=(String) data.get("executionId");
			 String uploadorExecution =(String) data.get("executionorupload");
			 
			 ClientJdbcInstance clientJdbcInstance= userService.getClientJdbcInstance(clientId);
			 clientAppDbJdbcTemplate= clientJdbcInstance.getClientAppdbJdbcTemplate();
			 userPackage = packageService.getPackageWithoutUserId(Integer.valueOf(packageId), clientAppDbJdbcTemplate);
			 sourceCount = packageService.getSourceCountByPackageId(Integer.valueOf(packageId), Integer.valueOf(userPackage.getUserId()), clientAppDbJdbcTemplate);
			 userPackage.setSourceCount(sourceCount);
			 incrementalLoads = packageService.getIncrementalLoadCount(Integer.valueOf(packageId), Integer.valueOf(executionId), clientAppDbJdbcTemplate);
			 packageExecution = packageService.getPackageExecutionInfo(Integer.valueOf(packageId),Integer.valueOf(executionId),uploadorExecution,clientAppDbJdbcTemplate);
			 packageExecution.setIncrementalLoadCount(incrementalLoads);
			 packageExecution.setUserPackage(userPackage);
			/* if(!uploadorExecution.equals("all")){
					executionComments = packageService.getUploadAndExecutionStatusComments(Long.parseLong(executionId), uploadorExecution,clientAppDbJdbcTemplate);
				}else{
					executionComments = packageService.getUploadStatusAndExecutionStatusComments(Long.parseLong(executionId),clientAppDbJdbcTemplate);	
				}*/
			 if(packageExecution != null){
				    message.setCode("SUCCESS");
					message.setText(messageSource.getMessage("anvizent.message.text.successfullyLoadedPacakgeExecutionDetails", null, locale));
					messages.add(message);
			 }
			 reponse.setObject(packageExecution);
			 reponse.setMessages(messages);
		}catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch(Throwable e){
			message.setCode("FAILED");
			message.setText(messageSource.getMessage("anvizent.message.text.failedtoloadPackageExecutiondetails", null, locale));
			messages.add(message);
			e.printStackTrace();
		}
		 return new ResponseEntity<Object>(reponse,HttpStatus.OK);
	}
	
	
	@SuppressWarnings("null")
	@RequestMapping(value="/s3fileDownload", method=RequestMethod.POST, produces =  "application/json")
	public ResponseEntity<Object> s3fileDownload(@RequestParam(value="secretKey") String secretKey, @RequestParam(value="accessKey") String accessKey, 
			                      @RequestParam(value="filepath") String filepath, @RequestParam(value="bucketName") String bucketName, Locale locale){
		 DataResponse reponse=null;
		 Message message = new Message();
		 List<Message> messages = new ArrayList<>();
		 String downloadedFilePath = null;
		 try{
			 s3Client = getAmazonS3Client(accessKey, secretKey);
			 downloadedFilePath = downloadFileFromS3(filepath, bucketName, true);
			 if(downloadedFilePath !=null){
				 message.setCode("SUCCESS");
				 message.setText("filepath:"+downloadedFilePath);
				 messages.add(message);
			 }
			 else{
				 throw new Exception("unable to download the file from s3");
			 }
		 }catch(Throwable e){
			 message.setCode("FAILED");
			 message.setText(e.getMessage());
			 messages.add(message);
		 }
		    reponse.addMessages(messages);
		
		//s3Client = getAmazonS3Client("AKIAJ7DXBCEPL3EYAIKQ", "7R4nD0jJzxllmfKU758IACXLH6kZTpRqySZll73X");
		//downloadFileFromS3("P1HAe0q-mtP_GziLitT5RrMP2gdX6WGNYwvp68D84vooPurxiIOSZsTFljpBzqqajgb5HGCVPbxnceUVMTiVcEaHGCvMjMz36yO1Of46QBM", true);
		
		return new ResponseEntity<Object>(reponse,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/downloadFileFromS3", method=RequestMethod.POST, produces =  "application/json")
	public ResponseEntity<Object> downloadFileFromS3(
			                      @RequestParam(value="secretKey") String secretKey, 
			                      @RequestParam(value="accessKey") String accessKey, 
			                      @RequestParam(value="filepath") String filepath,
			                      @RequestParam(value="bucketName") String bucketName,
			                      @RequestParam(value="multiPartEnabled") Boolean multiPartEnabled,
			                      @RequestParam(value="encryptionRequired") Boolean encryptionRequired,
			                      Locale locale){
		DataResponse reponse = new DataResponse();
		 Message message = new Message();
		 List<Message> messages = new ArrayList<>();
		 try{
			 
			 S3FileProcessor s3FileProcessor = new S3FileProcessor();
			 
			 S3BucketInfo s3BucketInfo = new S3BucketInfo();
			 
			 s3BucketInfo.setBucketName(bucketName);
			 s3BucketInfo.setAccessKey(accessKey);
			 s3BucketInfo.setSecretKey(secretKey);
			 
			 List<String> filesPaths = s3FileProcessor.downloadFilesFromS3(filepath, null,null,multiPartEnabled,encryptionRequired, s3BucketInfo);
			 
			 
			 if(filesPaths != null && filesPaths.size() > 0){
				 message.setCode("SUCCESS");
				 message.setText("filepath:"+filesPaths);
				 messages.add(message);
			 }
			 else{
				 message.setCode("FAILED");
				 message.setText("unable to download the file from s3");
				 messages.add(message);
				 throw new Exception("unable to download the file from s3");
			 }
		 }catch(Throwable e){
			 message.setCode("FAILED");
			 message.setText(e.getMessage());
			 messages.add(message);
		 }
		    reponse.addMessages(messages);
		
		return new ResponseEntity<Object>(reponse,HttpStatus.OK);
	}
	
	
	/*@SuppressWarnings("null")
	@RequestMapping(value="/s3fileDownloadNew", method=RequestMethod.POST, produces =  "application/json")
	public ResponseEntity<Object> s3fileDownload(@RequestParam(value="secretKey") String secretKey, Locale locale){
		 DataResponse reponse=null;
		 Message message = new Message();
		 List<Message> messages = new ArrayList<>();
		 String downloadedFilePath = null;
		 try{
			 
			 if(downloadedFilePath !=null){
				 message.setCode("SUCCESS");
				 message.setText("filepath:"+downloadedFilePath);
				 messages.add(message);
			 }
			 else{
				 throw new Exception("unable to download the file from s3");
			 }
		 }catch(Throwable e){
			 message.setCode("FAILED");
			 message.setText(e.getMessage());
			 messages.add(message);
		 }
		    reponse.addMessages(messages);
		
		//s3Client = getAmazonS3Client("AKIAJ7DXBCEPL3EYAIKQ", "7R4nD0jJzxllmfKU758IACXLH6kZTpRqySZll73X");
		//downloadFileFromS3("P1HAe0q-mtP_GziLitT5RrMP2gdX6WGNYwvp68D84vooPurxiIOSZsTFljpBzqqajgb5HGCVPbxnceUVMTiVcEaHGCvMjMz36yO1Of46QBM", true);
		
		return new ResponseEntity<Object>(reponse,HttpStatus.OK);
	}*/
	public String downloadFileFromS3(String filePathInS3, String bucketName, boolean isEncryptionEnabled) throws AmazonS3Exception {
		File encrtptedS3File = null;
		File decryptedFile = null;
		File tempDecryptedFile = null;
		String downloadFilePath = null;
		EncryptionUtility encryptionUtility;
		try {
			encrtptedS3File = downloadS3File(filePathInS3, bucketName);
			String encryptedfileName = encrtptedS3File.getName();
			encryptionUtility = new EncryptionUtility(PRIVATE_KEY, IV);
			String decryptedFileName = encryptionUtility.decrypt(encryptedfileName);
			if (StringUtils.isNotBlank(decryptedFileName)) {
				String downloadFileDir = com.datamodel.anvizent.helper.Constants.Temp.getTempFileDir() + "decryptedFiles/";
				FileUtils.forceMkdir(new File(downloadFileDir));
				decryptedFile = new File(downloadFileDir + decryptedFileName);
				logger.debug("Is dyscryption enabled ? " + isEncryptionEnabled);
				String dir = com.datamodel.anvizent.helper.Constants.Temp.getTempFileDir() + "downloadedFiles/";
				FileUtils.forceMkdir(new File(dir));
				String destinationFilePath = dir + FilenameUtils.getName(encrtptedS3File.getAbsolutePath());
				tempDecryptedFile = encryptionUtility.decryptFile(encrtptedS3File, destinationFilePath);
				if (tempDecryptedFile != null) {
					FileUtils.copyFile(tempDecryptedFile, decryptedFile);
				}
				downloadFilePath = downloadFileDir + decryptedFileName;
			}
		} catch (Exception e) {
			throw new AmazonS3Exception(e.getMessage(), e);
		} finally {
			deleteQuietly(encrtptedS3File);
			deleteQuietly(tempDecryptedFile);
		}
		return downloadFilePath;
	}
	
	public static boolean deleteQuietly(File file) {
		if (file == null) {
			return false;
		}
		try {
			if (file.isDirectory()) {
				FileUtils.cleanDirectory(file);
			}
		} catch (Exception ignored) {
		}

		try {
			return file.delete();
		} catch (Exception ignored) {
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	private AmazonS3 getAmazonS3Client(String accessKey, String secretKey) {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		return new AmazonS3Client(credentials);
	}
	
	private File downloadS3File(String filePathInS3, String bucketName) throws IOException {

		FileOutputStream outstream = null;
		File encrtptedFile = null;
		try {
			// Step 1 : download the encrypted file

              logger.info("downloading started from S3..." + filePathInS3);
			S3Object obj = s3Client.getObject(new GetObjectRequest(bucketName, filePathInS3));
			String dir = com.datamodel.anvizent.helper.Constants.Temp.getTempFileDir();
			FileUtils.forceMkdir(new File(dir));
			String tempFileName = obj.getKey();
			if (obj.getKey().indexOf("/") != -1) {
				tempFileName = StringUtils.substringAfterLast(obj.getKey(), "/");
			}

			encrtptedFile = new File(dir + tempFileName);
			outstream = new FileOutputStream(encrtptedFile);
			logger.debug("copying stream into temp file...");
			IOUtils.copy(obj.getObjectContent(), outstream);
			logger.debug("Download ...");
		} finally {
			if (outstream != null) {
				outstream.close();
			}
		}
		return encrtptedFile;
	}
	
	@RequestMapping(value="/anvizentClientList", method = RequestMethod.GET)
	public ResponseEntity<List<Integer>> clientList( HttpServletRequest request, Locale locale ){
		List<Integer>  clientList = null;
		try {
			clientList = userDao.getClientIds(null);
		}catch(Throwable e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Integer>>(clientList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/insertClientJobUploadandExecutionProperty", method =  RequestMethod.POST, produces =  "application/json")
	public ResponseEntity<String> insertClientJobUploadandExecutionLinitProperty(@RequestBody List<Integer> clientIds, HttpServletRequest request, Locale locale){
		Map<Integer, Boolean> propertyInsertStatus = new HashMap<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			for(Integer id: clientIds) {
				try{
					ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(id+"");
					clientAppDbJdbcTemplate=clientJdbcInstance.getClientAppdbJdbcTemplate();
					boolean status = userDao.insertClientJobUploadandExecutionLimitProperty(id, clientAppDbJdbcTemplate);
					propertyInsertStatus.put(id, status);
				}catch(Exception e){
					e.printStackTrace();
					propertyInsertStatus.put(id, false);
				}
			}
		}catch(Throwable e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(propertyInsertStatus.toString(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/deleteClientJobUploadandExecutionProperty", method =  RequestMethod.POST, produces =  "application/json")
	public ResponseEntity<String> deleteClientJobUploadandExecutionLinitProperty(@RequestBody List<Integer> clientIds, HttpServletRequest request, Locale locale){
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			for(Integer id: clientIds) {
				try{
					ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(id+"");
					if(clientJdbcInstance != null) {
						logger.info("Client Id:::: " + id);
						clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
					}
					userDao.deleteClientJobUploadandExecutionLinitProperty(id, clientAppDbJdbcTemplate);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Throwable e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	

}
