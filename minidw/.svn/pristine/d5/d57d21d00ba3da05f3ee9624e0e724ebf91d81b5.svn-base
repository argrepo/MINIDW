package com.datamodel.anvizent.data.controller;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.MessageSource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.anvizent.client.data.to.csv.path.converter.ClientDBProcessor;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.HistoricalLoadForm;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RunHistoricalLoad extends Thread {

	boolean running = true;
	int historicalLoadId;
	CustomRequest customRequest ;
	RestTemplateUtilities packageRestUtilities;
	RestTemplateUtilities restUtilities;
	String userId, clientId, broweserDetails, deploymentType;
	Log logger;
	MessageSource messageSource;
	Locale locale;
	SimpleDateFormat startDateFormat;
	SimpleDateFormat sdf;
    boolean isWebApp;
    S3BucketInfo s3BucketInfo;
    FileSettings fileSettings;
    String userName;
	public RunHistoricalLoad(int historicalLoadId, RestTemplateUtilities packageRestUtilities, RestTemplateUtilities restUtilities, String userId, Log logger,
			MessageSource messageSource, Locale locale, String broweserDetails,String deploymentType,boolean isWebApp,S3BucketInfo s3BucketInfo,String userName,String clientId,String contextPath,FileSettings fileSettings) {
		this.historicalLoadId = historicalLoadId;
		this.packageRestUtilities = packageRestUtilities;
		this.restUtilities = restUtilities;
		this.messageSource = messageSource;
		this.clientId = userId;
		this.locale = locale;
		String user_id = "";
		this.logger = logger;
		this.broweserDetails = broweserDetails;
		this.deploymentType = deploymentType;
		this.isWebApp=isWebApp;
		this.s3BucketInfo=s3BucketInfo;
		this.userName=userName;
		this.fileSettings=fileSettings;
		try {
			user_id = EncryptionServiceImpl.getInstance().decrypt(userId);
			String[] userIdAndLocale = StringUtils.split(user_id, '#');
			user_id = userIdAndLocale[0];
		} catch (Exception e) {
			logger.error("unable to decrypt userId :: " + e.getLocalizedMessage());
		}
		customRequest = new CustomRequest(contextPath,clientId, clientId, broweserDetails, deploymentType, null);
		this.userId = user_id;
		startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", locale);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
		
	}

	@Override
	public void run() {
		
		try {
			DataResponse dataResponse;
			LinkedMultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
			form.add("historicalLoadId", historicalLoadId);
			form.add("runningStatus", running);
			
			dataResponse = restUtilities.postRestObject(customRequest, "/updateHistoricalLoadRunningStatus", form, clientId);
			logger.info("History load started for id : " + historicalLoadId + " dataResponse " + dataResponse);
			executeInterval();
			running = false;
			form = new LinkedMultiValueMap<String, Object>();
			form.add("historicalLoadId", historicalLoadId);
			form.add("runningStatus", running);
			dataResponse = restUtilities.postRestObject(customRequest, "/updateHistoricalLoadRunningStatus", form, clientId);
			logger.info("History load ended for id : " + historicalLoadId + " dataResponse " + dataResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void executeInterval() {
		if (running) {
			File tempFile = null;
			String directoryPath=null;
			File originalFile = null;
			try {
				MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
				map.add("loadId", historicalLoadId);
				DataResponse dataResponse = packageRestUtilities.postRestObject(customRequest, "/getHistoricalLoadDetailsByIdWithConnectionDetails", map, clientId);
				if (dataResponse != null && dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						@SuppressWarnings("unchecked")
						LinkedHashMap<String, Object> databaseobj = (LinkedHashMap<String, Object>) dataResponse.getObject();
						HistoricalLoadForm historicalLoadForm = mapper.convertValue(databaseobj, new TypeReference<HistoricalLoadForm>() {
						});

						if (historicalLoadForm != null && historicalLoadForm.getIlConnection() != null) {

							String query = historicalLoadForm.getHistiricalQueryScript();
							if (query.contains("/*")) {
								query = StringUtils.replace(StringUtils.replace(query, "/*", ""), "*/", "");
							}

							String startDate = historicalLoadForm.getStartDate();
							String toDate = historicalLoadForm.getEndDate();

							Date fromDateD = sdf.parse(startDate);
							Date toDateD = sdf.parse(toDate);
							if (fromDateD.compareTo(toDateD) > 0) {
								LinkedMultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
								form.add("historicalLoadId", historicalLoadId);
								form.add("endDate", toDate);
								form.add("ilId", historicalLoadForm.getIlId());
								form.add("connectionId", historicalLoadForm.getIlConnection().getConnectionId());
								restUtilities.postRestObject(customRequest, "/updateHistoricalLoadExecutionStatus", form, clientId);
								return;
							}

							if (query.contains("{fromDate}")) {
								query = query.replace("{fromDate}", "'" + startDate + "'");
							}
							if (query.contains("{toDate}")) {
								query = query.replace("{toDate}", "'" + toDate + "'");
							}
							ILConnectionMapping mapping = new ILConnectionMapping();
							mapping.setiLquery(query);
							historicalLoadForm.getIlConnection().setWebApp(isWebApp);
							mapping.setiLConnection(historicalLoadForm.getIlConnection());
							mapping.setClientId(userId);
							mapping.setTypeOfCommand("Query");
							mapping.setiLId(historicalLoadForm.getIlId());
							mapping.setdLId(0);
							mapping.setConnectionMappingId(historicalLoadId);
							mapping.setPackageId(0);
							
							String s3LogicalDirPath = "datafile_U" + mapping.getClientId() + "_HLID" + historicalLoadId +"_";
							Integer ilId = mapping.getiLId();
							if (ilId != null && ilId != 0) {
								s3LogicalDirPath += "IL"+ilId + "_" ;
							}
							s3LogicalDirPath += CommonUtils.generateUniqueIdWithTimestamp();
							ClientDBProcessor clientDBProcessor = new ClientDBProcessor();
							HashMap<String, Object> processedMap = clientDBProcessor.processClientDBData(Constants.Temp.getTempFileDir(), null, mapping, mapping.getiLConnection().getDatabase().getConnector_id(), false, null, historicalLoadForm.isMultipartEnabled(), (int)historicalLoadForm.getNoOfRecordsPerFile());
							Path tempFileName = (Path) processedMap.get("tempFile");
							Path tempDir = (Path) processedMap.get("tempDir");		
							
							if (historicalLoadForm.isMultipartEnabled()) {
								originalFile = tempDir.toFile();
								directoryPath = originalFile.getAbsolutePath();
							} else {
								tempFile = tempFileName.toFile();
								directoryPath = FilenameUtils.getFullPath(tempFile.getAbsolutePath());
								s3LogicalDirPath += ".csv";
								originalFile = new File(directoryPath + s3LogicalDirPath);
								tempFile.renameTo(originalFile);
							}
								
								int rowCount = clientDBProcessor.getSourceCount();//CommonUtils.getRowCountOfQueryOrSP(mapping,null);
								LinkedMultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
								boolean isEncryptionRequired = fileSettings.getFileEncryption();
								// upload to S3
								SourceFileInfo sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, originalFile, userId, 0, userName, null, deploymentType,s3LogicalDirPath ,historicalLoadForm.isMultipartEnabled(),isEncryptionRequired);	
								    form.add("filePath", sourceFileInfo.getFilePath());
									form.add("ilId", historicalLoadForm.getIlId()); 
									form.add("noOfRecords", rowCount);
									form.add("startDate", startDate);
									form.add("endDate", toDate);
									form.add("historicalLoadId", historicalLoadId);
									form.add("fileSize", clientDBProcessor.getSourceFileSize());
								String startTime = startDateFormat.format(new Date());
								logger.info("file uploading started " + startTime + " for load Id " + historicalLoadId);

								if (running) {
									DataResponse updateDataResponse = restUtilities.postRestObject(customRequest, "/updateHistoricalLoadData", form, clientId);
									if (updateDataResponse != null && updateDataResponse.getHasMessages()) {
										if (updateDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
											logger.info("History load interval completed");
											String endTime = startDateFormat.format(new Date());
											logger.info("file uploading completed for " + endTime + " for load Id " + historicalLoadId);
											executeInterval();
										} else {
											logger.info("History load error " + updateDataResponse.getMessages().get(0).getText());
										}

									} else {
										logger.error("interval failed");
									}
								}
								if (!deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
									try {
										FileUtils.forceDelete(new File(directoryPath));
									} catch (Exception e) {
										System.out.println("Unable to delete created csv for " + e.getMessage());
									}
								}

						} else {
							logger.error("Unable to fetch client db connection details for HistoricalLoad Id " + historicalLoadId);
						}

					} else {
						logger.error("error while fetching details " + dataResponse.getMessages().get(0).getText());
					}
				} else {
					logger.error(messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void terminate() {
		System.out.println("manually terminated for id " + historicalLoadId);
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

}
