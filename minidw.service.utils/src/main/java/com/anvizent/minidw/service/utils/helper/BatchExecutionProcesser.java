package com.anvizent.minidw.service.utils.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class BatchExecutionProcesser extends JdbcDaoSupport {
	
	private String batchId = UUID.randomUUID().toString();
	private int totalCount = 0;
	private int insertedCount = 0;
	private int failedCount = 0;
	private int iterationCount = 0;
	private String sql;
	private int[] argTypes;
	private List<Integer> errorIndexs = new ArrayList<>();
	
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getInsertedCount() {
		return insertedCount;
	}

	public void setInsertedCount(int insertedCount) {
		this.insertedCount = insertedCount;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}

	public int getIterationCount() {
		return iterationCount;
	}

	public void setIterationCount(int iterationCount) {
		this.iterationCount = iterationCount;
	}

	public int[] getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(int[] argTypes) {
		this.argTypes = argTypes;
	}

	public List<Integer> getErrorIndexs() {
		return errorIndexs;
	}

	public void setErrorIndexs(List<Integer> errorIndexs) {
		this.errorIndexs = errorIndexs;
	}

	public BatchExecutionProcesser(DataSource dataSource) {
		this(null, dataSource);
	}

	public BatchExecutionProcesser(String sql,DataSource dataSource) {
		this(sql, null, dataSource);
	}

	public BatchExecutionProcesser(String sql, int[] argTypes,DataSource dataSource) {
		this.sql = sql;
		this.argTypes = argTypes;
		setDataSource(dataSource);
	}
	
	
	public void executeBatchUpdates(String[] sqls) {
		//executeBatchUpdatesWithBatch(sqls);
		executeBatchUpdatesWithoutBatch(sqls);
	}
	
	public void executeBatchUpdatesWithBatch(String[] sqls) {
		int[] result = getJdbcTemplate().batchUpdate(sqls);
		processStats(result);
	}
	
	public void executeBatchUpdatesWithoutBatch(String[] sqls) {

		int updatedCount = getJdbcTemplate().execute(new StatementCallback<Integer>() {
			@Override
			public Integer doInStatement(Statement stmt) throws SQLException, DataAccessException {
				int successCount = 0;
				for (String sql : sqls) {
					int updatedCount = 0;

					try {
						updatedCount = stmt.executeUpdate(sql);
					} catch (Exception e) {
						logger.error(e);
					}
					if (updatedCount == 1) {
						successCount++;
					}
				}
				return successCount;
			}
		});
		totalCount += sqls.length;
		insertedCount += updatedCount;
		failedCount += sqls.length - updatedCount;
		
	}

	public void executeSpringBatch(List<Object[]> batchArgs) {
		executeSpringBatch(batchArgs, argTypes);
	}
	
	public void executeSpringBatch(List<Object[]> batchArgs, int[] argTypes) {
		int[] result = getJdbcTemplate().batchUpdate(sql, batchArgs, argTypes);
		processStats(result);
		iterationCount++;
		totalCount += result.length;
	}
	
	
	public void executePlainBatch(List<Object[]> batchArgs, int[] columnTypes) {
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = getConnection();
			pst = con.prepareStatement(sql);
			
			for (Object[] data : batchArgs) {
				int paramIndex = 0;
				for (Object inValue : data) {
					paramIndex++;
					int colType;
					if (columnTypes == null || columnTypes.length < paramIndex) {
						colType = SqlTypeValue.TYPE_UNKNOWN;
					}
					else {
						colType = columnTypes[paramIndex - 1];
					}
					switch(colType) {
					case Types.VARCHAR:
					case Types.DECIMAL:
						pst.setString(paramIndex, (String)inValue);
						break;
					case Types.BIGINT:
					case Types.INTEGER:
						pst.setLong(paramIndex, (long)inValue);
						break;
					case Types.BOOLEAN:
						pst.setBoolean(paramIndex, (Boolean) inValue);
						break;
					default:
						pst.setObject(paramIndex, inValue);
					}
				}
				pst.addBatch();
			}
			int[] result = pst.executeBatch();
			
			processStats(result);
			iterationCount++;
			totalCount += result.length;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

	void processStats(int[] result) {
		int ctr = 0;
		for (int updatedCount : result) {
			ctr++;
			if (updatedCount == 1) {
				insertedCount++;
			} else {
				failedCount++;
				errorIndexs.add(totalCount + ctr);
			}
		}
	}

}
