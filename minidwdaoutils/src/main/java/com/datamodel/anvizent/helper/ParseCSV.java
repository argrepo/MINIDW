package com.datamodel.anvizent.helper;

import java.io.BufferedWriter;

/**
 * @author srinu.chinka
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.datamodel.anvizent.common.exception.FlatFileReadingException;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.Table;
import com.opencsv.CSVReader;

public class ParseCSV {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParseCSV.class);

	private final String FILE_PATH;

	private boolean validateInsert = true;

	public boolean isValidateInsert() {
		return validateInsert;
	}

	public void setValidateInsert(boolean validateInsert) {
		this.validateInsert = validateInsert;
	}

	public ParseCSV(String filePath) {
		this.FILE_PATH = filePath;
	}

	public List<String> readColumns(String separatorChar, String stringQuoteChar) throws IOException {

		List<String> columns = null;

		boolean valid = isValidCSV();

		if (valid) {
			CSVReader reader = null;

			try {
				reader = createReader(separatorChar, stringQuoteChar);
				columns = processCsvHeader(reader);

			} finally {
				LOGGER.debug(" Closing CSV Reader Object .. ");
				if (reader != null)
					reader.close();
			}
		} else {
			LOGGER.info("Given CSV file is not valid : {}", FILE_PATH);
		}

		return columns;
	}

	private List<String> processCsvHeader(CSVReader reader) throws IOException
	{
		List<String> columns = null;
		String[] headerRow = reader.readNext();
		columns = new ArrayList<>();

		for (String col : headerRow)
		{
			if( StringUtils.isNotEmpty(col) ) {
			col = col.replaceAll("\\s+", "_").replaceAll("\\W+", "_").trim();
			if( col.length() >= 65 )
			{
				col = col.substring(0, 64);
			}
			columns.add(col);
			}
		}

		return columns;
	}

	public List<String> getColumnsDataType(String separatorChar, String stringQuoteChar) throws IOException {
		LOGGER.debug("get column data types of csv file.. ");
		List<String> datatypes = new ArrayList<>();

		boolean valid = isValidCSV();

		if (valid) {
			CSVReader reader = null;

			try {
				reader = createReader(separatorChar, stringQuoteChar);
				int count = 0;
				String[] datarow = reader.readNext();

				while (datarow != null) {
					int colslen = datarow.length;
					for (int i = 0; i < colslen; i++) {
						String value = datarow[i];

						if (value.matches("^[-]?[0-9*]{1,10}$")) {
							datatypes.add("INT");
						} else if (value.matches("^[-]?[0-9]{10,}$")) {
							datatypes.add("BIGINT");
						} else if (value.matches("^[0-1]{1}$")) {
							datatypes.add("BIT");
						} else if (value.matches("^([-]?\\d*\\.\\d*)$")) {
							datatypes.add("FLOAT");
						} else if (value.matches("[0-9a-zA-z!\"\',/@#$*\\s]*")) {
							datatypes.add("VARCHAR");
						} else {
							datatypes.add("VARCHAR");
						}
					}
					datarow = reader.readNext();
					count++;

					if (count < 1) {
						continue;
					} else
						break;
				}

			} finally {
				LOGGER.debug(" Closing CSV Reader Object .. ");
				if (reader != null)
					reader.close();
			}
		} else {
			LOGGER.info("Given CSV file is not valid : {}", FILE_PATH);
		}

		return datatypes;
	}

	private CSVReader createReader(String separatorChar, String stringQuoteChar) throws FileNotFoundException, UnsupportedEncodingException {

		CSVReader reader = null;

		LOGGER.debug("creating csv reader object using given file path.");

		File csvFile = new File(FILE_PATH);

		boolean separatorPresent = separatorChar != null && separatorChar.length() == 1;
		boolean quotecharPresent = stringQuoteChar != null && stringQuoteChar.length() == 1;

		if (separatorPresent && quotecharPresent) {
			reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), Constants.Config.ENCODING_TYPE), separatorChar.charAt(0),
					stringQuoteChar.charAt(0));
		} else if (separatorPresent) {
			reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), Constants.Config.ENCODING_TYPE), separatorChar.charAt(0));
		} else {
			reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), Constants.Config.ENCODING_TYPE), ',', '"');
		}

		return reader;
	}

	private boolean isValidCSV() {

		LOGGER.debug("validation csv file .. ");

		boolean valid = false;

		if (FILE_PATH != null && FILE_PATH.length() > 0) {
			File csvFile = new File(FILE_PATH);

			valid = csvFile.exists();
		} else {
			LOGGER.info("File path is empty or null .. {}", FILE_PATH);
		}

		return valid;
	}

	public boolean processCSVDataToFile(String outputFilePath, List<String> iLColumnNames, List<String> selectedFileHeaders, List<String> dafaultValues,
			String separatorChar, String stringQuoteChar) throws IOException {

		boolean isProcessed = Boolean.FALSE;
		boolean valid = isValidCSV();
		BufferedWriter fileWriter = null;
		if (valid) {

			CSVReader reader = null;

			reader = createReader(separatorChar, stringQuoteChar);

			List<String> columns = processCsvHeader(reader);

			fileWriter = new BufferedWriter(new FileWriterWithEncoding(outputFilePath, "UTF-8"));

			int i = 1, colslength = iLColumnNames.size();
			for (String iLColumn : iLColumnNames) {
				fileWriter.append(iLColumn.trim());
				if (i < colslength)
					fileWriter.append(",");
				i++;
			}

			fileWriter.append(System.getProperty("line.separator"));

			String[] datarow = reader.readNext();

			while (datarow != null) {
				for (int j = 0; j < iLColumnNames.size(); j++) {
					String fileHeader = selectedFileHeaders.get(j);
					int colIndex;
					String data = null;
					if (StringUtils.isNotBlank(fileHeader)) {
						colIndex = columns.indexOf(fileHeader.trim());
						if (colIndex == -1) {
							throw new IOException("Source Column Header: " + fileHeader.trim() + " is not mapped with Saved Mapped Header.");
						}
						data = datarow[colIndex];
					} else {
						String dafaultValue = dafaultValues.get(j);
						if (StringUtils.isNotBlank(dafaultValue)) {
							data = dafaultValue;
						}
					}

					if (StringUtils.isBlank(data)) {
						data = "";
					}

					data = CommonUtils.sanitizeForCsv(data);
					fileWriter.append(data);

					if (j < colslength - 1) {
						fileWriter.append(",");
					}
				}

				fileWriter.append(System.getProperty("line.separator"));

				datarow = reader.readNext();
			}
			if (fileWriter != null)
				fileWriter.close();
			if (reader != null)
				reader.close();
			isProcessed = Boolean.TRUE;

		}
		return isProcessed;
	}

	private String buildInsertScript(Table table) {
		StringBuilder insertScript = new StringBuilder();

		String tableName = table.getTableName();

		List<String> tColumns = table.getOriginalColumnNames();
		int colslen = tColumns.size();

		insertScript.append("INSERT INTO ")
		.append(tableName).append(" ( \n ");

		for (int i = 0; i < colslen; i++) {
			String column = tColumns.get(i);
			String colname = column;

			insertScript.append("`").append(colname).append("`");

			if (i < colslen - 1)
				insertScript.append(", ");
		}
		insertScript.append(" \n ) VALUES ( \n ");

		return insertScript.toString();
	}

	public Map<String, Object> processCSVData(Table table, DataSource dataSource, String separatorChar, String stringQuoteChar) throws Exception {

		Map<String, Object> processedData = new HashMap<>();
		LOGGER.debug("processing csv data .. ");

		boolean valid = isValidCSV();

		if (valid) {

			CSVReader reader = null;

			try {
				reader = createReader(separatorChar, stringQuoteChar);

				List<String> columns = processCsvHeader(reader);

				LOGGER.debug("columns before applying pattern : {}", columns);

				for (int i = 0; i < columns.size(); i++) {
					String col = columns.get(i);
					col = col.trim().replaceAll("\\s+", "_");
					columns.set(i, col);
				}

				LOGGER.debug("columns after applying pattern : {}", columns);

				String tableName = table.getTableName();
				String schemaName = table.getSchemaName();

				List<Column> tColumns = table.getColumns();
				int colslen = tColumns.size();

				LOGGER.debug("Schema Name : {} --> Table Name : {} ", schemaName, tableName);

				LOGGER.debug("Columns Size : {} ", colslen);

				String[] datarow = reader.readNext();

				StringBuilder selectScript = new StringBuilder();
				selectScript.append("SELECT COUNT(*) FROM ").append(schemaName).append(".").append(tableName).append(" \n WHERE ");
				String insertScript = buildInsertScript(table);
				DBDataOperations dbOp = new DBDataOperations();
				dbOp.setDataSource(dataSource);

				int total = 0, success = 0, fail = 0, duplicates = 0;

				while (datarow != null) {

					StringBuilder insertQuery = new StringBuilder(insertScript);
					StringBuilder selectQuery = new StringBuilder(selectScript);

					for (int i = 0; i < colslen; i++) {
						Column column = tColumns.get(i);
						String colname = column.getColumnName();
						String datatype = column.getDataType();
						int colIndex = columns.indexOf(colname);

						selectQuery.append(colname).append(" = ");

						String value = datarow[colIndex];

						if ("text".equalsIgnoreCase((String) datatype) || "varchar".equalsIgnoreCase(datatype)) {

							value = value.replaceAll("'", "''");

							insertQuery.append("'").append(value).append("'");
							selectQuery.append("'").append(value).append("'");

						} else if ("datetime".equalsIgnoreCase(datatype)) {
							insertQuery.append("'").append(value).append("'");
							selectQuery.append("'").append(value).append("'");
						} else {
							insertQuery.append(value);
							selectQuery.append(value);
						}

						if (i < colslen - 1) {
							insertQuery.append(", ");
							selectQuery.append(" AND ");

						}
					}

					insertQuery.append(" \n )");
					try {
						boolean recordexists = false;
						// validate file data or not.
						if (validateInsert) {
							recordexists = dbOp.checkRecordExist(selectQuery.toString());
						}

						if (recordexists) {
							duplicates++;
						} else {
							boolean queryExeStatus = dbOp.executeQuery(insertQuery.toString());

							if (queryExeStatus)
								success++;
							else
								fail++;
						}

					} catch (Exception e) {
						fail++;
						LOGGER.error("Error while execution the insert query script ", e);
					}

					total++;
					datarow = reader.readNext();
				}

				LOGGER.debug("Total Records : {} ", total);
				LOGGER.debug("Sccuess Records : {} ", success);
				LOGGER.debug("Failed Records : {} ", fail);
				LOGGER.debug("Duplicate Records : {} ", duplicates);

				processedData.put("totalRecords", total);
				processedData.put("successRecords", success);
				processedData.put("failedRecords", fail);
				processedData.put("duplicateRecords", duplicates);
			} finally {
				LOGGER.debug("Closing CSV reader object .. ");
				if (reader != null)
					reader.close();
			}
		}

		return processedData;
	}

	public Map<String, Object> processCSVDataBatch(ClientData clientData, DataSource dataSource, String separatorChar, String stringQuoteChar,
			String clientSchemaName) throws FlatFileReadingException {

		Map<String, Object> result = new HashMap<>();
		Table table = clientData.getUserPackage().getTable();
		LOGGER.debug("processing csv data batch.. ");

		boolean valid = isValidCSV();

		if (valid) {

			CSVReader reader = null;
			List<String> columns = null;
			try {
					reader = createReader(separatorChar, stringQuoteChar);
					columns = processCsvHeader(reader);
				LOGGER.debug("columns before applying pattern : {}", columns);

				for (int i = 0; i < columns.size(); i++) {
					String col = columns.get(i);
					col = col.trim().replaceAll("\\s+", "_");
					columns.set(i, col);
				}

				LOGGER.debug("columns after applying pattern : {}", columns);

				String tableName = table.getTableName();
				String schemaName = table.getSchemaName();

				List<Column> tColumns = table.getColumns();
				int colslen = tColumns.size();

				LOGGER.debug("Schema Name : {} --> Table Name : {} ", schemaName, tableName);

				LOGGER.debug("Columns Size : {} ", colslen);

				String[] datarow = reader.readNext();

				String insertScript = buildInsertScript(table);

				DBDataOperations dbOp = new DBDataOperations();
				dbOp.setDataSource(dataSource);

				int total = 0, success = 0, fail = 0, duplicates = 0;

				int index = 0;
				List<String> queries = new ArrayList<>();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
				SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startTime = startDateFormat.format(new Date());
				String batchId = clientData.getUserId() + "_" + clientData.getUserPackage().getPackageId() + "_" + clientData.getUserPackage().getPackageName()
						+ "_" + sdf.format(new Date());

				while (datarow != null) {

					StringBuilder insertQuery = new StringBuilder(insertScript);

					for (int i = 0; i < colslen; i++) {
						Column column = tColumns.get(i);
						String colname = column.getColumnName();
						String datatype = column.getDataType();
						int colIndex = columns.indexOf(colname);

						String value = datarow[colIndex];

						if (StringUtils.isEmpty(value)) {
							insertQuery.append("coalesce(null,DEFAULT(").append(colname).append(")) ");

						} else {
							if ("text".equalsIgnoreCase(datatype) || "varchar".equalsIgnoreCase(datatype)) {
								value = value.replaceAll("'", "''");
								insertQuery.append("'").append(value).append("'");
							} else if ("datetime".equalsIgnoreCase(datatype)) {
								insertQuery.append("'").append(value).append("'");
							} else {
								if (NumberUtils.isNumber(value) || "bit".equalsIgnoreCase(datatype)) {
									insertQuery.append(value);
								} else {
									value = value.replaceAll("'", "''");
									insertQuery.append("'").append(value).append("'");
								}

							}
						}

						if (i < colslen - 1) {
							insertQuery.append(", ");

						}
					}

					insertQuery.append(" \n )");

					queries.add(insertQuery.toString());
					total++;
					index++;

					if (index % 1000 == 0) {
						String[] querriesArr = queries.toArray(new String[0]);
						int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId() + "",
								clientData.getUserPackage().getPackageName(), clientData.getUserId(), batchId);

						success += updates;
						fail += (queries.size() - updates);

						queries.clear();
						index = 0;
						LOGGER.debug("executed records : " + total);
					}

					datarow = reader.readNext();
				}

				if (queries.size() > 0) {
					String[] querriesArr = queries.toArray(new String[0]);
					int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId() + "",
							clientData.getUserPackage().getPackageName(), clientData.getUserId(), batchId);

					success += updates;
					fail += (queries.size() - updates);
				}

				String endTime = startDateFormat.format(new Date());
				StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
				loadSummerySql.append(clientSchemaName).append(
						".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
				loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId).append("', '").append(tableName).append("', ")
						.append(total).append(", ").append(success).append(", ").append(fail).append(", '").append(startTime).append("', '").append(endTime)
						.append("', 'Success', 'unknown', now(), 'Custom');");
				try {
					dbOp.getJdbcTemplate().update(loadSummerySql.toString());
				} catch (Exception er) {
				}

				LOGGER.debug("Total Records : {} ", total);
				LOGGER.debug("Sccuess Records : {} ", success);
				LOGGER.debug("Failed Records : {} ", fail);
				LOGGER.debug("Duplicate Records : {} ", duplicates);

				result.put("totalRecords", total);
				result.put("successRecords", success);
				result.put("failedRecords", fail);
				result.put("duplicateRecords", duplicates);
			}  catch (FileNotFoundException | UnsupportedEncodingException e) {
				throw new FlatFileReadingException("File reading failed. " + e.getMessage() , e);
			} catch (IOException e) {
				throw new FlatFileReadingException("File reading failed. " + e.getMessage() , e);
			} finally {
				LOGGER.debug("Closing CSV reader object .. ");
				if (reader != null)
					try {
						reader.close();
					} catch (IOException e) {
						LOGGER.error("File closing failed " + e.getMessage());
						e.printStackTrace();
					}
			}
		}

		return result;
	}

	public Map<String, Object> processCSVDataBatch1(ClientData clientData, DataSource dataSource, String separatorChar, String stringQuoteChar,
			String clientSchemaName) throws Exception {

		Map<String, Object> result = new HashMap<>();
		Table table = clientData.getUserPackage().getTable();
		LOGGER.debug("processing csv data batch.. ");

		boolean valid = isValidCSV();

		if (valid) {

			CSVReader reader = null;

			try {
				reader = createReader(separatorChar, stringQuoteChar);

				List<String> columns = processCsvHeader(reader);

				LOGGER.debug("columns before applying pattern : {}", columns);

				for (int i = 0; i < columns.size(); i++) {
					String col = columns.get(i);
					col = col.trim().replaceAll("\\s+", "_");
					columns.set(i, col);
				}

				LOGGER.debug("columns after applying pattern : {}", columns);

				String tableName = table.getTableName();
				String schemaName = table.getSchemaName();

				List<Column> tColumns = table.getColumns();
				int colslen = tColumns.size();

				LOGGER.debug("Schema Name : {} --> Table Name : {} ", schemaName, tableName);

				LOGGER.debug("Columns Size : {} ", colslen);

				String[] datarow = reader.readNext();

				String insertScript = buildInsertScript(table);

				DBDataOperations dbOp = new DBDataOperations();
				dbOp.setDataSource(dataSource);

				int total = 0, success = 0, fail = 0, duplicates = 0;

				int index = 0;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
				SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startTime = startDateFormat.format(new Date());

				String batchId = clientData.getUserId() + "_" + clientData.getUserPackage().getPackageId() + "_" + clientData.getUserPackage().getPackageName()
						+ "_" + sdf.format(new Date());
				List<List<Object>> params = new ArrayList<>();
				while (datarow != null) {

					List<Object> param = new ArrayList<>();
					for (int i = 0; i < colslen; i++) {
						int colIndex = columns.indexOf(tColumns.get(i).getColumnName());
						String value = "";
						try {
							value = datarow[colIndex];
						} catch (Exception er) {
						}
						param.add(value);
					}
					params.add(param);
					total++;
					index++;

					if (index % 1000 == 0) {
						int updates = dbOp.batchExecute(insertScript, params, tableName, clientSchemaName, clientData.getUserPackage().getPackageId() + "",
								clientData.getUserPackage().getPackageName(), clientData.getUserId(), batchId);

						success += updates;
						fail += (params.size() - updates);

						params.clear();
						index = 0;
						LOGGER.debug("executed records : " + total);
					}

					datarow = reader.readNext();
				}

				if (params.size() > 0) {
					int updates = dbOp.batchExecute(insertScript, params, tableName, clientSchemaName, clientData.getUserPackage().getPackageId() + "",
							clientData.getUserPackage().getPackageName(), clientData.getUserId(), batchId);

					success += updates;
					fail += (params.size() - updates);
				}

				String endTime = startDateFormat.format(new Date());
				StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
				loadSummerySql.append(clientSchemaName).append(
						".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
				loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId).append("', '").append(tableName).append("', ")
						.append(total).append(", ").append(success).append(", ").append(fail)
						.append(", '" + startTime + "', '" + endTime + "', 'Success', 'unknown', now(), 'Custom');");
				try {
					dbOp.getJdbcTemplate().update(loadSummerySql.toString());
				} catch (Exception er) {
				}

				LOGGER.debug("Total Records : {} ", total);
				LOGGER.debug("Sccuess Records : {} ", success);
				LOGGER.debug("Failed Records : {} ", fail);
				LOGGER.debug("Duplicate Records : {} ", duplicates);

				result.put("totalRecords", total);
				result.put("successRecords", success);
				result.put("failedRecords", fail);
				result.put("duplicateRecords", duplicates);
			} finally {
				LOGGER.debug("Closing CSV reader object .. ");
				if (reader != null)
					reader.close();
			}
		}

		return result;
	}

	public static class DBDataOperations extends JdbcDaoSupport {

		private static final Logger LOGGER = LoggerFactory.getLogger(DBDataOperations.class);

		public boolean executeQuery(String query) {

			boolean status = false;
			try {
				int count = getJdbcTemplate().update(query);
				status = count > 0;
			} catch (Exception e) {
				LOGGER.error("Error while exeucting ", e.getMessage());
			}

			return status;
		}

		public boolean checkRecordExist(String query) {

			boolean exist = false;

			try {
				Integer count = getJdbcTemplate().queryForObject(query, Integer.class);

				exist = (count != null && count.intValue() > 0);

			} catch (Exception e) {
				LOGGER.error("Error while checking for existing record", e);
			}

			return exist;
		}

		public int batchExecute(String[] queries, String tableName, String clientStagingSchemaName, String packageId, String packageName, String userId,
				String batchId) {
			int exes = 0;

			try {
				 int updates = getJdbcTemplate().execute(new StatementCallback<Integer>() {
					@Override
					public Integer doInStatement(Statement stmt) throws SQLException, DataAccessException {
						int successCount = 0;
						String failureSql = "INSERT INTO " + clientStagingSchemaName
								+ ".ETL_JOB_ERROR_LOG ( DataSource_Id, BATCH_ID, ERROR_CODE, ERROR_TYPE, ERROR_MSG, ERROR_SYNTAX, ADDED_DATETIME, ADDED_USER) VALUES( ";
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
				exes = updates;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error while executing batch", e.getMessage());
			}

			return exes;
		}

		public int batchExecute(String query, List<List<Object>> params, String tableName, String clientStagingSchemaName, String packageId, String packageName,
				String userId, String batchId) {
			int updates = 0;

			try {

				updates = getJdbcTemplate().execute(query, new PreparedStatementCallback<Integer>() {

					@Override
					public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

						int successCount = 0;
						String failureSql = "INSERT INTO " + clientStagingSchemaName
								+ ".ETL_JOB_ERROR_LOG ( DataSource_Id, BATCH_ID, ERROR_CODE, ERROR_TYPE, ERROR_MSG, ERROR_SYNTAX, ADDED_DATETIME, ADDED_USER) VALUES( 'unknown', '"
								+ batchId + "', 1, 'Data Error', ?, ?, now(),'Custom' )";
						List<List<Object>> errTrackParams = new ArrayList<>();
						for (List<Object> list : params) {
							try {
								for (int i = 0, j = 1; i < list.size(); i++, j++) {
									ps.setObject(j, list.get(i));
								}
								int insertedStatus = ps.executeUpdate();
								ps.clearParameters();
								if (insertedStatus == 1) {
									successCount++;
								}
							} catch (Exception e) {
								String errMessage = e.getMessage();
								List<Object> errorTrack = new ArrayList<>();
								errorTrack.add(errMessage);
								errorTrack.add(errMessage);
								errTrackParams.add(errorTrack);
							}
						}
						if (errTrackParams.size() > 0) {
							updateErrorLog(failureSql, errTrackParams);
						}
						return successCount;
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error while executing batch", e.getMessage());
			}

			return updates;
		}

		public int updateErrorLog(String query, List<List<Object>> params) {
			int updates = 0;

			try {

				updates = getJdbcTemplate().execute(query, new PreparedStatementCallback<Integer>() {

					@Override
					public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {

						for (List<Object> list : params) {
							try {
								for (int i = 0, j = 1; i < list.size(); i++, j++) {
									ps.setObject(j, list.get(i));
								}
								ps.executeUpdate();
							} catch (Exception e) {
							}
						}
						return params.size();
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error while executing batch", e.getMessage());
			}

			return updates;
		}

	}


	public List<List<String>> processCSVDataForPreview(String separatorChar, String stringQuoteChar) throws Exception {
		return processCSVDataForPreview(separatorChar, stringQuoteChar,10);
	}
	public List<List<String>> processCSVDataForPreview(String separatorChar, String stringQuoteChar,int limit) throws Exception {

		List<List<String>> processedData = new ArrayList<>();
		LOGGER.debug("processing csv data .. ");

		boolean valid = isValidCSV();

		if (valid) {

			CSVReader reader = null;

			try {
				reader = createReader(separatorChar, stringQuoteChar);
				List<String> columns = processCsvHeader(reader);
				processedData.add(columns);
				int colslen = columns.size();
				int total = 0;
				String[] datarow = reader.readNext();
				List<String> row = null;
				while (datarow != null) {
					if (total > limit)
						break;
					try {
						row = new ArrayList<>();
						for (int i = 0; i < colslen; i++) {
							row.add(datarow[i]);
						}
						processedData.add(row);
						total++;
					} catch (ArrayIndexOutOfBoundsException e) {
						LOGGER.error(e.getMessage());
					}
					datarow = reader.readNext();
				}
			} finally {
				LOGGER.debug("Closing CSV reader object .. ");
				if (reader != null)
					reader.close();
			}
		}

		return processedData;
	}

}