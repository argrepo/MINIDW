package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
import com.datamodel.anvizent.service.dao.ELTConfigTagsDao;
import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;

@Repository
public class ELTConfigTagsDaoImpl extends JdbcDaoSupport implements ELTConfigTagsDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ELTConfigTagsDaoImpl.class);

	private SqlHelper sqlHelper;

	@Autowired
	public ELTConfigTagsDaoImpl(@Qualifier("app_dataSource") DataSource datSource) {
		try {
			setDataSource(datSource);
			this.sqlHelper = new SqlHelper(ELTConfigTagsDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating ELTDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public int saveEltStgKeyConfig(ELTConfigTags eltStgKeyConfig, JdbcTemplate commonJdbcTemplate) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("saveEltStgKeyConfig");
			save = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, new Object[] { eltStgKeyConfig.getSeqId(), eltStgKeyConfig.getTagId(),
					eltStgKeyConfig.getKey(), eltStgKeyConfig.getValue() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveEltStgKeyConfig()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveEltStgKeyConfig()", e);

		}
		return save;
	}

	@Override
	public int updateEltStgKeyConfig(ELTConfigTags eltStgKeyConfig, JdbcTemplate commonJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateEltStgKeyConfig");
			update = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql,
					new Object[] { eltStgKeyConfig.getSeqId(), eltStgKeyConfig.getTagId(), eltStgKeyConfig.getKey(),
							eltStgKeyConfig.getValue(), eltStgKeyConfig.getId() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateEltStgKeyConfig()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateEltStgKeyConfig()", e);

		}
		return update;
	}

	@Override
	public List<ELTConfigTags> getELTStgConfigInfo(JdbcTemplate commonJdbcTemplate) {
		List<ELTConfigTags> eltStgKeyList = null;
		try {
			String sql = sqlHelper.getSql("getELTStgConfigInfo");
			eltStgKeyList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new RowMapper<ELTConfigTags>() {
				@Override
				public ELTConfigTags mapRow(ResultSet rs, int rowNum) throws SQLException {
					ELTConfigTags eltStgInfo = new ELTConfigTags();
					eltStgInfo.setId(rs.getLong("id"));
					eltStgInfo.setSeqId(rs.getLong("seq_id"));
					eltStgInfo.setKey(rs.getString("config_key"));
					eltStgInfo.setValue(rs.getString("config_value"));
					return eltStgInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getELTStgConfigInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getELTStgConfigInfo()", e);
			e.getMessage();
		}
		return eltStgKeyList;
	}

	@Override
	public int deleteEltStgKeyConfigById(Integer id, JdbcTemplate commonJdbcTemplate) {
		int count = 0;
		try {
			String sql = sqlHelper.getSql("deleteEltStgKeyConfigById");
			count = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, new Object[] { id });

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteEltStgKeyConfigById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteEltStgKeyConfigById", e);

		}

		return count;
	}

	@Override
	public List<ELTConfigTags> getELTMainConfigInfo(JdbcTemplate commonJdbcTemplate) {
		List<ELTConfigTags> eltMainKeyList = null;
		try {
			String sql = sqlHelper.getSql("getELTMainConfigInfo");
			eltMainKeyList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new RowMapper<ELTConfigTags>() {
				@Override
				public ELTConfigTags mapRow(ResultSet rs, int rowNum) throws SQLException {
					ELTConfigTags eltStgInfo = new ELTConfigTags();
					eltStgInfo.setId(rs.getLong("id"));
					eltStgInfo.setSeqId(rs.getLong("seq_id"));
					eltStgInfo.setKey(rs.getString("stg_key"));
					eltStgInfo.setValue(rs.getString("stg_value"));
					return eltStgInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getELTMainConfigInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getELTMainConfigInfo()", e);
			e.getMessage();
		}
		return eltMainKeyList;
	}

	@Override
	public List<ELTConfigTags> getEltConfigTags(JdbcTemplate commonJdbcTemplate) {
		List<ELTConfigTags> eltStgKeyList = null;
		try {
			String sql = sqlHelper.getSql("getEltConfigTags");
			eltStgKeyList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new RowMapper<ELTConfigTags>() {
				@Override
				public ELTConfigTags mapRow(ResultSet rs, int rowNum) throws SQLException {
					ELTConfigTags eltStgInfo = new ELTConfigTags();
					eltStgInfo.setTagId(rs.getLong("tag_id"));
					eltStgInfo.setTagName(rs.getString("tag_name"));
					eltStgInfo.setActive(rs.getBoolean("is_active"));
					return eltStgInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getEltConfigTags()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getEltConfigTags()", e);
			e.getMessage();
		}
		return eltStgKeyList;
	}

	@Override
	public long saveEltConfigPairInfo(ELTConfigTags eltilConfigs, JdbcTemplate commonJdbcTemplate) {
		long tagId = 0;
		try {
			String sql = sqlHelper.getSql("saveEltConfigPairInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getRequiredJdbcTemplate(commonJdbcTemplate).update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "tag_id" });
					ps.setString(1, eltilConfigs.getTagName());
					ps.setBoolean(2, eltilConfigs.getActive());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				tagId = autoIncrement.longValue();
			}

		} catch (DataAccessException ae) {
			LOGGER.error("error while saveEltStgKeyConfig()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveEltStgKeyConfig()", e);
			throw new AnvizentRuntimeException(e);
		}
		return tagId;
	}

	@Override
	public List<ELTConfigTags> getEltConfigByTagId(Integer tagId, JdbcTemplate commonJdbcTemplate) {
		List<ELTConfigTags> eltConfigList = null;
		try {
			String sql = sqlHelper.getSql("getEltConfigByTagId");

			eltConfigList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new Object[] { tagId }, new RowMapper<ELTConfigTags>() {
				@Override
				public ELTConfigTags mapRow(ResultSet rs, int rowNum) throws SQLException {
					ELTConfigTags eltInfo = new ELTConfigTags();
					eltInfo.setId(rs.getLong("id"));
					eltInfo.setTagId(rs.getLong("tag_id"));
					eltInfo.setSeqId(rs.getLong("seq_id"));
					eltInfo.setKey(rs.getString("config_key"));
					eltInfo.setValue(rs.getString("config_value"));
					return eltInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getEltConfigByTagId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getEltConfigByTagId()", e);
			e.getMessage();
		}
		return eltConfigList;
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
	public int updateEltConfigTags(ELTConfigTags eltilConfigs, JdbcTemplate commonJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateEltConfigTags");
			update = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql,
					new Object[] { eltilConfigs.getTagName(), eltilConfigs.getActive(),
							eltilConfigs.getTagId() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateEltConfigTags()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateEltConfigTags()", e);

		}
		return update;
	}

	@Override
	public ELTConfigTags getEltConfigTagsByID(long id, JdbcTemplate commonJdbcTemplate) {
		ELTConfigTags eltKeyConfig = null;
		try {
			String sql = sqlHelper.getSql("getEltConfigTagsByID");
			eltKeyConfig = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql,new Object[] {id}, new ResultSetExtractor<ELTConfigTags>(){

				@Override
				public ELTConfigTags extractData(ResultSet rs) throws SQLException, DataAccessException {
					ELTConfigTags eltKeyConfigInfo = new ELTConfigTags();
					if (rs != null && rs.next()) {
						eltKeyConfigInfo.setTagName(rs.getString("tag_name"));
						eltKeyConfigInfo.setActive(rs.getBoolean("is_active"));
						return eltKeyConfigInfo;
					}else{
						return null;
					}
					
				}
				
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getEltJobTagInfoById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getEltJobTagInfoById()", e);
		}
		return eltKeyConfig;
	}


	JdbcTemplate getRequiredJdbcTemplate(JdbcTemplate clientjdbcTemplate) {
		return clientjdbcTemplate == null ? getJdbcTemplate() : clientjdbcTemplate;
	}

	@Override
	public int deleteALLEltStgKeyConfigById(Integer tagId, JdbcTemplate commonJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("deleteALLEltStgKeyConfigById");
			update = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, tagId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteALLEltStgKeyConfigById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteALLEltStgKeyConfigById()", e);
		}
		return update;
	}

	@Override
	public long saveEltCloneTagInfo(ELTConfigTags eltilConfigs, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("saveEltCloneTagInfo");
			
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getRequiredJdbcTemplate(commonJdbcTemplate).update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, eltilConfigs.getTagName());
					ps.setBoolean(2, eltilConfigs.getActive());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}
	}catch (DuplicateKeyException ae) {
		LOGGER.error("error while saveIlInfo()", ae);
		throw new AnvizentDuplicateFileNameException(ae);
	} catch (DataAccessException ae) {
		LOGGER.error("error while saveEltCloneTagInfo()", ae);
		throw new AnvizentRuntimeException(ae);
	} catch (SqlNotFoundException e) {
		LOGGER.error("error while saveEltCloneTagInfo()", e);
		
	}
		return id;
	}

	
	
	@Override
	public void saveEltCloneTagKeyPairInfo(long id, Long tagId, JdbcTemplate commonJdbcTemplate) {
		ELTConfigTags eltKeyConfig = null;
		try {
			String sql = sqlHelper.getSql("getEltCloneTagKeyPairInfo");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, new Object[]{id,tagId});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveEltCloneTagKeyPairInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveEltCloneTagKeyPairInfo()", e);
		}
		
	}

	@Override
	public void saveELTKeyValuePairs(long tagId, List<Map<String, Object>> valuesList, JdbcTemplate commonJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("saveELTKeyValuePairs");
			int seqId = 1;
			for (Map<String, Object> mapValues : valuesList) {
				for(Map.Entry<String, Object> entry : mapValues.entrySet()) {
					 String key = entry.getKey();
					 String value = (String) entry.getValue();
					 getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, new Object[] {tagId,seqId,key,value});
					 seqId++;
				}
			}
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveELTKeyValuePairs()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveELTKeyValuePairs()", e);
			throw new AnvizentRuntimeException(e);
		}
	}

}
