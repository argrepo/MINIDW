/**
 * 
 */
package com.datamodel.anvizent.service.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ParseCSV.DBDataOperations;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.FileInfo;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;
import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.WebServiceJoin;

/**
 * @author srinu.chinka
 *
 */
@Service
public class FileDaoImpl  implements FileDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileDaoImpl.class);

	private SqlHelper sqlHelper;

	public FileDaoImpl() {
		try {
			this.sqlHelper = new SqlHelper(FileDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating FileDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public boolean insertFileColumnDetails(FileInfo fileInfo , JdbcTemplate clientJdbcTemplate) {
		boolean status = false;

		try {

			String sql = sqlHelper.getSql("insertFileColumnDetails");

			KeyHolder holder = new GeneratedKeyHolder();

			int count = clientJdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

					PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					int index = 1;
					pstmt.setString(index++, fileInfo.getClientid());
					pstmt.setObject(index++, fileInfo.getPackageId());
					pstmt.setObject(index++, fileInfo.getFilePath());
					pstmt.setObject(index++, fileInfo.getSourceFileInfoId());
					pstmt.setString(index++, fileInfo.getFileType());
					pstmt.setString(index++, fileInfo.getDelimeter());
					pstmt.setBoolean(index++, fileInfo.getIsFirstRowHasColumnNames());
					pstmt.setString(index++, fileInfo.getFileHeaders());
					pstmt.setString(index++, fileInfo.getModification().getCreatedBy());
					pstmt.setObject(index++, fileInfo.getModification().getCreatedTime());
					pstmt.setObject(index++, fileInfo.getIsTempTableCreated());
					pstmt.setObject(index++, fileInfo.getFileColumnDataTypes());

					return pstmt;
				}
			}, holder);

			status = count > 0;

			if (status) {
				fileInfo.setFileId(holder.getKey().intValue());
			}

		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while insertFileColumnDetails()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while insertFileColumnDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while insertFileColumnDetails()", e);
			
		}

		return status;
	}

	@Override
	public List<String> getFileHeaderColumns(String clientId, String packageId, String filename, String mappingid, JdbcTemplate clientJdbcTemplate){

		List<String> columns = null;


		try {
			String sql = sqlHelper.getSql("getFileHeaderColumns");

			LOGGER.debug("executing query : {} ", sql);

			String cols = clientJdbcTemplate.query(sql, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					String str = null;
					if (rs != null && rs.next()) {
						str = rs.getString(1);
					}
					return str;
				}
			}, Integer.parseInt(clientId), Integer.parseInt(packageId));

			if (CommonUtils.isNotEmpty(cols)) {
				String[] colsarr = cols.split(",");
				columns = Arrays.asList(colsarr);
			}

		} catch (DataAccessException ae) {
			LOGGER.error("error while getFileHeaderColumns()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getFileHeaderColumns()", e);
			
		}
		return columns;
	}
	
	
	@Override
	public boolean updateFileHeaderByFileId(String headers, String userId, int packageId, int fileId,
			JdbcTemplate clientJdbcTemplate) {
		

		try {

			String sql = sqlHelper.getSql("updateFileHeaderByFileId");

			LOGGER.debug("executing query : {} ", sql);

			int update = clientJdbcTemplate.update(sql, new Object[] { headers, userId, packageId, fileId});

			if (update > 0) {
				return true;
			}
		} catch (DataAccessException ae) {
			LOGGER.error("error while getFileInfoForCustomPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getFileInfoForCustomPackage()", e);
		}
		
		return false;
	}
	

	@Override
	public Map<String, Object> getFileInfoForCustomPackage(String clientId, String packageId, JdbcTemplate clientJdbcTemplate){

		Map<String, Object> data = null;

		try {

			String sql = sqlHelper.getSql("getFileInfoForCustomPackage");

			LOGGER.debug("executing query : {} ", sql);

			data = clientJdbcTemplate.queryForMap(sql, Integer.parseInt(clientId), Integer.parseInt(packageId));

		} catch (DataAccessException ae) {
			LOGGER.error("error while getFileInfoForCustomPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getFileInfoForCustomPackage()", e);
			
		}

		return data;
	}

	@Override
	public List<FileInfo> getFileInfoByPackage(String clientId, int packageId, JdbcTemplate clientJdbcTemplate){
		List<FileInfo> fileList = null;
		try {
			String sql = sqlHelper.getSql("getFileInfoByPackage");
			fileList = clientJdbcTemplate.query(sql, new Object[] { clientId, packageId }, new RowMapper<FileInfo>() {
				public FileInfo mapRow(ResultSet rs, int i) throws SQLException {
					FileInfo fileInfo = new FileInfo();
					fileInfo.setFileId(rs.getInt("id"));
					fileInfo.setClientid(rs.getString("clientId"));
					fileInfo.setPackageId(rs.getInt("packageId"));
					fileInfo.setFilePath(rs.getString("file_path"));
					fileInfo.setFileType(rs.getString("file_type"));
					fileInfo.setDelimeter(rs.getString("delimeter"));
					fileInfo.setIsFirstRowHasColumnNames(rs.getBoolean("is_first_row_has_column_names"));
					fileInfo.setFileHeaders(rs.getString("file_headers"));
					fileInfo.setSelectedFileHeaders(rs.getString("selected_file_headers"));
					fileInfo.setIsTempTableCreated(rs.getBoolean("is_temp_table_created"));
					fileInfo.setFileColumnDataTypes(rs.getString("column_datatypes"));
					fileInfo.setSourceFileInfoId(rs.getInt("source_file_info_id"));
					return fileInfo;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getFileInfoByPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getFileInfoByPackage()", e);
			
		}

		return fileList;
	}

	@Override
	public FileInfo getFileInfoByFileId(String clientId, String packageId, String fileId, JdbcTemplate clientJdbcTemplate){

		FileInfo fileInfo = null;

		try {
			String sql = sqlHelper.getSql("getFileInfoByFileId");

			clientJdbcTemplate.query(sql, new Object[] { clientId, packageId, fileId }, new ResultSetExtractor<FileInfo>() {

				@Override
				public FileInfo extractData(ResultSet rs) throws SQLException, DataAccessException {

					if (rs.next()) {
						FileInfo fileInfo = new FileInfo();
						fileInfo.setFileId(rs.getInt("id"));
						fileInfo.setClientid(rs.getString("clientId"));
						fileInfo.setPackageId(rs.getInt("packageId"));
						fileInfo.setFilePath(rs.getString("file_path"));
						fileInfo.setFileType(rs.getString("file_type"));
						fileInfo.setDelimeter(rs.getString("delimeter"));
						fileInfo.setIsFirstRowHasColumnNames(rs.getBoolean("is_first_row_has_column_names"));
						fileInfo.setFileHeaders(rs.getString("file_headers"));
						fileInfo.setSelectedFileHeaders(rs.getString("selected_file_headers"));

						return fileInfo;
					}

					return null;
				}

			});

		} catch (Exception e) {
			LOGGER.error("error while getting file name : ", e);
		}

		return fileInfo;
	}

	@Override
	public boolean insertTempTableMapping(FileInfo fileInfo, Table table, Modification modification, JdbcTemplate clientJdbcTemplate){

		try {

			String sql = sqlHelper.getSql("insertTempTableMapping");

			LOGGER.debug("SQL Query : {}", sql);

			int count = clientJdbcTemplate.update(sql, new Object[] { fileInfo.getFileId(), fileInfo.getPackageId(), fileInfo.getClientid(),
					table.getTableName(), false, modification.getCreatedBy(), modification.getCreatedTime(), fileInfo.getIlMappingId()

			});

			return count > 0;

		} catch (Exception e) {
			LOGGER.error("Error while inserting temp table mapping details ", e);
		}

		return false;
	}

	@Override
	public List<Table> getCustomTempTables(String clientId, String packageId, JdbcTemplate clientJdbcTemplate){

		List<Table> tables = null;

		try {
			String sql = sqlHelper.getSql("getCustomTempTables");

			tables = clientJdbcTemplate.query(sql, new Object[] { clientId, packageId }, new RowMapper<Table>() {

				@Override
				public Table mapRow(ResultSet rs, int rowNum) throws SQLException {

					try {

						if (rs == null) {
							return null;
						}

						Table table = new Table();

						table.setTableName(rs.getString("temp_table_name"));
						table.setIsPrimaryTable(rs.getBoolean("is_primary_table"));

						String filename = rs.getString("file_path");
						filename = FilenameUtils.getName(filename);
						try {
							filename = EncryptionServiceImpl.getInstance().decrypt(filename);
						} catch (Exception e) {
						}

						table.setFileName(filename);

						String fileHeaders = rs.getString("file_headers");

						List<Column> columns = new ArrayList<>();
						Column column = null;

						String[] headers = null;

						if (rs.getString("file_type").equals(Constants.FileType.CSV)) {
							headers = fileHeaders.split(rs.getString("delimeter"));
						} else if (rs.getString("file_type").equals(Constants.FileType.XLS)) {
							headers = fileHeaders.split(",");
						} else if (rs.getString("file_type").equals(Constants.FileType.XLSX)) {
							headers = fileHeaders.split(",");
						}
						if ( headers == null) {
							throw new Exception();
						}

						for (String header : headers) {
							column = new Column();

							column.setColumnName(header);
							column.setColumnSize("500");
							column.setDataType("VARCHAR");

							columns.add(column);
						}

						table.setColumns(columns);

						return table;
					} catch (Exception e) {
						LOGGER.error("Error while converting database row to table object ", e);
					}

					return null;
				}

			});

		} catch (Exception e) {
			LOGGER.error("Error while getting the temp tables .. ", e);
		}

		return tables;
	}
	
	@Override
	public Table getCustomTempTableByMappingId(String clientId, String packageId, int mappingId, JdbcTemplate clientJdbcTemplate){

		Table table = null;

		try {
			String sql = sqlHelper.getSql("getCustomTempTableByMappingId");

			table = clientJdbcTemplate.query(sql, new Object[] { clientId, packageId, mappingId }, new ResultSetExtractor<Table>() {

				@Override
				public Table extractData(ResultSet rs) throws SQLException {

					try {

						if (rs.next()) {
							Table table = new Table();
							table.setFileId(rs.getInt("fileId"));
							table.setTableName(rs.getString("temp_table_name"));
							table.setIsPrimaryTable(rs.getBoolean("is_primary_table"));

							String filename = rs.getString("file_path");
							filename = FilenameUtils.getName(filename);
							try {
								filename = EncryptionServiceImpl.getInstance().decrypt(filename);
							} catch (Exception e) {
							}

							table.setFileName(filename);

							String fileHeaders = rs.getString("file_headers");

							List<Column> columns = new ArrayList<>();
							Column column = null;

							String[] headers = null;

							if (rs.getString("file_type").equals(Constants.FileType.CSV)) {
								headers = fileHeaders.split(rs.getString("delimeter"));
							} else if (rs.getString("file_type").equals(Constants.FileType.XLS)) {
								headers = fileHeaders.split(",");
							} else if (rs.getString("file_type").equals(Constants.FileType.XLSX)) {
								headers = fileHeaders.split(",");
							}
							if ( headers == null) {
								throw new Exception();
							}

							for (String header : headers) {
								column = new Column();

								column.setColumnName(header);
								column.setColumnSize("500");
								column.setDataType("VARCHAR");

								columns.add(column);
							}

							table.setColumns(columns);

							return table;
						}

						
					} catch (Exception e) {
						LOGGER.error("Error while converting database row to table object ", e);
					}

					return null;
				}

			});

		} catch (Exception e) {
			LOGGER.error("Error while getting the temp tables .. ", e);
		}

		return table;
	}

	@Override
	public boolean validateCustomTempTablesQuery(String clientId, String packageId, String industryId, String query, JdbcTemplate clientJdbcTemplate) {

		try {
			if (StringUtils.isEmpty(query))
				return false;

			clientJdbcTemplate.execute(query);
			return true;
		} catch (Exception e) {
			LOGGER.error("error while validating custom table query ", e.getMessage());
		}
		return false;
	}

	@Override
	public boolean saveCustomTempTablesQuery(ClientData clientData, JdbcTemplate clientJdbcTemplate){

		try {
			String sql = sqlHelper.getSql("saveCustomTempTablesQuery");

			int count = clientJdbcTemplate.update(sql,
					new Object[] { clientData.getUserId(), clientData.getUserPackage().getPackageId(), clientData.getIlConnectionMapping().getiLquery(),
							clientData.getModification().getCreatedBy(), clientData.getModification().getCreatedTime() });

			return count > 0;

		} catch (Exception e) {
			LOGGER.error("Error while inserting temp tables query into database ...");
		}

		return false;
	}

	@Override
	public Map<String, Object> processCustomTargetTableQuery(ClientData clientData, String clientStagingSchema, JdbcTemplate clientJdbcTemplate) {

		Map<String, Object> processedData = new HashMap<>();
		List<Table> tables = null;
		try {

			String tempQuery = sqlHelper.getSql("getCustomTempQuery");

			String clientId = clientData.getUserId(); 
			String packageId = clientData.getUserPackage().getPackageId() + "";

			String query = clientJdbcTemplate.queryForObject(tempQuery, new Object[] { clientId, packageId }, new RowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {

					try {
						if (rs != null) {
							return rs.getString(1);
						}
					} catch (Exception e) {
						LOGGER.error("", e);
					}

					return null;
				}

			});

			if (StringUtils.isNotEmpty(query)) {

				tables = getCustomTempTables(clientId, packageId , clientJdbcTemplate);

				for (Table t : tables) {
					query = query.replaceAll(t.getTableName().trim(), clientStagingSchema + "." + t.getTableName());
				}

				LOGGER.debug("query : {} ", query);

				Table targetTable = clientData.getUserPackage().getTable();

				processedData.put("totalRecords", 0);
				processedData.put("successRecords", 0);
				processedData.put("failedRecords", 0);

				StringBuilder insertScript = new StringBuilder();

				insertScript.append("INSERT INTO ").append(targetTable.getTableName()).append("(");

				List<Column> columns = targetTable.getColumns();

				int i = 1, length = columns.size();

				for (Column column : columns) {
					insertScript.append("`").append(column.getColumnName()).append("`");

					if (i < length) {
						insertScript.append(", ");
					}

					i++;
				}

				insertScript.append(") \n ");

				insertScript.append(" VALUES (");

				String insertQuery = insertScript.toString();

				LOGGER.debug("insert Query : {} ", insertQuery);

				List<String> queries = new ArrayList<>();

				clientJdbcTemplate.query(query, new RowCallbackHandler() {

					@Override
					public void processRow(ResultSet rs) throws SQLException {

						if (rs != null) {
							processedData.put("totalRecords", ((Integer) processedData.get("totalRecords")) + 1);

							Object[] args = new Object[length];

							for (int i = 0; i < length; i++) {
								args[i] = rs.getString(i + 1);
							}

							try {
								int columnsLength = columns.size();
								StringBuilder query = new StringBuilder(insertQuery);
								for (int i = 0; i < columnsLength; i++) {

									String dataType = columns.get(i).getDataType();
									Object data = args[i];

									if (StringUtils.isEmpty((String) data)) {
										query.append("coalesce(null,DEFAULT(").append(columns.get(i).getColumnName()).append(")) ");
									} else {
										if ("text".equalsIgnoreCase(dataType) || "varchar".equalsIgnoreCase(dataType)) {
											data = ((String) data).replaceAll("'", "''");
											query.append("'").append(data).append("'");
										} else if ("datetime".equalsIgnoreCase(dataType)) {
											query.append("'").append(data).append("'");
										} else {
											if (NumberUtils.isNumber(data.toString()) || "bit".equalsIgnoreCase(dataType)) {
												query.append(data);
											} else {
												data = data.toString().replaceAll("'", "''");
												query.append("'").append(data).append("'");
											}
										}
									}
									if (i < columnsLength - 1) {
										query.append(", ");

									}
								}
								query.append(" )");
								queries.add(query.toString());
							} catch (Exception e) {
								// TODO handle exception
							}
						}
					}
				});
				String[] querriesArr = queries.toArray(new String[0]);

				int updated = clientJdbcTemplate.execute(new StatementCallback<Integer>() {
					@Override
					public Integer doInStatement(Statement stmt) throws SQLException, DataAccessException {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
						String batchId = clientData.getUserId() + "_" + clientData.getUserPackage().getPackageId() + "_"
								+ clientData.getUserPackage().getPackageName() + "_" + sdf.format(new Date());

						int count = 0; 
						String failureSql = "INSERT INTO " + clientStagingSchema
								+ ".ETL_JOB_ERROR_LOG ( DataSource_Id, BATCH_ID, ERROR_CODE, ERROR_TYPE, ERROR_MSG, ERROR_SYNTAX, ADDED_DATETIME, ADDED_USER) VALUES( ";
						SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String startTime = startDateFormat.format(new Date());

						for (String sqlStmt : queries) {
							try {
								int insertedStatus = stmt.executeUpdate(sqlStmt);
								if (insertedStatus == 1) {
									count++;
								}
							} catch (Exception e) {
								final StringBuilder sb = new StringBuilder(failureSql);
								String errMessage = e.getMessage().replaceAll("'", "''");
								sb.append("'").append("unknown").append("', ").append("'").append(batchId).append("', ").append(1).append(",'")
										.append("Data Error").append("','").append(errMessage).append("','").append(errMessage).append("', now(),'Custom')");
								try {
									stmt.executeUpdate(sb.toString());
								} catch (Exception er) {
								}
							}

						}
						String endTime = startDateFormat.format(new Date());
						int fail = querriesArr.length - count;
						StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
						loadSummerySql.append(clientStagingSchema).append(
								".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
						loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId).append("', '").append(targetTable.getTableName())
								.append("', ").append(queries.size()).append(", ").append(count).append(", ").append(fail).append(", '").append(startTime)
								.append("', '").append(endTime).append("', 'Success', 'unknown', now(), 'Custom');");
						try {
							stmt.executeUpdate(loadSummerySql.toString());
						} catch (Exception er) {
						}

						return count;
					}
				});
				processedData.put("successRecords", ((Integer) processedData.get("successRecords")) + updated);
				processedData.put("failedRecords", ((Integer) processedData.get("failedRecords")) + (queries.size() - updated));
				System.out.println("processedData>>" + processedData);

			}

		} catch (Exception e) {
			LOGGER.error("Error while inserting query data", e);
		}

		return processedData;
	}

	@Override
	public boolean saveCustomTargetDerivativeTable(String clientId, String packageId, String industryId, String query, String tablename, String tableid,
			String ccols, String schemaName, Modification modification, JdbcTemplate clientJdbcTemplate){

		try {

			String sql = sqlHelper.getSql("saveCustomTargetDerivativeTable");

			LOGGER.debug("sql : {}", sql);

			if (StringUtils.isEmpty(ccols))
				ccols = null;

			int count = clientJdbcTemplate.update(sql, new Object[] { tableid, clientId, packageId, schemaName, tablename, ccols, query,
					modification.getCreatedBy(), modification.getCreatedTime() });

			return count > 0;

		} catch (Exception e) {
			LOGGER.error("Error while saving data custom target table data ", e);
			return false;
		}
	}

	public List<TableDerivative> getCustomTargetDerivativeTables(String clientId, int packageId, Integer tableId, JdbcTemplate clientJdbcTemplate){
		List<TableDerivative> tables = null;

		try {

			String sql = sqlHelper.getSql("getCustomTargetDerivativeTables");

			tables = clientJdbcTemplate.query(sql, new Object[] { tableId, clientId, packageId }, new RowMapper<TableDerivative>() {

				@Override
				public TableDerivative mapRow(ResultSet rs, int rowNum) throws SQLException {

					TableDerivative table = new TableDerivative();

					table.setTableId(rs.getInt("id"));
					table.setTargetTableId(rs.getInt("target_table_id"));
					table.setClientId(rs.getString("client_id"));
					table.setPackageId(rs.getInt("package_id"));
					table.setSchemaName(rs.getString("schemaName"));
					table.setTableName(rs.getString("target_table_name"));
					table.setQuery(rs.getString("custom_target_table_query"));

					return table;
				}

			});

		} catch (Exception e) {
			LOGGER.error("Error while reading tables data ", e);
		}

		return tables;
	}

	@Override
	public Map<String, Object> processCustomTargetDerivativeTable(TableDerivative tableDerivative, String targetTableName, JdbcTemplate clientJdbcTemplate) {

		Map<String, Object> processedData = new HashMap<>();
		try {

			String tableName = tableDerivative.getTableName();

			String query = tableDerivative.getQuery();
			List<Column> derivedColumns = tableDerivative.getColumns();

			int total = 0, success = 0, fail = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
			SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = startDateFormat.format(new Date());
			String batchId = tableDerivative.getClientId() + "_" + tableDerivative.getPackageId() + "_" + tableDerivative.getPackageName() + "_"
					+ sdf.format(new Date());

			DBDataOperations dbOp = new DBDataOperations();
			DataSource dataSource = clientJdbcTemplate.getDataSource();
			dbOp.setDataSource(dataSource);

			String colNameQuery = "select * from " +tableName + " where 1<>1";

			List<String> columnNames = clientJdbcTemplate.query(colNameQuery, new ResultSetExtractor<List<String>>() {
				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					ResultSetMetaData rm = rs.getMetaData();

					List<String> cols = new ArrayList<>();

					int len = rm.getColumnCount();

					for (int i = 1; i <= len; i++) {
						cols.add(rm.getColumnLabel(i));
					}
					return cols;
				}
			});

			StringBuilder insertScript = new StringBuilder();

			insertScript.append("insert into ").append(tableName).append("(");

			int index = 1, length = columnNames.size();

			for (String colname : columnNames) {
				insertScript.append("`").append(colname).append("`");
				if (index < length)
					insertScript.append(", ");
				index++;
			}
			insertScript.append(") ");
			insertScript.append("VALUES(");
			String insertQuery = insertScript.toString();
			processedData.put("totalRecords", 0);
			processedData.put("successRecords", 0);
			processedData.put("failedRecords", 0);
			List<String> queries = new ArrayList<>();
			boolean isProcessed = false;
			try {

				clientJdbcTemplate.query(query, new RowCallbackHandler() {

					@Override
					public void processRow(ResultSet rs) throws SQLException {
						if (rs != null) {
							processedData.put("totalRecords", ((Integer) processedData.get("totalRecords")) + 1);

							Object[] args = new Object[length];

							for (int i = 0; i < length; i++) {
								args[i] = rs.getString(i + 1);
							}
							int derivedColumnsLength = derivedColumns.size();
							try {
								StringBuilder query = new StringBuilder(insertQuery);
								for (int i = 0; i < derivedColumnsLength; i++) {

									String dataType = derivedColumns.get(i).getDataType();
									Object data = args[i];
									if (StringUtils.isEmpty((String) data)) {
										query.append(null + "");
									} else {
										if ("text".equalsIgnoreCase(dataType) || "varchar".equalsIgnoreCase(dataType)) {
											data = ((String) data).replaceAll("'", "''");
											query.append("'").append(data).append("'");
										} else if ("datetime".equalsIgnoreCase(dataType)) {
											query.append("'").append(data).append("'");
										} else {
											if (NumberUtils.isNumber(data.toString()) || "bit".equalsIgnoreCase(dataType)) {
												query.append(data);
											} else {
												data = data.toString().replaceAll("'", "''");
												query.append("'").append(data).append("'");
											}
										}
									}
									if (i < derivedColumnsLength - 1) {
										query.append(", ");

									}
								}
								query.append(" )");
								queries.add(query.toString());

							} catch (Exception e) {
								// TODO Handle exception
							}
						}
					}
				});

				int updated = clientJdbcTemplate.execute(new StatementCallback<Integer>() {
					@Override
					public Integer doInStatement(Statement stmt) throws SQLException, DataAccessException {
						int successCount = 0;
						String failureSql = "INSERT INTO " + tableDerivative.getSchemaName()
								+ "_staging.ETL_JOB_ERROR_LOG ( DataSource_Id, BATCH_ID, ERROR_CODE, ERROR_TYPE, ERROR_MSG, ERROR_SYNTAX, ADDED_DATETIME, ADDED_USER) VALUES( ";
						for (String sqlStmt : queries) {
							try {
								int insertedStatus = stmt.executeUpdate(sqlStmt);
								if (insertedStatus == 1) {
									successCount++;
								}
							} catch (Exception e) {
								final StringBuilder sb = new StringBuilder(failureSql);
								String errMessage = e.getMessage().replaceAll("'", "''");
								sb.append("'").append("unknown").append("', ").append("'").append(batchId).append("', ").append(1).append(",'")
										.append("Data Error").append("','").append(errMessage).append("','").append(errMessage).append("', now(),'Custom')");
								try {
									stmt.executeUpdate(sb.toString());
								} catch (Exception er) {
								}
							}
						}

						return successCount;
					}
				});
				total = (Integer) processedData.get("totalRecords");
				success = (Integer) processedData.get("successRecords") + updated;
				fail = (Integer) processedData.get("failedRecords") + queries.size() - updated;

				String endTime = startDateFormat.format(new Date());
				StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
				loadSummerySql.append(tableDerivative.getSchemaName()).append("_staging").append(
						".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
				loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId).append("', '").append(tableName).append("', ")
						.append(total).append(", ").append(success).append(", ").append(fail).append(", '").append(startTime).append("', '").append(endTime)
						.append("', 'Success', 'unknown', now(), 'Custom');");
				try {
					clientJdbcTemplate.update(loadSummerySql.toString());
				} catch (Exception er) {
				}

				processedData.put("successRecords", ((Integer) processedData.get("successRecords")) + updated);
				processedData.put("failedRecords", ((Integer) processedData.get("failedRecords")) + (queries.size() - updated));
				System.out.println("processedData>>" + processedData);
				isProcessed = true;

			} catch (Exception e) {
				LOGGER.error("Error while exeucuting query ..", e);
				isProcessed = false;
			}

			processedData.put("isProcessed", isProcessed);

		} catch (Exception e) {
			LOGGER.error("error while processing custom target tables ", e);

		}

		return processedData;
	}

	@Override
	public void updateCustomTargetDerivativeTableResults(TableDerivative tableDerivative, Map<String, Object> processedResults, Modification modification , JdbcTemplate clientJdbcTemplate) {

		try {

			String sql = sqlHelper.getSql("updateCustomTargetDerivativeTableResults");

			LOGGER.debug("sql : {} ", sql);

			int count = clientJdbcTemplate.update(sql,
					new Object[] { processedResults.get("isProcessed"), processedResults.get("successRecords"), processedResults.get("failedRecords"),
							processedResults.get("totalRecords"), modification.getCreatedBy(), modification.getCreatedTime(),
							tableDerivative.getTargetTableId(), tableDerivative.getClientId(), tableDerivative.getPackageId(), tableDerivative.getTableId() });

			LOGGER.debug("execution count : {} ", count);

		} catch (Exception e) {
			LOGGER.error("Error while processing target table results ", e);
		}

	}

	@Override
	public Table createCustomTargetDerivativeTable(String clientId, String packageId, String industryId, String query, String tablename, String targetTable,
			String ccols, String schemaName, JdbcTemplate clientJdbcTemplate) {

		Table table = null;

		try {

			List<String> customColNames = new ArrayList<>();
			List<String> customColDatatypes = new ArrayList<>();
			List<String> customColLength = new ArrayList<>();
			if (StringUtils.isNotEmpty(ccols)) {
				String customcols[] = ccols.split("##");

				for (String customcol : customcols) {
					String[] col = customcol.split("::");
					String datatypevals = col[1];
					String[] typeval = datatypevals.split("\\^");
					customColNames.add(col[0]);

					customColDatatypes.add(typeval[0]);

					if (typeval.length > 1) {
						customColLength.add(typeval[1]);
					} else {
						customColLength.add("");
					}
				}
			}

			List<Column> columns = clientJdbcTemplate.query(query, new ResultSetExtractor<List<Column>>() {

				@Override
				public List<Column> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Column> columns = null;

					if (rs == null)
						return null;

					columns = new ArrayList<>();
					ResultSetMetaData rm = rs.getMetaData();
					int len = rm.getColumnCount();

					for (int i = 1; i <= len; i++) {

						String colname = rm.getColumnLabel(i);

						if (!customColNames.contains(colname)) {
							
							String datatype = rm.getColumnTypeName(i); 
							Column column = new Column();
							column.setColumnName(rm.getColumnLabel(i));
							column.setDataType(datatype);

							if("VARCHAR".equals(datatype)) {
								if(rm.getPrecision(i) > 10000) {
									column.setDataType("TEXT");
								}else {
									column.setColumnSize(rm.getPrecision(i) + "");
								}
							}else if("BLOB".equals(datatype) || "DATE".equals(datatype) || "DATETIME".equals(datatype) || "TIME".equals(datatype)
									|| "TINYBLOB".equals(datatype) || "YEAR".equals(datatype)){
								column.setColumnSize("");
							}else {
								column.setColumnSize(rm.getPrecision(i) + "");
							}
							
							if ("DECIMAL".equals(datatype)) {
								int precision = rm.getPrecision(i);
								int scale = rm.getScale(i);
								if (scale <= precision) {
									column.setDecimalPoints(rm.getScale(i));
									column.setColumnSize(precision +", "+scale);
								}
								else {
									if (precision > 20) {
										// setting decimal points as 5, if the
										// column doesn't have proper decimal
										// point values.
										column.setDecimalPoints(5);
										column.setColumnSize(precision +", "+5);
									}
									else {
										column.setDecimalPoints(2);
										column.setColumnSize(precision +", "+2);
									}
								}
							}
							column.setDefaultValue("");
							column.setIsAutoIncrement(false);
							column.setIsNotNull(false);
							column.setIsPrimaryKey(false);
							column.setIsUnique(false);

							columns.add(column);
						}
					}
					return columns;
				}
			});

			int index = 0;
			for (String customColName : customColNames) {

				Column column = new Column();
				column.setColumnName(customColName);

				String datatype = customColDatatypes.get(index);
				String length = customColLength.get(index);
				column.setDataType(datatype);

				if (StringUtils.isEmpty(length)) {
					if ("VARCHAR".equals(datatype))
						column.setColumnSize("255");
					else if ("INT".equals(datatype) || "BIGINT".equals(datatype))
						column.setColumnSize("11");
					else if ("BIT".equals(datatype))
						column.setColumnSize("1");
					else if ("DECIMAL".equals(datatype)) {
						column.setColumnSize("24");
						column.setDecimalPoints(new Integer("7"));
					} else
						column.setColumnSize("");
				} else {
					if ("VARCHAR".equals(datatype))
						column.setColumnSize(length);
					else if ("INT".equals(datatype) || "BIGINT".equals(datatype))
						column.setColumnSize(length);
					else if ("BIT".equals(datatype))
						column.setColumnSize(length);
					else if ("DECIMAL".equals(datatype)) {
						String len[] = length.split("~");

						column.setColumnSize(len[0]);
						if (len.length > 1) {
							if (StringUtils.isNotEmpty(len[1]) && Integer.parseInt(len[1]) > 0) {
								column.setDecimalPoints(Integer.parseInt(len[1]));
							} else {
								if (Integer.parseInt(len[0]) > 20)
									column.setDecimalPoints(5);
								else
									column.setDecimalPoints(2);
							}
						} else {
							if (Integer.parseInt(len[0]) > 20) {
								column.setDecimalPoints(5);
							} else
								column.setDecimalPoints(2);
						}
					} else
						column.setColumnSize("");
				}
				column.setDefaultValue("");
				column.setIsAutoIncrement(false);
				column.setIsNotNull(false);
				column.setIsPrimaryKey(false);
				column.setIsUnique(false);

				columns.add(column);

				index++;
			}

			table = new Table();

			table.setTableName(targetTable);
			table.setColumns(columns);
			table.setSchemaName(schemaName);

		} catch (Exception e) {
			LOGGER.error("Error while saving data custom target table data ", e);
		}

		return table;
	}

	@Override
	public void truncateTable(String schemaName, String tableName, JdbcTemplate clientJdbcTemplate) {
		try {
			clientJdbcTemplate.execute("truncate table " + tableName + ";");
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	@Override
	public int updateFilesHavingSameColumns(String clientId, int packageId, boolean filesHavingSameColumns, JdbcTemplate clientJdbcTemplate) {
		int i = -1;
		try {
			String sql = sqlHelper.getSql("updateFilesHavingSameColumns");

			i = clientJdbcTemplate.update(sql, new Object[] { filesHavingSameColumns, clientId, packageId });

		} catch (DataAccessException ae) {
			LOGGER.error("error while insertFileColumnDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getFileInfoByPackage()", e);
			
		}
		return i;
	}

	@Override
	public List<Table> getCustomFileTempTableMappings(String clientId, String packageId, JdbcTemplate clientJdbcTemplate) {

		List<Table> tables = null;

		try {

			String sql = sqlHelper.getSql("getCustomFileTempTableMappings");

			tables = clientJdbcTemplate.query(sql, new Object[] { Integer.parseInt(packageId), clientId }, new RowMapper<Table>() {

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
			LOGGER.error("error while insertFileColumnDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getFileInfoByPackage()", e);
			
		}

		return tables;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllTables(String clientId, String clientSchemaName, JdbcTemplate clientJdbcTemplate) {

		List<String> alltables = null;

		try {

			Object tablesobj = JdbcUtils.extractDatabaseMetaData(clientJdbcTemplate.getDataSource(), new DatabaseMetaDataCallback() {

				@Override
				public List<String> processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException {

					List<String> tables = new ArrayList<>();

					try {
						ResultSet tableMetaData = dbmd.getTables(null, null, "%", null);

						while (tableMetaData.next()) {
							tables.add(tableMetaData.getString("TABLE_NAME"));
						}

					} catch (Exception e) {
						LOGGER.error("Error while reading database meta data ", e);
					}

					return tables;
				}
			});

			alltables = (List<String>) tablesobj;

		} catch (Exception e) {
			LOGGER.error("Error while getting all tables : ", e);
		}

		return alltables;
	}

	@Override
	public boolean isTableExists(String clientId, String clientSchemaName, String tableName, JdbcTemplate clientJdbcTemplate) {

		Boolean exists = Boolean.FALSE;
		try {

			if (StringUtils.isEmpty(tableName) ) {
				return exists;
			}

			Object tablesobj = JdbcUtils.extractDatabaseMetaData(clientJdbcTemplate.getDataSource(), new DatabaseMetaDataCallback() {

				@Override
				public Boolean processMetaData(DatabaseMetaData dbmd) throws SQLException, MetaDataAccessException {

					try {
						ResultSet tableMetaData = dbmd.getTables(null, null, "%", null);

						while (tableMetaData.next()) {
							String dbtablename = tableMetaData.getString("TABLE_NAME");
							if (StringUtils.equalsIgnoreCase(dbtablename, tableName)) {
								return true;
							}
						}

					} catch (Exception e) {
						LOGGER.error("Error while reading database meta data ", e);
					}

					return false;
				}
			});

			exists = (Boolean) tablesobj;

			System.out.println();

		} catch (Exception e) {
			LOGGER.error("Error while getting all tables : ", e);
		}

		return exists;
	}

	@Override
	public TableDerivative getCustomTargetDerivativeTablesById(String clientId, int packageId, Integer targetTableId, Integer tableId, JdbcTemplate clientJdbcTemplate) {
		TableDerivative tables = null;

		try {

			String sql = sqlHelper.getSql("getCustomTargetDerivativeTablesById");

			tables = clientJdbcTemplate.query(sql, new Object[] { tableId, targetTableId, clientId, packageId }, new ResultSetExtractor<TableDerivative>() {

				@Override
				public TableDerivative extractData(ResultSet rs) throws SQLException, DataAccessException {

					if (rs.next()) {
						TableDerivative table = new TableDerivative();

						table.setTableId(rs.getInt("id"));
						table.setTargetTableId(rs.getInt("target_table_id"));
						table.setClientId(rs.getString("client_id"));
						table.setPackageId(rs.getInt("package_id"));
						table.setSchemaName(rs.getString("schemaName"));
						table.setTableName(rs.getString("target_table_name"));
						table.setQuery(rs.getString("custom_target_table_query"));
						return table;
					}
					return null;
				}

			});

		} catch (Exception e) {
			LOGGER.error("Error while reading tables data ", e);
		}

		return tables;
	}

	@Override
	public boolean deleteCustomTempTables(String packageId, String userId,JdbcTemplate clientAppDbJdbcTemplate, String accessKey, String secretKey, String bucketName,
			JdbcTemplate clientJdbcTemplate) {
		boolean isDeleted = true;
		try {

			List<Table> tempTables = getCustomFileTempTableMappings(userId, packageId , clientAppDbJdbcTemplate);

			if (tempTables != null && tempTables.size() > 0) {
				String queryForDeleteCustomTempTableInfo = sqlHelper.getSql("deleteCustomTempTableInfo");
				String queryForDeleteFileHeadersInfo = sqlHelper.getSql("deleteFileHeadersInfo");

				List<FileInfo> fileHeadersInfo = getFileInfoByPackage(userId, Integer.parseInt(packageId) , clientAppDbJdbcTemplate);

				if (fileHeadersInfo != null && fileHeadersInfo.size() > 0) {
					List<Integer> fileIds = new ArrayList<>();
					/*for (FileInfo file : fileHeadersInfo) {
						if (file.getIsTempTableCreated()) {
							try {
								AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(accessKey, secretKey, bucketName);
								amazonS3Utilities.deleteFileFromS3Bucket(file.getFilePath());
							} catch (Exception e) {
							}
							fileIds.add(file.getFileId());
						}
					}*/

					clientAppDbJdbcTemplate.batchUpdate(queryForDeleteFileHeadersInfo, new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							Integer flatFile = fileIds.get(i);
							ps.setInt(1, flatFile);
							ps.setString(2, packageId);
							ps.setString(3, userId);
						}

						@Override
						public int getBatchSize() {
							return fileIds.size();
						}
					});

				}

				for (int i = 0; i < tempTables.size(); i++) {
					try {
						clientJdbcTemplate.execute("drop table if exists `" + tempTables.get(i).getTableName()+"`");
					} catch (Exception e) {
						LOGGER.error("Unable to drop " + tempTables.get(i).getTableName() + " table");
					}
				}

				clientAppDbJdbcTemplate.update(queryForDeleteCustomTempTableInfo, packageId, userId);
			}
		} catch (Exception e) {
			LOGGER.error("", e);
			isDeleted = false;
		}
		return isDeleted;
	}

	@Override
	public List<String> fileNameByIlId(int ilId, JdbcTemplate clientJdbcTemplate) {

		List<String> fileNames = null;
		try {
			String fileNameSql = sqlHelper.getSql("fileNameByIlId");
			fileNames = clientJdbcTemplate.query(fileNameSql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs != null) {
						return rs.getString(1);
					} else {
						return null;
					}

				}
			}, ilId);

		} catch (DataAccessException ae) {
			LOGGER.error("error while fileNameByIlId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOGGER.error("error while fileNameByIlId", e);
			
		}
		return fileNames;

	}

	@Override
	public List<String> fileNameByXRefIlId(int ilId, JdbcTemplate clientJdbcTemplate) {


		List<String> fileNames = null;
		try {

			String fileNameSql = sqlHelper.getSql("fileNameByXRefIlId");
			fileNames = clientJdbcTemplate.query(fileNameSql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs != null) {
						return rs.getString(1);
					} else {
						return null;
					}

				}
			}, ilId);

		} catch (DataAccessException ae) {
			LOGGER.error("error while fileNameByXRefIlId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOGGER.error("error while fileNameByXRefIlId", e);
			
		}
		return fileNames;

	}

	@Override
	public String getTempTableNameByMappingId(int connectionMappingId, JdbcTemplate clientJdbcTemplate) {
		String tempTableName = null;
		try {
			String sql = sqlHelper.getSql("getTempTableNameByMappingId");
			tempTableName = clientJdbcTemplate.queryForObject(sql, String.class, connectionMappingId);

		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getTempTableNameByMappingId", e);
			
		}

		return tempTableName;
	}

	@Override
	public void saveUploadedFileInfo(SourceFileInfo s3fileInfo, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("saveUploadedFileInfo");

			clientJdbcTemplate.update(sql,
					new Object[] { s3fileInfo.getUserId(), s3fileInfo.getPackageId(), s3fileInfo.getIlConnectionMappingId(), s3fileInfo.getFileName(),
							s3fileInfo.getFileType(), s3fileInfo.getStorageType(), s3fileInfo.getFileSize(), s3fileInfo.getUploadStartTime(),
							s3fileInfo.getUploadEndTime(), s3fileInfo.getFilePath(), s3fileInfo.getBucketName(), s3fileInfo.getModification().getCreatedBy(),
							s3fileInfo.getModification().getCreatedTime() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while saveUploadedFileInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOGGER.error("Error while inserting Uploaded File Info details ", e);
			
		}

	}

	@Override
	public void deleteUploadedFileInfo(String filePath, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteUploadedFileInfo");
			clientJdbcTemplate.update(sql, filePath);

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteUploadedFileInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOGGER.error("Error while inserting deleteUploadedFileInfo ", e);
			
		}

	}

	@Override
	public boolean insertIlConnectionWebServiceMapping(FileInfo fileInfo, Table table, Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate) {

		try {

			String sql = sqlHelper.getSql("insertPackageWsSourceMapping");
			
			int count = clientJdbcTemplate.update(sql,
					new Object[] { 
							fileInfo.getFileId(), 
							webService.getPackageId(),
							webService.getClientId(),
							webService.getIlId(), 
							webService.getUrl(),
							//webService.getBaseUrl(),
							webService.getBaseUrlRequired(),
							webService.getRequestMethod(),
							webService.getResponseObjName(), 
							webService.getResponseColumnObjName(),
							webService.getApiName(),
							webService.getApiPathParams(), 
							webService.getRequestParameters(), 
							webService.getApiBodyParams(), 
							webService.getPaginationType(),
							webService.getPaginationRequired(),
							webService.getPaginationRequestParamsData(),
							webService.getIncrementalUpdate(),
							webService.getIncrementalUpdateparamdata(),
							table.getTableName(), 
							table.getTableStructure(), 
							modification.getCreatedBy(),
							modification.getCreatedTime(),
							webService.getSoapBodyElement()
					});

			return count > 0;

		} catch (Exception e) {
			LOGGER.error("Error while inserting temp table mapping details ", e);
		}

		return false;

	}

	@Override
	public List<Table> getCustomTempTablesForWebservice(String clientId, String packageId, int ilId, JdbcTemplate clientJdbcTemplate) {

		List<Table> tables = null;

		try {
			String sql = sqlHelper.getSql("getCustomTempTablesForWebservice");

			tables = clientJdbcTemplate.query(sql, new Object[] { clientId, packageId, ilId }, new RowMapper<Table>() {

				@Override
				public Table mapRow(ResultSet rs, int rowNum) throws SQLException {

					try {

						if (rs == null) {
							return null;
						}

						Table table = new Table();

						table.setTableName(rs.getString("temp_table_name"));
						table.setTableId(rs.getInt("wsm_id"));

						String fileName = rs.getString("file_path");
						table.setFileName(StringUtils.substringAfterLast(fileName, "/"));

						table.setFilePath(fileName);

						WebServiceJoin webServiceJoin = new WebServiceJoin();
						webServiceJoin.setWebServiceApiName(rs.getString("web_service_api_name"));
						webServiceJoin.setWebServiceUrl(rs.getString("web_service_url"));

						table.setWebServiceJoin(webServiceJoin);

						String fileHeaders = rs.getString("file_headers");

						table.setFileId(rs.getInt("fileId"));

						List<Column> columns = new ArrayList<>();
						Column column = null;

						String[] headers = null;

						if (rs.getString("file_type").equals(Constants.FileType.CSV)) {
							headers = fileHeaders.split(rs.getString("delimeter"));
						} else if (rs.getString("file_type").equals(Constants.FileType.XLS)) {
							headers = fileHeaders.split(",");
						} else if (rs.getString("file_type").equals(Constants.FileType.XLSX)) {
							headers = fileHeaders.split(",");
						}
						if ( headers == null) {
							throw new Exception();
						}

						for (String header : headers) {
							column = new Column();

							column.setColumnName(header);
							column.setColumnSize("500");
							column.setDataType("VARCHAR");

							columns.add(column);
						}

						table.setColumns(columns);

						return table;
					} catch (Exception e) {
						LOGGER.error("Error while converting database row to table object ", e);
					}

					return null;
				}

			});

		} catch (Exception e) {
			LOGGER.error("Error while getting the temp tables .. ", e);
		}

		return tables;

	}

	@Override
	public void deleteIlConnectionWebServiceMapping(FileInfo fileInfo, Table table, Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate) {

		try {
			String sql = sqlHelper.getSql("deleteIlConnectionWebServiceMapping");
			clientJdbcTemplate.update(sql, webService.getIlId(), fileInfo.getClientid(), fileInfo.getPackageId());

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteIlConnectionWebServiceMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOGGER.error("Error while inserting deleteIlConnectionWebServiceMapping ", e);
			
		}

	}

	@Override
	public void deletefileHeader(FileInfo fileInfo, Table table, Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate) {

		try {
			String sql = sqlHelper.getSql("deletefileHeader");
			clientJdbcTemplate.update(sql, table.getFileId(), fileInfo.getClientid(), fileInfo.getPackageId());

		} catch (DataAccessException ae) {
			LOGGER.error("error while deletefileHeader", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOGGER.error("Error while inserting deletefileHeader ", e);
			
		}

	}

	@Override
	public String getCustomTempQuery(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		String query = null;
		try {

			String tempQuery = sqlHelper.getSql("getCustomTempQuery");

			String clientId = clientData.getUserId(); 
			String packageId = clientData.getUserPackage().getPackageId() + "";

			 query = clientJdbcTemplate.queryForObject(tempQuery, new Object[] { clientId, packageId }, new RowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {

					try {
						if (rs != null) {
							return rs.getString(1);
						}
					} catch (Exception e) {
						LOGGER.error("", e);
					}

					return null;
				}

			});

	}catch (DataAccessException ae) {
		LOGGER.error("error while getCustomTempQuery", ae);
		throw new AnvizentRuntimeException(ae);
	} catch (Exception e) {
		LOGGER.error("Error while  getCustomTempQuery ", e);
		
	}
		return query;
	}

	@Override
	public void deleteTableDataByCalenderKey(String schemaName, String insertDateIntoTableName, Integer calendarKey,
			JdbcTemplate clientJdbcTemplate) {
		try {
			clientJdbcTemplate.execute("delete from " + insertDateIntoTableName + " where Account_Calendar_Key =" + calendarKey + ";");
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		
	}
	@Override
	public int insertIlConnectionWebServiceMappingForJsonOrXml(Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate)
	{
		int id = 0;
		try {

			final String sql = sqlHelper.getSql("insertIlConnectionWebServiceMappingForJsonOrXml");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
				 
					ps.setObject(1,webService.getPackageId());
					ps.setObject(2,webService.getClientId());
				    ps.setObject(3,webService.getIlId()); 
				    ps.setObject(4,webService.getUrl());
					ps.setObject(5,webService.getBaseUrlRequired());
				    ps.setObject(6,webService.getRequestMethod());
				    ps.setObject(7,webService.getResponseObjName());
				    ps.setObject(8,webService.getResponseColumnObjName());
					ps.setObject(9,webService.getApiName());
				    ps.setObject(10,webService.getApiPathParams());
				    ps.setObject(11,webService.getRequestParameters()); 
				    ps.setObject(12,webService.getApiBodyParams());
				    ps.setObject(13,webService.getPaginationType());
				    ps.setObject(14,webService.getPaginationRequired());
				    ps.setObject(15,webService.getPaginationRequestParamsData());
				    ps.setObject(16,webService.getIncrementalUpdate());
				    ps.setObject(17,webService.getIncrementalUpdateparamdata());
				    ps.setObject(18,modification.getCreatedBy());
				    ps.setObject(19,modification.getCreatedTime());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}

		} catch (DataAccessException ae) {
			LOGGER.error("error while insertIlConnectionWebServiceMappingForJsonOrXml()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while insertIlConnectionWebServiceMappingForJsonOrXml()", e);
			
		}
		return id;
	}

	@Override
	public int updateDbConSSlAuthCertFilesInfo(int dbTypeId, int conId, String fileName, String filePath, File file, Modification modification,JdbcTemplate clientJdbcTemplate)
	{
		int id = 0;
		try {
				//String SQL = "delete from minidwcs_db_con_ssl_auth_cert_files_info where con_id = "+conId;
				//clientJdbcTemplate.execute(SQL);
			  
				final String sql = sqlHelper.getSql("insertDbConSSlAuthCertFilesInfo");
				KeyHolder keyHolder = new GeneratedKeyHolder();
				clientJdbcTemplate.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
			            FileInputStream input = null;
						try{
							input = new FileInputStream(file);
						}catch ( FileNotFoundException e )
						{
							e.printStackTrace();
						}
						ps.setObject(1,dbTypeId);
						ps.setObject(2,conId);
					    ps.setObject(3,fileName); 
						ps.setObject(4,filePath);
					    ps.setBinaryStream(5,input);
					    ps.setObject(6,modification.getCreatedBy());
					    ps.setObject(7,modification.getCreatedTime());
						return ps;
					}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateDbConSSlAuthCertFilesInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateDbConSSlAuthCertFilesInfo()", e);
			
		}
		return id;
	}

	@Override
	public int deleteAndUpdateDbConSSlAuthCertFilesInfo(int dbTypeId, int conId, String fileName, String filePath, File file,String deleteFileName,Modification modification, JdbcTemplate clientJdbcTemplate)
	{
		int id = 0;
		try {
				String deleteDbConSSlAuthCertFilesInfo = sqlHelper.getSql("deleteDbConSSlAuthCertFilesInfo");
				clientJdbcTemplate.update(deleteDbConSSlAuthCertFilesInfo, dbTypeId,conId, deleteFileName);
			     
				final String sql = sqlHelper.getSql("updateDbConSSlAuthCertFilesInfo");
				clientJdbcTemplate.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql);
			            FileInputStream input = null;
						try{
							input = new FileInputStream(file);
						}catch ( FileNotFoundException e )
						{
							e.printStackTrace();
						}
					    ps.setObject(1,fileName); 
						ps.setObject(2,filePath);
					    ps.setBinaryStream(3,input);
						ps.setObject(4,dbTypeId);
						ps.setObject(5,conId);
					    ps.setObject(6,modification.getCreatedBy());
					    ps.setObject(7,modification.getCreatedTime());
						return ps;
					}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteAndUpdateDbConSSlAuthCertFilesInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteAndUpdateDbConSSlAuthCertFilesInfo()", e);
			
		}
		return id;
	}

	@Override
	public List<String> getDbConSslAuthCertFilesInfo(int dbTypeId, int conId, JdbcTemplate clientJdbcTemplate)
	{
		List<String> fileNames = null;
		try {
			String fileNameSql = sqlHelper.getSql("getDbConSslAuthCertFilesInfo");
			fileNames = clientJdbcTemplate.query(fileNameSql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs != null) {
						return rs.getString(1);
					} else {
						return null;
					}
				}
			}, dbTypeId,conId);

		} catch (DataAccessException ae) {
			LOGGER.error("error while getDbConSslAuthCertFilesInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOGGER.error("error while getDbConSslAuthCertFilesInfo", e);
		}
		return fileNames;
	}

	@Override
	public int updateSSLTrustKeyStorePathsAtDbConnection(int dbTypeId, int conId, String filePaths, Modification modification, JdbcTemplate clientJdbcTemplate)
	{
		int id = 0;
		try {
			String sql = sqlHelper.getSql("updateSSLTrustKeyStorePathsAtDbConnection");
			id = clientJdbcTemplate.update(sql,
					new Object[] { filePaths,
							modification.getModifiedBy(),
							modification.getModifiedTime(), dbTypeId,conId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateSSLTrustKeyStorePathsAtDbConnection()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateSSLTrustKeyStorePathsAtDbConnection()", e);
			
		}
     return id;
	}
}
