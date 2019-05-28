package com.anvizent.client.metadata.update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class DBToSQLExecution {
	public static final char QUOTE_CHAR = '`';
	public DBToSQLExecution() {
		System.out.println("DB TO Sql Connection");
	}
	
	public List<String> dumpData(Connection dbConn, Connection destConn, List<String> tableNames ) throws SQLException {
		return dumpData(dbConn, destConn, tableNames, false);
	}

	public List<String> dumpData(Connection dbConn, Connection destConn, List<String> tableNames,boolean isTruncateInsert) throws SQLException {

		List<String> errorInfo = new ArrayList<>();

		executeInsertScripts(destConn, "set foreign_key_checks=0");
		for (String tblName : tableNames) {

			try {
				if (tblName == null || tblName.length() == 0) {
					continue;
				}
				if (isTruncateInsert) {
					executeInsertScripts(destConn, "truncate table " + tblName);
				}
				List<String> insertStatements = getInsertStatements(dbConn, tblName);
				executeInsertScripts(destConn, insertStatements);
			} catch (SQLException e) {
				errorInfo.add(tblName + " execution failed due to " + e.getMessage());
			}
		}
		executeInsertScripts(destConn, "set foreign_key_checks=1");
		return errorInfo;
	}
	
	
	public List<String> dumpData(Connection destConn,List<String> queries, String tableName,boolean isTruncateInsert) throws SQLException {

		List<String> errorInfo = new ArrayList<>();

		executeInsertScripts(destConn, "set foreign_key_checks=0");

			try {
				if (isTruncateInsert) {
					executeInsertScripts(destConn, "truncate table " + tableName);
				}
				executeInsertScripts(destConn, queries);
			} catch (SQLException e) {
				errorInfo.add(tableName + " execution failed due to " + e.getMessage());
			}
		executeInsertScripts(destConn, "set foreign_key_checks=1");
		return errorInfo;
	}
	

	private  void executeInsertScripts(Connection destConn, String query) throws SQLException {
		executeInsertScripts(destConn, Arrays.asList(query));
	}
	
	public void executeInsertScripts(Connection destConn, List<String> queriesList) throws SQLException {
		Statement statement = null;
		try {
			statement = destConn.createStatement();
			for (String query : queriesList) {
				statement.addBatch(query);
			}
			int[] count = statement.executeBatch();
			System.out.println(count);
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	public List<String> getInsertStatements(Connection dbConn, List<String> tableNames) throws SQLException {
		return getInsertStatements(dbConn, tableNames, false);
	}

	public List<String> getInsertStatements(Connection dbConn, List<String> tableNames, boolean isTableTruncationRequired ) throws SQLException {
		List<String> queries = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		for (String tableName : tableNames) {
			try {
				
				if (isTableTruncationRequired) {
					queries.add("truncate table " + QUOTE_CHAR + tableName + QUOTE_CHAR+ "");
				}
				
				preparedStatement = dbConn.prepareStatement("SELECT * FROM " + QUOTE_CHAR + tableName + QUOTE_CHAR);
				resultSet = preparedStatement.executeQuery();
				ResultSetMetaData metaData = resultSet.getMetaData();
				int columnCount = metaData.getColumnCount();
				StringBuilder stringBuilder = new StringBuilder("INSERT INTO " + QUOTE_CHAR + tableName + QUOTE_CHAR + "( ");
				addColumnNames(stringBuilder, columnCount, metaData);
				while (resultSet.next()) {
					stringBuilder.append("(");

					for (int i = 0; i < columnCount; i++) {
						if (i > 0) {
							stringBuilder.append(", ");
						}

						Object value = resultSet.getObject(i + 1);
						if (value instanceof Integer || value instanceof Long || value instanceof Float
								|| value instanceof Byte || value instanceof Short || value instanceof java.math.BigDecimal
								|| value instanceof Boolean) {
							stringBuilder.append(value);
						} else if (value instanceof String || value instanceof Character || value instanceof Date
								|| value instanceof java.sql.Date || value instanceof Timestamp) {
							stringBuilder.append("'" + value.toString().replaceAll("'", "''").replaceAll("\r\n", " \t") + "'");
						} else if (value == null) {
							stringBuilder.append("NULL");
						} else {
							stringBuilder.append(value);
						}
					}

					if(!resultSet.isLast()){
						stringBuilder.append("),\n");
					}else{
						stringBuilder.append(")");
					}
				}
				queries.add(stringBuilder.toString());
			} finally {
				try {
					if (resultSet != null) {
						resultSet.close();
					}
					if (preparedStatement != null) {
						preparedStatement.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return queries;
	}
	
	
	
	public List<String> getInsertStatements(Connection dbConn, String tableName) throws SQLException {
		return getInsertStatements(dbConn, Arrays.asList(tableName));
	}


	
	public void addColumnNames(StringBuilder stringBuilder, int columnCount, ResultSetMetaData metaData)
			throws SQLException {
		for (int i = 1; i <= columnCount; i++) {
			stringBuilder.append(QUOTE_CHAR + metaData.getColumnName(i) + QUOTE_CHAR);

			if (i < columnCount) {
				stringBuilder.append(',');
				stringBuilder.append(' ');
			}
		}

		stringBuilder.append(") VALUES ");
	}



}