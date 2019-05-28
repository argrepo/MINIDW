package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.datamodel.anvizent.service.model.EltLoadParameters;

public class EltLoadParametersExtracter implements ResultSetExtractor<EltLoadParameters> {

	@Override
	public EltLoadParameters extractData(ResultSet rs) throws SQLException, DataAccessException {
		if (rs.next()) {
			return getMasterConfig(rs);
		}
		return null;
	}
	
	public EltLoadParameters getMasterConfig(ResultSet rs) throws SQLException {
		EltLoadParameters eltLoadParameters = new EltLoadParameters();
		eltLoadParameters.setId(rs.getLong("id"));
		eltLoadParameters.setName(rs.getString("name"));
		eltLoadParameters.setNoOfExecutors(rs.getInt("no_of_executors"));
		eltLoadParameters.setExecutorMemory(rs.getInt("executor_memory"));
		eltLoadParameters.setExecutorMemoryType(rs.getString("executor_memory_type"));
		eltLoadParameters.setExecutorCores(rs.getInt("executor_cores"));
		eltLoadParameters.setActive(rs.getBoolean("is_active"));
		return eltLoadParameters;
	}
	
}
