package com.datamodel.anvizent.helper.minidw;

import static com.anvizent.minidw.service.utils.MinidwServiceUtil.isValidateDataResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.anvizent.minidw.service.utils.processor.FlatFileProcessor;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.common.exception.UploadandExecutionFailedException;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CrossReferenceLogs;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "restApiMetaDataService")
public class RestApiMetaDataService implements MetaDataFetch {
	protected static final Log log = LogFactory.getLog(FlatFileProcessor.class);

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("scheduleServicesRestTemplateUtilities")
	private RestTemplateUtilities scheduleRestUtilities;
	
	@Autowired
	@Qualifier("crossReferenceRestTemplateUtilities")
	private RestTemplateUtilities crossReferenceRestUtilities;

	@Autowired
	CommonProcessor commonProcessor;

	@Override
	public PackageExecution saveUploadInfo(PackageExecution pExecution, CustomRequest customRequest) {
		try {
			DataResponse packageExectionDataResponse = scheduleRestUtilities.postRestObject(customRequest,
					"/savePackageExectionInfo", pExecution, customRequest.getEncUserId());
			if (packageExectionDataResponse != null && packageExectionDataResponse.getHasMessages()) {
				if (packageExectionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					Integer executionId = (Integer) packageExectionDataResponse.getObject();
					pExecution.setExecutionId(executionId);
				}
			}
		} catch (Throwable t) {
			throw new UploadandExecutionFailedException("\n unable to save execution info while file uploading");
		}
		return pExecution;
	}

	@Override
	public void updateUploadInfo(PackageExecution packExecution, CustomRequest customRequest) {
		DataResponse response = new DataResponse();
		try {
			response = packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutionUploadInfo",
					packExecution, customRequest.getEncUserId());
			if (!isValidateDataResponse(response)) {
				throw new Exception("Failed to update pacakge execution upload info");
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void saveExecutionSourceMappingInfo(PackageExecution packageExecution, SourceFileInfo sourceFileInfo,
			CustomRequest customRequest) {
		DataResponse postRestObject = packageRestUtilities.postRestObject(customRequest,
				"/updatePackageExecutorSourceMappingInfo", sourceFileInfo, customRequest.getEncUserId());
		isValidateDataResponse(postRestObject);
	}

	@Override
	public String dateForIncrementalUpdateQuery(int iLId, int connectionId, String typeOfSource,int packageSourceMappingId ,CustomRequest customRequest) {
		String incrementalUpdateDate = "";
		try {
			DataResponse response = packageRestUtilities.getRestObject(customRequest,
					"/dateForIncrementalUpdateQuery?ilId={iLID}&dataSourceId={dataSourceID}&typeOfSource={typeOfSource}&packageSourceMappingId={packageSourceMappingId}",
					DataResponse.class, customRequest.getEncUserId(), iLId, connectionId, typeOfSource,packageSourceMappingId);
			if (response != null && response.getHasMessages() && response.getMessages().get(0).getCode()
					.equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)) {
				incrementalUpdateDate = (String) response.getObject();
			} else {
				throw new Exception("Failed to get incremental update query date");
			}
		} catch (Exception t) {
		}
		return incrementalUpdateDate;
	}

	@Override
	public int saveSourceFileInfo(SourceFileInfo sourceFileInfo, CustomRequest customRequest) {
		int sourceFileId = 0;
		DataResponse uploadDataResponse = packageRestUtilities.postRestObject(customRequest, "/updateSourceFileInfo",
				sourceFileInfo, customRequest.getEncUserId());
		if (isValidateDataResponse(uploadDataResponse)) {
			sourceFileId = (Integer) uploadDataResponse.getObject();
			sourceFileInfo.setSourceFileId(sourceFileId);
		}
		return sourceFileId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Package getPackageDetails(CustomRequest customRequest, String clientId) {
		Package userPackage = null;
		DataResponse response = packageRestUtilities.getRestObject(customRequest, "/getStandardPackage",
				customRequest.getEncUserId());
		if (isValidateDataResponse(response)) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			userPackage = mapper.convertValue(map, new TypeReference<Package>() {
			});
		}
		return userPackage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ILConnectionMapping> getPackageSourceMappingListByIds(CustomRequest customRequest, DLInfo dlInfo,
			String clientId) {
		List<ILConnectionMapping> ilConnectionMappings = null;
		DataResponse mappingResponse = packageRestUtilities.postRestObject(customRequest,
				"/getILConnectionMappingInfoByMappingIds", dlInfo, customRequest.getEncUserId());
		if (isValidateDataResponse(mappingResponse)) {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			List<?> list = (List<Map<String, Object>>) mappingResponse.getObject();
			ilConnectionMappings = mapper.convertValue(list, new TypeReference<List<ILConnectionMapping>>() {
			});
		}
		return ilConnectionMappings;
	}

	

	@Override
	public void updateUploadInfo(String status, String comments, PackageExecution packExecution,
			CustomRequest customRequest) {
		updateUploadInfo(commonProcessor.getUploadStatus(packExecution.getExecutionId(), status, comments,
				packExecution.getTimeZone()), customRequest);
	}

	@Override
	public void reloadAnvizentTableAccessEndPoint(PackageExecution packageExecution, CustomRequest customRequest) {
		isValidateDataResponse(packageRestUtilities.postRestObject(customRequest, "/schedule/reloadUrlIntegration",
				packageExecution, customRequest.getEncUserId()));
	}

	@Override
	public void alertsAndThreshould(PackageExecution packageExecution, CustomRequest customRequest) {
		isValidateDataResponse(packageRestUtilities.postRestObject(customRequest, "/schedule/alertsAndThreshoulds",
				packageExecution, customRequest.getEncUserId()));
	}

	@Override
	public void druidIntegration(PackageExecution packageExecution, CustomRequest customRequest) {
		isValidateDataResponse(packageRestUtilities.postRestObject(customRequest, "/schedule/druidIntegration",
				packageExecution, customRequest.getEncUserId()));
	}

	@Override
	public void saveUpdateIncrementalDates(PackageExecution packageExecution, CustomRequest customRequest) {
		isValidateDataResponse(packageRestUtilities.postRestObject(customRequest, "/saveOrUpdateIncrementalUpdate",
				packageExecution, customRequest.getEncUserId()));
	}

	@Override
	public List<PackageExecutorMappingInfo> getPackageExecutorSourceMappingInfoList(PackageExecution packageExecution,
			CustomRequest customRequest) {
		List<PackageExecutorMappingInfo> packageExecutorMappingInfoList = null;
		DataResponse pkExecutorMappingInfoListDataResponse = packageRestUtilities.postRestObject(customRequest,
				"/getPackageExecutorSourceMappingInfoList", packageExecution, customRequest.getEncUserId());
		if (isValidateDataResponse(pkExecutorMappingInfoListDataResponse)) {
			@SuppressWarnings("unchecked")
			List<?> list = (List<Map<String, Object>>) pkExecutorMappingInfoListDataResponse.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			packageExecutorMappingInfoList = mapper.convertValue(list,
					new TypeReference<List<PackageExecutorMappingInfo>>() {
					});
		}
		return packageExecutorMappingInfoList;
	}

	@Override
	public void saveExecutionInfo(PackageExecution packageExecution, CustomRequest customRequest) {
		isValidateDataResponse(packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutionStatus",
				packageExecution, customRequest.getEncUserId()));
	}

	@Override
	public void updateExecutionInfo(PackageExecution packageExecution, CustomRequest customRequest) {
		isValidateDataResponse(packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutionStatusInfo",
				packageExecution, customRequest.getEncUserId()));
	}

	@Override
	public void saveExecutionInfo(String status, String comments, PackageExecution packageExecution,
			CustomRequest customRequest) {
		saveExecutionInfo(commonProcessor.getExecutionStatus(packageExecution.getExecutionId(), status, comments,
				packageExecution.getTimeZone()), customRequest);
	}

	@Override
	public void updateExecutionInfo(String status, String comments, PackageExecution packageExecution,
			CustomRequest customRequest) {
		updateExecutionInfo(commonProcessor.getExecutionStatus(packageExecution.getExecutionId(), status, comments,
				packageExecution.getTimeZone()), customRequest);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String runIl(PackageExecutorMappingInfo packageExecutorMappingInfo, CustomRequest customRequest) {
		String tableName = null;
		packageExecutorMappingInfo.getPackageExecution().setClientId(customRequest.getClientId());
		DataResponse runIlDataSource = packageRestUtilities.postRestObject(customRequest, "/runIL",
				packageExecutorMappingInfo, customRequest.getEncUserId());
		if (isValidateDataResponse(runIlDataSource)) {
			Map<String, Object> responseMap = (Map<String, Object>) runIlDataSource.getObject();
			tableName = (String) (responseMap.get("iLTableName"));
		}
		return tableName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> runDl(MultiValueMap<Object, Object> dlMap, PackageExecution packageExecution,
			CustomRequest customRequest) {
		Map<String, Object> responseMap = null;
		DataResponse runDlResponse = packageRestUtilities.postRestObject(customRequest, "/runDL", dlMap,
				customRequest.getEncUserId());
		if (runDlResponse != null && runDlResponse.getMessages() != null) {
			if (runDlResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				responseMap = (Map<String, Object>) runDlResponse.getObject();
			} else {
				throw new PackageExecutionException("Dl execution failed for execution Id:"
						+ packageExecution.getExecutionId() + " dataResponse " + runDlResponse);
			}
		} else {
			throw new PackageExecutionException(
					"Dl execution failed for execution Id:" + packageExecution.getExecutionId());
		}
		return responseMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> runDDlayouts(Set<Integer> ddLTableSet, CustomRequest customRequest) {
		List<String> totalTableNamesList = new ArrayList<>();
		DataResponse ddlLayoutsResponce = packageRestUtilities.postRestObject(customRequest, "/runDDlayoutsById",
				ddLTableSet, customRequest.getEncUserId());
		if (isValidateDataResponse(ddlLayoutsResponce)) {
			List<String> ddlsList = (List<String>) ddlLayoutsResponce.getObject();
			if (ddlsList != null && ddlsList.size() > 0) {
				for (String ddlTable : ddlsList) {
					totalTableNamesList.add(ddlTable);
				}
			}
		}
		return totalTableNamesList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DLInfo getSourcesByDlId(int dlId, CustomRequest customRequest) {
		DLInfo dLInfo = null;
		DataResponse dLInfoResponce = packageRestUtilities.getRestObject(customRequest, "/getSourceInfo/{dlId}",
				customRequest.getEncUserId(), dlId);
		if (isValidateDataResponse(dLInfoResponce)) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dLInfoResponce.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			dLInfo = mapper.convertValue(map, new TypeReference<DLInfo>() {
			});
		}
		return dLInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public WebServiceApi getIlConnectionWebServiceMapping(CustomRequest customRequest, Integer packageId, Integer ilId,
			Integer connectionMappingId) {
		WebServiceApi webServiceApi = null;
		MultiValueMap<Object, Object> multiMap = new LinkedMultiValueMap<>();
		multiMap.add("packageId", packageId);
		multiMap.add("ilId", ilId);
		multiMap.add("connectionMappingId", connectionMappingId);
		DataResponse dataResponse = packageRestUtilities.postRestObject(customRequest, "/ilConnectionWebServiceMapping", multiMap,
				customRequest.getEncUserId());
		if (isValidateDataResponse(dataResponse)) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			webServiceApi = mapper.convertValue(map, new TypeReference<WebServiceApi>() {
			});
		}
		return webServiceApi;
	}

	@SuppressWarnings("unchecked")
	@Override
	public WebServiceConnectionMaster getWebServiceConnectionDetails(Long webServiceConId,
			CustomRequest customRequest) {
		WebServiceConnectionMaster webServiceConnectionMaster = null;
		DataResponse webServiceConnectionMasterResponse = packageRestUtilities.getRestObject(customRequest,
				"/getWebServiceConnectionMaster/{"+ webServiceConId +"}", customRequest.getEncUserId(), webServiceConId);
		if (isValidateDataResponse(webServiceConnectionMasterResponse)) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) webServiceConnectionMasterResponse
					.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			webServiceConnectionMaster = mapper.convertValue(map, new TypeReference<WebServiceConnectionMaster>() {
			});
		}
		return webServiceConnectionMaster;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Table> getTempTablesAndWebServiceJoinUrls(Integer packageId, Integer ilId, Integer connectionMappingId,
			CustomRequest customRequest) {
		List<Table> tempTableList = null;
		MultiValueMap<Object, Object> multiMap = new LinkedMultiValueMap<>();
		multiMap.add("packageId", packageId);
		multiMap.add("ilId", ilId);
		multiMap.add("connectionMappingId", connectionMappingId);
		DataResponse tempTableResponse = packageRestUtilities.postRestObject(customRequest,
				"/getTempTablesAndWebServiceJoinUrls", multiMap, customRequest.getEncUserId());
		if (isValidateDataResponse(tempTableResponse)) {
			List<?> map = (List<Map<String, Object>>) tempTableResponse
					.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			tempTableList = mapper.convertValue(map, new TypeReference<List<Table>>() {
			});
		}
		return tempTableList;
	}

	@Override
	public boolean isTableExists(String tableName, CustomRequest customRequest) {
		boolean istableExists = Boolean.FALSE;
		DataResponse tempTableResponse = packageRestUtilities.getRestObject(customRequest,
				"/checkTableExists/{"+ tableName +"}", customRequest.getEncUserId(),tableName);
		if (isValidateDataResponse(tempTableResponse)) {
			
			istableExists = (boolean) tempTableResponse.getObject();
		}
		return istableExists;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Column> getTableStructure(String tableName, int industryId, CustomRequest customRequest) {
		List<Column> cloumnList = null;
		DataResponse cloumnResponse = packageRestUtilities.getRestObject(customRequest, "/getTableStructure/{"+tableName+"}", customRequest.getEncUserId(), tableName);
		if (isValidateDataResponse(cloumnResponse)) {
			List<?> map = (List<Map<String, Object>>) cloumnResponse
					.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			cloumnList = mapper.convertValue(map, new TypeReference<List<Column>>() {
			});
		}
		return cloumnList;
	}

	@Override
	public void truncateTable(String tableName, String schemaType, CustomRequest customRequest) {
		MultiValueMap<Object, Object> multiMap = new LinkedMultiValueMap<>();
		multiMap.add("tableName", tableName);
		multiMap.add("schemaType", schemaType);
		packageRestUtilities.postRestObject(customRequest, "/truncateTable", multiMap, customRequest.getEncUserId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public FileSettings getFileSettingsInfo(CustomRequest customRequest) {
		FileSettings fileSettings = null;
		DataResponse fileSettingsDataResponse = packageRestUtilities.getRestObject(customRequest, "/getFileSettings", customRequest.getEncUserId());
		if (isValidateDataResponse(fileSettingsDataResponse)) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) fileSettingsDataResponse
					.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			fileSettings = mapper.convertValue(map, new TypeReference<FileSettings>() {
			});
		}
		return fileSettings;
	}

	@Override
	public JdbcTemplate getClientStagingJdbcTemplate(CustomRequest customRequest) {
		JdbcTemplate clientStagingJdbcTemplate = null;
		try{
			DataResponse dataResponse = packageRestUtilities.getRestObject(customRequest, "/getClientStagingJdbcTemplate", customRequest.getEncUserId());
			if (isValidateDataResponse(dataResponse)) {
				clientStagingJdbcTemplate = (JdbcTemplate) dataResponse.getObject();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return clientStagingJdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getClientDbDetails(CustomRequest customRequest) {
		Map<String, Object> clientDbDetails = null;
		DataResponse dataResponse = packageRestUtilities.getRestObject(customRequest, "/getClientDbDetails", customRequest.getEncUserId());
		if (isValidateDataResponse(dataResponse)) {
			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse
					.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			clientDbDetails = mapper.convertValue(map, new TypeReference<Map<String, Object>>() {
			});
		}
		return clientDbDetails;
	}

	@Override
	public void processDataFromFileBatch(String uploadedPath, Integer webServiceConnectionId, String tableName, CustomRequest customRequest) {
		MultiValueMap<Object, Object> map = new  LinkedMultiValueMap<Object, Object>();
		
		map.add("filepath", uploadedPath);
		map.add("webServiceConnectionId", webServiceConnectionId);
		map.add("tableName", tableName);
		
		packageRestUtilities.postRestObject(customRequest, "/processWebserviceDataFromFileBatch", map, customRequest.getEncUserId());
	}


	@Override
	public String createFileByConnection(ILConnectionMapping ilConnectionmapping, S3BucketInfo s3BucketInfo, FileSettings fileSettings, String deploymentType , CustomRequest customRequest) throws AmazonS3Exception, OnpremFileCopyException, IOException {
		String filePath = null;
		DataResponse dataResponse = packageRestUtilities.postRestObject(customRequest, "/processWebserviceFileCreation", ilConnectionmapping, customRequest.getEncUserId());
		if (isValidateDataResponse(dataResponse)) {
			filePath = (String) dataResponse.getObject();
			List<String> filesList = MinidwServiceUtil.downloadFilesFromS3(filePath, null, deploymentType, false, s3BucketInfo, fileSettings.getFileEncryption());
			filePath = filesList.get(0);
		}
		
		return filePath;
		
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> runCustomDataSets(List<String> tableList, String runType, CustomRequest customRequest) {
		List<String> ddlTableList = null;
		MultiValueMap<Object, Object> map = new  LinkedMultiValueMap<Object, Object>();
		map.add("runType", runType);
		map.add("tablesList", String.join(",", tableList));
		DataResponse dataResponse = packageRestUtilities.postRestObject(customRequest, "/runDDlayoutListCustomPackage", map, customRequest.getEncUserId());
		if (isValidateDataResponse(dataResponse)) {
			ddlTableList = (List<String>) dataResponse.getObject();
		}
		return ddlTableList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrossReferenceLogs> getCrossReferencesByIlId(int ilId, CustomRequest customRequest)
	{
		List<CrossReferenceLogs> crossReferenceLogs = null;
		DataResponse dataResponse = crossReferenceRestUtilities.getRestObject(customRequest, "/crossReferencesByIlId/{ilId}", customRequest.getEncUserId(), ilId);
		if (isValidateDataResponse(dataResponse)) {
			List<?> map = (List<Map<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			crossReferenceLogs = mapper.convertValue(map, new TypeReference<List<CrossReferenceLogs>>() {
			});
		}
		return crossReferenceLogs;
	}

	@Override
	public void processCrossReference(CrossReferenceLogs crossReference, CustomRequest customRequest)
	{
		DataResponse response = crossReferenceRestUtilities.postRestObject(customRequest, "/processCrossReference", crossReference, customRequest.getEncUserId());
		isValidateDataResponse(response);
	}

	@Override
	public int updateRetryPaginationAtWsMapping(int id, String retryPaginationData, CustomRequest customRequest)
	{
		int updateCount = 0;
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<Object, Object>();
		map.add("id", id);
		map.add("retryPaginationData", retryPaginationData);
		DataResponse dataResponse = packageRestUtilities.postRestObject(customRequest, "/updateRetryPaginationAtWsMapping", map, customRequest.getEncUserId());
		if( isValidateDataResponse(dataResponse) )
		{
			updateCount = (int) dataResponse.getObject();
		}
		return updateCount;
	}

	@Override
	public String runRBM(BusinessModal businessModal, CustomRequest customRequest) {
		String responseText = null;
		DataResponse runRBMResponse =  packageRestUtilities.postRestObject(customRequest, "/runRBM", businessModal, customRequest.getEncUserId());
		if (runRBMResponse != null && runRBMResponse.getMessages() != null) {
			if (runRBMResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				responseText = runRBMResponse.getMessages().get(0).getText();
			} else {
				throw new PackageExecutionException("R Job execution failed for business model Id:" + businessModal.getBmid() + " dataResponse : " + runRBMResponse.getMessages().get(0).getText());
			}
		}
		return responseText;
	}

	@Override
	public int updateIncrementalDatePaginationAtWsMapping(int id, String incrementalPaginationData, CustomRequest customRequest)
	{
		int updateCount = 0;
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<Object, Object>();
		map.add("id", id);
		map.add("incrementalPaginationData", incrementalPaginationData);
		DataResponse dataResponse = packageRestUtilities.postRestObject(customRequest, "/updateIncrementalDatePaginationAtWsMapping", map, customRequest.getEncUserId());
		if( isValidateDataResponse(dataResponse) )
		{
			updateCount = (int) dataResponse.getObject();
		}
		return updateCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public S3BucketInfo getS3BucketInfo(CustomRequest customRequest)
	{
		S3BucketInfo s3BucketInfo = null;
		DataResponse s3BucketInfoDataResponse = packageRestUtilities.getRestObject(customRequest, "/getS3BucketInfoFromClientId",customRequest.getEncUserId());
		if( s3BucketInfoDataResponse != null && s3BucketInfoDataResponse.getHasMessages() )
		{
			if( s3BucketInfoDataResponse.getMessages().get(0).getCode().equals("SUCCESS") )
			{
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) s3BucketInfoDataResponse.getObject();
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				s3BucketInfo = mapper.convertValue(map, new TypeReference<S3BucketInfo>(){});
			}
		}
		return s3BucketInfo;
	}

}
