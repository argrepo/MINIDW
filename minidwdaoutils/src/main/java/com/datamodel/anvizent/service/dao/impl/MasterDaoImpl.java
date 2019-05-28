/**
 * 
 */
package com.datamodel.anvizent.service.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.MasterDao;

public class MasterDaoImpl extends JdbcDaoSupport implements MasterDao {

	private SqlHelper sqlHelper;
	protected static final Log LOG = LogFactory.getLog(MasterDaoImpl.class);
	PlatformTransactionManager transactionManager = null;
	
	
	public MasterDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
		try {
			this.sqlHelper = new SqlHelper(MasterDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating QuartzDaoImpl SqlHelper.", ex);
		}
	}
	
	JdbcTemplate getRequiredJdbcTemplate(JdbcTemplate clientjdbcTemplate) {
		return clientjdbcTemplate == null ? getJdbcTemplate() : clientjdbcTemplate;
	}

	@Override
	public Map<String, String> getJobExecutionLimitByClient(String clientId, JdbcTemplate jdbcTemplate) {
		Map<String, String> props = new HashMap<>();
		try {
			String sql = sqlHelper.getSql("getJobUploadandExecutionLimitByClient");
			props = getRequiredJdbcTemplate(jdbcTemplate).query(sql, new ResultSetExtractor<Map<String, String>> (){
				@Override
				public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Map<String, String> props = new HashMap<>();
					if(rs == null)
					   return null;
					while (rs.next()) {
						props.put(rs.getString("prop_key"), rs.getString("prop_value"));
					}
					return props;
				}
			});
		}catch (DataAccessException ae) {
			LOG.error("error while updateMasterConfigDefault", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateMasterConfigDefault", e);
		}
		return props;
	}
	
}
