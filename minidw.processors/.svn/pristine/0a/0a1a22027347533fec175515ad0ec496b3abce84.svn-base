package com.anvizent.minidw.service.utils.processor;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CrossReferenceLogs;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DLInfo;
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

public interface MetaDataFetch {
	
	public PackageExecution saveUploadInfo(PackageExecution pExecution, CustomRequest customRequest);
	
	public void updateUploadInfo(String status, String message,PackageExecution packExecution, CustomRequest customRequest);
	
	public void updateUploadInfo(PackageExecution packExecution, CustomRequest customRequest);

	public void saveExecutionSourceMappingInfo(PackageExecution packageExecution, SourceFileInfo sourceFileInfo, CustomRequest customRequest);
	
	public int saveSourceFileInfo(SourceFileInfo sourceFileInfo, CustomRequest customRequest);
	
	public String dateForIncrementalUpdateQuery(int iLId, int connectionId, String connectionType, int packageSourceMappingId,CustomRequest customRequest);

	public Package getPackageDetails(CustomRequest customRequest,String clientId);
	
	public List<ILConnectionMapping> getPackageSourceMappingListByIds(CustomRequest customRequest,DLInfo dlInfo,String clientId);
	
	public void reloadAnvizentTableAccessEndPoint(PackageExecution packageExecution, CustomRequest customRequest);
	
	public void alertsAndThreshould(PackageExecution packageExecution, CustomRequest customRequest);
	
	public void druidIntegration(PackageExecution packageExecution, CustomRequest customRequest);
	
	public void saveUpdateIncrementalDates(PackageExecution packageExecution, CustomRequest customRequest);
	
	public List<PackageExecutorMappingInfo> getPackageExecutorSourceMappingInfoList(PackageExecution packageExecution, CustomRequest customRequest);
	
	public void saveExecutionInfo(PackageExecution pExecution, CustomRequest customRequest);
	
	public String runIl(PackageExecutorMappingInfo packageExecutorMappingInfo, CustomRequest customRequest);
	
	public String runRBM(BusinessModal businessModal, CustomRequest customRequest);
	
	public Map<String,Object> runDl(MultiValueMap<Object, Object> dlMap, PackageExecution packageExecution, CustomRequest customRequest);
	
	public List<String> runDDlayouts(Set<Integer> ddLTableSet, CustomRequest customRequest);
	
	public void saveExecutionInfo(String status,String comments,PackageExecution packageExecution, CustomRequest customRequest);
	
	public void updateExecutionInfo(String status,String comments,PackageExecution packageExecution, CustomRequest customRequest);
	
	public void updateExecutionInfo(PackageExecution packageExecution, CustomRequest customRequest);
	
	public DLInfo getSourcesByDlId(int dlId, CustomRequest customRequest);

	public WebServiceApi getIlConnectionWebServiceMapping(CustomRequest customRequest, Integer packageId, Integer ilId, Integer connectionMappingId);

	public WebServiceConnectionMaster getWebServiceConnectionDetails(Long valueOf, CustomRequest customRequest);

	public List<Table> getTempTablesAndWebServiceJoinUrls(Integer packageId, Integer ilId, Integer connectionMappingId,
			CustomRequest customRequest);

	public boolean isTableExists(String tableName, CustomRequest customRequest);

	public List<Column> getTableStructure(String tableName, int industryId, CustomRequest customRequest);

	public void truncateTable(String tableName, String schemaType, CustomRequest customRequest);

	public FileSettings getFileSettingsInfo(CustomRequest customRequest);

	public JdbcTemplate getClientStagingJdbcTemplate(CustomRequest customRequest);

	public Map<String, Object> getClientDbDetails(CustomRequest customRequest);

	public void processDataFromFileBatch(String uploadedPath, Integer webServiceConnectionId, String tableName, CustomRequest customRequest);
	
	public String createFileByConnection(ILConnectionMapping ilConnectionmapping, S3BucketInfo s3BucketInfo, FileSettings fileSettings, String deploymentType , CustomRequest customRequest)  throws AmazonS3Exception, OnpremFileCopyException, IOException ;

	public List<String> runCustomDataSets(List<String> tableList, String runType, CustomRequest customRequest);

	public List<CrossReferenceLogs> getCrossReferencesByIlId(int ilId, CustomRequest customRequest);

	public void processCrossReference(CrossReferenceLogs crossReference, CustomRequest customRequest);
	
	public int updateRetryPaginationAtWsMapping(int id , String retryPaginationData,CustomRequest customRequest);
	
	public int updateIncrementalDatePaginationAtWsMapping(int id , String incrementalPaginationData,CustomRequest customRequest);
	
	public S3BucketInfo getS3BucketInfo(CustomRequest customRequest);

}
