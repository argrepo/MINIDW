package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.ELTJobTagsDao;
import com.datamodel.anvizent.service.dao.util.EltJobInfoMapper;
import com.datamodel.anvizent.service.dao.util.EltJobTagInfoExtractor;
import com.datamodel.anvizent.service.dao.util.EltJobTagInfoMapper;
import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;
import com.datamodel.anvizent.service.model.EltJobTagInfo;

@Repository
public class ELTJobTagsDaoImpl extends JdbcDaoSupport implements ELTJobTagsDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ELTJobTagsDaoImpl.class);

	private SqlHelper sqlHelper; 

	@Autowired
	public ELTJobTagsDaoImpl(@Qualifier("app_dataSource") DataSource datSource) {
		try {
			setDataSource(datSource);
			this.sqlHelper = new SqlHelper(ELTJobTagsDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating ELTDaoImpl SqlHelper.", ex);
		}
	}


	@Override
	public List<EltJobTagInfo> getEltJobTagList(JdbcTemplate commonJdbcTemplate) {
		List<EltJobTagInfo> eltjobTagInfoList = null;
		try {
			String sql = sqlHelper.getSql("getEltJobTagList");
			eltjobTagInfoList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new EltJobTagInfoMapper());
		} catch (DataAccessException ae) {
			LOGGER.error("error while getEltJobTagList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getEltJobTagList()", e);
		}
		return eltjobTagInfoList;
	}

	@Override
	public EltJobTagInfo getEltJobTagInfoById(long tagId, JdbcTemplate commonJdbcTemplate) {
		EltJobTagInfo eltjobTagInfo = null;
		try {
			String sql = sqlHelper.getSql("getEltJobTagInfoById");
			eltjobTagInfo = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new EltJobTagInfoExtractor(), tagId);
			if (eltjobTagInfo != null ) {
				sql = sqlHelper.getSql("getEltJobInfo");
				List<EltJobInfo> jobInfoList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql,new EltJobInfoMapper(),tagId);
				jobInfoList.forEach(job -> {
					job.setDerivedComponent(getEltJobDerivedMappingInfo(job.getId(),null));
				});
				eltjobTagInfo.setJobsList(jobInfoList);
			}
		} catch (DataAccessException ae) {
			LOGGER.error("error while getEltJobTagInfoById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getEltJobTagInfoById()", e);
		}
		return eltjobTagInfo;
	}
	
	@Override
	public List<ELTConfigTags> getEltJobDerivedMappingInfo(long jobId,JdbcTemplate commonJdbcTemplate) {
		List<ELTConfigTags> eltjobTagInfoList = null;
		try {
			String sql = sqlHelper.getSql("getEltJobDerivedMappingInfo");
			eltjobTagInfoList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new Object[]{jobId}, new RowMapper<ELTConfigTags>(){
				@Override
				public ELTConfigTags mapRow(ResultSet rs, int rowNum) throws SQLException {
					ELTConfigTags eLTKeyConfig = new ELTConfigTags();
					eLTKeyConfig.setTagId(rs.getLong("tag_id"));
					eLTKeyConfig.setTagName(rs.getString("tag_name"));
					return eLTKeyConfig;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getEltJobDerivedMappingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getEltJobDerivedMappingInfo()", e);
		}
		return eltjobTagInfoList;
	}
	
	
	@Override
	public int addEltJobTagInfo(EltJobTagInfo eltJobTagInfo, JdbcTemplate commonJdbcTemplate) {
		int id = 0;
			try {
				String sql = sqlHelper.getSql("addEltJobTagInfo");
				
				KeyHolder keyHolder = new GeneratedKeyHolder();
				getRequiredJdbcTemplate(commonJdbcTemplate).update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
						ps.setString(1, eltJobTagInfo.getTagName());
						ps.setLong(2, eltJobTagInfo.getGlobalValues().getTagId());
						ps.setBoolean(3, eltJobTagInfo.isActive());
						ps.setString(4, eltJobTagInfo.getModification().getCreatedBy());
						ps.setString(5, eltJobTagInfo.getModification().getCreatedTime());
						return ps;
					}
				}, keyHolder);
				if (keyHolder != null) {
					Number autoIncrement = keyHolder.getKey();
					id = autoIncrement.intValue();
				}
		} catch (DataAccessException ae) {
			LOGGER.error("error while addEltJobTagInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addEltJobTagInfo()", e);
			
		}
		return  id;
		
	}

	@Override
	public int updateEltJobTagInfo(EltJobTagInfo eltJobTagInfo, JdbcTemplate commonJdbcTemplate) {
		int id = 0;
		try {
			String sql = sqlHelper.getSql("updateEltJobTagInfo");
			id = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, eltJobTagInfo.getTagName(),eltJobTagInfo.getGlobalValues().getTagId(), eltJobTagInfo.isActive(),
					eltJobTagInfo.getModification().getCreatedBy(), eltJobTagInfo.getModification().getCreatedTime(),
					eltJobTagInfo.getTagId());
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateEltJobTagInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateEltJobTagInfo()", e);
		}
		return id;
	}

	@Override
	public void deleteEltJobTagInfo(long tagId, JdbcTemplate commonJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteEltJobTagInfo");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, tagId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateEltJobTagInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateEltJobTagInfo()", e);
		}
	}

	@Override
	public long saveEltJobMappingInfo(EltJobInfo eltJobInfo, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("saveEltJobMappingInfo");
			
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getRequiredJdbcTemplate(commonJdbcTemplate).update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setLong(1, eltJobInfo.getJobTagId());
					ps.setString(2, eltJobInfo.getJobName());
					ps.setInt(3, eltJobInfo.getJobSeq());
					ps.setObject(4, eltJobInfo.getConfigProp().getTagId());
					ps.setObject(5, eltJobInfo.getValuesProp().getTagId());
					ps.setObject(6,eltJobInfo.getStatsProp().getTagId());
					
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.longValue();
			}
	} catch (DataAccessException ae) {
		LOGGER.error("error while createHybridClientsGroupingInfo()", ae);
		throw new AnvizentRuntimeException(ae);
	} catch (SqlNotFoundException e) {
		LOGGER.error("error while createHybridClientsGroupingInfo()", e);
		
	}
		return (int) id;
	}

	
	@Override
	public int saveEltDerivedComponentInfo(long mappingId, ELTConfigTags keyConfig, JdbcTemplate commonJdbcTemplate) {
		int count = 0;
		try {
			String sql = sqlHelper.getSql("addDerivedComponentTags");
				count = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, mappingId, keyConfig.getTagId());
		} catch (DataAccessException ae) {
			LOGGER.error("error while addEltJobTagInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addEltJobTagInfo()", e);
			count = 0;
		}
		return count;
	}

	@Override
	public int updateEltJobMappingInfo(EltJobInfo eltJobInfo, JdbcTemplate commonJdbcTemplate) {
		int id = 0;
		try {
			String sql = sqlHelper.getSql("updateEltJobMappingInfo");
			id = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, 
					eltJobInfo.getJobName(),
					eltJobInfo.getJobSeq(),
					eltJobInfo.getConfigProp().getTagId(),
					eltJobInfo.getValuesProp().getTagId(),
					eltJobInfo.getStatsProp().getTagId(),
					eltJobInfo.getActiveStatus(),
					eltJobInfo.getId());
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateEltJobTagInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateEltJobTagInfo()", e);
		}
		return id;
	}

	
	@Override
	public void deleteDerivedByMappingId(long derivedId, JdbcTemplate commonJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteDerivedByMappingId");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, derivedId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while derivedId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while derivedId()", e);
		}
	}

	

	@Override
	public int updateEltJobSequenceInfo(EltJobInfo eltJobInfo, JdbcTemplate commonJdbcTemplate) {
			int update = 0;
			try {
				String sql = sqlHelper.getSql("updateEltJobSequenceInfo");
				update = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql,
						new Object[] { eltJobInfo.getJobSeq(),
								eltJobInfo.getId() });
			} catch (DataAccessException ae) {
				LOGGER.error("error while updateEltStgKeyConfig()", ae);
				throw new AnvizentRuntimeException(ae);
			} catch (SqlNotFoundException e) {
				LOGGER.error("error while updateEltStgKeyConfig()", e);

			}
			return update;
		}
	

	JdbcTemplate getRequiredJdbcTemplate(JdbcTemplate clientjdbcTemplate) {
		return clientjdbcTemplate == null ? getJdbcTemplate() : clientjdbcTemplate;
	}

	

	
	

}
