package com.datamodel.anvizent.data.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.client.data.to.csv.path.converter.ClientDBProcessor;
import com.anvizent.client.data.to.csv.path.converter.exception.QueryParcingException;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mahender.alaveni
 *
 */
public class PackageUploadExecutor extends Thread {
	
	
	PackageExecution packageExecution;
	RestTemplateUtilities packageRestUtilities;
	CustomRequest customRequest ;
	String userId,encUserId, clientId, browserDetails, deploymentType;
	Log LOGGER;
	SimpleDateFormat startDateFormat;
	SimpleDateFormat sdf;
	boolean isWebApp;
	S3BucketInfo s3BucketInfo;
	String userName;
	Integer packageId;
    String csvFolderPath;
    FileSettings fileSettings;
    User user;
	boolean runOnlyDL = false;
    boolean runOnlyIL = false;
    Set<String> totalTablesSet = new HashSet<>();
    int totalRecordsProcessed = 0;
	int totalRecordsFailed = 0;
	int totalDuplicateRecordsFound = 0;
	int totalNoOfRecords = 0;
	int executionState = 1;
	MetaDataFetch metadataFetch;
	
    List<Map<String, Object>> incremtalUpdateList = new ArrayList<Map<String, Object>>();
     Map<String, Object>  havingSameColumsResultsMap = new HashMap<String, Object>();
	public PackageUploadExecutor(PackageExecution packageExecution, RestTemplateUtilities packageRestUtilities,  Log LOGGER, 
			String browserDetails, String deploymentType, S3BucketInfo s3BucketInfo,
			Integer packageId,String csvFolderPath,FileSettings fileSettings,User user,String webServiceContextUrl, MetaDataFetch metadataFetch) {
		this.packageExecution = packageExecution;
		this.packageRestUtilities = packageRestUtilities;
		this.clientId = user.getClientId();
		this.encUserId = user.getUserId();
		String user_id = "";
		this.LOGGER = LOGGER;
		this.browserDetails = browserDetails; 
		this.deploymentType = deploymentType;
		this.s3BucketInfo = s3BucketInfo;
		this.userName = user.getUserName();
		this.packageId=packageId;
		this.csvFolderPath=csvFolderPath;
		this.fileSettings=fileSettings;
		this.user=user;
		this.metadataFetch = metadataFetch;
		try {
			if ( this.s3BucketInfo == null ) {
				this.s3BucketInfo = new S3BucketInfo();
				this.s3BucketInfo.setId(0);
			}
			user_id = EncryptionServiceImpl.getInstance().decrypt(user.getUserId());
			String[] userIdAndLocale = StringUtils.split(user_id, '#');
			user_id = userIdAndLocale[0];
			
			String runType = packageExecution.getRunType();
			if (runType != null && runType.equalsIgnoreCase(Constants.jobType.DL)) {
				runOnlyDL = true;
			}
			if (runType != null && runType.equalsIgnoreCase(Constants.jobType.IL)) {
				runOnlyIL = true;
			}
			
		} catch (Exception e) {
			LOGGER.error("unable to decrypt userId :: " + e.getLocalizedMessage());
		}
		customRequest = new CustomRequest(webServiceContextUrl,clientId, clientId, browserDetails, deploymentType,null, encUserId);
		this.userId = user_id;
		startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Set<Integer> dLIds = new HashSet<Integer>();
		Package userPackage = null;
		StringBuilder dlExecutionMappingInfoComments = new StringBuilder();
		List<PackageExecutorMappingInfo> pkExecutorMappingInfoList = null;
		String executionStatus = Constants.ExecutionStatus.COMPLETED;
		try {
			
			DataResponse userPackageDataResponse = packageRestUtilities.getRestObject(customRequest,"/getPackagesById/{" + packageId + "}", encUserId, packageId);
			if (userPackageDataResponse != null && userPackageDataResponse.getHasMessages() && userPackageDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userPackageDataResponse.getObject();
				ObjectMapper packageMapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				userPackage = packageMapper.convertValue(map, new TypeReference<Package>() {
				});
	         
			if(userPackage.getIsStandard()){
			  //standard package start here
			   if(!runOnlyDL){
			     try{
					runNow();
					executionState = 2;
					updatePackageExecutionUploadInfo(Constants.ExecutionStatus.COMPLETED,"\n IL source files upload completed.");
				   }catch(PackageExecutionException e){LOGGER.error("error", e); 
						updatePackageExecutionUploadInfo(Constants.ExecutionStatus.FAILED,"\n"+e.getMessage());
					} 
				String executionStartedInfo = (!runOnlyIL) ? "IL source files & DL execution started.\n\n" : "IL source files execution started.\n\n";
				String executionCompletedInfo = (!runOnlyIL) ? "\n\nIL source files & DL execution completed." : "\n\nIL source files execution completed.";
				 
				if(packageExecution.isJobExecutionRequired()){
					executionState = 3;
				 updateExecutionStatus(Constants.ExecutionStatus.STARTED,executionStartedInfo);
				
				 DataResponse pkExecutorMappingInfoListDataResponse = packageRestUtilities.postRestObject(customRequest, "/getPackageExecutorSourceMappingInfoList",packageExecution,encUserId);
				 if(pkExecutorMappingInfoListDataResponse != null && pkExecutorMappingInfoListDataResponse.getMessages() != null){
					if(pkExecutorMappingInfoListDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
						List<?> list = (List<Map<String, Object>>) pkExecutorMappingInfoListDataResponse.getObject();
						ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						pkExecutorMappingInfoList = mapper.convertValue(list, new TypeReference<List<PackageExecutorMappingInfo>>() {});
						if(pkExecutorMappingInfoList != null && !pkExecutorMappingInfoList.isEmpty()){
						for(PackageExecutorMappingInfo packageExecutorMappingInfo : pkExecutorMappingInfoList){
							packageExecutorMappingInfo.setPackageExecution(packageExecution);
							DataResponse runIlDataSource = packageRestUtilities.postRestObject(customRequest, "/runIL",packageExecutorMappingInfo,encUserId);
							if(runIlDataSource != null && runIlDataSource.getMessages() != null){
								if(runIlDataSource.getMessages().get(0).getCode().equals("SUCCESS")){
									Map<String,Object> responseMap = (Map<String,Object>) runIlDataSource.getObject();
									totalTablesSet.add((String)(responseMap.get("iLTableName")));
									if (!runOnlyIL) {
										dLIds.add((Integer)(responseMap.get("dlId")));
									}
								}else{
									executionStatus=Constants.ExecutionStatus.FAILED;
									throw new PackageExecutionException("\n IL execution failed: "+runIlDataSource.getMessages().get(0).getText());
								}
							} 
						}
					  }else{
						  executionStatus=Constants.ExecutionStatus.FAILED;
						  throw new PackageExecutionException("\n IL execution failed.Unable to get executor mapping info list.");
					  }
					   if(!runOnlyIL){
						   if(dLIds != null && dLIds.size() > 0){
								 dlExecutionMappingInfoComments.append("\n DL execution started.");
								 dlExecutionMappingInfoComments.append(runDls(dLIds)); 
								 dlExecutionMappingInfoComments.append("\n DL execution completed.");
							 } 
					   }
					}else{
						executionStatus=Constants.ExecutionStatus.FAILED;
						throw new PackageExecutionException( " Package Executor source mapping not found for execution id : " + packageExecution.getExecutionId() +"with error: "+pkExecutorMappingInfoListDataResponse.getMessages().get(0).getText());
					}
				 } 
				 updatePackageExecutionStatusInfo(executionStatus,dlExecutionMappingInfoComments.toString()+executionCompletedInfo);
				 executionState = 4;
			    }
			   }else{
				   executionState = 3;
				   if(packageExecution.isJobExecutionRequired()){
				    updateExecutionStatus(Constants.ExecutionStatus.STARTED,"DL execution started.\n\n");
				   //get Dl id by package Id in il connection mapping
				   List<DLInfo> dlInfoList = null;
				   DataResponse dlListResponse = packageRestUtilities.postRestObject(customRequest, "/getILConnectionMappingDlInfoByPackageId",packageExecution,encUserId);
					if(dlListResponse != null && dlListResponse.getMessages() != null){
						if(dlListResponse.getMessages().get(0).getCode().equals("SUCCESS")){
							List<?> list = (List<Map<String, Object>>) dlListResponse.getObject();
							ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							dlInfoList = mapper.convertValue(list, new TypeReference<List<DLInfo>>() {});
							for(DLInfo dlInfo : dlInfoList){
								dLIds.add(dlInfo.getdL_id());
							}
							if(dLIds != null && dLIds.size() > 0){
								 dlExecutionMappingInfoComments.append(runDls(dLIds)); 
							 } 
						}else{
							executionStatus=Constants.ExecutionStatus.FAILED;
							throw new PackageExecutionException("DL execution failed: "+dlListResponse.getMessages().get(0).getText());
						}
					} 
					updatePackageExecutionStatusInfo(executionStatus,dlExecutionMappingInfoComments.toString()+"\n\nDL execution completed.");
			     }
				   executionState = 4;
			   }
			   if(packageExecution.isJobExecutionRequired()){
			   // update package schedule status
			    ClientData clientData = new ClientData();
			    userPackage.setIsScheduled(Boolean.TRUE);
			    userPackage.setScheduleStatus(Constants.Status.STATUS_DONE);
			    clientData.setUserPackage(userPackage);
			    packageRestUtilities.postRestObject(customRequest, "/updatePackageScheduleStatus",  clientData, encUserId);
			    
				// ws save or update IncrementalUpdate
			    if(incremtalUpdateList != null && incremtalUpdateList.size() > 0){
				    packageExecution.setIncremtalUpdateList(incremtalUpdateList);
	                packageRestUtilities.postRestObject(customRequest, "/saveOrUpdateIncrementalUpdate", packageExecution, encUserId);
			    }
			   }
			   }else{
				   //custom package start here
				   try{
					runNow();
					executionState = 2;
					updatePackageExecutionUploadInfo(Constants.ExecutionStatus.COMPLETED,"\n Source files upload completed.");
				   }catch(PackageExecutionException e){ 
						updatePackageExecutionUploadInfo(Constants.ExecutionStatus.FAILED,"\n"+e.getMessage());
					} 
				  
				if(packageExecution.isJobExecutionRequired()){
					updateExecutionStatus(Constants.ExecutionStatus.STARTED, "\n Execution started for custom package. \n Source files execution started.");
					executionState = 3;
					DataResponse pkExecutorMappingInfoListDataResponse = packageRestUtilities.postRestObject(customRequest, "/getPackageExecutorSourceMappingInfoList",packageExecution,encUserId);
					if(pkExecutorMappingInfoListDataResponse != null && pkExecutorMappingInfoListDataResponse.getMessages() != null){
						if(pkExecutorMappingInfoListDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
							List<?> list = (List<Map<String, Object>>) pkExecutorMappingInfoListDataResponse.getObject();
							ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							pkExecutorMappingInfoList = mapper.convertValue(list, new TypeReference<List<PackageExecutorMappingInfo>>() {});
							if(pkExecutorMappingInfoList != null && !pkExecutorMappingInfoList.isEmpty()){
							for(PackageExecutorMappingInfo packageExecutorMappingInfo : pkExecutorMappingInfoList){
								packageExecutorMappingInfo.setPackageExecution(packageExecution);
								DataResponse runCustomPackageDataSource = packageRestUtilities.postRestObject(customRequest, "/runCustomPackage",packageExecutorMappingInfo,encUserId);
								if(runCustomPackageDataSource != null && runCustomPackageDataSource.getMessages() != null){
									if(!runCustomPackageDataSource.getMessages().get(0).getCode().equals("SUCCESS")){  
										executionStatus=Constants.ExecutionStatus.FAILED;
									    throw new PackageExecutionException("\n"+runCustomPackageDataSource.getMessages().get(0).getText());
									}else{
										if(userPackage.getFilesHavingSameColumns()){
										Map<String,Object>  recordsProcessedMap =  (HashMap<String,Object>)runCustomPackageDataSource.getObject();
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
							}
						}else{
							 executionStatus=Constants.ExecutionStatus.FAILED;
							 throw new PackageExecutionException("\n Custom package execution failed.Unable to get executor mapping info list.");
						}
						 if(!userPackage.getFilesHavingSameColumns()) {
							DataResponse executeCustomPackageTargetTable = packageRestUtilities.postRestObject(customRequest, "/executeCustomPackageTargetTable",packageExecution,encUserId);
							if(executeCustomPackageTargetTable != null && executeCustomPackageTargetTable.getMessages() != null){
								if(executeCustomPackageTargetTable.getMessages().get(0).getCode().equals("SUCCESS")){  
									totalTablesSet.add(executeCustomPackageTargetTable.getObject().toString());
								}else{
									executionStatus=Constants.ExecutionStatus.FAILED;
									throw new PackageExecutionException("\n"+executeCustomPackageTargetTable.getMessages().get(0).getText());
								}
							} 
						  }else{
							  havingSameColumsResultsMap.put("totalRecordsProcessed", totalRecordsProcessed);
							  havingSameColumsResultsMap.put("totalDuplicateRecordsFound", totalDuplicateRecordsFound);
							  havingSameColumsResultsMap.put("totalRecordsFailed", totalRecordsFailed);
							  havingSameColumsResultsMap.put("totalNoOfRecords", totalNoOfRecords);
							  packageExecution.setHavingSameColumsResultsMap(havingSameColumsResultsMap);
							  packageRestUtilities.postRestObject(customRequest, "/updateTargetTableInfo",packageExecution,encUserId);
						  }
							DataResponse executeCustomPackageDerivedTables = packageRestUtilities.postRestObject(customRequest, "/executeCustomPackageDerivedTables",packageExecution,encUserId);
							if(executeCustomPackageDerivedTables != null && executeCustomPackageDerivedTables.getMessages() != null){
								if(executeCustomPackageDerivedTables.getMessages().get(0).getCode().equals("SUCCESS")){
									List<String> tableSet = (ArrayList<String>) executeCustomPackageDerivedTables.getObject();
									for(String table : tableSet){
										totalTablesSet.add(table);
									}
								} 
							} 
						
						}else{
							executionStatus=Constants.ExecutionStatus.FAILED;
							throw new PackageExecutionException(" Package Executor source Mapping not found for execution id : " + packageExecution.getExecutionId() + " error: " + pkExecutorMappingInfoListDataResponse.getMessages().get(0).getText());
						}
					 } 
					updatePackageExecutionStatusInfo(executionStatus,"\n Source files execution completed.\n Execution completed for custom package.");
					executionState = 4;
				 }
			   }
			 if(packageExecution.isJobExecutionRequired()){
				// fetch all dependency ddl s based on executed table list
			    if(totalTablesSet != null && totalTablesSet.size() > 0){
				List<String> totalTablesList = new ArrayList<>(totalTablesSet);
				packageExecution.setDerivedTablesList(totalTablesList);
				String runType = com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.RUNNOW;
				List<String> ddlsList = metadataFetch.runCustomDataSets(totalTablesList, runType, customRequest);
				//DataResponse ddlLayoutsResponce = packageRestUtilities.postRestObject(customRequest, "/runDDlayoutList", packageExecution, encUserId);
					if (ddlsList != null && ddlsList.size() > 0 ) {
						for(String ddlTable: ddlsList){
							totalTablesList.add(ddlTable);
						}
					}
				packageRestUtilities.postRestObject(customRequest, "/schedule/reloadUrlIntegration", packageExecution, encUserId);
				packageRestUtilities.postRestObject(customRequest, "/schedule/alertsAndThreshoulds", packageExecution, encUserId);
				packageRestUtilities.postRestObject(customRequest, "/schedule/druidIntegration", packageExecution, encUserId);
			   }
			 }
			}else{
				executionStatus=Constants.ExecutionStatus.FAILED;
				throw new PackageExecutionException("Package details not found for pakage id:"+packageId);
			}
			
		} catch(PackageExecutionException e){
			LOGGER.error("PackageExecutionException  ", e);
			executionStatus=Constants.ExecutionStatus.FAILED;
			updatePackageExecutionStatusInfo(executionStatus,"\n"+e.toString());
		} catch(Throwable e){ 
			executionStatus=Constants.ExecutionStatus.FAILED;
			updatePackageExecutionStatusInfo(executionStatus,"\n"+e.toString());
			LOGGER.error("Throwable  ", e); 
		} 

	}

@SuppressWarnings("unchecked")
String runDls(Set<Integer> dLIds) throws PackageExecutionException {
	 StringBuilder executionMappingInfoComments = new StringBuilder();
	 for(int dLId : dLIds){
			MultiValueMap<Object, Object> dlMap = new LinkedMultiValueMap<>();
			dlMap.add("dLId", dLId);
			dlMap.add("packageId",packageId);
			dlMap.add("executionId",packageExecution.getExecutionId());
			dlMap.add("timeZone",packageExecution.getTimeZone());
			DataResponse runDlDataSource = packageRestUtilities.postRestObject(customRequest, "/runDL",dlMap,encUserId);
			if(runDlDataSource != null && runDlDataSource.getMessages() != null){
				if(runDlDataSource.getMessages().get(0).getCode().equals("SUCCESS")){
					Map<String,Object> responseMap = (Map<String,Object>) runDlDataSource.getObject();
					executionMappingInfoComments.append((String)(responseMap.get("executionMappingInfoComments")));
					totalTablesSet.add((String)(responseMap.get("dlTableName")));
				}else{
					throw new PackageExecutionException("Dl execution failed for package id : " +  packageId + "dl id : "+dLId+"execution Id:"+packageExecution.getExecutionId()+" dataResponse " + runDlDataSource);
				}
			}
		}
	 return executionMappingInfoComments.toString();
	}
	
	@SuppressWarnings("unchecked")
	private void runNow() throws PackageExecutionException  {
		 
			SourceFileInfo sourceFileInfo = null;
			int source=0;
			DataResponse dataResponse = packageRestUtilities.getRestObject(customRequest, "/getILsConnectionMappingInfoByPackage/{" + packageId + "}",encUserId,packageId);
			List<?> l = (List<Map<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<ClientData> mappingInfoList = mapper.convertValue(l, new TypeReference<List<ClientData>>() {
			}); 
			if(mappingInfoList != null && mappingInfoList.size() > 0){
			String noOfSourcesString = " No Of Sources: " + mappingInfoList.size() +"\n";
			for (ClientData mappingInfo : mappingInfoList) {
				 source++;
				 StringBuilder sourceTypes = new StringBuilder(noOfSourcesString + " \n"+" "+TimeZoneDateHelper.getFormattedDateString()+" Source No "+ source +"\t Source Type: ");
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
					 sourceTypes.append("\n IL id: ").append(mappingInfo.getIlConnectionMapping().getiLId());
					 sourceTypes.append(" DL id: ").append(mappingInfo.getIlConnectionMapping().getdLId());
				 }
				 
				 updatePackageExecutionUploadInfo(Constants.ExecutionStatus.INPROGRESS,sourceTypes.toString());
				if (!mappingInfo.getIlConnectionMapping().getIsFlatFile() && !mappingInfo.getIlConnectionMapping().getIsWebservice()) {
					//data base start here
					if (mappingInfo.getIlConnectionMapping().getiLConnection() != null) {
					 fileUploadByClientApp(encUserId, userId, packageId, mappingInfo);
					//data base end here
					}else {
						throw new PackageExecutionException("Connection details not found for the source no:"+source+" for connection mapping id:"+mappingInfo.getIlConnectionMapping().getConnectionMappingId());
					 }
				}else if(mappingInfo.getIlConnectionMapping().getIsFlatFile()){
					try{
					//flat file start here
					sourceFileInfo = new SourceFileInfo();
					sourceFileInfo.setS3BucketInfo(s3BucketInfo);
					if(s3BucketInfo != null){
						sourceFileInfo.setS3BucketInfo(s3BucketInfo);
					}else{
						S3BucketInfo s3BucketInfo = new S3BucketInfo();
						s3BucketInfo.setId(0);
						sourceFileInfo.setS3BucketInfo(s3BucketInfo);
					}
					sourceFileInfo.setStorageType(mappingInfo.getIlConnectionMapping().getStorageType());
					sourceFileInfo.setPackageExecution(MinidwServiceUtil.getUploadStatus(packageExecution.getExecutionId(), Constants.ExecutionStatus.COMPLETED, "Uploaded successfully.",packageExecution.getTimeZone()));
					sourceFileInfo.setFilePath(mappingInfo.getIlConnectionMapping().getFilePath());
					sourceFileInfo.setIlConnectionMappingId(mappingInfo.getIlConnectionMapping().getConnectionMappingId());
					Modification modification = new Modification(new Date());
					modification.setCreatedBy(userName);
					sourceFileInfo.setSourceFileId(mappingInfo.getIlConnectionMapping().getSourceFileInfoId());
					ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
					Integer ilId = ilConnectionMapping.getiLId();
					 
					updatePackageExecutionUploadInfo(Constants.ExecutionStatus.INPROGRESS,"\n Flat file source \n IL id:"+ilId+"\n S3/Local file path is:"+mappingInfo.getIlConnectionMapping().getFilePath());
					updatePackageExecutorSourceMappingInfo(Constants.ExecutionStatus.COMPLETED,"Uploaded successfully.", sourceFileInfo);
					  
					//flat file end here
					}catch (AmazonS3Exception e ) {
						 throw new PackageExecutionException(e.getMessage());
						
					}
				}else if( mappingInfo.getIlConnectionMapping().getIsWebservice()){
					try{
					//web service start here
						if(s3BucketInfo != null){
							mappingInfo.getIlConnectionMapping().setS3BucketId(s3BucketInfo.getId());
						}else{
							mappingInfo.getIlConnectionMapping().setS3BucketId(0);
						}
						mappingInfo.getIlConnectionMapping().setPackageExecutionId(packageExecution.getExecutionId());
						mappingInfo.getIlConnectionMapping().setDeploymentType(deploymentType);
						mappingInfo.getIlConnectionMapping().setTimeZone(packageExecution.getTimeZone());
						mappingInfo.getIlConnectionMapping().setPackageExecutionId(packageExecution.getExecutionId());
					DataResponse wsFilePathDataResponse = packageRestUtilities.postRestObject(customRequest, "/schedule/wsUploadExecutor",mappingInfo,encUserId);
					if (wsFilePathDataResponse != null && wsFilePathDataResponse.getHasMessages() ) {
						 if(wsFilePathDataResponse.getMessages().get(0).getCode().equals("SUCCESS") && wsFilePathDataResponse.getObject() != null){
							       Map<String,Object> responseMap = (Map<String,Object>) wsFilePathDataResponse.getObject();
							       List<Map<String, Object>> incremtalList = (List<Map<String, Object>>) responseMap.get("incremtalUpdateList");
								   for(Map<String, Object> incremtalMap :incremtalList){
									   incremtalUpdateList.add(incremtalMap);
								   }
						  }else if(wsFilePathDataResponse.getMessages().get(0).getCode().equals("ERROR")){
							  throw new PackageExecutionException(wsFilePathDataResponse.getMessages().get(0).getText());
						  }
				 	 }
					//web service end here
				}catch (AmazonS3Exception e) {
					 throw new PackageExecutionException(e);
				}catch (Exception e) {
					 throw new PackageExecutionException(e);
				}
			 }
			}
			}else  {
				 throw new PackageExecutionException("Mapping info list not found for package id: "+packageId);
			}

	}
	@SuppressWarnings("unused")
	private void fileUploadByClientApp(String encryptedUserId, String userId, Integer packageId, ClientData mappingInfo) throws PackageExecutionException {
	    int limit = -1;
		File tempFile = null;
		String directoryPath = null;
		PackageExecution packExecution = null;
		StringBuilder uploadStatusInfo = new StringBuilder();
		SourceFileInfo sourceFileInfo = null;
		try {
			String query = mappingInfo.getIlConnectionMapping().getiLquery();
			if (StringUtils.isBlank(query)) { 
				throw new PackageExecutionException("Query Should Not be Empty Please Check The Source Queries.");
			}
			
			ClientDBProcessor clientDBProcessor = new ClientDBProcessor();
			boolean isIncrementalUpdate = mappingInfo.getIlConnectionMapping().getIsIncrementalUpdate();
			String incrementalUpdateDate = null;
			int iLId = mappingInfo.getIlConnectionMapping().getiLId();
			boolean isMultiPartEnabled = fileSettings.getMultiPartEnabled();
			boolean isEncryptionRequired = fileSettings.getFileEncryption();
			if (isMultiPartEnabled) {
				limit = fileSettings.getNoOfRecordsPerFile();//isMultiPartEnabled = false;
			}
			if ( isIncrementalUpdate ) {
				DataResponse data1 = packageRestUtilities.getRestObject(customRequest,  "/dateForIncrementalUpdateQuery?ilId={iLID}&dataSourceId={dataSourceID}&typeOfSource=database",
						DataResponse.class, encryptedUserId, iLId, mappingInfo.getIlConnectionMapping().getiLConnection().getConnectionId());
				incrementalUpdateDate = (String) data1.getObject();
				limit = -1;
			}
			
			ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
			String s3LogicalDirPath = "datafiles_U" + userId + "_P" + packageId + "_M" + mappingInfo.getIlConnectionMapping().getConnectionMappingId();
			Integer ilId = ilConnectionMapping.getiLId();
			if (ilId != null && ilId != 0) {
				s3LogicalDirPath += "_IL"+ilId + "_" +"DL"+ilConnectionMapping.getdLId() ;
			}
			s3LogicalDirPath += "_" + CommonUtils.generateUniqueIdWithTimestamp();		
			updatePackageExecutionUploadInfo(Constants.ExecutionStatus.INPROGRESS, " " + TimeZoneDateHelper.getFormattedDateString() + " Source data writing started ");
			HashMap<String, Object> processedMap = clientDBProcessor.processClientDBData(csvFolderPath, incrementalUpdateDate, mappingInfo.getIlConnectionMapping(), mappingInfo.getIlConnectionMapping().getiLConnection().getDatabase().getConnector_id(), isIncrementalUpdate, limit == -1? null:limit, isMultiPartEnabled, fileSettings.getNoOfRecordsPerFile());
			updatePackageExecutionUploadInfo(Constants.ExecutionStatus.INPROGRESS," " + TimeZoneDateHelper.getFormattedDateString() + " Source data writing completed ");
			Path tempDir = (Path) processedMap.get("tempDir");
			String maxDate = (String) processedMap.get("maxDate");
			int sourceCount = (int)processedMap.get("sourceCount");
			mappingInfo.getIlConnectionMapping().setClientId(userId);
			
			if(mappingInfo.getIlConnectionMapping().getiLConnection() != null){
				mappingInfo.getIlConnectionMapping().getiLConnection().setWebApp(isWebApp);
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
			     
			    //String startTime = MinidwServiceUtil.getCurrentTimeString(packageExecution.getTimeZone());
			    String startTime = TimeZoneDateHelper.getFormattedDateString();
				LOGGER.info("Database file uploading started " + startTime + " for IL mapping id " + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
				uploadStatusInfo = new StringBuilder();
				uploadStatusInfo.append("\n Database  file uploading started " + startTime + " for IL mapping id: " + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
				
				if(originalFile != null){
				LOGGER.info("deploymentType - > " + deploymentType);
				  sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, originalFile, userId, ilConnectionMapping.getPackageId() , user.getUserName(), ilConnectionMapping.getConnectionMappingId(), deploymentType,s3LogicalDirPath ,isMultiPartEnabled,isEncryptionRequired);
				}else{
					uploadStatusInfo.append("\n Database file is empty  for IL mapping id: " + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
				}
				//String endTime = MinidwServiceUtil.getCurrentTimeString(packageExecution.getTimeZone());
				String endTime  = TimeZoneDateHelper.getFormattedDateString();
				LOGGER.info("Database file uploading completed for " + endTime + " for IL mapping id " + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
				
				uploadStatusInfo.append("\n Database  file uploading completed for " + endTime + " for IL mapping id: " + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
				
				// added in /fileUploadByClientsApp
				int rowCount = 0;
				if (limit == -1) {
					//rowCount = CommonUtils.getRowCountOfQueryOrSP(mappingInfo.getIlConnectionMapping(), incrementalUpdateDate);
					sourceFileInfo.setRowCount(sourceCount);
					sourceFileInfo.setIncrementalDateValue(maxDate);
					sourceFileInfo.setIncrementalUpdate(true);
				}
				sourceFileInfo.setMultiPartFile(isMultiPartEnabled);
				if(s3BucketInfo != null){
					sourceFileInfo.setS3BucketInfo(s3BucketInfo);
				}else{
					S3BucketInfo s3BucketInfo = new S3BucketInfo();
					s3BucketInfo.setId(0);
					sourceFileInfo.setS3BucketInfo(s3BucketInfo);
				}
				sourceFileInfo.setStorageType(mappingInfo.getIlConnectionMapping().getStorageType());
				sourceFileInfo.setDelimeter(Constants.FileTypeDelimiter.CSV_DELIMITER);
				sourceFileInfo.setFileType(Constants.FileType.CSV);
				 
				DataResponse uploadDataResponse = updateSourceFileInfo(sourceFileInfo);
				if (uploadDataResponse != null && uploadDataResponse.getHasMessages() ) {
					 if(uploadDataResponse.getMessages().get(0).getCode().equals("SUCCESS") && uploadDataResponse.getObject() != null){
						 sourceFileInfo.setSourceFileId((Integer)uploadDataResponse.getObject());
						 uploadStatusInfo.append("\n Multipart enabled :"+isMultiPartEnabled+" \n Database source \n IL id :"+iLId+" \n S3 file path is:"+sourceFileInfo.getFilePath() +"\n file size: " + FileUtils.byteCountToDisplaySize(originalFile.length()));
				  } else {
					  uploadStatusInfo.append("Uploading failed.  " + uploadDataResponse.getMessages().get(0).getText() );  
				  }
				} else {
					uploadStatusInfo.append("Uploading failed. ");
				}
				updatePackageExecutionUploadInfo(Constants.ExecutionStatus.INPROGRESS,uploadStatusInfo.toString());
				updatePackageExecutorSourceMappingInfo(Constants.ExecutionStatus.COMPLETED,"Uploaded success fully", sourceFileInfo);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | QueryParcingException
				| SQLException | IOException | AmazonS3Exception | OnpremFileCopyException e) {
			 throw new PackageExecutionException(e);
			
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
	
	void updatePackageExecutionUploadInfo(String executionOrUploadStatus,String executionOrUploadComments){
		 PackageExecution packExecution = MinidwServiceUtil.getUploadStatus(packageExecution.getExecutionId(),executionOrUploadStatus, executionOrUploadComments,packageExecution.getTimeZone());
		 packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutionUploadInfo",  packExecution, encUserId);
	}
    void updatePackageExecutorSourceMappingInfo(String executionOrUploadStatus,String executionOrUploadComments,SourceFileInfo sourceFileInfo){
    	 sourceFileInfo.setPackageExecution(MinidwServiceUtil.getUploadStatus(packageExecution.getExecutionId(), executionOrUploadStatus,executionOrUploadComments,packageExecution.getTimeZone()));
	     packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutorSourceMappingInfo", sourceFileInfo, encUserId);
	}  
   DataResponse updateSourceFileInfo(SourceFileInfo sourceFileInfo){
	   return packageRestUtilities.postRestObject(customRequest, "/updateSourceFileInfo", sourceFileInfo, encUserId);
    }
   void updateExecutionStatus(String executionOrUploadStatus,String executionOrUploadComments){
	   PackageExecution packExecution = MinidwServiceUtil.getExecutionStatus(packageExecution.getExecutionId(),executionOrUploadStatus, executionOrUploadComments,packageExecution.getTimeZone());
	   packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutionStatus", packExecution, encUserId);
    } 
   void updatePackageExecutionStatusInfo(String executionOrUploadStatus,String executionOrUploadComments){
	   PackageExecution packExecution = MinidwServiceUtil.getExecutionStatus(packageExecution.getExecutionId(),executionOrUploadStatus, executionOrUploadComments,packageExecution.getTimeZone());
	   packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutionStatusInfo", packExecution, encUserId);
    }
    
   @Override
public void interrupt() {
	   if ( executionState == 1 ) {
		   updatePackageExecutionUploadInfo("TERMINATED","\n " + TimeZoneDateHelper.getFormattedDateString() + " Uploading terminated due application reload");
	   } else {
		   updatePackageExecutionStatusInfo("TERMINATED","\n " + TimeZoneDateHelper.getFormattedDateString() + " Execution terminated due application reload"); 
	   }
	   LOGGER.info("Package executor interupted");
	super.interrupt();
}
   
}
