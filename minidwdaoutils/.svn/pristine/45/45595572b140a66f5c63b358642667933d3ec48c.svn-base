package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.ELTMasterConfigDao;
import com.datamodel.anvizent.service.dao.util.EltMasterConfigExtracter;
import com.datamodel.anvizent.service.dao.util.EltMasterConfigMapper;
import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltMasterConfiguration;
import com.datamodel.anvizent.service.model.Modification;

@Repository
public class ELTMasterConfigDaoImpl extends JdbcDaoSupport implements ELTMasterConfigDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ELTMasterConfigDaoImpl.class);

	private SqlHelper sqlHelper;

	@Autowired
	public ELTMasterConfigDaoImpl(@Qualifier("app_dataSource") DataSource datSource) {
		try {
			setDataSource(datSource);
			this.sqlHelper = new SqlHelper(ELTMasterConfigDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating ELTDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public List<EltMasterConfiguration> fetchAllMasterConfigList(JdbcTemplate commonJdbcTemplate) {
		List<EltMasterConfiguration> masterList = null;
		try {
			String sql = sqlHelper.getSql("fetchAllMasterConfigList");
			masterList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new EltMasterConfigMapper());
		} catch (DataAccessException ae) {
			LOGGER.error("error while fetchAllMasterConfigList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while fetchAllMasterConfigList()", e);
		}
		return masterList;
	}

	@Override
	public EltMasterConfiguration fetchMasterConfigById(long id, JdbcTemplate commonJdbcTemplate) {
		EltMasterConfiguration masterList = null;
		try { 
			String sql = sqlHelper.getSql("fetchMasterConfigById");
			masterList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new EltMasterConfigExtracter(), id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while fetchMasterConfigById()", ae); 
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while fetchMasterConfigById()", e);
		}
		return masterList;
	}

	@Override
	public long addMasterConfig(EltMasterConfiguration eltMasterConfiguration, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("addMasterConfig");

			KeyHolder keyHolder = new GeneratedKeyHolder();
			getRequiredJdbcTemplate(commonJdbcTemplate).update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, eltMasterConfiguration.getName());
					ps.setString(2, eltMasterConfiguration.getSparkJobPath());
					ps.setString(3, eltMasterConfiguration.getEltClassPath());
					ps.setString(4, eltMasterConfiguration.getEltLibraryPath());
					ps.setString(5, eltMasterConfiguration.getMaster());
					ps.setString(6, eltMasterConfiguration.getDeployMode());
					ps.setString(7, eltMasterConfiguration.getSparkMaster());
					ps.setBoolean(8, eltMasterConfiguration.isActive());
					ps.setString(9, eltMasterConfiguration.getSourceType());
					ps.setString(10, eltMasterConfiguration.getSparkSubmitMode());
					ps.setString(11, eltMasterConfiguration.getHost());
					ps.setInt(12, eltMasterConfiguration.getPort());
					ps.setString(13, eltMasterConfiguration.getJobSubmitMode());

					ps.setBoolean(14, eltMasterConfiguration.getMasterDefault());
					ps.setString(15, eltMasterConfiguration.getModification().getCreatedBy());
					ps.setString(16, eltMasterConfiguration.getModification().getCreatedTime());
					ps.setString(17, eltMasterConfiguration.getAuthenticationType());
					ps.setString(18, eltMasterConfiguration.getPassword());
					ps.setString(19, eltMasterConfiguration.getPpkFile());
					ps.setString(20, eltMasterConfiguration.getUserName());

					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}
			eltMasterConfiguration.setId(id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while addMasterConfig()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addMasterConfig()", e);
		}
		return id;
	}

	@Override
	public long updateMasterConfig(EltMasterConfiguration eltMasterConfiguration, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("updateMasterConfig");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, eltMasterConfiguration.getName(),
					eltMasterConfiguration.getSparkJobPath(), eltMasterConfiguration.getEltClassPath(),
					eltMasterConfiguration.getEltLibraryPath(), eltMasterConfiguration.getMaster(),
					eltMasterConfiguration.getDeployMode(), eltMasterConfiguration.getSparkMaster(),
					eltMasterConfiguration.isActive(), eltMasterConfiguration.getSourceType(),
					eltMasterConfiguration.getSparkSubmitMode(),
					eltMasterConfiguration.getHost(), eltMasterConfiguration.getPort(),
					eltMasterConfiguration.getJobSubmitMode(), eltMasterConfiguration.getMasterDefault(),
					eltMasterConfiguration.getModification().getCreatedBy(),
					eltMasterConfiguration.getModification().getCreatedTime(),
					eltMasterConfiguration.getAuthenticationType(), eltMasterConfiguration.getPassword(),
					eltMasterConfiguration.getUserName(),
					eltMasterConfiguration.getPpkFile(),
					eltMasterConfiguration.getId());

			id = eltMasterConfiguration.getId();

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateMasterConfig()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateMasterConfig()", e);
		}
		return id;
	}


	@Override
	public void deleteMasterConfigById(long id, JdbcTemplate commonJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteMasterConfigById");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateMasterConfig()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateMasterConfig()", e);
		}
	}

	@Override
	public List<ELTConfigTags> fetchMasterConfigEnvironmentVariablesByMappingId(long id,
			JdbcTemplate commonJdbcTemplate) {
		List<ELTConfigTags> environmentVariables = null;
		try {
			String sql = sqlHelper.getSql("fetchMasterConfigEnvironmentVariablesByMappingId");
			environmentVariables = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql,
					new RowMapper<ELTConfigTags>() {
						@Override
						public ELTConfigTags mapRow(ResultSet rs, int rowNum) throws SQLException {
							ELTConfigTags keyConfig = new ELTConfigTags();
							keyConfig.setKey(rs.getString("key"));
							keyConfig.setValue(rs.getString("value"));
							return keyConfig;
						}
					}, id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while fetchMasterConfigEnvironmentVariablesByMappingId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while fetchMasterConfigEnvironmentVariablesByMappingId()", e);
		}
		return environmentVariables;
	}

	@Override
	public long addMasterConfigEnvironmentVariablesByMappingId(long id, List<ELTConfigTags> environmentVariables,
			JdbcTemplate commonJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("addMasterConfigEnvironmentVariablesByMappingId");
			List<Object[]> environmentVariablesList = new ArrayList<>();
			environmentVariables.forEach(keyConfig -> {
				environmentVariablesList.add(new Object[] { id, keyConfig.getKey(), keyConfig.getValue() });
			});
			getRequiredJdbcTemplate(commonJdbcTemplate).batchUpdate(sql, environmentVariablesList,
					new int[] { Types.BIGINT, Types.VARCHAR, Types.VARCHAR });
		} catch (DataAccessException ae) {
			LOGGER.error("error while addMasterConfigEnvironmentVariablesByMappingId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addMasterConfigEnvironmentVariablesByMappingId()", e);
		}
		return 0;
	}

	@Override
	public void deleteMasterConfigEnvironmentVariablesByMappingId(long id, JdbcTemplate commonJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteMasterConfigEnvironmentVariablesByMappingId");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteMasterConfigEnvironmentVariablesByMappingId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteMasterConfigEnvironmentVariablesByMappingId()", e);
		}
	}

	JdbcTemplate getRequiredJdbcTemplate(JdbcTemplate clientjdbcTemplate) {
		return clientjdbcTemplate == null ? getJdbcTemplate() : clientjdbcTemplate;
	}

	public int updateMasterConfigDefault(JdbcTemplate commonJdbcTemplate) {
		int count = 0;
		try {
			String sql = sqlHelper.getSql("updateMasterConfigDefault");
			count = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, new Object[] { false });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateMasterConfigDefault", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateMasterConfigDefault", e);

		}
		return count;

	}

	@Override
	public EltMasterConfiguration fetchDefaultMasterConfig(JdbcTemplate commonJdbcTemplate) {
		EltMasterConfiguration eltMasterConfiguration = null;
		try {
			String sql = sqlHelper.getSql("fetchDefaultMasterConfig");
			eltMasterConfiguration = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql,
					new EltMasterConfigExtracter());
			if (eltMasterConfiguration != null) {
				eltMasterConfiguration.setEnvironmentVariables(fetchMasterConfigEnvironmentVariablesByMappingId(
						eltMasterConfiguration.getId(), commonJdbcTemplate));
			}
		} catch (DataAccessException ae) {
			LOGGER.error("error while fetchDefaultMasterConfig()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while fetchDefaultMasterConfig()", e);
		}
		return eltMasterConfiguration;
	}

	@Override
	public void saveEltInitiatedInfo(long executionId, long packageId, long scheduleId, Integer dlId, int ilId,
			Object jobId, String executionResponsemap, Modification modification,JdbcTemplate commonJdbcTemplate) {
		
		int save = 0;
		try {
			String sql = sqlHelper.getSql("saveEltInitiatedInfo");
			save = getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, new Object[] {
					executionId,packageId,scheduleId,dlId,ilId,jobId,executionResponsemap,modification.getCreatedBy(),modification.getCreatedTime()
			});
		} catch (SqlNotFoundException e) {
			e.printStackTrace();
		}
	}

}