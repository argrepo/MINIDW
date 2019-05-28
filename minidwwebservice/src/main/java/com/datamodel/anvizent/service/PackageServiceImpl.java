package com.datamodel.anvizent.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.StandardPackageDao;
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

public class PackageServiceImpl implements PackageService {

	protected static final Log log = LogFactory.getLog(PackageServiceImpl.class);
	//@Autowired
	private PackageDao packageDao;
	@Autowired
	private StandardPackageDao standardPackageDao;

	@Override
	public PackageDao getPackageDao() {
		return packageDao;
	}
	public PackageServiceImpl(PackageDao packageDao) {
		this.packageDao = packageDao;
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int createPackage(ClientData clientData , JdbcTemplate clientJdbctemplate) {
		return packageDao.createPackage(clientData , clientJdbctemplate);
	}

	@Override
	public List<Package> getUserPackages(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getUserPackages(clientId , clientJdbctemplate);
	}

	@Override
	public Package getPackageById(int packageId, String clientId, JdbcTemplate clientJdbctemplate) {
		if (packageId == 0) {
			return standardPackageDao.fetchStandardPackageInfo(clientId, clientJdbctemplate);
		} else {
			return packageDao.getPackageById(packageId, clientId , clientJdbctemplate);
		}
	}

	@Override
	public Package getPackageByIdWithOutStatusFlag(int packageId, String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getPackageByIdWithOutStatusFlag(packageId, clientId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void deletePackage(int packageId, String clientId, JdbcTemplate clientJdbctemplate) {
		packageDao.deletePackage(packageId, clientId , clientJdbctemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int createILConnection(ILConnection iLConnection, JdbcTemplate clientJdbctemplate) {

		// encrypt client connection details
		try {
			iLConnection.setConnectionType(EncryptionServiceImpl.getInstance().encrypt(iLConnection.getConnectionType()));
			iLConnection.setServer(EncryptionServiceImpl.getInstance().encrypt(iLConnection.getServer()));

			if (StringUtils.isNotEmpty(iLConnection.getUsername()))
				iLConnection.setUsername(EncryptionServiceImpl.getInstance().encrypt(iLConnection.getUsername()));

			if (StringUtils.isNotEmpty(iLConnection.getPassword()))
				iLConnection.setPassword(EncryptionServiceImpl.getInstance().encrypt(iLConnection.getPassword()));

		} catch (Exception e) {
			log.error("error while encrypting iLConnection");
			e.printStackTrace();
		}
		return packageDao.createILConnection(iLConnection , clientJdbctemplate);

	}

	@Override
	public List<Database> getDatabaseTypes(JdbcTemplate clientJdbctemplate) {
		return packageDao.getDatabaseTypes(clientJdbctemplate);
	}

	@Override
	public List<Database> getClientDatabaseTypes(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getClientDatabaseTypes(clientId , clientJdbctemplate);
	}

	@Override
	public List<ILConnection> getILConnections(String clientId, String userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILConnections(clientId, userId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveILConnectionMapping(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbctemplate) {
		return packageDao.saveILConnectionMapping(iLConnectionMapping , clientJdbctemplate);

	}

	@Override
	public ILConnection getILConnectionById(int connectionId, String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILConnectionById(connectionId, clientId , clientJdbctemplate);
	}

	@Override
	public List<Column> getTableStructure(String schemaName, String tableName, int industryId, String clientId, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getTableStructure(schemaName, tableName, industryId, clientId, clientJdbcTemplate);
	}

	@Override
	public List<ILConnectionMapping> getPendingILs(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getPendingILs(clientId , clientJdbctemplate);
	}

	@Override
	public List<ILConnectionMapping> getILConnectionMappingInfo(Integer iLId, Integer dLId, Integer packageId, String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILConnectionMappingInfo(iLId, dLId, packageId, clientId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void updatePackageScheduleStatus(ClientData clientData, JdbcTemplate clientJdbctemplate) {
		packageDao.updatePackageScheduleStatus(clientData , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void updatePackageMappingStatus(ClientData clientData, JdbcTemplate clientJdbctemplate) {
		packageDao.updatePackageMappingStatus(clientData , clientJdbctemplate);
	}

	@Override
	public List<ClientData> getILConnectionMappingInfoByPackage(String userId, String clientId, int packageId, JdbcTemplate clientJdbctemplate) {
		return getILConnectionMappingInfoByPackage(userId, clientId, packageId, clientJdbctemplate,true);
	} 
	
	@Override
	public List<ClientData> getILConnectionMappingInfoByPackage(String userId, String clientId, int packageId, JdbcTemplate clientJdbctemplate,boolean isActiveRequired) {
		Package userPackage = getPackageById(packageId, clientId , clientJdbctemplate);
		boolean isStandard = false;
		if (userPackage == null) {
			if (packageId == 0 ) {
				isStandard = true;
			} else {
				isStandard = false;
			}
		} else {
			isStandard = userPackage.getIsStandard();
		}
		return packageDao.getILConnectionMappingInfoByPackage(userId, clientId, packageId, isStandard , clientJdbctemplate,isActiveRequired);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveTargetTableInfo(ClientData clientData, JdbcTemplate clientJdbctemplate) {
		return packageDao.saveTargetTableInfo(clientData , clientJdbctemplate);
	}

	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	@Override
	public String createTargetTable(ClientData clientData, JdbcTemplate clientJdbcTemplate) throws Exception {
		return packageDao.createTargetTable(clientData, clientJdbcTemplate);
	}

	@Override
	public ClientData getTargetTableInfoByPackage(String clientId, int packageId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getTargetTableInfoByPackage(clientId, packageId , clientJdbctemplate);
	}

	@Override
	public List<Table> getAllTargetTablesOfCustomPackage(String clientId, int packageId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getAllTargetTablesOfCustomPackage(clientId, packageId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public boolean deleteConnectionMapping(String clientId, String packageId, String mappingId, JdbcTemplate clientJdbctemplate) {
		return packageDao.deleteConnectionMapping(clientId, packageId, mappingId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateDLMappingStatus(String clientId, int packageId, int dLid, int iLId, boolean dLMappingStatus, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateDLMappingStatus(clientId, packageId, dLid, iLId, dLMappingStatus , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateILJobStatusForRunNow(String clientId, int packageId, int dLid, int iLId, String iLJobStatusForRunNow, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateILJobStatusForRunNow(clientId, packageId, dLid, iLId, iLJobStatusForRunNow , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateTargetTableInfo(ClientData clientData, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateTargetTableInfo(clientData , clientJdbctemplate);
	}

	@Override
	public DLInfo getDLById(int dLId, String clinetId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDLById(dLId, clinetId , clientJdbctemplate);
	}

	@Override
	public DLInfo getDLByIdWithJobName(int dLId, String clinetId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDLByIdWithJobName(dLId, clinetId , clientJdbctemplate);
	}

	@Override
	public ILInfo getILById(int iLId, String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILById(iLId, clientId , clientJdbctemplate);
	}

	@Override
	public ILInfo getILByIdWithJobName(int iLId, String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILByIdWithJobName(iLId, clientId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void deleteIlSource(int connectionMappingId, String clientId, JdbcTemplate clientJdbctemplate) {
		packageDao.deleteIlSource(connectionMappingId, clientId , clientJdbctemplate);

	}

	@Override
	public Map<String, Object> showCustomPackageTablesStatus(String clientId, String packageId, String industryId, JdbcTemplate clientJdbctemplate) {
		return packageDao.showCustomPackageTablesStatus(clientId, packageId, industryId , clientJdbctemplate);
	}

	@Override
	public Map<String, String> getIlContextParams(Integer ilId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getIlContextParams(ilId , clientJdbctemplate);
	}

	@Override
	public Map<String, String> getDlContextParams(int dlId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDlContextParams(dlId , clientJdbctemplate);
	}

	@Override
	public void parseContextParams(final Map<String, String> contextParams, final Map<String, String> paramsVals) {

		Set<Map.Entry<String, String>> set = contextParams.entrySet();

		for (Map.Entry<String, String> entry : set) {

			String paramval = entry.getValue();

			if (paramval.indexOf("{") != -1) {

				int endindex = paramval.indexOf("}");

				String key = paramval.substring(1, endindex);
				String value = paramsVals.get(key);

				if (value != null) {
					entry.setValue(paramval.replace("{" + key + "}", value));
				} else
					System.out.println(key + " --> " + paramval + " --> " + value);
			}
		}

	}

	@Override
	public List<Object> getTablePreview(ILConnectionMapping ilConnectionMapping) throws Exception {
		return packageDao.getTablePreview(ilConnectionMapping);
	}

	@Override
	public boolean isTargetTableExist(String clientId, String tablename, JdbcTemplate clientJdbctemplate) {
		return packageDao.isTargetTableExist(clientId, tablename , clientJdbctemplate);
	}

	@Override
	public String getIlEpicorQuery(int databasetypeid, int iLid, JdbcTemplate clientJdbctemplate) {
		return packageDao.getIlEpicorQuery(databasetypeid, iLid , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void updateFilePathForDatabaseConnection(long sourceFileId,int ilconnectionMappingId, String filePath, String storageType,Integer bucketId,Boolean multiPartEnabled, JdbcTemplate clientJdbctemplate) {
		packageDao.updateFilePathForDatabaseConnection(sourceFileId,ilconnectionMappingId, filePath, storageType,bucketId,multiPartEnabled , clientJdbctemplate);

	}

	@Override
	public void dropTable(String schemaName, String tableName, JdbcTemplate clientJdbcTemplate) {
		packageDao.dropTable(schemaName, tableName, clientJdbcTemplate);
	}

	@Override
	public boolean isILMapped(String userId, int packageId, int ilId,int dlId, String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.isILMapped(userId, packageId, ilId, dlId, clientId , clientJdbctemplate);
	}

	@Override
	public ILConnectionMapping getILConnectionMappingInfoForPreview(int mappingId, int packageId, String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILConnectionMappingInfoForPreview(mappingId, packageId, clientId , clientJdbctemplate);
	}

	@Override
	public void disablePackage(List<Integer> packageIds, JdbcTemplate clientJdbctemplate) {
		packageDao.disablePackage(packageIds , clientJdbctemplate);

	}

	@Override
	public boolean deleteILConnection(int connectionId, String clientId, JdbcTemplate clientJdbctemplate) {

		return packageDao.deleteILConnection(connectionId, clientId , clientJdbctemplate);
	}

	@Override
	public void deleteILConnectionMapping(int connectionId, JdbcTemplate clientJdbctemplate) {

		packageDao.deleteILConnectionMapping(connectionId , clientJdbctemplate);
	}

	@Override
	public List<JobResult> getJobResults(String packageId, String userId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getJobResults(packageId, userId, clientStagingSchemaName, clientJdbcTemplate);
	}

	@Override
	public List<JobResult> getJobResultsByDate(String packageId, String userId, String clientStagingSchemaName, String fromDate, String toDate,
			JdbcTemplate clientJdbcTemplate) {
		return packageDao.getJobResultsByDate(packageId, userId, clientStagingSchemaName, fromDate, toDate, clientJdbcTemplate);
	}

	@Override
	public List<JobResult> getJobErrorLogs(String batchId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getJobErrorLogs(batchId, clientStagingSchemaName, clientJdbcTemplate);
	}

	@Override
	public boolean deleteCustomTablesBYPackageId(Package userPackage, String userId, JdbcTemplate clientJdbcTemplate,JdbcTemplate clientAppJdbcTemplate) {
		return packageDao.deleteCustomTablesBYPackageId(userPackage, userId, clientJdbcTemplate,clientAppJdbcTemplate);
	}

	@Override
	public boolean deleteCustomTablesBYTableId(TableDerivative tableDerivative, int packageId, JdbcTemplate clientJdbcTemplate,JdbcTemplate clientAppJdbcTemplate) {
		return packageDao.deleteCustomTablesBYTableId(tableDerivative, packageId, clientJdbcTemplate, clientAppJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateIlSource(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateIlSource(iLConnectionMapping , clientJdbctemplate);

	}

	@Override
	public List<DLInfo> getClientDLs(String userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getClientDLs(userId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void saveTargetTableAliasNames(ClientData clientData, JdbcTemplate clientJdbctemplate) {
		packageDao.saveTargetTableAliasNames(clientData , clientJdbctemplate);

	}

	@Override
	public List<Map<String, Object>> targetTableAliasColumns(int tableId, JdbcTemplate clientJdbctemplate) {

		return packageDao.targetTableAliasColumns(tableId , clientJdbctemplate);
	}

	@Override
	public List<ILConnectionMapping> getIlConnectionMappingInfoByPackageId(String userId, Integer packageId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getIlConnectionMappingInfoByPackageId(userId, packageId , clientJdbctemplate);
	}

	@Override
	public List<String> getTargetTables(String clientSchemaName, JdbcTemplate clientJdbcTemplate) {

		return packageDao.getTargetTables(clientSchemaName, clientJdbcTemplate);

	}

	@Override
	public List<String> derivedTableMappingInfo(int packageId, String userId, JdbcTemplate clientJdbctemplate) {

		return packageDao.derivedTableMappingInfo(packageId, userId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updatePackageIsClientDbTablesStatus(String clientId, int packageId, boolean isClientDbTables, JdbcTemplate clientJdbctemplate) {
		return packageDao.updatePackageIsClientDbTablesStatus(clientId, packageId, isClientDbTables , clientJdbctemplate);
	}

	@Override
	public ClientData getClientSourceDetails(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getClientSourceDetails(clientId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void saveDerivedTableMappingInfo(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbctemplate) {
		packageDao.saveDerivedTableMappingInfo(iLConnectionMapping , clientJdbctemplate);

	}

	@Override
	public List<Package> getAllUserPackages(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getAllUserPackages(clientId , clientJdbctemplate);
	}

	@Override
	public List<Package> getUserPackages(String clientId, boolean isStandardPackages, JdbcTemplate clientJdbctemplate) {
		return packageDao.getUserPackages(clientId, isStandardPackages , clientJdbctemplate);
	}

	@Override
	public long getMaxFileSize(boolean isTrailUser, JdbcTemplate clientJdbctemplate) {
		return packageDao.getMaxFileSize(isTrailUser , clientJdbctemplate);
	}

	@Override
	public int getConnectorId(int database_type_id, JdbcTemplate clientJdbctemplate) {

		return packageDao.getConnectorId(database_type_id , clientJdbctemplate);
	}

	@Override
	public String getTargetTableQuery(String clientId, int packageId, JdbcTemplate clientJdbctemplate) {

		
		String targetTableQuery = packageDao.getTargetTableQuery(clientId, packageId , clientJdbctemplate);
		if (StringUtils.isNotBlank(targetTableQuery)) {
			final String STARTER_MARKER = "/*index_started*/";
			final String ENDING_MARKER = "/*index_ended*/";
			
			int startingIndex = targetTableQuery.indexOf(STARTER_MARKER);
			if (startingIndex != -1) {
				int endingIndex = targetTableQuery.lastIndexOf(ENDING_MARKER);
				if (endingIndex != -1) {
					targetTableQuery = targetTableQuery.substring(startingIndex + STARTER_MARKER.length(), endingIndex);
				}
			}
			
		}
		return targetTableQuery;

	}
	
	@Override
	public boolean updateTargetTableQuery(String targetTableQuery, String clientId, int packageId,
			JdbcTemplate clientJdbcTemplate) {
		return packageDao.updateTargetTableQuery(targetTableQuery, clientId, packageId, clientJdbcTemplate);
	}

	@Override
	public String getILDefaultIncrementalQuery(int ilId, int databaseTypeId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILDefaultIncrementalQuery(ilId, databaseTypeId , clientJdbctemplate);
	}

	@Override
	public String getILIncrementalDate(String dataSourceId, String ILTableName, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getILIncrementalDate(dataSourceId, ILTableName, clientJdbcTemplate);
	}

	@Override
	public ILConnectionMapping getDatesForHistoricalLoad(int connectionMappingId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDatesForHistoricalLoad(connectionMappingId , clientJdbctemplate);
	}

	@Override
	public HistoricalLoadForm getDatesForHistoricalLoading(int loadId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDatesForHistoricalLoading(loadId , clientJdbctemplate);
	}

	@Override
	public List<DLInfo> getAnalyticalDLs(int dl_id, JdbcTemplate clientJdbctemplate) {
		return packageDao.getAnalyticalDLs(dl_id , clientJdbctemplate);
	}

	@Override
	public boolean purgeUserTables(String clientSchema, List<String> purgeScript, JdbcTemplate clientJdbcTemplate) {
		return packageDao.purgeUserTables(clientSchema, purgeScript, clientJdbcTemplate);
	}

	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	@Override
	public int activateUserPackage(int packageId, int userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.activateUserPackage(packageId, userId , clientJdbctemplate);
	}
	
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	@Override
	public Message deleteUserPackage(int userId ,int packageId, JdbcTemplate clientJdbctemplate) {
		return packageDao.deleteUserPackage(userId, packageId, clientJdbctemplate);
	}

	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	@Override
	public Message renameUserPackage(int userId, int packageId, String packageName, JdbcTemplate clientJdbctemplate) {
		return packageDao.renameUserPackage(userId, packageId, packageName , clientJdbctemplate);
	}

	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	@Override
	public void updateDatabaseConnection(ILConnection iLConnection, JdbcTemplate clientJdbctemplate) {
		try {
			iLConnection.setConnectionType(EncryptionServiceImpl.getInstance().encrypt(iLConnection.getConnectionType()));
			iLConnection.setServer(EncryptionServiceImpl.getInstance().encrypt(iLConnection.getServer()));

			if (StringUtils.isNotEmpty(iLConnection.getUsername()))
				iLConnection.setUsername(EncryptionServiceImpl.getInstance().encrypt(iLConnection.getUsername()));

			if (StringUtils.isNotEmpty(iLConnection.getPassword()))
				iLConnection.setPassword(EncryptionServiceImpl.getInstance().encrypt(iLConnection.getPassword()));

		} catch (Exception e) {
			log.error("error while encrypting iLConnection");
			e.printStackTrace();
		}
		packageDao.updateDatabaseConnection(iLConnection , clientJdbctemplate);
	}

	public List<Table> getClientILsandDLs(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getClientILsandDLs(clientId , clientJdbctemplate);
	}

	public List<Package> getPackagesByILConnectionId(int ilconnectionId, int userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getPackagesByILConnectionId(ilconnectionId, userId , clientJdbctemplate);
	}

	@Override
	public Integer getSourceCountByPackageId(int packageId, int userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getSourceCountByPackageId(packageId, userId , clientJdbctemplate);
	}

	@Override
	public Map<Integer, String> getAllWebServices(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getAllWebServices(clientId , clientJdbctemplate);
	}

	@Override
	public Map<Integer, String> getWebServicesByClientId(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getWebServicesByClientId(clientId , clientJdbctemplate);
	}

	@Override
	public WebService getDefaultILWebServiceMapping(int webserviceId, int ilId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDefaultILWebServiceMapping(webserviceId, ilId , clientJdbctemplate);
	}

	@Override
	public Map<String, String> getDbSchemaVaribles(String clientId, int connectorId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDbSchemaVaribles(clientId, connectorId , clientJdbctemplate);
	}

	@Override
	public int logError(Throwable ex, HttpServletRequest request, JdbcTemplate clientJdbctemplate) {
		return packageDao.logError(CommonUtils.createErrorLog(ex, request) , clientJdbctemplate);
	}

	@Override
	public List<String> getDefaultInsertsForIL(List<Integer> ilIds, String clientSchemaName, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDefaultInsertsForIL(ilIds, clientSchemaName , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void saveILMappingHeadersForWebService(String userId, int packageId, int dLId, int iLId, String mappedHeaders, JdbcTemplate clientJdbctemplate) {
		packageDao.saveILMappingHeadersForWebService(userId, packageId, dLId, iLId, mappedHeaders , clientJdbctemplate);
	}

	@Override
	public String getILMappingHeadersForWebService(int packageId, int dLId, int iLId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILMappingHeadersForWebService(packageId, dLId, iLId , clientJdbctemplate);
	}

	@Override
	public String getWebServiceById(int id, JdbcTemplate clientJdbctemplate) {
		return packageDao.getWebServiceById(id , clientJdbctemplate);
	}

	@Override
	public List<ClientData> getDependentPackagesForScheduling(String clientId, List<String> derivedTablesList, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDependentPackagesForScheduling(clientId, derivedTablesList , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateMappedHeadersForWebservice(String userId, ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateMappedHeadersForWebservice(userId, iLConnectionMapping , clientJdbctemplate);
	}

	@Override
	public String getMappedHeadersForWebservice(String userId, int iLId, int mappingId, int webserviceId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getMappedHeadersForWebservice(userId, iLId, mappingId, webserviceId , clientJdbctemplate);
	}


	@Override
	public String getDerivedTableQuery(String clientId, int packageId, String targetTableName, int targetTableId, int tableId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDerivedTableQuery(clientId, packageId, targetTableName, targetTableId, tableId , clientJdbctemplate);
	}

	@Override
	public String getILHistoricalLoadQuery(int ilId, int databaseTypeId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILHistoricalLoadQuery(ilId, databaseTypeId , clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveDruidDataSourceMasterInfo(JdbcTemplate clientJdbcTemplate, String clientStagingSchema, String tableName) {
		return packageDao.saveDruidDataSourceMasterInfo(clientJdbcTemplate, clientStagingSchema, tableName);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveDruidDataSourceMappingInfo(JdbcTemplate clientJdbcTemaplate, String clientStagingSchema, int druidDatasourceId, String priority) {
		return packageDao.saveDruidDataSourceMappingInfo(clientJdbcTemaplate, clientStagingSchema, druidDatasourceId, priority);
	}

	@Override
	public int getClientDruidDataSourceId(JdbcTemplate clientJdbcTemplate, String clientStagingSchema, String tableName) {
		return packageDao.getClientDruidDataSourceId(clientJdbcTemplate, clientStagingSchema, tableName);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateDruidDataSourceMasterVersion(JdbcTemplate clientJdbcTemplate, String clientStagingSchema, long newVersion, String tableName) {
		return packageDao.updateDruidDataSourceMasterVersion(clientJdbcTemplate, clientStagingSchema, newVersion, tableName);
	}

	@Override
	public WebService getDefaultILWebServiceMappingForExactOnline(int webserviceId, int ilId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDefaultILWebServiceMappingForExactOnline(webserviceId, ilId, clientJdbctemplate);
	}

	@Override
	public Map<Integer, String> getAuthenticationTypes(JdbcTemplate clientJdbctemplate) {
		return packageDao.getAuthenticationTypes(clientJdbctemplate);
	}

	@Override
	public int updateAuthenticationCodeForExactOnline(String clientId, String authCode, String authType, Modification modification, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateAuthenticationCodeForExactOnline(clientId, authCode, authType, modification, clientJdbctemplate);
	}

	@Override
	public String getAuthCode(String clientId, String authType, JdbcTemplate clientJdbctemplate) {
		return packageDao.getAuthCode(clientId, authType, clientJdbctemplate);
	}

	@Override
	public int updateAuthenticationTokenForExactOnline(String clientId, String authCode, String authRefreshToken, String authType, Modification modification, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateAuthenticationTokenForExactOnline(clientId, authCode, authRefreshToken, authType, modification, clientJdbctemplate);
	}

	@Override
	public OAuth2 getAuthToken(String clientId, String authType, JdbcTemplate clientJdbctemplate) {
		return packageDao.getAuthToken(clientId, authType, clientJdbctemplate);
	}

	public int updateHistoricalUpdatedTime(String updatedDate, int ilConnectionMappingId, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateHistoricalUpdatedTime(updatedDate, ilConnectionMappingId, clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveHistoricalLoad(String clientId, HistoricalLoadForm historicalLoadForm, Modification modification, JdbcTemplate clientJdbctemplate) {
		return packageDao.saveHistoricalLoad(clientId, historicalLoadForm, modification, clientJdbctemplate);
	}

	@Override
	public List<HistoricalLoadForm> getHistoricalLoad(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getHistoricalLoad(clientId, clientJdbctemplate);
	}

	@Override
	public HistoricalLoadForm getHistoricalLoadDetailsById(String clientId, Integer loadId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getHistoricalLoadDetailsById(clientId, loadId, clientJdbctemplate);
	}

	@Override
	public HistoricalLoadForm getHistoricalLoadDetailsByIdWithConnectionDetails(String clientId, Integer loadId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getHistoricalLoadDetailsByIdWithConnectionDetails(clientId, loadId, clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateIlHistoryStatus(HistoricalLoadStatus historicalLoadStatus, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateIlHistoryStatus(historicalLoadStatus, clientJdbctemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateHistoricalLoad(String clientId, HistoricalLoadForm historicalLoadForm, Modification modification, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateHistoricalLoad(clientId, historicalLoadForm, modification, clientJdbctemplate);
	}

	@Override
	public List<HistoricalLoadStatus> getHistoricalLoadUploadStatus(String clientid, int historicalLoadId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getHistoricalLoadUploadStatus(clientid, historicalLoadId, clientJdbctemplate);
	}

	@Override
	public List<JobResult> getJobResultsForHistoricalLoad(int ilId, Long historicalLoadId, String clientId, String clientStagingSchemaName,
			JdbcTemplate clientJdbcTemplate) {
		return packageDao.getJobResultsForHistoricalLoad(ilId, historicalLoadId, clientId, clientStagingSchemaName, clientJdbcTemplate);
	}

	@Override
	public List<JobResult> getJobResultsForHistoricalLoadByDate(int ilId, Long historicalLoadId, String clientId, String clientStagingSchemaName,
			String fromDate, String toDate, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getJobResultsForHistoricalLoadByDate(ilId, historicalLoadId, clientId, clientStagingSchemaName, fromDate, toDate, clientJdbcTemplate);
	}

	@Override
	public List<JobResult> getJobResultsForDefaultCurrencyLoad(JdbcTemplate clientJdbctemplate) {
		return packageDao.getJobResultsForDefaultCurrencyLoad(clientJdbctemplate);
	}

	@Override
	public List<JobResult> getJobResultsForDefaultCurrencyLoad( String fromDate, String toDate, JdbcTemplate clientJdbctemplate) {
		return packageDao.getJobResultsForDefaultCurrencyLoad( fromDate, toDate, clientJdbctemplate);
	}

	@Override
	public int updateIlConnectionWebServiceMapping(int connectionMappingId, int ilId, String userId, int packageId, Modification modification, String wsIds, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateIlConnectionWebServiceMapping(connectionMappingId, ilId, userId, packageId, modification, wsIds, clientJdbctemplate);
	}

	@Override
	public String getMappedQueryForWebserviceJoin(String userId, int iLId, int mappingId, int webserviceId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getMappedQueryForWebserviceJoin(userId, iLId, mappingId, webserviceId, clientJdbctemplate);
	}

	@Override
	public List<Table> getTempTablesAndWebServiceJoinUrls(String userId, int packageId, int ilId, int ilConnectionMappingId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getTempTablesAndWebServiceJoinUrls(userId, packageId, ilId, ilConnectionMappingId, clientJdbctemplate);
	}

	@Override
	public WebService getWebServiceMasterById(int id, String userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getWebServiceMasterById(id, userId, clientJdbctemplate);
	}

	@Override
	public int updateWsApiIlConnectionWebServiceMapping(WebService webservice, int ilConnectionMappingId, Modification modification, String userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateWsApiIlConnectionWebServiceMapping(webservice, ilConnectionMappingId, modification, userId, clientJdbctemplate);
	}

	@Override
	public WebServiceApi getIlConnectionWebServiceMapping(String userId, int packageId, int ilId, int ilConnectionMappingId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getIlConnectionWebServiceMapping(userId, packageId, ilId, ilConnectionMappingId, clientJdbctemplate);
	}

	@Override
	public String getILIncrementalUpdateDate(String clientStagingSchema, int iLId, String dataSourceId, String typeOfSource,int packageSourceMappingId, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getILIncrementalUpdateDate(clientStagingSchema, iLId, dataSourceId, typeOfSource, packageSourceMappingId,clientJdbcTemplate);
	}

	@Override
	public List<String> getColumnHeadersByQuery(String tempQuery, JdbcTemplate clientJdbcTemplate,boolean isDDl) {
		return packageDao.getColumnHeadersByQuery(tempQuery, clientJdbcTemplate,isDDl);
	}

	@Override
	public List<User> getClientUserDeatils(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getClientUserDeatils(clientId, clientJdbctemplate);
	}

	@Override
	public List<String> getClientUserIds(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getClientUserIds(clientId, clientJdbctemplate);
	}

	@Override
	public List<Map<String, Object>> getTableData(String query, String parametrs, JdbcTemplate clientJdbctemplate) {
		return packageDao.getTableData(query,parametrs, clientJdbctemplate);
	}
	
	@Override
	public List<ClientDataSources> getDataSourceList(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDataSourceList(clientId, clientJdbctemplate);
	}
	

	@Override
	public List<DDLayout> getDDlayoutTablesList(String clientId,String userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDDlayoutTablesList(clientId,userId, clientJdbctemplate);
	}

	@Override
	public DDLayout getDDlayoutTable(String clientId, int id,String userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDDlayoutTable(clientId,id,userId, clientJdbctemplate);
	}

	@Override
	public int updateDDlayoutTable(String clientId,DDLayout dDLayout,String userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateDDlayoutTable(clientId,dDLayout,userId, clientJdbctemplate);
	}

	@Override
	public int saveDDlTableInfo(ClientData clientData,String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.saveDDlTableInfo(clientData,clientId, clientJdbctemplate);
	}

	@Override
	public void dropDDlayoutTable(String clientId, DDLayout dDLayout,JdbcTemplate jdbcTemplate) {
		packageDao.dropDDlayoutTable(clientId,dDLayout,jdbcTemplate);
	}

	@Override
	public int deleteDDlayoutTable(String userId, DDLayout dDLayout, JdbcTemplate clientJdbctemplate) {
		return packageDao.deleteDDlayoutTable(userId,dDLayout, clientJdbctemplate);
	}

	@Override
	public List<DDLayout> getDDlayoutList(String clientId, List<String> derivedtableList,String userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDDlayoutList(clientId,derivedtableList,userId, clientJdbctemplate);
	}
	
	@Override
	public int createDataSourceList(ClientDataSources clientDataSources, JdbcTemplate clientJdbctemplate) {
		return packageDao.createDataSourceList(clientDataSources, clientJdbctemplate);
	}

	@Override
	public int updateDataSourceDetails(Integer mappingId, String dataSouceName,String dataSouceNameOther,Integer packageId, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateDataSourceDetails(mappingId,dataSouceName,dataSouceNameOther,packageId, clientJdbctemplate);
	}

	@Override
	public List<ScheduleResult> getScheduleStartTime(int packageId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getScheduleStartTime(packageId, clientJdbctemplate);
	}
	
	public List<ILInfo> getClientIlsList(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getClientIlsList(clientId, clientJdbctemplate);
	}
	@Override
	public List<ScheduleResult> getScheduleDetails(int packageId ,String scheduleStartTime, JdbcTemplate clientJdbctemplate) {
		return packageDao.getScheduleDetails(packageId,scheduleStartTime, clientJdbctemplate);
	}

	@Override
	public int updateDDlayoutTableAuditLogs(String userId, DDLayout dDLayout, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateDDlayoutTableAuditLogs(userId,dDLayout, clientJdbctemplate);
	}

	@Override
	public List<DDLayout> getDDlayoutTableAuditLogs(String userId, DDLayout dDLayout, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDDlayoutTableAuditLogs(userId,dDLayout, clientJdbctemplate);
	}

	@Override
	public DDLayout viewDDlTableSelectQry(String userId, DDLayout dDLayout, JdbcTemplate clientJdbctemplate) {
		return packageDao.viewDDlTableSelectQry(userId,dDLayout, clientJdbctemplate);
	}
	@Override
	public int updatePackageExecutorSourceMappingInfo(SourceFileInfo sourceFileInfo, JdbcTemplate clientJdbctemplate) {
		return packageDao.updatePackageExecutorSourceMappingInfo(sourceFileInfo , clientJdbctemplate);
	}

	@Override
	public int updateSourceFileInfo(SourceFileInfo sourceFileInfo, JdbcTemplate clientJdbctemplate) {
		return packageDao.updateSourceFileInfo(sourceFileInfo, clientJdbctemplate);
	}

	@Override
	public List<PackageExecutorMappingInfo> getPackageExecutorSourceMappingInfoList(long executionId, JdbcTemplate clientJdbctemplate) {
		 return packageDao.getPackageExecutorSourceMappingInfoList(executionId, clientJdbctemplate);
	}

	@Override
	public ILConnectionMapping getIlConnectionMappingByPackageExecutorSourceMappingInfo(long executionId,
			long executionMappingId, JdbcTemplate clientJdbctemplate) {
		 return packageDao.getIlConnectionMappingByPackageExecutorSourceMappingInfo(executionId,executionMappingId, clientJdbctemplate);
	}
	@Override
	public Database getDbIdDriverNameAndProtocal(int databaseTypeId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getDbIdDriverNameAndProtocal(databaseTypeId, clientJdbctemplate);
	}

	@Override
	public List<PackageExecution> getPackageExecution(Integer packageId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getPackageExecution(packageId, clientJdbctemplate);
	}

	@Override
	public List<DLInfo> getILConnectionMappingDlInfoByPackageId(Integer packageId, String userId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getILConnectionMappingDlInfoByPackageId(packageId,userId, clientJdbctemplate);
	}

	@Override
	public FileSettings getFileSettingsInfo(String clientId, JdbcTemplate clientJdbctemplate) {
		return packageDao.getFileSettingsInfo(clientId, clientJdbctemplate);
	}

	@Override
	public String getUploadAndExecutionStatusComments(long executionId,String uploadOrExecution, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getUploadAndExecutionStatusComments(executionId,uploadOrExecution,clientJdbcTemplate);
	}

	@Override
	public List<PackageExecution> getInProgressPackageExecutionList(Integer packageId, JdbcTemplate clientJdbcTemplated) {
		return packageDao.getInProgressPackageExecutionList(packageId,clientJdbcTemplated);
	}

	@Override
	public String getUploadStatusAndExecutionStatusComments(long executionId,  
			JdbcTemplate clientJdbcTemplate) {
		return packageDao.getUploadStatusAndExecutionStatusComments(executionId,clientJdbcTemplate);
	}
	@Override
	public int updatePackageExecutionTargetTableInfo(PackageExecution packageExecution,
			JdbcTemplate clientJdbcTemplate) {
		 return packageDao.updatePackageExecutionTargetTableInfo(packageExecution,clientJdbcTemplate);
	}
	@Override
	public List<PackageExecution> getPackageExecutionSourceMappingInfo(long executionId,
			JdbcTemplate clientJdbcTemplate) {
		return packageDao.getPackageExecutionSourceMappingInfo(executionId,clientJdbcTemplate);
	}
	@Override
	public List<PackageExecution> getPackageExecutionTargetTableInfo(long executionId,
			JdbcTemplate clientJdbcTemplate) {
		return packageDao.getPackageExecutionTargetTableInfo(executionId,clientJdbcTemplate);
	}
	
	@Override
	public int updateDatesForOnceRecurrence(Schedule schedule, JdbcTemplate clientJdbcTemplate) {
		
		return packageDao.updateDatesForOnceRecurrence(schedule,clientJdbcTemplate);
	}
	@Override
	public Package getPackageWithoutUserId(int packageId, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getPackageWithoutUserId(packageId,clientJdbcTemplate);
	}
	@Override
	public PackageExecution getPackageExecutionInfo(Integer packageId, Integer executionId, String uploaderExecution, JdbcTemplate clientJdbcTemplate) {
		 return packageDao.getPackageExecutionInfo(packageId, executionId, uploaderExecution, clientJdbcTemplate);
	}
	@Override
	public int getIncrementalLoadCount(Integer packageId, Integer executionId, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getIncrementalLoadCount(packageId, executionId, clientJdbcTemplate);
	}
	@Override
	public int updateIsActiveStatusIlSource(String connectionMappingId, Boolean isActiveRequired,JdbcTemplate clientAppDbJdbcTemplate) {
		return packageDao.updateIsActiveStatusIlSource(connectionMappingId,isActiveRequired,clientAppDbJdbcTemplate);
	}
	@Override
	public int saveApisDataInfo(ApisDataInfo apisDataInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return packageDao.saveApisDataInfo(apisDataInfo,clientAppDbJdbcTemplate);
	}
	@Override
	public ApisDataInfo getApistDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		return packageDao.getApistDetailsById(id,clientAppDbJdbcTemplate);
	}
	@Override
	public ApisDataInfo getApistDetailsByEndPointName(String endPointName, JdbcTemplate clientAppDbJdbcTemplate) {
		return packageDao.getApistDetailsByEndPointName(endPointName,clientAppDbJdbcTemplate);
	}
	@Override
	public List<ApisDataInfo> getApisDetails(JdbcTemplate clientAppDbJdbcTemplate) {
		return packageDao.getApisDetails(clientAppDbJdbcTemplate);
	}
	@Override
	public List<JobResult> getJobResultsForCrossReference(int ilId, String clientId, String clientStagingSchemaName,
			JdbcTemplate clientJdbcTemplate) {
		return packageDao.getJobResultsForCrossReference(ilId, clientId, clientStagingSchemaName, clientJdbcTemplate);
	}
	@Override
	public List<JobResult> getJobResultsForCrossReferenceByDate(int ilId, String clientId,
			String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate) {
		return packageDao.getJobResultsForCrossReferenceByDate(ilId, clientId, clientStagingSchemaName, fromDate, toDate, clientJdbcTemplate);
	}
	@Override
	public boolean cloneHistoricalLoadDetailsById(Integer loadId, JdbcTemplate clientAppDbJdbcTemplate) {
		return packageDao.cloneHistoricalLoadDetailsById(loadId, clientAppDbJdbcTemplate);
	}
	@Override
	public int updatePackageName(Integer packageId, String packageName, JdbcTemplate clientAppDbJdbcTemplate) {
		return packageDao.updatePackageName(packageId,packageName,clientAppDbJdbcTemplate);
	}
	@Override
	public List<JobResult> getJobResultsForCrossReferenceByConditionId(int conditionId, int ilId, String clientId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate)
	{
		return packageDao.getJobResultsForCrossReferenceByConditionId(conditionId, ilId, clientId, clientStagingSchemaName, clientJdbcTemplate);
	}
	@Override
	public List<JobResult> getJobResultsForCrossReferenceByDateById(Integer conditionId, Integer ilId, String clientId, String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate)
	{
		return packageDao.getJobResultsForCrossReferenceByDateById(conditionId, ilId, clientId, fromDate, toDate, clientJdbcTemplate);
	}
	
	@Override
	public int updatePackageAdvancedField(Integer packageId, boolean status, JdbcTemplate clientAppDbJdbcTemplate) {
		return packageDao.updatePackageAdvancedField(packageId, status, clientAppDbJdbcTemplate);
	}
	@Override
	public void getUpdatedTargetMappedQuery(List<String> query, JdbcTemplate clientJdbcTemplate) {
		packageDao.getUpdatedTargetMappedQuery(query,clientJdbcTemplate);
	}
	@Override
	public void deleteCustomTempTableByMappingId(Integer packageId, String mappingId, String clientId, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate)
	{
		packageDao.deleteCustomTempTableByMappingId(packageId, mappingId, clientId, clientJdbcTemplate, clientAppDbJdbcTemplate);
	}
	@Override
	public List<ELTClusterLogsInfo> getEltInitiateInfo(int executionId, JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return packageDao.getEltInitiateInfo(executionId,clientAppDbJdbcTemplate);
	}
	@Override
	public void editTargetMappedQuery(String query, JdbcTemplate clientJdbcTemplate) {
		packageDao.editTargetMappedQuery(query,clientJdbcTemplate);
	}
	@Override
	public List<DataTypesInfo> getDataTypesList()
	{
		return packageDao.getDataTypesList();
	}
	@Override
	public int getPackageExecutionCount(int packageId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return packageDao.getPackageExecutionCount(packageId,clientAppDbJdbcTemplate);
	}
	@Override
	public List<PackageExecution> getPackageExecutionByPagination(Integer packageId, int offset, int limit, JdbcTemplate clientJdbctemplate)
	{
		return packageDao.getPackageExecutionByPagination(packageId,offset,limit,clientJdbctemplate);
	}
	@Override
	public int updateWsDataSourceDetails(Integer mappingId, String dataSouceName, String dataSouceNameOther, Integer packageId, String wsApiUrl, JdbcTemplate clientJdbctemplate)
	{
		return packageDao.updateWsDataSourceDetails(mappingId,dataSouceName,dataSouceNameOther,packageId,wsApiUrl,clientJdbctemplate);
	}
	@Override
	public String getWsSourceMappingIdsByMappingId(int mappindId, Integer packageId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return packageDao.getWsSourceMappingIdsByMappingId( mappindId, packageId, clientAppDbJdbcTemplate);
	}
	@Override
	public int updateWsJoinDataSourceDetails(Integer mappingId,int wsMappingId, String dataSouceName, String dataSouceNameOther, Integer packageId, String wsApiUrl, JdbcTemplate clientJdbctemplate)
	{
		return packageDao.updateWsJoinDataSourceDetails(mappingId,wsMappingId,dataSouceName,dataSouceNameOther,packageId,wsApiUrl,clientJdbctemplate);
	}
	@Override
	public int updateWsJoinUrlAtPackageSourceMapping(int mappindId, Integer packageId, String wsJoinUrl, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return packageDao.updateWsJoinUrlAtPackageSourceMapping(  mappindId,   packageId,   wsJoinUrl,   clientAppDbJdbcTemplate);
	}
	@Override
	public String getSslFileNamesByConId(int conId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return packageDao.getSslFileNamesByConId(conId,clientAppDbJdbcTemplate);
	}
}
