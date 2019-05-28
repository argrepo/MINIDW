package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.datamodel.anvizent.service.model.EltLoadParameters;

public class EltLoadParametersMapper implements RowMapper<EltLoadParameters> {


	@Override
	public EltLoadParameters mapRow(ResultSet rs, int rowNum) throws SQLException {
		return getMasterConfig(rs);
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
