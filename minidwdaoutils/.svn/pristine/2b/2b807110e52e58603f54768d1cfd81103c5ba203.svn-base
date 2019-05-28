package com.datamodel.anvizent.service.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.dao.PackageDao;
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
import com.datamodel.anvizent.service.model.Industry;
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
import com.datamodel.anvizent.service.model.WebServiceJoin;

/**
 * 
 * @author rakesh.gajula
 *
 */

public class PackageDaoImpl extends JdbcDaoSupport implements PackageDao {

	protected static final Log LOG = LogFactory.getLog(PackageDaoImpl.class);

	private SqlHelper sqlHelper;
	@Autowired
	private PlatformTransactionManager transactionManager;

	public PackageDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
		try {
			this.sqlHelper = new SqlHelper(PackageDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating PackageDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public int createPackage(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		Integer packageId = -1;
		try {
			String sql = sqlHelper.getSql("createPackage");
			clientJdbcTemplate.update(sql, new Object[] { clientData.getUserPackage().getPackageName(),
					clientData.getUserPackage().getIndustry().getId(), clientData.getUserPackage().getIsStandard(),
					clientData.getUserPackage().getDescription(), clientData.getUserPackage().getIsScheduled(),
					clientData.getUserPackage().getIsMapped(), clientData.getUserPackage().getScheduleStatus(), 
					clientData.getUserPackage().getTrailingMonths(),
					clientData.getUserPackage().getIsAdvanced(),
					clientData.getUserId(), clientData.getModification().getCreatedBy(),
					clientData.getModification().getCreatedTime() });
			// get package id
			String sqlGetPackageIdByName = sqlHelper.getSql("getPackageIdByName");
			packageId = clientJdbcTemplate.query(sqlGetPackageIdByName,
					new Object[] { clientData.getUserPackage().getPackageName(), clientData.getUserId() },
					new ResultSetExtractor<Integer>() {
						@Override
						public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
							return rs.next() ? rs.getInt("package_id") : null;
						}
					});

		} catch (DuplicateKeyException ae) {
			LOG.error("error while createPackage()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOG.error("error while createPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while createPackage()", e);
			
		}
		return packageId;
	}

	@Override
	public List<Package> getUserPackages(String clientId, JdbcTemplate clientJdbcTemplate) {
		List<Package> packageList = null;
		try {
			String sql = sqlHelper.getSql("getUserPackages");
			packageList = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<Package>() {
				public Package mapRow(ResultSet rs, int i) throws SQLException {
					Package userPackage = new Package();
					userPackage.setPackageId(rs.getInt("package_id"));
					userPackage.setPackageName(rs.getString("package_name"));
					userPackage.setDescription(rs.getString("description"));
					userPackage.setIndustry(new Industry(rs.getInt("industryId"), rs.getString("industryName")));
					userPackage.setIsStandard(rs.getBoolean("isStandard"));
					userPackage.setIsActive(rs.getBoolean("isActive"));
					// userPackage.setIsScheduled(rs.getBoolean("isScheduled"));
					userPackage.setIsScheduled(false);
					userPackage.setIsMapped(rs.getBoolean("isMapped"));
					userPackage.setScheduleStatus(rs.getString("schedule_status"));
					userPackage.setIsClientDbTables(rs.getBoolean("isClientDbTables"));
					userPackage.setTrailingMonths(rs.getInt("trailing_months"));
					Modification modification = new Modification();
					modification.setCreatedTime(rs.getString("created_time"));
					userPackage.setModification(modification);
					userPackage.setIsAdvanced(rs.getBoolean("isAdvanced"));
					return userPackage;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getUserPackages()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getUserPackages()", e);
			
		}

		return packageList;
	}

	@Override
	public Package getPackageById(int packageId, String clientId, JdbcTemplate clientJdbcTemplate) {
		Package userPackage = null;
		try {

			String sql = sqlHelper.getSql("getPackageById");
			userPackage = clientJdbcTemplate.query(sql, new Object[] { packageId },
					new ResultSetExtractor<Package>() {
						@Override
						public Package extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								Package userPackage = new Package();
								userPackage.setPackageId(rs.getInt("package_id"));
								userPackage.setPackageName(rs.getString("package_name"));
								userPackage.setDescription(rs.getString("description"));
								userPackage.setIndustry(
										new Industry(rs.getInt("industryId"), rs.getString("industryName")));
								userPackage.setIsStandard(rs.getBoolean("isStandard"));
								userPackage.setIsActive(rs.getBoolean("isActive"));
								userPackage.setIsScheduled(rs.getBoolean("isScheduled"));
								userPackage.setIsMapped(rs.getBoolean("isMapped"));
								userPackage.setScheduleStatus(rs.getString("schedule_status"));
								userPackage.setIsClientDbTables(rs.getBoolean("isClientDbTables"));
								userPackage.setTrailingMonths(rs.getInt("trailing_months"));
								Modification mo = new Modification();
								mo.setModifiedBy(rs.getString("modified_by"));
								userPackage.setModification(mo);
								userPackage.setIsAdvanced(rs.getBoolean("isAdvanced"));
								Object s = rs.getObject("files_having_same_columns");
								if (s == null) {
									userPackage.setFilesHavingSameColumns(null);
								} else {
									userPackage.setFilesHavingSameColumns(rs.getBoolean("files_having_same_columns"));
								}

								return userPackage;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getPackageById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageById()", e);
			
		}
		return userPackage;
	}

	@Override
	public Package getPackageByIdWithOutStatusFlag(int packageId, String clientId, JdbcTemplate clientJdbcTemplate) {
		Package userPackage = null;
		;
		try {

			String sql = sqlHelper.getSql("getPackageByIdWithOutStatusFlag");
			userPackage = clientJdbcTemplate.query(sql, new Object[] { packageId, clientId },
					new ResultSetExtractor<Package>() {
						@Override
						public Package extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								Package userPackage = new Package();
								userPackage.setPackageId(rs.getInt("package_id"));
								userPackage.setPackageName(rs.getString("package_name"));
								userPackage.setDescription(rs.getString("description"));
								userPackage.setIndustry(
										new Industry(rs.getInt("industryId"), rs.getString("industryName")));
								userPackage.setIsStandard(rs.getBoolean("isStandard"));
								userPackage.setIsActive(rs.getBoolean("isActive"));
								userPackage.setIsScheduled(rs.getBoolean("isScheduled"));
								userPackage.setIsMapped(rs.getBoolean("isMapped"));
								userPackage.setScheduleStatus(rs.getString("schedule_status"));
								userPackage.setIsClientDbTables(rs.getBoolean("isClientDbTables"));
								userPackage.setTrailingMonths(rs.getInt("trailing_months"));
								Modification mo = new Modification();
								mo.setModifiedBy(rs.getString("modified_by"));
								userPackage.setModification(mo);
								userPackage.setIsAdvanced(rs.getBoolean("isAdvanced"));
								Object s = rs.getObject("files_having_same_columns");
								if (s == null) {
									userPackage.setFilesHavingSameColumns(null);
								} else {
									userPackage.setFilesHavingSameColumns(rs.getBoolean("files_having_same_columns"));
								}

								return userPackage;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getPackageByIdWithOutStatusFlag()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageByIdWithOutStatusFlag()", e);
			
		}
		return userPackage;
	}

	@Override
	public void deletePackage(int packageId, String clientId, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deletePackage");
			clientJdbcTemplate.update(sql, new Object[] { packageId, clientId });
			LOG.info("Package " + packageId + "deleted Succesfully for client : " + clientId + "");
		} catch (DataAccessException ae) {
			LOG.error("error while createPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while createPackage()", e);
			
		}

	}

	@Override
	public int createILConnection(ILConnection iLConnection, JdbcTemplate clientJdbcTemplate)
	{
		int conId = 0;
		try
		{
			final String sql = sqlHelper.getSql("createILConnection");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator()
			{
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
				{
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "connection_id" });
					ps.setObject(1, iLConnection.getConnectionName());
					ps.setObject(2, iLConnection.getConnectionType());
					ps.setObject(3, iLConnection.getClientId());
					ps.setObject(4, iLConnection.getDatabase().getId());
					ps.setObject(5, iLConnection.getServer());
					ps.setObject(6, iLConnection.getUsername());
					ps.setObject(7, iLConnection.getPassword());
					ps.setObject(8, iLConnection.getDateFormat());
					ps.setObject(9, iLConnection.getTimeZone());
					ps.setObject(10, iLConnection.isAvailableInCloud());
					ps.setObject(11, iLConnection.getModification().getCreatedBy());
					ps.setObject(12, iLConnection.getModification().getCreatedTime());
					ps.setObject(13, iLConnection.getDataSourceName());
					ps.setObject(14, iLConnection.isSslEnable()); 
					ps.setObject(15, iLConnection.getDbVariables());
					return ps;
				}
			}, keyHolder);
			if( keyHolder != null )
			{
				Number autoIncrement = keyHolder.getKey();
				conId = autoIncrement.intValue();
			}
		}
		catch ( DuplicateKeyException ae )
		{
			LOG.error("error while createILConnection()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while createILConnection()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while createILConnection()", e);
		}
		return conId;
	}

	@Override
	public List<Database> getDatabaseTypes(JdbcTemplate clientJdbcTemplate) {
		List<Database> databaseTypes = null;
		try {
			String sql = sqlHelper.getSql("getDatabaseTypes");
			databaseTypes = clientJdbcTemplate.query(sql, new Object[] {}, new RowMapper<Database>() {
				public Database mapRow(ResultSet rs, int i) throws SQLException {
					Database database = new Database();
					database.setId(rs.getInt("id"));
					database.setName(rs.getString("name"));
					database.setConnector_id(rs.getInt("connector_id"));
					return database;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getDatabaseTypes()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDatabaseTypes()", e);
			
		}

		return databaseTypes;
	}

	@Override
	public List<Database> getClientDatabaseTypes(String clientId, JdbcTemplate clientJdbcTemplate) {
		List<Database> databaseTypes = null;
		try {
			String sql = sqlHelper.getSql("getClientDatabaseTypes");
			databaseTypes = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<Database>() {
				public Database mapRow(ResultSet rs, int i) throws SQLException {
					Database database = new Database();
					database.setId(rs.getInt("id"));
					database.setName(rs.getString("name"));
					database.setConnector_id(rs.getInt("connector_id"));
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setConnectionStringParams(rs.getString("connection_string_params"));
					database.setUrlFormat(rs.getString("url_format"));
					return database;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getDatabaseTypes()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDatabaseTypes()", e);
			
		}

		return databaseTypes;
	}

	@Override
	public List<ILConnection> getILConnections(String clientId, String userId, JdbcTemplate clientJdbcTemplate) {
		List<ILConnection> connectionList = null;
		try {
			String sql = sqlHelper.getSql("getUserILConnections");
			connectionList = clientJdbcTemplate.query(sql, new Object[] { clientId, userId },
					new RowMapper<ILConnection>() {
						public ILConnection mapRow(ResultSet rs, int i) throws SQLException {
							ILConnection connection = new ILConnection();
							connection.setConnectionId(rs.getInt("connection_id"));
							connection.setConnectionName(rs.getString("connection_name"));
							connection.setAvailableInCloud(rs.getBoolean("available_in_cloud"));
							Database database = new Database();
							database.setUrlFormat(rs.getString("url_format"));
							database.setProtocal(rs.getString("protocal"));
							database.setConnectionStringParams(rs.getString("connection_string_params"));
							database.setName(rs.getString("name"));
							database.setConnector_id(rs.getInt("connector_id"));
							connection.setDatabase(database);
							return connection;
						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getILConnections()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILConnections()", e);
			
		}

		return connectionList;

	}

	@Override
	public int saveILConnectionMapping(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbcTemplate) {

		int connectionMappingId = 0;

		try {

			final String sql = sqlHelper.getSql("saveILConnectionMapping");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					Integer connectionId = null;
					if (iLConnectionMapping.getiLConnection() != null) {
						connectionId = iLConnectionMapping.getiLConnection().getConnectionId();
					}
					ps.setObject(1, iLConnectionMapping.getIsMapped());
					ps.setObject(2, iLConnectionMapping.getIsFlatFile());
					ps.setString(3, iLConnectionMapping.getFileType());
					ps.setLong(4, iLConnectionMapping.getSourceFileInfoId());
					ps.setString(5, iLConnectionMapping.getDelimeter());
					ps.setObject(6, iLConnectionMapping.getIsFirstRowHasColoumnNames());
					ps.setObject(7, connectionId);
					ps.setString(8, iLConnectionMapping.getTypeOfCommand());
					ps.setString(9, iLConnectionMapping.getDatabaseName());
					ps.setString(10, iLConnectionMapping.getiLquery());
					ps.setObject(11, iLConnectionMapping.getIsIncrementalUpdate());
					ps.setString(12, iLConnectionMapping.getProcedureParameters());
					ps.setObject(13, iLConnectionMapping.getiLId());
					ps.setObject(14, iLConnectionMapping.getdLId());
					ps.setObject(15, iLConnectionMapping.getTargetTableId());
					ps.setObject(16, iLConnectionMapping.getPackageId());
					ps.setObject(17, null);
					ps.setObject(18, iLConnectionMapping.getClientId());
					ps.setString(19, iLConnectionMapping.getModification().getCreatedBy());
					ps.setString(20, iLConnectionMapping.getModification().getCreatedTime());
					ps.setObject(21, iLConnectionMapping.getIsHavingParentTable());
					ps.setString(22, null);
					ps.setString(23, iLConnectionMapping.getIlSourceName());
					ps.setObject(24, iLConnectionMapping.getIsWebservice());
					ps.setObject(25, iLConnectionMapping.getWsConId());
					ps.setString(26, iLConnectionMapping.getWebserviceMappingHeaders());
					ps.setObject(27, iLConnectionMapping.getIsHistoricalLoad());
					ps.setString(28, iLConnectionMapping.getHistoricalFromDate());
					ps.setString(29, iLConnectionMapping.getHistoricalToDate());
					ps.setObject(30, iLConnectionMapping.getLoadInterval());
					ps.setString(31, null);
					ps.setObject(32, iLConnectionMapping.isJoinWebService());
					ps.setString(33, iLConnectionMapping.getWebserviceJoinUrls());
					ps.setString(34, iLConnectionMapping.getMaxDateQuery());
					ps.setString(35, iLConnectionMapping.getStorageType());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				connectionMappingId = autoIncrement.intValue();
			}

		} catch (DataAccessException ae) {
			LOG.error("error while saveILConnectionMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveILConnectionMapping()", e);
			
		}
		return connectionMappingId;
	}

	@Override
	public ILConnection getILConnectionById(int connectionId, String clientId, JdbcTemplate clientJdbcTemplate) {
		ILConnection iLConnection = null;
		;
		try {
			String sql = sqlHelper.getSql("getILConnectionById");

			iLConnection = clientJdbcTemplate.query(sql, new Object[] { connectionId },
					new ResultSetExtractor<ILConnection>() {
						@Override
						public ILConnection extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								ILConnection connection = new ILConnection();
								connection.setConnectionId(rs.getInt("connection_id"));
								connection.setConnectionName(rs.getString("connection_name"));

								Database dataBase = new Database();
								dataBase.setId(rs.getInt("database_Id"));
								dataBase.setName(rs.getString("database_Name"));
								dataBase.setConnector_id(rs.getInt("connectorId"));
								dataBase.setDriverName(rs.getString("driver_name"));
								dataBase.setProtocal(rs.getString("protocal"));
								dataBase.setUrlFormat(rs.getString("url_format"));
								connection.setDatabase(dataBase);
								connection.setDateFormat(rs.getString("date_format"));
								connection.setTimeZone(rs.getString("time_zone"));
								connection.setDataSourceName(rs.getString("data_source_name"));
								connection.setDbVariables(rs.getString("db_variables"));
								connection.setSslEnable(rs.getBoolean("ssl_enable"));
								connection.setSslTrustKeyStoreFilePaths(rs.getString("ssl_trust_key_store_file_paths"));
								// decrypt client connection details
								try {
									connection.setConnectionType(EncryptionServiceImpl.getInstance()
											.decrypt(rs.getString("connection_type")));
									connection.setUsername(
											EncryptionServiceImpl.getInstance().decrypt(rs.getString("username")));
									connection.setPassword(
											EncryptionServiceImpl.getInstance().decrypt(rs.getString("password")));
									connection.setServer(
											EncryptionServiceImpl.getInstance().decrypt(rs.getString("server")));
								} catch (Exception e) {
									LOG.error("error while decrypting getILConnectionById", e);
								}

								// TODO get remaining fields if required
								return connection;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getILConnectionById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILConnectionById()", e);
			
		}
		return iLConnection;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Column> getTableStructure(String schemaName, String tableName, int industryId, String clientId,
			JdbcTemplate clientJdbcTemplate) {
		List<Column> columnList = null;
		final String table = tableName;
		try {
			columnList = (List<Column>) clientJdbcTemplate.execute(new StatementCallback() {
				@Override
				public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
					java.sql.DatabaseMetaData metaData = stmt.getConnection().getMetaData();
					ResultSet primaryKeyRs = metaData.getPrimaryKeys(null, null, table);
					ResultSet rs = metaData.getColumns(null, null, table, null);

					List<Column> primaryKeyColumnInfo = new ArrayList<>();
					while (primaryKeyRs.next()) {
						Column primaryKeycolumn = new Column();
						primaryKeycolumn.setColumnName(primaryKeyRs.getString("COLUMN_NAME"));
						primaryKeycolumn
								.setIsPrimaryKey(primaryKeyRs.getString("PK_NAME").equals("PRIMARY") ? true : false);
						primaryKeyColumnInfo.add(primaryKeycolumn);
					}
					/*
					 * ResultSetMetaData rsmetaData=rs.getMetaData(); int
					 * countcolumns = rsmetaData.getColumnCount(); for ( int
					 * i=1;i<=countcolumns;i++) { System.out.println(
					 * "columns -- > " + rsmetaData.getColumnLabel(i)); }
					 */
					List<Column> columnInfo = new ArrayList<>();
					while (rs.next()) {

						Column column = new Column();
						column.setSchemaName(rs.getString("TABLE_SCHEM"));
						column.setTableName(rs.getString("TABLE_NAME"));
						column.setColumnName(rs.getString("COLUMN_NAME"));
						column.setDataType(rs.getString("TYPE_NAME"));
						if (rs.getString("TYPE_NAME").contains("FLOAT") || rs.getString("TYPE_NAME").contains("DOUBLE")
								|| rs.getString("TYPE_NAME").contains("DECIMAL")
								|| rs.getString("TYPE_NAME").contains("NUMERIC")) {
							String scale = rs.getString("DECIMAL_DIGITS");
							if (scale != null) {
								column.setColumnSize(
										rs.getString("COLUMN_SIZE") + "," + rs.getString("DECIMAL_DIGITS"));
							} else {
								column.setColumnSize(rs.getString("COLUMN_SIZE"));
							}

						} else {
							column.setColumnSize(rs.getString("COLUMN_SIZE"));
						}
						column.setIsPrimaryKey(false);
						column.setIsNotNull(rs.getString("IS_NULLABLE").equals("YES") ? false : true);
						column.setDefaultValue(rs.getString("COLUMN_DEF"));
						column.setIsAutoIncrement(rs.getString("IS_AUTOINCREMENT").equals("YES") ? true : false);
						columnInfo.add(column);
					}
					for (Column pkColumn : primaryKeyColumnInfo) {
						for (Column column : columnInfo) {
							if (column.getColumnName().equals(pkColumn.getColumnName())) {
								if (pkColumn.getIsPrimaryKey()) {
									column.setIsPrimaryKey(true);
								}

							}
						}

					}

					return columnInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getTableStructure()", ae);
			throw new AnvizentRuntimeException(ae);
		}

		return columnList;
	}

	@Override
	public List<ILConnectionMapping> getPendingILs(String clientId, JdbcTemplate clientJdbcTemplate) {
		List<ILConnectionMapping> pendingILList = null;
		try {
			String sql = sqlHelper.getSql("getPendingILs");
			pendingILList = clientJdbcTemplate.query(sql, new Object[] { clientId },
					new RowMapper<ILConnectionMapping>() {
						public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {

							ILConnectionMapping pendingIL = new ILConnectionMapping();
							pendingIL.setIsFlatFile(rs.getBoolean("isFlatFile"));
							pendingIL.setFileType(rs.getString("fileType") != null ? rs.getString("fileType") : "");
							pendingIL.setFilePath(rs.getString("filePath") != null ? rs.getString("filePath") : "");
							pendingIL.setDelimeter(rs.getString("delimeter") != null ? rs.getString("delimeter") : "");
							pendingIL.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
							pendingIL.setTypeOfCommand(
									rs.getString("type_of_command") != null ? rs.getString("type_of_command") : "");
							pendingIL.setiLquery(rs.getString("IL_query") != null ? rs.getString("IL_query") : "");
							pendingIL.setDatabaseName(
									rs.getString("Database_Name") != null ? rs.getString("Database_Name") : "");

							ILConnection connection = new ILConnection();
							connection.setConnectionId(rs.getInt("connection_id"));
							connection.setConnectionName(
									rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
							connection.setConnectionType(
									rs.getString("connection_type") != null ? rs.getString("connection_type") : "");

							Database database = new Database();
							database.setId(rs.getInt("id"));
							database.setName(rs.getString("name") != null ? rs.getString("name") : "");

							connection.setDatabase(database);
							connection.setServer(rs.getString("server") != null ? rs.getString("server") : "");
							connection.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
							connection.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
							connection.setSslEnable(rs.getBoolean("ssl_enable"));
							connection.setSslTrustKeyStoreFilePaths(rs.getString("ssl_trust_key_store_file_paths"));
							pendingIL.setiLConnection(connection);

							pendingIL.setiLId(rs.getInt("IL_id"));
							pendingIL.setdLId(rs.getInt("DL_id"));
							pendingIL.setPackageId(rs.getInt("Package_id"));
							return pendingIL;
						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getPendingILs()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPendingILs()", e);
			
		}

		return pendingILList;
	}

	@Override
	public List<ILConnectionMapping> getILConnectionMappingInfo(Integer iLId, Integer dLId, Integer packageId,
			String clientId, JdbcTemplate clientJdbcTemplate) {

		List<ILConnectionMapping> iLMappingList = new ArrayList<>();
		List<ILConnectionMapping> iLMappingListDatabase = new ArrayList<>();
		List<ILConnectionMapping> iLMappingListFile = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getILConnectionMappingInfo");

			iLMappingListDatabase = clientJdbcTemplate.query(sql, new Object[] { clientId, iLId, dLId, packageId },
					new RowMapper<ILConnectionMapping>() {
						public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {
							ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
							try {
								ilConnectionMapping.setClientId(clientId);
								ilConnectionMapping.setdLId(dLId);
								ilConnectionMapping.setiLId(iLId);
								ilConnectionMapping.setPackageId(packageId);
								ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
								ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
								ilConnectionMapping.setFileType(rs.getString("fileType"));
								ilConnectionMapping.setFilePath(rs.getString("filePath"));
								ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
								ilConnectionMapping
										.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
								ilConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
								ilConnectionMapping
										.setiLquery(rs.getString("IL_query") != null ? (rs.getString("IL_query")) : "");
								ilConnectionMapping.setDatabaseName(
										rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
								ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));
								ilConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));

								ILConnection connection = new ILConnection();
								connection.setConnectionId(rs.getInt("connection_id"));
								connection.setConnectionName(
										rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
								connection.setConnectionType(
										rs.getString("connection_type") != null ? rs.getString("connection_type") : "");

								Database database = new Database();
								database.setId(rs.getInt("id"));
								database.setName(rs.getString("name") != null ? rs.getString("name") : "");
								connection.setDatabase(database);
								connection.setServer(rs.getString("server") != null ? rs.getString("server") : "");
								connection
										.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
								connection.setSslEnable(rs.getBoolean("ssl_enable"));
								connection.setSslTrustKeyStoreFilePaths(rs.getString("ssl_trust_key_store_file_paths"));
								ilConnectionMapping.setiLConnection(connection);

								Modification modification = new Modification();
								modification.setCreatedTime(rs.getString("created_time"));
								ilConnectionMapping.setModification(modification);

								ilConnectionMapping.setiLConnection(connection);
							} catch (Exception e) {
								LOG.error("error while getILConnectionMappingInfo()", e);
								
							}
							return ilConnectionMapping;
						}

					});

			String sql2 = sqlHelper.getSql("getILConnectionMappingInfoWhenFile");
			iLMappingListFile = clientJdbcTemplate.query(sql2, new Object[] { clientId, iLId, dLId, packageId },
					new RowMapper<ILConnectionMapping>() {
						public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {
							ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
							try {

								ilConnectionMapping.setClientId(clientId);
								ilConnectionMapping.setdLId(dLId);
								ilConnectionMapping.setiLId(iLId);
								ilConnectionMapping.setPackageId(packageId);
								ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
								ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
								ilConnectionMapping.setFileType(rs.getString("fileType"));
								ilConnectionMapping.setFilePath(rs.getString("filePath"));
								ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
								ilConnectionMapping
										.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
								ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));

								Modification modification = new Modification();
								modification.setCreatedTime(rs.getString("created_time"));
								ilConnectionMapping.setModification(modification);
							} catch (Exception e) {
								LOG.error("error while getILConnectionMappingInfo()", e);
								
							}
							return ilConnectionMapping;
						}

					});

			for (ILConnectionMapping ilConnectionMapping : iLMappingListDatabase) {
				iLMappingList.add(ilConnectionMapping);
			}
			for (ILConnectionMapping ilConnectionMapping : iLMappingListFile) {
				iLMappingList.add(ilConnectionMapping);
			}

		} catch (DataAccessException ae) {
			LOG.error("error while getILConnectionMappingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILConnectionMappingInfo()", e);
			
		}

		return iLMappingList;
	}

	@Override
	public void updatePackageScheduleStatus(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("updatePackageScheduleStatus");
			clientJdbcTemplate.update(sql,
					new Object[] { clientData.getUserPackage().getIsScheduled(),
							clientData.getUserPackage().getScheduleStatus(),
							clientData.getModification().getModifiedBy(),
							clientData.getModification().getModifiedTime(), clientData.getUserPackage().getPackageId() });
		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageScheduleStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageScheduleStatus()", e);
			
		}

	}

	@Override
	public void updatePackageMappingStatus(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("updatePackageMappingStatus");
			clientJdbcTemplate.update(sql,
					new Object[] { clientData.getUserPackage().getIsMapped(),
							clientData.getModification().getModifiedBy(),
							clientData.getModification().getModifiedTime(), clientData.getUserPackage().getPackageId() });
		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageMappingStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageMappingStatus()", e);
			
		}
	}

	@Override
	public List<ClientData> getILConnectionMappingInfoByPackage(String userId, String clientId, int packageId,
			boolean isStandard, JdbcTemplate clientJdbcTemplate,boolean isActiveRequired) {
		List<ClientData> iLMappingList = new ArrayList<>();
		List<ClientData> iLMappingListWithDatabase = null;
		List<ClientData> iLMappingListWithFile = null;
		List<ClientData> iLMappingListWithWebService = null;
		try {

			String sql = sqlHelper.getSql("getILConnectionMappingInfoByPackage");
			Object[] parameters = new Object[] { clientId, packageId };

			/*
			 * if(isStandard){ sql += "and isDLMapped = '1'"; }
			 */
			iLMappingListWithDatabase = clientJdbcTemplate.query(sql, parameters, new RowMapper<ClientData>() {
				public ClientData mapRow(ResultSet rs, int i) throws SQLException {
				if(isActiveRequired && !rs.getBoolean("isActive")){
					return null;
				}
				if (!isStandard && !userId.equals(rs.getString("userid"))) {
					return null;
				}
					
					ClientData clientData = new ClientData();
					clientData.setUserId(userId);
					
					Package userPackage = new Package();
					userPackage.setPackageId(packageId);
					userPackage.setIsStandard(isStandard);
					clientData.setUserPackage(userPackage);

					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setClientId(rs.getString("userid"));
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setFileType(rs.getString("fileType"));
					ilConnectionMapping.setFilePath(rs.getString("filePath"));
					ilConnectionMapping.setSourceFileInfoId(rs.getLong("source_file_info_id"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
					ilConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
					ilConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
					ilConnectionMapping.setiLquery(rs.getString("IL_query") != null ? (rs.getString("IL_query")) : "");
					ilConnectionMapping.setMaxDateQuery(
							rs.getString("max_date_query") != null ? (rs.getString("max_date_query")) : "");
					ilConnectionMapping.setDatabaseName(
							rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
					ilConnectionMapping.setIsIncrementalUpdate(rs.getBoolean("isIncrementalUpdate"));
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));
					ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
					ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));

					ilConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));

					ilConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
					ilConnectionMapping.setParent_table_name(rs.getString("parent_table_name"));

					ILConnection connection = new ILConnection();
					connection.setConnectionId(rs.getInt("connection_id"));
					connection.setConnectionName(
							rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
					connection.setConnectionType(
							rs.getString("connection_type") != null ? rs.getString("connection_type") : "");
					connection.setAvailableInCloud(rs.getBoolean("available_in_cloud"));
					connection.setDbVariables(rs.getString("db_variables"));
					connection.setSslEnable(rs.getBoolean("ssl_enable"));
					connection.setSslTrustKeyStoreFilePaths(rs.getString("ssl_trust_key_store_file_paths"));
					
					Database database = new Database();
					database.setId(rs.getInt("dataBaseId"));
					database.setName(rs.getString("name") != null ? rs.getString("name") : "");
					database.setConnector_id(rs.getInt("connectorId"));
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setUrlFormat(rs.getString("url_format"));
					database.setConnectorJars(rs.getString("connector_jars"));
					
					connection.setDatabase(database);
					connection.setServer(rs.getString("server") != null ? rs.getString("server") : "");
					connection.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
					connection.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
					ilConnectionMapping.setiLConnection(connection);
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
					ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));

					WebService webService = new WebService();
					webService.setWsConId(rs.getInt("webservice_Id"));

					ilConnectionMapping.setWebService(webService);

					ilConnectionMapping.setIsHistoricalLoad(rs.getBoolean("is_historical_load"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));
					clientData.setIlConnectionMapping(ilConnectionMapping);

					return clientData;
				}

			});

			String sql2 = sqlHelper.getSql("getILConnectionMappingInfoWhenFileBYPackage");
			Object[] parameters2 = new Object[] { packageId };
			iLMappingListWithFile = clientJdbcTemplate.query(sql2, parameters2, new RowMapper<ClientData>() {
				public ClientData mapRow(ResultSet rs, int i) throws SQLException {
					if(isActiveRequired && !rs.getBoolean("isActive")){
						return null;
					}
					if (!isStandard && !userId.equals(rs.getString("userid"))) {
						return null;
					}
					ClientData clientData = new ClientData();
					clientData.setUserId(userId);

					Package userPackage = new Package();
					userPackage.setPackageId(packageId);
					userPackage.setIsStandard(isStandard);
					clientData.setUserPackage(userPackage);

					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setClientId(rs.getString("userid"));
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
					ilConnectionMapping.setFileType(rs.getString("fileType"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setFilePath(rs.getString("filePath"));
					ilConnectionMapping.setEncryptionRequired(rs.getBoolean("encryption_required"));
					ilConnectionMapping.setSourceFileInfoId(rs.getLong("source_file_info_id"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
					ilConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));
					ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));
					ilConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
					ILConnection connection = new ILConnection();
					Database database = new Database();
					connection.setDatabase(database);
					ilConnectionMapping.setiLConnection(connection);
					clientData.setIlConnectionMapping(ilConnectionMapping);
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
					ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));

					WebService webService = new WebService();
					webService.setWsConId(rs.getInt("webservice_Id"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));

					ilConnectionMapping.setWebService(webService);

					clientData.setIlConnectionMapping(ilConnectionMapping);
					return clientData;
				}

			});

			Object[] parameters4 = new Object[] { clientId, packageId };
			String sql4 = sqlHelper.getSql("getILConnectionMappingInfoWhenWebService");
			iLMappingListWithWebService = clientJdbcTemplate.query(sql4, parameters4, new RowMapper<ClientData>() {
				public ClientData mapRow(ResultSet rs, int i) throws SQLException {
					if(isActiveRequired && !rs.getBoolean("isActive")){
						return null;
					}
					if (!isStandard && !userId.equals(rs.getString("userid"))) {
						return null;
					}
					ClientData clientData = new ClientData();
					clientData.setUserId(userId);

					Package userPackage = new Package();
					userPackage.setPackageId(packageId);
					userPackage.setIsStandard(isStandard);
					clientData.setUserPackage(userPackage);

					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setClientId(rs.getString("userid"));
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
					ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));
					ilConnectionMapping.setWebserviceMappingHeaders(rs.getString("webservice_mapping_headers"));
					ilConnectionMapping.setIsFlatFile(Boolean.FALSE);
					ilConnectionMapping.setFileType(rs.getString("fileType"));
					ilConnectionMapping.setIsHavingParentTable(Boolean.FALSE);
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));
					ilConnectionMapping.setJoinWebService(rs.getBoolean("is_join_web_service"));
					ilConnectionMapping.setiLquery(rs.getString("IL_query"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setWebserviceJoinUrls(rs.getString("web_service_join_urls"));

					WebService webService = new WebService();
					webService.setApiName(rs.getString("api_name"));
					webService.setUrl(rs.getString("api_url"));
					webService.setWebserviceName(rs.getString("web_service_name"));
					webService.setWsConId(rs.getInt("webservice_Id"));
					
					ilConnectionMapping.setWebService(webService);

					clientData.setIlConnectionMapping(ilConnectionMapping);

					return clientData;
				}

			});
			for (ClientData clientData : iLMappingListWithDatabase) {
				if(clientData != null){
					iLMappingList.add(clientData);
				}
			}
			for (ClientData clientData : iLMappingListWithFile) {
				if(clientData != null){
					iLMappingList.add(clientData);
				}
			}
			for (ClientData clientData : iLMappingListWithWebService) {
				if(clientData != null){
					iLMappingList.add(clientData);
				}
			}
		} catch (DataAccessException ae) {
			LOG.error("error while getILConnectionMappingInfoByPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILConnectionMappingInfoByPackage()", e);
			
		}

		return iLMappingList;
	}

	@Override
	public int saveTargetTableInfo(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		int targetTableId = -1;
		try {
			final ClientData clientDataObject = clientData;
			final String sql = sqlHelper.getSql("saveTargetTableInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, clientDataObject.getUserId());
					ps.setInt(2, clientDataObject.getUserPackage().getPackageId());
					ps.setString(3, clientDataObject.getUserPackage().getTable().getSchemaName());
					ps.setString(4, clientDataObject.getUserPackage().getTable().getTableName());
					ps.setBoolean(5, clientDataObject.getUserPackage().getTable().getIsDirect());
					ps.setString(6, clientDataObject.getModification().getipAddress());
					ps.setString(7, clientDataObject.getModification().getCreatedBy());
					ps.setString(8, clientDataObject.getModification().getCreatedTime());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				targetTableId = autoIncrement.intValue();
			}
		} catch (DuplicateKeyException ae) {
			LOG.error("error while saveTargetTableInfo()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOG.error("error while saveTargetTableInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveTargetTableInfo()", e);
			
		}

		return targetTableId;
	}

	@Override
	public String createTargetTable(ClientData clientData, JdbcTemplate clientJdbcTemplate) throws Exception {
		//String sql = buildMySQLCreateTable(clientData);
		String sql = buildNewMySQLCreateTable(clientData);
		clientJdbcTemplate.execute(sql);
		LOG.info(clientData.getUserPackage().getTable().getTableName() + " table created in client "
				+ clientData.getUserId() + " schema");
		return sql;

	}
	
	
	
	public String buildNewMySQLCreateTable(ClientData clientData) {

		/*
		 * CREATE TABLE manufacturing_1001.new_table ( id BIGINT(10) NOT NULL
		 * AUTO_INCREMENT , name VARCHAR(45) NOT NULL , isActive BIT(1) NOT NULL
		 * , date DATETIME NOT NULL , PRIMARY KEY (id, name, isActive, date) ,
		 * UNIQUE INDEX id_UNIQUE (id ASC) );
		 */

		String script = null;
		StringBuilder query = new StringBuilder("CREATE TABLE");
		query.append(" `");
		query.append(clientData.getUserPackage().getTable().getTableName());
		query.append("`(\n");
		// add columns
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (StringUtils.isNotBlank(column.getColumnName())) {

				String columnName = column.getColumnName();

				columnName = columnName.trim().replaceAll("\\s+", "_").replaceAll("\\W+", "_");

				query.append("`").append(columnName).append("`");
				query.append(" ");
				query.append(column.getDataType());

				// append the column size
				if (StringUtils.isNotEmpty(column.getColumnSize())) {
					query.append("(" + column.getColumnSize() + ")");
				}
				query.append(" ");
				if (column.getIsNotNull().equals(Boolean.TRUE)) {
					query.append("NOT NULL");
				} else {
					query.append("NULL");
				}
				if ((column.getDataType().equals("BIGINT") || column.getDataType().equals("INT"))
						&& column.getIsAutoIncrement().equals(Boolean.TRUE)) {
					query.append(" ");
					query.append("AUTO_INCREMENT");
				}
				if (StringUtils.isNotBlank(column.getDefaultValue())) {
					query.append(" DEFAULT ");

					if (column.getDataType().equals("BIGINT") || column.getDataType().equals("INT")
							|| column.getDataType().equals("DECIMAL") || column.getDataType().equals("BIT"))
						query.append(column.getDefaultValue());
					else
						query.append("'").append(column.getDefaultValue()).append("'");
				}
				query.append(",\n");
			}
		}
		query.replace(query.lastIndexOf(","), query.length(), "");
		// check for PKs
		boolean isHavingPK = Boolean.FALSE;
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (column.getIsPrimaryKey().equals(Boolean.TRUE)) {
				isHavingPK = Boolean.TRUE;
				break;
			}
		}
		if (isHavingPK) {
			query.append(",\n");
			query.append("PRIMARY KEY (");
		}
		// add PKs
		StringBuilder primaryKeyConstraint = new StringBuilder();
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (column.getIsPrimaryKey()) {
				if (column.getIsAutoIncrement()) {
					String primaryKeyConstraintStr = primaryKeyConstraint.toString();
					primaryKeyConstraint = new StringBuilder();
					primaryKeyConstraint.append("`").append(column.getColumnName()).append("`");
					primaryKeyConstraint.append(",").append(primaryKeyConstraintStr);

				} else {
					primaryKeyConstraint.append("`").append(column.getColumnName()).append("`");
					primaryKeyConstraint.append(",");
				}

			}
		}
		// remove ',' at the end of last PK
		if (isHavingPK) {
			primaryKeyConstraint.replace(primaryKeyConstraint.lastIndexOf(","), primaryKeyConstraint.length(), "");
			primaryKeyConstraint.append(")");
		}
		query.append(primaryKeyConstraint);
		// add UQs
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (column.getIsUnique()) {
				query.append(",\n");
				query.append("UNIQUE INDEX ");
				query.append("`").append(column.getColumnName() + "_UNIQUE").append("`");
				query.append(" (");
				query.append("`").append(column.getColumnName()).append("`");
				query.append(" ASC)");
			}
		}
		query.append(") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ");
		script = query.toString();
		LOG.debug("query>>>>>>\n" + script);
		return script;
	}
	
	
	
	
	
	
	
	
	
	

	public String buildMySQLCreateTable(ClientData clientData) {

		/*
		 * CREATE TABLE manufacturing_1001.new_table ( id BIGINT(10) NOT NULL
		 * AUTO_INCREMENT , name VARCHAR(45) NOT NULL , isActive BIT(1) NOT NULL
		 * , date DATETIME NOT NULL , PRIMARY KEY (id, name, isActive, date) ,
		 * UNIQUE INDEX id_UNIQUE (id ASC) );
		 */

		String script = null;
		StringBuilder query = new StringBuilder("CREATE TABLE");
		query.append(" `");
		query.append(clientData.getUserPackage().getTable().getTableName());
		query.append("`(\n");
		// add columns
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (StringUtils.isNotBlank(column.getColumnName())) {

				String columnName = column.getColumnName();

				columnName = columnName.trim().replaceAll("\\s+", "_").replaceAll("\\W+", "_");

				query.append("`").append(columnName).append("`");
				query.append(" ");
				query.append(column.getDataType());

				// append the column size
				if (!column.getDataType().equals("DATETIME")) {

					boolean addbraces = true;

					/*
					 * if (column.getColumnSize() == null) { addbraces = false;
					 * }
					 */
					if(!column.getDataType().equals("DATE") ){

						addbraces = (column.getColumnSize() != null);
	
						if (addbraces && column.getDataType().equals("DECIMAL")) {
							addbraces = (column.getDecimalPoints() != null);
						}
	
						if (addbraces)
							query.append("(");
	
						if (column.getDataType().equals("DECIMAL")) {
							if (column.getColumnSize() != null && column.getDecimalPoints() != null) {
								query.append(column.getColumnSize());
								query.append(",");
								query.append(column.getDecimalPoints());
							} /*
								 * else{ query.append("10,2"); }
								 */
						} else {
							if (StringUtils.isNotBlank(column.getColumnSize())) {
								query.append(column.getColumnSize());
							}
						}
	
						if (addbraces)
							query.append(")");
					}
			     }
				query.append(" ");
				if (column.getIsNotNull().equals(Boolean.TRUE)) {
					query.append("NOT NULL");
				} else {
					query.append("NULL");
				}
				if ((column.getDataType().equals("BIGINT") || column.getDataType().equals("INT"))
						&& column.getIsAutoIncrement().equals(Boolean.TRUE)) {
					query.append(" ");
					query.append("AUTO_INCREMENT");
				}
				if (StringUtils.isNotBlank(column.getDefaultValue())) {
					query.append(" DEFAULT ");

					if (column.getDataType().equals("BIGINT") || column.getDataType().equals("INT")
							|| column.getDataType().equals("DECIMAL") || column.getDataType().equals("BIT"))
						query.append(column.getDefaultValue());
					else
						query.append("'").append(column.getDefaultValue()).append("'");
				}
				query.append(",\n");
			}
		}
		query.replace(query.lastIndexOf(","), query.length(), "");
		// check for PKs
		boolean isHavingPK = Boolean.FALSE;
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (column.getIsPrimaryKey().equals(Boolean.TRUE)) {
				isHavingPK = Boolean.TRUE;
				break;
			}
		}
		if (isHavingPK) {
			query.append(",\n");
			query.append("PRIMARY KEY (");
		}
		// add PKs
		StringBuilder primaryKeyConstraint = new StringBuilder();
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (column.getIsPrimaryKey()) {
				if (column.getIsAutoIncrement()) {
					String primaryKeyConstraintStr = primaryKeyConstraint.toString();
					primaryKeyConstraint = new StringBuilder();
					primaryKeyConstraint.append("`").append(column.getColumnName()).append("`");
					primaryKeyConstraint.append(",").append(primaryKeyConstraintStr);

				} else {
					primaryKeyConstraint.append("`").append(column.getColumnName()).append("`");
					primaryKeyConstraint.append(",");
				}

			}
		}
		// remove ',' at the end of last PK
		if (isHavingPK) {
			primaryKeyConstraint.replace(primaryKeyConstraint.lastIndexOf(","), primaryKeyConstraint.length(), "");
			primaryKeyConstraint.append(")");
		}
		query.append(primaryKeyConstraint);
		// add UQs
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (column.getIsUnique()) {
				query.append(",\n");
				query.append("UNIQUE INDEX ");
				query.append("`").append(column.getColumnName() + "_UNIQUE").append("`");
				query.append(" (");
				query.append("`").append(column.getColumnName()).append("`");
				query.append(" ASC)");
			}
		}
		query.append(") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ");
		script = query.toString();
		LOG.debug("query>>>>>>\n" + script);
		return script;
	}

	@Override
	public ClientData getTargetTableInfoByPackage(String clientId, int packageId, JdbcTemplate clientJdbcTemplate) {
		ClientData targetTableInfo = null;
		;
		try {
			String sql = sqlHelper.getSql("getTargetTableInfoByPackage");

			targetTableInfo = clientJdbcTemplate.query(sql, new Object[] { clientId, packageId },
					new ResultSetExtractor<ClientData>() {
						@Override
						public ClientData extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								ClientData clientData = new ClientData();
								clientData.setUserId(clientId);

								Package userPackage = new Package();
								userPackage.setPackageId(packageId);

								Table table = new Table();
								table.setTableId(rs.getInt("id"));
								table.setSchemaName(rs.getString("schemaName"));
								table.setTableName(rs.getString("target_table_name"));
								table.setIsProcessed(rs.getBoolean("isProcessed"));
								table.setIsDirect(rs.getBoolean("direct_or_fetch_from_file"));
								table.setNoOfRecordsProcessed(rs.getInt("no_of_records_processed"));
								table.setNoOfRecordsFailed(rs.getInt("no_of_records_failed"));
								table.setDuplicateRecords(rs.getInt("duplicate_records"));
								table.setTotalRecords(rs.getInt("total_records"));
								userPackage.setTable(table);
								clientData.setUserPackage(userPackage);
								return clientData;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getTargetTableInfoByPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getTargetTableInfoByPackage()", e);
			
		}
		return targetTableInfo;
	}

	@Override
	public List<Table> getAllTargetTablesOfCustomPackage(String clientId, int packageId,
			JdbcTemplate clientJdbcTemplate) {
		List<Table> targetTableList = null;
		try {
			String sql = sqlHelper.getSql("getAllTargetTablesOfCustomPackage");
			targetTableList = clientJdbcTemplate.query(sql, new Object[] { clientId, packageId },
					new RowMapper<Table>() {
						public Table mapRow(ResultSet rs, int i) throws SQLException {
							Table targetTable = new Table();
							targetTable.setTableId(rs.getInt("id"));
							targetTable.setSchemaName(rs.getString("schemaName"));
							targetTable.setTableName(rs.getString("target_table_name"));
							return targetTable;
						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getAllTargetTablesOfCustomPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getAllTargetTablesOfCustomPackage()", e);
			
		}

		return targetTableList;
	}

	@Override
	public int updateDLMappingStatus(String clientId, int packageId, int dLid, int iLId, boolean dLMappingStatus,
			JdbcTemplate clientJdbcTemplate) {
		int i = -1;
		try {
			String sql = sqlHelper.getSql("updateDLMappingStatus");
			i = clientJdbcTemplate.update(sql, new Object[] { dLMappingStatus, clientId, packageId, dLid, iLId });
		} catch (DataAccessException ae) {
			LOG.error("error while updateDLMappingStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateDLMappingStatus()", e);
			
		}
		return i;
	}

	@Override
	public int updateILJobStatusForRunNow(String clientId, int packageId, int dLid, int iLId,
			String iLJobStatusForRunNow, JdbcTemplate clientJdbcTemplate) {
		int i = -1;
		try {
			String sql = sqlHelper.getSql("updateILJobStatusForRunNow");
			i = clientJdbcTemplate.update(sql, new Object[] { iLJobStatusForRunNow, clientId, packageId, dLid, iLId });
		} catch (DataAccessException ae) {
			LOG.error("error while updateILJobStatusForRunNow()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateILJobStatusForRunNow()", e);
			
		}
		return i;
	}

	@Override
	public int updateTargetTableInfo(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		int i = -1;
		try {
			String sql = sqlHelper.getSql("updateTargetTableInfo");
			i = clientJdbcTemplate.update(sql,
					new Object[] { clientData.getUserPackage().getTable().getIsProcessed(),
							clientData.getUserPackage().getTable().getNoOfRecordsProcessed(),
							clientData.getUserPackage().getTable().getNoOfRecordsFailed(),
							clientData.getUserPackage().getTable().getDuplicateRecords(),
							clientData.getUserPackage().getTable().getTotalRecords(),
							clientData.getModification().getModifiedBy(),
							clientData.getModification().getModifiedTime(), clientData.getUserId(),
							clientData.getUserPackage().getPackageId(),
							clientData.getUserPackage().getTable().getTableId() });
		} catch (DataAccessException ae) {
			LOG.error("error while updateTargetTableInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateTargetTableInfo()", e);
			
		}
		return i;
	}

	@Override
	public boolean deleteConnectionMapping(String clientId, String packageId, String mappingId,
			JdbcTemplate clientJdbcTemplate) {
		boolean status = false;

		try {
			int pkgId = Integer.parseInt(packageId);
			Integer sourceFileInfoId = null;

			List<ILConnectionMapping> connectionMappingList = getIlConnectionMappingInfoByPackageId(clientId, pkgId,clientJdbcTemplate);
			for (ILConnectionMapping iLConnectionMapping : connectionMappingList) {
				if (iLConnectionMapping.getConnectionMappingId() == Integer.parseInt(mappingId)) {
					sourceFileInfoId = (int) iLConnectionMapping.getSourceFileInfoId();
				}
			}

			String sql = sqlHelper.getSql("deleteConnectionMapping");

			int count = clientJdbcTemplate.update(sql, new Object[] { clientId, packageId, mappingId });

			status = count > 0;
  
			sql = sqlHelper.getSql("deleteCustomFileHeaderInfoMapping");

			clientJdbcTemplate.update(sql, new Object[] { clientId, packageId, sourceFileInfoId });

		} catch (DataAccessException ae) {
			LOG.error("error while updateTargetTableInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateTargetTableInfo()", e);
			
		}

		return status;
	}

	@Override
	public DLInfo getDLById(int dLId, String clinetId, JdbcTemplate clientJdbcTemplate) {
		DLInfo dLInfo = null;
		;
		try {
			String sql = sqlHelper.getSql("getDLById");

			dLInfo = clientJdbcTemplate.query(sql, new Object[] { dLId }, new ResultSetExtractor<DLInfo>() {
				@Override
				public DLInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						DLInfo dl = new DLInfo();
						dl.setdL_id(rs.getInt("DL_id"));
						dl.setdL_name(rs.getString("DL_name"));
						dl.setdL_table_name(rs.getString("dl_table_name"));
						dl.setDescription(rs.getString("description"));
						dl.setdL_schema(rs.getString("DL_schema"));
						dl.setIndustry(new Industry(rs.getInt("industry_id")));
						dl.setIsStandard(rs.getBoolean("isStandard"));
						dl.setJobName(rs.getString("Job_name"));
						dl.setDL_tgt_key(rs.getString("DL_tgt_key"));

						return dl;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getDLById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDLById()", e);
			
		}
		return dLInfo;
	}

	@Override
	public DLInfo getDLByIdWithJobName(int dLId, String clinetId, JdbcTemplate clientJdbcTemplate) {
		DLInfo dLInfo = null;
		;
		try {
			String sql = sqlHelper.getSql("getDLByIdWithJobName");

			dLInfo = clientJdbcTemplate.query(sql, new Object[] { clinetId, dLId }, new ResultSetExtractor<DLInfo>() {
				@Override
				public DLInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						DLInfo dl = new DLInfo();
						dl.setdL_id(rs.getInt("DL_id"));
						dl.setdL_name(rs.getString("DL_name"));
						dl.setdL_table_name(rs.getString("dl_table_name"));
						dl.setDescription(rs.getString("description"));
						dl.setdL_schema(rs.getString("DL_schema"));
						dl.setIndustry(new Industry(rs.getInt("industry_id")));
						dl.setIsStandard(rs.getBoolean("isStandard"));
						dl.setJobName(rs.getString("Job_name"));
						dl.setDL_tgt_key(rs.getString("DL_tgt_key"));
						dl.setDependencyJars(rs.getString("dependency_jars"));
						dl.setJobExecutionType(rs.getString("job_execution_type"));
						dl.setJobTagId(rs.getLong("elt_job_tag"));
						dl.setLoadParameterId(rs.getLong("elt_load_parameter"));
						dl.setMasterParameterId(rs.getLong("elt_master_id"));
						dl.setTrailingMonths(rs.getInt("trailing_months"));
						return dl;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getDLByIdWithJobName()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDLByIdWithJobName()", e);
			
		}
		return dLInfo;
	}

	@Override
	public ILInfo getILById(int iLId, String clientId, JdbcTemplate clientJdbcTemplate) {
		ILInfo iLInfo = null;
		;
		try {
			String sql = sqlHelper.getSql("getILById");

			iLInfo = clientJdbcTemplate.query(sql, new Object[] { iLId }, new ResultSetExtractor<ILInfo>() {
				@Override
				public ILInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						ILInfo iL = new ILInfo();
						iL.setiL_id(rs.getInt("IL_id"));
						iL.setiL_name(rs.getString("IL_name"));
						iL.setiL_table_name(rs.getString("il_table_name"));
						iL.setXref_il_table_name(rs.getString("xref_il_table_name"));
						iL.setDescription(rs.getString("description"));
						iL.setiL_schema(rs.getString("IL_schema"));
						iL.setSharedDLIds(rs.getString("DL_id"));
						iL.setJobName(rs.getString("Job_name"));
						iL.setSrcFileContextParamKey(rs.getString("src_file_context_param_key"));
						iL.setTargetTableContextParamKey(rs.getString("target_table_context_param_key"));
						return iL;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getILById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILById()", e);
			
		}
		return iLInfo;
	}

	@Override
	public ILInfo getILByIdWithJobName(int iLId, String clientId, JdbcTemplate clientJdbcTemplate) {
		ILInfo iLInfo = null;
		;
		try {
			String sql = sqlHelper.getSql("getILByIdWithJobName");

			iLInfo = clientJdbcTemplate.query(sql, new Object[] { clientId, iLId }, new ResultSetExtractor<ILInfo>() {
				@Override
				public ILInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						ILInfo iL = new ILInfo();
						iL.setiL_id(rs.getInt("IL_id"));
						iL.setiL_name(rs.getString("IL_name"));
						iL.setiL_table_name(rs.getString("il_table_name"));
						iL.setXref_il_table_name(rs.getString("xref_il_table_name"));
						iL.setDescription(rs.getString("description"));
						iL.setiL_schema(rs.getString("IL_schema"));
						iL.setSharedDLIds(rs.getString("DL_id"));
						iL.setJobName(rs.getString("Job_name"));
						iL.setSrcFileContextParamKey(rs.getString("src_file_context_param_key"));
						iL.setTargetTableContextParamKey(rs.getString("target_table_context_param_key"));
						iL.setDependencyJars(rs.getString("dependency_jars"));
						iL.setJobExecutionType(rs.getString("job_execution_type"));
						iL.setJobTagId(rs.getLong("elt_job_tag"));
						iL.setLoadParameterId(rs.getLong("elt_load_parameter"));
						iL.setMasterParameterId(rs.getLong("elt_master_id"));
						
						return iL;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getILByIdWithJobName()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILByIdWithJobName()", e);
			
		}
		return iLInfo;
	}

	@Override
	public void deleteIlSource(int connectionMappingId, String clientId, JdbcTemplate clientJdbcTemplate) {

		try {
			String sql = sqlHelper.getSql("deleteIlSource");

			clientJdbcTemplate.update(sql, new Object[] { connectionMappingId });

			// String sqlStatus = sqlHelper.getSql("deleteIlSourceByMappingId");

			// clientJdbcTemplate.update(sqlStatus, new Object[] {
			// connectionMappingId, "Pending" });

		} catch (DataAccessException ae) {
			LOG.error("error while deleteIlSource()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while deleteIlSource()", e);
			
		}

	}

	@Override
	public Map<String, Object> showCustomPackageTablesStatus(String clientId, String packageId, String industryId,
			JdbcTemplate clientJdbcTemplate) {

		Map<String, Object> tablesData = null;

		try {

			String sql = sqlHelper.getSql("showCustomTargetTableStatus");

			Table targetTable = clientJdbcTemplate.queryForObject(sql, new Object[] { clientId, packageId },
					new RowMapper<Table>() {

						@Override
						public Table mapRow(ResultSet rs, int rowNum) throws SQLException {

							if (rs == null)
								return null;

							Table table = new Table();
							table.setTableId(rs.getInt("id"));
							table.setTableName(rs.getString("target_table_name"));
							table.setNoOfRecordsFailed(rs.getInt("no_of_records_failed"));
							table.setNoOfRecordsProcessed(rs.getInt("no_of_records_processed"));
							table.setTotalRecords(rs.getInt("total_records"));

							return table;
						}
					});

			if (targetTable != null) {
				tablesData = new HashMap<>();
				tablesData.put("table", targetTable);

				sql = sqlHelper.getSql("showCustomTargetDerivativeTableStatus");

				List<Table> derivativeTables = clientJdbcTemplate.query(sql,
						new Object[] { clientId, packageId, targetTable.getTableId() }, new RowMapper<Table>() {

							@Override
							public Table mapRow(ResultSet rs, int rowNum) throws SQLException {

								if (rs == null)
									return null;

								Table table = new Table();

								table.setTableName(rs.getString("target_table_name"));
								table.setNoOfRecordsFailed(rs.getInt("no_of_records_failed"));
								table.setNoOfRecordsProcessed(rs.getInt("no_of_records_processed"));
								table.setTotalRecords(rs.getInt("total_records"));

								return table;
							}

						});

				tablesData.put("derivatives", derivativeTables);
			}
		} catch (DataAccessException ae) {
			LOG.error("error while getILById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILById()", e);
			
		} catch (Exception e) {
			LOG.error("error wihle reading custom package tables status ", e);
		}
		return tablesData;
	}

	@Override
	public Map<String, String> getIlContextParams(Integer ilId, JdbcTemplate clientJdbcTemplate) {

		Map<String, String> contextParams = null;

		try {

			String sql = sqlHelper.getSql("getIlContextParams");

			contextParams = clientJdbcTemplate.query(sql, new Object[] { ilId },
					new ResultSetExtractor<Map<String, String>>() {

						@Override
						public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs == null)
								return null;

							Map<String, String> contextParams = new LinkedHashMap<>();

							while (rs.next()) {
								contextParams.put(rs.getString(1), rs.getString(2));
							}

							return contextParams;
						}
					});

		} catch (Exception e) {
			LOG.error("Error while reading il context params ", e);
		}

		return contextParams;
	}

	@Override
	public Map<String, String> getDlContextParams(Integer dlId, JdbcTemplate clientJdbcTemplate) {

		Map<String, String> contextParams = null;

		try {

			String sql = sqlHelper.getSql("getDlContextParams");

			contextParams = clientJdbcTemplate.query(sql, new Object[] { dlId },
					new ResultSetExtractor<Map<String, String>>() {

						@Override
						public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs == null)
								return null;

							Map<String, String> contextParams = new LinkedHashMap<>();

							while (rs.next()) {
								contextParams.put(rs.getString(1), rs.getString(2));
							}

							return contextParams;
						}
					});

		} catch (Exception e) {
			LOG.error("Error while reading dl context params ", e);
		}

		return contextParams;
	}

	@Override
	public List<Object> getTablePreview(ILConnectionMapping ilConnectionMapping) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		String inputQuery = null;
		CallableStatement cstmt = null;
		List<String> columns = new ArrayList<>();
		List<Object> previewtableList = new ArrayList<>();
		ResultSetMetaData metaData = null;
		try {
			conn = CommonUtils.connectDatabase(ilConnectionMapping.getiLConnection());
			if (conn != null) {
				if (ilConnectionMapping.getTypeOfCommand().equals("Query")) {

					int connectorId = ilConnectionMapping.getiLConnection().getDatabase().getConnector_id();
					// MySQL
					if (connectorId == 1) {
						inputQuery = ilConnectionMapping.getiLquery() + " LIMIT 10 ";
					}
					// SQL Server
					else if (connectorId == 2) {
						inputQuery = "select top 10 * from (" + ilConnectionMapping.getiLquery() + ") x";
					}
					// Oracle
					else if (connectorId == 4) {
						inputQuery = "select * from (" + ilConnectionMapping.getiLquery() + ") WHERE ROWNUM <= 10 ";
					}
					// DB2
					else if (connectorId == 5) {
						inputQuery = "select * from (" + ilConnectionMapping.getiLquery()
								+ ") FETCH FIRST 10 ROWS ONLY ";
					}

					// Salesforce
					else if (connectorId == 6) {
						inputQuery = ilConnectionMapping.getiLquery() + " LIMIT 10 ";
					}
					stmt = conn.prepareStatement(inputQuery);
					rs = stmt.executeQuery();
					metaData = rs.getMetaData();
					if (rs != null) {
						int countcolumns = metaData.getColumnCount();
						for (int i = 1; i <= countcolumns; i++) {
							columns.add(metaData.getColumnLabel(i));
						}
						previewtableList.add(columns);
						while (rs.next()) {
							List<Object> rows = new ArrayList<>();
							for (int i = 1; i <= countcolumns; i++) {
								rows.add(rs.getString(i));
							}
							previewtableList.add(rows);
						}
					}

				} else if (ilConnectionMapping.getTypeOfCommand().equals("Stored Procedure")) {
					int databaseid = ilConnectionMapping.getiLConnection().getDatabase().getId();
					// MySQL-1 , DB2 -7
					if (databaseid == 1 || databaseid == 7) {
						inputQuery = "{ call " + ilConnectionMapping.getiLquery() + "() }";
					}
					// SQL Server-2 , Epicor with Sql Server-5 ,Oracle(4) , JD
					// Edwards(6)
					else if (databaseid == 2 || databaseid == 4 || databaseid == 5 || databaseid == 6) {

						inputQuery = "exec " + ilConnectionMapping.getiLquery();
					}
					cstmt = conn.prepareCall(inputQuery);
					rs = cstmt.executeQuery();
					metaData = rs.getMetaData();
					if (rs != null) {
						int countcolumns = metaData.getColumnCount();
						for (int i = 1; i <= countcolumns; i++) {
							columns.add(metaData.getColumnLabel(i));
						}
						previewtableList.add(columns);
						while (rs.next()) {
							List<Object> rows = new ArrayList<>();
							for (int i = 1; i <= countcolumns; i++) {
								rows.add(rs.getString(i));
							}
							previewtableList.add(rows);
						}
					}

				}
			}
		} finally {
			if (stmt != null)
				stmt.close();
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}
		return previewtableList;
	}

	@Override
	public boolean isTargetTableExist(String clientId, String tablename, JdbcTemplate clientJdbcTemplate) {
		boolean exists = false;
		try {
			String sql = sqlHelper.getSql("isTargetTableExist");

			Integer existingcount = clientJdbcTemplate.queryForObject(sql, new Object[] { clientId, tablename },
					new RowMapper<Integer>() {
						@Override
						public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
							if (rs == null)
								return null;

							return rs.getInt(1);
						}
					});

			exists = !(existingcount == null || existingcount.intValue() == 0);

		} catch (DataAccessException ae) {
			LOG.error("error while getIlEpicorQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getIlEpicorQuery()", e);
		}

		return exists;
	}

	@Override
	public String getIlEpicorQuery(int databasetypeid, int iLid, JdbcTemplate clientJdbcTemplate) {
		String ilEpicorQuery = null;
		try {
			String sql = sqlHelper.getSql("getIlEpicorQuery");
			ilEpicorQuery = clientJdbcTemplate.query(sql, new Object[] { iLid, databasetypeid },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String s = rs.getString("il_query");
								return s;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getIlEpicorQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getIlEpicorQuery()", e);
			
		}

		return ilEpicorQuery;

	}

	@Override
	public void updateFilePathForDatabaseConnection(long sourceFileId,int ilconnectionMappingId, String filePath, String storageType,
			Integer bucketId, Boolean multiPartEnabled, JdbcTemplate clientJdbcTemplate) {

		try {
			String sql = sqlHelper.getSql("updateFilePathForDatabaseConnection");
			String fileType = Constants.FileType.CSV;
			String delimeter = ",";
			clientJdbcTemplate.update(sql, new Object[] { sourceFileId, filePath, fileType, delimeter, storageType, bucketId,
					multiPartEnabled, ilconnectionMappingId });

		} catch (DataAccessException ae) {
			LOG.error("error while updateFilePathForDatabaseConnection()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateFilePathForDatabaseConnection()", e);
			
		}
	}

	@Override
	public void dropTable(String schemaName, String tableName, JdbcTemplate clientJdbcTemplate) {
		try {
			clientJdbcTemplate.execute("drop table IF EXISTS `" + tableName + "`;");
		} catch (Exception e) {
			LOG.error("error while dropTable()", e);
		}

	}

	@Override
	public boolean isILMapped(String userId, int packageId, int ilId,int dlId, String clientId,
			JdbcTemplate clientJdbcTemplate) {
		Boolean isILMapped = Boolean.FALSE;
		try {
			String sql = sqlHelper.getSql("isILMapped");
			Integer i = clientJdbcTemplate.queryForObject(sql,
					new Object[] { packageId, ilId, dlId, clientId, packageId, ilId, dlId }, Integer.class);
			if (i != null) {
				if (i.intValue() == 0) {
					isILMapped = Boolean.FALSE;
				} else {

					isILMapped = Boolean.TRUE;
				}
			}

		} catch (DataAccessException ae) {
			LOG.error("error while isILMapped", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while isILMapped", e);
			
		}

		return isILMapped;
	}

	@Override
	public ILConnectionMapping getILConnectionMappingInfoForPreview(int mappingId, int packageId, String clientId,
			JdbcTemplate clientJdbcTemplate) {

		ILConnectionMapping iLMapping = null;
		try {

			String sql = sqlHelper.getSql("getILConnectionMappingInfoWhenFilePreview");
			Object[] parametres = new Object[] { mappingId, packageId };
			iLMapping = clientJdbcTemplate.query(sql, parametres, new ResultSetExtractor<ILConnectionMapping>() {
				@Override
				public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {

					if (rs != null && rs.next()) {
						ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
						try {
							ilConnectionMapping.setClientId(clientId);
							ilConnectionMapping.setdLId(rs.getInt("DL_id"));
							ilConnectionMapping.setiLId(rs.getInt("IL_id"));
							ilConnectionMapping.setPackageId(packageId);
							ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
							ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
							ilConnectionMapping.setFileType(rs.getString("fileType"));
							ilConnectionMapping.setS3BucketId(rs.getInt("s3_bucket_id"));
							ilConnectionMapping.setStorageType(rs.getString("storage_type"));
							ilConnectionMapping.setFilePath(rs.getString("filePath"));
							ilConnectionMapping.setEncryptionRequired(rs.getBoolean("encryption_required"));
							ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
							ilConnectionMapping
									.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
							ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));
							ilConnectionMapping.setIsIncrementalUpdate(rs.getBoolean("isIncrementalUpdate"));
							ilConnectionMapping.setIsHistoricalLoad(rs.getBoolean("is_historical_load"));
							ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));
							ilConnectionMapping.setIsWebservice(false);
						} catch (Exception e) {
							LOG.error("error while getILConnectionMappingInfo()", e);
							
						}
						return ilConnectionMapping;
					} else {
						return null;
					}

				}
			});
			if (iLMapping == null) {

				String sql2 = sqlHelper.getSql("getILConnectionMappingInfoWhenDatabaseById");
				Object[] parameters2 = new Object[] { mappingId, packageId };
				iLMapping = clientJdbcTemplate.query(sql2, parameters2, new ResultSetExtractor<ILConnectionMapping>() {
					@Override
					public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {

						if (rs != null && rs.next()) {
							ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
							try {
								ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
								ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
								ilConnectionMapping.setFileType(rs.getString("fileType"));
								ilConnectionMapping.setStorageType(rs.getString("storage_type"));
								ilConnectionMapping.setFilePath(rs.getString("filePath"));
								ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
								ilConnectionMapping
										.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
								ilConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
								ilConnectionMapping
										.setiLquery(rs.getString("IL_query") != null ? (rs.getString("IL_query")) : "");
								ilConnectionMapping.setMaxDateQuery(
										rs.getString("max_date_query") != null ? (rs.getString("max_date_query")) : "");
								ilConnectionMapping.setDatabaseName(
										rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
								ilConnectionMapping.setiLId(rs.getInt("IL_id"));
								ilConnectionMapping.setdLId(rs.getInt("DL_id"));
								ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
								ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));
								ilConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));
								ilConnectionMapping.setIsIncrementalUpdate(rs.getBoolean("isIncrementalUpdate"));
								ilConnectionMapping.setIsHistoricalLoad(rs.getBoolean("is_historical_load"));
								ILConnection connection = new ILConnection();
								connection.setConnectionId(rs.getInt("connection_id"));
								connection.setConnectionName(
										rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
								connection.setConnectionType(
										rs.getString("connection_type") != null ? rs.getString("connection_type") : "");
								connection.setAvailableInCloud(rs.getBoolean("available_in_cloud"));
								connection.setSslEnable(rs.getBoolean("ssl_enable"));
								connection.setSslTrustKeyStoreFilePaths(rs.getString("ssl_trust_key_store_file_paths"));
								
								Database database = new Database();
								database.setId(rs.getInt("dataBaseId"));
								database.setName(rs.getString("name") != null ? rs.getString("name") : "");
								database.setConnector_id(rs.getInt("connectorId"));
								database.setDriverName(rs.getString("driver_name"));
								database.setProtocal(rs.getString("protocal"));
								database.setUrlFormat(rs.getString("url_format"));

								connection.setDatabase(database);
								connection.setServer(rs.getString("server") != null ? rs.getString("server") : "");
								connection
										.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
								connection
										.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
								connection.setDbVariables(rs.getString("db_variables"));
								ilConnectionMapping.setiLConnection(connection);
								ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
								ilConnectionMapping.setIsWebservice(false);
							} catch (Exception e) {
								LOG.error("error while getILConnectionMappingInfo()", e);
								
							}
							return ilConnectionMapping;
						} else {
							return null;
						}

					}
				});
			}

			if (iLMapping == null) {
				String sql3 = sqlHelper.getSql("getILConnectionMappingInfoWhenWebServiceByMappingId");
				Object[] parameters3 = new Object[] { mappingId, packageId };
				iLMapping = clientJdbcTemplate.query(sql3, parameters3, new ResultSetExtractor<ILConnectionMapping>() {

					@Override
					public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs != null && rs.next()) {
						ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
						ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
						ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
						ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
						ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));
						ilConnectionMapping.setWebserviceMappingHeaders(rs.getString("webservice_mapping_headers"));
						ilConnectionMapping.setIsFlatFile(Boolean.FALSE);
						ilConnectionMapping.setIsHavingParentTable(Boolean.FALSE);
						ilConnectionMapping.setiLId(rs.getInt("IL_id"));
						ilConnectionMapping.setdLId(rs.getInt("DL_id"));
						ilConnectionMapping.setJoinWebService(rs.getBoolean("is_join_web_service"));
						ilConnectionMapping.setiLquery(rs.getString("IL_query"));
						ilConnectionMapping.setWebserviceJoinUrls(rs.getString("web_service_join_urls"));
						ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
						WebService webService = new WebService();
						webService.setApiName(rs.getString("api_name"));
						webService.setUrl(rs.getString("api_url"));
						webService.setWebserviceName(rs.getString("web_service_name"));
						webService.setWsConId(rs.getInt("webservice_Id"));

						ilConnectionMapping.setWebService(webService);
						return ilConnectionMapping;
						} else {
							return null;
						}
					}
				});
			}

		} catch (DataAccessException ae) {
			LOG.error("error while getILConnectionMappingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILConnectionMappingInfo()", e);
			
		}

		return iLMapping;
	}

	@Override
	public void disablePackage(List<Integer> packageIds, JdbcTemplate clientJdbcTemplate) {
		try {

			String sql = sqlHelper.getSql("disablePackage");
			String sqlupdate = sqlHelper.getSql("updateIsScheduled");
			for(Integer packageId : packageIds){
				clientJdbcTemplate.update(sql, new Object[] { packageId });
				clientJdbcTemplate.update(sqlupdate, new Object[] { packageId });
				LOG.info("Package is Disabled. Id : " + packageId + "");
			}
		} catch (DataAccessException ae) {
			LOG.error("error while disablePackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while disablePackage()", e);
			
		}

	}

	@Override
	public List<JobResult> getJobResults(String packageId, String userId, String clientStagingSchema,
			JdbcTemplate clientJdbcTemplate) {
		List<JobResult> resultList = null;
		try {
			String batchIdParam = userId + "\\_" + packageId + "\\_%";
			String sql = sqlHelper.getSql("getJobResults");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam }, new RowMapper<JobResult>() {
				public JobResult mapRow(ResultSet rs, int i) throws SQLException {
					JobResult jobresult = new JobResult();
					jobresult.setPackageId(Integer.parseInt(packageId));
					/* jobresult.setUserId(userId); */
					jobresult.setBatchId(rs.getString("BATCH_ID"));
					jobresult.setJobName(rs.getString("JOB_NAME"));
					jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
					jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
					jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
					jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
					jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
					jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));

					jobresult.setErrorRowsCount(
							rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");

					return jobresult;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getViewResults()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getViewResults()", e);
			
		}

		return resultList;
	}

	@Override
	public List<JobResult> getJobResultsByDate(String packageId, String userId, String clientStagingSchema,
			String fromDate, String toDate, JdbcTemplate clientJdbcTemplate) {
		List<JobResult> resultList = null;
		try {
			String batchIdParam = userId + "\\_" + packageId + "\\_%";
			String sql = sqlHelper.getSql("getJobResultsByDate");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam, fromDate, toDate },
					new RowMapper<JobResult>() {
						public JobResult mapRow(ResultSet rs, int i) throws SQLException {
							JobResult jobresult = new JobResult();
							jobresult.setPackageId(Integer.parseInt(packageId));
							/* jobresult.setUserId(userId); */
							jobresult.setBatchId(rs.getString("BATCH_ID"));
							jobresult.setJobName(rs.getString("JOB_NAME"));
							jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
							jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
							jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
							jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
							jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
							jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
							jobresult.setErrorRowsCount(
									rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");

							return jobresult;
						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getViewResults()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getViewResults()", e);
			
		}

		return resultList;
	}

	@Override
	public boolean deleteILConnection(int connectionId, String clientId, JdbcTemplate clientJdbcTemplate) {
		boolean status = false;
		int count = 0;
		try {
			String sql = sqlHelper.getSql("deleteILConnection");
			count = clientJdbcTemplate.update(sql, new Object[] { connectionId, clientId });
			status = count > 0;
		} catch (DataAccessException ae) {
			LOG.error("error while deleteILConnection()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while deleteILConnection()", e);
			
		}
		return status;
	}

	@Override
	public void deleteILConnectionMapping(int connectionId, JdbcTemplate clientJdbcTemplate) {

		try {
			String sql = sqlHelper.getSql("deleteIlConnectionMapping");
			clientJdbcTemplate.update(sql, new Object[] { connectionId });

		} catch (DataAccessException ae) {
			LOG.error("error while deleteILConnectionMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while deleteILConnectionMapping()", e);
			
		}

	}

	@Override
	public List<JobResult> getJobErrorLogs(String batchId, String clientStagingSchema,
			JdbcTemplate clientJdbcTemplate) {
		List<JobResult> resultList = null;
		try {

			JdbcTemplate jdbcTemplate = null;

			if (clientJdbcTemplate != null) {
				jdbcTemplate = clientJdbcTemplate;
			} else {
				jdbcTemplate = getJdbcTemplate();
			}

			String sql = sqlHelper.getSql("getJobErrorLogs");
			resultList = jdbcTemplate.query(sql, new Object[] { batchId }, new RowMapper<JobResult>() {
				public JobResult mapRow(ResultSet rs, int i) throws SQLException {
					JobResult jobresult = new JobResult();
					jobresult.setErrorId(rs.getInt("ERROR_ID"));
					jobresult.setBatchId(rs.getString("BATCH_ID"));
					jobresult.setErrorCode(rs.getString("ERROR_CODE"));
					jobresult.setErrorType(rs.getString("ERROR_TYPE"));
					jobresult.setErrorMessage(rs.getString("ERROR_MSG"));
					jobresult.setErrorSyntax(rs.getString("ERROR_SYNTAX"));
					jobresult.setDataSetValue(rs.getString("DATA_VALUE_SET"));
					return jobresult;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getJobErrorLogs()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getJobErrorLogs()", e);
			
		}

		return resultList;
	}

	@Override
	public int updateIlSource(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbcTemplate) {


		int count = 0;
		try {
			String sql = sqlHelper.getSql("updateIlSource");
			count = clientJdbcTemplate.update(sql,
					new Object[] { iLConnectionMapping.getTypeOfCommand(), iLConnectionMapping.getiLquery(),
							iLConnectionMapping.getMaxDateQuery(), iLConnectionMapping.getDatabaseName(),
							iLConnectionMapping.getModification().getCreatedBy(),
							iLConnectionMapping.getModification().getCreatedTime(),
							iLConnectionMapping.getIsIncrementalUpdate(),
							iLConnectionMapping.getConnectionMappingId() });

		} catch (DataAccessException ae) {
			LOG.error("error while deleteILConnection()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while deleteILConnection()", e);
			
		}
		return count;

	}

	@Override
	public boolean deleteCustomTablesBYPackageId(Package userpackage, String userId, JdbcTemplate clientJdbcTemplate,JdbcTemplate clientAppJdbcTemplate) {
		boolean resultList = false;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		List<TableDerivative> derivedTables = userpackage.getDerivedTables();
		Table targetTable = userpackage.getTable();

		try {
			String queryForDeleteCustomPackageDerivedTableInfo = sqlHelper.getSql("deleteCustomPackageDerivedTableInfo");
			String queryForDeleteCustomPackageTargetTableInfo = sqlHelper.getSql("deleteCustomPackageTargetTableInfo");

			if (derivedTables != null && derivedTables.size() > 0) {
				String[] sqlDropDerivedTables = new String[derivedTables.size()];
				for (int i = 0; i < derivedTables.size(); i++) {
					sqlDropDerivedTables[i] = "drop table if exists `" + derivedTables.get(i).getTableName() + "`";
				}
				clientJdbcTemplate.batchUpdate(sqlDropDerivedTables);
				clientAppJdbcTemplate.batchUpdate(queryForDeleteCustomPackageDerivedTableInfo,
						new BatchPreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {
								TableDerivative tableDerivative = derivedTables.get(i);
								ps.setInt(1, tableDerivative.getTableId());
								ps.setInt(2, tableDerivative.getTargetTableId());
								ps.setInt(3, userpackage.getPackageId());
							}

							@Override
							public int getBatchSize() {
								return derivedTables.size();
							}
						});

			}

			if (targetTable != null && targetTable.getTableName() != null) {
				clientJdbcTemplate.update("drop table if exists `" + targetTable.getTableName() + "`");
				if (!userpackage.getFilesHavingSameColumns()) {
					clientAppJdbcTemplate.update(sqlHelper.getSql("deleteCustomTempTablesQuery"), userId,userpackage.getPackageId());
				}
				clientAppJdbcTemplate.update(queryForDeleteCustomPackageTargetTableInfo, targetTable.getTableId(), userpackage.getPackageId());
				clientAppJdbcTemplate.update(sqlHelper.getSql("updatePackageMappingStatus"), new Boolean(false),
						userpackage.getModification().getModifiedBy(), CommonDateHelper.formatDateAsString(new Date()),
						userpackage.getPackageId());
				clientAppJdbcTemplate.update(sqlHelper.getSql("updatePackageScheduleStatus"), new Boolean(false),
						Constants.Status.STATUS_PENDING, userpackage.getModification().getModifiedBy(),
						CommonDateHelper.formatDateAsString(new Date()), userpackage.getPackageId());
			}
			resultList = true;
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			LOG.error("in deleteCustomTablesBYPackageId()",e);
		}
		return resultList;
	}

	@Override
	public boolean deleteCustomTablesBYTableId(TableDerivative tableDerivative, int packageId,
			JdbcTemplate clientJdbcTemplate,JdbcTemplate clientAppJdbcTemplate) {  
		boolean resultList = false;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String queryForDeleteCustomPackageDerivedTableInfo = sqlHelper
					.getSql("deleteCustomPackageDerivedTableInfo");

			clientJdbcTemplate.update("drop table if exists `" + tableDerivative.getTableName() + "`");
			clientAppJdbcTemplate.update(queryForDeleteCustomPackageDerivedTableInfo, tableDerivative.getTableId(),
					tableDerivative.getTargetTableId(), packageId);
			resultList = true;
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			LOG.error("in deleteCustomTablesBYTableId()",e);
		}
		return resultList;
	}

	@Override
	public List<DLInfo> getClientDLs(String userId, JdbcTemplate clientJdbcTemplate) {
		List<DLInfo> dlList = null;
		try {
			String sql = sqlHelper.getSql("getClientDLs");
			String getKpis = sqlHelper.getSql("getKpiList");

			dlList = clientJdbcTemplate.query(sql, new Object[] { userId, userId }, new RowMapper<DLInfo>() {
				public DLInfo mapRow(ResultSet rs, int i) throws SQLException {
					DLInfo dLInfo = new DLInfo();
					dLInfo.setdL_id(rs.getInt("DL_id"));
					dLInfo.setdL_name(rs.getString("DL_name"));
					dLInfo.setdL_table_name(rs.getString("dl_table_name"));
					dLInfo.setDescription(rs.getString("description"));
					dLInfo.setIndustry(new Industry(rs.getInt("industry_id")));
					dLInfo.setIsStandard(rs.getBoolean("isStandard"));
					dLInfo.setJobName(rs.getString("Job_name"));
					dLInfo.setIsLocked(rs.getBoolean("isLocked"));
					return dLInfo;
				}

			});

			for (DLInfo dlInfo : dlList) {
				List<String> kpiList = clientJdbcTemplate.query(getKpis, new Object[] { dlInfo.getdL_id() },
						new RowMapper<String>() {
							public String mapRow(ResultSet rs, int i) throws SQLException {
								String kpi = rs.getString("kpi_name");
								return kpi;
							}
						});
				dlInfo.setKpi(kpiList);
			}

		} catch (DataAccessException ae) {
			LOG.error("error while getClientDLs", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientDLs", e);
			
		}

		return dlList;
	}

	@Override
	public void saveTargetTableAliasNames(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		try {
			List<Column> seletedColumns = clientData.getUserPackage().getTable().getColumns();
			List<String> originalColumns = clientData.getUserPackage().getTable().getOriginalColumnNames();

			List<String> aliasColumn = new ArrayList<String>();
			for (Column seletedColumn : seletedColumns) {
				aliasColumn.add(seletedColumn.getColumnName());
			}
			List<String> originalColumnsList = new ArrayList<String>(originalColumns);
			originalColumnsList.removeAll(aliasColumn);

			List<String> aliasColsList = new ArrayList<String>(aliasColumn);
			aliasColsList.removeAll(originalColumns);

			String saveIlsSql = sqlHelper.getSql("saveTargetTableAliasNames");
			clientJdbcTemplate.batchUpdate(saveIlsSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, clientData.getUserPackage().getTable().getTableId());
					ps.setString(2, originalColumnsList.get(i));
					ps.setString(3, aliasColsList.get(i));
					ps.setString(4, clientData.getModification().getCreatedBy());
					ps.setString(5, clientData.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return originalColumnsList.size();
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while saveTargetTableAliasNames", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveTargetTableAliasNames", e);
			
		}

	}

	@Override
	public List<Map<String, Object>> targetTableAliasColumns(int tableId, JdbcTemplate clientJdbcTemplate) {
		List<Map<String, Object>> targetTableAliasColumns = null;
		try {
			String targetTableAliasColumnsSql = sqlHelper.getSql("targetTableAliasColumns");
			targetTableAliasColumns = clientJdbcTemplate.queryForList(targetTableAliasColumnsSql,
					new Object[] { tableId });
		} catch (DataAccessException ae) {
			LOG.error("error while targetTableAliasColumns", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while targetTableAliasColumns", e);
			
		}
		return targetTableAliasColumns;

	}

	/**
	 * (Javadoc)
	 * 
	 * @param userId
	 * @param packageId
	 * @return List&lt;ILConnectionMapping&gt;
	 */
 
	@Override
	public List<ILConnectionMapping> getIlConnectionMappingInfoByPackageId(String userId, Integer packageId,
			JdbcTemplate clientJdbcTemplate) {
		List<ILConnectionMapping> mappingList1 = null;
		List<ILConnectionMapping> mappingList2 = null;
		List<ILConnectionMapping> finalMappingList = new ArrayList<ILConnectionMapping>() ;
		try {
			String sql = sqlHelper.getSql("getIlConnectionMappingInfoByPackageId");
			String sql1 = sqlHelper.getSql("getIlConnectionMappingInfoByPackageIdWhenDb");
			mappingList1 = clientJdbcTemplate.query(sql, new Object[] { userId, packageId },
					new RowMapper<ILConnectionMapping>() {
						public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {
							ILConnectionMapping dilcConnectionMapping = new ILConnectionMapping();
							dilcConnectionMapping.setConnectionMappingId(rs.getInt("id"));
							dilcConnectionMapping.setClientId(rs.getString("userid"));
							dilcConnectionMapping.setProcedureParameters(rs.getString("Package_id"));
							dilcConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
							dilcConnectionMapping.setIsDLMapped(rs.getBoolean("isDLMapped"));
							dilcConnectionMapping.setIsMapped(rs.getBoolean("isMapped"));
							dilcConnectionMapping.setFileType(rs.getString("file_type"));
							dilcConnectionMapping.setDelimeter(rs.getString("delimeter"));
							dilcConnectionMapping.setFilePath(rs.getString("file_path"));
							dilcConnectionMapping.setSourceFileInfoId(rs.getLong("id"));
							dilcConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
							dilcConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
							ILConnection ilConnection = new ILConnection();
							ilConnection.setConnectionId(rs.getInt("connection_id"));
							dilcConnectionMapping.setiLConnection(ilConnection);
							dilcConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
							dilcConnectionMapping.setiLquery(rs.getString("IL_query"));
							dilcConnectionMapping.setDatabaseName(rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
							dilcConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));
							Database database = new Database();
							database.setId(rs.getInt("Database_id"));
							Modification modification = new Modification();
							modification.setCreatedBy(rs.getString("created_by"));
							modification.setCreatedTime(CommonDateHelper.formatDateAsString(new Date()));
							dilcConnectionMapping.setModification(modification);
							dilcConnectionMapping.setEncryptionRequired(rs.getBoolean("encryption_required"));
							return dilcConnectionMapping;
						}
					});
			mappingList2 = clientJdbcTemplate.query(sql1, new Object[] { userId, packageId },
					new RowMapper<ILConnectionMapping>() {
						public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {
							ILConnectionMapping dilcConnectionMapping = new ILConnectionMapping();
							dilcConnectionMapping.setConnectionMappingId(rs.getInt("id"));
							dilcConnectionMapping.setClientId(rs.getString("userid"));
							dilcConnectionMapping.setProcedureParameters(rs.getString("Package_id"));
							dilcConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
							dilcConnectionMapping.setIsDLMapped(rs.getBoolean("isDLMapped"));
							dilcConnectionMapping.setIsMapped(rs.getBoolean("isMapped"));
							dilcConnectionMapping.setFileType(rs.getString("fileType"));
							dilcConnectionMapping.setDelimeter(rs.getString("delimeter"));
							dilcConnectionMapping.setFilePath(rs.getString("filePath"));
							dilcConnectionMapping.setSourceFileInfoId(rs.getLong("source_file_info_id"));
							dilcConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
							dilcConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
							ILConnection ilConnection = new ILConnection();
							ilConnection.setConnectionId(rs.getInt("connection_id"));
							dilcConnectionMapping.setiLConnection(ilConnection);
							dilcConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
							dilcConnectionMapping.setiLquery(rs.getString("IL_query"));
							dilcConnectionMapping.setDatabaseName(rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
							dilcConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));
							Database database = new Database();
							database.setId(rs.getInt("Database_id"));
							Modification modification = new Modification();
							modification.setCreatedBy(rs.getString("created_by"));
							modification.setCreatedTime(CommonDateHelper.formatDateAsString(new Date()));
							dilcConnectionMapping.setModification(modification);
							return dilcConnectionMapping;
						}
					});
			if(mappingList1 != null && mappingList1.size() > 0){
				finalMappingList.addAll(mappingList1);
			}
			if(mappingList2 != null && mappingList2.size() > 0){
				finalMappingList.addAll(mappingList2);
			}
			
		} catch (DataAccessException ae) {
			LOG.error("error while getClientDLs", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientDLs", e);
			
		}
		return finalMappingList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<String> getTargetTables(String clientSchemaName, JdbcTemplate clientJdbcTemplate) {

		List<String> targetTableList = null;

		try {

			targetTableList = (List<String>) clientJdbcTemplate.execute(new StatementCallback() {
				@Override
				public List<String> doInStatement(Statement stmt) throws SQLException, DataAccessException {
					java.sql.DatabaseMetaData metaData = stmt.getConnection().getMetaData();
					String[] types = { "TABLE" };
					ResultSet rs = metaData.getTables(null, null, "%", types);
					List<String> tableNameList = new ArrayList<String>();
					while (rs.next()) {
						tableNameList.add(rs.getString("TABLE_NAME"));
					}
					return tableNameList;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while targetTableAliasColumns", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOG.error("error while targetTableAliasColumns", e);
			
		}
		return targetTableList;

	}

	public List<String> derivedTableMappingInfo(int packageId, String userId, JdbcTemplate clientJdbcTemplate) {

		List<String> targetTableList = null;

		try {
			String derivedTableSql = sqlHelper.getSql("derivedTableMappingInfo");
			targetTableList = clientJdbcTemplate.query(derivedTableSql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs != null) {
						return rs.getString(1);
					} else {
						return null;
					}
				}
			}, userId, packageId);

		} catch (DataAccessException ae) {
			LOG.error("error while derivedTableMappingInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOG.error("error while derivedTableMappingInfo", e);
			
		}
		return targetTableList;

	}

	@Override
	public int updatePackageIsClientDbTablesStatus(String clientId, int packageId, boolean isClientDbTables,
			JdbcTemplate clientJdbcTemplate) {
		int i = -1;
		try {
			String sql = sqlHelper.getSql("updatePackageIsClientDbTablesStatus");

			i = clientJdbcTemplate.update(sql, new Object[] { isClientDbTables, packageId });

		} catch (DataAccessException ae) {
			LOG.error("error while insertFileColumnDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getFileInfoByPackage()", e);
			
		}
		return i;
	}

	@Override
	public void saveDerivedTableMappingInfo(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbcTemplate) {


		try {
			String sql = sqlHelper.getSql("saveDerivedTableMapping");
			String parentTable = iLConnectionMapping.getParent_table_name();
			String[] parentTables = parentTable.substring(0, parentTable.length() - 1).split(",");
			List<String> parentTablesList = Arrays.asList(parentTables);
			clientJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Integer connectionId = null;
					if (iLConnectionMapping.getiLConnection() != null) {
						connectionId = iLConnectionMapping.getiLConnection().getConnectionId();
					} else {
						connectionId = 0;
					}
					ps.setBoolean(1, iLConnectionMapping.getIsMapped());
					ps.setBoolean(2, iLConnectionMapping.getIsFlatFile());
					ps.setString(3, iLConnectionMapping.getFileType());
					ps.setString(4, iLConnectionMapping.getFilePath());
					ps.setString(5, iLConnectionMapping.getDelimeter());
					ps.setBoolean(6, iLConnectionMapping.getIsFirstRowHasColoumnNames());
					ps.setInt(7, connectionId);
					ps.setString(8, iLConnectionMapping.getTypeOfCommand());
					ps.setString(9, iLConnectionMapping.getiLquery());
					ps.setString(10, iLConnectionMapping.getProcedureParameters());
					ps.setInt(11, iLConnectionMapping.getPackageId());
					ps.setString(12, null);
					ps.setString(13, iLConnectionMapping.getClientId());
					ps.setString(14, iLConnectionMapping.getModification().getCreatedBy());
					ps.setString(15, iLConnectionMapping.getModification().getCreatedTime());
					ps.setBoolean(16, iLConnectionMapping.getIsHavingParentTable());
					ps.setString(17, parentTablesList.get(i));
				}

				@Override
				public int getBatchSize() {

					return parentTablesList.size();
				}

			});
		} catch (DataAccessException ae) {
			LOG.error("error while saveILConnectionMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveILConnectionMapping()", e);
			
		}

	}

	@Override
	public List<Package> getAllUserPackages(String clientId, JdbcTemplate clientJdbcTemplate) {

		List<Package> packageList = null;
		try {
			String sql = sqlHelper.getSql("getAllUserPackages");
			packageList = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<Package>() {
				public Package mapRow(ResultSet rs, int i) throws SQLException {
					Package userPackage = new Package();
					userPackage.setPackageId(rs.getInt("package_id"));
					userPackage.setPackageName(rs.getString("package_name"));
					userPackage.setDescription(rs.getString("description"));
					userPackage.setIndustry(new Industry(rs.getInt("industryId"), rs.getString("industryName")));
					userPackage.setIsStandard(rs.getBoolean("isStandard"));
					userPackage.setIsActive(rs.getBoolean("isActive"));
					userPackage.setIsScheduled(rs.getBoolean("isScheduled"));
					userPackage.setIsAdvanced(rs.getBoolean("isAdvanced"));
					userPackage.setIsMapped(rs.getBoolean("isMapped"));
					userPackage.setScheduleStatus(rs.getString("schedule_status"));
					userPackage.setIsClientDbTables(rs.getBoolean("isClientDbTables"));
					userPackage.setTrailingMonths(rs.getInt("trailing_months"));
					userPackage.setFilesHavingSameColumns(rs.getBoolean("files_having_same_columns"));
					Modification modification = new Modification();
					modification.setCreatedTime(rs.getString("created_time"));
					userPackage.setModification(modification);

					if (StringUtils.isNotBlank(rs.getString("target_table_name"))) {
						Table table = new Table();
						table.setTableId(rs.getInt("id"));
						table.setSchemaName(rs.getString("schemaName"));
						table.setTableName(rs.getString("target_table_name"));
						table.setIsProcessed(rs.getBoolean("isProcessed"));
						table.setIsDirect(rs.getBoolean("direct_or_fetch_from_file"));
						table.setNoOfRecordsProcessed(rs.getInt("no_of_records_processed"));
						table.setNoOfRecordsFailed(rs.getInt("no_of_records_failed"));
						table.setDuplicateRecords(rs.getInt("duplicate_records"));
						table.setTotalRecords(rs.getInt("total_records"));
						userPackage.setTable(table);
					}

					return userPackage;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getUserPackages()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getUserPackages()", e);
			
		}

		return packageList;

	}

	@Override
	public List<Package> getUserPackages(String clientId, boolean isStandardPackages, JdbcTemplate clientJdbcTemplate) {

		List<Package> packageList = null;
		try {
			String sql = sqlHelper.getSql("getPackages"	);
			packageList = clientJdbcTemplate.query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, clientId);
					preparedStatement.setBoolean(2, isStandardPackages);
				}
			}, new RowMapper<Package>() {
				public Package mapRow(ResultSet rs, int i) throws SQLException {
					Package userPackage = new Package();
					userPackage.setPackageId(rs.getInt("package_id"));
					userPackage.setPackageName(rs.getString("package_name"));
					userPackage.setDescription(rs.getString("description"));
					userPackage.setIndustry(new Industry(rs.getInt("industryId"), rs.getString("industryName")));
					userPackage.setIsStandard(rs.getBoolean("isStandard"));
					userPackage.setIsActive(rs.getBoolean("isActive"));
					userPackage.setIsScheduled(rs.getBoolean("isScheduled"));
					userPackage.setIsAdvanced(rs.getBoolean("isAdvanced"));
					userPackage.setIsMapped(rs.getBoolean("isMapped"));
					userPackage.setScheduleStatus(rs.getString("schedule_status"));
					userPackage.setIsClientDbTables(rs.getBoolean("isClientDbTables"));
					userPackage.setTrailingMonths(rs.getInt("trailing_months"));
					userPackage.setFilesHavingSameColumns(rs.getBoolean("files_having_same_columns"));
					Modification modification = new Modification();
					modification.setCreatedTime(rs.getString("created_time"));
					userPackage.setModification(modification);

					Table table = new Table();
					table.setTableId(rs.getInt("id"));
					table.setSchemaName(rs.getString("schemaName"));
					table.setTableName(rs.getString("target_table_name"));
					table.setIsProcessed(rs.getBoolean("isProcessed"));
					table.setIsDirect(rs.getBoolean("direct_or_fetch_from_file"));
					table.setNoOfRecordsProcessed(rs.getInt("no_of_records_processed"));
					table.setNoOfRecordsFailed(rs.getInt("no_of_records_failed"));
					table.setDuplicateRecords(rs.getInt("duplicate_records"));
					table.setTotalRecords(rs.getInt("total_records"));
					userPackage.setTable(table);

					return userPackage;
				}

			});

		} catch (SqlNotFoundException | DataAccessException ae) {
			LOG.error("error while getUserPackages()", ae);
			throw new AnvizentRuntimeException(ae);
		}

		return packageList;

	}

	@Override
	public ClientData getClientSourceDetails(String clientId, JdbcTemplate clientJdbcTemplate) {
		ClientData clientData = null;
		;
		try {
			String sql = sqlHelper.getSql("getClientSourceDetails");

			clientData = clientJdbcTemplate.query(sql, new Object[] { clientId }, new ResultSetExtractor<ClientData>() {
				@Override
				public ClientData extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						ClientData clientData = new ClientData();
						clientData.setIs_flat_file_locked(rs.getBoolean("is_flat_file_locked"));
						clientData.setIs_database_locked(rs.getBoolean("is_database_locked"));
						return clientData;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getClientSourceDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientSourceDetails()", e);
			
		}
		return clientData;
	}

	@Override
	public long getMaxFileSize(boolean isTrailUser, JdbcTemplate clientJdbcTemplate) {

		long maxFileSize = 0;
		try {
			String getMaxFileSize = null;
			if (!isTrailUser) {
				getMaxFileSize = sqlHelper.getSql("getMaxFileSize");
			} else {
				getMaxFileSize = sqlHelper.getSql("getMaxFileSizeForTrialUser");
			}

			maxFileSize = clientJdbcTemplate.queryForObject(getMaxFileSize, Long.class);

		} catch (DataAccessException ae) {
			LOG.error("error while getMaxFileSize", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getMaxFileSize", e);
			
		}

		return maxFileSize;

	}

	@Override
	public int getConnectorId(int database_type_id, JdbcTemplate clientJdbcTemplate) {

		int id = -1;
		try {

			String sql = sqlHelper.getSql("getConnectorId");
			id = clientJdbcTemplate.queryForObject(sql, new Object[] { database_type_id }, Integer.class);

		} catch (DataAccessException ae) {
			LOG.error("error while getConnectorId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getConnectorId", e);
			
		}

		return id;

	}

	@Override
	public String getTargetTableQuery(String clientId, int packageId, JdbcTemplate clientJdbcTemplate) {

		String targetTableQuery = null;
		try {
			String sql = sqlHelper.getSql("getTargetTableQuery");
			targetTableQuery = clientJdbcTemplate.query(sql, new Object[] { clientId, packageId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String query = rs.getString("temp_query");
								return query;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getTargetTableQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getTargetTableQuery()", e);
			
		}

		return targetTableQuery;

	}
	
	@Override
	public boolean updateTargetTableQuery(String targetTableQuery, String clientId, int packageId, JdbcTemplate clientJdbcTemplate) {
		boolean status = false;
		try {
			String sql = sqlHelper.getSql("updateTargetTableQuery");
			int updatedCount = clientJdbcTemplate.update(sql, new Object[] { targetTableQuery, clientId, packageId });
			if (updatedCount > 0) {
				status = true;
			}
		} catch (DataAccessException ae) {
			LOG.error("error while updateTargetTableQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateTargetTableQuery()", e);
		}
		
		return status;
		
	}

	@Override
	public String getILDefaultIncrementalQuery(int ilId, int databaseTypeId, JdbcTemplate clientJdbcTemplate) {

		String query = null;
		;
		try {
			String sql = sqlHelper.getSql("getILDefaultIncrementalQuery");

			query = clientJdbcTemplate.query(sql, new Object[] { ilId, databaseTypeId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String defaultquery = rs.getString("il_incremental_update_query");
								return defaultquery;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getILDefaultIncrementalQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILDefaultIncrementalQuery()", e);
			
		}
		return query;

	}

	@Override
	public String getILIncrementalDate(String dataSourceId, String ILTableName, JdbcTemplate clientJdbcTemplate) {

		String date = null;
		try {
			String query = sqlHelper.getSql("getILIncrementalDate");
			date = clientJdbcTemplate.query(query, new Object[] { ILTableName, ILTableName },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String dateInc = rs.getString(1);
								if (StringUtils.isNotBlank(dateInc)) {
									dateInc = StringUtils.substringBefore(dateInc, ".");
								}
								return dateInc;
							} else {
								return null;
							}

						}

					});
		} catch (Exception e) {
			LOG.error("error while getting incremental date ", e);
			throw new AnvizentRuntimeException(e);
		}

		return date;
	}

	@Override
	public ILConnectionMapping getDatesForHistoricalLoad(int connectionMappingId, JdbcTemplate clientJdbcTemplate) {
		ILConnectionMapping iLConnectionMapping = null;

		try {
			String sql = sqlHelper.getSql("getDatesForHistoricalLoad");
			iLConnectionMapping = clientJdbcTemplate.query(sql, new ResultSetExtractor<ILConnectionMapping>() {

				@Override
				public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {
					ILConnectionMapping iLConnectionMapping = new ILConnectionMapping();
					if (rs == null) {
						return null;
					}

					while (rs.next()) {
						iLConnectionMapping.setIsHistoricalLoad(rs.getBoolean("is_historical_load"));
						iLConnectionMapping.setHistoricalFromDate(rs.getString("historical_from_date"));
						iLConnectionMapping.setHistoricalToDate(rs.getString("historical_to_date"));
						iLConnectionMapping.setLoadInterval(rs.getInt("historical_load_interval"));
						iLConnectionMapping.setHistoricalLastUpdatedTime(rs.getString("historical_last_updated_time"));
					}
					return iLConnectionMapping;
				}

			}, connectionMappingId);

		} catch (DataAccessException ae) {
			LOG.error("error while getDatesForHistoricalLoad()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDatesForHistoricalLoad", e);
			
		}
		return iLConnectionMapping;
	}

	@Override
	public HistoricalLoadForm getDatesForHistoricalLoading(int loadId, JdbcTemplate clientJdbcTemplate) {
		HistoricalLoadForm historicalLoad = null;

		try {
			String sql = sqlHelper.getSql("getDatesForHistoricalLoading");
			historicalLoad = clientJdbcTemplate.query(sql, new ResultSetExtractor<HistoricalLoadForm>() {

				@Override
				public HistoricalLoadForm extractData(ResultSet rs) throws SQLException, DataAccessException {
					HistoricalLoadForm historicalLoad = new HistoricalLoadForm();
					if (rs == null) {
						return null;
					}

					while (rs.next()) {
						historicalLoad.setHistoricalFromDate(rs.getString("from_date"));
						historicalLoad.setHistoricalToDate(rs.getString("to_date"));
						historicalLoad.setLoadInterval(rs.getInt("historical_interval"));
						historicalLoad.setHistoricalLastUpdatedTime(rs.getString("histrorical_updated_date"));
					}
					return historicalLoad;
				}

			}, loadId);

		} catch (DataAccessException ae) {
			LOG.error("error while getDatesForHistoricalLoading()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDatesForHistoricalLoad", e);
			
		}
		return historicalLoad;
	}

	// a helper table were I note the schemas that I'm not interested in

	@Override
	public List<DLInfo> getAnalyticalDLs(int dlId, JdbcTemplate clientJdbcTemplate) {
		List<DLInfo> onlyDlList = null;
		try {
			String sql = sqlHelper.getSql("getAnalyticalDLs");

			onlyDlList = clientJdbcTemplate.query(sql, new Object[] { dlId }, new RowMapper<DLInfo>() {
				public DLInfo mapRow(ResultSet rs, int i) throws SQLException {
					DLInfo dLInfo = new DLInfo();
					dLInfo.setAnalytical_dl_id(rs.getInt("Analytical_DL_ID"));
					dLInfo.setAnalytical_DL_Job_Name(rs.getString("Job_Name"));
					dLInfo.setAnalytical_DL_table_name(rs.getString("Analytical_DL_Table_Name"));
					return dLInfo;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getAnalyticalDLs", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getAnalyticalDLs", e);
			
		}

		return onlyDlList;
	}

	@Override
	public boolean purgeUserTables(String clientSchema, List<String> purgeScript, JdbcTemplate clientJdbcTemplate) {
		boolean result = false;

		try {
			LOG.info("purge tables list : " + purgeScript);
			clientJdbcTemplate.update("SET foreign_key_checks = 0");
			for (String purgeSql : purgeScript) {
				try {
					if (StringUtils.isBlank(purgeSql.trim())) {
						continue;
					}
					clientJdbcTemplate.update(purgeSql);
				} catch (Exception e) {
					LOG.error(""+e.getMessage(),e);
				}
			}
			result = true;
		} catch (Exception e) {
			LOG.error("error while purgeUserTables()", e);
		} finally {
			clientJdbcTemplate.update("SET foreign_key_checks = 1");
		}
		return result;
	}

	@Override
	public int activateUserPackage(int packageId, int userId, JdbcTemplate clientJdbcTemplate) {
		int status = 0;
		try {
			String sql = sqlHelper.getSql("activateUserPackage");
			status = clientJdbcTemplate.update(sql, new Object[] { userId, packageId });
			
			String sqlupdate = sqlHelper.getSql("updateIsScheduledToActive");
			clientJdbcTemplate.update(sqlupdate, new Object[] { packageId });

		} catch (DataAccessException ae) {
			LOG.error("error while activateUserPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while activateUserPackage()", e);
			
		}

		return status;
	}
	
	@Override
	public Message deleteUserPackage(int userId, int packageId, JdbcTemplate clientJdbcTemplate) {
		int status = 0;
		Message message = new Message();
		message.setCode("ERROR");
		try {
			String sql = sqlHelper.getSql("deleteUserPackage");
			status = clientJdbcTemplate.update(sql, new Object[] { userId, packageId });
			if (status > 0)
				message.setCode("SUCCESS");
		} catch (DataAccessException ae) {
			LOG.error("error while deleteUserPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while deleteUserPackage()", e);
			
		}

		return message;
	}

	@Override
	public Message renameUserPackage(int userId, int packageId, String packageName, JdbcTemplate clientJdbcTemplate) {
		int status = 0;
		Message message = new Message();
		message.setCode("ERROR");
		try {
			String sql = sqlHelper.getSql("renameUserPackage");
			status = clientJdbcTemplate.update(sql, new Object[] { packageName, userId, packageId });
			if (status > 0)
				message.setCode("SUCCESS");

		} catch (DataAccessException ae) {
			LOG.error("error while renameUserPackage()", ae);
			message.setText(ae.getMessage());
		} catch (SqlNotFoundException e) {
			LOG.error("error while renameUserPackage()", e);
			message.setText(e.getMessage());
			
		}

		return message;
	}

	@Override
	public void updateDatabaseConnection(ILConnection iLConnection, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("updateDatabaseConnection");
			clientJdbcTemplate.update(sql,
					new Object[] { iLConnection.getConnectionName(), iLConnection.getConnectionType(),
							iLConnection.getServer(), iLConnection.getUsername(), iLConnection.getPassword(),
							iLConnection.getDateFormat(), iLConnection.getTimeZone(), iLConnection.isAvailableInCloud(),
							iLConnection.getModification().getCreatedBy(),
							iLConnection.getModification().getCreatedTime(), iLConnection.getDataSourceName(),
							iLConnection.isSslEnable(), 
							iLConnection.getDbVariables(),
							iLConnection.getConnectionId(), iLConnection.getClientId() });
			LOG.info("Connection " + iLConnection.getConnectionName() + "updated Succesfully for client : "
					+ iLConnection.getClientId() + "");
		} catch (DuplicateKeyException ae) {
			LOG.error("error while updateDatabaseConnection()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOG.error("error while updateDatabaseConnection()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateDatabaseConnection()", e);
			
		}

	}

	@Override
	public List<Table> getClientILsandDLs(String clientId, JdbcTemplate clientJdbcTemplate) {
		List<Table> tableList = null;
		try {
			String sql = sqlHelper.getSql("getClientILsandDLs");
			tableList = clientJdbcTemplate.query(sql, new Object[] {}, new RowMapper<Table>() {
				public Table mapRow(ResultSet rs, int i) throws SQLException {
					Table table = new Table();
					table.setTableName(rs.getString("tbl_name"));
					return table;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getClientILsandDLs()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientILsandDLs()", e);
			
		}
		return tableList;

	}

	@Override
	public List<Package> getPackagesByILConnectionId(int ilconnectionId, int userId, JdbcTemplate clientJdbcTemplate) {
		List<Package> packages = null;
		try {
			String sql = sqlHelper.getSql("getPackagesByILConnectionId");
			packages = clientJdbcTemplate.query(sql, new Object[] { ilconnectionId }, new RowMapper<Package>() {
				public Package mapRow(ResultSet rs, int i) throws SQLException {
					Package userPackage = new Package();
					userPackage.setPackageId(rs.getInt("Package_id"));
					userPackage.setIsStandard(rs.getBoolean("isStandard"));
					userPackage.setFilesHavingSameColumns(rs.getBoolean("files_having_same_columns"));
					Industry industry = new Industry();
					industry.setId(rs.getInt("industry_id"));
					userPackage.setIndustry(industry);
					return userPackage;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getClientILsandDLs()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientILsandDLs()", e);
			
		}
		return packages;

	}

	@Override
	public Integer getSourceCountByPackageId(int packageId, int userId, JdbcTemplate clientJdbcTemplate) {
		Integer sourceCount = 0;
		try {
			String sql = sqlHelper.getSql("getSourceCountByPackageId");

			sourceCount = clientJdbcTemplate.query(sql, new Object[] { packageId }, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						Integer count = rs.getInt("rowcount");
						return count;
					} else {
						return 0;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getSourceCountByPackageId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getSourceCountByPackageId()", e);
			
		}
		return sourceCount;
	}

	@Override
	public Map<Integer, String> getAllWebServices(String clientId, JdbcTemplate clientJdbcTemplate) {

		Map<Integer, String> contextParams = null;

		try {

			String sql = sqlHelper.getSql("getAllWebServices");

			contextParams = clientJdbcTemplate.query(sql, new Object[] { clientId },
					new ResultSetExtractor<Map<Integer, String>>() {

						@Override
						public Map<Integer, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs == null)
								return null;

							Map<Integer, String> contextParams = new LinkedHashMap<>();

							while (rs.next()) {
								contextParams.put(rs.getInt(1), rs.getString(2));
							}

							return contextParams;
						}
					});

		} catch (Exception e) {
			LOG.error("Error while reading getAllWebServices ", e);
		}

		return contextParams;
	}

	@Override
	public Map<Integer, String> getWebServicesByClientId(String clientId, JdbcTemplate clientJdbcTemplate) {

		Map<Integer, String> contextParams = null;

		try {

			String sql = sqlHelper.getSql("getWebServicesByClientId");

			contextParams = clientJdbcTemplate.query(sql, new Object[] { clientId },
					new ResultSetExtractor<Map<Integer, String>>() {

						@Override
						public Map<Integer, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs == null)
								return null;

							Map<Integer, String> contextParams = new LinkedHashMap<>();

							while (rs.next()) {
								contextParams.put(rs.getInt(1), rs.getString(2));
							}

							return contextParams;
						}
					});

		} catch (Exception e) {
			LOG.error("Error while reading getWebServicesByClientId ", e);
		}

		return contextParams;
	}

	@Override
	public int logError(ErrorLog errorLog, JdbcTemplate clientJdbcTemplate) {
		int updatedCount = -1;
		try {
			String sql = sqlHelper.getSql("logError");
			updatedCount = getRequiredJdbcTemplate(clientJdbcTemplate).update(sql,
					new Object[] { errorLog.getErrorCode(), errorLog.getErrorMessage(), errorLog.getErrorBody(),
							errorLog.getRequestedService(), errorLog.getReceivedParameters(), errorLog.getUserId(),
							errorLog.getClientDetails() });

		} catch (Exception e) {
			LOG.error("Unable to store the error in db log ",e);
			LOG.error(errorLog);
		}
		return updatedCount;
	}

	@Override
	public WebService getDefaultILWebServiceMapping(int webserviceId, int lId, JdbcTemplate clientJdbcTemplate) {
		WebService webService = null;
		;
		try {
			String sql = sqlHelper.getSql("getDefaultILWebServiceMapping");

			webService = clientJdbcTemplate.query(sql, new Object[] { webserviceId, lId },
					new ResultSetExtractor<WebService>() {
						@Override
						public WebService extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								WebService webService = new WebService();
								webService.setAuthenticationRequired(rs.getBoolean("is_Authentication_Required"));
								webService.setAuthentication_Method_Type(rs.getString("authentication_method_type"));
								webService.setAuthentication_Request_Params(
										rs.getString("authentication_request_params"));
								String authenticationParams = rs.getString("authentication_request_params");
								List<Map<String, String>> authRequsetParamsList = new ArrayList<>();
								if (StringUtils.isNotBlank(authenticationParams)) {
									if (authenticationParams.contains("||")) {
										String[] authParams = StringUtils.split(authenticationParams, "||");
										for (int i = 0; i < authParams.length; i++) {
											String[] params = authParams[i].split(":");
											String key = params[0].trim();
											String value = params[1].trim();
											Map<String, String> map = new HashMap<>();
											map.put(key, value);
											authRequsetParamsList.add(map);
										}
									} else {
										String[] params = authenticationParams.split(":");
										String key = params[0].trim();
										String value = params[1].trim();
										Map<String, String> map = new HashMap<>();
										map.put(key, value);
										authRequsetParamsList.add(map);
									}

								}
								webService.setAuthRequsetParams(authRequsetParamsList);
								webService.setAuthenticationUrl(rs.getString("authentication_url"));
								webService.setCookieRequired(rs.getBoolean("is_Cookie_Required"));
								webService.setTokenName(rs.getString("token_name"));
								webService.setResponseObjName(rs.getString("response_object_name"));
								webService.setUrl(rs.getString("default_web_service_url"));
								webService.setWeb_Service_Method_Type(rs.getString("web_service_method_type"));
								webService.setHeaders(rs.getString("header_details"));
								webService.setWeb_Service_Path_Params(rs.getString("web_service_path_params"));
								webService.setWeb_Service_Request_Body_Params(
										rs.getString("web_service_request_body_params"));
								webService.setRequestMethod(rs.getString("web_service_method_type"));
								webService.setApiName(rs.getString("api_name"));
								webService.setWeb_service_sub_url(rs.getString("default_web_service_sub_url"));
								webService.setWeb_service_sub_url_param(rs.getString("web_service_sub_url_param"));
								webService.setResponseColumnObjName(rs.getString("response_column_object_name"));
								webService.setUserTokenName(rs.getString("user_token_name"));
								webService.setWeb_service_id(rs.getString("web_service_id"));

								return webService;

							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getDefaultILWebServiceMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDefaultILWebServiceMapping()", e);
			
		}
		return webService;
	}

	@Override
	public Map<String, String> getDbSchemaVaribles(String clientId, int connectorId, JdbcTemplate clientJdbcTemplate) {

		Map<String, String> contextParams = null;

		try {

			String sql = sqlHelper.getSql("getDbSchemaVaribles");

			contextParams = clientJdbcTemplate.query(sql, new Object[] { connectorId },
					new ResultSetExtractor<Map<String, String>>() {

						@Override
						public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs == null)
								return null;

							Map<String, String> contextParams = new LinkedHashMap<>();

							while (rs.next()) {
								contextParams.put(rs.getString(1), rs.getString(2));
							}

							return contextParams;
						}
					});

		} catch (Exception e) {
			LOG.error("Error while reading getDbSchemaVaribles ", e);
		}

		return contextParams;

	}

	@Override
	public List<String> getDefaultInsertsForIL(List<Integer> ilIds, String clientSchemaName,
			JdbcTemplate clientJdbcTemplate) {

		List<String> insertScript = null;

		try {
			StringBuilder ilIdsQuery = new StringBuilder();
			if (ilIds != null && ilIds.size() > 0) {
				ilIds.forEach(tablId -> {
					ilIdsQuery.append(", ").append(tablId);
				});
				ilIdsQuery.deleteCharAt(0);
			} else {
				return null;
			}
			String sql = "select purge_script from minidwcm_il_info where  IL_id in (" + ilIdsQuery.toString()
					+ ") and purge_script is not null";

			insertScript = clientJdbcTemplate.query(sql, new ResultSetExtractor<List<String>>() {
				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<String> sqlList = new ArrayList<>();
					if (rs == null)
						return sqlList;

					while (rs.next()) {
						String purgeScript = rs.getString("purge_script");
						String[] purgeScripts = StringUtils.split(purgeScript, ";");
						for (String purgeSql : purgeScripts) {
							sqlList.add(StringUtils.replace(purgeSql, "{clientSchemaName}", clientSchemaName));
						}
					}
					return sqlList;
				}
			});

		} catch (Exception e) {
			LOG.error("Error while reading getDbSchemaVaribles ", e);
		}

		return insertScript;

	}

	@Override
	public void saveILMappingHeadersForWebService(String userId, int packageId, int dLId, int iLId,
			String mappedHeaders, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("saveILMappingHeadersForWebService");
			clientJdbcTemplate.update(sql, new Object[] { userId, packageId, dLId, iLId, mappedHeaders });

		} catch (DataAccessException ae) {
			LOG.error("error while saveILConnectionMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveILConnectionMapping()", e);
			
		}

	}

	@Override
	public String getILMappingHeadersForWebService(int packageId, int dLId, int iLId, JdbcTemplate clientJdbcTemplate) {
		String mappedHeaders = null;
		;
		try {
			String sql = sqlHelper.getSql("getILMappingHeadersForWebService");

			mappedHeaders = clientJdbcTemplate.query(sql, new Object[] { packageId, dLId, iLId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String headers = rs.getString("mapped_headers");
								// TODO get remaining fields if required
								return headers;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getILMappingHeadersForWebService()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILMappingHeadersForWebService()", e);
			
		}
		return mappedHeaders;
	}

	@Override
	public String getWebServiceById(int id, JdbcTemplate clientJdbcTemplate) {

		String webserviceName = null;
		;
		try {
			String sql = sqlHelper.getSql("getWebServiceById");

			webserviceName = clientJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						String serviceName = rs.getString("web_service_name");
						// TODO get remaining fields if required
						return serviceName;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getILMappingHeadersForWebService()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILMappingHeadersForWebService()", e);
			
		}
		return webserviceName;

	}

	@Override
	public List<ClientData> getDependentPackagesForScheduling(String clientId, List<String> derivedTablesList,
			JdbcTemplate clientJdbcTemplate) {
		// TODO Auto-generated method stub
		List<ClientData> clientData = new ArrayList<>();
		try {
			StringBuilder builder = new StringBuilder();
			derivedTablesList.forEach(value -> {
				builder.append("?,");
			});

			derivedTablesList.add(0, clientId);
			String sql = StringUtils.replace(sqlHelper.getSql("getDependentPackagesForScheduling"), "{tableNames}",
					builder.deleteCharAt(builder.length() - 1).toString());
			clientData = clientJdbcTemplate.query(sql, derivedTablesList.toArray(), new RowMapper<ClientData>() {
				public ClientData mapRow(ResultSet rs, int i) throws SQLException {
					ClientData clientData = new ClientData();

					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setConnectionMappingId(rs.getInt("connection_mapping_id"));
					ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
					ilConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
					ilConnectionMapping.setParent_table_name(rs.getString("parent_table_name"));

					Package userPackage = new Package();
					userPackage.setIndustry(new Industry(rs.getInt("industry_id")));

					Schedule schedule = new Schedule();
					schedule.setScheduleId(rs.getInt("schedule_id"));

					clientData.setUserPackage(userPackage);
					clientData.setIlConnectionMapping(ilConnectionMapping);
					clientData.setSchedule(schedule);
					return clientData;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getDependentPackagesForScheduling()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDependentPackagesForScheduling()", e);
			
		}

		return clientData;
	}

	@Override
	public int updateMappedHeadersForWebservice(String userId, ILConnectionMapping iLConnectionMapping,
			JdbcTemplate clientJdbcTemplate) {
		int count = -1;
		try {

			String sql = sqlHelper.getSql("updateMappedHeadersForWebservice");
			count = clientJdbcTemplate.update(sql,
					new Object[] { iLConnectionMapping.getWebserviceMappingHeaders(), iLConnectionMapping.getiLquery(),
							iLConnectionMapping.getModification().getModifiedBy(),
							iLConnectionMapping.getModification().getCreatedTime(), iLConnectionMapping.getPackageId(),
							iLConnectionMapping.getdLId(), iLConnectionMapping.getiLId(),
							iLConnectionMapping.getConnectionMappingId(),

					});

		} catch (DataAccessException ae) {
			LOG.error("error while updateMappedHeadersForWebservice()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateMappedHeadersForWebservice()", e);
			
		}
		return count;
	}

	@Override
	public String getMappedHeadersForWebservice(String userId, int iLId, int mappingId, int webserviceId,
			JdbcTemplate clientJdbcTemplate) {

		String mappedHeadersForWebservice = null;
		;
		try {
			String sql = sqlHelper.getSql("getMappedHeadersForWebservice");

			mappedHeadersForWebservice = clientJdbcTemplate.query(sql, new Object[] { iLId, mappingId, webserviceId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String mappedHeaders = rs.getString("webservice_mapping_headers");
								return mappedHeaders;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getILMappingHeadersForWebService()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILMappingHeadersForWebService()", e);
			
		}
		return mappedHeadersForWebservice;
	}

	@Override
	public String getDerivedTableQuery(String clientId, int packageId, String targetTableName, int targetTableId,
			int tableId, JdbcTemplate clientJdbcTemplate) {

		String derivedTableQuery = null;
		try {
			String sql = sqlHelper.getSql("getDerivedTableQuery");
			derivedTableQuery = clientJdbcTemplate.query(sql,
					new Object[] { clientId, packageId, targetTableName, targetTableId, tableId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String query = rs.getString("custom_target_table_query");
								return query;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getTargetTableQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getTargetTableQuery()", e);
			
		}

		return derivedTableQuery;

	}

	@Override
	public String getILHistoricalLoadQuery(int ilId, int databaseTypeId, JdbcTemplate clientJdbcTemplate) {

		String query = null;
		;
		try {
			String sql = sqlHelper.getSql("getILHistoricalLoadQuery");

			query = clientJdbcTemplate.query(sql, new Object[] { ilId, databaseTypeId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String defaultquery = rs.getString("historical_load_query");
								return defaultquery;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getILHistoricalLoadQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILHistoricalLoadQuery()", e);
			
		}
		return query;

	}

	@Override
	public int saveDruidDataSourceMasterInfo(JdbcTemplate clientJdbcTemplate, String clientStagingSchema,
			String table_name) {
		int id = 0;
		try {
			String sql = "insert into " + clientStagingSchema + ".druid_data_source_master(table_name) values(?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, table_name);
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}

		} catch (DataAccessException ae) {
			LOG.error("error while saveDruidDataSourceMasterInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return id;
	}

	@Override
	public int saveDruidDataSourceMappingInfo(JdbcTemplate clientJdbcTemaplate, String clientStagingSchema,
			int druidDatasourceId, String priority) {
		int id = 0;
		try {
			String sql = "insert into " + clientStagingSchema
					+ ".druid_data_source_info(druid_data_source_master_id,priority) values(?,?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemaplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setInt(1, druidDatasourceId);
					ps.setString(2, priority);
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}

		} catch (DataAccessException ae) {
			LOG.error("error while saveDruidDataSourceMappingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return id;
	}

	@Override
	public int getClientDruidDataSourceId(JdbcTemplate clientJdbcTemplate, String clientStagingSchema,
			String tableName) {
		int datasourceId = 0;
		;
		try {
			String sql = "select id from " + clientStagingSchema
					+ ".druid_data_source_master where table_name =? and active = '1'";

			datasourceId = clientJdbcTemplate.query(sql, new Object[] { tableName }, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						Integer id = rs.getInt("id");
						return id;
					} else {
						return 0;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getClientDruidDataSourceId()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return datasourceId;
	}

	@Override
	public int updateDruidDataSourceMasterVersion(JdbcTemplate clientJdbcTemplate, String clientStagingSchema,
			long newVersion, String tableName) {
		int id = 0;
		try {
			String sql = "update " + clientStagingSchema
					+ ".druid_data_source_master set current_version_number = ? where table_name = ? and active = '1'";
			id = clientJdbcTemplate.update(sql, newVersion, tableName);
		} catch (DataAccessException ae) {
			LOG.error("error while updateDruidDataSourceMasterVersion()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return id;
	}

	@Override
	public WebService getDefaultILWebServiceMappingForExactOnline(int webserviceId, int ilId,
			JdbcTemplate clientJdbcTemplate) {
		WebService webService = null;
		;
		try {
			String sql = sqlHelper.getSql("getDefaultILWebServiceMappingForExactOnline");

			webService = clientJdbcTemplate.query(sql, new Object[] { webserviceId, ilId },
					new ResultSetExtractor<WebService>() {
						@Override
						public WebService extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {

								WebService webService = new WebService();

								webService.setAuthenticationRequired(rs.getBoolean("is_Authentication_Required"));
								webService.setAuthentication_Method_Type(rs.getString("authentication_method_type"));
								webService.setAuthenticationUrl(rs.getString("authentication_url"));
								webService.setCookieRequired(rs.getBoolean("is_Cookie_Required"));
								webService.setTokenName(rs.getString("token_name"));
								webService.setResponseObjName(rs.getString("response_object_name"));
								webService.setUrl(rs.getString("default_web_service_url"));
								webService.setWeb_Service_Method_Type(rs.getString("web_service_method_type"));
								webService.setHeaders(rs.getString("header_details"));
								webService.setRequestMethod(rs.getString("web_service_method_type"));
								webService.setApiName(rs.getString("api_name"));
								webService.setWeb_service_sub_url(rs.getString("default_web_service_sub_url"));
								webService.setWeb_service_sub_url_param(rs.getString("web_service_sub_url_param"));
								webService.setResponseColumnObjName(rs.getString("response_column_object_name"));

								OAuth2 oauth2 = new OAuth2();
								oauth2.setAccessTokenUrl(rs.getString("access_token_url"));
								oauth2.setAuthenticationType(rs.getString("authentication_type"));
								oauth2.setClientIdentifier(rs.getString("client_identifier"));
								oauth2.setClientSecret(rs.getString("client_secret"));
								oauth2.setGrantType(rs.getString("grant_type"));
								oauth2.setRedirectUrl(rs.getString("redirect_uri"));
								oauth2.setResponseType(rs.getString("response_type"));
								oauth2.setScope(rs.getString("scope"));
								oauth2.setState(rs.getString("state"));

								webService.setOauth2(oauth2);

								return webService;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getDefaultILWebServiceMappingForExactOnline()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDefaultILWebServiceMappingForExactOnline()", e);
			
		}
		return webService;
	}

	@Override
	public Map<Integer, String> getAuthenticationTypes(JdbcTemplate clientJdbcTemplate) {

		Map<Integer, String> authTypes = null;

		try {

			String sql = sqlHelper.getSql("getAuthenticationTypes");

			authTypes = clientJdbcTemplate.query(sql, new ResultSetExtractor<Map<Integer, String>>() {

				@Override
				public Map<Integer, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs == null)
						return null;

					Map<Integer, String> contextParams = new LinkedHashMap<>();

					while (rs.next()) {
						contextParams.put(rs.getInt(1), rs.getString(2));
					}

					return contextParams;
				}
			});

		} catch (Exception e) {
			LOG.error("Error while reading getAuthenticationTypes ", e);
		}

		return authTypes;
	}

	@Override
	public int updateAuthenticationCodeForExactOnline(String clientId, String authCode, String authType,
			Modification modification, JdbcTemplate clientJdbcTemplate) {
		int count = -1;
		try {
			String sql = sqlHelper.getSql("updateAuthenticationCodeForExactOnline");
			count = clientJdbcTemplate.update(sql,
					new Object[] { authCode, modification.getCreatedBy(), modification.getCreatedTime(), authType });
		} catch (DataAccessException ae) {
			LOG.error("error while updateAuthenticationCodeForExactOnline()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateAuthenticationCodeForExactOnline()", e);
			
		}
		return count;
	}

	@Override
	public String getAuthCode(String clientId, String authType, JdbcTemplate clientJdbcTemplate) {
		String getAuthCode = null;
		;
		try {
			String sql = sqlHelper.getSql("getAuthCode");

			getAuthCode = clientJdbcTemplate.query(sql, new Object[] { authType }, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						String schemaName = rs.getString("authentication_code");
						return schemaName;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getAuthCode()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getAuthCode()", e);
			
		}
		return getAuthCode;
	}

	@Override
	public int updateAuthenticationTokenForExactOnline(String clientId, String authToken, String authRefreshToken,
			String authType, Modification modification, JdbcTemplate clientJdbcTemplate) {
		int count = -1;
		try {
			String sql = sqlHelper.getSql("updateAuthenticationTokenForExactOnline");
			count = clientJdbcTemplate.update(sql, new Object[] { authToken, authRefreshToken,
					modification.getCreatedBy(), modification.getCreatedTime(), authType });
		} catch (DataAccessException ae) {
			LOG.error("error while updateAuthenticationTokenForExactOnline()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateAuthenticationTokenForExactOnline()", e);
			
		}
		return count;
	}

	@Override
	public OAuth2 getAuthToken(String clientId, String authType, JdbcTemplate clientJdbcTemplate) {
		OAuth2 oauth2 = null;
		;
		try {
			String sql = sqlHelper.getSql("getAuthToken");

			oauth2 = clientJdbcTemplate.query(sql, new Object[] { authType }, new ResultSetExtractor<OAuth2>() {
				@Override
				public OAuth2 extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {

						OAuth2 oAuth2 = new OAuth2();
						oAuth2.setAccessTokenValue(rs.getString("authentication_token"));
						oAuth2.setRefreshTokenValue(rs.getString("authentication_refresh_token"));
						return oAuth2;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getAuthToken()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getAuthToken()", e);
			
		}
		return oauth2;
	}

	@Override
	public int updateHistoricalUpdatedTime(String updatedDate, int ilConnectionMappingId,
			JdbcTemplate clientJdbcTemplate) {
		int count = -1;
		try {
			String sql = sqlHelper.getSql("updateHistoricalUpdatedTime");
			count = clientJdbcTemplate.update(sql, new Object[] { updatedDate, ilConnectionMappingId });
		} catch (DataAccessException ae) {
			LOG.error("error while updateHistoricalUpdatedTime()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateHistoricalUpdatedTime()", e);
			
		}
		return count;
	}

	@Override
	public int saveHistoricalLoad(String clientId, HistoricalLoadForm historicalLoadForm, Modification modification,
			JdbcTemplate clientJdbcTemplate) {

		// TODO Auto-generated method stub
		int count = -1;
		try {
			String sql = sqlHelper.getSql("saveHistoricalLoad");
			count = clientJdbcTemplate.update(sql,
					new Object[] { historicalLoadForm.getClientId(), historicalLoadForm.getIlId(),
							historicalLoadForm.getConnectorId(), historicalLoadForm.getHistiricalQueryScript(),
							historicalLoadForm.getHistoricalFromDate(), historicalLoadForm.getHistoricalToDate(),
							historicalLoadForm.getLoadInterval(),historicalLoadForm.isMultipartEnabled(),
							historicalLoadForm.getNoOfRecordsPerFile(),
							modification.getCreatedBy(),
							modification.getCreatedTime(), historicalLoadForm.getDataSourceName() });

		} catch (DataAccessException ae) {
			LOG.error("error while saveHistoricalLoad()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveHistoricalLoad()", e);
			
		}

		return count;
	}

	@Override
	public List<HistoricalLoadForm> getHistoricalLoad(String clientId, JdbcTemplate clientJdbcTemplate) {
		List<HistoricalLoadForm> historicalLoadList = null;
		try {

			String sql = sqlHelper.getSql("getHistoricalLoad");
			historicalLoadList = clientJdbcTemplate.query(sql, new Object[] { clientId },
					new RowMapper<HistoricalLoadForm>() {
						@Override
						public HistoricalLoadForm mapRow(ResultSet rs, int rowNum) throws SQLException {
							HistoricalLoadForm historicalLoadForm = new HistoricalLoadForm();
							historicalLoadForm.setId(rs.getInt("id"));
							historicalLoadForm.setIlId(rs.getInt("il_id"));
							historicalLoadForm.setHistoricalFromDate(rs.getString("from_date"));
							historicalLoadForm.setHistoricalLastUpdatedTime(rs.getString("histrorical_updated_date"));
							historicalLoadForm.setHistoricalToDate(rs.getString("to_date"));
							historicalLoadForm.setLoadInterval(rs.getInt("historical_interval"));
							historicalLoadForm.setMultipartEnabled(rs.getBoolean("multipart_enabled"));
							historicalLoadForm.setNoOfRecordsPerFile(rs.getLong("no_of_records_per_file"));
							historicalLoadForm.setIlName(rs.getString("IL_name"));
							historicalLoadForm.setConnectorName(rs.getString("connection_name"));
							historicalLoadForm.setExecuted(rs.getBoolean("isExecuted"));
							historicalLoadForm.setRunning(rs.getBoolean("is_running"));
							return historicalLoadForm;
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getHistoricalLoad()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getHistoricalLoad()", e);
			
		}
		return historicalLoadList;
	}

	@Override
	public HistoricalLoadForm getHistoricalLoadDetailsById(String clientId, Integer loadId,
			JdbcTemplate clientJdbcTemplate) {
		HistoricalLoadForm historicalLoadList = null;
		try {

			String sql = sqlHelper.getSql("getHistoricalLoadDetailsById");

			historicalLoadList = clientJdbcTemplate.query(sql, new Object[] { clientId, loadId },
					new ResultSetExtractor<HistoricalLoadForm>() {
						@Override
						public HistoricalLoadForm extractData(ResultSet rs) throws SQLException, DataAccessException {

							if (rs != null && rs.next()) {
								HistoricalLoadForm historicalLoadForm = new HistoricalLoadForm();
								historicalLoadForm.setId(rs.getInt("id"));
								historicalLoadForm.setIlId(rs.getInt("il_id"));
								historicalLoadForm.setDataSourceName(rs.getString("data_souce_name"));
								historicalLoadForm.setConnectorId(rs.getInt("connector_id"));
								historicalLoadForm.setHistiricalQueryScript(rs.getString("historical_query"));
								historicalLoadForm
										.setHistoricalLastUpdatedTime(rs.getString("histrorical_updated_date"));
								historicalLoadForm.setHistoricalFromDate(rs.getString("from_date"));
								historicalLoadForm.setHistoricalToDate(rs.getString("to_date"));
								historicalLoadForm.setLoadInterval(rs.getInt("historical_interval"));
								historicalLoadForm.setMultipartEnabled(rs.getBoolean("multipart_enabled"));
								historicalLoadForm.setNoOfRecordsPerFile(rs.getLong("no_of_records_per_file"));
								historicalLoadForm.setIlName(rs.getString("IL_name"));
								historicalLoadForm.setConnectorName(rs.getString("connection_name"));
								historicalLoadForm.setExecuted(rs.getBoolean("isExecuted"));
								historicalLoadForm.setRunning(rs.getBoolean("is_running"));
								return historicalLoadForm;
							} else {
								return null;
							}
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getHistoricalLoad()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getHistoricalLoad()", e);
			
		}
		return historicalLoadList;
	}

	@Override
	public HistoricalLoadForm getHistoricalLoadDetailsByIdWithConnectionDetails(String clientId, Integer loadId,
			JdbcTemplate clientJdbcTemplate) {
		HistoricalLoadForm historicalLoadList = null;
		try {

			String sql = sqlHelper.getSql("getHistoricalLoadDetailsByIdWithConnectionDetails");

			historicalLoadList = clientJdbcTemplate.query(sql, new Object[] { clientId, loadId },
					new ResultSetExtractor<HistoricalLoadForm>() {
						@Override
						public HistoricalLoadForm extractData(ResultSet rs) throws SQLException, DataAccessException {

							if (rs != null && rs.next()) {
								HistoricalLoadForm historicalLoadForm = new HistoricalLoadForm();
								historicalLoadForm.setId(rs.getInt("id"));
								historicalLoadForm.setIlId(rs.getInt("il_id"));
								historicalLoadForm.setDataSourceName(rs.getString("data_souce_name"));
								historicalLoadForm.setHistiricalQueryScript(rs.getString("historical_query"));
								historicalLoadForm
										.setHistoricalLastUpdatedTime(rs.getString("histrorical_updated_date"));
								historicalLoadForm.setHistoricalFromDate(rs.getString("from_date"));
								historicalLoadForm.setHistoricalToDate(rs.getString("to_date"));
								historicalLoadForm.setLoadInterval(rs.getInt("historical_interval"));
								historicalLoadForm.setMultipartEnabled(rs.getBoolean("multipart_enabled"));
								historicalLoadForm.setNoOfRecordsPerFile(rs.getLong("no_of_records_per_file"));
								historicalLoadForm.setIlName(rs.getString("IL_name"));
								historicalLoadForm.setConnectorName(rs.getString("connection_name"));
								historicalLoadForm.setExecuted(rs.getBoolean("isExecuted"));
								historicalLoadForm.setRunning(rs.getBoolean("is_running"));
								try {
									ILConnection ilConnection = new ILConnection();
									historicalLoadForm.setIlConnection(ilConnection);
									ilConnection.setServer(
											EncryptionServiceImpl.getInstance().decrypt(rs.getString("server")));
									ilConnection.setUsername(
											EncryptionServiceImpl.getInstance().decrypt(rs.getString("username")));
									ilConnection.setPassword(
											EncryptionServiceImpl.getInstance().decrypt(rs.getString("password")));
									ilConnection.setSslEnable(rs.getBoolean("ssl_enable"));
									ilConnection.setSslTrustKeyStoreFilePaths(rs.getString("ssl_trust_key_store_file_paths"));
									Database database = new Database();
									database.setConnector_id(rs.getInt("dbid"));
									database.setName(rs.getString("dbname"));
									database.setConnectorName(rs.getString("connector_name"));
									database.setDriverName(rs.getString("driver_name"));
									database.setProtocal(rs.getString("protocal"));
									database.setUrlFormat(rs.getString("url_format"));

									ilConnection.setConnectionId(rs.getInt("connection_id"));
									ilConnection.setAvailableInCloud(rs.getBoolean("available_in_cloud"));
									ilConnection.setDatabase(database);
								} catch (Exception e) {
									LOG.error("Unable to decrypt connection detila",e);
									
								}

								return historicalLoadForm;
							} else {
								return null;
							}
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getHistoricalLoadDetailsByIdWithConnectionDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOG.error("error while getHistoricalLoadDetailsByIdWithConnectionDetails()", e);
			
		}
		return historicalLoadList;
	}

	@Override
	public int updateIlHistoryStatus(HistoricalLoadStatus historicalLoadStatus, JdbcTemplate clientJdbcTemplate) {
		int status = 0;
		try {
			String sql = sqlHelper.getSql("updateIlHistoryStatus");
			status = clientJdbcTemplate.update(sql,
					new Object[] { historicalLoadStatus.getHistoricalLoadId(), historicalLoadStatus.getFromDate(),
							historicalLoadStatus.getToDate(), historicalLoadStatus.getStartDate(),
							historicalLoadStatus.getEndDate(), historicalLoadStatus.getLoadInterval(),
							historicalLoadStatus.getLastUpdatedDate(), historicalLoadStatus.getS3FileName(),
							historicalLoadStatus.getFileSize(), historicalLoadStatus.getSourceRecordsCount(),
							historicalLoadStatus.isIlExecutionStatus(),
							historicalLoadStatus.getModification().getCreatedBy(),
							historicalLoadStatus.getModification().getCreatedTime(), historicalLoadStatus.getComment(),
							historicalLoadStatus.getStorageType() });

		} catch (DataAccessException ae) {
			LOG.error("error while updateIlHistoryStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateIlHistoryStatus()", e);
			
		}

		return status;
	}

	@Override
	public int updateHistoricalLoad(String clientId, HistoricalLoadForm historicalLoadForm, Modification modification,
			JdbcTemplate clientJdbcTemplate) {

		int count = -1;
		try {
			String sql = sqlHelper.getSql("updateHistoricalLoad");
			count = clientJdbcTemplate.update(sql,
					new Object[] { historicalLoadForm.getClientId(), historicalLoadForm.getIlId(),
							historicalLoadForm.getConnectorId(), historicalLoadForm.getHistiricalQueryScript(),
							historicalLoadForm.getHistoricalFromDate(), historicalLoadForm.getHistoricalToDate(),
							historicalLoadForm.getLoadInterval(),
							historicalLoadForm.isMultipartEnabled(),
							historicalLoadForm.getNoOfRecordsPerFile(),
							modification.getCreatedBy(),
							modification.getCreatedTime(), historicalLoadForm.getDataSourceName(),
							historicalLoadForm.getId()

					});

		} catch (DataAccessException ae) {
			LOG.error("error while updateHistoricalLoad()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateHistoricalLoad()", e);
			
		}

		return count;

	}

	@Override
	public List<HistoricalLoadStatus> getHistoricalLoadUploadStatus(String clientid, int historicalLoadId,
			JdbcTemplate clientJdbcTemplate) {
		List<HistoricalLoadStatus> historicalLoadStatusList = null;
		try {

			String sql = sqlHelper.getSql("getHistoricalLoadUploadStatus");

			historicalLoadStatusList = clientJdbcTemplate.query(sql, new Object[] { historicalLoadId },
					new RowMapper<HistoricalLoadStatus>() {
						@Override
						public HistoricalLoadStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
							HistoricalLoadStatus historicalLoadStatus = new HistoricalLoadStatus();
							historicalLoadStatus.setId(rs.getInt("id"));
							historicalLoadStatus.setHistoricalLoadId(rs.getInt("historical_load_id"));
							historicalLoadStatus.setFromDate(rs.getString("from_date"));
							historicalLoadStatus.setToDate(rs.getString("to_date"));
							historicalLoadStatus.setStartDate(rs.getString("start_date"));
							historicalLoadStatus.setEndDate(rs.getString("end_date"));
							historicalLoadStatus.setLoadInterval(rs.getInt("load_interval"));
							historicalLoadStatus.setLastUpdatedDate(rs.getString("last_updated_date"));
							historicalLoadStatus.setS3FileName(rs.getString("s3_file_path"));
							historicalLoadStatus
									.setFileSizeToDisplay(FileUtils.byteCountToDisplaySize(rs.getLong("file_size")));
							historicalLoadStatus.setSourceRecordsCount(rs.getLong("source_records_count"));
							historicalLoadStatus.setIlExecutionStatus(rs.getBoolean("il_execution_status"));
							historicalLoadStatus.setComment(rs.getString("comment"));
							Modification modification = new Modification();
							modification.setCreatedTime(rs.getString("created_time"));
							modification.setModifiedTime(rs.getString("modified_time"));
							historicalLoadStatus.setModification(modification);
							return historicalLoadStatus;

						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getHistoricalLoad()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getHistoricalLoad()", e);
			
		}
		return historicalLoadStatusList;
	}

	@Override
	public List<JobResult> getJobResultsForHistoricalLoad(int ilId, Long historicalLoadId, String clientId,
			String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate) {

		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_" + ilId + "\\_" + historicalLoadId + "\\_%";
			String sql = sqlHelper.getSql("getJobResultsForHistoricalLoad");

			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam }, new RowMapper<JobResult>() {
				public JobResult mapRow(ResultSet rs, int i) throws SQLException {
					JobResult jobresult = new JobResult();
					/* jobresult.setPackageId(Integer.parseInt(packageId)); */
					/* jobresult.setUserId(userId); */
					jobresult.setIlId(ilId);
					jobresult.setBatchId(rs.getString("BATCH_ID"));
					jobresult.setJobName(rs.getString("JOB_NAME"));
					jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
					jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
					jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
					jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
					jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
					jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
					jobresult.setErrorRowsCount(
							rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");

					return jobresult;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getJobResultsForHistoricalLoad()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getHistoricalLoad()", e);
			
		}

		return resultList;

	}

	@Override
	public List<JobResult> getJobResultsForHistoricalLoadByDate(int ilId, Long historicalLoadId, String clientId,
			String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate) {

		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_" + ilId + "\\_" + historicalLoadId + "\\_%";
			String sql = sqlHelper.getSql("getJobResultsForHistoricalLoadByDate");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam, fromDate, toDate },
					new RowMapper<JobResult>() {
						public JobResult mapRow(ResultSet rs, int i) throws SQLException {
							JobResult jobresult = new JobResult();
							/*
							 * jobresult.setPackageId(Integer.parseInt(packageId
							 * ));
							 */
							jobresult.setIlId(ilId);
							/* jobresult.setUserId(userId); */
							jobresult.setBatchId(rs.getString("BATCH_ID"));
							jobresult.setJobName(rs.getString("JOB_NAME"));
							jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
							jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
							jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
							jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
							jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
							jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
							jobresult.setErrorRowsCount(
									rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");

							return jobresult;
						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getJobResultsForHistoricalLoadByDate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getHistoricalLoad()", e);
			
		}

		return resultList;

	}

	@Override
	public List<JobResult> getJobResultsForDefaultCurrencyLoad(JdbcTemplate clientJdbcTemplate) {

		List<JobResult> resultList = null;
		try {
			String batchIdParam = "IL_Currency_Rate\\_%";
			String sql = sqlHelper.getSql("getJobResultsForCurrencyLoad");
			
			resultList = getRequiredJdbcTemplate(clientJdbcTemplate).query(sql, new Object[] { batchIdParam }, new RowMapper<JobResult>() {
				public JobResult mapRow(ResultSet rs, int i) throws SQLException {
					JobResult jobresult = new JobResult();
					jobresult.setBatchId(rs.getString("BATCH_ID"));
					jobresult.setJobName(rs.getString("JOB_NAME"));
					jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
					jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
					jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
					jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
					jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
					jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
					jobresult.setErrorRowsCount(rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");
					return jobresult;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getJobResultsForDefaultCurrencyLoad()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getJobResultsForDefaultCurrencyLoad()", e);
			
		}
		return resultList;
	}

	@Override
	public List<JobResult> getJobResultsForDefaultCurrencyLoad(String fromDate, String toDate,
			JdbcTemplate clientJdbcTemplate) {

		List<JobResult> resultList = null;
		try {
			String batchIdParam = "IL_Currency_Rate\\_%";
			String sql = sqlHelper.getSql("getJobResultsForCurrencyLoadByDate");
			resultList = getRequiredJdbcTemplate(clientJdbcTemplate).query(sql, new Object[] { batchIdParam, fromDate, toDate },
					new RowMapper<JobResult>() {
						public JobResult mapRow(ResultSet rs, int i) throws SQLException {
							JobResult jobresult = new JobResult();
							jobresult.setBatchId(rs.getString("BATCH_ID"));
							jobresult.setJobName(rs.getString("JOB_NAME"));
							jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
							jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
							jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
							jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
							jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
							jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
							jobresult.setErrorRowsCount(rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");
							return jobresult;
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getJobResultsForDefaultCurrencyLoadByDate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getJobResultsForDefaultCurrencyLoadByDate()", e);
			
		}
		return resultList;
	}

	@Override
	public int updateIlConnectionWebServiceMapping(int connectionMappingId, int ilId, String userId, int packageId,
			Modification modification, String wsIds, JdbcTemplate clientJdbcTemplate) {

		int count = -1;
		try {
			String sql = sqlHelper.getSql("updateIlConnectionWebServiceMapping");

			sql = StringUtils.replace(sql, "{wsIds}", wsIds);

			count = clientJdbcTemplate.update(sql, new Object[] { connectionMappingId, modification.getCreatedBy(),
					modification.getCreatedTime(), ilId, userId, packageId });

		} catch (DataAccessException ae) {
			LOG.error("error while updateIlConnectionWebServiceMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateIlConnectionWebServiceMapping()", e);
			
		}

		return count;

	}

	@Override
	public String getMappedQueryForWebserviceJoin(String userId, int iLId, int mappingId, int webserviceId,
			JdbcTemplate clientJdbcTemplate) {

		String iLQuery = null;
		;
		try {
			String sql = sqlHelper.getSql("getMappedQueryForWebserviceJoin");

			iLQuery = clientJdbcTemplate.query(sql, new Object[] { iLId, mappingId, webserviceId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String iLQuery = rs.getString("IL_query");
								return iLQuery;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getMappedQueryForWebserviceJoin()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getMappedQueryForWebserviceJoin()", e);
			
		}
		return iLQuery;

	}

	@Override
	public List<Table> getTempTablesAndWebServiceJoinUrls(String userId, int packageId, int ilId,
			int ilConnectionMappingId, JdbcTemplate clientJdbcTemplate) {

		List<Table> tables = null;

		try {
			String sql = sqlHelper.getSql("getTempTablesAndWebServiceJoinUrls");

			tables = clientJdbcTemplate.query(sql, new Object[] { userId, packageId, ilId, ilConnectionMappingId },
					new RowMapper<Table>() {

						@Override
						public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
							try {
								if (rs == null) {
									return null;
								}
								Table table = new Table();
								table.setTableName(rs.getString("temp_table_name"));
								table.setIlId(rs.getInt("il_id"));
								table.setTableStructure(rs.getString("temp_table_structure"));
								WebServiceJoin webServiceJoin = new WebServiceJoin();
								webServiceJoin.setWsMappingId(rs.getInt("id"));
								webServiceJoin.setWebServiceUrl(rs.getString("web_service_url"));
								webServiceJoin.setWebServiceApiName(rs.getString("web_service_api_name"));
								webServiceJoin.setResponseObjectName(rs.getString("web_service_response_object_name"));
								webServiceJoin.setResponseColumnObjectName(rs.getString("response_column_object_name"));
								webServiceJoin.setWebServiceMethodType(rs.getString("web_service_method_type"));
								webServiceJoin.setIncrementalUpdate(rs.getBoolean("incremental_update"));
								webServiceJoin.setIncrementalUpdateparamdata(rs.getString("incremental_update_params"));
								webServiceJoin.setSoapBodyElement(rs.getString("soap_body_element"));
								webServiceJoin.setRetryPaginationData(rs.getString("retry_pagination"));
								table.setWebServiceJoin(webServiceJoin);
								return table;
							} catch (Exception e) {
								LOG.error("Error while converting database row to table object ", e);
							}
							return null;
						}
					});

		} catch (Exception e) {
			LOG.error("Error while getting the temp tables .. ", e);
		}

		return tables;

	}

	@Override
	public WebService getWebServiceMasterById(int id, String userId, JdbcTemplate clientJdbcTemplate) {

		WebService webService = null;
		;
		try {
			String sql = sqlHelper.getSql("getWebServiceMasterById");

			webService = clientJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<WebService>() {
				@Override
				public WebService extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						WebService webService = new WebService();
						webService.setWeb_service_id(rs.getString("id"));
						webService.setAuthenticationUrl(rs.getString("authentication_url"));
						webService.setAuthentication_Method_Type(rs.getString("authentication_method_type"));
						return webService;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getWebServiceMasterById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getWebServiceMasterById()", e);
			
		}
		return webService;

	}

	@Override
	public int updateWsApiIlConnectionWebServiceMapping(WebService webservice, int ilConnectionMappingId,
			Modification modification, String userId, JdbcTemplate clientJdbcTemplate) {

		int count = -1;
		try {
			String sql = sqlHelper.getSql("updateWsApiIlConnectionWebServiceMapping");
			count = clientJdbcTemplate.update(sql,
					new Object[] { 
							webservice.getPackageId(), 
							Integer.parseInt(userId), 
							webservice.getIlId(),
							ilConnectionMappingId, 
							webservice.getUrl(), 
							//webservice.getBaseUrl(),
							webservice.getBaseUrlRequired(),
							webservice.getRequestMethod(),
							webservice.getApiName(),
							webservice.getResponseObjName(),
							webservice.getResponseColumnObjName(), 
							webservice.getApiPathParams(),
							webservice.getRequestParameters(),
							webservice.getApiBodyParams(),
							webservice.getPaginationRequired(),
							webservice.getPaginationType(),
							webservice.getPaginationRequestParamsData(),
							webservice.getIncrementalUpdate(),
							webservice.getSoapBodyElement(),
							webservice.getIncrementalUpdateparamdata(),
							modification.getCreatedBy(),
							modification.getCreatedTime() 
							});

		} catch (DataAccessException ae) {
			LOG.error("error while updateIlConnectionWebServiceMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateIlConnectionWebServiceMapping()", e);
			
		}

		return count;

	}

	@Override
	public WebServiceApi getIlConnectionWebServiceMapping(String userId, int packageId, int ilId,
			int ilConnectionMappingId, JdbcTemplate clientJdbcTemplate) {

		WebServiceApi webServicApi = null;
		try {
			String sql = sqlHelper.getSql("getIlConnectionWebServiceMapping");

			webServicApi = clientJdbcTemplate.query(sql,
					new Object[] { userId, packageId, ilId, ilConnectionMappingId },
					new ResultSetExtractor<WebServiceApi>() {
						@Override
						public WebServiceApi extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								WebServiceApi webServicApi = new WebServiceApi();
								webServicApi.setId(rs.getLong("id"));
								webServicApi.setApiName(rs.getString("web_service_api_name"));
								webServicApi.setApiUrl(rs.getString("web_service_url"));
								webServicApi.setBaseUrlRequired(rs.getBoolean("base_url_required"));
								webServicApi.setApiMethodType(rs.getString("web_service_method_type"));
								webServicApi.setApiPathParams(rs.getString("api_path_params"));
								webServicApi.setResponseObjectName(rs.getString("web_service_response_object_name"));
								webServicApi.setResponseColumnObjectName(rs.getString("response_column_object_name"));
								webServicApi.setApiRequestParams(rs.getString("api_request_params"));
								webServicApi.setPaginationRequired(rs.getBoolean("pagination_required"));
								webServicApi.setPaginationType(rs.getString("pagination_type"));
								webServicApi.setPaginationRequestParamsData(rs.getString("pagination_request_params"));
								webServicApi.setIncrementalUpdate(rs.getBoolean("incremental_update"));
								webServicApi.setIncrementalUpdateparamdata(rs.getString("incremental_update_params"));
								webServicApi.setApiBodyParams(rs.getString("api_body_params"));
								webServicApi.setIlId(rs.getLong("il_id"));
								webServicApi.setSoapBodyElement(rs.getString("soap_body_element"));
								webServicApi.setRetryPaginationData(rs.getString("retry_pagination"));
								return webServicApi;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getIlConnectionWebServiceMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getIlConnectionWebServiceMapping()", e);
			
		}
		return webServicApi;

	}

	@Override
	public String getILIncrementalUpdateDate(String clientStagingSchema, int ilId, String dataSourceId,
			String typeOfSource, int packageSourceMappingId,JdbcTemplate clientJdbcTemplate) {
		String date = null;
		try {
			String sql = sqlHelper.getSql("getILIncrementalUpdateDate");

			date = clientJdbcTemplate.query(sql, new Object[] { ilId, dataSourceId, typeOfSource,packageSourceMappingId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								return rs.getString(1);
							}
							return null;
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getILIncrementalUpdateDate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOG.error("error while getILIncrementalUpdateDate()", e);
			
		}

		return date;
	}

	@Override
	public List<String> getColumnHeadersByQuery(String tempQuery, JdbcTemplate clientJdbcTemplate, boolean isDDl) {
		List<String> queryColumns = null;
		try {

			queryColumns = clientJdbcTemplate.query(tempQuery, new ResultSetExtractor<List<String>>() {
				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					List<String> columnHeaders = new ArrayList<>();
					for (int i = 1; i <= columnCount; i++) {
						// if(!isDDl){
						String columnHeader = rsmd.getColumnLabel(i);
						columnHeaders.add(columnHeader);
						/*
						 * }else{ String columnHeader = rsmd.getColumnName(i);
						 * columnHeaders.add(columnHeader); }
						 */
					}
					return columnHeaders;
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getILIncrementalUpdateDate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOG.error("error while getILIncrementalUpdateDate()", e);
			
		}

		return queryColumns;
	}

	@Override
	public List<User> getClientUserDeatils(String clientId, JdbcTemplate clientJdbcTemplate) {

		List<User> usersList = null;
		try {
			String sql = sqlHelper.getSql("getClientUserDeatils");
			usersList = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<User>() {
				public User mapRow(ResultSet rs, int i) throws SQLException {
					User user = new User();
					try {
						user.setUserId(EncryptionServiceImpl.getInstance().encrypt(rs.getString("userId")));
					} catch (Exception e) {
						throw new AnvizentRuntimeException(e);
					}
					return user;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getClientUserDeatils()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientUserDeatils()", e);
			
		}

		return usersList;

	}

	@Override
	public List<String> getClientUserIds(String clientId, JdbcTemplate clientJdbcTemplate) {

		List<String> usersList = null;
		try {
			String sql = sqlHelper.getSql("getClientUserDeatils");
			usersList = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int i) throws SQLException {
					try {
						return EncryptionServiceImpl.getInstance().encrypt(rs.getString("userId"));
					} catch (Exception e) {
						throw new AnvizentRuntimeException(e);
					}
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getClientUserIds()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientUserIds()", e);
			
		}

		return usersList;

	}

	@Override
	public List<Map<String, Object>> getTableData(String query, String parametrs, JdbcTemplate clientJdbcTemplate) {

		List<Map<String, Object>> getTableData = null;
		try {
			getTableData = clientJdbcTemplate.queryForList(query);

		} catch (DataAccessException ae) {
			LOG.error("error while getTableData()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return getTableData;

	}

	@Override
	public List<ClientDataSources> getDataSourceList(String clientId, JdbcTemplate clientJdbcTemplate) {
		List<ClientDataSources> dataSources = null;
		try {
			String sql = sqlHelper.getSql("getDataSourceList");
			dataSources = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<ClientDataSources>() {
				@Override
				public ClientDataSources mapRow(ResultSet rs, int rowNum) throws SQLException {
					ClientDataSources dataSource = new ClientDataSources();
					dataSource.setId(rs.getInt("id"));
					dataSource.setClientId(rs.getInt("client_id"));
					dataSource.setDataSourceName(rs.getString("datasource_name"));
					return dataSource;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getDataSourceList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDataSourceList()", e);
			
		}
		return dataSources;
	}

	@Override
	public List<DDLayout> getDDlayoutTablesList(String clientId, String userId, JdbcTemplate clientJdbcTemplate) {
		List<DDLayout> dDLayoutTablesList = null;
		try {
			String sql = sqlHelper.getSql("getDDlayoutTablesList");
			dDLayoutTablesList = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<DDLayout>() {
				public DDLayout mapRow(ResultSet rs, int i) throws SQLException {
					DDLayout dDLayout = new DDLayout();
					dDLayout.setId(rs.getInt("id"));
					dDLayout.setClientId(rs.getString("client_id"));
					dDLayout.setTableName(rs.getString("table_name"));
					dDLayout.setTableDes(rs.getString("table_desc"));
					dDLayout.setTable5ructure(rs.getString("table_structure"));
					dDLayout.setSelectQry(rs.getString("select_query"));
					dDLayout.setDdlTables(rs.getString("ddl_tables"));
					dDLayout.setDdlEditable(StringUtils.equals(userId, rs.getString("userid")));
					return dDLayout;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getDDlayoutTablesList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDDlayoutTablesList()", e);
			
		}

		return dDLayoutTablesList;

	}

	@Override
	public DDLayout getDDlayoutTable(String clientId, int id, String userId, JdbcTemplate clientJdbcTemplate) {
		DDLayout dDLayout = null;
		try {
			String sql = sqlHelper.getSql("getDDlayoutTable");
			dDLayout = clientJdbcTemplate.query(sql, new Object[] { id, clientId }, new ResultSetExtractor<DDLayout>() {
				@Override
				public DDLayout extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						DDLayout dDLayout = new DDLayout();
						dDLayout.setId(rs.getInt("id"));
						dDLayout.setTableName(rs.getString("table_name"));
						dDLayout.setSelectQry(rs.getString("select_query"));
						dDLayout.setDdlTables(rs.getString("ddl_tables"));
						dDLayout.setClientId(rs.getString("client_id"));
						dDLayout.setDdlEditable(StringUtils.equals(userId, rs.getString("userid")));
						return dDLayout;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getDDlayoutTable()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDDlayoutTable()", e);
			
		}
		return dDLayout;
	}

	@Override
	public int updateDDlayoutTable(String clientId, DDLayout dDLayout, String userId, JdbcTemplate clientJdbcTemplate) {
		int count = -1;
		try {
			String sql = sqlHelper.getSql("updateDDlayoutTable");
			count = clientJdbcTemplate.update(sql,
					new Object[] { dDLayout.getSelectQry(), dDLayout.getModification().getCreatedBy(),
							dDLayout.getModification().getCreatedTime(), dDLayout.getId(), clientId, userId });
		} catch (DataAccessException ae) {
			LOG.error("error while updateDDlayoutTable()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateDDlayoutTable()", e);
			
		}
		return count;
	}

	@Override
	public int saveDDlTableInfo(ClientData clientData, String clientId, JdbcTemplate clientJdbcTemplate) {
		int targetTableId = -1;
		try {
			final ClientData clientDataObject = clientData;
			final String sql = sqlHelper.getSql("saveDDlTableInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, clientId);
					ps.setString(2, clientDataObject.getUserId());
					ps.setString(3, clientDataObject.getUserPackage().getTable().getTableName());
					ps.setString(4, clientDataObject.getUserPackage().getTable().getTableName());
					ps.setString(5, clientDataObject.getUserPackage().getTable().getTableStructure());
					ps.setString(6, clientDataObject.getIlConnectionMapping().getiLquery());
					ps.setString(7, clientDataObject.getUserPackage().getTable().getDdlTables());
					ps.setString(8, clientDataObject.getModification().getCreatedBy());
					ps.setString(9, clientDataObject.getModification().getCreatedTime());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				targetTableId = autoIncrement.intValue();
			}
		} catch (DuplicateKeyException ae) {
			LOG.error("error while saveTargetTableInfo()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOG.error("error while saveTargetTableInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveTargetTableInfo()", e);
			
		}

		return targetTableId;
	}

	@Override
	public void dropDDlayoutTable(String clientId, DDLayout dDLayout, JdbcTemplate jdbcTemplate) {
		try {
			String sql = "drop table if exists `" + dDLayout.getTableName() + "`";
			jdbcTemplate.execute(sql);
		} catch (DataAccessException ae) {
			LOG.error("error while dropDDlayoutTable()", ae);
			throw new AnvizentRuntimeException(ae);
		}
	}

	@Override
	public int deleteDDlayoutTable(String userId, DDLayout dDLayout, JdbcTemplate clientJdbcTemplate) {
		int count = -1;
		try {
			String sql = sqlHelper.getSql("deleteDDlayoutTable");
			count = clientJdbcTemplate.update(sql, new Object[] { dDLayout.getId(), dDLayout.getClientId(), userId });
		} catch (DataAccessException ae) {
			LOG.error("error while updateDDlayoutTable()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateDDlayoutTable()", e);
			
		}
		return count;
	}

	@Override
	public List<DDLayout> getDDlayoutList(String clientId, List<String> derivedTablesList, String userId,
			JdbcTemplate clientJdbcTemplate) {
		List<DDLayout> dDLayouts = new ArrayList<>();
		try {
			StringBuilder builder = new StringBuilder();
			List<String> paramObj = new ArrayList<>(derivedTablesList);
			derivedTablesList.forEach(value -> {
				builder.append(" find_in_set(?,ddl_tables) or");
			});
			paramObj.add(0, clientId);
			String sql = StringUtils.replace(sqlHelper.getSql("getDDlayoutList"), "{tableNames}",
					builder.delete(builder.length() - 2, builder.length()).toString());
			dDLayouts = clientJdbcTemplate.query(sql, paramObj.toArray(), new RowMapper<DDLayout>() {
				public DDLayout mapRow(ResultSet rs, int i) throws SQLException {
					DDLayout dDLayout = new DDLayout();
					dDLayout.setId(rs.getInt("id"));
					dDLayout.setClientId(rs.getString("client_id"));
					dDLayout.setTableName(rs.getString("table_name"));
					dDLayout.setTableDes(rs.getString("table_desc"));
					dDLayout.setTable5ructure(rs.getString("table_structure"));
					dDLayout.setSelectQry(rs.getString("select_query"));
					dDLayout.setDdlTables(rs.getString("ddl_tables"));
					return dDLayout;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getDDlayoutList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDDlayoutList()", e);
			
		}
		return dDLayouts;
	}

	@Override
	public int createDataSourceList(ClientDataSources clientDataSources, JdbcTemplate clientJdbcTemplate) {

		int saveDataSource = 0;
		try {
			String sql = sqlHelper.getSql("createDataSourceList");
			saveDataSource = clientJdbcTemplate.update(sql,
					new Object[] { clientDataSources.getClientId(), clientDataSources.getDataSourceName(),
							clientDataSources.getDataSourceFrom(), clientDataSources.getModification().getCreatedBy(),
							clientDataSources.getModification().getCreatedTime()

					});
		} catch (DataAccessException ae) {
			LOG.error("error while createDataSourceList()", ae);
			// throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while createDataSourceList()", e);
			
		}
		return saveDataSource;
	}

	@Override
	public int updateDataSourceDetails(Integer mappingId, String dataSouceName, String dataSouceNameOther,
			Integer packageId, JdbcTemplate clientJdbcTemplate) {
		int i = -1;
		try {
			String sql = sqlHelper.getSql("updateDataSourceDetails");
			i = clientJdbcTemplate.update(sql, new Object[] { dataSouceName, mappingId, packageId });

		} catch (SqlNotFoundException e) {
			LOG.error("error while createDataSourceList()", e);
			
		}
		return i;
	}

	@Override
	public List<ILInfo> getClientIlsList(String clientId, JdbcTemplate clientJdbcTemplate) {
		List<ILInfo> ilsList = new ArrayList<>();
		String sql = null;
		try {
			sql = sqlHelper.getSql("getClientIlsList");
			ilsList = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<ILInfo>() {
				@Override
				public ILInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					ILInfo ilInfo = new ILInfo();
					ilInfo.setiL_id(rs.getInt("IL_id"));
					ilInfo.setiL_name(rs.getString("IL_name"));
					ilInfo.setiLType(rs.getString("il_type"));
					ilInfo.setiL_table_name(rs.getString("il_table_name"));
					ilInfo.setDescription(rs.getString("description"));
					return ilInfo;
				}
			});
		} catch (DataAccessException e) {
			LOG.error("error while getClientIlsList()", e);
			throw new AnvizentRuntimeException(e);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientIlsList()", e);
			
		}
		return ilsList;
	}

	@Override
	public List<ScheduleResult> getScheduleStartTime(int packageId, JdbcTemplate clientJdbcTemplate) {

		List<ScheduleResult> scheduleResults = null;
		try {
			String sql = sqlHelper.getSql("getScheduleStartTime");

			scheduleResults = clientJdbcTemplate.query(sql, new RowMapper<ScheduleResult>() {

				@Override
				public ScheduleResult mapRow(ResultSet rs, int rowNum) throws SQLException {
					ScheduleResult scheduleResult = new ScheduleResult();
					scheduleResult.setScheduleStartTime(rs.getString("schedule_start_date_time"));
					scheduleResult.setClientSchedulerStatus(rs.getString("client_schedular_status"));
					scheduleResult.setServerSchedulerStatus(rs.getString("server_schedular_status"));
					scheduleResult.setCount(rs.getInt("count"));

					return scheduleResult;
				}

			}, packageId);

		} catch (DataAccessException ae) {
			LOG.error("error while getScheduleStartTime()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getScheduleStartTime()", e);
			
		}
		return scheduleResults;
	}

	@Override
	public List<ScheduleResult> getScheduleDetails(int packageId, String scheduleStartTime,
			JdbcTemplate clientJdbcTemplate) {

		List<ScheduleResult> scheduleDtetails = null;
		try {
			String sql = sqlHelper.getSql("getScheduleDetails");
			scheduleDtetails = clientJdbcTemplate.query(sql, new Object[] { packageId, scheduleStartTime },
					new RowMapper<ScheduleResult>() {

						@Override
						public ScheduleResult mapRow(ResultSet rs, int rowNum) throws SQLException {
							ScheduleResult scheduleInfo = new ScheduleResult();
							scheduleInfo.setId(rs.getInt("schedulerid"));
							scheduleInfo.setDlName(rs.getString("DL_name"));
							scheduleInfo.setIlName(rs.getString("IL_name"));
							scheduleInfo.setClientSchedulerStatus(rs.getString("client_schedular_status"));
							scheduleInfo
									.setClientSchedulerStatusDetails(rs.getString("client_scheduler_status_details"));
							scheduleInfo.setServerSchedulerStatus(rs.getString("server_schedular_status"));
							scheduleInfo
									.setServerSchedulerStatusDetails(rs.getString("server_scheduler_status_details"));
							return scheduleInfo;
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getScheduleStartTime()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getScheduleStartTime()", e);
			
		}
		return scheduleDtetails;
	}

	@Override
	public int updateDDlayoutTableAuditLogs(String userId, DDLayout dDLayout, JdbcTemplate clientJdbcTemplate) {
		int i = -1;
		try {
			String sql = sqlHelper.getSql("updateDDlayoutTableAuditLogs");
			i = clientJdbcTemplate.update(sql,
					new Object[] { dDLayout.getClientId(), userId, dDLayout.getId(), dDLayout.getInsertedCount(),
							dDLayout.getRunType(), dDLayout.getSelectQry(), dDLayout.getDdlTables(),
							dDLayout.getErrorMessage(), dDLayout.getModification().getCreatedBy(),
							dDLayout.getModification().getCreatedTime() });

		} catch (SqlNotFoundException e) {
			LOG.error("error while updateDDlayoutTableAuditLogs()", e);
			
		}
		return i;
	}

	@Override
	public List<DDLayout> getDDlayoutTableAuditLogs(String userId, DDLayout dDLayout, JdbcTemplate clientJdbcTemplate) {
		List<DDLayout> dDLayouts = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getDDlayoutTableAuditLogs");
			dDLayouts = clientJdbcTemplate.query(sql, new Object[] { dDLayout.getClientId(), dDLayout.getId() },
					new RowMapper<DDLayout>() {
						public DDLayout mapRow(ResultSet rs, int i) throws SQLException {
							DDLayout dDLayout = new DDLayout();
							dDLayout.setDdlAuditTblId(rs.getInt("id"));
							dDLayout.setId(rs.getInt("ddl_table_id"));
							dDLayout.setInsertedCount(rs.getInt("records_count"));
							dDLayout.setRunType(rs.getString("run_type"));
							dDLayout.setSelectQry(rs.getString("select_qry"));
							dDLayout.setDdlTables(rs.getString("ddl_sources"));
							dDLayout.setErrorMessage(rs.getString("error_messages"));
							Modification modification = new Modification();
							modification.setCreatedTime(rs.getString("created_time"));
							dDLayout.setModification(modification);
							return dDLayout;
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getDDlayoutTableAuditLogs()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDDlayoutTableAuditLogs()", e);
			
		}
		return dDLayouts;
	}

	@Override
	public DDLayout viewDDlTableSelectQry(String userId, DDLayout dDLayout, JdbcTemplate clientJdbcTemplate) {
		DDLayout ddlLayout = null;
		try {
			String sql = sqlHelper.getSql("viewDDlTableSelectQry");
			ddlLayout = clientJdbcTemplate.query(sql,
					new Object[] { dDLayout.getClientId(), userId, dDLayout.getDdlAuditTblId() },
					new ResultSetExtractor<DDLayout>() {
						@Override
						public DDLayout extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								DDLayout dDLayout = new DDLayout();
								dDLayout.setDdlAuditTblId(rs.getInt("id"));
								dDLayout.setSelectQry(rs.getString("select_qry"));
								return dDLayout;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while viewDDlTableSelectQry()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while viewDDlTableSelectQry()", e);
			
		}
		return ddlLayout;
	}

	@Override
	public int updatePackageExecutorSourceMappingInfo(SourceFileInfo sourceFileInfo, JdbcTemplate clientJdbcTemplate) {
		int i = -1;
		try {
			String sql = sqlHelper.getSql("updatePackageExecutorSourceMappingInfo");
			i = clientJdbcTemplate.update(sql,
					new Object[] { 
							sourceFileInfo.getPackageExecution().getExecutionId(),
							sourceFileInfo.getIlConnectionMappingId(),
							sourceFileInfo.getSourceFileId(),
							sourceFileInfo.isIncrementalUpdate(),
							sourceFileInfo.getIncrementalDateValue(),
							sourceFileInfo.getPackageExecution().getUploadStatus(),
							sourceFileInfo.getPackageExecution().getUploadComments(),
							sourceFileInfo.getPackageExecution().getExecutionStatus(),
							sourceFileInfo.getPackageExecution().getExecutionComments(),
							sourceFileInfo.getModification().getCreatedBy(),
							sourceFileInfo.getModification().getCreatedTime() });

		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageExecutorSourceMappingInfo()", e);
			
		}
		return i;
	}

	@Override
	public ILConnectionMapping getIlConnectionMappingByPackageExecutorSourceMappingInfo(long executionId,
			long executionMappingId, JdbcTemplate clientJdbcTemplate) {

		ILConnectionMapping iLConnectionMapping = null;
		try {
			String sql = sqlHelper.getSql("getIlConnectionMappingByPackageExecutorSourceMappingInfo");
			Object[] parametres = new Object[] { executionId, executionMappingId };
			iLConnectionMapping = clientJdbcTemplate.query(sql, parametres,
					new ResultSetExtractor<ILConnectionMapping>() {
						@Override
						public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs != null && rs.next()) {
								ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
								try {
									ilConnectionMapping.setConnectionMappingId(rs.getInt("il_con_mapping_id"));
									ilConnectionMapping.setExecutionMappingId(rs.getLong("execution_mapping_id"));
									ilConnectionMapping.setiLId(rs.getInt("IL_id"));
									ilConnectionMapping.setdLId(rs.getInt("dL_Id"));
									ilConnectionMapping.setPackageId(rs.getInt("package_id"));
									ilConnectionMapping.setSourceFileInfoId(rs.getLong("source_file_info_id"));
									ilConnectionMapping.setMultipartEnabled(rs.getBoolean("multi_part_enabled"));
									ilConnectionMapping.setStorageType(rs.getString("storage_type"));
									ilConnectionMapping.setS3BucketId(rs.getInt("s3_bucket_id"));
									ilConnectionMapping.setFilePath(rs.getString("s3_file_path"));
									ilConnectionMapping.setFileType(rs.getString("fileType"));
									ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
									ilConnectionMapping.setEncryptionRequired(rs.getBoolean("encryption_required"));
									ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
									ilConnectionMapping.setIncrementalDateValue(rs.getString("incremental_date"));
									ilConnectionMapping.setIsIncrementalUpdate(rs.getBoolean("incremental_update"));

								} catch (Exception e) {
									LOG.error("error while getIlConnectionMappingByPackageExecutorSourceMappingInfo()",
											e);
									
								}
								return ilConnectionMapping;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getIlConnectionMappingByPackageExecutorSourceMappingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getIlConnectionMappingByPackageExecutorSourceMappingInfo()", e);
			
		}
		return iLConnectionMapping;
	}

	@Override
	public int updateSourceFileInfo(SourceFileInfo sourceFileInfo, JdbcTemplate clientJdbcTemplate) {
		int sourceFileId = -1;
		try {
			final String sql = sqlHelper.getSql("updateSourceFileInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setBoolean(1, sourceFileInfo.isMultiPartFile());
					ps.setString(2, sourceFileInfo.getStorageType());
					ps.setInt(3, (sourceFileInfo.getS3BucketInfo() == null || sourceFileInfo.getS3BucketInfo().getId() == null ) ? 0 : sourceFileInfo.getS3BucketInfo().getId());
					ps.setString(4, sourceFileInfo.getFilePath());
					ps.setString(5, sourceFileInfo.getFileType());
					ps.setString(6, sourceFileInfo.getDelimeter());
					ps.setString(7, sourceFileInfo.getModification().getCreatedBy());
					ps.setString(8, sourceFileInfo.getModification().getCreatedTime());
					ps.setInt(9, sourceFileInfo.getRowCount());
					ps.setString(10, sourceFileInfo.getFileSize());
					ps.setObject(11, sourceFileInfo.getEncryptionRequired());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				sourceFileId = autoIncrement.intValue();
			}
		} catch (DataAccessException ae) {
			LOG.error("error while updateSourceFileInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateSourceFileInfo()", e);
			
		}
		return sourceFileId;
	}

	@Override
	public List<PackageExecutorMappingInfo> getPackageExecutorSourceMappingInfoList(long executionId,
			JdbcTemplate clientJdbcTemplate) {
		List<PackageExecutorMappingInfo> packageExecutorMappingInfoList = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getPackageExecutorSourceMappingInfo");
			packageExecutorMappingInfoList = clientJdbcTemplate.query(sql, new Object[] { executionId },
					new RowMapper<PackageExecutorMappingInfo>() {
						public PackageExecutorMappingInfo mapRow(ResultSet rs, int i) throws SQLException {
							PackageExecutorMappingInfo packageExecutorMappingInfo = new PackageExecutorMappingInfo();
							packageExecutorMappingInfo.setId(rs.getLong("execution_mapping_id"));
							packageExecutorMappingInfo.setExecutionId(rs.getLong("execution_id"));
							PackageExecution packageExecution = new PackageExecution();
							packageExecution.setDlId(rs.getInt("dl_id"));
							packageExecution.setIlId(rs.getInt("IL_id"));
							packageExecution.setTimeZone(rs.getString("time_zone"));
							packageExecutorMappingInfo.setPackageExecution(packageExecution);
							return packageExecutorMappingInfo;
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getPackageExecutorSourceMappingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageExecutorSourceMappingInfo()", e);
			
		}
		return packageExecutorMappingInfoList;

	}

	@Override
	public Database getDbIdDriverNameAndProtocal(int databaseTypeId, JdbcTemplate clientJdbcTemplate) {
		Database database = null;
		try {
			String sql = sqlHelper.getSql("getDbIdDriverNameAndProtocal");
			database = clientJdbcTemplate.query(sql, new Object[] { databaseTypeId },
					new ResultSetExtractor<Database>() {
						@Override
						public Database extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								Database database = new Database();
								database.setDriverName(rs.getString("driver_name"));
								database.setProtocal(rs.getString("protocal"));
								database.setConnector_id(rs.getInt("id"));
								return database;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getDbIdDriverNameAndProtocal()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDbIdDriverNameAndProtocal()", e);
			
		}
		return database;
	}

	@Override
	public List<PackageExecution> getPackageExecution(Integer packageId, JdbcTemplate clientJdbcTemplate) {
		List<PackageExecution> packageDetails = null;
		try {
			String sql = sqlHelper.getSql("getScheduleDetails");
			packageDetails = clientJdbcTemplate.query(sql, new Object[] { packageId },
					new RowMapper<PackageExecution>() {

						@Override
						public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
							PackageExecution packageExecution = new PackageExecution();
							packageExecution.setExecutionId(rs.getInt("execution_id"));
							packageExecution.setPackageId(rs.getInt("package_id"));
							packageExecution.setInitiatedFrom(rs.getString("initiated_from"));
							packageExecution.setRunType(rs.getString("run_type"));
							packageExecution.setUploadStatus(rs.getString("upload_status"));
							packageExecution.setExecutionStatus(rs.getString("execution_status"));
							packageExecution.setDruidStatus(rs.getString("druid_status"));
							packageExecution.setExecutionStartDate(rs.getString("exection_start_date"));
							packageExecution.setLastExecutedDate(rs.getString("last_executed_date"));
							packageExecution.setUploadStartDate(rs.getString("upload_start_date"));
							packageExecution.setLastUploadedDate(rs.getString("last_uploaded_date")); 
							packageExecution.setDruidStartDate(rs.getString("druid_start_date"));
							packageExecution.setDruidEndDate(rs.getString("druid_end_date"));
							return packageExecution;
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getPackageExecution()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageExecution()", e);
			
		}
		return packageDetails;

	}

	@Override
	public List<DLInfo> getILConnectionMappingDlInfoByPackageId(Integer packageId, String userId,
			JdbcTemplate clientJdbcTemplate) {
		List<DLInfo> dLInfo = null;
		try {
			String sql = sqlHelper.getSql("getILConnectionMappingDlInfoByPackageId");
			dLInfo = clientJdbcTemplate.query(sql, new Object[] { packageId, userId }, new RowMapper<DLInfo>() {
				@Override
				public DLInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					DLInfo dLInfo = new DLInfo();
					dLInfo.setdL_id(rs.getInt("DL_id"));
					return dLInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getILConnectionMappingDlInfoByPackageId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILConnectionMappingDlInfoByPackageId()", e);
			
		}
		return dLInfo;
	}

	@Override
	public FileSettings getFileSettingsInfo(String clientId, JdbcTemplate clientJdbcTemplate) {
		FileSettings fileSettings = null;
		try {
			String sql = sqlHelper.getSql("getFileSettingsInfo");
			fileSettings = clientJdbcTemplate.queryForObject(sql, new RowMapper<FileSettings>() {
				public FileSettings mapRow(ResultSet rs, int i) throws SQLException {
					FileSettings fileSettings = new FileSettings();
					fileSettings.setMaxFileSizeInMb(rs.getInt("max_file_size_in_mb"));
					fileSettings.setTrailUserMaxFileSizeInMb(rs.getLong("trail_user_max_file_size_in_mb"));
					fileSettings.setMultiPartEnabled(rs.getBoolean("multipart_file_enabled"));
					fileSettings.setNoOfRecordsPerFile(rs.getInt("no_of_records_per_file"));
					fileSettings.setFileEncryption(rs.getBoolean("file_encryption"));
					return fileSettings;
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getS3BucketInfoByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getS3BucketInfoByClientId", e);
			
		}
		return fileSettings;
	}

	@Override
	public Package getPackageSchedule(String clientId, int packageId, int scheduleId, JdbcTemplate clientJdbcTemplate) {
		Package packageSchedule = null;
		try {
			String sql = sqlHelper.getSql("getPackageSchedule");
			packageSchedule = clientJdbcTemplate.queryForObject(sql, new Object[] { packageId, scheduleId },
					new RowMapper<Package>() {
						public Package mapRow(ResultSet rs, int i) throws SQLException {
							Package packageschedule = new Package();
							packageschedule.setPackageId(packageId);
							packageschedule.setTimeZone(rs.getString("time_zone"));
							return packageschedule;
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getPackageSchedule", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageSchedule", e);
			
		}
		return packageSchedule;
	}

	@Override
	public String getUploadAndExecutionStatusComments(long executionId, String uploadOrExecution,
			JdbcTemplate clientJdbcTemplate) {

		String executionComments = null;
		try {

			String sql = "";
			String columnName = "";

			if (uploadOrExecution.equals("upload")) {
				sql = sqlHelper.getSql("getUploadStatusComments");
				columnName = "upload_comments";
			} else if (uploadOrExecution.equals("execution")) {
				sql = sqlHelper.getSql("getExecutionStatusComments");
				columnName = "execution_comments";
			} else if (uploadOrExecution.equals("druidStatus")) {
				sql = sqlHelper.getSql("getDruidStatusComments");
				columnName = "druid_comments";
			}
			final String columnName1 = columnName;
			executionComments = clientJdbcTemplate.query(sql, new Object[] { executionId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								return rs.getString(columnName1);
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getUploadAndExecutionStatusComments()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getUploadAndExecutionStatusComments()", e);
			
		}
		return executionComments;

	}

	@Override
	public List<PackageExecution> getInProgressPackageExecutionList(Integer packageId,
			JdbcTemplate clientJdbcTemplate) {
		List<PackageExecution> packageDetails = null;
		try {
			String sql = sqlHelper.getSql("getInProgressPackageExecutionList");
			packageDetails = clientJdbcTemplate.query(sql, new Object[] { packageId },
					new RowMapper<PackageExecution>() {

						@Override
						public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
							PackageExecution packageExecution = new PackageExecution();
							packageExecution.setExecutionId(rs.getInt("execution_id"));
							packageExecution.setPackageId(rs.getInt("package_id"));
							packageExecution.setInitiatedFrom(rs.getString("initiated_from"));
							packageExecution.setRunType(rs.getString("run_type"));
							packageExecution.setUploadStatus(rs.getString("upload_status"));
							packageExecution.setExecutionStatus(rs.getString("execution_status"));
							packageExecution.setExecutionComments(rs.getString("upload_comments"));
							packageExecution.setExecutionComments(rs.getString("execution_comments"));
							return packageExecution;
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getInProgressPackageExecutionList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getInProgressPackageExecutionList()", e);
			
		}
		return packageDetails;

	}

	@Override
	public String getUploadStatusAndExecutionStatusComments(long executionId, JdbcTemplate clientJdbcTemplate) {

		String executionComments = null;
		try {
			String sql = sqlHelper.getSql("getUploadStatusAndExecutionStatusComments");
			executionComments = clientJdbcTemplate.query(sql, new Object[] { executionId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								return rs.getString("upload_and_execute");
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getUploadStatusAndExecutionStatusComments()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getUploadStatusAndExecutionStatusComments()", e);
			
		}
		return executionComments;

	}

	@Override
	public List<PackageExecution> getScheduledPackagesForExecution(JdbcTemplate clientJdbcTemplate) {
		List<PackageExecution> packageDetails = null;
		try {
			String sql = sqlHelper.getSql("getScheduledPackagesForExecution");
			packageDetails = clientJdbcTemplate.query(sql, new RowMapper<PackageExecution>() {

				@Override
				public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
					PackageExecution packageExecution = new PackageExecution();
					packageExecution.setExecutionId(rs.getInt("execution_id"));
					Package userPackage = new Package();
					int packageId = rs.getInt("package_id");
					userPackage.setPackageId(packageId);
					userPackage.setPackageName(rs.getString("package_name"));
					userPackage.setIsStandard(rs.getBoolean("isStandard"));
					packageExecution.setUserPackage(userPackage);
					packageExecution.setPackageId(packageId);
					packageExecution.setScheduleId(rs.getLong("schedule_id"));
					packageExecution.setUserId(rs.getString("user_id"));
					packageExecution.setUploadStatus(rs.getString("upload_status"));
					packageExecution.setUploadStartDate(rs.getString("upload_start_date"));
					packageExecution.setLastUploadedDate(rs.getString("last_uploaded_date"));
					packageExecution.setInitiatedFrom(rs.getString("initiated_from"));
					packageExecution.setRunType(rs.getString("run_type"));
					packageExecution.setDlId(rs.getInt("dl_id"));
					packageExecution.setDdlToRun(rs.getString("ddl_to_run"));
					return packageExecution;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getScheduledPackagesForExecution()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getScheduledPackagesForExecution()", e);
		}
		return packageDetails;

	}
	

	JdbcTemplate getRequiredJdbcTemplate (JdbcTemplate clientAppDbJdbcTemplate) {
		return clientAppDbJdbcTemplate != null ? clientAppDbJdbcTemplate : getJdbcTemplate();
	}

	@Override
	public int updatePackageExecutionTargetTableInfo(PackageExecution packageExecution,
			JdbcTemplate clientJdbcTemplate) {

		int insertCount = -1;
		try {
			final String sql = sqlHelper.getSql("updatePackageExecutionTargetTableInfo");
			insertCount = clientJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql);
					ps.setObject(1, packageExecution.getExecutionId());
					ps.setObject(2, packageExecution.getTagetTableName());
					ps.setObject(3, packageExecution.getExecutionStatus());
					ps.setObject(4, packageExecution.getExecutionComments());
					ps.setObject(5, packageExecution.getExecutionStartDate());
					ps.setObject(6, packageExecution.getLastExecutedDate());
					ps.setObject(7, packageExecution.getDruidStatus());
					ps.setObject(8, packageExecution.getDruidComments());
					ps.setObject(9, packageExecution.getModification().getCreatedBy());
					ps.setObject(10, packageExecution.getModification().getCreatedTime());
					return ps;
				}	 
			});
		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageExecutionTargetTableInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageExecutionTargetTableInfo()", e);
			
		}
		return insertCount;
	}

	@Override
	public List<PackageExecution> getPackageExecutionSourceMappingInfo(long executionId,
			JdbcTemplate clientJdbcTemplate) {
		List<PackageExecution> packageDetails = null;
		try {
			String sql = sqlHelper.getSql("getPackageExecutionSourceMappingInfo");
			packageDetails = clientJdbcTemplate.query(sql,new Object[]{executionId}, new RowMapper<PackageExecution>() {

				@Override
				public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
					PackageExecution packageExecution = new PackageExecution();
					packageExecution.setExecutionId(rs.getInt("execution_id"));
					packageExecution.setUploadStatus(rs.getString("upload_status"));
					packageExecution.setUploadComments(rs.getString("upload_comments"));
					packageExecution.setExecutionStatus(rs.getString("execution_status"));
					packageExecution.setExecutionComments(rs.getString("execution_comments"));
					return packageExecution;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getPackageExecutionSourceMappingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageExecutionSourceMappingInfo()", e);
		}
		return packageDetails;

	}

	@Override
	public List<PackageExecution> getPackageExecutionTargetTableInfo(long executionId,
			JdbcTemplate clientJdbcTemplate) {
		List<PackageExecution> packageDetails = null;
		try {
			String sql = sqlHelper.getSql("getPackageExecutionTargetTableInfo");
			packageDetails = clientJdbcTemplate.query(sql,new Object[]{executionId}, new RowMapper<PackageExecution>() {

				@Override
				public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
					PackageExecution packageExecution = new PackageExecution();
					packageExecution.setTagetTableName(rs.getString("table_name"));
					packageExecution.setExecutionId(rs.getInt("execution_id"));
					packageExecution.setExecutionStatus(rs.getString("execution_status"));
					packageExecution.setExecutionComments(rs.getString("execution_comments"));
					packageExecution.setExecutionStartDate(rs.getString("execution_start_date"));
					packageExecution.setLastExecutedDate(rs.getString("execution_end_date"));
					return packageExecution;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getPackageExecutionTargetTableInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageExecutionTargetTableInfo()", e);
		}
		return packageDetails;
	}
	
	@Override
	public int updateDatesForOnceRecurrence(Schedule schedule, JdbcTemplate clientJdbcTemplate) {
		int updatedCount = 0;
		try {
			String sql = sqlHelper.getSql("updateDatesForOnceRecurrence");
			updatedCount = clientJdbcTemplate.update(sql, new Object[]{schedule.getScheduleStartDate(),schedule.getScheduleStartTime(),schedule.getScheduleStartDate(),schedule.getTimeZone(),schedule.getScheduleId()});

		} catch (DataAccessException ae) {
			LOG.error("error while updateDatesForOnceRecurrence()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateDatesForOnceRecurrence()", e);
		}
		return updatedCount;
	}

	@Override
	public ClientJobExecutionParameters getClientJobExecutionParams(JdbcTemplate clientJdbcTemplate) {

		ClientJobExecutionParameters clientJobExecutionParameters = null;
		try {
			String sql = sqlHelper.getSql("getClientJobExecutionParams");
			clientJobExecutionParameters = clientJdbcTemplate.query(sql, new Object[] { },
					new ResultSetExtractor<ClientJobExecutionParameters>() {
						@Override
						public ClientJobExecutionParameters extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							if (rs != null && rs.next()) {
								ClientJobExecutionParameters clientJobExecutionParameters = new ClientJobExecutionParameters();
								clientJobExecutionParameters.setSourceTimeZone(rs.getString("source_time_zone"));
								clientJobExecutionParameters.setDestTimeZone(rs.getString("dest_time_zone"));
								clientJobExecutionParameters.setNullReplaceValues(rs.getString("null_raplace_value"));
								clientJobExecutionParameters.setCaseSensitive(rs.getBoolean("case_sensitive"));
								clientJobExecutionParameters.setInterval(rs.getInt("trial_interval"));
								return clientJobExecutionParameters;
							}
							return null;
						}
						
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getClientJobExecutionParams", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientJobExecutionParams", e);
		}
		return clientJobExecutionParameters;
	
	}

	@Override
	public boolean getDlLoadType(long executionId, JdbcTemplate clientJdbcTemplate) {

		Boolean dlLoadType = false;
		try {
			String sql = sqlHelper.getSql("getDlLoadType");
			dlLoadType = clientJdbcTemplate.query(sql, new Object[] { executionId },
					new ResultSetExtractor<Boolean>() {
						@Override
						public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								return rs.getBoolean("dl_load_type");
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getDlLoadType()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDlLoadType()", e);
			
		}
		return dlLoadType;
	}

	@Override
	public Package getPackageWithoutUserId(int packageId, JdbcTemplate clientJdbcTemplate) {
		Package userPackage = null;
		try {

			String sql = sqlHelper.getSql("getPackagewithoutUserId");
			userPackage = clientJdbcTemplate.query(sql, new Object[] { packageId },
					new ResultSetExtractor<Package>() {
						@Override
						public Package extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								Package userPackage = new Package();
								userPackage.setPackageId(rs.getInt("package_id"));
								userPackage.setPackageName(rs.getString("package_name"));
								userPackage.setDescription(rs.getString("description"));
								userPackage.setIndustry(
										new Industry(rs.getInt("industryId"), rs.getString("industryName")));
								userPackage.setIsStandard(rs.getBoolean("isStandard"));
								userPackage.setIsActive(rs.getBoolean("isActive"));
								userPackage.setIsScheduled(rs.getBoolean("isScheduled"));
								userPackage.setIsMapped(rs.getBoolean("isMapped"));
								userPackage.setScheduleStatus(rs.getString("schedule_status"));
								userPackage.setIsClientDbTables(rs.getBoolean("isClientDbTables"));
								userPackage.setTrailingMonths(rs.getInt("trailing_months"));
								Modification mo = new Modification();
								mo.setModifiedBy(rs.getString("modified_by"));
								userPackage.setModification(mo);
								userPackage.setIsAdvanced(rs.getBoolean("isAdvanced"));
								userPackage.setUserId(rs.getString("user_id"));
								Object s = rs.getObject("files_having_same_columns");
								if (s == null) {
									userPackage.setFilesHavingSameColumns(null);
								} else {
									userPackage.setFilesHavingSameColumns(rs.getBoolean("files_having_same_columns"));
								}

								return userPackage;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getPackageById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageById()", e);
			
		}
		return userPackage;
	}
	
	@Override
	public List<DDLayout> getDDlayoutListByClientId(String clientId, List<String> derivedTablesList, String userId,
			JdbcTemplate clientJdbcTemplate) {
		List<DDLayout> dDLayouts = new ArrayList<>();
		try {
			StringBuilder builder = new StringBuilder();
			derivedTablesList.forEach(value -> {
				builder.append(" find_in_set(?,table_name) or");
			});
			derivedTablesList.add(0, clientId);
			//derivedTablesList.add(1, userId);
			String sql = StringUtils.replace(sqlHelper.getSql("getDDlayoutListByClinetId"), "{tableNames}",
					builder.delete(builder.length() - 2, builder.length()).toString());
			dDLayouts = clientJdbcTemplate.query(sql, derivedTablesList.toArray(), new RowMapper<DDLayout>() {
				public DDLayout mapRow(ResultSet rs, int i) throws SQLException {
					DDLayout dDLayout = new DDLayout();
					dDLayout.setId(rs.getInt("id"));
					dDLayout.setClientId(rs.getString("client_id"));
					dDLayout.setTableName(rs.getString("table_name"));
					dDLayout.setTableDes(rs.getString("table_desc"));
					dDLayout.setTable5ructure(rs.getString("table_structure"));
					dDLayout.setSelectQry(rs.getString("select_query"));
					dDLayout.setDdlTables(rs.getString("ddl_tables"));
					return dDLayout;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getDDlayoutList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDDlayoutList()", e);
			
		}
		return dDLayouts;
	}

	@Override
	public PackageExecution getPackageExecutionInfo(Integer packageId, Integer executionId, String uploaderExecution, JdbcTemplate clientJdbcTemplate) {
		PackageExecution packageExecution = new PackageExecution();
		try{
			String sql = sqlHelper.getSql("getPackageExecutionInfo");
			packageExecution = clientJdbcTemplate.queryForObject(sql, new Object[] {packageId, executionId}, new RowMapper<PackageExecution>() {
				public PackageExecution mapRow(ResultSet rs, int i) throws SQLException{
					PackageExecution executionInfo = new PackageExecution();
					  executionInfo.setPackageId(rs.getLong("package_id"));
					  executionInfo.setExecutionId(rs.getLong("execution_id"));
					  executionInfo.setScheduleId(rs.getLong("schedule_id"));
					  executionInfo.setInitiatedFrom(rs.getString("initiated_from"));
					  executionInfo.setRunType(rs.getString("run_type"));
					  executionInfo.setUploadStatus(rs.getString("upload_status"));
					  executionInfo.setUploadComments(rs.getString("upload_comments"));
					  executionInfo.setUploadStartDate(rs.getString("upload_start_date"));
					  executionInfo.setLastUploadedDate(rs.getString("last_uploaded_date"));
					  executionInfo.setExecutionStatus(rs.getString("execution_status"));
					  executionInfo.setExecutionComments(rs.getString("execution_comments"));
					  executionInfo.setExecutionStartDate(rs.getString("exection_start_date"));
					  executionInfo.setLastExecutedDate(rs.getString("last_executed_date"));
					  executionInfo.setDruidStatus(rs.getString("druid_status"));
					  executionInfo.setDruidComments(rs.getString("druid_comments"));
					  executionInfo.setDruidStartDate(rs.getString("druid_start_date"));
					  executionInfo.setDruidEndDate(rs.getString("druid_end_date"));
					  executionInfo.setTimeZone(rs.getString("time_zone"));
					  
					return executionInfo;
				}
			});
		}catch(DataAccessException de){
			LOG.error("error while reading packageexecution infor",de);
			throw new AnvizentRuntimeException(de);
		}catch(SqlNotFoundException e){
			LOG.error("error while reading packageexecution infor",e);
			
			
		}
		return packageExecution;
	}

	@Override
	public int getIncrementalLoadCount(Integer packageId, Integer executionId, JdbcTemplate clientJdbcTemplate) {
		int noofIncrementals=0;
		try{
			String sql =sqlHelper.getSql("getIncrementaLoads");
			noofIncrementals = clientJdbcTemplate.query(sql, new Object[] {executionId}, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						Integer count =rs.getInt("incrementalLoads");
						return count;
					}
					else{
						return 0;
					}
				}
			});
		}catch (DataAccessException ae) {
			LOG.error("error while getSourceCountByPackageId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getSourceCountByPackageId()", e);
			
		}
		
		return noofIncrementals;
	}

	@Override
	public int updateIsActiveStatusIlSource(String connectionMappingId, Boolean isActiveRequired,
			JdbcTemplate clientJdbcTemplate) {
		int updateStatus = 0;
		try {
			String sql = sqlHelper.getSql("updateIsActiveStatusIlSource");
			updateStatus = clientJdbcTemplate.update(sql, new Object[] { isActiveRequired,connectionMappingId});
		} catch (DataAccessException ae) {
			LOG.error("error while updateIsActiveStatusIlSource()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateIsActiveStatusIlSource()", e);
			
		}
		return updateStatus;
	}

	@Override
	public int saveApisDataInfo(ApisDataInfo apisDataInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			
			if(apisDataInfo.getId() != 0){
				String sql = sqlHelper.getSql("updateApisDataInfo");
				save = clientAppDbJdbcTemplate.update(sql, new Object[]{
						apisDataInfo.getApiName(),
						apisDataInfo.getEndPointUrl(),
						apisDataInfo.getApiDescription(),
						apisDataInfo.getMethodType(),
						apisDataInfo.getApiQuery(),
						apisDataInfo.getActive(),
						apisDataInfo.getUserId(),
						apisDataInfo.getModification().getModifiedBy(),
						apisDataInfo.getModification().getModifiedTime(),
						apisDataInfo.getId()
						
				});
			}else{
				String sql = sqlHelper.getSql("saveApisDataInfo");
				save = clientAppDbJdbcTemplate.update(sql, new Object[]{
						apisDataInfo.getApiName(),
						apisDataInfo.getEndPointUrl(),
						apisDataInfo.getApiDescription(),
						apisDataInfo.getMethodType(),
						apisDataInfo.getApiQuery(),
						apisDataInfo.getUserId(),
						apisDataInfo.getModification().getCreatedBy(),
						apisDataInfo.getModification().getCreatedTime()
						
				});
			}
			
		} catch(DataAccessException de){
			LOG.error("error while reading saveApisDataInfo infor",de);
			throw new AnvizentRuntimeException(de);
		}catch(SqlNotFoundException e){
			LOG.error("error while reading saveApisDataInfo infor",e);
			
			
		}
		return save;
	}

	@Override
	public ApisDataInfo getApistDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		ApisDataInfo apisData = null;
		try {
			String sql = sqlHelper.getSql("getApistDetailsById");
			apisData = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<ApisDataInfo>() {
				
				@Override
				public ApisDataInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					ApisDataInfo apisDataInfo = new ApisDataInfo();
					if (rs != null && rs.next()) {
						apisDataInfo.setId(rs.getInt("id"));
						apisDataInfo.setApiName(rs.getString("apiname"));
						apisDataInfo.setEndPointUrl(rs.getString("endpoint_uri"));
						apisDataInfo.setApiDescription(rs.getString("api_description"));
						apisDataInfo.setMethodType(rs.getString("method_type"));
						apisDataInfo.setApiQuery(rs.getString("api_query"));
						apisDataInfo.setUserId(rs.getInt("user_id"));
						Modification modification = new Modification();
						modification.setCreatedBy(rs.getString("created_by"));
						modification.setCreatedTime(rs.getString("created_date"));
						apisDataInfo.setModification(modification);
						apisDataInfo.setActive(rs.getBoolean("is_active"));
						return apisDataInfo;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getApisDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getApisDetails()", e);
			
		}
		
		return apisData;
	}
	

	@Override
	public ApisDataInfo getApistDetailsByEndPointName(String endPointName, JdbcTemplate clientAppDbJdbcTemplate) {
		ApisDataInfo apisData = null;
		try {
			String sql = sqlHelper.getSql("getApistDetailsByEndPointName");
			apisData = clientAppDbJdbcTemplate.query(sql, new Object[] { endPointName }, new ResultSetExtractor<ApisDataInfo>() {
				
				@Override
				public ApisDataInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					ApisDataInfo apisDataInfo = new ApisDataInfo();
					if (rs != null && rs.next()) {
						apisDataInfo.setId(rs.getInt("id"));
						apisDataInfo.setApiName(rs.getString("apiname"));
						apisDataInfo.setEndPointUrl(rs.getString("endpoint_uri"));
						apisDataInfo.setApiDescription(rs.getString("api_description"));
						apisDataInfo.setMethodType(rs.getString("method_type"));
						apisDataInfo.setApiQuery(rs.getString("api_query"));
						apisDataInfo.setUserId(rs.getInt("user_id"));
						Modification modification = new Modification();
						modification.setCreatedBy(rs.getString("created_by"));
						modification.setCreatedTime(rs.getString("created_date"));
						apisDataInfo.setActive(rs.getBoolean("is_active"));
						apisDataInfo.setModification(modification);
						return apisDataInfo;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getApisDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getApisDetails()", e);
			
		}
		
		return apisData;
	}

	@Override
	public List<ApisDataInfo> getApisDetails(JdbcTemplate clientAppDbJdbcTemplate) {
		List<ApisDataInfo> apisData= null;
		try {
			String sql = sqlHelper.getSql("getApisDetails");
			apisData = clientAppDbJdbcTemplate.query(sql,new RowMapper<ApisDataInfo>() {
				@Override
				public ApisDataInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					ApisDataInfo apisDataList = new ApisDataInfo();
					
					
					apisDataList.setApiName(rs.getString("apiname"));
					apisDataList.setEndPointUrl(rs.getString("endpoint_uri"));
					apisDataList.setMethodType(rs.getString("method_type"));
					//apisDataList.setApiQuery(rs.getString("api_query"));
					apisDataList.setUserId(rs.getInt("user_id"));
					apisDataList.setId(rs.getInt("id"));
					apisDataList.setApiDescription(rs.getString("api_description"));
					apisDataList.setActive(rs.getBoolean("is_active"));
					/*Modification modification = new Modification();
					modification.setCreatedBy(rs.getString("created_by"));
					modification.setCreatedTime(rs.getString("created_date"));
					apisDataList.setModification(modification);*/
					
					
					return apisDataList;
				}
			});
			
		} catch (DataAccessException ae) {
			LOG.error("error while getApisDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getApisDetails()", e);
			
		}
		return apisData;
	}

	@Override
	public List<DDLayout> getDDlayoutListByIds(String clientId, Set<Integer> ddLTableSet, String userId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<DDLayout> dDLayouts = new ArrayList<>();
		try {
			StringBuilder builder = new StringBuilder();
			ddLTableSet.forEach(value -> {
				builder.append("?,");
			});
			List<Integer> paramObj = new ArrayList<>(ddLTableSet);
			
			paramObj.add(0, Integer.valueOf(clientId));
			paramObj.add(1, Integer.valueOf(userId));
			String sql = StringUtils.replace(sqlHelper.getSql("getDDlayoutListById"), "{ddlIds}", builder.deleteCharAt(builder.length() - 1).toString());
			dDLayouts = clientAppDbJdbcTemplate.query(sql,  paramObj.toArray(), new RowMapper<DDLayout>() {
				public DDLayout mapRow(ResultSet rs, int i) throws SQLException {
					DDLayout dDLayout = new DDLayout();
					dDLayout.setId(rs.getInt("id"));
					dDLayout.setClientId(rs.getString("client_id"));
					dDLayout.setTableName(rs.getString("table_name"));
					dDLayout.setTableDes(rs.getString("table_desc"));
					dDLayout.setTable5ructure(rs.getString("table_structure"));
					dDLayout.setSelectQry(rs.getString("select_query"));
					dDLayout.setDdlTables(rs.getString("ddl_tables"));
					return dDLayout;
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getDDlayoutList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDDlayoutList()", e);
		}
		return dDLayouts;
	}
	
	
	@Override
	public List<JobResult> getJobResultsForCrossReference(int ilId, String clientId, String clientStagingSchemaName,
			JdbcTemplate clientJdbcTemplate) {

		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_xref\\_" + ilId + "\\_%";
			String sql = sqlHelper.getSql("getJobResultsForHistoricalLoad");

			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam }, new RowMapper<JobResult>() {
				public JobResult mapRow(ResultSet rs, int i) throws SQLException {
					JobResult jobresult = new JobResult();
					/* jobresult.setPackageId(Integer.parseInt(packageId)); */
					/* jobresult.setUserId(userId); */
					jobresult.setIlId(ilId);
					jobresult.setBatchId(rs.getString("BATCH_ID"));
					jobresult.setJobName(rs.getString("JOB_NAME"));
					jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
					jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
					jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
					jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
					jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
					jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
					jobresult.setErrorRowsCount(
							rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");

					return jobresult;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getJobResultsForCrossReference()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getJobResultsForCrossReference()", e);
			
		}

		return resultList;
	}

	@Override
	public List<JobResult> getJobResultsForCrossReferenceByDate(int ilId, String clientId,
			String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate) {

		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_xref\\_" + ilId + "\\_%";
			String sql = sqlHelper.getSql("getJobResultsForHistoricalLoadByDate");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam, fromDate, toDate },
					new RowMapper<JobResult>() {
						public JobResult mapRow(ResultSet rs, int i) throws SQLException {
							JobResult jobresult = new JobResult();
							/*
							 * jobresult.setPackageId(Integer.parseInt(packageId
							 * ));
							 */
							jobresult.setIlId(ilId);
							/* jobresult.setUserId(userId); */
							jobresult.setBatchId(rs.getString("BATCH_ID"));
							jobresult.setJobName(rs.getString("JOB_NAME"));
							jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
							jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
							jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
							jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
							jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
							jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
							jobresult.setErrorRowsCount(
									rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");

							return jobresult;
						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getJobResultsForCrossReferenceByDate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getJobResultsForCrossReferenceByDate()", e);
			
		}

		return resultList;
	}

	@Override
	public Table getTempTableByWebServiceConnectionId(Integer webServiceConnectionId,
			JdbcTemplate clientAppdbJdbcTemplate) {
		Table tempTable = null;

		try {
			String sql = sqlHelper.getSql("getTempTablebyWebserviceConnectionId");
			tempTable = clientAppdbJdbcTemplate.query(sql, new Object[] { webServiceConnectionId }, new ResultSetExtractor<Table>() {
						@Override
						public Table extractData(ResultSet rs) throws SQLException, DataAccessException {
						   if (rs != null && rs.next())
							 {
								Table table = new Table();
								table.setTableName(rs.getString("temp_table_name"));
								table.setIlId(rs.getInt("il_id"));
								table.setTableStructure(rs.getString("temp_table_structure"));
								WebServiceJoin webServiceJoin = new WebServiceJoin();
								webServiceJoin.setWsMappingId(rs.getInt("id"));
								webServiceJoin.setWebServiceUrl(rs.getString("web_service_url"));
								webServiceJoin.setWebServiceApiName(rs.getString("web_service_api_name"));
								webServiceJoin.setResponseObjectName(rs.getString("web_service_response_object_name"));
								webServiceJoin.setResponseColumnObjectName(rs.getString("response_column_object_name"));
								webServiceJoin.setWebServiceMethodType(rs.getString("web_service_method_type"));
								table.setWebServiceJoin(webServiceJoin);
								return table;
						    }
							else {
								return null;
							}
						}
					});
		} catch (Exception e) {
			LOG.error("Error while getting the temp tables .. ", e);
		}
		return tempTable;
	}

	@Override
	public boolean cloneHistoricalLoadDetailsById(Integer loadId, JdbcTemplate clientAppDbJdbcTemplate) {
		boolean cloneStatus = false;
		try {
			String sql = sqlHelper.getSql("cloneHistoricalLoadDetailsById");
			int updatedCount = clientAppDbJdbcTemplate.update(sql, new Object[] { loadId });
			
			if (updatedCount == 1) {
				cloneStatus = true;
			}

		} catch (DuplicateKeyException ae) {
			LOG.error("error while cloneHistoricalLoadDetailsById()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOG.error("error while cloneHistoricalLoadDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while cloneHistoricalLoadDetailsById()", e);
			
		}
		return cloneStatus;
	}

	@Override
	public int updatePackageName(Integer packageId, String packageName, JdbcTemplate clientAppDbJdbcTemplate) {
		int updateStatus = 0;
		try {
			String sql = sqlHelper.getSql("updatePackageName");
			updateStatus = clientAppDbJdbcTemplate.update(sql, new Object[] {packageName,packageId});
		} catch (DuplicateKeyException ae) {
			LOG.error("error while updatePackageName()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageName()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageName()", e);
			
		}
		return updateStatus;
	}

	@Override
	public int updatePackageAdvancedField(Integer packageId, boolean status, JdbcTemplate clientAppDbJdbcTemplate) {
		int updateStatus = 0;
		try {
			String sql = sqlHelper.getSql("updatePackageAdvancedField");
			updateStatus = clientAppDbJdbcTemplate.update(sql, new Object[] {status,packageId});
		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageAdvancedField()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageAdvancedField()", e);
			
		}
		return updateStatus;
	}
	

	@Override
	public List<JobResult> getJobResultsForCrossReferenceByConditionId(int conditionId, int ilId, String clientId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate)
	{

		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_xref\\_" + ilId + "\\_" + conditionId + "\\_%";
			String sql = sqlHelper.getSql("getJobResultsForHistoricalLoad");

			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam }, new RowMapper<JobResult>() {
				public JobResult mapRow(ResultSet rs, int i) throws SQLException {
					JobResult jobresult = new JobResult();
					jobresult.setIlId(ilId);
					jobresult.setXrefConditionId(conditionId);
					jobresult.setBatchId(rs.getString("BATCH_ID"));
					jobresult.setJobName(rs.getString("JOB_NAME"));
					jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
					jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
					jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
					jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
					jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
					jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
					jobresult.setErrorRowsCount(
							rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");

					return jobresult;
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getJobResultsForCrossReference()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getJobResultsForCrossReference()", e);
		}

 		return resultList;
	}

	@Override
	public List<JobResult> getJobResultsForCrossReferenceByDateById(Integer conditionId, Integer ilId, String clientId, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate)
	{

		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_xref\\_" + ilId + "\\_" + conditionId + "\\_%";
			String sql = sqlHelper.getSql("getJobResultsForHistoricalLoadByDate");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam, fromDate, toDate },
					new RowMapper<JobResult>() {
						public JobResult mapRow(ResultSet rs, int i) throws SQLException {
							JobResult jobresult = new JobResult();
							jobresult.setIlId(ilId);
							jobresult.setXrefConditionId(conditionId);
							jobresult.setBatchId(rs.getString("BATCH_ID"));
							jobresult.setJobName(rs.getString("JOB_NAME"));
							jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
							jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
							jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
							jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
							jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
							jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
							jobresult.setErrorRowsCount(
									rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");

							return jobresult;
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getJobResultsForCrossReferenceByDate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getJobResultsForCrossReferenceByDate()", e);	
			
		}

		return resultList;
	}

	@Override
	public void getUpdatedTargetMappedQuery(List<String> query, JdbcTemplate clientJdbcTemplate) {
		try {
			for (String execQuery : query) {
				   if(StringUtils.isNotEmpty(execQuery))
					   clientJdbcTemplate.execute(execQuery);
			}
			 
		} catch (DataAccessException ae) {
			LOG.error("error while getUpdatedTargetMappedQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		}
	}

	@Override
	public void deleteCustomTempTableByMappingId(Integer packageId, String mappingId, String clientId, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate)
	{
		try {
			List<Table> tempTables = getCustomFileTempTableMappingsByMappingId(clientId, packageId, mappingId, clientAppDbJdbcTemplate);
			if (tempTables != null && tempTables.size() > 0) {
				String queryForDeleteCustomTempTableInfo = sqlHelper.getSql("deleteCustomTempTableInfoByMappingId");

				for (int i = 0; i < tempTables.size(); i++) {
					try {
						clientJdbcTemplate.execute("drop table if exists `" + tempTables.get(i).getTableName()+"`");
					} catch (Exception e) {
						logger.error("Unable to drop " + tempTables.get(i).getTableName() + " table");
					}
				}
				clientAppDbJdbcTemplate.update(queryForDeleteCustomTempTableInfo, packageId, clientId, mappingId);
			}
		}catch(DataAccessException ae) {
			LOG.error("error while deleteCustomTempTableByMappingId()", ae);
			throw new AnvizentRuntimeException(ae);
		}catch (SqlNotFoundException e) {
			LOG.error("error while deleteCustomTempTableByMappingId()", e);	
			
		}
		
	}

	private List<Table> getCustomFileTempTableMappingsByMappingId(String clientId, Integer packageId, String mappingId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<Table> tables = null;
		try {
			String sql = sqlHelper.getSql("getCustomFileTempTableMappingsByMappingId");
			tables = clientAppDbJdbcTemplate.query(sql, new Object[] {packageId, clientId, mappingId }, new RowMapper<Table>() {
				@Override
				public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs == null)
						return null;
					Table table = new Table();
					table.setTableId(rs.getInt("id"));
					table.setTableName(rs.getString("temp_table_name"));

					return table;
				}
			});
		} catch (DataAccessException ae) {
			logger.error("error while getCustomFileTempTableMappingsByMappingId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while getCustomFileTempTableMappingsByMappingId()", e);
		}

		return tables;
	}

	@Override
	public List<ELTClusterLogsInfo> getEltInitiateInfo(int executionId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ELTClusterLogsInfo> eltInitiateRespose = null;
		try {
			String sql = sqlHelper.getSql("getEltInitiateInfo");
			eltInitiateRespose = clientAppDbJdbcTemplate.query(sql, new Object[] {executionId}, new RowMapper<ELTClusterLogsInfo>() {
				@Override
				public ELTClusterLogsInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs == null)
						return null;
					ELTClusterLogsInfo eltInitiateInfo = new ELTClusterLogsInfo();
					eltInitiateInfo.setExecutionId(rs.getLong("execution_id"));
					eltInitiateInfo.setPackageId(rs.getLong("package_id"));
					eltInitiateInfo.setScheduleId(rs.getLong("schedule_id"));
					eltInitiateInfo.setDlId(rs.getInt("dl_id"));
					eltInitiateInfo.setIlId(rs.getInt("il_id"));
					eltInitiateInfo.setEltInitiateId(rs.getLong("initiated_id"));
					eltInitiateInfo.setEltInitiateObject(rs.getString("initiated_object"));

					return eltInitiateInfo;
				}
			});
		} catch (DataAccessException ae) {
			logger.error("error while getEltInitiateInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while getEltInitiateInfo()", e);
		}

		return eltInitiateRespose;
	}

	@Override
	public void editTargetMappedQuery(String query, JdbcTemplate clientJdbcTemplate) {
		try {
			if(StringUtils.isNotEmpty(query))
				   clientJdbcTemplate.execute(query);
		} catch (Exception e) {
				LOG.error("error while getUpdatedTargetMappedQuery()", e);
				throw new AnvizentRuntimeException(e);
		}
		
	}

	@Override
	public List<DataTypesInfo> getDataTypesList()
	{
		List<DataTypesInfo> dataTypesList = null;
		try {

			String sql = sqlHelper.getSql("getDataTypesList");
			dataTypesList = getJdbcTemplate().query(sql, new RowMapper<DataTypesInfo>() {
				public DataTypesInfo mapRow(ResultSet rs, int i) throws SQLException {
					DataTypesInfo dataTypesInfo = new DataTypesInfo();
					dataTypesInfo.setId(rs.getInt("id"));
					dataTypesInfo.setDataTypeName(rs.getString("datatypename"));
					dataTypesInfo.setRange(rs.getString("datatypesize"));
					return dataTypesInfo;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getDataTypesList", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getDataTypesList", e);
			e.printStackTrace();
		}

		return dataTypesList;
	}

	@Override
	public int getPackageExecutionCount(int packageId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		int count = 0;
		try {
			String sql = sqlHelper.getSql("getPackageExecutionCount");
			
			count = clientAppDbJdbcTemplate.queryForObject(sql, new Object[] { packageId },
					new RowMapper<Integer>() {
						@Override
						public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
							if (rs == null)
								return null;
							return rs.getInt(1);
						}
					});
			
		} catch (DataAccessException ae) {
			LOG.error("error while getPackageExecutionCount()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageExecutionCount()", e);
		}
		return count;
	}

	@Override
	public List<PackageExecution> getPackageExecutionByPagination(Integer packageId, int offset, int limit, JdbcTemplate clientJdbctemplate)
	{
		List<PackageExecution> packageDetails = null;
		try {
			String sql = sqlHelper.getSql("getPackageExecutionByPagination");
			packageDetails = clientJdbctemplate.query(sql, new Object[] { packageId,limit ,offset },
					new RowMapper<PackageExecution>() {

						@Override
						public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
							PackageExecution packageExecution = new PackageExecution();
							packageExecution.setExecutionId(rs.getInt("execution_id"));
							packageExecution.setPackageId(rs.getInt("package_id"));
							packageExecution.setInitiatedFrom(rs.getString("initiated_from"));
							packageExecution.setRunType(rs.getString("run_type"));
							packageExecution.setUploadStatus(rs.getString("upload_status"));
							packageExecution.setExecutionStatus(rs.getString("execution_status"));
							packageExecution.setDruidStatus(rs.getString("druid_status"));
							packageExecution.setExecutionStartDate(rs.getString("exection_start_date"));
							packageExecution.setLastExecutedDate(rs.getString("last_executed_date"));
							packageExecution.setUploadStartDate(rs.getString("upload_start_date"));
							packageExecution.setLastUploadedDate(rs.getString("last_uploaded_date")); 
							packageExecution.setDruidStartDate(rs.getString("druid_start_date"));
							packageExecution.setDruidEndDate(rs.getString("druid_end_date"));
							return packageExecution;
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getPackageExecutionByPagination()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageExecutionByPagination()", e);
			
		}
		return packageDetails;

	}
	@Override
	public int updateWsDataSourceDetails(Integer mappingId, String dataSouceName, String dataSouceNameOther, Integer packageId, String wsApiUrl, JdbcTemplate clientJdbctemplate)
	{
		int i = -1;
		try {
			String updateDataSourceDetailsSql = sqlHelper.getSql("updateDataSourceDetails");
			String updateWsDataSourceDetailsSql = sqlHelper.getSql("updateWsDataSourceDetails");
			i = clientJdbctemplate.update(updateDataSourceDetailsSql, new Object[] { dataSouceName ,mappingId, packageId });
            if( i!= 0){
            	i = clientJdbctemplate.update(updateWsDataSourceDetailsSql, new Object[] { wsApiUrl ,mappingId, packageId });
            }
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateWsDataSourceDetails()", e);
			
		}
		return i;
	}

	@Override
	public int updateWsJoinDataSourceDetails(Integer mappingId,int wsMappingId, String dataSouceName, String dataSouceNameOther, Integer packageId, String wsApiUrl, JdbcTemplate clientJdbctemplate)
	{
		int i = -1;
		try {
			String updateWsDataSourceDetailsSql = sqlHelper.getSql("updateWsJoinDataSourceDetails");
            i = clientJdbctemplate.update(updateWsDataSourceDetailsSql, new Object[] { wsApiUrl ,mappingId, packageId ,wsMappingId});
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateWsJoinDataSourceDetails()", e);
			
		}
		return i;
	}

	@Override
	public String getWsSourceMappingIdsByMappingId(int mappindId, Integer packageId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		String ilEpicorQuery = null;
		try {
			String sql = sqlHelper.getSql("getWsSourceMappingIdsByMappingId");
			ilEpicorQuery = clientAppDbJdbcTemplate.query(sql, new Object[] { mappindId, packageId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String s = rs.getString("procedure_parameters");
								return s;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getWsSourceMappingIdsByMappingId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getWsSourceMappingIdsByMappingId()", e);
			
		}

		return ilEpicorQuery;

	}

	@Override
	public int updateWsJoinUrlAtPackageSourceMapping(int mappingId, Integer packageId, String wsJoinUrl, JdbcTemplate clientAppDbJdbcTemplate)
	{
		int i = -1;
		try {
			String updateWsDataSourceDetailsSql = sqlHelper.getSql("updateWsJoinUrlAtPackageSourceMapping");
            i = clientAppDbJdbcTemplate.update(updateWsDataSourceDetailsSql, new Object[] { wsJoinUrl ,mappingId, packageId });
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateWsJoinUrlAtPackageSourceMapping()", e);
			
		}
		return i;
	}

	@Override
	public  String  getSslFileNamesByConId(int conId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		String fileNames = null;
		try {
			String sql = sqlHelper.getSql("getSslFileNamesByConId");
			fileNames = clientAppDbJdbcTemplate.query(sql, new Object[] { conId },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								String s = rs.getString("ssl_trust_key_store_file_paths");
								return s;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getSslFileNamesByConId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getSslFileNamesByConId()", e);
			
		}
		return fileNames;
	}
	
}