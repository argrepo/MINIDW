package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.datamodel.anvizent.security.AESConverter;
import com.datamodel.anvizent.service.model.CloudClient;

public class CloudClientMapper implements RowMapper<CloudClient> {

	@Override
	public CloudClient mapRow(ResultSet rs, int rowNum) throws SQLException {
		CloudClient cloudClient = new CloudClient();
		cloudClient.setId(rs.getLong("id"));
		cloudClient.setClientName(rs.getString("clientname"));
		cloudClient.setClientDescription(rs.getString("clientdescription"));
		cloudClient.setLicenseId(rs.getLong("licenseid"));
		try {
			cloudClient.setDeploymentType(AESConverter.decrypt(rs.getString("deployment_type")));
		} catch (Exception e) {e.getMessage();}
		cloudClient.setActive(rs.getBoolean("is_active"));
		cloudClient.setDruidEnabled(rs.getBoolean("isdruid_enabled"));
		cloudClient.setRegionId(rs.getLong("region_id"));
		cloudClient.setClusterId(rs.getLong("cluster_id"));
		return cloudClient;
	}

}
