/**
 * 
 */
package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.helper.CrossReferenceConstants;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.dao.CrossReferenceDao;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CrossReferenceForm;
import com.datamodel.anvizent.service.model.CrossReferenceLogs;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.User;

/**
 * @author rajesh.anthari Created Date: 14-Feb-2017 Last Modified Date:
 *         14-Feb-2017
 */
@Repository
public class CrossReferenceDaoImpl implements CrossReferenceDao
{

	private static final Logger LOG = LoggerFactory.getLogger(CrossReferenceDaoImpl.class);
	
	private @Value("${crosssreference.auto.merge.classname:local_project.il_xref_auto_merge_main_v2_0_1.IL_XRef_Auto_Merge_Main_v2}") String AUTO_MERGE_JOB_CLASS; // = "local_project.il_xref_auto_merge_main_v1_0_1.IL_XRef_Auto_Merge_Main_v1";
	private @Value("${crosssreference.dependancyjars.list.auto.merge:il_xref_auto_merge_main_v2_0_1.jar,il_xref_auto_merge_v2_0_1.jar}") String AUTO_MERGE_DEPENDENCY_JARS;// = "il_xref_auto_merge_main_v1_0_1.jar,il_xref_auto_merge_v1_0_1.jar";
	
	private SqlHelper sqlHelper;

	List<String> excludeColumnsList = new ArrayList<>();

	public CrossReferenceDaoImpl()
	{
		try
		{
			this.sqlHelper = new SqlHelper(CrossReferenceDaoImpl.class);
			excludeColumnsList.add("Added_Date");
			excludeColumnsList.add("Added_User");
			excludeColumnsList.add("Updated_Date");
			excludeColumnsList.add("Updated_User");
		}
		catch ( SQLException ex )
		{
			throw new DataAccessResourceFailureException("Error creating CrossReferenceDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public List<ILInfo> getAllClientILsForXref(String clientId, JdbcTemplate clientAppDbJdbcTemplate)
	{

		LOG.info("in getAllClientILsForXref()");
		List<ILInfo> ilList = null;
		try
		{
			String sql = sqlHelper.getSql("getAllClientILs");
			ilList = clientAppDbJdbcTemplate.query(sql, new Object[] { clientId, clientId }, new RowMapper<ILInfo>()
			{
				public ILInfo mapRow(ResultSet rs, int i) throws SQLException
				{
					ILInfo iLInfo = new ILInfo();
					iLInfo.setiL_id(rs.getInt("IL_id"));
					iLInfo.setiL_name(rs.getString("IL_name"));
					iLInfo.setiL_table_name(rs.getString("il_table_name"));
					iLInfo.setXref_il_table_name(rs.getString("xref_il_table_name"));
					iLInfo.setStandard(rs.getBoolean("is_mapping_completed"));

					return iLInfo;
				}

			});

		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while getAllClientILsForXref", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while getAllClientILsForXref", e);

		}

		return ilList;
	}

	@Override
	public List<Object> getDistinctValues(String columnName, String tableName, String xRefTableName, String columnValue, boolean isXrefValueNull, String xRefColumnName, JdbcTemplate clientJdbcTemplate)
	{

		List<Object> valuesList = null;
		List<String> params = new ArrayList<>();

		if( StringUtils.isBlank(columnName) || columnName.equals("0") )
		{
			return null;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("select distinct `").append(columnName).append("` from `").append((isXrefValueNull ? tableName : xRefTableName)).append("`");

		StringBuilder xrefCondition = new StringBuilder();
		if( isXrefValueNull )
		{
			// xrefCondition.append(" `").append(xRefColumnName).append("` is
			// null");
		}
		else
		{
			xrefCondition.append(" `").append(xRefColumnName).append("` is not null and `").append(xRefColumnName).append("` in (SELECT `").append(xRefColumnName).append("` FROM `").append(tableName).append("` where `").append(xRefColumnName).append("` is not null group by `").append(xRefColumnName)
					.append("` having count(1)>0)");
		}
		if( StringUtils.isNotBlank(columnName) && StringUtils.isNotBlank(columnValue) )
		{
			params.add("%" + columnValue + "%");
			sql.append(" where `").append(columnName).append("` like ? ").append(StringUtils.isNotBlank(xrefCondition.toString()) ? " and" + xrefCondition : "");
		}
		else
		{
			sql.append(" where ").append(xrefCondition);
		}
		sql.append("order by `").append(columnName).append("` asc ");
		sql.append(" limit 500");

		valuesList = clientJdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<Object>()
		{
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				String object = rs.getString(1);
				if( StringUtils.isEmpty(object) || "null".equalsIgnoreCase(object) )
				{
					return null;
				}
				else
				{
					return object;
				}

			}
		});
		valuesList.removeAll(Collections.singleton(null));

		return valuesList;

	}

	@Override
	public Map<String, Object> getMatchedRecordWithMap(String columnName, List<String> values, String tableName, String xRefTableName, boolean isXrefValueNull, String xRefColumnName, JdbcTemplate clientJdbcTemplate)
	{
		LOG.info("in getMatchedRecordWithMap()");

		final Map<String, Object> matchedRecordsListData = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from `").append(tableName).append("`");

		StringBuilder builder = new StringBuilder();
		values.forEach(value ->
		{
			builder.append("?,");
		});

		StringBuilder xrefCondition = new StringBuilder();
		if( isXrefValueNull )
		{
			xrefCondition.append(" `").append(columnName).append("` in ( ").append(builder.deleteCharAt(builder.length() - 1).toString()).append(" ) ");// append("
																																						// )
																																						// and
																																						// ").append("
																																						// `").append(xRefColumnName).append("`
																																						// is
																																						// null");
		}
		else
		{
			xrefCondition.append(" `").append(xRefColumnName).append("` in (SELECT `").append(xRefColumnName).append("` FROM `").append(tableName).append("` where `").append(xRefColumnName).append("` in ( select `").append(xRefColumnName).append("` from `").append(xRefTableName).append("` where ")
					.append(" `").append(columnName).append("` in ( ").append(builder.deleteCharAt(builder.length() - 1).toString()).append(") )").append("group by `").append(xRefColumnName).append("` having count(1)>0 )").append("order by `").append(xRefColumnName).append("` ");
		}
		if( values.size() > 0 )
		{
			sql.append(" where ").append(xrefCondition);
		}
		else
		{
			sql.append(" where 1!=1 ");
		}

		List<String> exceptionColumnsList = new ArrayList<>(this.excludeColumnsList);
		clientJdbcTemplate.execute(new ConnectionCallback<Object>()
		{
			@Override
			public Object doInConnection(Connection con) throws SQLException, DataAccessException
			{
				java.sql.DatabaseMetaData metaData = con.getMetaData();
				ResultSet rs = metaData.getColumns(null, null, tableName, null);
				String autoIncrementColumnName = "";
				String xRefKeyColumnName = "";
				while (rs.next())
				{
					if( rs.getString("IS_AUTOINCREMENT").equals("YES") )
					{
						autoIncrementColumnName = rs.getString("COLUMN_NAME");
					}

					if( rs.getString("COLUMN_NAME").startsWith("XRef_") )
					{
						xRefKeyColumnName = rs.getString("COLUMN_NAME");
					}
				}
				exceptionColumnsList.add(autoIncrementColumnName);
				exceptionColumnsList.add(xRefKeyColumnName);
				matchedRecordsListData.put("autoincrement_column_name", autoIncrementColumnName);
				matchedRecordsListData.put("xRefKeyColumnName", xRefKeyColumnName);
				return null;
			}
		});

		clientJdbcTemplate.query(sql.toString(), values.toArray(), new ResultSetExtractor<Map<String, Object>>()
		{
			@Override
			public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException
			{

				exceptionColumnsList.add("");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				List<Map<String, Object>> valuesList = new ArrayList<>();
				List<String> headerDataforDisplay = new ArrayList<>();
				List<String> headerData = new ArrayList<>();
				for (int c = 1; c <= columnCount; c++)
				{
					if( exceptionColumnsList.indexOf(rsmd.getColumnLabel(c)) == -1 )
					{
						headerDataforDisplay.add(rsmd.getColumnLabel(c));
					}
					headerData.add(rsmd.getColumnLabel(c));

				}
				matchedRecordsListData.put("headersList", headerDataforDisplay);

				while (rs.next())
				{
					Map<String, Object> rowData = new HashMap<>();
					for (int c = 0; c < columnCount; c++)
					{
						rowData.put(headerData.get(c), rs.getString(headerData.get(c)));
					}
					valuesList.add(rowData);
				}
				matchedRecordsListData.put("valuesList", valuesList);
				return null;
			}
		});

		return matchedRecordsListData;
	}

	@Override
	public Map<String, Object> mergeXreferenceRecords(String ilTableName, String xRefTableName, String autoincrementColumnName, String xRefKeyColumnName, List<Column> columns, List<String> mergeColumnValues, String xReferenceColumnDataValues, String columnName, String ilXreferenceValue,
			String conditionName, JdbcTemplate clientJdbcTemplate)
	{
		LOG.info("in mergeXreferenceRecords()");
		boolean mergeStatus = false;
		long generatedXrefkey = 0L;
		Map<String, Object> returnParams = new HashMap<String, Object>();
		try
		{
			List<String> excludeColumnsList = new ArrayList<>(this.excludeColumnsList);
			excludeColumnsList.add(autoincrementColumnName);
			excludeColumnsList.add(xRefKeyColumnName);

			String columnsList = getInsertColumnsList(autoincrementColumnName, xRefKeyColumnName, columns, excludeColumnsList);

			String columnParamsPlaceholder = getColumnParamPlaceholders(autoincrementColumnName, xRefKeyColumnName, columns, excludeColumnsList);

			String insertIntoSql = prepareXRefInsertIntoSQLStatement(columnsList, columnParamsPlaceholder, xRefTableName);

			List<Column> dataParams = getDataSetList(columns, xReferenceColumnDataValues, excludeColumnsList);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator()
			{
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
				{
					PreparedStatement pst = connection.prepareStatement(insertIntoSql, new String[] { "xref_key" });
					int columnsSize = dataParams.size();
					for (int i = 0, j = 1; i < columnsSize; i++, j++)
					{
						Column c = dataParams.get(i);
						if( c.getDataType().toLowerCase().equals("bit") )
						{
							pst.setBoolean(j, Boolean.parseBoolean(c.getDefaultValue()));
						}
						else
						{
							pst.setObject(j, c.getDefaultValue());
						}
					}
					return pst;
				}
			}, keyHolder);
			if( keyHolder != null )
			{
				Number autoIncrement = keyHolder.getKey();
				generatedXrefkey = autoIncrement.longValue();
			}
			if( generatedXrefkey != 0L )
			{
				List<String> updateParamsList = new ArrayList<>(mergeColumnValues);
				updateParamsList.add(0, generatedXrefkey + "");
				String updateSql = prepareXRefUpdateSQLStatement(xRefKeyColumnName, autoincrementColumnName, ilTableName, mergeColumnValues.size());
				int updatedCount = clientJdbcTemplate.update(updateSql, updateParamsList.toArray());
				updateXrefConditionNameinXrefTable(xRefTableName, conditionName, xRefKeyColumnName, generatedXrefkey, clientJdbcTemplate);
				if( updatedCount == mergeColumnValues.size() )
				{	
					mergeStatus = true;
				}
				else
				{
					LOG.info("X-Ref updation failed");
					LOG.info("No. of records to be updated is " + (mergeColumnValues.size()));
					LOG.info("But Updated count is " + updatedCount);
					LOG.info("So reverting xref record from " + xRefTableName + " table ");

					updateParamsList = new ArrayList<>(mergeColumnValues);
					updateParamsList.add(0, null);
					updateParamsList.add(xReferenceColumnDataValues);
					/* set null to updated records */
					clientJdbcTemplate.update(updateSql, updateParamsList.toArray());
					/* delete from xref table */
					String deleteXrefRecordSql = prepareXRefDeleteSQLStatement(xRefKeyColumnName, xRefTableName);
					clientJdbcTemplate.update(deleteXrefRecordSql, generatedXrefkey);
				}
			}
			returnParams.put("mergeStatus", mergeStatus);
			returnParams.put("xref_key", generatedXrefkey);
		}
		catch ( ArrayIndexOutOfBoundsException e )
		{
			LOG.error("Array index error");
			throw new AnvizentRuntimeException("Unable to process your request");
		}
		catch ( NonTransientDataAccessException e )
		{
			LOG.error("Array index error");
			throw new AnvizentRuntimeException(e.getRootCause().getMessage());
		}
		catch ( Exception e )
		{
			LOG.error("error while mergeXreferenceRecords()", e);
			throw new AnvizentRuntimeException(e);
		}

		return returnParams;
	}

	private void updateXrefConditionNameinXrefTable(String xRefTableName, String conditionName, String xrefKeyColumnName, long xrefkey,
			JdbcTemplate clientJdbcTemplate)
	{
		try
		{
			clientJdbcTemplate.execute(new ConnectionCallback<Object>()
			{
				boolean isColumnExists = false ;
				@Override
				public Object doInConnection(Connection con) throws SQLException, DataAccessException
				{
					java.sql.DatabaseMetaData metaData = con.getMetaData();
					ResultSet rs = metaData.getColumns(null, null, xRefTableName, "XRefName");
					while (rs.next())
					{
						isColumnExists = true;
					}
					String updateConditionNameSQL = prepareXrefTableUpdateQuery(xRefTableName, xrefKeyColumnName);
					PreparedStatement pst = con.prepareStatement(updateConditionNameSQL);
					if(isColumnExists) {
						pst.setString(1, conditionName);
						pst.setLong(2, xrefkey);
						return pst.executeUpdate();
					}else {
						addXrefNameColumnandUpdate(xRefTableName, conditionName, xrefKeyColumnName, xrefkey, clientJdbcTemplate);
						pst.setString(1, conditionName);
						pst.setLong(2, xrefkey);
						return pst.executeUpdate();
					}
				}
			});
		}
		catch ( Exception e )
		{
			throw new RuntimeException("Error while updating xrefName in xref table " + e);
		}
	}

	private void addXrefNameColumnandUpdate(String xRefTableName, String conditionName, String xrefKeyColumnName, long xrefkey,
			JdbcTemplate clientJdbcTemplate) {
		try {
			StringBuilder addColumnStatement = new StringBuilder();
			addColumnStatement.append("ALTER TABLE `").append(xRefTableName).append("` ADD COLUMN `XRefName` VARCHAR(200) NULL DEFAULT NULL");
			clientJdbcTemplate.update(addColumnStatement.toString());
 		}catch( Exception e) {
			throw new RuntimeException("Error while alter "+ xRefTableName + " table " + e);
		}
	}

	private String prepareXrefTableUpdateQuery(String xRefTableName, String xrefKeyColumnName)
	{
		StringBuilder query = new StringBuilder();
		query.append("update `").append(xRefTableName).append("` set `XRefName` = ? ").append("where `").append(xrefKeyColumnName).append("` = ?");
		return query.toString();
	}

	private List<Column> getDataSetList(List<Column> columns, String xReferenceColumnDataValues, List<String> excludeColumnsList)
	{
		JSONObject dataColumns = new JSONObject(xReferenceColumnDataValues);
		List<Column> dataObj = new ArrayList<>();
		columns.forEach(column ->
		{
			if( excludeColumnsList.indexOf(column.getColumnName()) == -1 )
			{
				String columnValue = dataColumns.getString(column.getColumnName());
				Column newColumn = new Column();
				newColumn.setDataType(column.getDataType());
				newColumn.setDefaultValue(StringUtils.isNotBlank(columnValue) ? columnValue : null);
				dataObj.add(newColumn);
			}

		});
		return dataObj;
	}

	@Override
	public boolean splitXreferenceRecords(String ilTableName, String xRefTableName, String autoincrementColumnName, String xRefKeyColumnName, List<Column> columns, List<String> splitColumnValues, JdbcTemplate clientJdbcTemplate)
	{
		LOG.info("in splitXreferenceRecords()");
		boolean splitStatus = false;
		try
		{
			for (String splitValue : splitColumnValues)
			{
				String updateSql = prepareXRefUpdateSQLStatement(xRefKeyColumnName, autoincrementColumnName, ilTableName, 1);
				clientJdbcTemplate.update(updateSql, new Object[] { null, splitValue });
			}

			splitStatus = true;
		}
		catch ( ArrayIndexOutOfBoundsException e )
		{

			LOG.error("Array index error");
			throw new AnvizentRuntimeException("Unable to process your request");
		}
		catch ( Exception e )
		{
			LOG.error("error while mergeXreferenceRecords()", e);
			throw new AnvizentRuntimeException(e);
		}

		return splitStatus;
	}

	public String getInsertColumnsList(String autoincrementColumnName, String xRefKeyColumnName, List<Column> columns, List<String> excludeColumnsList) throws Exception
	{

		StringBuilder columnsList = new StringBuilder();
		for (Column column : columns)
		{
			if( excludeColumnsList.indexOf(column.getColumnName()) == -1 )
			{
				columnsList.append("`").append(column.getColumnName()).append("`,");
			}
		}
		if( columnsList.length() > 0 )
		{
			columnsList.deleteCharAt(columnsList.length() - 1);
		}

		return columnsList.toString();
	}

	public String getColumnParamPlaceholders(String autoincrementColumnName, String xRefKeyColumnName, List<Column> columns, List<String> excludeColumnsList) throws Exception
	{

		StringBuilder columnsList = new StringBuilder();
		for (Column column : columns)
		{
			if( excludeColumnsList.indexOf(column.getColumnName()) == -1 )
			{
				columnsList.append("?").append(",");
			}
		}
		if( columnsList.length() > 0 )
		{
			columnsList.deleteCharAt(columnsList.length() - 1);
		}

		return columnsList.toString();
	}

	@SuppressWarnings("unused")
	private String prepareXRefInsertIntoSQLStatementold(String columnsList, String autoincrementColumnName, String xRefKeyColumnName, boolean isXrefValueNull, String xRefTableName, String ilTableName, String columnName, String ilXreferenceValue, String splitOrMerge)
	{
		StringBuilder insertSql = new StringBuilder();
		String xrefCondition = "";
		if( isXrefValueNull )
		{
			xrefCondition = " `" + xRefKeyColumnName + "` is null";
		}
		else
		{
			xrefCondition = " `" + xRefKeyColumnName + "` is not null";
		}

		String ilColumnsList = columnsList;

		if( splitOrMerge.equals("merge") )
		{
			ilColumnsList = StringUtils.replace(ilColumnsList, "`" + columnName + "`", "?");
		}

		insertSql.append("INSERT INTO `").append(xRefTableName).append("`(").append(columnsList).append(")").append(" SELECT ").append(ilColumnsList).append(" FROM `").append(ilTableName).append("` WHERE `").append(autoincrementColumnName).append("` = ? and ").append(xrefCondition);

		return insertSql.toString();
	}

	private String prepareXRefInsertIntoSQLStatement(String columnsList, String columnParamsPlaceholder, String xRefTableName)
	{
		StringBuilder insertSql = new StringBuilder();

		insertSql.append("INSERT INTO `").append(xRefTableName).append("`(").append(columnsList).append(") values( ").append(columnParamsPlaceholder).append(" )");

		return insertSql.toString();
	}

	public String prepareXRefUpdateSQLStatement(String xRefKeyColumnName, String autoincrementColumnName, String ilTableName, int noOfRecordsToBeUpdated)
	{
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("UPDATE `").append(ilTableName).append("` SET `").append(xRefKeyColumnName).append("` = ? where `").append(autoincrementColumnName).append("` in ( ");
		for (int c = 0; c < noOfRecordsToBeUpdated; c++)
		{
			if( c != 0 )
			{
				updateSql.append(",");
			}
			updateSql.append(" ?");
		}
		updateSql.append(" ) ");
		return updateSql.toString();
	}

	public String prepareXRefDeleteSQLStatement(String xRefKeyColumnName, String xRefTableName)
	{
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("DELETE FROM `").append(xRefTableName).append("` WHERE `").append(xRefKeyColumnName).append("` = ? ");
		return updateSql.toString();
	}

	@Override
	public Map<String, Object> getExistingXrefRecord(String tableName, String columnName, String existingXrefValue, JdbcTemplate clientJdbcTemplate)
	{
		LOG.info("in getExistingXrefRecord()");
		Map<String, Object> map = new LinkedHashMap<>();
		List<Map<String, Object>> listOfXrefData = new ArrayList<>();
		try
		{
			String sql = sqlHelper.getSql("getExistingXrefRecord");
			sql = StringUtils.replace(sql, "{tableName}", tableName);
			sql = StringUtils.replace(sql, "{columnName}", columnName);
			clientJdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Object>>()
			{

				@Override
				public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					List<String> headers = new ArrayList<>();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnsCount = rsmd.getColumnCount();
					for (int i = 1; i <= columnsCount; i++)
					{
						headers.add(rsmd.getColumnName(i));
					}

					map.put("headers", headers);

					while (rs.next())
					{
						Map<String, Object> row = new HashMap<>();
						for (int i = 0; i < headers.size(); i++)
						{
							row.put(headers.get(i), rs.getString(headers.get(i)));
						}
						listOfXrefData.add(row);
					}
					map.put("listOfXrefData", listOfXrefData);
					return map;
				}

			}, existingXrefValue);

		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while getExistingXrefRecord()", ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while getExistingXrefRecord()", e);

		}
		return map;
	}

	@Override
	public Map<String, Object> mergeCrossReferenceRecordsWithExistingXref(String ilTableName, String existingXrefKey, List<String> mergeColumnValues, String xRefKeyColumnName, String autoincrementColumnName, JdbcTemplate clientJdbcTemplate)
	{
		LOG.info("in mergeCrossReferenceRecordsWithExistingXref");
		boolean isMerged = false;
		Map<String, Object> response = new HashMap<>();
		try
		{
			String sql = prepareXRefUpdateSQLStatement(xRefKeyColumnName, autoincrementColumnName, ilTableName, mergeColumnValues.size());
			mergeColumnValues.add(0, existingXrefKey);
			int count = clientJdbcTemplate.update(sql, mergeColumnValues.toArray());
			if( count == mergeColumnValues.size() - 1 )
			{
				isMerged = true;
			}
			else
			{
				mergeColumnValues.remove(0);
				mergeColumnValues.add(0, null);
				clientJdbcTemplate.update(sql, mergeColumnValues.toArray());
			}
			response.put("mergeStatus", isMerged);
			response.put("xref_key", existingXrefKey);
		}
		catch ( Exception e )
		{
			LOG.error("error while mergeCrossReferenceRecordsWithExistingXref()", e);
			throw new AnvizentRuntimeException(e);
		}
		return response;
	}

	@Override
	public int crossReferenceAuditLogs(String ilId, String columnName, String crossReferenceOption, String ilColumnValue, String newOrExistingXref, String xReferenceColumnValue, List<String> mergeColumnValues, String autoXrefQueries, String clientId, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate)
	{
		LOG.info("in crossReferenceAuditLogs");
		int count = 0;
		try
		{
			String crossReference = sqlHelper.getSql("crossReferenceAuditLogs");
			count = clientAppDbJdbcTemplate.update(crossReference, new Object[] { ilId, columnName, crossReferenceOption, ilColumnValue, newOrExistingXref, xReferenceColumnValue, mergeColumnValues, autoXrefQueries, clientId, modification.getCreatedBy(), modification.getCreatedTime() });

		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while crossReferenceAuditLogs()", ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while crossReferenceAuditLogs()", e);

		}
		return count;
	}

	@Override
	public List<CrossReferenceForm> getCrossReferenceInfoById(int ilid, String clientId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		LOG.info("in getCrossReferenceInfoById");
		List<CrossReferenceForm> crossReferenceList = null;
		try
		{
			String sql = sqlHelper.getSql("getCrossReferenceInfoById");
			crossReferenceList = clientAppDbJdbcTemplate.query(sql, new Object[] { ilid, clientId }, new RowMapper<CrossReferenceForm>()
			{

				@Override
				public CrossReferenceForm mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					List<String> colsList = new ArrayList<>();
					colsList.add(rs.getString("column_values"));
					List<String> mergeColsList = new ArrayList<>();
					mergeColsList.add(rs.getString("mergecolumns"));
					CrossReferenceForm crossReferenceForm = new CrossReferenceForm();
					crossReferenceForm.setId(rs.getInt("id"));
					crossReferenceForm.setIlColumnName(rs.getString("column_name"));
					crossReferenceForm.setCrossReferenceOption(rs.getString("xreference_type"));
					crossReferenceForm.setIlColumnValue(colsList);
					crossReferenceForm.setNewOrExistingXref(rs.getString("xref"));
					crossReferenceForm.setIlXreferenceColumn(rs.getString("xref_column_values"));
					crossReferenceForm.setIlMergeColumns(mergeColsList);
					return crossReferenceForm;
				}

			});
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while getCrossReferenceInfoById()", ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while getCrossReferenceInfoById()", e);

		}
		return crossReferenceList;
	}

	@Override
	public String getColumnValues(int id, String clientId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		LOG.info("in getColumnValues");
		String ColumnValues = null;
		try
		{
			String sql = sqlHelper.getSql("getColumnValues");

			ColumnValues = clientAppDbJdbcTemplate.query(sql, new Object[] { id, clientId }, new ResultSetExtractor<String>()
			{

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					if( rs != null && rs.next() )
					{
						return rs.getString("xref_column_values");
					}
					else
					{
						return null;
					}

				}

			});
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while getColumnValues()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while getColumnValues()", e);

		}
		return ColumnValues;
	}

	@Override
	public String getAutoMergeQueriesById(int id, String clientId, JdbcTemplate jdbcTemplate)
	{
		LOG.info("in getAutoMergeQueriesById");
		String columnValues = null;
		try
		{
			String sql = sqlHelper.getSql("getAutoMergeQueriesById");

			columnValues = jdbcTemplate.query(sql, new Object[] { id, clientId }, new ResultSetExtractor<String>()
			{

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					if( rs != null && rs.next() )
					{
						return rs.getString("auto_xref_queries");
					}
					else
					{
						return null;
					}

				}

			});
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while getColumnValues()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while getColumnValues()", e);

		}
		return columnValues;
	}

	@Override
	public int unMergeSelectedCrossReferenceRecord(String ilTableName, String autoincrementColumnName, String xRefKeyColumnName, Integer ilColumnValue, JdbcTemplate clientJdbcTemplate)
	{
		String sql = prepareUnMergerStatement(ilTableName, autoincrementColumnName, xRefKeyColumnName);
		int updatedCount = clientJdbcTemplate.update(sql, ilColumnValue);
		return updatedCount;
	}

	String prepareUnMergerStatement(String ilTableName, String autoincrementColumnName, String xRefKeyColumnName)
	{
		/*
		 * query for reference : UPDATE IL_TABLE SET XREF_COLUMN = ? WHERE
		 * IL_AUTO_COLUMN=? AND XREF_COLUMN IS NOT NULL
		 */
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ").append(ilTableName).append(" SET ").append(xRefKeyColumnName).append(" = null WHERE ").append(autoincrementColumnName).append(" = ? AND ").append(xRefKeyColumnName).append(" IS NOT NULL ");

		return query.toString();
	}

	@Override
	public Map<String, Object> getXrefTableRecordsWithMap(String xrefIlTableName, JdbcTemplate clientJdbcTemplate)
	{

		Map<String, Object> matchedRecordsListData = new HashMap<String, Object>();
		List<String> exceptionColumnsList = new ArrayList<>(this.excludeColumnsList);
		clientJdbcTemplate.execute(new ConnectionCallback<Object>()
		{
			@Override
			public Object doInConnection(Connection con) throws SQLException, DataAccessException
			{
				java.sql.DatabaseMetaData metaData = con.getMetaData();
				ResultSet rs = metaData.getColumns(null, null, xrefIlTableName, null);
				String autoIncrementColumnName = "";
				String xRefKeyColumnName = "";
				while (rs.next())
				{
					if( rs.getString("IS_AUTOINCREMENT").equals("YES") )
					{
						autoIncrementColumnName = rs.getString("COLUMN_NAME");
					}

					if( rs.getString("COLUMN_NAME").startsWith("XRef_") )
					{
						xRefKeyColumnName = rs.getString("COLUMN_NAME");
					}
				}
				exceptionColumnsList.add(autoIncrementColumnName);
				exceptionColumnsList.add(xRefKeyColumnName);
				matchedRecordsListData.put("autoincrement_column_name", autoIncrementColumnName);
				matchedRecordsListData.put("xRefKeyColumnName", xRefKeyColumnName);
				return null;
			}
		});
		String sqlQuery = "select * from " + xrefIlTableName;
		clientJdbcTemplate.query(sqlQuery, new ResultSetExtractor<Map<String, Object>>()
		{
			@Override
			public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException
			{

				exceptionColumnsList.add("");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				List<Map<String, Object>> valuesList = new ArrayList<>();
				List<String> headerDataforDisplay = new ArrayList<>();
				List<String> headerData = new ArrayList<>();
				for (int c = 1; c <= columnCount; c++)
				{
					if( exceptionColumnsList.indexOf(rsmd.getColumnLabel(c)) == -1 )
					{
						headerDataforDisplay.add(rsmd.getColumnLabel(c));
					}
					headerData.add(rsmd.getColumnLabel(c));

				}
				matchedRecordsListData.put("headersList", headerDataforDisplay);

				while (rs.next())
				{
					Map<String, Object> rowData = new HashMap<>();
					for (int c = 0; c < columnCount; c++)
					{
						rowData.put(headerData.get(c), rs.getString(headerData.get(c)));
					}
					valuesList.add(rowData);
				}
				matchedRecordsListData.put("valuesList", valuesList);
				return null;
			}
		});

		return matchedRecordsListData;
	}

	@Override
	public Map<String, Object> getUnMergeRowWithColumns(CrossReferenceForm crossReferenceForm, JdbcTemplate clientJdbcTemplate)
	{

		Map<String, Object> matchedRecordsListData = new HashMap<String, Object>();
		List<String> exceptionColumnsList = new ArrayList<>(this.excludeColumnsList);
		matchedRecordsListData.put("autoincrement_column_name", crossReferenceForm.getAutoincrementColumnName());
		matchedRecordsListData.put("xRefKeyColumnName", crossReferenceForm.getxRefKeyColumnName());

		String sqlQuery = "select * from " + crossReferenceForm.getIlName() + " where " + crossReferenceForm.getAutoincrementColumnName() + " = ? ";
		clientJdbcTemplate.query(sqlQuery, new Object[] { crossReferenceForm.getId() }, new ResultSetExtractor<Map<String, Object>>()
		{
			@Override
			public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException
			{

				exceptionColumnsList.add("");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				List<Map<String, Object>> valuesList = new ArrayList<>();
				List<String> headerDataforDisplay = new ArrayList<>();
				List<String> headerData = new ArrayList<>();
				for (int c = 1; c <= columnCount; c++)
				{
					if( exceptionColumnsList.indexOf(rsmd.getColumnLabel(c)) == -1 )
					{
						headerDataforDisplay.add(rsmd.getColumnLabel(c));
					}
					headerData.add(rsmd.getColumnLabel(c));

				}
				matchedRecordsListData.put("headersList", headerDataforDisplay);

				while (rs.next())
				{
					Map<String, Object> rowData = new HashMap<>();
					for (int c = 0; c < columnCount; c++)
					{
						rowData.put(headerData.get(c), rs.getString(headerData.get(c)));
					}
					valuesList.add(rowData);
				}
				matchedRecordsListData.put("valuesList", valuesList);
				return null;
			}
		});

		return matchedRecordsListData;
	}

	@Override
	public Map<String, Object> processAutoMergeCrossReference(CrossReferenceForm crossReferenceForm, ILInfo ilInfo, User user, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppJdbcTemplate) throws Exception
	{
		Map<String, Object> crossRefData = new LinkedHashMap<>();
		List<String> excludeColumnsList = new ArrayList<>(this.excludeColumnsList);
		excludeColumnsList.add(crossReferenceForm.getAutoincrementColumnName());
		excludeColumnsList.add(crossReferenceForm.getxRefKeyColumnName());
		JSONObject jsonObject = new JSONObject();
		String startDate = TimeZoneDateHelper.getFormattedDateString();
		try
		{
			String columnsList = getInsertColumnsList(null, null, ilInfo.getColumns(), excludeColumnsList);
			String groupByQuery = prepareAutoMergeGroupByCondition(crossReferenceForm.getIlMergeColumns(), ilInfo.getiL_table_name(), crossReferenceForm.getxRefKeyColumnName());
			String xRefInsertQuery = prepareAutoMergeXrefInertStatementWithConditions(columnsList, ilInfo.getiL_table_name(), ilInfo.getXref_il_table_name(), crossReferenceForm.getxRefKeyColumnName());

			jsonObject.put("groupByQuery", groupByQuery);
			jsonObject.put("xRefInsertQuery", xRefInsertQuery);

			List<Map<String, Object>> listObjectObject = clientJdbcTemplate.query(groupByQuery, new ResultSetExtractor<List<Map<String, Object>>>()
			{
				@Override
				public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					List<Map<String, Object>> listObjectObject = new ArrayList<>();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					while (rs.next())
					{
						Map<String, Object> columnData = new HashMap<>();
						for (int c = 1; c <= columnCount; c++)
						{
							columnData.put(rsmd.getColumnName(c), rs.getObject(c));
						}
						listObjectObject.add(columnData);
					}
					return listObjectObject;
				}
			});
			jsonObject.put("noOfXRefRecords", listObjectObject.size());

			LOG.info("List", listObjectObject);

			for (Map<String, Object> columnData : listObjectObject)
			{
				StringBuilder whereClause = new StringBuilder();
				Object[] dataObject = prepareAutoMergeWhereClause(whereClause, crossReferenceForm.getIlMergeColumns(), columnData);
				StringBuilder xRefRecordInsertQuery = new StringBuilder(xRefInsertQuery.toString() + whereClause.toString());
				xRefRecordInsertQuery.append(" limit 1");
				KeyHolder xRefKeyHolder = new GeneratedKeyHolder();
				clientJdbcTemplate.update(new PreparedStatementCreator()
				{
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException
					{
						PreparedStatement preparedStatement = con.prepareStatement(xRefRecordInsertQuery.toString(), new String[] { "id" });
						int p = 0;
						for (Object obj : dataObject)
						{
							preparedStatement.setObject(++p, obj);
						}
						return preparedStatement;
					}
				}, xRefKeyHolder);
				long crossReferenceId = 0L;
				if( xRefKeyHolder != null )
				{
					Number autoIncrement = xRefKeyHolder.getKey();
					crossReferenceId = autoIncrement.longValue();
					String ilXrefUpdateQuery = prepareAutoMergeXRefUpdateQuery(ilInfo.getiL_table_name(), crossReferenceForm.getxRefKeyColumnName()) + whereClause.toString();
					List<Object> listObj = new ArrayList<>(Arrays.asList(dataObject));
					listObj.add(0, crossReferenceId);
					int ilUpdationCount = clientJdbcTemplate.update(ilXrefUpdateQuery, listObj.toArray());
					LOG.info("il updation count ", ilUpdationCount);
					crossRefData.put("" + crossReferenceId, ilUpdationCount);
				}
			}

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			String endDate = TimeZoneDateHelper.getFormattedDateString();
			automergeLogUpdate(new CrossReferenceLogs(crossReferenceForm.getId(), crossReferenceForm.getIlId(), CrossReferenceConstants.MERGER.toString(), CrossReferenceConstants.AUTOMERGE.toString(), crossReferenceForm.getConditionName(), String.join(",", crossReferenceForm.getIlMergeColumns()),
					crossReferenceForm.getApplicableDate(), jsonObject.toString(), startDate, endDate, modification, new String(), ""), clientAppJdbcTemplate);
		}
		catch ( Exception e )
		{
			LOG.error("", e);
			throw e;
		}
		return crossRefData;
	}

	@Override
	public int bulkmergeLogUpdate(long conditionId, CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate)
	{

		LOG.info("in bulkmergeLogUpdate()");
		int updatedCount = 0;
		try
		{
			String sql = sqlHelper.getSql("bulkmergeLogUpdate");
			updatedCount = clientAppJdbcTemplate.update(sql, new Object[] {  crossReferenceLogs.getIlId(), crossReferenceLogs.getCrossReferenceOption(),
					   crossReferenceLogs.getTypeOfMerge(), crossReferenceLogs.getConditionName(), crossReferenceLogs.getBulkMergeReferenceFields(),
					   crossReferenceLogs.getBulkMergeXreferenceFields(), crossReferenceLogs.getSourceFileInfoId(), crossReferenceLogs.getApplicableDate(),
					   crossReferenceLogs.getStartDate(), crossReferenceLogs.getEndDate(), crossReferenceLogs.getModification().getCreatedBy(),
			           crossReferenceLogs.getModification().getCreatedTime(), conditionId});
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while bulkmergeLogUpdate", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while bulkmergeLogUpdate", e);

		}

		return updatedCount;
	}
	

	@Override
	public int automergeLogUpdate(CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate)
	{

		LOG.info("in automergeLogUpdate()");
		int updatedCount = 0;
		try
		{
			String sql = sqlHelper.getSql("automergeLogUpdate");
			updatedCount = clientAppJdbcTemplate.update(sql, new Object[] { crossReferenceLogs.getIlId(), crossReferenceLogs.getCrossReferenceOption(), crossReferenceLogs.getTypeOfMerge(), crossReferenceLogs.getConditionName(), crossReferenceLogs.getAutoMergeColumns(),
					crossReferenceLogs.getApplicableDate(), crossReferenceLogs.getStats(), crossReferenceLogs.getStartDate(), crossReferenceLogs.getEndDate(), crossReferenceLogs.getModification().getCreatedBy(), crossReferenceLogs.getModification().getCreatedTime() });
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while bulkmergeLogUpdate", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while bulkmergeLogUpdate", e);
		}
		return updatedCount;
	}

	@Override
	public int manualmergeLogUpdate(CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate)
	{
		LOG.info("in manualmergeLogUpdate()");
		int updatedCount = 0;
		try
		{
			String sql = sqlHelper.getSql("manualmergeLogUpdate");
			updatedCount = clientAppJdbcTemplate.update(sql,
					new Object[] { crossReferenceLogs.getIlId(), crossReferenceLogs.getCrossReferenceOption(), crossReferenceLogs.getTypeOfMerge(), crossReferenceLogs.getConditionName(), crossReferenceLogs.getManualMergeColumnName(), crossReferenceLogs.getManualMergeColumnValues(),
							crossReferenceLogs.getTypeOfXref(), crossReferenceLogs.getExistingSelectedXrefValue(), crossReferenceLogs.getSelectedXrefKeyValue(),crossReferenceLogs.getXrefKey(), crossReferenceLogs.getApplicableDate(), crossReferenceLogs.getStats(), crossReferenceLogs.getStartDate(), crossReferenceLogs.getEndDate(),
							crossReferenceLogs.getModification().getCreatedBy(), crossReferenceLogs.getModification().getCreatedTime(), crossReferenceLogs.getId() });
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while manualmergeLogUpdate", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while manualmergeLogUpdate", e);
		}
		return updatedCount;
	}
	
	@Override
	public int manualmergeLogInsert(CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate)
	{
		LOG.info("in manualmergeLogInsert()");
		int updatedCount = 0;
		try
		{
			String sql = sqlHelper.getSql("manualmergeLogInsert");
			updatedCount = clientAppJdbcTemplate.update(sql,
					new Object[] { crossReferenceLogs.getIlId(), crossReferenceLogs.getCrossReferenceOption(), crossReferenceLogs.getTypeOfMerge(), crossReferenceLogs.getConditionName(), crossReferenceLogs.getManualMergeColumnName(), crossReferenceLogs.getManualMergeColumnValues(),
							crossReferenceLogs.getTypeOfXref(), crossReferenceLogs.getExistingSelectedXrefValue(), crossReferenceLogs.getSelectedXrefKeyValue(),crossReferenceLogs.getXrefKey(), crossReferenceLogs.getApplicableDate(), crossReferenceLogs.getStats(), crossReferenceLogs.getStartDate(), crossReferenceLogs.getEndDate(),
							crossReferenceLogs.getModification().getCreatedBy(), crossReferenceLogs.getModification().getCreatedTime() });
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while manualmergeLogInsert", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while manualmergeLogInsert", e);
		}
		return updatedCount;
	}

	@Override
	public int splitLogUpdate(CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate)
	{
		/*ilid = ?, xreference_type = ?, stats = ?, start_date = ?, 
				end_date = ?, created_by = ?, created_time = ? type_of_merge = ?, condition_name = ?, manual_merge_column_name = ?, manual_merge_column_values = ?, 
				type_of_xref = ?, existing_selected_xref_value = ?, selected_xref_key_value = ?, bulkmerge_reference_fields = ?, bulkmerge_xreference_fields = ?, 
				source_file_info_id = ?, auto_merge_columns = ?, applicable_date = ?, auto_merge_column_obj = ?, XRef_Key = ?, stats  = ?*/
		LOG.info("in splitLogUpdate()");
		int updatedCount = 0;
		try
		{
			String sql = sqlHelper.getSql("splitLogUpdate");
			updatedCount = clientAppJdbcTemplate.update(sql, new Object[] { crossReferenceLogs.getIlId(), crossReferenceLogs.getCrossReferenceOption(), crossReferenceLogs.getStats(), crossReferenceLogs.getStartDate(), crossReferenceLogs.getEndDate(),
					crossReferenceLogs.getModification().getCreatedBy(), crossReferenceLogs.getModification().getCreatedTime(), 
					crossReferenceLogs.getTypeOfMerge(), crossReferenceLogs.getConditionName(), crossReferenceLogs.getManualMergeColumnName(), crossReferenceLogs.getManualMergeColumnValues(),
					crossReferenceLogs.getTypeOfXref(), crossReferenceLogs.getExistingSelectedXrefValue(), crossReferenceLogs.getSelectedXrefKeyValue(), crossReferenceLogs.getBulkMergeReferenceFields(),
					crossReferenceLogs.getBulkMergeXreferenceFields(), crossReferenceLogs.getSourceFileInfoId(), crossReferenceLogs.getAutoMergeColumns(), crossReferenceLogs.getApplicableDate(), 
					crossReferenceLogs.getConditionObject(), crossReferenceLogs.getXrefKey(), crossReferenceLogs.getStats(),
					crossReferenceLogs.getId() });
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while splitLogUpdate", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while splitLogUpdate", e);
		}
		return updatedCount;
	}
	
	@Override
	public int splitLogInsert(CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate)
	{
		LOG.info("in splitLogInsert()");
		int updatedCount = 0;
		try
		{
			String sql = sqlHelper.getSql("splitLogInsert");
			updatedCount = clientAppJdbcTemplate.update(sql, new Object[] { crossReferenceLogs.getIlId(), crossReferenceLogs.getCrossReferenceOption(), crossReferenceLogs.getStats(), crossReferenceLogs.getStartDate(), crossReferenceLogs.getEndDate(),
					crossReferenceLogs.getModification().getCreatedBy(), crossReferenceLogs.getModification().getCreatedTime() });
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while splitLogInsert", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while splitLogInsert", e);
		}
		return updatedCount;
	}

	String prepareAutoMergeGroupByCondition(List<String> columnNames, String tableName, String xRefKeyColumnName)
	{
		char columnQuotation = '`';
		String groupedColumnNames = columnQuotation + String.join("`,`", columnNames) + columnQuotation;
		StringBuilder groupByQuery = new StringBuilder();
		groupByQuery.append("select ").append(groupedColumnNames).append(", count(*) from ").append(tableName).append(" where ").append(xRefKeyColumnName).append(" is null group by ").append(groupedColumnNames);
		return groupByQuery.toString();
	}

	String prepareAutoMergeXrefInertStatementWithConditions(String tablecolumnsList, String ilTableName, String xRefTableName, String xRefColumnName)
	{

		StringBuilder xRefInsertQuery = new StringBuilder();
		xRefInsertQuery.append("insert into ").append(xRefTableName).append("(").append(tablecolumnsList).append(") \n").append(" select ").append(tablecolumnsList).append(" from ").append(ilTableName).append(" where ").append(xRefColumnName).append(" is null and ");
		return xRefInsertQuery.toString();
	}

	public String prepareAutoMergeXRefUpdateQuery(String ilTableName, String xRefKeyColumnName)
	{
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("UPDATE `").append(ilTableName).append("` SET `").append(xRefKeyColumnName).append("` = ? where ");
		return updateSql.toString();
	}

	public Object[] prepareAutoMergeWhereClause(StringBuilder whereClause, List<String> columnNames, Map<String, Object> columnValues)
	{
		List<Object> columnObjValues = new ArrayList<>();
		for (String columnName : columnNames)
		{
			whereClause.append(" `").append(columnName).append("`");
			Object columnValue = columnValues.get(columnName);
			if( columnValue != null )
			{
				whereClause.append(" = ? ");
				columnObjValues.add(columnValue);
			}
			else
			{
				whereClause.append(" is null ");
			}
			whereClause.append(" and ");
		}
		whereClause.delete(whereClause.length() - 4, whereClause.length());
		return columnObjValues.toArray();
	}

	@Override
	public List<CrossReferenceLogs> getCrossReferenceLogsInfo(int ilId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<CrossReferenceLogs> crossReferenceLogsList = null;
		try
		{
			String sql = sqlHelper.getSql("getCrossReferenceLogsInfo");
			crossReferenceLogsList = clientAppDbJdbcTemplate.query(sql, new Object[] { ilId }, new RowMapper<CrossReferenceLogs>()
			{
				@Override
				public CrossReferenceLogs mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					CrossReferenceLogs crossLogsInfo = new CrossReferenceLogs();
					crossLogsInfo.setId(rs.getInt("id"));
					crossLogsInfo.setIlId(rs.getInt("ilid"));
					crossLogsInfo.setCrossReferenceOption(rs.getString("xreference_type"));
					crossLogsInfo.setTypeOfMerge(rs.getString("type_of_merge"));
					crossLogsInfo.setConditionName(rs.getString("condition_name"));
					crossLogsInfo.setManualMergeColumnName(rs.getString("manual_merge_column_name"));
					crossLogsInfo.setManualMergeColumnValues(rs.getString("manual_merge_column_values"));
					crossLogsInfo.setTypeOfXref(rs.getString("type_of_xref"));
					crossLogsInfo.setExistingSelectedXrefValue(rs.getString("existing_selected_xref_value"));
					crossLogsInfo.setSelectedXrefKeyValue(rs.getString("selected_xref_key_value"));
					crossLogsInfo.setBulkMergeReferenceFields(rs.getString("bulkmerge_reference_fields"));
					crossLogsInfo.setBulkMergeXreferenceFields(rs.getString("bulkmerge_xreference_fields"));
					crossLogsInfo.setSourceFileInfoId(rs.getInt("source_file_info_id"));
					crossLogsInfo.setAutoMergeColumns(rs.getString("auto_merge_columns"));
					crossLogsInfo.setApplicableDate(rs.getString("applicable_date"));
					crossLogsInfo.setStartDate(rs.getString("start_date"));
					crossLogsInfo.setEndDate(rs.getString("end_date"));
					return crossLogsInfo;
				}
			});

		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while getCrossReferenceLogsInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while getCrossReferenceLogsInfo()", e);

		}
		return crossReferenceLogsList;
	}

	@Override
	public CrossReferenceLogs getCrossReferenceLogsInfoById(int id, JdbcTemplate clientAppDbJdbcTemplate)
	{
		CrossReferenceLogs crossReferenceLogs = null;
		try
		{
			String sql = sqlHelper.getSql("getCrossReferenceLogsInfoById");
			crossReferenceLogs = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<CrossReferenceLogs>()
			{
				@Override
				public CrossReferenceLogs extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					CrossReferenceLogs crossLogsInfo = new CrossReferenceLogs();
					if( rs != null && rs.next() )
					{
						crossLogsInfo.setId(rs.getInt("id"));
						crossLogsInfo.setIlId(rs.getInt("ilid"));
						crossLogsInfo.setCrossReferenceOption(rs.getString("xreference_type"));
						crossLogsInfo.setTypeOfMerge(rs.getString("type_of_merge"));
						crossLogsInfo.setConditionName(rs.getString("condition_name"));
						crossLogsInfo.setConditionObject(rs.getString("auto_merge_column_obj"));
						crossLogsInfo.setManualMergeColumnName(rs.getString("manual_merge_column_name"));
						crossLogsInfo.setManualMergeColumnValues(rs.getString("manual_merge_column_values"));
						crossLogsInfo.setTypeOfXref(rs.getString("type_of_xref"));
						crossLogsInfo.setExistingSelectedXrefValue(rs.getString("existing_selected_xref_value"));
						crossLogsInfo.setSelectedXrefKeyValue(rs.getString("selected_xref_key_value"));
						crossLogsInfo.setBulkMergeReferenceFields(rs.getString("bulkmerge_reference_fields"));
						crossLogsInfo.setBulkMergeXreferenceFields(rs.getString("bulkmerge_xreference_fields"));
						crossLogsInfo.setSourceFileInfoId(rs.getInt("source_file_info_id"));
						crossLogsInfo.setAutoMergeColumns(rs.getString("auto_merge_columns"));
						crossLogsInfo.setApplicableDate(rs.getString("applicable_date"));
						crossLogsInfo.setStats(rs.getString("stats"));
						crossLogsInfo.setStartDate(rs.getString("start_date"));
						crossLogsInfo.setEndDate(rs.getString("end_date"));
						return crossLogsInfo;
					}
					else
					{
						return null;
					}

				}
			});
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while getCrossReferenceLogsInfoById()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while getCrossReferenceLogsInfoById()", e);

		}
		return crossReferenceLogs;
	}

	@Override
	public List<CrossReferenceLogs> getAllCrossReferences(String clientIdfromHeader, JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<CrossReferenceLogs> crossReferenceLogsList = null;
		try
		{
			String sql = sqlHelper.getSql("getAllCrossReferences");
			crossReferenceLogsList = clientAppDbJdbcTemplate.query(sql, new RowMapper<CrossReferenceLogs>()
			{
				@Override
				public CrossReferenceLogs mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					CrossReferenceLogs crossLogsInfo = new CrossReferenceLogs();
					crossLogsInfo.setId(rs.getInt("id"));
					crossLogsInfo.setIlId(rs.getInt("ilid"));
					crossLogsInfo.setDimension(rs.getString("IL_name"));
					crossLogsInfo.setCrossReferenceOption(rs.getString("xreference_type"));
					crossLogsInfo.setTypeOfMerge(rs.getString("type_of_merge"));
					crossLogsInfo.setConditionName(rs.getString("condition_name"));
					crossLogsInfo.setManualMergeColumnName(rs.getString("manual_merge_column_name"));
					crossLogsInfo.setManualMergeColumnValues(rs.getString("manual_merge_column_values"));
					crossLogsInfo.setTypeOfXref(rs.getString("type_of_xref"));
					crossLogsInfo.setExistingSelectedXrefValue(rs.getString("existing_selected_xref_value"));
					crossLogsInfo.setSelectedXrefKeyValue(rs.getString("selected_xref_key_value"));
					crossLogsInfo.setBulkMergeReferenceFields(rs.getString("bulkmerge_reference_fields"));
					crossLogsInfo.setBulkMergeXreferenceFields(rs.getString("bulkmerge_xreference_fields"));
					crossLogsInfo.setSourceFileInfoId(rs.getInt("source_file_info_id"));
					crossLogsInfo.setAutoMergeColumns(rs.getString("auto_merge_columns"));
					crossLogsInfo.setApplicableDate(rs.getString("applicable_date"));
					crossLogsInfo.setStartDate(rs.getString("start_date"));
					crossLogsInfo.setEndDate(rs.getString("end_date"));
					crossLogsInfo.setActive(rs.getBoolean("isActive"));
					return crossLogsInfo;
				}
			});
		}
		catch ( DataAccessException ae ){
			LOG.error("error while getCrossReferenceLogsInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e ){
			LOG.error("error while getCrossReferenceLogsInfo()", e);
		}
		return crossReferenceLogsList;
	}

	@Override
	public int saveAutoMergeCrossReference(CrossReferenceForm crossReferenceForm, ILInfo ilInfo, User user, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate)
	{
		String startDate = TimeZoneDateHelper.getFormattedDateString();
		String endDate = TimeZoneDateHelper.getFormattedDateString();
		Modification modification = new Modification(new Date());
		modification.setCreatedBy(user.getUserName());
		CrossReferenceLogs crossReferenceLogs = new CrossReferenceLogs(crossReferenceForm.getId(), crossReferenceForm.getIlId(), CrossReferenceConstants.MERGER.toString(), CrossReferenceConstants.AUTOMERGE.toString(), crossReferenceForm.getConditionName(), String.join(",", crossReferenceForm.getIlMergeColumns()),
				crossReferenceForm.getApplicableDate(), null, startDate, endDate, modification, crossReferenceForm.getConditionObject(), "");
		LOG.info("in saveAutoMergeCrossReference()");
		int updatedCount = 0;
		try
		{
			String sql = sqlHelper.getSql("savemergeLogUpdate");
			updatedCount = clientAppDbJdbcTemplate.update(sql, new Object[] { crossReferenceLogs.getIlId(), crossReferenceLogs.getCrossReferenceOption(), crossReferenceLogs.getTypeOfMerge(), crossReferenceLogs.getConditionName(), crossReferenceLogs.getAutoMergeColumns(),
					crossReferenceLogs.getApplicableDate(), crossReferenceLogs.getStats(), crossReferenceLogs.getStartDate(), 
					crossReferenceLogs.getEndDate(), crossReferenceLogs.getModification().getCreatedBy(), 
					crossReferenceLogs.getModification().getCreatedTime(), crossReferenceLogs.getConditionObject() });

		}
		catch ( DataAccessException ae ){
			LOG.error("error while saveAutoMergeCrossReference", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e ){
			LOG.error("error while saveAutoMergeCrossReference", e);
		}

		return updatedCount;
	}
	
	@Override
	public int updateAutoMergeCrossReference(CrossReferenceForm crossReferenceForm, ILInfo ilInfo, User user, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate) {

		String startDate = TimeZoneDateHelper.getFormattedDateString();
		String endDate = TimeZoneDateHelper.getFormattedDateString();
		Modification modification = new Modification(new Date());
		modification.setCreatedBy(user.getUserName());
		CrossReferenceLogs crossReferenceLogs = new CrossReferenceLogs(crossReferenceForm.getId(), crossReferenceForm.getIlId(), CrossReferenceConstants.MERGER.toString(), CrossReferenceConstants.AUTOMERGE.toString(), crossReferenceForm.getConditionName(), String.join(",", crossReferenceForm.getIlMergeColumns()),
				crossReferenceForm.getApplicableDate(), null, startDate, endDate, modification, crossReferenceForm.getConditionObject(),"");
		LOG.info("in updateAutoMergeCrossReference()");
		int updatedCount = 0;
		try
		{
			String sql = sqlHelper.getSql("updatemergeLogUpdate");
			updatedCount = clientAppDbJdbcTemplate.update(sql, new Object[] { crossReferenceLogs.getIlId(), crossReferenceLogs.getCrossReferenceOption(), crossReferenceLogs.getTypeOfMerge(), crossReferenceLogs.getConditionName(), crossReferenceLogs.getAutoMergeColumns(),
					crossReferenceLogs.getApplicableDate(), crossReferenceLogs.getStats(), crossReferenceLogs.getStartDate(), 
					crossReferenceLogs.getEndDate(), crossReferenceLogs.getModification().getCreatedBy(), 
					crossReferenceLogs.getModification().getCreatedTime(), crossReferenceLogs.getConditionObject() , crossReferenceLogs.getId()});

		}
		catch ( DataAccessException ae ){
			LOG.error("error while updateAutoMergeCrossReference", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e ){
			LOG.error("error while updateAutoMergeCrossReference", e);
		}

		return updatedCount;
	
	}

	@Override
	public List<CrossReferenceLogs> getCrossReferencesByIlId(int ilId, String clientId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<CrossReferenceLogs> crossReferenceLogsList = null;
		try
		{
			String sql = sqlHelper.getSql("getCrossReferencesByIlId");
			crossReferenceLogsList = clientAppDbJdbcTemplate.query(sql, new Object[] {ilId, "automerge"}, new RowMapper<CrossReferenceLogs>()
			{
				@Override
				public CrossReferenceLogs mapRow(ResultSet rs, int rowNum) throws SQLException
				{
					CrossReferenceLogs crossLogsInfo = new CrossReferenceLogs();
					crossLogsInfo.setId(rs.getInt("id"));
					crossLogsInfo.setIlId(rs.getInt("ilid"));
					crossLogsInfo.setDimension(rs.getString("IL_name"));
					crossLogsInfo.setCrossReferenceOption(rs.getString("xreference_type"));
					crossLogsInfo.setTypeOfMerge(rs.getString("type_of_merge"));
					crossLogsInfo.setConditionName(rs.getString("condition_name"));
					crossLogsInfo.setConditionObject(rs.getString("auto_merge_column_obj"));
					crossLogsInfo.setManualMergeColumnName(rs.getString("manual_merge_column_name"));
					crossLogsInfo.setManualMergeColumnValues(rs.getString("manual_merge_column_values"));
					crossLogsInfo.setTypeOfXref(rs.getString("type_of_xref"));
					crossLogsInfo.setExistingSelectedXrefValue(rs.getString("existing_selected_xref_value"));
					crossLogsInfo.setSelectedXrefKeyValue(rs.getString("selected_xref_key_value"));
					crossLogsInfo.setBulkMergeReferenceFields(rs.getString("bulkmerge_reference_fields"));
					crossLogsInfo.setBulkMergeXreferenceFields(rs.getString("bulkmerge_xreference_fields"));
					crossLogsInfo.setSourceFileInfoId(rs.getInt("source_file_info_id"));
					crossLogsInfo.setAutoMergeColumns(rs.getString("auto_merge_columns"));
					crossLogsInfo.setApplicableDate(rs.getString("applicable_date"));
					crossLogsInfo.setStartDate(rs.getString("start_date"));
					crossLogsInfo.setEndDate(rs.getString("end_date"));
					return crossLogsInfo;
				}
			});
		}
		catch ( DataAccessException ae ){
			LOG.error("error while getCrossReferenceLogsInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e ){
			LOG.error("error while getCrossReferenceLogsInfo()", e);
		}
		return crossReferenceLogsList;
	}

	@Override
	public void processAutoMergeCrossReference(ILInfo ilInfo, User user, Map<String, Object> clientDbCredentials, CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate)
	{
		try {
			String dbHost = clientDbCredentials.get("region_hostname").toString();
			String dbPort = clientDbCredentials.get("region_port").toString();
			String dbUserName = clientDbCredentials.get("clientdb_username").toString();
			String dbPassword = clientDbCredentials.get("clientdb_password").toString();
			String mainSchemaName = clientDbCredentials.get("clientdb_schema").toString();
			String stagingSchemaName = clientDbCredentials.get("clientdb_staging_schema").toString();
			
			LOG.info("Cross reference job class name " + AUTO_MERGE_JOB_CLASS );
			LOG.info("Cross reference dependency jars " + AUTO_MERGE_DEPENDENCY_JARS );
			Map<String, String> xRefContextParams = MinidwServiceUtil.getAutoMergeXRefContextParams(dbHost, dbPort, stagingSchemaName, dbUserName, dbPassword, dbHost, 
					                               dbPort, mainSchemaName, dbUserName, dbPassword, ilInfo, user.getClientId(), Constants.Temp.getTempFileDir(),
					                               crossReferenceLogs.getConditionObject(),crossReferenceLogs.getAutoMergeColumns(),null, crossReferenceLogs.getApplicableDate(),
					                               crossReferenceLogs.getId(), crossReferenceLogs.getXrefExecutionType(), crossReferenceLogs.getConditionName());
			JobExecutionInfo xrefJobStatus =  MinidwServiceUtil.runXrefJob(xRefContextParams, ilInfo,AUTO_MERGE_JOB_CLASS,AUTO_MERGE_DEPENDENCY_JARS);
			if ( xrefJobStatus.getStatusCode() == 1) {
				throw new RuntimeException(xrefJobStatus.getExecutionMessages());
			}
		}catch(Exception e) {
			throw new AnvizentRuntimeException(e);
		}
	
	}

	@Override
	public void archiveCrossReferenceById(String clientId, Integer conditionId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		try {
			String sql = sqlHelper.getSql("deleteCrossReferenceById");
			clientAppDbJdbcTemplate.update(sql, conditionId);
		} catch (DataAccessException ae) {
			LOG.error("error while deleteLoadParametersById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while deleteLoadParametersById()", e);
		}
	}

	@Override
	public long bulkmergeLogInsert(CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate)
	{

		LOG.info("in bulkmergeLogInsert()");
		long generatedConditionId = 0;
		try
		{
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String sql = sqlHelper.getSql("bulkmergeLogInsert");
			clientAppJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setLong(1, crossReferenceLogs.getIlId());
					ps.setString(2, crossReferenceLogs.getCrossReferenceOption());
					ps.setString(3, crossReferenceLogs.getTypeOfMerge());
					ps.setString(4, crossReferenceLogs.getConditionName());
					ps.setString(5,crossReferenceLogs.getBulkMergeReferenceFields());
					ps.setString(6, crossReferenceLogs.getBulkMergeXreferenceFields());
					ps.setInt(7, crossReferenceLogs.getSourceFileInfoId());
					ps.setString(8, crossReferenceLogs.getApplicableDate());
					ps.setString(9,  crossReferenceLogs.getStartDate());
					ps.setString(10, crossReferenceLogs.getEndDate());
					ps.setString(11, crossReferenceLogs.getModification().getCreatedBy());
					ps.setString(12, crossReferenceLogs.getModification().getCreatedTime() );

					return ps;
				}
			}, keyHolder);
		
			if(keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				generatedConditionId = autoIncrement.longValue();
			}
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while bulkmergeLogInsert", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while bulkmergeLogInsert", e);
		}
		return generatedConditionId;
	}

	@Override
	public void activateCrossReferenceById(String clientId, Integer conditionId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		try {
			String sql = sqlHelper.getSql("activateCrossReferenceById");
			clientAppDbJdbcTemplate.update(sql, conditionId);
		} catch (DataAccessException ae) {
			LOG.error("error while deleteLoadParametersById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while deleteLoadParametersById()", e);
		}
	}

}