package com.datamodel.anvizent.service.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.ApisDataInfo;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataTypesInfo;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ELTClusterLogsInfo;
import com.datamodel.anvizent.service.model.ErrorLog;
import com.datamodel.anvizent.service.model.FileSettings;
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
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.ScheduleResult;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.WebServiceApi;

/**
 * 
 * @author rakesh.gajula
 *
 */public interface PackageDao {

	int createPackage(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	List<Package> getUserPackages(String clientId, JdbcTemplate clientJdbcTemplate);

	Package getPackageById(int packageId, String clientId, JdbcTemplate clientJdbcTemplate);

	Package getPackageByIdWithOutStatusFlag(int packageId, String clientId, JdbcTemplate clientJdbcTemplate);

	void deletePackage(int packageId, String clientId, JdbcTemplate clientJdbcTemplate);

	int createILConnection(ILConnection iLConnection, JdbcTemplate clientJdbcTemplate);

	List<Database> getDatabaseTypes(JdbcTemplate clientJdbcTemplate);

	List<Database> getClientDatabaseTypes(String clientId, JdbcTemplate clientJdbcTemplate);

	List<ILConnection> getILConnections(String clientId, String userId, JdbcTemplate clientJdbcTemplate);

	int saveILConnectionMapping(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbcTemplate);

	ILConnection getILConnectionById(int connectionId, String clientId, JdbcTemplate clientJdbcTemplate);

	List<Column> getTableStructure(String schemaName, String tableName, int industryId, String clientId, JdbcTemplate clientJdbcTemplate);

	List<ILConnectionMapping> getPendingILs(String clientId, JdbcTemplate clientJdbcTemplate);

	List<ILConnectionMapping> getILConnectionMappingInfo(Integer iLId, Integer dLId, Integer packageId, String clientId, JdbcTemplate clientJdbcTemplate);

	void updatePackageMappingStatus(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	void updatePackageScheduleStatus(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	List<ClientData> getILConnectionMappingInfoByPackage(String userId, String clientId, int packageId, boolean isStandard, JdbcTemplate clientJdbcTemplate,boolean isActiveRequired);

	int saveTargetTableInfo(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	String createTargetTable(ClientData clientData, JdbcTemplate clientJdbcTemplate) throws Exception;

	ClientData getTargetTableInfoByPackage(String clientId, int packageId, JdbcTemplate clientJdbcTemplate);

	List<Table> getAllTargetTablesOfCustomPackage(String clientId, int packageId, JdbcTemplate clientJdbcTemplate);

	int updateDLMappingStatus(String clientId, int packageId, int dLid, int iLId, boolean dLMappingStatus, JdbcTemplate clientJdbcTemplate);

	int updateILJobStatusForRunNow(String clientId, int packageId, int dLid, int iLId, String iLJobStatusForRunNow, JdbcTemplate clientJdbcTemplate);

	int updateTargetTableInfo(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	DLInfo getDLById(int dLId, String clinetId, JdbcTemplate clientJdbcTemplate);

	DLInfo getDLByIdWithJobName(int dLId, String clinetId, JdbcTemplate clientJdbcTemplate);

	ILInfo getILById(int iLId, String clientId, JdbcTemplate clientJdbcTemplate);

	ILInfo getILByIdWithJobName(int iLId, String clientId, JdbcTemplate clientJdbcTemplate);

	boolean deleteConnectionMapping(String clientId, String packageId, String mappingId, JdbcTemplate clientJdbcTemplate);

	void deleteIlSource(int connectionMappingId, String clientId, JdbcTemplate clientJdbcTemplate);

	Map<String, Object> showCustomPackageTablesStatus(String clientId, String packageId, String industryId, JdbcTemplate clientJdbcTemplate);

	Map<String, String> getIlContextParams(Integer ilId, JdbcTemplate clientJdbcTemplate);

	Map<String, String> getDlContextParams(Integer dlId, JdbcTemplate clientJdbcTemplate);

	List<Object> getTablePreview(ILConnectionMapping ilConnectionMapping) throws Exception;

	boolean isTargetTableExist(String clientId, String tablename, JdbcTemplate clientJdbcTemplate);

	String getIlEpicorQuery(int databasetypeid, int iLid, JdbcTemplate clientJdbcTemplate);

	void updateFilePathForDatabaseConnection(long sourceFileId,int ilconnectionMappingId, String filePath, String storageType,Integer bucketId,Boolean multiPartEnabled, JdbcTemplate clientJdbcTemplate);

	void dropTable(String schemaName, String tableName, JdbcTemplate clientJdbcTemplate);

	boolean isILMapped(String userId, int packageId, int ilId,int dlId, String clientId, JdbcTemplate clientJdbcTemplate);

	ILConnectionMapping getILConnectionMappingInfoForPreview(int mappingId, int packageId, String clientId, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResults(String packageId, String userId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsByDate(String packageId, String userId, String clientStagingSchemaName, String jobStartDate, String jobEndDate,
			JdbcTemplate clientJdbcTemplate);

	void disablePackage(List<Integer> packageIds, JdbcTemplate clientJdbcTemplate);

	boolean deleteILConnection(int connectionId, String clientId, JdbcTemplate clientJdbcTemplate);

	void deleteILConnectionMapping(int connectionId, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobErrorLogs(String batchId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	int updateIlSource(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbcTemplate);

	boolean deleteCustomTablesBYPackageId(Package userPackage, String userId, JdbcTemplate clientJdbcTemplate,JdbcTemplate clientAppJdbcTemplate);

	public boolean deleteCustomTablesBYTableId(TableDerivative tableDerivative, int packageId, JdbcTemplate clientJdbcTemplate,JdbcTemplate clientAppJdbcTemplate);

	List<DLInfo> getClientDLs(String userId, JdbcTemplate clientJdbcTemplate);

	void saveTargetTableAliasNames(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	List<Map<String, Object>> targetTableAliasColumns(int tableId, JdbcTemplate clientJdbcTemplate);

	public List<ILConnectionMapping> getIlConnectionMappingInfoByPackageId(String userId, Integer packageId, JdbcTemplate clientJdbcTemplate);

	List<String> getTargetTables(String clientSchemaName, JdbcTemplate clientJdbcTemplate);

	List<String> derivedTableMappingInfo(int packageId, String userId, JdbcTemplate clientJdbcTemplate);

	public int updatePackageIsClientDbTablesStatus(String clientId, int packageId, boolean isClientDbTables, JdbcTemplate clientJdbcTemplate);

	ClientData getClientSourceDetails(String clientId, JdbcTemplate clientJdbcTemplate);

	void saveDerivedTableMappingInfo(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbcTemplate);

	List<Package> getAllUserPackages(String clientId, JdbcTemplate clientJdbcTemplate);

	List<Package> getUserPackages(String clientId, boolean isStandardPackages, JdbcTemplate clientJdbcTemplate);

	long getMaxFileSize(boolean isTrailUser, JdbcTemplate clientJdbcTemplate);

	int getConnectorId(int databaseTypeId, JdbcTemplate clientJdbcTemplate);

	String getTargetTableQuery(String clientId, int packageId, JdbcTemplate clientJdbcTemplate);

	boolean updateTargetTableQuery(String targetTableQuery, String clientId, int packageId, JdbcTemplate clientJdbcTemplate);
	
	String getILDefaultIncrementalQuery(int ilId, int databaseTypeId, JdbcTemplate clientJdbcTemplate);

	String getILIncrementalDate(String clientStagingSchema, String iLTableName, JdbcTemplate clientJdbcTemplate);

	ILConnectionMapping getDatesForHistoricalLoad(int connectionMappingId, JdbcTemplate clientJdbcTemplate);

	HistoricalLoadForm getDatesForHistoricalLoading(int loadId, JdbcTemplate clientJdbcTemplate);

	List<DLInfo> getAnalyticalDLs(int dlId, JdbcTemplate clientJdbcTemplate);

	boolean purgeUserTables(String clientSchema, List<String> purgeScript, JdbcTemplate clientJdbcTemplate);

	int activateUserPackage(int packageId, int userId, JdbcTemplate clientJdbcTemplate);

	Message renameUserPackage(int userId, int packageId, String packageName, JdbcTemplate clientJdbcTemplate);

	void updateDatabaseConnection(ILConnection ilConnection, JdbcTemplate clientJdbcTemplate);

	public List<Table> getClientILsandDLs(String clientId, JdbcTemplate clientJdbcTemplate);

	List<Package> getPackagesByILConnectionId(int ilconnectionId, int userId, JdbcTemplate clientJdbcTemplate);

	Integer getSourceCountByPackageId(int packageId, int userId, JdbcTemplate clientJdbcTemplate);

	Map<Integer, String> getAllWebServices(String clientId, JdbcTemplate clientJdbcTemplate);

	Map<Integer, String> getWebServicesByClientId(String clientId, JdbcTemplate clientJdbcTemplate);

	WebService getDefaultILWebServiceMapping(int webserviceId, int ilId, JdbcTemplate clientJdbcTemplate);

	int logError(ErrorLog errorLog, JdbcTemplate clientJdbcTemplate);

	Map<String, String> getDbSchemaVaribles(String clientId, int connectorId, JdbcTemplate clientJdbcTemplate);

	public List<String> getDefaultInsertsForIL(List<Integer> ilIds, String clientSchemaName, JdbcTemplate clientJdbcTemplate);

	public void saveILMappingHeadersForWebService(String userId, int pakageId, int dLId, int iLId, String mappedHeaders, JdbcTemplate clientJdbcTemplate);

	public String getILMappingHeadersForWebService(int packageId, int dLId, int iLId, JdbcTemplate clientJdbcTemplate);

	public String getWebServiceById(int id, JdbcTemplate clientJdbcTemplate);

	List<ClientData> getDependentPackagesForScheduling(String clientId, List<String> derivedTablesList, JdbcTemplate clientJdbcTemplate);

	public int updateMappedHeadersForWebservice(String userId, ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbcTemplate);

	public String getMappedHeadersForWebservice(String userId, int iLId, int mappingId, int webserviceId, JdbcTemplate clientJdbcTemplate);

	String getDerivedTableQuery(String clientId, int packageId, String targetTableName, int targetTableId, int tableId, JdbcTemplate clientJdbcTemplate);

	String getILHistoricalLoadQuery(int ilId, int databaseTypeId, JdbcTemplate clientJdbcTemplate);

	int saveDruidDataSourceMasterInfo(JdbcTemplate clientJdbcTemaplate, String clientStagingSchema, String tableName);

	int saveDruidDataSourceMappingInfo(JdbcTemplate clientJdbcTemaplate, String clientStagingSchema, int druidDatasourceId, String priority);

	int getClientDruidDataSourceId(JdbcTemplate clientJdbcTemplate, String clientStagingSchema, String tableName);

	int updateDruidDataSourceMasterVersion(JdbcTemplate clientJdbcTemplate, String clientStagingSchema, long newVersion, String tableName);

	WebService getDefaultILWebServiceMappingForExactOnline(int webserviceId, int ilId, JdbcTemplate clientJdbcTemplate);

	Map<Integer, String> getAuthenticationTypes(JdbcTemplate clientJdbcTemplate);

	int updateAuthenticationCodeForExactOnline(String clientId, String authCode, String authType, Modification modification, JdbcTemplate clientJdbcTemplate);

	String getAuthCode(String clientId, String authType, JdbcTemplate clientJdbcTemplate);

	int updateAuthenticationTokenForExactOnline(String clientId, String authCode, String authRefreshToken, String authType, Modification modification, JdbcTemplate clientJdbcTemplate);

	OAuth2 getAuthToken(String clientId, String authType, JdbcTemplate clientJdbcTemplate);

	int updateHistoricalUpdatedTime(String updatedDate, int ilConnectionMappingId, JdbcTemplate clientJdbcTemplate);

	int saveHistoricalLoad(String clientId, HistoricalLoadForm historicalLoadForm, Modification modification, JdbcTemplate clientJdbcTemplate);

	List<HistoricalLoadForm> getHistoricalLoad(String clientId, JdbcTemplate clientJdbcTemplate);

	HistoricalLoadForm getHistoricalLoadDetailsById(String clientId, Integer loadId, JdbcTemplate clientJdbcTemplate);

	HistoricalLoadForm getHistoricalLoadDetailsByIdWithConnectionDetails(String clientId, Integer loadId, JdbcTemplate clientJdbcTemplate);

	int updateIlHistoryStatus(HistoricalLoadStatus historicalLoadStatus, JdbcTemplate clientJdbcTemplate);

	int updateHistoricalLoad(String clientId, HistoricalLoadForm historicalLoadForm, Modification modification, JdbcTemplate clientJdbcTemplate);

	List<HistoricalLoadStatus> getHistoricalLoadUploadStatus(String clientid, int historicalLoadId, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsForHistoricalLoad(int ilId, Long historicalLoadId, String clientId, String clientStagingSchemaName,
			JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsForHistoricalLoadByDate(int ilId, Long historicalLoadId, String clientId, String clientStagingSchemaName, String fromDate,
			String toDate, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsForDefaultCurrencyLoad(JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsForDefaultCurrencyLoad( String fromDate, String toDate, JdbcTemplate clientJdbcTemplate);

	int updateIlConnectionWebServiceMapping(int connectionMappingId, int ilId, String clientId, int packageId, Modification modification, String wsIds, JdbcTemplate clientJdbcTemplate);

	public String getMappedQueryForWebserviceJoin(String userId, int iLId, int mappingId, int webserviceId, JdbcTemplate clientJdbcTemplate);

	List<Table> getTempTablesAndWebServiceJoinUrls(String userId, int packageId, int ilId, int ilConnectionMappingId, JdbcTemplate clientJdbcTemplate);

	WebService getWebServiceMasterById(int id, String userId, JdbcTemplate clientJdbcTemplate);

	int updateWsApiIlConnectionWebServiceMapping(WebService webservice, int ilConnectionMappingId, Modification modification, String userId, JdbcTemplate clientJdbcTemplate);

	WebServiceApi getIlConnectionWebServiceMapping(String userId, int packageId, int ilId, int ilConnectionMappingId, JdbcTemplate clientJdbcTemplate);

	String getILIncrementalUpdateDate(String clientStagingSchema, int iLId, String dataSourceId, String typeOfSource,int packageSourceMappingId, JdbcTemplate clientJdbcTemplate);

	List<String> getColumnHeadersByQuery(String tempQuery, JdbcTemplate clientJdbcTemplate,boolean isDDl);

	List<User> getClientUserDeatils(String clientId, JdbcTemplate clientJdbcTemplate);

	List<String> getClientUserIds(String clientId, JdbcTemplate clientJdbcTemplate);
	
    List<Map<String, Object>> getTableData(String query,String parametrs, JdbcTemplate clientJdbcTemplate);
    
    List<ILInfo> getClientIlsList(String clientId, JdbcTemplate clientJdbcTemplate);
	
    List<ClientDataSources> getDataSourceList(String clientId, JdbcTemplate clientJdbcTemplate);
    
    int createDataSourceList(ClientDataSources clientDataSources, JdbcTemplate clientJdbcTemplate);

	int updateDataSourceDetails(Integer mappingId, String dataSouceName,String dataSouceNameOther,Integer packageId, JdbcTemplate clientJdbcTemplate);
	
	int updateWsDataSourceDetails(Integer mappingId, String dataSouceName,String dataSouceNameOther,Integer packageId,String wsApiUrl, JdbcTemplate clientJdbctemplate);
	
	int updateWsJoinDataSourceDetails(Integer mappingId,int wsMappingId ,String dataSouceName,String dataSouceNameOther,Integer packageId,String wsApiUrl, JdbcTemplate clientJdbctemplate);
	
	List<DDLayout> getDDlayoutTablesList(String clientId,String userId, JdbcTemplate clientJdbcTemplate);
	
    DDLayout getDDlayoutTable(String clientId,int id,String userId, JdbcTemplate clientJdbcTemplate);
    
    int updateDDlayoutTable(String clientId,  DDLayout dDLayout,String userId, JdbcTemplate clientJdbcTemplate);
    
    int saveDDlTableInfo(ClientData clientData,String clientId, JdbcTemplate clientJdbcTemplate);
    
    void dropDDlayoutTable(String clientId,  DDLayout dDLayout,JdbcTemplate jdbcTemplate);
    
    int deleteDDlayoutTable(String clientId,  DDLayout dDLayout, JdbcTemplate clientJdbcTemplate);
    
    List<DDLayout> getDDlayoutList(String clientId,List<String> derivedtableList,String userId, JdbcTemplate clientJdbcTemplate);
    
    int updateDDlayoutTableAuditLogs(String clientId,  DDLayout dDLayout, JdbcTemplate clientJdbcTemplate);
    
    List<DDLayout> getDDlayoutTableAuditLogs(String clientId,  DDLayout dDLayout, JdbcTemplate clientJdbcTemplate);
    
	List<ScheduleResult> getScheduleStartTime(int packageId, JdbcTemplate clientJdbcTemplate);

	List<ScheduleResult> getScheduleDetails(int packageId ,String scheduleStartTime, JdbcTemplate clientJdbcTemplate);
	
	DDLayout viewDDlTableSelectQry(String clientId,  DDLayout dDLayout, JdbcTemplate clientJdbcTemplate);
	
	Database getDbIdDriverNameAndProtocal(int databaseTypeId, JdbcTemplate clientJdbcTemplate);
	
	int updatePackageExecutorSourceMappingInfo(SourceFileInfo sourceFileInfo, JdbcTemplate clientJdbcTemplate);
	
	int updateSourceFileInfo(SourceFileInfo sourceFileInfo, JdbcTemplate clientJdbcTemplate);
	
	List<PackageExecutorMappingInfo> getPackageExecutorSourceMappingInfoList(long executionId, JdbcTemplate clientJdbcTemplate);
	
	ILConnectionMapping getIlConnectionMappingByPackageExecutorSourceMappingInfo(long executionId,long executionMappingId, JdbcTemplate clientJdbcTemplate);

	List<PackageExecution> getPackageExecution(Integer packageId , JdbcTemplate clientJdbcTemplate);
	
	List<DLInfo> getILConnectionMappingDlInfoByPackageId(Integer packageId,String userId, JdbcTemplate clientJdbcTemplate);
    
	FileSettings getFileSettingsInfo(String clientId, JdbcTemplate clientJdbcTemplate);
	
	Package getPackageSchedule(String clientId,int packageId,int schedileId, JdbcTemplate clientJdbcTemplate);
	
	String getUploadAndExecutionStatusComments(long executionId,String uploadOrExecution, JdbcTemplate clientJdbcTemplate);
	
	List<PackageExecution> getInProgressPackageExecutionList(Integer packageId, JdbcTemplate clientJdbcTemplate);
	
	String getUploadStatusAndExecutionStatusComments(long executionId, JdbcTemplate clientJdbcTemplate);
	
	public List<PackageExecution> getScheduledPackagesForExecution(JdbcTemplate clientJdbcTemplate);
	
    int updatePackageExecutionTargetTableInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
    
	List<PackageExecution> getPackageExecutionSourceMappingInfo(long executionId, JdbcTemplate clientJdbcTemplate);
	
	List<PackageExecution> getPackageExecutionTargetTableInfo(long executionId, JdbcTemplate clientJdbcTemplate);
	
	int updateDatesForOnceRecurrence(Schedule schedule, JdbcTemplate clientJdbcTemplate);
	
	ClientJobExecutionParameters  getClientJobExecutionParams(JdbcTemplate clientJdbcTemplate);
	
	boolean getDlLoadType(long executionId, JdbcTemplate clientJdbcTemplate);

	Package getPackageWithoutUserId(int packageId, JdbcTemplate clientJdbcTemplate);
	
	List<DDLayout> getDDlayoutListByClientId(String clientId,List<String> derivedtableList,String userId, JdbcTemplate clientJdbcTemplate);

	PackageExecution getPackageExecutionInfo(Integer packageId, Integer executionId, String uploaderExecution,	JdbcTemplate clientJdbcTemplate);

	int getIncrementalLoadCount(Integer packageId, Integer executionId, JdbcTemplate clientJdbcTemplate);

	int updateIsActiveStatusIlSource(String connectionMappingId, Boolean isActiveRequired,JdbcTemplate clientJdbcTemplate);

	int saveApisDataInfo(ApisDataInfo apisDataInfo, JdbcTemplate clientAppDbJdbcTemplate);

	ApisDataInfo getApistDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate);
	
	ApisDataInfo getApistDetailsByEndPointName(String endPointName, JdbcTemplate clientAppDbJdbcTemplate);

	List<ApisDataInfo> getApisDetails(JdbcTemplate clientAppDbJdbcTemplate);

	List<DDLayout> getDDlayoutListByIds(String clientId, Set<Integer> ddLTableSet, String userId, JdbcTemplate clientAppDbJdbcTemplate);

	List<JobResult> getJobResultsForCrossReference(int ilId,String clientId, String clientStagingSchemaName,
			JdbcTemplate clientJdbcTemplate);
	
	List<JobResult> getJobResultsForCrossReferenceByDate(int ilId, String clientId, String clientStagingSchemaName, String fromDate,
			String toDate, JdbcTemplate clientJdbcTemplate);

	Table getTempTableByWebServiceConnectionId(Integer webServiceConnectionId, JdbcTemplate clientAppdbJdbcTemplate);

	boolean cloneHistoricalLoadDetailsById(Integer loadId, JdbcTemplate clientAppDbJdbcTemplate);

	int updatePackageName(Integer packageId, String packageName, JdbcTemplate clientAppDbJdbcTemplate);
	
	int updatePackageAdvancedField(Integer packageId, boolean status, JdbcTemplate clientAppDbJdbcTemplate);

	List<JobResult> getJobResultsForCrossReferenceByConditionId(int conditionId, int ilId, String clientId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsForCrossReferenceByDateById(Integer conditionId, Integer ilId, String clientId, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate);

	void getUpdatedTargetMappedQuery(List<String> query, JdbcTemplate clientJdbcTemplate);

	void deleteCustomTempTableByMappingId(Integer packageId, String mappingId, String clientId, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate2);

	List<ELTClusterLogsInfo> getEltInitiateInfo(int executionId, JdbcTemplate clientAppDbJdbcTemplate);

	void editTargetMappedQuery(String query, JdbcTemplate clientJdbcTemplate);

	List<DataTypesInfo> getDataTypesList();

	Message deleteUserPackage(int userId, int packageId,  JdbcTemplate clientJdbcTemplate);

	int getPackageExecutionCount(int packageId, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<PackageExecution> getPackageExecutionByPagination(Integer packageId,int offset,int limit, JdbcTemplate clientJdbctemplate);
	
	String getWsSourceMappingIdsByMappingId(int mappindId, Integer packageId, JdbcTemplate clientAppDbJdbcTemplate);
	
	int updateWsJoinUrlAtPackageSourceMapping(int mappindId,Integer packageId,String wsJoinUrl, JdbcTemplate clientAppDbJdbcTemplate);
	
	String getSslFileNamesByConId(int conId, JdbcTemplate clientAppDbJdbcTemplate);
}
