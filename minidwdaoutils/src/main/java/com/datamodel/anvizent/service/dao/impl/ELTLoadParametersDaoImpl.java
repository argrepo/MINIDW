package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.ELTLoadParametersDao;
import com.datamodel.anvizent.service.dao.util.EltLoadParametersExtracter;
import com.datamodel.anvizent.service.dao.util.EltLoadParametersMapper;
import com.datamodel.anvizent.service.model.EltLoadParameters;

@Repository
public class ELTLoadParametersDaoImpl extends JdbcDaoSupport implements ELTLoadParametersDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ELTLoadParametersDaoImpl.class);

	private SqlHelper sqlHelper;

	@Autowired
	public ELTLoadParametersDaoImpl(@Qualifier("app_dataSource") DataSource datSource) {
		try {
			setDataSource(datSource);
			this.sqlHelper = new SqlHelper(ELTLoadParametersDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating ELTLoadParametersDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public List<EltLoadParameters> fetchLoadParametersList(JdbcTemplate commonJdbcTemplate) {
		List<EltLoadParameters> masterList = null;
		try {
			String sql = sqlHelper.getSql("fetchLoadParametersList");
			masterList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql,
					new EltLoadParametersMapper());
		} catch (DataAccessException ae) {
			LOGGER.error("error while fetchLoadParametersList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while fetchLoadParametersList()", e);
		}
		return masterList;
	}

	@Override
	public EltLoadParameters fetchLoadParametersById(long id, JdbcTemplate commonJdbcTemplate) {
		EltLoadParameters masterList = null;
		try {
			String sql = sqlHelper.getSql("fetchLoadParametersById");
			masterList = getRequiredJdbcTemplate(commonJdbcTemplate).query(sql, new EltLoadParametersExtracter(),
					id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while fetchLoadParametersById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while fetchLoadParametersById()", e);
		}
		return masterList;
	}

	@Override
	public long addLoadParameters(EltLoadParameters eltLoadParameters, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("addLoadParameters");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getRequiredJdbcTemplate(commonJdbcTemplate).update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, eltLoadParameters.getName());
					ps.setInt(2, eltLoadParameters.getNoOfExecutors());
					ps.setInt(3, eltLoadParameters.getExecutorMemory());
					ps.setString(4, eltLoadParameters.getExecutorMemoryType());
					ps.setInt(5, eltLoadParameters.getExecutorCores());
					ps.setBoolean(6, eltLoadParameters.isActive());
					ps.setString(7, eltLoadParameters.getModification().getCreatedBy());
					ps.setString(8, eltLoadParameters.getModification().getCreatedTime());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}
			eltLoadParameters.setId(id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while addLoadParameters()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addLoadParameters()", e);
		}
		return id;
	}

	@Override
	public long updateLoadParameters(EltLoadParameters eltLoadParameters, JdbcTemplate commonJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("updateLoadParameters");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, eltLoadParameters.getName(),
					eltLoadParameters.getNoOfExecutors(), eltLoadParameters.getExecutorMemory(),
					eltLoadParameters.getExecutorMemoryType(), eltLoadParameters.getExecutorCores(),
					eltLoadParameters.isActive(), eltLoadParameters.getModification().getCreatedBy(),
					eltLoadParameters.getModification().getCreatedTime(), eltLoadParameters.getId());
			id = eltLoadParameters.getId();
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateLoadParameters()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateLoadParameters()", e);
		}
		return id;
	}

	@Override
	public void deleteLoadParametersById(long id, JdbcTemplate commonJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteLoadParametersById");
			getRequiredJdbcTemplate(commonJdbcTemplate).update(sql, id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteLoadParametersById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteLoadParametersById()", e);
		}
	}

	JdbcTemplate getRequiredJdbcTemplate(JdbcTemplate clientjdbcTemplate) {
		return clientjdbcTemplate == null ? getJdbcTemplate() : clientjdbcTemplate;
	}

}