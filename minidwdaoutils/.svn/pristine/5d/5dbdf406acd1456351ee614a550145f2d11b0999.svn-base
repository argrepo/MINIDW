package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.SchedulerSlave;

public class SchedulerSlaveMapper implements RowMapper<SchedulerSlave> {

	@Override
	public SchedulerSlave mapRow(ResultSet rs, int rowNum) throws SQLException {
		SchedulerSlave slaveInfo = new SchedulerSlave();
		slaveInfo.setId(rs.getLong("id"));
		slaveInfo.setId(rs.getLong("master_id"));
		slaveInfo.setType(rs.getInt("type"));
		slaveInfo.setInstanceId(rs.getString("instance_id"));
		AwsCredentials aws = new AwsCredentials();
		aws.setId(rs.getInt("aws_id"));
		slaveInfo.setAws(aws);
		slaveInfo.setPackageExecutionCount(rs.getInt("no_of_package_executions"));
		slaveInfo.setFileUploadCount(rs.getInt("no_of_package_source_uploads"));
		slaveInfo.setHistoryLoadCount(rs.getInt("no_of_history_load_source_uploads"));
		slaveInfo.setHistoryExecutionCount(rs.getInt("no_of_history_load_executions"));
		slaveInfo.setRequestSchema(rs.getString("request_schema"));
		return slaveInfo;
	}

}
