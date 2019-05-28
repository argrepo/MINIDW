package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.datamodel.anvizent.service.model.EltMasterConfiguration;

public class EltMasterConfigExtracter implements ResultSetExtractor<EltMasterConfiguration>{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public EltMasterConfiguration extractData(ResultSet rs) throws SQLException, DataAccessException {
		if (rs.next()) {
			return getMasterConfig(rs);
		}
		return null;
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
		eltMasterConfiguration.setSparkSubmitMode(rs.getString("spark_submit_mode"));
		eltMasterConfiguration.setHost(rs.getString("host"));
		eltMasterConfiguration.setPort(rs.getInt("port"));
		eltMasterConfiguration.setJobSubmitMode(rs.getString("job_submit_mode"));
		eltMasterConfiguration.setAuthenticationType(rs.getString("authentication_type"));
		eltMasterConfiguration.setPassword(rs.getString("password"));
		eltMasterConfiguration.setPpkFile(rs.getString("ppk_file"));
		eltMasterConfiguration.setUserName(rs.getString("user_name"));
		return eltMasterConfiguration;
	}
}
