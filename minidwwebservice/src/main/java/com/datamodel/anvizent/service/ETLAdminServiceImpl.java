/**
 * 
 */
package com.datamodel.anvizent.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anvizent.client.metadata.update.DBToSQLExecution;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.ScriptRunner;
import com.datamodel.anvizent.service.dao.ETLAdminDao;
import com.datamodel.anvizent.service.dao.UserDao;
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
@Service
public class ETLAdminServiceImpl implements ETLAdminService {

	protected static final Log LOGGER = LogFactory.getLog(ETLAdminServiceImpl.class);

	@Autowired
	private ETLAdminDao etlAdminDao;

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int createContextParameter(ETLAdmin eTLAdmin, String parameterName, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createContextParameter(eTLAdmin, parameterName, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ContextParameter> getContextParameters(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getContextParameters(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public ContextParameter getContextParametersById(int paramId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getContextParametersById(paramId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveEtlDlIlMapping(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.saveEtlDlIlMapping(eTLAdmin, clientAppDbJdbcTemplate);

	}

	@Override
	public List<Map<String, Object>> getContextParamValue(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getContextParamValue(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveIlInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.saveIlInfo(eTLAdmin, clientAppDbJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveDlInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.saveDlInfo(eTLAdmin, clientAppDbJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveEtlDlContextParams(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.saveEtlDlContextParams(eTLAdmin, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveEtlDlJobsmapping(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.saveEtlDlJobsmapping(eTLAdmin, clientAppDbJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveDlClientidMapping(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.saveDlClientidMapping(eTLAdmin, clientAppDbJdbcTemplate);
	}

	@Override
	public List<DLInfo> getDlClientidMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getDlClientidMapping(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<DLInfo> getVerticalMappedDLsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getVerticalMappedDLsByClientId(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public ETLAdmin getClientSourceDetails(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getClientSourceDetails(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public int getExistContextParameter(String parameterName, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getExistContextParameter(parameterName, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void deleteDlClientidMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		etlAdminDao.deleteDlClientidMapping(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void deleteEtlDlIlMappingByDlId(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {

		etlAdminDao.deleteEtlDlIlMappingByDlId(eTLAdmin, clientAppDbJdbcTemplate);

	}

	@Override
	public List<DLInfo> getDLInfoById(int dLId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDLInfoById(dLId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<String> getFilesByDLId(int dLId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getFilesByDLId(dLId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ETLJobContextParam> getParamsByDLId(int dLId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getParamsByDLId(dLId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateDLInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateDLInfo(eTLAdmin, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteDLFileInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteDLFileInfo(eTLAdmin, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ILConnectionMapping> getDatabaseDetailsByILId(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDatabaseDetailsByILId(iLId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateILQuery(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateILQuery(eTLAdmin, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void deleteClientSourceDetails(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		etlAdminDao.deleteClientSourceDetails(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public int getMaxFileSize(JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getMaxFileSize(clientAppDbJdbcTemplate);
	}

	@Override
	public int updateFileSettings(FileSettings fileSettings, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.updateFileSettings(fileSettings, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteILById(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.deleteILById(iLId, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteDLById(int dLId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.deleteDLById(dLId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Map<String, String>> getTemplate(JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getTemplate(clientAppDbJdbcTemplate);
	}

	@Override
	public int addNewILAndXrefTemplateInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.addNewILAndXrefTemplateInfo(clientData, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateILAndXrefTemplateInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.updateILAndXrefTemplateInfo(clientData, clientAppDbJdbcTemplate);
	}

	public List<ErrorLog> getTopErrorLog(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getTopErrorLog(clientAppDbJdbcTemplate);
	}

	public ErrorLog getClientErrorLogById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getClientErrorLogById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ErrorLog> searchClientErrorLog(ErrorLog errorLog, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.searchClientErrorLog(errorLog, clientAppDbJdbcTemplate);
	}

	@Override
	public int createWebService(WebService webservice, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createWebService(webservice, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int createNewVertical(Industry industry, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createNewVertical(industry, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Industry> getExistingVerticals(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getExistingVerticals(clientAppDbJdbcTemplate);
	}

	@Override
	public Industry getVerticalDetailsById(int industryId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getVerticalDetailsById(industryId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateVerticalById(Industry industry, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateVerticalById(industry, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Kpi> getExistingkpis(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getExistingkpis(clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int createNewKpi(Kpi kpi, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createNewKpi(kpi, clientAppDbJdbcTemplate);
	}

	@Override
	public Kpi getKpiDetailsById(int kpiId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getKpiDetailsById(kpiId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateKpiById(Kpi kpi, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateKpiById(kpi, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ILInfo> getExistingILsInfo(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getExistingILsInfo(clientAppDbJdbcTemplate);
	}

	@Override
	public ILInfo getILInfoById(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getILInfoById(iLId, clientAppDbJdbcTemplate);
	}

	@Override
	public ILInfo getClientSpecificILInfoById(int iLId, int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getClientSpecificILInfoById(iLId, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ETLJobContextParam> getContextParamsByILId(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getContextParamsByILId(iLId, clientAppDbJdbcTemplate);
	}

	@Override
	public String getJobFilesByILId(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getJobFilesByILId(iLId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateILDetailsById(ILInfo iLInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateILDetailsById(iLInfo, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int deleteILContextParams(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteILContextParams(iLId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int deleteILJobFileInfo(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteILJobFileInfo(iLId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveEtlIlContextParams(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveEtlIlContextParams(eTLAdmin, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveILJobFileInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveILJobFileInfo(eTLAdmin, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Industry> getVerticalsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getVerticalsByClientId(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int deleteClientVerticalMappingById(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteClientVerticalMappingById(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveClientVerticalMapping(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveClientVerticalMapping(clientData, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int deleteClientVerticalMappedDLsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteClientVerticalMappedDLsByClientId(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Database> getAllDatabases(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getAllDatabases(clientAppDbJdbcTemplate);
	}

	@Override
	public List<Database> getDatabasesByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDatabasesByClientId(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Database> getConnectorsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getConnectorsByClientId(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Integer> getClientsByDatabaseId(int databaseId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getClientsByDatabaseId(databaseId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int deleteClientDatabaseMappingById(int clientId, String columnName, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteClientDatabaseMappingById(clientId, columnName, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveClientDatabaseMapping(int clientId, List<String> databases, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveClientDatabaseMapping(clientId, databases, modification, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int deleteClientConnectorMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteClientConnectorMapping(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveDatabaseClientMapping(int databaseId, List<String> clientIds, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveDatabaseClientMapping(databaseId, clientIds, modification, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Database> getDatabaseMappedConnectors(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDatabaseMappedConnectors(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int deleteClientConnectorMappingById(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteClientConnectorMappingById(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveClientConnectorMapping(int clientId, List<String> connectorIds, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveClientConnectorMapping(clientId, connectorIds, modification, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ILInfo> getILInfoByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getILInfoByClientId(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveClientSpecificIL(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveClientSpecificIL(clientData, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateClientSpecificIL(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateClientSpecificIL(clientData, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateClientSpecificILToDefault(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateClientSpecificILToDefault(clientData, clientAppDbJdbcTemplate);
	}

	@Override
	public List<DLInfo> getDLInfoByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDLInfoByClientId(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public DLInfo getClientSpecificDLInfoById(int dLId, int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getClientSpecificDLInfoById(dLId, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveClientSpecificDLInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveClientSpecificDLInfo(clientData, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateClientSpecificDLInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateClientSpecificDLInfo(clientData, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateClientSpecificDLToDefault(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateClientSpecificDLToDefault(clientData, clientAppDbJdbcTemplate);
	}

	@Override
	public AllMappingInfo getAllmappingInfoById(int clientId, JdbcTemplate clientAppDbJdbcTemplate,
			JdbcTemplate commonDbJdbcTemplate) {
		return etlAdminDao.getAllmappingInfoById(clientId, clientAppDbJdbcTemplate, commonDbJdbcTemplate);
	}

	@Override
	public List<CommonJob> getCommonJobInfo(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getCommonJobInfo(clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveCommonJobInfo(CommonJob commonJob, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveCommonJobInfo(commonJob, clientAppDbJdbcTemplate);
	}

	@Override
	public CommonJob getCommonJobInfoById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getCommonJobInfoById(id, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateCommonJobInfo(CommonJob commonJob, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateCommonJobInfo(commonJob, clientAppDbJdbcTemplate);
	}

	@Override
	public Map<Integer, String> getAllWebServices(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getAllWebServices(clientAppDbJdbcTemplate);
	}

	@Override
	public WebService getWebServiceAuthDetailsById(int webServiceId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getWebServiceAuthDetailsById(webServiceId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateWebServicesById(WebService webservice, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateWebServicesById(webservice, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveWsILMappingDetails(WebServiceILMapping webServiceILMapping, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveWsILMappingDetails(webServiceILMapping, clientAppDbJdbcTemplate);
	}

	@Override
	public List<WebServiceApi> getWSILMappingDetailsById(int wsTemplateId, int iLId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getWSILMappingDetailsById(wsTemplateId, iLId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<String> getWebserviceClients(int webServiceId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getWebserviceClients(webServiceId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveWebserviceClientMapping(int webserviceId, String clients, ETLAdmin eTLAdmin,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveWebserviceClientMapping(webserviceId, clients, eTLAdmin, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteWebserviceClientMapping(int webserviceId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteWebserviceClientMapping(webserviceId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<WebService> getWebserviceByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getWebserviceByClientId(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveClientWebserviceMapping(int clientId, List<Integer> webServiceIds, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveClientWebserviceMapping(clientId, webServiceIds, modification, clientAppDbJdbcTemplate);
	}

	@Override
	public Map<Integer, String> getAllKpis(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getAllKpis(clientAppDbJdbcTemplate);
	}

	@Override
	public List<String> getKpiListByDlId(int dlId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getKpiListByDlId(dlId, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteDlKpiMapping(int dlId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.deleteDlKpiMapping(dlId, clientAppDbJdbcTemplate);
	}

	@Override
	public int saveDlKpiMapping(int dlId, String kpis, ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.saveDlKpiMapping(dlId, kpis, eTLAdmin, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ILConnectionMapping> getDefaultQuery(int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDefaultQuery(databaseTypeId, clientAppDbJdbcTemplate);
	}

	@Override
	public String getIlQueryById(int ilId, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getIlQueryById(ilId, databaseTypeId, clientAppDbJdbcTemplate);
	}

	@Override
	public String getIlincrementalQueryById(int ilId, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getIlincrementalQueryById(ilId, databaseTypeId, clientAppDbJdbcTemplate);
	}

	@Override
	public String gethistoryLoadById(int ilId, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.gethistoryLoadById(ilId, databaseTypeId, clientAppDbJdbcTemplate);
	}

	@Override
	public String geMaxDateQueryById(Integer ilId, Integer databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.geMaxDateQueryById(ilId, databaseTypeId, clientAppDbJdbcTemplate);
	}

	@Override
	public Map<Integer, String> getNotMappedILsByDBTypeId(int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getNotMappedILsByDBTypeId(databaseTypeId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveILDefaultQuery(ILConnectionMapping ilconnectionMapping, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.saveILDefaultQuery(ilconnectionMapping, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public ILConnectionMapping editILDefaultQuery(int ilid, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.editILDefaultQuery(ilid, databaseTypeId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateILDefaultQuery(ILConnectionMapping ilconnectionMapping, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.updateILDefaultQuery(ilconnectionMapping, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Database> getDatabase(JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getDatabase(clientAppDbJdbcTemplate);
	}

	@Override
	public Database getDBDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getDBDetailsById(id, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateDatabase(Database database, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.updateDatabase(database, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int createDB(Database database, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.createDB(database, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Database> getconnector(JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getconnector(clientAppDbJdbcTemplate);
	}

	@Override
	public Database getConnectorDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getConnectorDetailsById(id, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateConnector(Database database, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.updateConnector(database, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int createConnector(Database database, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.createConnector(database, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int createContextParams(ContextParameter contextParameter, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createContextParams(contextParameter, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateContextParams(ContextParameter contextParameter, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateContextParams(contextParameter, clientAppDbJdbcTemplate);
	}

	@Override
	public List<TableScriptsForm> getTableScripts(JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getTableScripts(clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int addTableScripts(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.addTableScripts(tableScripts, clientAppDbJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateTableScripts(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.updateTableScripts(tableScripts, clientAppDbJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateScriptHistoryTable(int tableScriptId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.updateScriptHistoryTable(tableScriptId, clientAppDbJdbcTemplate);

	}

	@Override
	public List<ILInfo> getAllILs(int dlId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getAllILs(dlId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Kpi> getAllKpi(int dlId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getAllKpi(dlId, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteEtlDlJobsmapping(int dlId, String fileName, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.deleteEtlDlJobsmapping(dlId, fileName, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteDLContextParams(int dLId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.deleteDLContextParams(dLId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<TableScripts> getTableScriptsByClient(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getTableScriptsByClient(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int addTableScriptsMapping(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.addTableScriptsMapping(tableScripts, clientAppDbJdbcTemplate);

	}

	@Override
	public TableScriptsForm getTableScriptsMappingById(TableScriptsForm tableScripts,
			JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getTableScriptsMappingById(tableScripts, clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateTableScriptsMapping(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.updateTableScriptsMapping(tableScripts, clientAppDbJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveClientTableScriptsMapping(TableScriptsForm tableScriptsForm, JdbcTemplate clientAppDbJdbcTemplate,
			JdbcTemplate commonJdbcTemplate) {

		return etlAdminDao.saveClientTableScriptsMapping(tableScriptsForm, clientAppDbJdbcTemplate, commonJdbcTemplate);
	}

	@Override
	public int deleteClientTableScriptsMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate,
			JdbcTemplate commonJdbcTemplate) {

		return etlAdminDao.deleteClientTableScriptsMapping(clientId, clientAppDbJdbcTemplate, commonJdbcTemplate);
	}

	@Override
	public List<TableScripts> getTableScriptsMappingByClient(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getTableScriptsMappingByClient(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<TableScripts> getSqlScriptByScriptIds(int clientId, List<Integer> scriptIds,
			JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getSqlScriptByScriptIds(clientId, scriptIds, clientAppDbJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateTableScriptsMappingIsExecuted(int clientId, int scriptId, boolean isExecuted,
			Modification modification, JdbcTemplate clientAppDbJdbcTemplate, JdbcTemplate commonJdbcTemplate) {

		return etlAdminDao.updateTableScriptsMappingIsExecuted(clientId, scriptId, isExecuted, modification,
				clientAppDbJdbcTemplate, commonJdbcTemplate);

	}

	@Override
	public String getJobFilesByDLId(int dLId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getJobFilesByDLId(dLId, clientAppDbJdbcTemplate);
	}

	@Override
	public int getExecutedtableScriptCount(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getExecutedtableScriptCount(scriptId, clientAppDbJdbcTemplate);

	}

	@Override
	public int getMappedTableScriptCount(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getMappedTableScriptCount(scriptId, clientAppDbJdbcTemplate);

	}

	@Override
	public void updateTableScriptsMappingIsNotExecutedErrorMsg(int clientId, int scriptId, String errorMsg,
			Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		etlAdminDao.updateTableScriptsMappingIsNotExecutedErrorMsg(clientId, scriptId, errorMsg, modification,
				clientAppDbJdbcTemplate);
	}

	@Override
	public String getTableScriptsMappingIsNotExecutedErrorMsg(int clientId, int scriptId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getTableScriptsMappingIsNotExecutedErrorMsg(clientId, scriptId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateTableScriptsMappingIsError(int clientId, int scriptId, boolean isExecuted,
			Modification modification, JdbcTemplate clientAppDbJdbcTemplate, JdbcTemplate commonJdbcTemplate) {
		return etlAdminDao.updateTableScriptsMappingIsError(clientId, scriptId, isExecuted, modification,
				clientAppDbJdbcTemplate, commonJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveDefaultTemplateMasterMappingData(int templateId, String mappingType, List<Integer> masterIds,
			Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveDefaultTemplateMasterMappingData(templateId, mappingType, masterIds, modification,
				clientAppDbJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void updateInstantTableScriptExecution(TableScriptsForm tableScriptsForm, String status,
			String statusMessage, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		etlAdminDao.updateInstantTableScriptExecution(tableScriptsForm, status, statusMessage, modification,
				clientAppDbJdbcTemplate);
	}

	@Override
	public List<TableScriptsForm> getPreviousExecutedScripts(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getPreviousExecutedScripts(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public String getInstantTableScriptsIsNotExecutedErrorMsg(int id, int clientId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getInstantTableScriptsIsNotExecutedErrorMsg(id, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public String getPreviousTableScriptView(int id, int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		return etlAdminDao.getPreviousTableScriptView(id, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<DefaultTemplates> getAllDefaultTemplatesInfo(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getAllDefaultTemplatesInfo(clientAppDbJdbcTemplate);
	}

	@Override
	public int createDefaultTemplate(DefaultTemplates defaultTemplates, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createDefaultTemplate(defaultTemplates, clientAppDbJdbcTemplate);
	}

	@Override
	public DataResponse getDefaultTemplateMasterMappedData(int templateId, String mappingType,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDefaultTemplateMasterMappedData(templateId, mappingType, clientAppDbJdbcTemplate);
	}

	@Override
	public void deleteDefaultTemplateMasterMappedData(int templateId, String mappingType,
			JdbcTemplate clientAppDbJdbcTemplate) {
		etlAdminDao.deleteDefaultTemplateMasterMappedData(templateId, mappingType, clientAppDbJdbcTemplate);
	}

	@Override
	public DefaultTemplates getDefaultTemplateInfoById(int templateId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDefaultTemplateInfoById(templateId, clientAppDbJdbcTemplate);
	}

	@Override
	public void deleteClientWSMappings(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		etlAdminDao.deleteClientWSMappings(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateWsILMappingDetails(WebServiceILMapping webServiceILMapping, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateWsILMappingDetails(webServiceILMapping, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteWSILMappingDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.deleteWSILMappingDetailsById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ILInfo> getILsByWSMappingId(int wsTemplateId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getILsByWSMappingId(wsTemplateId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateDefaultTemplate(DefaultTemplates defaultTemplates, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateDefaultTemplate(defaultTemplates, clientAppDbJdbcTemplate);
	}

	@Override
	public int saveGeneralSettings(GeneralSettings generalSettings, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveGeneralSettings(generalSettings, clientAppDbJdbcTemplate);
	}

	@Override
	public GeneralSettings getSettingsInfoByID(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getSettingsInfoByID(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateGeneralSettings(GeneralSettings generalSettings, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateGeneralSettings(generalSettings, clientAppDbJdbcTemplate);
	}

	@Override
	public int saveclientConfigSettings(ClientConfigurationSettings clientConfigurationSettings,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveclientConfigSettings(clientConfigurationSettings, clientAppDbJdbcTemplate);
	}

	@Override
	public ClientConfigurationSettings getConfigSettingsInfoByID(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getConfigSettingsInfoByID(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateclientConfigSettings(ClientConfigurationSettings clientConfigurationSettings,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateclientConfigSettings(clientConfigurationSettings, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ServerConfigurations> getAllServerConfigurations(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getAllServerConfigurations(clientAppDbJdbcTemplate);
	}

	@Override
	public int saveServerConfigurationDetails(ServerConfigurations serverConfigurations, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveServerConfigurationDetails(serverConfigurations, modification, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateServerConfigurationDetails(ServerConfigurations serverConfigurations, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateServerConfigurationDetails(serverConfigurations, modification,
				clientAppDbJdbcTemplate);
	}

	@Override
	public ServerConfigurations getServerConfigurationDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getServerConfigurationDetailsById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public Map<Integer, String> getUsersByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getUsersByClientId(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Package> userPackageList(int clientUserId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.userPackageList(clientUserId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<String> getAllClientsByServer(JdbcTemplate jdbctemplate, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getAllClientsByServer(jdbctemplate, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Package> getPackageList(JdbcTemplate jdbctemplate, String clientId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getPackageList(jdbctemplate, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<User> getUsersList(JdbcTemplate jdbctemplate, String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getUsersList(jdbctemplate, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ILConnection> getDatabaseConnectionList(JdbcTemplate jdbctemplate, String clientId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDatabaseConnectionList(jdbctemplate, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<WebServiceConnectionMaster> getWsConnectionList(JdbcTemplate jdbctemplate, String clientId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getWsConnectionList(jdbctemplate, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ClientSpecificILDLJars> getClientSpecificILJarsList(JdbcTemplate jdbctemplate, String clientId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getClientSpecificILJarsList(jdbctemplate, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ClientSpecificILDLJars> getClientSpecificDLJarsList(JdbcTemplate jdbctemplate, String clientId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getClientSpecificDLJarsList(jdbctemplate, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Industry> getTemplatesList(JdbcTemplate jdbctemplate, String clientId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getTemplatesList(jdbctemplate, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<TableScripts> getTableScriptsList(JdbcTemplate jdbctemplate, String clientId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getTableScriptsList(jdbctemplate, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public int saveCurrencyIntegration(CurrencyIntegration currencyIntegration, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveCurrencyIntegration(currencyIntegration, clientAppDbJdbcTemplate);
	}

	@Override
	public CurrencyIntegration getCurrencyIntegration(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getCurrencyIntegration(clientAppDbJdbcTemplate);
	}

	@Override
	public int updateCurrencyIntegration(CurrencyIntegration currencyIntegration,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateCurrencyIntegration(currencyIntegration, clientAppDbJdbcTemplate);
	}

	@Override
	public List<VersionUpgrade> getVersionUpgrade(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getVersionUpgrade(clientAppDbJdbcTemplate);
	}

	@Override
	public int createVersionUpgrade(VersionUpgrade versionUpgrade, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createVersionUpgrade(versionUpgrade, clientAppDbJdbcTemplate);
	}

	@Override
	public VersionUpgrade getVersionUpgradeDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getVersionUpgradeDetailsById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public VersionUpgrade getVersionUpgradeDetailsByVersionNumber(String versionNumber,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getVersionUpgradeDetailsByVersionNumber(versionNumber, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateVersionUpgrade(VersionUpgrade versionUpgrade, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateVersionUpgrade(versionUpgrade, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ClientCurrencyMapping> getClientCurrencyMapping(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getClientCurrencyMapping(clientAppDbJdbcTemplate);
	}

	@Override
	public List<ClientCurrencyMapping> getClientCurrencyMappingWithCurrencyIds(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getClientCurrencyMappingWithCurrencyIds(clientAppDbJdbcTemplate);
	}

	@Override
	public int createClientCurrencyMapping(ClientCurrencyMapping clientCurrencyMapping, JdbcTemplate clientJdbcTemplate,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createClientCurrencyMapping(clientCurrencyMapping, clientJdbcTemplate,
				clientAppDbJdbcTemplate);
	}

	@Override
	public ClientCurrencyMapping getclientCurrencyMappingDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getclientCurrencyMappingDetailsById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public int createCurrencyMapping(ClientCurrencyMapping clientCurrencyMapping,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createCurrencyMapping(clientCurrencyMapping, clientAppDbJdbcTemplate);
	}

	@Override
	public ClientCurrencyMapping getDefaultCurrencyInfoById(int templateId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getDefaultCurrencyInfoById(templateId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateCurrencyMapping(ClientCurrencyMapping clientCurrencyMapping,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateCurrencyMapping(clientCurrencyMapping, clientAppDbJdbcTemplate);
	}

	@Override
	public List<CurrencyList> getCurrencyList(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getCurrencyList(clientAppDbJdbcTemplate);
	}

	@Override
	public int updateAllVersionsToOld(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateAllVersionsToOld(clientAppDbJdbcTemplate);
	}

	@Override
	public ClientCurrencyMapping getclientCurrencyMappingDetailsByClientId(int id,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getclientCurrencyMappingDetailsByClientId(id, clientAppDbJdbcTemplate);
	}

	@Override
	public int savePropertiesKeyValuePairs(Internalization internalization, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.savePropertiesKeyValuePairs(internalization, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Internalization> getPropertiesList(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getPropertiesList(clientAppDbJdbcTemplate);
	}

	@Override
	public Internalization getpropertiesKeyValuePairsById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getpropertiesKeyValuePairsById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public List<S3BucketInfo> getS3InfoList(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getS3InfoList(clientAppDbJdbcTemplate);
	}

	@Override
	public int saveS3BucketInfo(S3BucketInfo s3BucketInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveS3BucketInfo(s3BucketInfo, clientAppDbJdbcTemplate);
	}

	@Override
	public S3BucketInfo getS3BucketInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getS3BucketInfoById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public Map<Integer, String> getBucketList(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getBucketList(clientAppDbJdbcTemplate);
	}
	
	@Override
	public List<SchedulerMaster> getScheduledMasterInfoList(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getScheduledMasterInfoList(clientAppDbJdbcTemplate);
	}
	
	@Override
	public List<FileSettings> getFileSettingsList(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getFileSettingsList(clientAppDbJdbcTemplate);
	}

	@Override
	public int saveClientMapping(S3BucketInfo s3BucketInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveClientMapping(s3BucketInfo, clientAppDbJdbcTemplate);
	}

	@Override
	public List<MiddleLevelManager> getMiddleLevelManager(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getMiddleLevelManager(clientAppDbJdbcTemplate);
	}

	@Override
	public int updateMiddleLevelManager(MiddleLevelManager middleLevelManager, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateMiddleLevelManager(middleLevelManager, clientAppDbJdbcTemplate);
	}

	@Override
	public int createMiddleLevelManager(MiddleLevelManager middleLevelManager, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createMiddleLevelManager(middleLevelManager, clientAppDbJdbcTemplate);
	}

	@Override
	public MiddleLevelManager getMiddleLevelManagerDetailsById(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getMiddleLevelManagerDetailsById(clientAppDbJdbcTemplate);
	}

	@Override
	public List<AppDBVersionTableScripts> getAppDbVersionTableScripts(JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return etlAdminDao.getAppDbVersionTableScripts(clientAppDbJdbcTemplate);
	}

	@Override
	public int createAppDBVersionTableScripts(AppDBVersionTableScripts appDBVersionTableScripts,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createAppDBVersionTableScripts(appDBVersionTableScripts, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateAppDBVersionTableScriptsInfo(AppDBVersionTableScripts appDBVersionTableScripts,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateAppDBVersionTableScriptsInfo(appDBVersionTableScripts, clientAppDbJdbcTemplate);
	}

	@Override
	public AppDBVersionTableScripts getAppDbVersionTableScriptDetailsById(int id,
			JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return etlAdminDao.getAppDbVersionTableScriptDetailsById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public String getAppDBScriptById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return etlAdminDao.getAppDBScriptById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public String getMinidwDBScriptById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getMinidwDBScriptById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateDefaultStatus(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateDefaultStatus(clientAppDbJdbcTemplate);
	}

	@Override
	public List<HybridClientsGrouping> getHybridClientsGrouping(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getHybridClientsGrouping(clientAppDbJdbcTemplate);
	}

	@Override
	public int createHybridClientsGroupingInfo(HybridClientsGrouping hybridClientsGrouping,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createHybridClientsGroupingInfo(hybridClientsGrouping, clientAppDbJdbcTemplate);
	}

	@Override
	public HybridClientsGrouping getHybridClientGroupingDetailsById(long id, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getHybridClientGroupingDetailsById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateHybridClientsGroupingInfo(HybridClientsGrouping hybridClientsGrouping,
			JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return etlAdminDao.updateHybridClientsGroupingInfo(hybridClientsGrouping, clientAppDbJdbcTemplate);
	}

	@Override
	public int clientsInstantScriptExecution(TableScriptsForm tableScriptsForm, JdbcTemplate jdbcTemplate) {
		return etlAdminDao.saveClientsInstantScriptExecution(tableScriptsForm, jdbcTemplate);
	}

	@Override
	public int instantscriptExecutionOfClient(TableScriptsForm tableScriptsForm, JdbcTemplate jdbcTemplate) {
		return etlAdminDao.saveInstantScriptExecutionOfClient(tableScriptsForm, jdbcTemplate);
	}

	@Override
	public List<TableScriptsForm> getClientsInstantScriptExecution(JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getClientsInstantScriptExecution(clientAppDbJdbcTemplate);
	}

	@Override
	public List<TableScriptsForm> getInstantScriptExecutionOfClient(int clientInstantId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getInstantScriptExecutionOfClient(clientInstantId, clientAppDbJdbcTemplate);
	}

	@Override
	public String getSqlScriptByExecutionId(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getSqlScriptByExecutionId(scriptId, clientAppDbJdbcTemplate);
	}

	@Override
	public ClientJobExecutionParameters getclientJobExecutionParametersDetailsById(
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.getclientJobExecutionParametersDetailsById(clientAppDbJdbcTemplate);
	}

	@Override
	public int createClientJobExecutionParams(ClientJobExecutionParameters clientJobExecutionParameters,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.createClientJobExecutionParams(clientJobExecutionParameters, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateClientJobExecutionParams(ClientJobExecutionParameters clientJobExecutionParameters,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateClientJobExecutionParams(clientJobExecutionParameters, clientAppDbJdbcTemplate);
	}

	@Override
	public String executeInsertScripts(MultiClientInsertScriptsExecution multiClientInsertScriptsExecution,
			Connection connection,JdbcTemplate commonJdbcTemplate,String clientId) {
		List<String> insertStatements = null;
		String fileName=null;
		List<String> clientIdsList = null;
		String erroMsg = null;
		try {
			DBToSQLExecution dbDumpData = new DBToSQLExecution();
			
			insertStatements = dbDumpData.getInsertStatements(connection, multiClientInsertScriptsExecution.getTableNames(), multiClientInsertScriptsExecution.getTruncateTbl());
			insertStatements.add(0, "set foreign_key_checks=0");
			insertStatements.add("set foreign_key_checks=1");
			
			TableScriptsForm tableScripts = new TableScriptsForm();
			tableScripts.setClientIdList(multiClientInsertScriptsExecution.getClientIds());
			tableScripts.setSqlScript(insertStatements.toString());
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			tableScripts.setModification(modification);
			tableScripts.setExecutionType("Client Script Execution");
			clientIdsList = multiClientInsertScriptsExecution.getClientIds();
			
			if (clientIdsList != null && insertStatements != null) { 
				fileName = CommonUtils.writeScriptFile(insertStatements);
				int instantScriptId = etlAdminDao.saveClientsInstantScriptExecution(tableScripts,commonJdbcTemplate);
				if (instantScriptId > 0) {
					for (String client : clientIdsList) {
						Connection destinationConnection = null;
						
						try {
							ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(client));
							destinationConnection = clientJdbcInstance.getClientAppdbJdbcTemplate().getDataSource().getConnection();
							if (destinationConnection != null) {
								ScriptRunner runner = new ScriptRunner(destinationConnection, false, false);
								erroMsg = runner.runScript(new BufferedReader(new FileReader(fileName)));
							}
							if (StringUtils.isEmpty(erroMsg)) {
								erroMsg = "Executed Succesfully";
							}
						} catch (IOException | SQLException e) {
							erroMsg = MinidwServiceUtil.getErrorMessageString(e);
						} catch (Exception e) {
							erroMsg = MinidwServiceUtil.getErrorMessageString(e);
						} finally {
							if (destinationConnection != null) {
								destinationConnection.close();
							}
						}
						tableScripts.setClientInstantExecutionId(instantScriptId);
						tableScripts.setExecution_status_msg(erroMsg);
						tableScripts.setExecution_status(false);
						tableScripts.setClientId(Integer.parseInt(client));
						tableScripts.setClientInstantExecutionId(instantScriptId);
						etlAdminDao.saveInstantScriptExecutionOfClient(tableScripts, commonJdbcTemplate);
					}	
				}
			}
			
		} catch (Exception e) {
			erroMsg = MinidwServiceUtil.getErrorMessageString(e);
		}finally {
			try {
				if(fileName != null){
					Files.delete(Paths.get(fileName));
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		return erroMsg;
	}

	@Override
	public List<String> getDDlTableCreateScripts(String query, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getDDlTableCreateScripts(query,clientAppDbJdbcTemplate);
	}

	@Override
	public void createDDlTables(String[] createQuerys, JdbcTemplate clientAppDbJdbcTemplate)
	{
		clientAppDbJdbcTemplate.batchUpdate(createQuerys);
	}

	@Override
	public int saveSchedularMastereMapping(int clientId, List<Integer> schedularMappingIds, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.saveSchedularMasterClientMapping(clientId, schedularMappingIds, modification, clientAppDbJdbcTemplate) ;
	}

	@Override
	public int updateFilesetting( Integer id,JdbcTemplate clientAppDbJdbcTemplate) {
		return etlAdminDao.updateFilesetting(id,clientAppDbJdbcTemplate);
	}


	@Override
	public List<Map<String, Object>> getWishList(JdbcTemplate clientAppDbJdbcTemplate) {
		  return etlAdminDao.getWishList(clientAppDbJdbcTemplate);
	}

	@Override
	public Map<String, Integer> transeferDataERPtoClient(String clientId, String referenceClientName,Integer erpId, String referenceUserId, List<String> erptableList,
			JdbcTemplate clientAppDbJdbcTemplate) {
			
		return etlAdminDao.transeferDataERPtoClient(clientId,referenceClientName, erpId, referenceUserId, erptableList, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ERP> getERPList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getERPList(clientAppDbJdbcTemplate);
	}

	@Override
	public List<DLInfo> getDlInfoList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getDlInfoList(clientAppDbJdbcTemplate);
	}

	@Override
	public List<IlPrebuildQueriesMapping> getIlPrebuildQueriesMapping(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getIlPrebuildQueriesMapping(clientAppDbJdbcTemplate);
	}

	@Override
	public List<IlDlMapping> getIlDlMapping(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getIlDlMapping(clientAppDbJdbcTemplate);
	}

	@Override
	public List<DlKpiMapping> getDlKpiMapping(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getDlKpiMapping(clientAppDbJdbcTemplate);
	}

	@Override
	public List<WsTemplateAuthRequestParams> getWsTemplateAuthRequestParams(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getWsTemplateAuthRequestParams(clientAppDbJdbcTemplate);
	}

	@Override
	public List<WebServiceApi> getWsMappingList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getWsMappingList(clientAppDbJdbcTemplate);
	}

	@Override
	public List<String> getErpTableList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getErpTableList(clientAppDbJdbcTemplate);
	}

	@Override
	public List<String> getCmAndCstableList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getCmAndCstableList(clientAppDbJdbcTemplate);
	}

	@Override
	public List<User> getUserListFromClient(JdbcTemplate clientAppDbJdbcTemplate, int clientId)
	{
		return etlAdminDao.getUserListFromClient(clientAppDbJdbcTemplate,clientId);
	}

	@Override
	public int updateErpToClientMigrationMapping(JdbcTemplate clientAppDbJdbcTemplate, int clientId, int erpId,int userId,ETLAdmin eTLAdmin)
	{
		return etlAdminDao.updateErpToClientMigrationMapping(clientAppDbJdbcTemplate,clientId,erpId,userId, eTLAdmin);
	}

	@Override
	public int wishListToWishListAccessOnlyMapping(String[] clientInfoArray, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.wishListToWishListAccessOnlyMapping(clientInfoArray,clientAppDbJdbcTemplate);
	}

	@Override
	public List<String> getMasterTablesList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.getMasterTablesList(clientAppDbJdbcTemplate);
	}

	@Override
	public Map<String, Integer> transeferDataERPtoClientForMasterTables(List<String> tablesList, int erpId,String clientId,JdbcTemplate clientAppDbJdbcTemplate)
	{
		return etlAdminDao.transeferDataERPtoClientForMasterTables(tablesList,erpId,  clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public int getErpIdFromNameAndVersion(JdbcTemplate clientAppDbJdbcTemplate, String name, String version)
	{
		return etlAdminDao.getErpIdFromNameAndVersion(clientAppDbJdbcTemplate,name, version);
	}

	@Override
	public int encryptWebServiceAuthParams(JdbcTemplate clientAppDbJdbcTemplate, String query)
	{
		return etlAdminDao.encryptWebServiceAuthParams(clientAppDbJdbcTemplate,query);
	}

}
