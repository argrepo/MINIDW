package com.datamodel.anvizent.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.model.ApisDataInfo;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataTypesInfo;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ELTClusterLogsInfo;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.HistoricalLoadForm;
import com.datamodel.anvizent.service.model.HistoricalLoadStatus;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.OAuth2;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.ScheduleResult;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.WebServiceApi;

/**
 * 
 * @author rakesh.gajula
 *
 */
public interface PackageService {

	PackageDao getPackageDao();

	int createPackage(ClientData clientData , JdbcTemplate clientJdbctemplate);

	List<Package> getUserPackages(String clientId, JdbcTemplate clientJdbctemplate);

	Package getPackageById(int packageId, String clientId, JdbcTemplate clientJdbctemplate);

	Package getPackageByIdWithOutStatusFlag(int packageId, String clientId, JdbcTemplate clientJdbctemplate);

	void deletePackage(int packageId, String clientId, JdbcTemplate clientJdbctemplate);

	List<Database> getDatabaseTypes(JdbcTemplate clientJdbctemplate);

	List<Database> getClientDatabaseTypes(String clientId, JdbcTemplate clientJdbctemplate);

	int createILConnection(ILConnection iLConnection, JdbcTemplate clientJdbctemplate);

	List<ILConnection> getILConnections(String clientId, String userId, JdbcTemplate clientJdbctemplate);

	int saveILConnectionMapping(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbctemplate);

	ILConnection getILConnectionById(int connectionId, String clientId, JdbcTemplate clientJdbctemplate);

	List<Column> getTableStructure(String schemaName, String tableName, int industryId, String clientId, JdbcTemplate clientJdbcTemplate);

	List<ILConnectionMapping> getPendingILs(String clientId, JdbcTemplate clientJdbctemplate);

	List<ILConnectionMapping> getILConnectionMappingInfo(Integer iLId, Integer dLId, Integer packageId, String clientId, JdbcTemplate clientJdbctemplate);

	void updatePackageMappingStatus(ClientData clientData, JdbcTemplate clientJdbctemplate);

	void updatePackageScheduleStatus(ClientData clientData, JdbcTemplate clientJdbctemplate);

	List<ClientData> getILConnectionMappingInfoByPackage(String userId, String clientId, int packageId, JdbcTemplate clientJdbctemplate);
	
	List<ClientData> getILConnectionMappingInfoByPackage(String userId, String clientId, int packageId, JdbcTemplate clientJdbctemplat,boolean isActiveRequired);

	int saveTargetTableInfo(ClientData clientData, JdbcTemplate clientJdbctemplate);

	String createTargetTable(ClientData clientData, JdbcTemplate clientJdbcTemplate) throws Exception;

	ClientData getTargetTableInfoByPackage(String clientId, int packageId, JdbcTemplate clientJdbctemplate);

	List<Table> getAllTargetTablesOfCustomPackage(String clientId, int packageId, JdbcTemplate clientJdbctemplate);

	int updateDLMappingStatus(String clientId, int packageId, int dLid, int iLId, boolean dLMappingStatus, JdbcTemplate clientJdbctemplate);

	int updateILJobStatusForRunNow(String clientId, int packageId, int dLid, int iLId, String iLJobStatusForRunNow, JdbcTemplate clientJdbctemplate);

	int updateTargetTableInfo(ClientData clientData, JdbcTemplate clientJdbctemplate);

	DLInfo getDLById(int dLId, String clinetId, JdbcTemplate clientJdbctemplate);

	DLInfo getDLByIdWithJobName(int dLId, String clinetId, JdbcTemplate clientJdbctemplate);

	ILInfo getILById(int iLId, String clientId, JdbcTemplate clientJdbctemplate);

	ILInfo getILByIdWithJobName(int iLId, String clientId, JdbcTemplate clientJdbctemplate);

	boolean deleteConnectionMapping(String clientId, String packageId, String mappingId, JdbcTemplate clientJdbctemplate);

	Map<String, Object> showCustomPackageTablesStatus(String clientId, String packageId, String industryId, JdbcTemplate clientJdbctemplate);

	Map<String, String> getIlContextParams(Integer ilId, JdbcTemplate clientJdbctemplate);

	void parseContextParams(Map<String, String> contextParams, Map<String, String> paramsVals);

	Map<String, String> getDlContextParams(int dlId, JdbcTemplate clientJdbctemplate);

	void deleteIlSource(int connectionMappingId, String clientId, JdbcTemplate clientJdbctemplate);

	List<Object> getTablePreview(ILConnectionMapping ilConnectionMapping) throws Exception;

	boolean isTargetTableExist(String clientId, String tablename, JdbcTemplate clientJdbctemplate);

	String getIlEpicorQuery(int databasetypeid, int iLid, JdbcTemplate clientJdbctemplate);

	void updateFilePathForDatabaseConnection(long sourceFileId,int ilconnectionMappingId, String filePath, String storageType,Integer bucketId,Boolean multiPartEnabled, JdbcTemplate clientJdbctemplate);

	void dropTable(String schemaName, String tableName, JdbcTemplate clientJdbcTemplate);

	boolean isILMapped(String userId, int packageId, int ilId, int dlId, String clientId, JdbcTemplate clientJdbctemplate);

	ILConnectionMapping getILConnectionMappingInfoForPreview(int mappingId, int packageId, String clientId, JdbcTemplate clientJdbctemplate);

	void disablePackage(List<Integer> packageIds, JdbcTemplate clientJdbctemplate);

	List<JobResult> getJobResults(String packageId, String userId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsByDate(String packageId, String userId, String clientStagingSchemaName, String fromDate, String toDate,
			JdbcTemplate clientJdbcTemplate);

	boolean deleteILConnection(int connectionId, String clientId, JdbcTemplate clientJdbctemplate);

	void deleteILConnectionMapping(int connectionId, JdbcTemplate clientJdbctemplate);

	List<JobResult> getJobErrorLogs(String batchId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	boolean deleteCustomTablesBYPackageId(Package userPackage, String userId, JdbcTemplate clientJdbcTemplate,JdbcTemplate clientAppJdbcTemplate);

	public boolean deleteCustomTablesBYTableId(TableDerivative tableDerivative, int packageId, JdbcTemplate clientJdbcTemplate,JdbcTemplate clientAppJdbcTemplate);

	int updateIlSource(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbctemplate);

	List<DLInfo> getClientDLs(String userId, JdbcTemplate clientJdbctemplate);

	void saveTargetTableAliasNames(ClientData clientData, JdbcTemplate clientJdbctemplate);

	List<Map<String, Object>> targetTableAliasColumns(int tableId, JdbcTemplate clientJdbctemplate);

	public List<ILConnectionMapping> getIlConnectionMappingInfoByPackageId(String userId, Integer packageId, JdbcTemplate clientJdbctemplate);

	List<String> getTargetTables(String clientSchemaName, JdbcTemplate clientJdbcTemplate);

	List<String> derivedTableMappingInfo(int packageId, String userId, JdbcTemplate clientJdbctemplate);

	int updatePackageIsClientDbTablesStatus(String clientId, int packageId, boolean isClientDbTables, JdbcTemplate clientJdbctemplate);

	void saveDerivedTableMappingInfo(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbctemplate);

	List<Package> getAllUserPackages(String clientId, JdbcTemplate clientJdbctemplate);

	List<Package> getUserPackages(String clientId, boolean isStandardPackages, JdbcTemplate clientJdbctemplate);

	ClientData getClientSourceDetails(String clientId, JdbcTemplate clientJdbctemplate);

	long getMaxFileSize(boolean isTrailUser, JdbcTemplate clientJdbctemplate);

	int getConnectorId(int databaseTypeId, JdbcTemplate clientJdbctemplate);
	
	Database getDbIdDriverNameAndProtocal(int databaseTypeId, JdbcTemplate clientJdbctemplate);

	String getTargetTableQuery(String clientId, int packageId, JdbcTemplate clientJdbctemplate);
	
	boolean updateTargetTableQuery(String targetTableQuery, String clientId, int packageId, JdbcTemplate clientJdbcTemplate);

	String getILDefaultIncrementalQuery(int ilId, int databaseTypeId, JdbcTemplate clientJdbctemplate);

	String getILIncrementalDate(String clientStagingSchema, String ILTableName, JdbcTemplate clientJdbcTemplate);

	ILConnectionMapping getDatesForHistoricalLoad(int connectionMappingId, JdbcTemplate clientJdbctemplate);

	HistoricalLoadForm getDatesForHistoricalLoading(int loadId, JdbcTemplate clientJdbctemplate);

	List<DLInfo> getAnalyticalDLs(int dlId, JdbcTemplate clientJdbctemplate);

	boolean purgeUserTables(String clientSchema, List<String> purgeScript, JdbcTemplate clientJdbcTemplate);

	int activateUserPackage(int packageId, int userId, JdbcTemplate clientJdbctemplate);

	Message renameUserPackage(int userId, int packageId, String packageName, JdbcTemplate clientJdbctemplate);

	void updateDatabaseConnection(ILConnection ilConnection, JdbcTemplate clientJdbctemplate);

	public List<Table> getClientILsandDLs(String clientId, JdbcTemplate clientJdbctemplate);

	List<Package> getPackagesByILConnectionId(int ilconnectionId, int userId, JdbcTemplate clientJdbctemplate);

	Integer getSourceCountByPackageId(int packageId, int userId, JdbcTemplate clientJdbctemplate);

	Map<Integer, String> getAllWebServices(String clientId, JdbcTemplate clientJdbctemplate);

	Map<Integer, String> getWebServicesByClientId(String clientId, JdbcTemplate clientJdbctemplate);

	WebService getDefaultILWebServiceMapping(int webserviceId, int ilId, JdbcTemplate clientJdbctemplate);

	Map<String, String> getDbSchemaVaribles(String clientId, int connectorId, JdbcTemplate clientJdbctemplate);

	public List<String> getDefaultInsertsForIL(List<Integer> ilIds, String clientSchemaName, JdbcTemplate clientJdbctemplate);

	public void saveILMappingHeadersForWebService(String userId, int packageId, int dLId, int iLId, String mappedHeaders, JdbcTemplate clientJdbctemplate);

	public String getILMappingHeadersForWebService(int packageId, int dLId, int iLId, JdbcTemplate clientJdbctemplate);

	public String getWebServiceById(int id, JdbcTemplate clientJdbctemplate);

	List<ClientData> getDependentPackagesForScheduling(String clientId, List<String> derivedTablesList, JdbcTemplate clientJdbctemplate);

	public int updateMappedHeadersForWebservice(String userId, ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbctemplate);

	public String getMappedHeadersForWebservice(String userId, int iLId, int mappingId, int webserviceId, JdbcTemplate clientJdbctemplate);

	String getDerivedTableQuery(String clientId, int packageId, String targetTableName, int targetTableId, int tableId, JdbcTemplate clientJdbctemplate);

	String getILHistoricalLoadQuery(int ilId, int databaseTypeId, JdbcTemplate clientJdbctemplate);

	int saveDruidDataSourceMasterInfo(JdbcTemplate clientJdbcTemplate, String clientStagingSchema, String tableName);

	int saveDruidDataSourceMappingInfo(JdbcTemplate clientJdbcTemplate, String clientStagingSchema, int druidDatasourceId, String priority);

	int getClientDruidDataSourceId(JdbcTemplate clientJdbcTemplate, String clientStagingSchema, String tableName);

	int updateDruidDataSourceMasterVersion(JdbcTemplate clientJdbcTemplate, String clientStagingSchema, long newVersion, String tableName);

	WebService getDefaultILWebServiceMappingForExactOnline(int webserviceId, int ilId, JdbcTemplate clientJdbctemplate);

	Map<Integer, String> getAuthenticationTypes(JdbcTemplate clientJdbctemplate);

	int updateAuthenticationCodeForExactOnline(String clientId, String authCode, String authType, Modification modification, JdbcTemplate clientJdbctemplate);

	String getAuthCode(String clientId, String authType, JdbcTemplate clientJdbctemplate);

	int updateAuthenticationTokenForExactOnline(String clientId, String authCode, String authRefreshToken, String authType, Modification modification, JdbcTemplate clientJdbctemplate);

	OAuth2 getAuthToken(String clientId, String authType, JdbcTemplate clientJdbctemplate);

	int updateHistoricalUpdatedTime(String updatedDate, int ilConnectionMappingId, JdbcTemplate clientJdbctemplate);

	int saveHistoricalLoad(String clientId, HistoricalLoadForm historicalLoadForm, Modification modification, JdbcTemplate clientJdbctemplate);

	List<HistoricalLoadForm> getHistoricalLoad(String clientId, JdbcTemplate clientJdbctemplate);

	HistoricalLoadForm getHistoricalLoadDetailsById(String clientId, Integer loadId, JdbcTemplate clientJdbctemplate);

	HistoricalLoadForm getHistoricalLoadDetailsByIdWithConnectionDetails(String clientId, Integer loadId, JdbcTemplate clientJdbctemplate);

	int updateIlHistoryStatus(HistoricalLoadStatus historicalLoadStatus, JdbcTemplate clientJdbctemplate);

	int updateHistoricalLoad(String clientId, HistoricalLoadForm historicalLoadForm, Modification modification, JdbcTemplate clientJdbctemplate);

	List<HistoricalLoadStatus> getHistoricalLoadUploadStatus(String clientid, int historicalLoadId, JdbcTemplate clientJdbctemplate);

	List<JobResult> getJobResultsForHistoricalLoad(int ilId, Long historicalLoadId, String clientId, String clientStagingSchemaName,
			JdbcTemplate clientJdbcTemplate);
	
	List<JobResult> getJobResultsForHistoricalLoadByDate(int ilId, Long historicalLoadId, String clientId, String clientStagingSchemaName, String fromDate,
			String toDate, JdbcTemplate clientJdbcTemplate);
	
	List<JobResult> getJobResultsForDefaultCurrencyLoad(JdbcTemplate clientJdbctemplate);

	List<JobResult> getJobResultsForDefaultCurrencyLoad(String fromDate, String toDate, JdbcTemplate clientJdbctemplate);

	int updateIlConnectionWebServiceMapping(int connection_mapping_id, int ilId, String clientId, int packageId, Modification modification, String wsIds, JdbcTemplate clientJdbctemplate);

	public String getMappedQueryForWebserviceJoin(String userId, int iLId, int mappingId, int webserviceId, JdbcTemplate clientJdbctemplate);

	List<Table> getTempTablesAndWebServiceJoinUrls(String userId, int packageId, int ilId, int ilConnectionMappingId, JdbcTemplate clientJdbctemplate);

	WebService getWebServiceMasterById(int id, String userId, JdbcTemplate clientJdbctemplate);

	int updateWsApiIlConnectionWebServiceMapping(WebService webservice, int ilConnectionMappingId, Modification modification, String userId, JdbcTemplate clientJdbctemplate);

	WebServiceApi getIlConnectionWebServiceMapping(String userId, int packageId, int ilId, int ilConnectionMappingId, JdbcTemplate clientJdbctemplate);
	
	List<String> getColumnHeadersByQuery(String tempQuery, JdbcTemplate clientJdbcTemplate,boolean isDDl);

	List<User> getClientUserDeatils(String clientId, JdbcTemplate clientJdbctemplate);

	List<String> getClientUserIds(String clientId, JdbcTemplate clientJdbctemplate);
	
	List<Map<String, Object>> getTableData(String query,String parametrs, JdbcTemplate clientJdbctemplate);
	
	List<ILInfo> getClientIlsList(String clientId, JdbcTemplate clientJdbctemplate);

	List<ClientDataSources> getDataSourceList(String clientId, JdbcTemplate clientJdbctemplate);
    
	String getILIncrementalUpdateDate(String clientStagingSchema, int iLId, String dataSourceId, String typeOfSource,int packageSourceMappingId, JdbcTemplate clientJdbcTemplate);

	List<DDLayout> getDDlayoutTablesList(String clientId,String userId, JdbcTemplate clientJdbctemplate);
	
	DDLayout  getDDlayoutTable(String clientId,int id,String userId, JdbcTemplate clientJdbctemplate);
	
	int  updateDDlayoutTable(String clientId,DDLayout dDLayout,String userId, JdbcTemplate clientJdbctemplate);
	
	int saveDDlTableInfo(ClientData clientData,String clientId, JdbcTemplate clientJdbctemplate);
	
	void dropDDlayoutTable(String clientId,  DDLayout dDLayout,JdbcTemplate jdbcTemplate);
	
	int deleteDDlayoutTable(String clientId,  DDLayout dDLayout, JdbcTemplate clientJdbctemplate);
	
	List<DDLayout> getDDlayoutList(String clientId,List<String> derivedtableList,String userId, JdbcTemplate clientJdbctemplate);
	
	int createDataSourceList(ClientDataSources clientDataSources, JdbcTemplate clientJdbctemplate);
   
	int updateDataSourceDetails(Integer mappingId, String dataSouceName,String dataSouceNameOther,Integer packageId, JdbcTemplate clientJdbctemplate);
	
	int updateWsDataSourceDetails(Integer mappingId, String dataSouceName,String dataSouceNameOther,Integer packageId,String wsApiUrl, JdbcTemplate clientJdbctemplate);
	
	int updateWsJoinDataSourceDetails(Integer mappingId, int wsMappingId,String dataSouceName,String dataSouceNameOther,Integer packageId,String wsApiUrl, JdbcTemplate clientJdbctemplate);
	
    int updateDDlayoutTableAuditLogs(String clientId,  DDLayout dDLayout, JdbcTemplate clientJdbctemplate);
    
    List<DDLayout> getDDlayoutTableAuditLogs(String clientId,  DDLayout dDLayout, JdbcTemplate clientJdbctemplate);
	
	List<ScheduleResult> getScheduleStartTime(int packageId, JdbcTemplate clientJdbctemplate);

	int updatePackageExecutorSourceMappingInfo(SourceFileInfo sourceFileInfo, JdbcTemplate clientJdbctemplate);
	
	int updateSourceFileInfo(SourceFileInfo sourceFileInfo, JdbcTemplate clientJdbctemplate);
	
	List<PackageExecutorMappingInfo> getPackageExecutorSourceMappingInfoList(long executionId, JdbcTemplate clientJdbctemplate);
	
	ILConnectionMapping getIlConnectionMappingByPackageExecutorSourceMappingInfo(long executionId,long executionMappingId, JdbcTemplate clientJdbctemplate);

	List<ScheduleResult> getScheduleDetails(int packageId ,String scheduleStartTime, JdbcTemplate clientJdbctemplate);
	
	DDLayout viewDDlTableSelectQry(String clientId,  DDLayout dDLayout, JdbcTemplate clientJdbctemplate);
	
	List<PackageExecution> getPackageExecution(Integer packageId, JdbcTemplate clientJdbctemplate);

	int logError(Throwable ex, HttpServletRequest request, JdbcTemplate clientJdbctemplate);
	
	List<DLInfo> getILConnectionMappingDlInfoByPackageId(Integer packageId,String userId, JdbcTemplate clientJdbctemplate);
	
	FileSettings getFileSettingsInfo(String clientId, JdbcTemplate clientJdbctemplate);
	
	String getUploadAndExecutionStatusComments(long executionId,String uploadOrExecution, JdbcTemplate clientJdbcTemplate);

	List<PackageExecution> getInProgressPackageExecutionList(Integer packageId, JdbcTemplate clientJdbcTemplate);
	
	String getUploadStatusAndExecutionStatusComments(long executionId, JdbcTemplate clientJdbcTemplate);
	
	int updatePackageExecutionTargetTableInfo(PackageExecution packageExecution,JdbcTemplate clientJdbcTemplate);
	
	List<PackageExecution> getPackageExecutionSourceMappingInfo(long executionId, JdbcTemplate clientJdbcTemplate);
	
	List<PackageExecution> getPackageExecutionTargetTableInfo(long executionId, JdbcTemplate clientJdbcTemplate);
	
	int updateDatesForOnceRecurrence(Schedule schedule, JdbcTemplate clientJdbcTemplate);

	Package getPackageWithoutUserId(int packageIdInt, JdbcTemplate clientJdbcTemplate);

	PackageExecution getPackageExecutionInfo(Integer packageId, Integer executionId,  String uploaderExecution,	JdbcTemplate clientJdbcTemplate);

	int getIncrementalLoadCount(Integer valueOf, Integer valueOf2, JdbcTemplate clientAppDbJdbcTemplate);

	int updateIsActiveStatusIlSource(String connectionMappingId, Boolean isActiveRequired,JdbcTemplate clientAppDbJdbcTemplate);

	int saveApisDataInfo(ApisDataInfo apisDataInfo, JdbcTemplate clientAppDbJdbcTemplate);

	ApisDataInfo getApistDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate);
	
	ApisDataInfo getApistDetailsByEndPointName(String endPointName, JdbcTemplate clientAppDbJdbcTemplate);

	List<ApisDataInfo> getApisDetails(JdbcTemplate clientAppDbJdbcTemplate);
	
	List<JobResult> getJobResultsForCrossReference(int ilId,String clientId, String clientStagingSchemaName,
			JdbcTemplate clientJdbcTemplate);
	
	List<JobResult> getJobResultsForCrossReferenceByDate(int ilId, String clientId, String clientStagingSchemaName, String fromDate,
			String toDate, JdbcTemplate clientJdbcTemplate);

	boolean cloneHistoricalLoadDetailsById(Integer loadId, JdbcTemplate clientAppDbJdbcTemplate);

	int updatePackageName(Integer packageId, String packageName, JdbcTemplate clientAppDbJdbcTemplate);
	
	int updatePackageAdvancedField(Integer packageId, boolean status, JdbcTemplate clientAppDbJdbcTemplate);

	List<JobResult> getJobResultsForCrossReferenceByConditionId(int conditionId, int ilId, String clientId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsForCrossReferenceByDateById(Integer conditionId, Integer ilId, String clientId, String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate);

	void getUpdatedTargetMappedQuery(List<String> query, JdbcTemplate clientJdbcTemplate);

	void deleteCustomTempTableByMappingId(Integer packageId, String mappingId, String clientId, JdbcTemplate clientAppDbJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate2);

	List<ELTClusterLogsInfo> getEltInitiateInfo(int executionId, JdbcTemplate clientAppDbJdbcTemplate);

	void editTargetMappedQuery(String query, JdbcTemplate clientJdbcTemplate);

	List<DataTypesInfo> getDataTypesList();

	Message deleteUserPackage(int userId, int packageId, JdbcTemplate clientJdbctemplate);

	int getPackageExecutionCount(int packageId, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<PackageExecution> getPackageExecutionByPagination(Integer packageId,int offset,int limit, JdbcTemplate clientJdbctemplate);
	
	String getWsSourceMappingIdsByMappingId(int mappindId,Integer packageId, JdbcTemplate clientAppDbJdbcTemplate);
	
	int updateWsJoinUrlAtPackageSourceMapping(int mappindId,Integer packageId,String wsJoinUrl, JdbcTemplate clientAppDbJdbcTemplate);
	
	String getSslFileNamesByConId(int conId,JdbcTemplate clientAppDbJdbcTemplate);

}