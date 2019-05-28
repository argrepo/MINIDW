
/**
 * 
 */
package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerTriggerInfo;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;

public interface QuartzService {

	long startSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo, JdbcTemplate clientAppDbJdbcTemplate);

	int updateSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo, JdbcTemplate clientAppDbJdbcTemplate);

	long addSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo, JdbcTemplate clientAppDbJdbcTemplate);

	int updateSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo, JdbcTemplate clientAppDbJdbcTemplate);

	long addSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo,
			JdbcTemplate clientAppDbJdbcTemplate);

	int updateSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo,
			JdbcTemplate clientAppDbJdbcTemplate);

	List<QuartzSchedulerJobInfo> getScheduledJobsInfo(SchedulerFilterJobDetails schedulerFilterJobDetails,
			JdbcTemplate clientAppDbJdbcTemplate);

	List<QuartzSchedulerJobInfo> getScheduledJobsInfoById(long schedulerId, String timeZone, JdbcTemplate clientAppDbJdbcTemplate);

	List<QuartzSchedulerJobInfo> getTriggeredInfoByID(long jobId, String timeZone,JdbcTemplate clientAppDbJdbcTemplate);
	
	int updateSchedulerJobInfoStatusWithNextFireTime(QuartzSchedulerJobInfo quartzSchedulerJobInfo, JdbcTemplate clientAppDbJdbcTemplate) throws SqlNotFoundException; 

}
