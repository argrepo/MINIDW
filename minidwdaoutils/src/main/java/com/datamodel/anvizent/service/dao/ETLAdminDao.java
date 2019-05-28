/**
 * 
 */
package com.datamodel.anvizent.service.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.AllMappingInfo;
import com.datamodel.anvizent.service.model.AppDBVersionTableScripts;
import com.datamodel.anvizent.service.model.ClientConfigurationSettings;
import com.datamodel.anvizent.service.model.ClientCurrencyMapping;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.ClientSpecificILDLJars;
import com.datamodel.anvizent.service.model.CommonJob;
import com.datamodel.anvizent.service.model.ContextParameter;
import com.datamodel.anvizent.service.model.CurrencyIntegration;
import com.datamodel.anvizent.service.model.CurrencyList;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.DefaultTemplates;
import com.datamodel.anvizent.service.model.DlKpiMapping;
import com.datamodel.anvizent.service.model.ERP;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.ETLJobContextParam;
import com.datamodel.anvizent.service.model.ErrorLog;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.GeneralSettings;
import com.datamodel.anvizent.service.model.HybridClientsGrouping;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.IlDlMapping;
import com.datamodel.anvizent.service.model.IlPrebuildQueriesMapping;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.Internalization;
import com.datamodel.anvizent.service.model.Kpi;
import com.datamodel.anvizent.service.model.MiddleLevelManager;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.MultiClientInsertScriptsExecution;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.ServerConfigurations;
import com.datamodel.anvizent.service.model.TableScripts;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.VersionUpgrade;
import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.service.model.WebServiceILMapping;
import com.datamodel.anvizent.service.model.WsTemplateAuthRequestParams;

/**
 * @author rakesh.gajula
 *
 */
public interface ETLAdminDao {

	int getExistContextParameter(String parameterName, JdbcTemplate clientAppDbJdbcTemplate);

	int createContextParameter(ETLAdmin eTLAdmin, String parameterName, JdbcTemplate clientAppDbJdbcTemplate);

	List<ContextParameter> getContextParameters(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	ContextParameter getContextParametersById(int paramId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveEtlDlIlMapping(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	List<Map<String, Object>> getContextParamValue(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveIlInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	int saveDlInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	int saveEtlDlContextParams(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	int saveEtlDlJobsmapping(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	int saveDlClientidMapping(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	List<DLInfo> getDlClientidMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<DLInfo> getVerticalMappedDLsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	void deleteDlClientidMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	void deleteEtlDlIlMappingByDlId(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	List<DLInfo> getDLInfoById(int dLId, JdbcTemplate clientAppDbJdbcTemplate);

	List<String> getFilesByDLId(int dLId, JdbcTemplate clientAppDbJdbcTemplate);

	List<ETLJobContextParam> getParamsByDLId(int dLId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateDLInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteDLFileInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	List<ILConnectionMapping> getDatabaseDetailsByILId(int iLId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateILQuery(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	ETLAdmin getClientSourceDetails(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	void deleteClientSourceDetails(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int getMaxFileSize( JdbcTemplate clientAppDbJdbcTemplate);

	int updateFileSettings(FileSettings fileSettings, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteILById(int iLId, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteDLById(int dLId, JdbcTemplate clientAppDbJdbcTemplate);

	public List<Map<String, String>> getTemplate( JdbcTemplate clientAppDbJdbcTemplate);

	public List<ErrorLog> getTopErrorLog( JdbcTemplate clientAppDbJdbcTemplate);

	public int addNewILAndXrefTemplateInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate);

	public int updateILAndXrefTemplateInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate);

	public ErrorLog getClientErrorLogById(int id, JdbcTemplate clientAppDbJdbcTemplate);

	public List<ErrorLog> searchClientErrorLog(ErrorLog errorLog, JdbcTemplate clientAppDbJdbcTemplate);

	public int createWebService(WebService webservice, JdbcTemplate clientAppDbJdbcTemplate);

	int createNewVertical(Industry industry, JdbcTemplate clientAppDbJdbcTemplate);

	List<Industry> getExistingVerticals( JdbcTemplate clientAppDbJdbcTemplate);

	Industry getVerticalDetailsById(int industryId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateVerticalById(Industry industry, JdbcTemplate clientAppDbJdbcTemplate);

	List<Kpi> getExistingkpis( JdbcTemplate clientAppDbJdbcTemplate);

	int createNewKpi(Kpi kpi, JdbcTemplate clientAppDbJdbcTemplate);

	Kpi getKpiDetailsById(int kpiId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateKpiById(Kpi kpi, JdbcTemplate clientAppDbJdbcTemplate);

	List<ILInfo> getExistingILsInfo( JdbcTemplate clientAppDbJdbcTemplate);

	ILInfo getILInfoById(int iLId, JdbcTemplate clientAppDbJdbcTemplate);

	ILInfo getClientSpecificILInfoById(int ilId, int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<ETLJobContextParam> getContextParamsByILId(int iLId, JdbcTemplate clientAppDbJdbcTemplate);

	String getJobFilesByILId(int iLId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateILDetailsById(ILInfo iLInfo, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteILContextParams(int iLId, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteILJobFileInfo(int iLId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveEtlIlContextParams(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	int saveILJobFileInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	List<Industry> getVerticalsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteClientVerticalMappingById(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveClientVerticalMapping(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteClientVerticalMappedDLsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<Database> getAllDatabases( JdbcTemplate clientAppDbJdbcTemplate);

	List<Database> getDatabasesByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<Database> getConnectorsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<Integer> getClientsByDatabaseId(int databaseId, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteClientDatabaseMappingById(int clientId, String columnName, JdbcTemplate clientAppDbJdbcTemplate);

	int saveClientDatabaseMapping(int clientId, List<String> databases, Modification modification, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteClientConnectorMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveDatabaseClientMapping(int databaseId, List<String> clientIds, Modification modification, JdbcTemplate clientAppDbJdbcTemplate);

	List<Database> getDatabaseMappedConnectors(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteClientConnectorMappingById(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveClientConnectorMapping(int clientId, List<String> connectorIds, Modification modification, JdbcTemplate clientAppDbJdbcTemplate);

	List<ILInfo> getILInfoByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveClientSpecificIL(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate);

	int updateClientSpecificIL(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate);

	int updateClientSpecificILToDefault(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate);

	List<DLInfo> getDLInfoByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	DLInfo getClientSpecificDLInfoById(int dLId, int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveClientSpecificDLInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate);

	int updateClientSpecificDLInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate);

	int updateClientSpecificDLToDefault(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate);

	AllMappingInfo getAllmappingInfoById(int clientId, JdbcTemplate clientAppDbJdbcTemplate, JdbcTemplate commonDbJdbcTemplate);

	List<CommonJob> getCommonJobInfo( JdbcTemplate clientAppDbJdbcTemplate);

	int saveCommonJobInfo(CommonJob commonJob, JdbcTemplate clientAppDbJdbcTemplate);

	CommonJob getCommonJobInfoById(int id, JdbcTemplate clientAppDbJdbcTemplate);

	int updateCommonJobInfo(CommonJob commonJob, JdbcTemplate clientAppDbJdbcTemplate);

	Map<Integer, String> getAllWebServices( JdbcTemplate clientAppDbJdbcTemplate);

	WebService getWebServiceAuthDetailsById(int webServiceId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveWsILMappingDetails(WebServiceILMapping webServiceILMapping, JdbcTemplate clientAppDbJdbcTemplate);

	List<WebServiceApi> getWSILMappingDetailsById(int wsTemplateId, int iLId, JdbcTemplate clientAppDbJdbcTemplate);

	public int updateWebServicesById(WebService webservice, JdbcTemplate clientAppDbJdbcTemplate);

	List<String> getWebserviceClients(int webServiceId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveWebserviceClientMapping(int webserviceId, String clients, ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteWebserviceClientMapping(int webserviceId, JdbcTemplate clientAppDbJdbcTemplate);

	List<WebService> getWebserviceByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveClientWebserviceMapping(int clientId, List<Integer> webServiceIds, Modification modification, JdbcTemplate clientAppDbJdbcTemplate);

	Map<Integer, String> getAllKpis( JdbcTemplate clientAppDbJdbcTemplate);

	List<String> getKpiListByDlId(int dlId, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteDlKpiMapping(int dlId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveDlKpiMapping(int dlId, String kpis, ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate);

	List<ILConnectionMapping> getDefaultQuery(int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate);

	String getIlQueryById(int ilId, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate);

	String getIlincrementalQueryById(int ilId, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate);

	String gethistoryLoadById(int ilId, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate);

	String geMaxDateQueryById(Integer ilId, Integer databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate);

	Map<Integer, String> getNotMappedILsByDBTypeId(int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveILDefaultQuery(ILConnectionMapping ilconnectionMapping, JdbcTemplate clientAppDbJdbcTemplate);

	ILConnectionMapping editILDefaultQuery(int ilid, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateILDefaultQuery(ILConnectionMapping ilconnectionMapping, JdbcTemplate clientAppDbJdbcTemplate);

	List<Database> getDatabase( JdbcTemplate clientAppDbJdbcTemplate);

	Database getDBDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate);

	int updateDatabase(Database database, JdbcTemplate clientAppDbJdbcTemplate);

	int createDB(Database database, JdbcTemplate clientAppDbJdbcTemplate);

	List<Database> getconnector( JdbcTemplate clientAppDbJdbcTemplate);

	Database getConnectorDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate);

	int updateConnector(Database database, JdbcTemplate clientAppDbJdbcTemplate);

	int createConnector(Database database, JdbcTemplate clientAppDbJdbcTemplate);

	int createContextParams(ContextParameter contextParameter, JdbcTemplate clientAppDbJdbcTemplate);

	int updateContextParams(ContextParameter contextParameter, JdbcTemplate clientAppDbJdbcTemplate);

	List<TableScriptsForm> getTableScripts( JdbcTemplate clientAppDbJdbcTemplate);

	int addTableScripts(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate);

	int updateTableScripts(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate);

	int updateScriptHistoryTable(int tableScriptId, JdbcTemplate clientAppDbJdbcTemplate);

	List<ILInfo> getAllILs(int dlId, JdbcTemplate clientAppDbJdbcTemplate);

	List<Kpi> getAllKpi(int dlId, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteEtlDlJobsmapping(int dlId, String fileName, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteDLContextParams(int dLId, JdbcTemplate clientAppDbJdbcTemplate);

	List<TableScripts> getTableScriptsByClient(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int addTableScriptsMapping(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate);

	TableScriptsForm getTableScriptsMappingById(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate);

	int updateTableScriptsMapping(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate);

	int saveClientTableScriptsMapping(TableScriptsForm tableScriptsForm, JdbcTemplate clientAppDbJdbcTemplate,JdbcTemplate commonJdbcTemplate);

	int deleteClientTableScriptsMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate,JdbcTemplate commonJdbcTemplate);

	List<TableScripts> getTableScriptsMappingByClient(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<TableScripts> getSqlScriptByScriptIds(int clientId, List<Integer> scriptIds, JdbcTemplate clientAppDbJdbcTemplate);

	int updateTableScriptsMappingIsExecuted(int clientId, int scriptId, boolean isExecuted, Modification modification, JdbcTemplate clientAppDbJdbcTemplate,JdbcTemplate commonJdbcTemplate);

	String getJobFilesByDLId(int dLId, JdbcTemplate clientAppDbJdbcTemplate);

	int getExecutedtableScriptCount(int scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	int getMappedTableScriptCount(int scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	void updateTableScriptsMappingIsNotExecutedErrorMsg(int clientId, int scriptId, String errorMsg, Modification modification, JdbcTemplate clientAppDbJdbcTemplate);

	String getTableScriptsMappingIsNotExecutedErrorMsg(int clientId, int scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateTableScriptsMappingIsError(int clientId, int scriptId, boolean isExecuted, Modification modification, JdbcTemplate clientAppDbJdbcTemplate,JdbcTemplate commonJdbcTemplate);

	int saveDefaultTemplateMasterMappingData(int templateId, String mappingType, List<Integer> verticalIds, Modification modification, JdbcTemplate clientAppDbJdbcTemplate);

	void updateInstantTableScriptExecution(TableScriptsForm tableScriptsForm, String status, String statusMessage, Modification modification, JdbcTemplate clientAppDbJdbcTemplate);

	List<TableScriptsForm> getPreviousExecutedScripts(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	String getInstantTableScriptsIsNotExecutedErrorMsg(int id, int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	String getPreviousTableScriptView(int id, int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<DefaultTemplates> getAllDefaultTemplatesInfo( JdbcTemplate clientAppDbJdbcTemplate);

	int createDefaultTemplate(DefaultTemplates defaultTemplates, JdbcTemplate clientAppDbJdbcTemplate);

	DataResponse getDefaultTemplateMasterMappedData(int templateId, String mappingType, JdbcTemplate clientAppDbJdbcTemplate);

	void deleteDefaultTemplateMasterMappedData(int templateId, String mappingType, JdbcTemplate clientAppDbJdbcTemplate);

	DefaultTemplates getDefaultTemplateInfoById(int templateId, JdbcTemplate clientAppDbJdbcTemplate);

	void deleteClientWSMappings(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateWsILMappingDetails(WebServiceILMapping webServiceILMapping, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteWSILMappingDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate);

	List<ILInfo> getILsByWSMappingId(int wsTemplateId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateDefaultTemplate(DefaultTemplates defaultTemplates, JdbcTemplate clientAppDbJdbcTemplate);

	int saveGeneralSettings(GeneralSettings generalSettings, JdbcTemplate clientAppDbJdbcTemplate);

	GeneralSettings getSettingsInfoByID(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateGeneralSettings(GeneralSettings generalSettings, JdbcTemplate clientAppDbJdbcTemplate);

	int saveclientConfigSettings(ClientConfigurationSettings clientConfigurationSettings, JdbcTemplate clientAppDbJdbcTemplate);

	ClientConfigurationSettings getConfigSettingsInfoByID(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateclientConfigSettings(ClientConfigurationSettings clientConfigurationSettings, JdbcTemplate clientAppDbJdbcTemplate);

	List<ServerConfigurations> getAllServerConfigurations(JdbcTemplate clientAppDbJdbcTemplate);

	int saveServerConfigurationDetails(ServerConfigurations serverConfigurations, Modification modification, JdbcTemplate clientAppDbJdbcTemplate);

	int updateServerConfigurationDetails(ServerConfigurations serverConfigurations, Modification modification, JdbcTemplate clientAppDbJdbcTemplate);

	ServerConfigurations getServerConfigurationDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate);

	Map<Integer, String> getUsersByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<Package> userPackageList(int clientUserId, JdbcTemplate clientAppDbJdbcTemplate);
	

	List<String> getAllClientsByServer(JdbcTemplate jdbctemplate, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<Package> getPackageList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<User> getUsersList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<ILConnection> getDatabaseConnectionList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<WebServiceConnectionMaster> getWsConnectionList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<ClientSpecificILDLJars> getClientSpecificILJarsList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate); 
	
	List<ClientSpecificILDLJars> getClientSpecificDLJarsList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate); 
	
	List<Industry> getTemplatesList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<TableScripts> getTableScriptsList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate);
		

	int saveCurrencyIntegration(CurrencyIntegration currencyIntegration, JdbcTemplate clientAppDbJdbcTemplate);

	CurrencyIntegration getCurrencyIntegration(JdbcTemplate clientAppDbJdbcTemplate);

	int updateCurrencyIntegration(CurrencyIntegration currencyIntegration, JdbcTemplate clientAppDbJdbcTemplate);	

	List<VersionUpgrade> getVersionUpgrade(JdbcTemplate clientAppDbJdbcTemplate);

	int createVersionUpgrade(VersionUpgrade versionUpgrade, JdbcTemplate clientAppDbJdbcTemplate);

	VersionUpgrade getVersionUpgradeDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate);
	
	VersionUpgrade getVersionUpgradeDetailsByVersionNumber(String versionNumber, JdbcTemplate clientAppDbJdbcTemplate);

	int updateVersionUpgrade(VersionUpgrade versionUpgrade, JdbcTemplate clientAppDbJdbcTemplate);

	List<ClientCurrencyMapping> getClientCurrencyMapping(JdbcTemplate clientAppDbJdbcTemplate);
	
	List<ClientCurrencyMapping> getClientCurrencyMappingWithCurrencyIds(JdbcTemplate clientAppDbJdbcTemplate);

	int createClientCurrencyMapping(ClientCurrencyMapping clientCurrencyMapping, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate);

	ClientCurrencyMapping getclientCurrencyMappingDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate);

	int createCurrencyMapping(ClientCurrencyMapping clientCurrencyMapping, JdbcTemplate clientAppDbJdbcTemplate);

	ClientCurrencyMapping getDefaultCurrencyInfoById(int templateId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateCurrencyMapping(ClientCurrencyMapping clientCurrencyMapping, JdbcTemplate clientAppDbJdbcTemplate);

	List<CurrencyList> getCurrencyList(JdbcTemplate clientAppDbJdbcTemplate);
	
	int updateAllVersionsToOld( JdbcTemplate clientAppDbJdbcTemplate);

	ClientCurrencyMapping getclientCurrencyMappingDetailsByClientId(int id, JdbcTemplate clientAppDbJdbcTemplate);

	int savePropertiesKeyValuePairs(Internalization internalization, JdbcTemplate clientAppDbJdbcTemplate);

	List<Internalization> getPropertiesList(JdbcTemplate clientAppDbJdbcTemplate);

	Internalization getpropertiesKeyValuePairsById(Integer id, JdbcTemplate clientAppDbJdbcTemplate);

	List<S3BucketInfo> getS3InfoList(JdbcTemplate clientAppDbJdbcTemplate);

	int saveS3BucketInfo(S3BucketInfo s3BucketInfo, JdbcTemplate clientAppDbJdbcTemplate);

	S3BucketInfo getS3BucketInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate);

	Map<Integer, String> getBucketList(JdbcTemplate clientAppDbJdbcTemplate);

	int saveClientMapping(S3BucketInfo s3BucketInfo, JdbcTemplate clientAppDbJdbcTemplate);

	List<MiddleLevelManager> getMiddleLevelManager(JdbcTemplate clientAppDbJdbcTemplate);

	int updateMiddleLevelManager(MiddleLevelManager middleLevelManager, JdbcTemplate clientAppDbJdbcTemplate);

	int createMiddleLevelManager(MiddleLevelManager middleLevelManager, JdbcTemplate clientAppDbJdbcTemplate);

	MiddleLevelManager getMiddleLevelManagerDetailsById(JdbcTemplate clientAppDbJdbcTemplate);
	

	List<AppDBVersionTableScripts> getAppDbVersionTableScripts(JdbcTemplate clientAppDbJdbcTemplate);

	int createAppDBVersionTableScripts(AppDBVersionTableScripts appDBVersionTableScripts,
			JdbcTemplate clientAppDbJdbcTemplate);

	int updateAppDBVersionTableScriptsInfo(AppDBVersionTableScripts appDBVersionTableScripts,
			JdbcTemplate clientAppDbJdbcTemplate);

	AppDBVersionTableScripts getAppDbVersionTableScriptDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate);

	String getAppDBScriptById(Integer id, JdbcTemplate clientAppDbJdbcTemplate);

	String getMinidwDBScriptById(Integer id, JdbcTemplate clientAppDbJdbcTemplate);

	int updateDefaultStatus(JdbcTemplate clientAppDbJdbcTemplate);

	
	List<HybridClientsGrouping> getHybridClientsGrouping(JdbcTemplate clientAppDbJdbcTemplate);

	int createHybridClientsGroupingInfo(HybridClientsGrouping hybridClientsGrouping,JdbcTemplate clientAppDbJdbcTemplate);

	HybridClientsGrouping getHybridClientGroupingDetailsById(long id, JdbcTemplate clientAppDbJdbcTemplate);

	int updateHybridClientsGroupingInfo(HybridClientsGrouping hybridClientsGrouping,
			JdbcTemplate clientAppDbJdbcTemplate);


	int saveClientsInstantScriptExecution(TableScriptsForm tableScriptForm, JdbcTemplate jdbcTemplate);
	
	List<TableScriptsForm> getClientsInstantScriptExecution(JdbcTemplate clientAppDbJdbcTemplate) ;

	int saveInstantScriptExecutionOfClient(TableScriptsForm tableScriptForm, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<TableScriptsForm> getInstantScriptExecutionOfClient(int id, JdbcTemplate clientAppDbJdbcTemplate) ;
	
	String getSqlScriptByExecutionId(int scriptId,JdbcTemplate clientAppDbJdbcTemplate);

	ClientJobExecutionParameters getclientJobExecutionParametersDetailsById(JdbcTemplate clientAppDbJdbcTemplate);
	
	int saveSchedularMasterClientMapping(int clientId, List<Integer> schedularMasterIds, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate);
	
	int createClientJobExecutionParams(ClientJobExecutionParameters clientJobExecutionParameters,
			JdbcTemplate clientAppDbJdbcTemplate);

	int updateClientJobExecutionParams(ClientJobExecutionParameters clientJobExecutionParameters,
			JdbcTemplate clientAppDbJdbcTemplate);

	int executeInsertScripts(MultiClientInsertScriptsExecution multiClientInsertScriptsExecution,JdbcTemplate clientAppDbJdbcTemplate);

	List<String> getDDlTableCreateScripts(String query, JdbcTemplate clientAppDbJdbcTemplate);

	List<SchedulerMaster> getScheduledMasterInfoList(JdbcTemplate clientAppDbJdbcTemplate);

	List<FileSettings> getFileSettingsList(JdbcTemplate clientAppDbJdbcTemplate);

	int updateFilesetting( Integer id, JdbcTemplate clientAppDbJdbcTemplate);

	List<Map<String, Object>> getWishList(JdbcTemplate clientAppDbJdbcTemplate);

	Map<String, Integer> transeferDataERPtoClient(String clientId, String referenceClientName,Integer erpId, String referenceUserId, List<String> erptableList, JdbcTemplate clientAppDbJdbcTemplate);

	List<ERP> getERPList( JdbcTemplate clientAppDbJdbcTemplate);
	
	List<DLInfo> getDlInfoList( JdbcTemplate clientAppDbJdbcTemplate);
	
	List<IlPrebuildQueriesMapping> getIlPrebuildQueriesMapping( JdbcTemplate clientAppDbJdbcTemplate);
	
	List<IlDlMapping> getIlDlMapping(JdbcTemplate clientAppDbJdbcTemplate);
	
	List<DlKpiMapping> getDlKpiMapping(JdbcTemplate clientAppDbJdbcTemplate);
	
	List<WsTemplateAuthRequestParams> getWsTemplateAuthRequestParams(JdbcTemplate clientAppDbJdbcTemplate);
	
	List<WebServiceApi> getWsMappingList(JdbcTemplate clientAppDbJdbcTemplate);
	
	List<String> getErpTableList(JdbcTemplate clientAppDbJdbcTemplate);
	
	List<String> getCmAndCstableList(JdbcTemplate clientAppDbJdbcTemplate);
	
	List<User> getUserListFromClient(JdbcTemplate clientAppDbJdbcTemplate,int clientId);
	
	int updateErpToClientMigrationMapping(JdbcTemplate clientAppDbJdbcTemplate,int clientId,int erpId,int userId,ETLAdmin eTLAdmin);
	
	int wishListToWishListAccessOnlyMapping(String[] clientInfoArray, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<String> getMasterTablesList(JdbcTemplate clientAppDbJdbcTemplate);
	
	Map<String, Integer> transeferDataERPtoClientForMasterTables(List<String> tablesList,int erpId,String clientId, JdbcTemplate clientAppDbJdbcTemplate);
	
	int getErpIdFromNameAndVersion(JdbcTemplate clientAppDbJdbcTemplate, String name, String version);
	
	int encryptWebServiceAuthParams(JdbcTemplate clientAppDbJdbcTemplate,String query);
}