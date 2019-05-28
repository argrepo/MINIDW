package com.datamodel.anvizent.helper.minidw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.anvizent.client.data.to.csv.path.converter.exception.QueryParcingException;
import com.anvizent.minidw.client.jdbc.utils.ClientJDBCUtil;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ServerConfigurations;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TemplateMigration;
import com.datamodel.anvizent.service.model.User;
import com.opencsv.CSVWriter;
import com.opencsv.ResultSetHelper;
import com.opencsv.ResultSetHelperService;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class CommonUtils {
	protected static final Log LOGGER = LogFactory.getLog(CommonUtils.class);
	static ResultSetHelper resultService = new ResultSetHelperService();
	
	public static String getFileName(String filePath) {
		String fileName = null;
		filePath = filePath.replace("/", File.separator).replace("\\", File.separator);
		if (filePath != null) {
			int index = filePath.lastIndexOf(File.separator);
			fileName = filePath.substring(index + 1, filePath.length());
			return fileName;
		}
		return fileName;
	}

	public static User getUserDetails(HttpServletRequest request, RestTemplate restTemplate,
			String serviceContextPath) {
		User user = null;
		user = (User) SessionHelper.getSesionAttribute(request, "principal");
		return user;
	}

	public static File multipartToFile(MultipartFile multipart) {
		String dir = CommonUtils.createDir(Constants.Temp.getTempFileDir() + UUID.randomUUID() + "/");
		File tempFile = new File(dir + multipart.getOriginalFilename());
		try {
			multipart.transferTo(tempFile);
		} catch (IllegalStateException e) {
			LOGGER.error("", e);
		} catch (IOException e) {
			LOGGER.error("", e);
		}
		return tempFile;
	}

	public static File multipartToFile(MultipartFile multipart, String dir) throws IOException {
		File tempFile = null;
		if (StringUtils.isNotBlank(dir)) {
			CommonUtils.createDir(dir);
			tempFile = new File(dir + multipart.getOriginalFilename());
			try {
				multipart.transferTo(tempFile);
			} catch (IllegalStateException e) {
				LOGGER.error("", e);
			} catch (IOException e) {
				throw e;
			}
		}
		return tempFile;
	}

	// check system OS
	private static final String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	// ***********************//
	public static String createDir(String dirName) {

		if (StringUtils.isNotBlank(dirName)) {
			if (!new File(dirName).exists()) {
				new File(dirName).mkdirs();
				LOGGER.info("dir created:" + dirName);
			}

		}
		return dirName;
	}

	public static Connection connectDatabase(ILConnection iLConnection)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		Connection con = null;
		if (iLConnection != null) {
			con = ClientJDBCUtil.getClientDataBaseConnection(iLConnection.getDatabase().getConnector_id(),
					iLConnection.getServer(), iLConnection.getUsername(), iLConnection.getPassword(),iLConnection.getDatabase().getDriverName(),iLConnection.getDatabase().getProtocal(),iLConnection.isSslEnable(),iLConnection.getSslTrustKeyStoreFilePaths());
		} else {
			throw new SQLException("Database connection details not found");
		}
		return con;
	}

	public static String currentTime() {
		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
		String time = localDateFormat.format(new Date());
		return time.replace(":", "");
		
	}
	public static JdbcTemplate getClientJdbcTemplate(ILConnection iLConnection)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		JdbcTemplate jdbcTemplate = new JdbcTemplate();

		String driver = null;
		String url = null;
		if (iLConnection != null) {

			if (iLConnection.isWebApp() && !iLConnection.isAvailableInCloud()) {
				throw new IllegalAccessException("Can't connect to local database.");
			}

			driver = iLConnection.getDatabase().getDriverName();
			url = iLConnection.getDatabase().getProtocal();

			if (driver != null && url != null) {
				BasicDataSource dataSource = new BasicDataSource();
				dataSource.setDriverClassName(driver);
				dataSource.setUrl(url);
				dataSource.setInitialSize(1);
				try {
					if (StringUtils.isNotEmpty(iLConnection.getUsername())
							&& StringUtils.isNotEmpty(iLConnection.getPassword())) {
						dataSource.setUsername(iLConnection.getUsername());
						dataSource.setPassword(iLConnection.getPassword());
					} else if (StringUtils.isNotEmpty(iLConnection.getUsername())) {
						dataSource.setUsername(iLConnection.getUsername());
						dataSource.setPassword("");
					}

				} catch (Exception ex) {
					LOGGER.debug(ex.getMessage());
				}
				jdbcTemplate.setDataSource(dataSource);
			} else {
				throw new IllegalAccessException(
						"JDBC Driver Name and URL should not be empty or Invalid database Type");
			}
		} else {
			throw new IllegalAccessException("JDBC Driver Name and URL should not be empty or Invalid database Type");
		}

		return jdbcTemplate;
	}

	public static String createFileByConncetion(ILConnectionMapping ilConnectionMapping, int limit) throws Exception {

		SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
		String filePath = null;
		JdbcTemplate jdbcTemplate = null;

		jdbcTemplate = getClientJdbcTemplate(ilConnectionMapping.getiLConnection());
		String typeOfCommand = ilConnectionMapping.getTypeOfCommand();
		boolean isQuery = ("Query".equals(typeOfCommand));

		if (isQuery) {
			String queryStartTime = startDateFormat.format(new Date());
			LOGGER.info("Query execution started " + queryStartTime + " for IL Mapping Id "
					+ ilConnectionMapping.getiLConnection().getConnectionId());
			filePath = jdbcTemplate.query(ilConnectionMapping.getiLquery(), new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					String queryEndTime = startDateFormat.format(new Date());
					LOGGER.info("Query execution completed " + queryEndTime + " for IL Mapping Id "
							+ ilConnectionMapping.getiLConnection().getConnectionId());
					String filePath = "";
					String fileDir = CommonUtils.createDir(Constants.Temp.getTempFileDir());
					String tablename = rs.getMetaData().getTableName(1);
					String newfilename = "";

					if (StringUtils.isNotEmpty(tablename)) {
						newfilename = tablename + CommonUtils.generateUniqueIdWithTimestamp();
					} else {
						newfilename = "datafile_" + CommonUtils.generateUniqueIdWithTimestamp();
					}

					filePath = fileDir + newfilename + ".csv";

					String startTime = startDateFormat.format(new Date());
					LOGGER.info("file writing started " + startTime + " for IL Mapping Id "
							+ ilConnectionMapping.getiLConnection().getConnectionId());
					writeCSVFile(rs, filePath, limit);
					String endTime = startDateFormat.format(new Date());
					LOGGER.info("file writing completed for " + endTime + " for IL Mapping Id "
							+ ilConnectionMapping.getiLConnection().getConnectionId());
					return filePath;
				}
			});
		} else {
			filePath = jdbcTemplate.execute(new ConnectionCallback<String>() {
				@Override
				public String doInConnection(Connection con) throws SQLException, DataAccessException {
					String filePath = "";
					CallableStatement cstmt = null;
					ResultSet res = null;

					String procName = ilConnectionMapping.getiLquery();
					String procParams = ilConnectionMapping.getProcedureParameters();

					List<Map<String, String>> paramList = new ArrayList<>();

					if (StringUtils.isNotEmpty(procParams)) {
						String[] params = procParams.split("\\^");

						if (params.length > 0) {
							for (String param : params) {
								String[] p = param.split("=");

								if (p.length == 2) {
									Map<String, String> paramMap = new HashMap<>();
									paramMap.put("name", p[0]);
									paramMap.put("value", p[1]);
									paramList.add(paramMap);
								}
							}
						}
					}

					int noofparams = paramList.size();

					StringBuilder query = new StringBuilder();

					query.append("{call ").append(procName);

					if (noofparams > 0) {
						query.append("(");
						for (int i = 1; i <= noofparams; i++) {
							query.append("?");
							if (i < noofparams) {
								query.append(", ");
							}
						}
						query.append(")");
					}

					query.append("}");

					cstmt = con.prepareCall(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

					if (noofparams > 0) {
						int index = 1;
						for (Map<String, String> paramMap : paramList) {
							cstmt.setObject(index++, paramMap.get("value"));
						}
					}

					res = cstmt.executeQuery();
					String fileDir = CommonUtils.createDir(Constants.Temp.getTempFileDir());
					filePath = fileDir + (procName.replaceAll("\\.", "_").replaceAll("\\W+", "")) + ".csv";

					String startTime = startDateFormat.format(new Date());
					LOGGER.info("SP file writing started " + startTime + " for IL Mapping Id "
							+ ilConnectionMapping.getiLConnection().getConnectionId());
					writeCSVFile(res, filePath, limit);
					String endTime = startDateFormat.format(new Date());
					LOGGER.info("SP file writing completed for " + endTime + " for IL Mapping Id "
							+ ilConnectionMapping.getiLConnection().getConnectionId());
					return filePath;
				}
			});
		}

		return filePath;
	}

	public static void writeCSVFile(ResultSet res, String filePath, int limit) throws DataAccessException {

		LOGGER.debug("file path : " + filePath);
		FileWriterWithEncoding fw = null;
		CSVWriter csvWriter = null;
		try {
			fw = new FileWriterWithEncoding(filePath, Constants.Config.ENCODING_TYPE);

			csvWriter = new CSVWriter(fw, ',');
			List<String> columnHeaders = new ArrayList<>();
			// set table name as file name
			ResultSetMetaData rsMetaData = res.getMetaData();

			int columnCount = rsMetaData.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {
				String columnHeader = res.getMetaData().getColumnLabel(i);
				columnHeaders.add(columnHeader.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
			}
			csvWriter.writeNext(columnHeaders.toArray(new String[0]));

			if (limit == -1) {
				while (res.next()) {
					String[] datRow = resultService.getColumnValues(res, false, Constants.Config.DEFAULT_DATE_FORMAT,
							Constants.Config.DEFAULT_TIMESTAMP_FORMAT);
					csvWriter.writeNext(datRow, false);
				}
			} else {
				int count = 0;
				while (res.next() && count <= limit) {
					String[] datRow = resultService.getColumnValues(res, false, Constants.Config.DEFAULT_DATE_FORMAT,
							Constants.Config.DEFAULT_TIMESTAMP_FORMAT);
					csvWriter.writeNext(datRow, false);
					count++;
				}

			}

		} catch (Exception e) {
			throw new DataAccessException("CSV file creation failed", e) {
				private static final long serialVersionUID = 1L;
			};

		} finally {
			try {
				if (csvWriter != null) {
					csvWriter.close();
				}

				/*
				 * if (fw != null) { fw.flush(); fw.close(); }
				 */
			} catch (IOException e) {
				LOGGER.error("", e);
			}
		}

	}

	public static int getRowCount(ResultSet res) throws SQLException {
		res.last();
		int numberOfRows = res.getRow();
		res.beforeFirst();
		return numberOfRows;
	}

	public static String generateUniqueIdWithTimestamp() {

		String op = "";

		try {
			DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss_SS");
			Date currentTime = new Date();

			op = format.format(currentTime);

		} catch (Exception e) {
			LOGGER.error("Error while creating new unique id", e);
		}

		return op;
	}

	public static int getColumnCount(ResultSet res) throws SQLException {
		return res.getMetaData().getColumnCount();
	}
	public static String checkQuerySyntax(Connection con, ILConnectionMapping iMapping) {
		JSONArray jsonArr = null;
		String dVariables = iMapping.getiLConnection().getDbVariables();
		if(StringUtils.isNotBlank(dVariables)) {
			jsonArr = new JSONArray(dVariables);
		}
		return checkQuerySyntax(con, iMapping, jsonArr);
	}
	public static String checkQuerySyntax(Connection con, ILConnectionMapping iMapping,JSONArray jsonDbVar) {

		String message = null;
		Statement stmt = null;
		ResultSet rs = null;
		String formattedQuery = null;
		if (con != null && StringUtils.isNotBlank(iMapping.getiLquery())) {

			String typeOfCommand = iMapping.getTypeOfCommand();

			if ("Query".equals(typeOfCommand)) {

				try {
					stmt = con.createStatement();
					LOGGER.info("Query execution started " + new Date());
					//formattedQuery = "select * from ("+iMapping.getiLquery()+") t";
					formattedQuery = com.anvizent.minidw.client.jdbc.utils.CommonUtils.getFormatedQuery(iMapping.getiLquery(),jsonDbVar);
					try {
						stmt.setMaxRows(10);
					} catch (Exception e) {
						LOGGER.warn("setRows not supported",e);
					}
					rs = stmt.executeQuery(formattedQuery);
					if (rs.next()) {
						
					}
					LOGGER.info("Query execution completed " + new Date());
					message = "Query is OK.";
				} catch (SQLException e) {
					LOGGER.error("", e);
					message = e.getMessage();
				}finally {
					try {
						if (rs != null)
							rs.close();
						if (stmt != null)
							stmt.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}	
				}
			} else {

				CallableStatement cstmt = null;

				try {
					StringBuilder query = new StringBuilder();

					String procParams = iMapping.getProcedureParameters();

					List<Map<String, String>> paramList = new ArrayList<>();

					if (StringUtils.isNotEmpty(procParams)) {
						String[] params = procParams.split("\\^");

						if (params.length > 0) {
							for (String param : params) {
								String[] p = param.split("=");

								if (p.length == 2) {
									Map<String, String> paramMap = new HashMap<>();
									paramMap.put("name", p[0]);
									paramMap.put("value", p[1]);
									paramList.add(paramMap);
								}
							}
						}
					}

					int noofparams = paramList.size();

					query.append("{call ").append(iMapping.getiLquery());

					if (noofparams > 0) {
						query.append("(");
						for (int i = 0; i < noofparams; i++) {
							query.append("?");
							if (i < noofparams - 1)
								query.append(", ");
						}
						query.append(")");
					}

					query.append("}");

					LOGGER.debug("checking stored procedure : " + query);

					cstmt = con.prepareCall(query.toString());

					if (noofparams > 0) {
						int index = 1;
						for (Map<String, String> paramMap : paramList) {
							cstmt.setObject(index++, paramMap.get("value"));
						}
					}

					cstmt.execute();
					message = "Procedure is OK.";
				} catch (Exception e) {
					message = e.getMessage();
				}

			}
		}

		return message;
	}

	public static void removeFile(String folderName, String fileName) {

		File fo = new File(folderName);
		File file = new File(fileName);

		if (file != null && file.exists()) {
			file.delete();
		}
		if (fo != null && fo.exists()) {
			fo.delete();
		}
	}

	public static void createFile(String folderName, String fileName, MultipartFile file) throws Exception {
		if (folderName != null && fileName != null) {
			File folder = new File(folderName);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File fileTemp = new File(fileName);
			if (!fileTemp.exists()) {
				fileTemp.createNewFile();
			}
			file.transferTo(fileTemp);
		}
	}

	public static File createMultipartFile(String folderName, String fileName, MultipartFile file) throws Exception {
		File fileTemp = null;
		if (folderName != null && fileName != null) {
			File folder = new File(folderName);
		    fileTemp = new File(fileName);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			if (!fileTemp.exists()) {
				fileTemp.createNewFile();
			}
			file.transferTo(fileTemp);
		}
		return fileTemp;
	}
	
	public static List<Table> getSchemaRelatedTablesAndColumns(ILConnection iLConnection, String schemaName)
			throws Exception {
		LOGGER.info("in getSchemaRelatedTablesAndColumns()");
		List<Table> tables = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = connectDatabase(iLConnection);
			if (con != null) {
				DatabaseMetaData meta = con.getMetaData();
				String[] types = { "TABLE", "VIEW" };

				// MySQL
				if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MYSQL_DB_TYPE)) {
					String tablesQuery = null;
					String[] name = schemaName.split(",");
					if (iLConnection.isApisData() || iLConnection.isDdLayout()) {
						tablesQuery = "SELECT TABLE_NAME  FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE in('BASE TABLE','VIEW') AND TABLE_SCHEMA=? order by TABLE_NAME";
					} else {
						tablesQuery = "SELECT concat(TABLE_SCHEMA,'.',TABLE_NAME) as TABLE_NAME  FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE in('BASE TABLE','VIEW') AND TABLE_SCHEMA=? order by TABLE_NAME";
					}
					for (String schema : name) {
						pstmt = con.prepareStatement(tablesQuery);
						pstmt.setString(1, schema);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							Table table = new Table();
							table.setTableName(rs.getString(1));
							tables.add(table);
						}
					}

				}
				// SQL Server
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SQLSERVER_DB_TYPE)) {

					String tablesQuery = null;
					String[] name = schemaName.split(",");
					for (String schema : name) {
						String schemaReplaceBrackets = schema.replaceAll("\\[", "").replaceAll("\\]", "");
						tablesQuery = "SELECT  QUOTENAME(TABLE_SCHEMA) + '.' + QUOTENAME(TABLE_NAME) as TABLE_NAME_1  FROM "
								+ schema
								+ ".INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME not in('sysdiagrams') AND TABLE_CATALOG= ? order by TABLE_NAME";
						pstmt = con.prepareStatement(tablesQuery);
						pstmt.setString(1, schemaReplaceBrackets);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							Table table = new Table();
							table.setTableName(schema + "." + rs.getString(1));
							tables.add(table);
						}
					}

				}
				// Oracle
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.ORACLE_DB_TYPE)) {
					String tablesQuery = null;
					String[] name = schemaName.split(",");
					for (String schema : name) {
						tablesQuery = " select TABLE_NAME from SYS.ALL_TABLES  where OWNER = ? union all select VIEW_NAME from SYS.ALL_VIEWS where OWNER =  ? ";
						pstmt = con.prepareStatement(tablesQuery);
						pstmt.setString(1, schema);
						pstmt.setString(2, schema);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							Table table = new Table();
							table.setTableName(schema + "." + rs.getString(1));
							tables.add(table);
						}
					}
				}
				// DB2
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2_DB_TYPE)) {
					String tablesQuery = null;
					String[] name = schemaName.split(",");
					for (String schema : name) {
						tablesQuery = "SELECT  TABNAME FROM SYSCAT.TABLES WHERE ( TABSCHEMA =  ? ) AND ( TYPE = 'T')  order by TABNAME ";
						pstmt = con.prepareStatement(tablesQuery);
						pstmt.setString(1, schema);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							Table table = new Table();
							table.setTableName(schema + "." + rs.getString(1));
							tables.add(table);
						}
					}
				} // DB2AS400 , Microfocus
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2AS400_DB_TYPE)
						|| iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MICROFOCUS_DB_TYPE)
						|| iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.OPENEDGE_DB_TYPE)) {

					String[] name = schemaName.split(",");
					for (String schema : name) {
						rs = meta.getTables(null, schema, null, types);
						while (rs.next()) {
							Table table = new Table();
							table.setTableName(schema + "." + rs.getString("TABLE_NAME"));
							tables.add(table);
						}
					}
				}
				// Salesforce
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SALESFORCE_DB_TYPE)) {
					String[] name = schemaName.split(",");
					for (String schema : name) {
						rs = meta.getTables(null, schema, null, types);
						while (rs.next()) {
							Table table = new Table();
							table.setTableName(schema + "." + rs.getString("TABLE_NAME"));
							tables.add(table);
						}
					}
				}

				// MS Access
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MSACCESS_DB_TYPE)) {

					rs = meta.getTables(null, null, "%", null);
					while (rs.next()) {
						Table table = new Table();
						table.setTableName(rs.getString(3));
						tables.add(table);
					}
				} // postgre sql
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.POSTGRESQL_DB_TYPE)) {
					String tablesQuery = null;
					String[] name = schemaName.split(",");
					for (String schema : name) {
						tablesQuery = "SELECT concat(TABLE_SCHEMA,'.',TABLE_NAME) as TABLE_NAME  FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE in('BASE TABLE','VIEW') AND TABLE_SCHEMA=? order by TABLE_NAME";
						pstmt = con.prepareStatement(tablesQuery);
						pstmt.setString(1, schema);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							Table table = new Table();
							table.setTableName(rs.getString(1));
							tables.add(table);
						}
					}

				}
				Collections.sort(tables, new Comparator<Table>() {
					public int compare(Table result1, Table result2) {
						return result1.getTableName().compareToIgnoreCase(result2.getTableName());
					}
				});
			}
		} finally {
			try {

				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

		return tables;
	}

	public static List<Column> getTableRelatedColumns(ILConnection iLConnection, String schemaName, String tableName)
			throws Exception {
		LOGGER.info("in getTableRelatedColumns()");
		List<Column> columnList = new ArrayList<>();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String columnsQuery = null;
		try {
			con = connectDatabase(iLConnection);
			if (con != null) {
				DatabaseMetaData meta = con.getMetaData();

				// MySQL
				
				if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MYSQL_DB_TYPE)) {

					
					String schema = schemaName;
					String tabName = tableName;
					if ( !iLConnection.isApisData() && !iLConnection.isDdLayout()) {
						schema = tableName.split("\\.")[0];
						tabName = tableName.split("\\.")[1];
					}
					columnsQuery = "SELECT COLUMN_NAME,DATA_TYPE, COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=? AND TABLE_SCHEMA=? ";
					pstmt = con.prepareStatement(columnsQuery);
					pstmt.setString(1, tabName);
					pstmt.setString(2, schema);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						Column column = new Column();
						column.setColumnName(rs.getString(1));
						column.setDataType(rs.getString(2));
						column.setColumnSize(rs.getString(3));
						columnList.add(column);
					}
				}
				// SQL Server
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SQLSERVER_DB_TYPE)) {

					String schema = tableName.split("\\.")[0];
					columnsQuery = " SELECT QUOTENAME(COLUMN_NAME) as COLUMN_NAME, DATA_TYPE, DATA_TYPE + case when DATA_TYPE like '%char%' then '('+ cast(CHARACTER_MAXIMUM_LENGTH as varchar)+')' when DATA_TYPE in( 'numeric','real', 'decimal') then '('+ cast(NUMERIC_PRECISION as varchar)+','+ cast(NUMERIC_SCALE as varchar) +')'   else ''  end as Col_Len "
							+ " FROM  " + schema + ".INFORMATION_SCHEMA.COLUMNS WHERE  ( '" + schema
							+ "' +'.'+QUOTENAME(TABLE_SCHEMA) + '.' + QUOTENAME(TABLE_NAME) ) = ? ";
					pstmt = con.prepareStatement(columnsQuery);
					pstmt.setString(1, tableName);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						Column column = new Column();
						column.setColumnName(rs.getString(1));
						column.setDataType(rs.getString(2));
						column.setColumnSize(rs.getString(3));
						columnList.add(column);
					}
					// Oracle
				} else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.ORACLE_DB_TYPE)) {

					String schema = tableName.split("\\.")[0];
					String tabName = tableName.split("\\.")[1];
					columnsQuery = "SELECT column_name, data_type , DATA_LENGTH  FROM all_tab_columns where table_name = ? AND OWNER= ? ";
					pstmt = con.prepareStatement(columnsQuery);
					pstmt.setString(1, tabName);
					pstmt.setString(2, schema);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						Column column = new Column();
						column.setColumnName(rs.getString(1));
						column.setDataType(rs.getString(2));
						column.setColumnSize(rs.getString(2) + "(" + rs.getString(3) + ")");
						columnList.add(column);
					}
					// DB2
				} else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2_DB_TYPE)) {
					String schema = tableName.split("\\.")[0];
					String tabName = tableName.split("\\.")[1];
					columnsQuery = "select colname, typename ,length from syscat.columns where  TABNAME =  ? and TABSCHEMA = ? ";
					pstmt = con.prepareStatement(columnsQuery);
					pstmt.setString(1, tabName);
					pstmt.setString(2, schema);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						Column column = new Column();
						column.setColumnName(rs.getString(1));
						column.setDataType(rs.getString(2));
						column.setColumnSize(rs.getString(2) + "(" + rs.getString(3) + ")");
						columnList.add(column);
					}
				}
				// DB2AS400
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2AS400_DB_TYPE)
						||iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MICROFOCUS_DB_TYPE)
						||iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.OPENEDGE_DB_TYPE)) {
					String schema = tableName.split("\\.")[0];
					String tabName = tableName.split("\\.")[1];
					rs = meta.getColumns(null, schema, tabName, null);
					while (rs.next()) {
						Column column = new Column();
						column.setColumnName(rs.getString("COLUMN_NAME"));
						column.setDataType(rs.getString("TYPE_NAME"));
						column.setColumnSize(rs.getString("TYPE_NAME") + "(" + rs.getInt("COLUMN_SIZE") + ")");
						columnList.add(column);
					}
				}
				// Salesforce
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SALESFORCE_DB_TYPE)) {
					String schema = tableName.split("\\.")[0];
					String tabName = tableName.split("\\.")[1];
					rs = meta.getColumns(null, schema, tabName, null);
					while (rs.next()) {
						Column column = new Column();
						column.setColumnName(rs.getString("COLUMN_NAME"));
						column.setDataType(rs.getString("TYPE_NAME"));
						column.setColumnSize(rs.getString("TYPE_NAME") + "(" + rs.getInt("COLUMN_SIZE") + ")");
						columnList.add(column);
					}
				}
				// MS Access
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MSACCESS_DB_TYPE)) {
					rs = meta.getColumns(null, null, tableName, null);
					while (rs.next()) {
						Column column = new Column();
						column.setColumnName(rs.getString("COLUMN_NAME"));
						column.setDataType(rs.getString("TYPE_NAME"));
						column.setColumnSize(rs.getString("TYPE_NAME") + "(" + rs.getInt("COLUMN_SIZE") + ")");
						columnList.add(column);
					}
				} // postgre sql
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.POSTGRESQL_DB_TYPE)) {

					String schema = tableName.split("\\.")[0];
					String tabName = tableName.split("\\.")[1];

					columnsQuery = " select column_name,data_type,"
							+ " case when domain_name is not null then domain_name"
							+ " when data_type='character varying' THEN case when character_maximum_length is not null then 'varchar('||character_maximum_length||')' else 'varchar' end "
							+ " when data_type='numeric' then case when numeric_precision is not null  then 'numeric('||numeric_precision||','||numeric_scale||')' else 'numeric' end "
							+ " else data_type end as myType from information_schema.columns " + " where table_name='"
							+ tabName + "' AND TABLE_SCHEMA='" + schema + "'";
					pstmt = con.prepareStatement(columnsQuery);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						Column column = new Column();
						column.setColumnName(rs.getString(1));
						column.setDataType(rs.getString(2));
						column.setColumnSize(rs.getString(3));
						columnList.add(column);
					}
				}

			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

		return columnList;
	}

	public static List<String> getAllSchemasFromDatabase(ILConnection iLConnection) throws Exception {
		LOGGER.info("in getAllSchemasFromDatabase()");
		List<String> schemaList = new ArrayList<>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {

			con = connectDatabase(iLConnection);
			if (con != null) {
				DatabaseMetaData meta = con.getMetaData();
				// My SQL
				if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MYSQL_DB_TYPE)) {
					String shemaQuery = "select schema_name from information_schema.SCHEMATA where schema_name not in('information_schema') order by schema_name";
					pstmt = con.prepareStatement(shemaQuery);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						schemaList.add(rs.getString(1));
					}
				}
				// SQL Server
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SQLSERVER_DB_TYPE)) {

					String shemaQuery = "SELECT quotename(name) as DBName FROM sys.sysdatabases  WHERE HAS_DBACCESS(name) = 1 and (name not in ('master','tempdb','msdb','model') and name not like 'ReportServer$%') ";
					pstmt = con.prepareStatement(shemaQuery);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						schemaList.add(rs.getString(1));
					}
				}
				// Oracle ,Microfocus , Salesforce
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.ORACLE_DB_TYPE)
						|| iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MICROFOCUS_DB_TYPE)
						|| iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SALESFORCE_DB_TYPE)
						|| iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.OPENEDGE_DB_TYPE)) {
					rs = meta.getSchemas();
					while (rs.next()) {
						String schema = rs.getString("TABLE_SCHEM");
						if (isWantedSchema(schema, iLConnection.getDatabase().getConnector_id(),iLConnection.getDatabase().getProtocal())) {
							schemaList.add(schema);
						}
					}
				}
				// DB2
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2_DB_TYPE)) {

					String tablesQuery = " SELECT DISTINCT TABSCHEMA FROM  SYSCAT.TABLES WHERE (TABSCHEMA NOT LIKE 'SYS%') ";
					pstmt = con.prepareStatement(tablesQuery);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						schemaList.add(rs.getString(1));
					}

				}
				// DB2AS400
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2AS400_DB_TYPE)) {
					rs = meta.getSchemas();
					while (rs.next()) {
						String schema = rs.getString("TABLE_SCHEM");
						if (isWantedSchema(schema, iLConnection.getDatabase().getConnector_id(),iLConnection.getDatabase().getProtocal())) {
							schemaList.add(schema);
						}
					}
				}
				// MS Access 
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MSACCESS_DB_TYPE)) {
					schemaList.add(iLConnection.getServer());
				} 
				// PostgreSql 
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.POSTGRESQL_DB_TYPE)) {

					String shemaQuery = "select schema_name from information_schema.SCHEMATA where schema_name not in('information_schema','pg_catalog') order by schema_name";
					pstmt = con.prepareStatement(shemaQuery);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						schemaList.add(rs.getString(1));
					}

				}

				Collections.sort(schemaList, String.CASE_INSENSITIVE_ORDER);
			}

		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				LOGGER.error("", e);
			}

		}
		return schemaList;
	}

	// a helper table were I note the schemas that I'm not interested in
	public static boolean isWantedSchema(String schemaName, int connectionId,String protocal) {
		String[] defaultSchemas = {};
		List<String> defaultSchemasList = new ArrayList<>();
		if (protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MYSQL_DB_TYPE)
				|| protocal.equals(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.POSTGRESQL_DB_TYPE))
			defaultSchemas = new String[] { "mysql", "information_schema", "performance_schema", "innodb", "tmp" };
		else if (protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.ORACLE_DB_TYPE))
			defaultSchemas = new String[] { "ANONYMOUS", "APEX_030200", "APEX_PUBLIC_USER", "APPQOSSYS", "BI", "CTXSYS", 
					"DVF","DVSYS", "LBACSYS", 
					"DBSNMP", "DIP", "EXFSYS", "FLOWS_FILES", "HR", "IX", "MDDATA", "MDSYS", "MGMT_VIEW", "OE",
					"OLAPSYS", "ORACLE_OCM", "ORDDATA", "ORADPLUGINS", "ORDPLUGINS", "ORDSYS", "OUTLN", "OWBSYS", "OWBSYS_AUDIT", "PM",
					"SCOTT", "SH", "SI_INFORMTN_SCHEMA", "SPATIAL_CSW_ADMIN_USR", "SPATIAL_WFS_ADMIN_USR", "SYS",
					"SYSMAN", "SYSTEM", "WMSYS", "XDB", "XS$NULL" };
		else if (protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2_DB_TYPE))
			defaultSchemas = new String[] { "SYSCAT", "SYSFUN", "SYSIBM", "SYSIBMADM", "SYSIBMINTERNAL", "SYSIBMTS",
					"SYSPROC", "SYSPUBLIC", "SYSSTAT", "SYSTOOLS", "NULLID" };
		else if (protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2AS400_DB_TYPE))
			defaultSchemas = new String[] { "SYSCAT", "SYSFUN", "SYSIBM", "SYSIBMADM", "SYSIBMINTERNAL", "SYSIBMTS",
					"SYSPROC", "SYSPUBLIC", "SYSSTAT", "SYSTOOLS", "NULLID" };
		else if (protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SALESFORCE_DB_TYPE))
			defaultSchemas = new String[] { "INFORMATION_SCHEMA", "PUBLIC" };

		boolean wantedSchema = true;
		for (int i = 0; i < defaultSchemas.length; i++) {
			defaultSchemasList.add(defaultSchemas[i]);
		}
		if (defaultSchemasList.contains(schemaName)) {
			wantedSchema = false;
		}
		return wantedSchema;
	}
	public static List<Object> getTablePreview(ILConnectionMapping ilConnectionMapping) throws Exception {
		JSONArray jsonDBVariablesArr = null;
		String dVariables = ilConnectionMapping.getiLConnection().getDbVariables();
		if(StringUtils.isNotBlank(dVariables)) {
			jsonDBVariablesArr = new JSONArray(dVariables);
		}
		return getTablePreview(ilConnectionMapping, jsonDBVariablesArr);
	}
	public static List<Object> getTablePreview(ILConnectionMapping ilConnectionMapping,JSONArray dbVariables) throws Exception {

		SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		String inputQuery = null;
		String formattedQuery = null;
		CallableStatement cstmt = null;
		List<String> columns = new ArrayList<>();
		List<Object> previewtableList = new ArrayList<>();
		ResultSetMetaData metaData = null;
		try {
			conn = CommonUtils.connectDatabase(ilConnectionMapping.getiLConnection());
			if (conn != null) {
				if (ilConnectionMapping.getTypeOfCommand().equals("Query")) {

					inputQuery = ilConnectionMapping.getiLquery();
					String queryTime = startDateFormat.format(new Date());
					LOGGER.info("Query execution started " + queryTime);
					formattedQuery = com.anvizent.minidw.client.jdbc.utils.CommonUtils.getFormatedQuery(inputQuery,dbVariables);
					stmt = conn.prepareStatement(formattedQuery);
					try {
						stmt.setMaxRows(10);
					} catch (Exception e) {
						LOGGER.warn("setRows not supported",e);
					}
					rs = stmt.executeQuery();
					queryTime = startDateFormat.format(new Date());
					LOGGER.info("Query execution Complated " + queryTime);
					metaData = rs.getMetaData();
					if (rs != null) {
						queryTime = startDateFormat.format(new Date());
						LOGGER.info("Preview fetching Started " + queryTime);
						int countcolumns = metaData.getColumnCount();
						for (int i = 1; i <= countcolumns; i++) {
							columns.add(metaData.getColumnLabel(i));
						}
						previewtableList.add(columns);
						int rowCount = 0;
						while (rs.next()) {
							if (rowCount > 10) {
								break;
							}
							String[] datRow = resultService.getColumnValues(rs, false,
									Constants.Config.DEFAULT_DATE_FORMAT, Constants.Config.DEFAULT_TIMESTAMP_FORMAT);
							previewtableList.add(Arrays.asList(datRow));
							rowCount++;
						}
						queryTime = startDateFormat.format(new Date());
						LOGGER.info("Preview fetching Complated " + queryTime);
					}

				} else if (ilConnectionMapping.getTypeOfCommand().equals("Stored Procedure")) {

					//int connectorId = ilConnectionMapping.getiLConnection().getDatabase().getConnector_id();
					String protocal = ilConnectionMapping.getiLConnection().getDatabase().getProtocal();
					// MySQL-1 , DB2 -5 ,DB2AS400-7
					if (protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MYSQL_DB_TYPE)
							|| protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2_DB_TYPE)
							|| protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2AS400_DB_TYPE)) {
						inputQuery = "{ call " + ilConnectionMapping.getiLquery() + "() }";
					}
					// SQL Server-2 , Oracle - 4
					else if (protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SQLSERVER_DB_TYPE)
							|| protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.ORACLE_DB_TYPE)) {
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
						int rowCount = 0;
						while (rs.next()) {
							if (rowCount > 10) {
								break;
							}
							String[] datRow = resultService.getColumnValues(rs, false,
									Constants.Config.DEFAULT_DATE_FORMAT, Constants.Config.DEFAULT_TIMESTAMP_FORMAT);
							previewtableList.add(Arrays.asList(datRow));
							rowCount++;
						}
					}

				}
			}
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}
		return previewtableList;
	}
	

	public static List<String> getQueryHeaders(ILConnectionMapping ilConnectionMapping) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String inputQuery = null;
		CallableStatement cstmt = null;
		List<String> columns = new ArrayList<>();
		ResultSetMetaData metaData = null;
		try {
			conn = CommonUtils.connectDatabase(ilConnectionMapping.getiLConnection());
			if (conn != null) {
				if (ilConnectionMapping.getTypeOfCommand().equals("Query")) {

					inputQuery = ilConnectionMapping.getiLquery();
					LOGGER.info("Query execution started ");
					stmt = conn.prepareStatement(inputQuery);
					try {
						stmt.setMaxRows(10);
					} catch (Exception e) {
						LOGGER.warn("setRows not supported",e);
					}
					rs = stmt.executeQuery();
					LOGGER.info("Query execution Complated ");
					metaData = rs.getMetaData();
					if (rs != null) {
						LOGGER.info("Query Headers fetching Started ");
						int countcolumns = metaData.getColumnCount();
						for (int i = 1; i <= countcolumns; i++) {
							columns.add(metaData.getColumnLabel(i));
						}
						LOGGER.info("Query Headers fetching Completed ");
					}

				} else if (ilConnectionMapping.getTypeOfCommand().equals("Stored Procedure")) {

					//int connectorId = ilConnectionMapping.getiLConnection().getDatabase().getConnector_id();
					String protocal = ilConnectionMapping.getiLConnection().getDatabase().getProtocal();
					// MySQL-1 , DB2 -5 ,DB2AS400-7
					if (protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MYSQL_DB_TYPE)
							|| protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2_DB_TYPE)
							|| protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2AS400_DB_TYPE)) {
						inputQuery = "{ call " + ilConnectionMapping.getiLquery() + "() }";
					}
					// SQL Server-2 , Oracle - 4
					else if (protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SQLSERVER_DB_TYPE)
							|| protocal.contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.ORACLE_DB_TYPE)) {
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
					}

				}
			}
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}
		return columns;
	}

	public static void closeConnction(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) {
		}
	}

	public static void sendFIleToStream(String filePath, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		ServletContext context = request.getServletContext();
		File downloadFile = new File(filePath);
		FileInputStream inputStream = new FileInputStream(downloadFile);

		// get MIME type of the file
		String mimeType = context.getMimeType(filePath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		System.out.println("MIME type: " + mimeType);

		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("inline; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[Constants.Config.BUFFER_SIZE];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();
	}

	public static Boolean isWebApp(String webAppKey) throws Exception {
		String isWebAppDecrypt = null;
		isWebAppDecrypt = EncryptionServiceImpl.getInstance().decrypt(webAppKey);

		Boolean isWebApp = null;
		if (isWebAppDecrypt.equals(Constants.Config.WEBAPP_FALSE)) {
			isWebApp = Boolean.FALSE;
		} else if (isWebAppDecrypt.equals(Constants.Config.WEBAPP_TRUE)) {
			isWebApp = Boolean.TRUE;
		}
		return isWebApp;
	}

	/**
	 * get column headers by query or stored procedure
	 * 
	 * @param ilConnectionMapping
	 * @return
	 */
	public static List<String> getColumnHeadersByQuery(ILConnectionMapping ilConnectionMapping)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = null;
		List<String> columnHeaders = new ArrayList<>();
		try {
			/*
			 * if (!new File(filePath).exists()) {s new File(filePath).mkdir();
			 * }
			 */
			conn = connectDatabase(ilConnectionMapping.getiLConnection());
			Statement stmt = null;
			CallableStatement cstmt = null;

			if (conn != null) {

				String typeOfCommand = ilConnectionMapping.getTypeOfCommand();

				ResultSet res = null;

				boolean isQuery = ("Query".equals(typeOfCommand));

				if (isQuery) {

					String query = ilConnectionMapping.getiLquery();
					stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					res = stmt.executeQuery(query);
				} else {

					String procName = ilConnectionMapping.getiLquery();
					String procParams = ilConnectionMapping.getProcedureParameters();

					List<Map<String, String>> paramList = new ArrayList<>();

					if (StringUtils.isNotEmpty(procParams)) {
						String[] params = procParams.split("\\^");

						if (params.length > 0) {
							for (String param : params) {
								String[] p = param.split("=");

								if (p.length == 2) {
									Map<String, String> paramMap = new HashMap<>();
									paramMap.put("name", p[0]);
									paramMap.put("value", p[1]);
									paramList.add(paramMap);
								}
							}
						}
					}

					int noofparams = paramList.size();

					StringBuilder query = new StringBuilder();

					query.append("{call ").append(procName);

					if (noofparams > 0) {
						query.append("(");
						for (int i = 1; i <= noofparams; i++) {
							query.append("?");
							if (i < noofparams) {
								query.append(", ");
							}
						}
						query.append(")");
					}

					query.append("}");

					cstmt = conn.prepareCall(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

					if (noofparams > 0) {
						int index = 1;
						for (Map<String, String> paramMap : paramList) {
							cstmt.setObject(index++, paramMap.get("value"));
						}
					}

					res = cstmt.executeQuery();

				}
				// set table name as file name
				ResultSetMetaData rsMetaData = res.getMetaData();

				int columnCount = rsMetaData.getColumnCount();

				for (int i = 1; i <= columnCount; i++) {
					String columnHeader = res.getMetaData().getColumnLabel(i);
					columnHeaders.add(columnHeader.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));

				}

			}
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.error("", e);
			}
		}

		return columnHeaders;
	}

	public static List<String> getSchemaByDatabse(ILConnection iLConnection, String databaseName) throws Exception {
		LOGGER.info("in getSchemaByDatabse()");
		List<String> schemas = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String schemasQuery = null;
		try {
			con = connectDatabase(iLConnection);
			if (con != null) {

				// MySQL
				if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.MYSQL_DB_TYPE)) {

				}
				// SQL Server
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SQLSERVER_DB_TYPE)) {
					String replacedDatabaseName = databaseName.replaceAll("\\[", "").replaceAll("\\]", "");
					schemasQuery = " SELECT  DISTINCT QUOTENAME(TABLE_SCHEMA)  FROM " + databaseName
							+ ".INFORMATION_SCHEMA.TABLES WHERE TABLE_CATALOG=? ";
					pstmt = con.prepareStatement(schemasQuery);
					pstmt.setString(1, replacedDatabaseName);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						schemas.add(rs.getString(1));
					}

				}
				// Oracle
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.ORACLE_DB_TYPE)) {

				}
				// DB2
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2_DB_TYPE)) {

				} // DB2AS400
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.DB2AS400_DB_TYPE)) {

				}
				// Salesforce
				else if (iLConnection.getDatabase().getProtocal().contains(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverType.SALESFORCE_DB_TYPE)) {

				}

			}
		} finally {
			try {

				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}

		return schemas;
	}

	public static String replaceDateValue(String incrementalUpdateDate, String ilQuery) throws QueryParcingException {
		if (incrementalUpdateDate == null) {
			return ilQuery;
		} else {

			int startIndex = ilQuery.indexOf("/*");
			int endIndex = 0;
			boolean dateFound = false;

			while (startIndex != -1) {
				endIndex = ilQuery.indexOf("*/", startIndex);

				if (endIndex == -1) {
					ilQuery += "*/";
					endIndex = ilQuery.length() - 2;
					break;
				}
				if (ilQuery.substring(startIndex, endIndex).contains("{date}")) {
					dateFound = true;
					break;
				}
				startIndex = ilQuery.indexOf("/*", endIndex);
			}

			if (!dateFound) {
				throw new QueryParcingException("Commented incremental query part not found");
			}

			String commentedQuery = ilQuery.substring(startIndex + 2, endIndex).replace("{date}",
					"'" + incrementalUpdateDate + "'");
			String removeComentsAddedDate = ilQuery.substring(0, startIndex) + commentedQuery
					+ ilQuery.substring(endIndex + 2);

			return removeComentsAddedDate;
		}
	}

	public static int getRowCountOfQueryOrSP(ILConnectionMapping ilConnectionMapping, String incrementalUpdateDate)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
			QueryParcingException {
		int rowCount = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		CallableStatement cstmt = null;
		ResultSet res = null;
		try {
			conn = connectDatabase(ilConnectionMapping.getiLConnection());
			if (conn != null) {
				String typeOfCommand = ilConnectionMapping.getTypeOfCommand();
				boolean isQuery = ("Query".equals(typeOfCommand));
				if (isQuery) {
					String query = ilConnectionMapping.getiLquery();
					query = replaceDateValue(incrementalUpdateDate, query);

					String sql = "Select count(*) as row_count from (" + query + ") count_table";
					pstmt = conn.prepareStatement(sql);
					try {
						res = pstmt.executeQuery();
						if (res != null && res.next()) {
							rowCount = res.getInt("row_count");
						}
					} catch (Exception e) {
						LOGGER.error("Unable to fetch rowcount for the source query " + sql);
						rowCount = 0;
					}

				} else {
					String procName = ilConnectionMapping.getiLquery();
					String procParams = ilConnectionMapping.getProcedureParameters();
					List<Map<String, String>> paramList = new ArrayList<>();

					if (StringUtils.isNotEmpty(procParams)) {
						String[] params = procParams.split("\\^");

						if (params.length > 0) {
							for (String param : params) {
								String[] p = param.split("=");
								if (p.length == 2) {
									Map<String, String> paramMap = new HashMap<>();
									paramMap.put("name", p[0]);
									paramMap.put("value", p[1]);
									paramList.add(paramMap);
								}
							}
						}
					}

					int noofparams = paramList.size();
					StringBuilder query = new StringBuilder();
					query.append("{call ").append(procName);

					if (noofparams > 0) {
						query.append("(");
						for (int i = 1; i <= noofparams; i++) {
							query.append("?");
							if (i < noofparams) {
								query.append(", ");
							}
						}
						query.append(")");
					}
					query.append("}");
					cstmt = conn.prepareCall(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

					if (noofparams > 0) {
						int index = 1;
						for (Map<String, String> paramMap : paramList) {
							cstmt.setObject(index++, paramMap.get("value"));
						}
					}
					res = cstmt.executeQuery();
					if (res != null && res.next()) {
						res.last();
						rowCount = res.getRow();
					}
				}
			}
		} finally {
			try {

				if (res != null) {
					res.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

		return rowCount;
	}

	public static String getClientBrowserDetails(HttpServletRequest request) {
		StringBuilder clientInformation = new StringBuilder();
		if (request != null) {
			String userAgent = request.getHeader("User-Agent");
			String ipaddress = request.getRemoteAddr();
			clientInformation.append("IP Address: ").append(ipaddress).append("\t");
			clientInformation.append("Browser Details:").append(userAgent).append("");
		}
		return clientInformation.toString();
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getAvailableJarsList(HttpServletRequest request,
			RestTemplateUtilities restUtilities, String type) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getAvailableJarsList", user.getUserId());
		if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<Map<String, Object>> jars = (List<Map<String, Object>>) dataResponse.getObject();
			if (!type.equals("all")) {
				jars = jars.stream().filter(jar -> jar.get("fileName").toString().startsWith(type))
						.collect(Collectors.toList());
			}
			return jars;
		} else {
			return new ArrayList<>();
		}
	}

	public static void setActiveScreenName(String screenName, HttpSession session) {
		session.setAttribute("activePage", screenName);
	}

	public static String getPhysicalAddress() {
		String macAddress = "";
		InetAddress ip;
		try {

			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();

			System.out.print("Current MAC address : ");

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			macAddress = sb.toString();
			System.out.println(sb.toString());

		} catch (Exception e) {
			System.out.println("Error occured while retrieving Mac address " + e.getMessage());
		}
		return macAddress;
	}

	public static ILConnection getIlConnection(ServerConfigurations serverConfigurations) {

		ILConnection ilConnection = new ILConnection();
		Database database = new Database();
		database.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
		database.setProtocal(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
		database.setDriverName(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
		ilConnection.setServer(serverConfigurations.getIpAddress() + ":" + serverConfigurations.getPortNumber() + "/"
				+ serverConfigurations.getMinidwSchemaName());
		ilConnection.setUsername(serverConfigurations.getUserName());
		ilConnection.setPassword(serverConfigurations.getServerPassword());
		ilConnection.setDatabase(database);

		return ilConnection;
	}

	public static List<com.datamodel.anvizent.service.model.Package> getPackageList(ILConnection ilConnection,
			String serverClientId)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<com.datamodel.anvizent.service.model.Package> packageList = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = connectDatabase(ilConnection);
			pstmt = con.prepareStatement(
					"select * from package where user_id in (select userid from user where client_id = ?)");
			pstmt.setString(1, serverClientId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				com.datamodel.anvizent.service.model.Package packageFromServer = new com.datamodel.anvizent.service.model.Package();
				packageFromServer.setPackageId(rs.getInt("package_id"));
				packageFromServer.setPackageName(rs.getString("package_name"));
				packageFromServer.setIsStandard(rs.getBoolean("isStandard"));
				packageList.add(packageFromServer);
			}
		} finally {
			try {

				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return packageList;
	}

	public static List<com.datamodel.anvizent.service.model.User> getUsersList(ILConnection ilConnection,
			TemplateMigration templateMigration)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<com.datamodel.anvizent.service.model.User> userList = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = CommonUtils.connectDatabase(ilConnection);
			pstmt = con.prepareStatement(
					"select userId,user_name from user where userId in ( select distinct user_id from package where package_id in (?))");
			pstmt.setString(1, templateMigration.getPackageIds());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				com.datamodel.anvizent.service.model.User userDetails = new com.datamodel.anvizent.service.model.User();
				userDetails.setUserId(rs.getString("userId"));
				userDetails.setUserName(rs.getString("user_name"));
				userList.add(userDetails);
			}
		} finally {
			try {

				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return userList;
	}

	public static List<com.datamodel.anvizent.service.model.ILConnectionMapping> getDatabaseConnectionList(
			ILConnection ilConnection, TemplateMigration templateMigration)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<com.datamodel.anvizent.service.model.ILConnectionMapping> iLConnectionMappingList = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = CommonUtils.connectDatabase(ilConnection);
			pstmt = con
					.prepareStatement("select distinct connection_id from  il_connection_mapping where Package_id in ("
							+ templateMigration.getPackageIds()
							+ ") and connection_id IS NOT NULL and connection_id != 0");
			// pstmt.setString(1, templateMigration.getPackageIds());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				com.datamodel.anvizent.service.model.ILConnectionMapping iLConnectionMapping = new com.datamodel.anvizent.service.model.ILConnectionMapping();
				ILConnection iLConnection = new ILConnection();
				iLConnection.setConnectionId(rs.getInt("connection_id"));
				iLConnectionMapping.setiLConnection(iLConnection);
				iLConnectionMappingList.add(iLConnectionMapping);
			}
		} finally {
			try {

				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return iLConnectionMappingList;
	}

	public static List<com.datamodel.anvizent.service.model.ILConnectionMapping> getWsConnectionList(
			ILConnection ilConnection, TemplateMigration templateMigration)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<com.datamodel.anvizent.service.model.ILConnectionMapping> iLConnectionMappingList = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = CommonUtils.connectDatabase(ilConnection);
			pstmt = con
					.prepareStatement("select distinct webservice_Id from  il_connection_mapping where Package_id in ("
							+ templateMigration.getPackageIds()
							+ ") and webservice_Id IS NOT NULL and webservice_Id != 0");
			// pstmt.setString(1, templateMigration.getPackageIds());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				com.datamodel.anvizent.service.model.ILConnectionMapping iLConnectionMapping = new com.datamodel.anvizent.service.model.ILConnectionMapping();
				iLConnectionMapping.setWsConId((rs.getInt("webservice_Id")));
				iLConnectionMappingList.add(iLConnectionMapping);
			}
		} finally {
			try {

				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return iLConnectionMappingList;
	}
	public static void multipartFileToLib(MultipartFile file,String libPath) throws IOException{ 
		 if (!file.isEmpty()) {
      	  try {
          String uploadPath = libPath + File.separator;
          FileCopyUtils.copy(file.getBytes(), new File(uploadPath+file.getOriginalFilename()));
          } catch (IOException e) {
		    LOGGER.error("", e);
		  }
      }
	}
	public static String multipartFileToLibPath(MultipartFile file,String libPath) throws IOException{ 
		String uploadPath = null;
		 if (!file.isEmpty()) {
     	  try {
           uploadPath = libPath + File.separator+file.getOriginalFilename();
         FileCopyUtils.copy(file.getBytes(), new File(uploadPath));
         } catch (IOException e) {
		    LOGGER.error("", e);
		  }
     }
		 return uploadPath;
	}
	public static List<String> stringToList(String strArr, String splitChar) {
		List<String> list = new ArrayList<>();
		String[] arr = strArr.split(splitChar);
		for (String str : arr) {
			list.add(str);
		}
		return list;

	}
	public static String decryptedUserId(String encryptedUserId) throws Exception {
		String userId = EncryptionServiceImpl.getInstance().decrypt(encryptedUserId);
		String[] userIdAndLocale = StringUtils.split(userId, '#');
		return userIdAndLocale[0];
	}
}
