package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.HierarchicalDao;
import com.datamodel.anvizent.service.dao.util.HierarchicalExtracter;
import com.datamodel.anvizent.service.dao.util.HierarchicalMapper;
import com.datamodel.anvizent.service.model.Hierarchical;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Modification;

@Repository
public class HierarchicalDaoImpl extends JdbcDaoSupport implements HierarchicalDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalDaoImpl.class);

	private SqlHelper sqlHelper;

	@Autowired
	public HierarchicalDaoImpl(@Qualifier("app_dataSource") DataSource datSource) {
		try {
			setDataSource(datSource);
			this.sqlHelper = new SqlHelper(HierarchicalDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating HierarchicalDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public List<Hierarchical> fetchAllHierarchicalList(JdbcTemplate commonJdbcTemplate) {
		List<Hierarchical> hierarchicalList = null;
		try {
			String sql = sqlHelper.getSql("fetchAllHierarchicalList");
			hierarchicalList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new HierarchicalMapper());
		} catch (DataAccessException ae) {
			LOGGER.error("error while fetchAllHierarchicalList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while fetchAllHierarchicalList()", e);
		}
		return hierarchicalList;
	}

	@Override
	public Hierarchical fetchHierarchicalById(long id, JdbcTemplate commonJdbcTemplate) {
		Hierarchical hierarchical = null;
		try {
			String sql = sqlHelper.getSql("fetchHierarchicalById");
			hierarchical = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new HierarchicalExtracter(), id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while fetchHierarchicalById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while fetchHierarchicalById()", e);
		}
		return hierarchical;
	}

	@Override
	public long addHierarchical(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("addHierarchical");

			KeyHolder keyHolder = new GeneratedKeyHolder();
			getRequiredJdbcTemplate(commonJdbcTemplate).update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, hierarchical.getName());
					ps.setString(2, hierarchical.getDescription());
					ps.setString(3, hierarchical.getHierarchicalFormData());
					ps.setBoolean(4, hierarchical.isActive());
					ps.setString(5, hierarchical.getModification().getCreatedBy());
					ps.setString(6, hierarchical.getModification().getCreatedTime());
					ps.setString(7, hierarchical.getHierarchicalLevelData());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}
			hierarchical.setId(id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while addHierarchical()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addHierarchical()", e);
		}
		return id;
	}

	@Override
	public long updateHierarchical(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("updateHierarchical");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, hierarchical.getName(),
					hierarchical.getDescription(), hierarchical.getHierarchicalFormData(), hierarchical.isActive(),
					hierarchical.getModification().getCreatedBy(), hierarchical.getModification().getCreatedTime(),hierarchical.getHierarchicalLevelData(),
					hierarchical.getId());
			id = hierarchical.getId();

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateHierarchical()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateHierarchical()", e);
		}
		return id;
	}

	@Override
	public void deleteHierarchicalById(long id, JdbcTemplate commonJdbcTemplate) {
		try {
			String deleteHierarchyStructure = sqlHelper.getSql("deleteHierarchicalById");
			String deleteHierarchyMapping = sqlHelper.getSql("deleteHierarchicalMappingByHierarchyId");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(deleteHierarchyStructure, id);
			getRequiredJdbcTemplate(commonJdbcTemplate).update(deleteHierarchyMapping, id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteHierarchicalById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteHierarchicalById()", e);  
		}
	}

	JdbcTemplate getRequiredJdbcTemplate(JdbcTemplate clientjdbcTemplate) {
		return clientjdbcTemplate == null ? getJdbcTemplate() : clientjdbcTemplate;
	}

	@Override
	public long addHierarchicalAssociation(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate) {
		long associationId = 0;
		try {
			String sql = sqlHelper.getSql("addHierarchicalAssociation");

			KeyHolder keyHolder = new GeneratedKeyHolder();
			getRequiredJdbcTemplate(commonJdbcTemplate).update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "association_id" });
					ps.setLong(1, hierarchical.getId());
					ps.setString(2, hierarchical.getAssociationName());
					ps.setString(3, hierarchical.getMeasures());
					ps.setString(4, hierarchical.getDimensions());
					ps.setString(5, hierarchical.getTableName());
					ps.setString(6, hierarchical.getModification().getCreatedBy());
					ps.setString(7, hierarchical.getModification().getCreatedTime());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				associationId = autoIncrement.intValue();
			}
			hierarchical.setAssociationId(associationId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while addHierarchical()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addHierarchical()", e);
		}
		return associationId;
	}

	@Override
	public long updateHierarchicalAssociation(Hierarchical hierarchical, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("updateHierarchicalAssociation");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, hierarchical.getId(),
					hierarchical.getAssociationName(), hierarchical.getMeasures(), hierarchical.getDimensions(), hierarchical.getTableName(),
					hierarchical.getModification().getCreatedBy(), hierarchical.getModification().getCreatedTime(),
					hierarchical.getAssociationId());
			id = hierarchical.getId();

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateHierarchical()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateHierarchical()", e);
		}
		return id;
		
	}

	@Override
	public Hierarchical fetchHierarchicalAssociationByConfigId(long configId, JdbcTemplate commonJdbcTemplate) {
		Hierarchical hierarchical = new Hierarchical();
		try {
			String sql = sqlHelper.getSql("getHierarchicalAssociation");
			
			hierarchical = commonJdbcTemplate.query(sql, new Object[] { configId },
				new ResultSetExtractor<Hierarchical>() {
					@Override
					public Hierarchical extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							Hierarchical associateObj = new Hierarchical();
								associateObj.setId(rs.getLong("hierarchy_id"));
								associateObj.setAssociationId(rs.getLong("association_id"));
								associateObj.setAssociationName(rs.getString("hierarchy_association_name"));
								associateObj.setMeasures(rs.getString("measures"));
								associateObj.setDimensions(rs.getString("dimensions"));
								associateObj.setTableName(rs.getString("il_table_name"));
								associateObj.setIlId(rs.getInt("il_id"));
								associateObj.setDlId(rs.getInt("dl_id"));
							return associateObj;
						} else {
							return null;
						}
					}
				});
		} catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			
		}
	return hierarchical; 
    }

	@Override
	public Hierarchical fetachHierarchicalAndMappingInfo(long configId, JdbcTemplate commonJdbcTemplate) {
		Hierarchical hierarchical = new Hierarchical();
		try {
			String sql = sqlHelper.getSql("getHierarchicalandMapping");
			
			hierarchical = commonJdbcTemplate.query(sql, new Object[] { configId },
				new ResultSetExtractor<Hierarchical>() {
					@Override
					public Hierarchical extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							Hierarchical obj = new Hierarchical();
							obj.setId(rs.getLong("id"));
							obj.setName(rs.getString("name"));
							obj.setDescription(rs.getString("description"));
							obj.setHierarchicalFormData(rs.getString("hierarchical_form_data"));
							obj.setActive(rs.getBoolean("is_active"));
							obj.setId(rs.getLong("hierarchy_id"));
							obj.setAssociationId(rs.getLong("association_id"));
							obj.setAssociationName(rs.getString("hierarchy_association_name"));
							obj.setMeasures(rs.getString("measures"));
							obj.setDimensions(rs.getString("dimensions"));
							obj.setTableName(rs.getString("il_table_name"));
							
							Modification modification = new Modification();
							modification.setCreatedBy(rs.getString("created_by"));
							modification.setCreatedTime(rs.getString("created_time"));
							modification.setModifiedBy(rs.getString("modified_by"));
							modification.setModifiedTime(rs.getString("modified_date"));
							obj.setModification(modification);
							
							return obj;
						} else {
							return null;
						}
					}
				});
		} catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			
		}
	return hierarchical; 

	}

	@Override
	public List<JobResult> getJobResultsForHierarchical(Integer hierarchicalId, String hierarchicalName,
			String clientId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate) {


		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "_" + hierarchicalId + "_" + hierarchicalName + "\\_%";
			String sql = sqlHelper.getSql("getJobResultsForHierarchical");

			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam }, new RowMapper<JobResult>() {
				public JobResult mapRow(ResultSet rs, int i) throws SQLException {
					JobResult jobresult = new JobResult();
					/* jobresult.setPackageId(Integer.parseInt(packageId)); */
					/* jobresult.setUserId(userId); */
					jobresult.setHierarchicalId(hierarchicalId);
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
			logger.error("error while getJobResultsForCrossReference()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while getJobResultsForCrossReference()", e);
			
		}
		return resultList;
	
	}

	@Override
	public List<JobResult> getJobResultsForHierarchicalByDate(Integer hierarchicalId, String hierarchicalName,
			String clientId, String clientStagingSchemaName, String fromDate, String toDate,
			JdbcTemplate clientJdbcTemplate) {

		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_" + hierarchicalId + "_" + hierarchicalName + "\\_%";
			String sql = sqlHelper.getSql("getJobResultsForHierarchicalByDate");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam, fromDate, toDate },
					new RowMapper<JobResult>() {
						public JobResult mapRow(ResultSet rs, int i) throws SQLException {
							JobResult jobresult = new JobResult();
							jobresult.setHierarchicalId(hierarchicalId);
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
			logger.error("error while getJobResultsForCrossReferenceByDate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while getJobResultsForCrossReferenceByDate()", e);
		}
		return resultList;
	}

	@Override
	public List<Object> getDistinctValues(String columnName, String tableName, JdbcTemplate clientJdbcTemplate) {
			List<Object> valuesList = null;
			List<String> params = new ArrayList<>();
	
			if (StringUtils.isBlank(columnName) || columnName.equals("0")) {
				return null;
			}
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct `").append(columnName).append("` from `")
					.append(tableName).append("` order by `").append(columnName).append("` limit 1000");
	
			valuesList = clientJdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<Object>() {
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					String object = rs.getString(1);
					if (StringUtils.isEmpty(object) || "null".equalsIgnoreCase(object)) {
						return null;
					} else {
						return object;
					}
				}
			});
			valuesList.removeAll(Collections.singleton(null));
			return valuesList;
		}

	@Override
	public List<Object> getDistinctValuesByRange(String columnName, String tableName, String fromRange,
			String toRange, JdbcTemplate clientJdbcTemplate) {
		List<Object> valuesList = null;
		List<String> params = new ArrayList<>();
		params.add(fromRange);
		params.add(toRange);
		if (StringUtils.isBlank(columnName) || columnName.equals("0")) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct `").append(columnName).append("` from `")
				.append(tableName).append("`").append(" where `").append(columnName).append("` between ")
				.append(" ? and ? order by ").append(columnName).append(" asc limit 1000 ");

		valuesList = clientJdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<Object>() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				String object = rs.getString(1);
				if (StringUtils.isEmpty(object) || "null".equalsIgnoreCase(object)) {
					return null;
				} else {
					return rs.getObject(1);
				}
			}
		});
		valuesList.removeAll(Collections.singleton(null));
		return valuesList;
	 }

	@Override
	public List<Object> getDistinctValuesByPattern(String columnName, String tableName, String patternValue,
			String patternRangeValue, JdbcTemplate clientJdbcTemplate) {

		List<Object> valuesList = null;
		List<String> params = new ArrayList<>();
		if (StringUtils.isBlank(columnName) || columnName.equals("0")) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct `").append(columnName).append("` from `")
				.append(tableName).append("`").append(" where `").append(columnName).append("` like ? ");
		
			if(patternRangeValue.equalsIgnoreCase("starts_with")) {
				params.add(patternValue + "%");
			}else if(patternRangeValue.equalsIgnoreCase("ends_with")){
				params.add("%" + patternValue);
			}else if(patternRangeValue.equalsIgnoreCase("contains")) {
				params.add("%" + patternValue + "%");
			}

		valuesList = clientJdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<Object>() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				String object = rs.getString(1);
				if (StringUtils.isEmpty(object) || "null".equalsIgnoreCase(object)) {
					return null;
				} else {
					return rs.getObject(1);
				}
			}
		});
		valuesList.removeAll(Collections.singleton(null));
		return valuesList;
	}
	
	
  }