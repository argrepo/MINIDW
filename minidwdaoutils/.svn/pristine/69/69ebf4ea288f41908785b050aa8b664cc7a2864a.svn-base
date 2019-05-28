package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.datamodel.anvizent.service.model.EltMasterConfiguration;

public class EltMasterConfigMapper implements RowMapper<EltMasterConfiguration> {

	@Override
	public EltMasterConfiguration mapRow(ResultSet rs, int rowNum) throws SQLException {
		return getMasterConfig(rs);
	}
	
	public EltMasterConfiguration getMasterConfig(ResultSet rs) throws SQLException {
		EltMasterConfiguration eltMasterConfiguration = new EltMasterConfiguration();
		eltMasterConfiguration.setId(rs.getLong("id"));
		eltMasterConfiguration.setName(rs.getString("name"));
		eltMasterConfiguration.setSparkJobPath(rs.getString("spark_job_path"));
		eltMasterConfiguration.setEltClassPath(rs.getString("elt_class_path"));
		eltMasterConfiguration.setEltLibraryPath(rs.getString("elt_library_path"));
		eltMasterConfiguration.setMaster(rs.getString("master"));
		eltMasterConfiguration.setDeployMode(rs.getString("deploy_mode"));
		eltMasterConfiguration.setSparkMaster(rs.getString("spark_master"));
		eltMasterConfiguration.setActive(rs.getBoolean("is_active"));
		eltMasterConfiguration.setMasterDefault(rs.getBoolean("is_default"));
		eltMasterConfiguration.setSourceType(rs.getString("source_type"));
		return eltMasterConfiguration;
	}
	
}
