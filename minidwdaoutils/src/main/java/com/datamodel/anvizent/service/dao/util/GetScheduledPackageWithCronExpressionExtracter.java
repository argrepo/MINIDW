package com.datamodel.anvizent.service.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.Schedule;

public class GetScheduledPackageWithCronExpressionExtracter implements ResultSetExtractor<Schedule> {

	@Override
	public Schedule extractData(ResultSet rs) throws SQLException, DataAccessException {
		if (rs.next()) {
			return getData(rs);
		}
		return null;
	}
	
	Schedule getData(ResultSet rs) throws SQLException {
		Schedule scheduleinfo = new Schedule();
		scheduleinfo.setScheduleId(rs.getInt("schedule_id"));
		scheduleinfo.setPackageId(rs.getInt("package_ID"));
		scheduleinfo.setRecurrencePattern(rs.getString("recurrence_pattern"));
		scheduleinfo.setScheduleStartDate(rs.getString("start_date"));
		scheduleinfo.setScheduleEndDate(rs.getString("end_date"));
		scheduleinfo.setTimeZone(rs.getString("time_zone"));
		scheduleinfo.setCronExpression(rs.getString("cron_expression"));
		scheduleinfo.setUserId(rs.getString("user_id"));
		scheduleinfo.setScheduleType(rs.getString("schedule_type"));
		Package userPackage = new Package();
		userPackage.setPackageId(rs.getInt("package_ID"));
		userPackage.setPackageName(rs.getString("package_name"));
		userPackage.setIsStandard(rs.getBoolean("isStandard"));
		userPackage.setIsActive(rs.getBoolean("isactive"));
		scheduleinfo.setUserPackage(userPackage);
		return scheduleinfo;
	}

}
