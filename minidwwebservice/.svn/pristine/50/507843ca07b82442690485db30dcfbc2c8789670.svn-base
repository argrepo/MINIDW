/**
 * 
 */
package com.datamodel.anvizent.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.QuartzDao;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerTriggerInfo;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;

@Service
public class QuartzServiceImpl implements QuartzService {

	protected static final Log LOG = LogFactory.getLog(QuartzServiceImpl.class);

	@Autowired
	private QuartzDao quartzDao;

	@Override
	public long startSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return quartzDao.startSchedulerInfo(quartzSchedulerInfo, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return quartzDao.updateScheduledInfo(quartzSchedulerInfo, clientAppDbJdbcTemplate);
	}

	@Override
	public long addSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return quartzDao.addSchedulerJobInfo(quartzSchedulerJobInfo, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return quartzDao.updateSchedulerJobInfo(quartzSchedulerJobInfo, clientAppDbJdbcTemplate);
	}

	@Override
	public long addSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return quartzDao.addSchedulerTriggerInfo(quartzSchedulerTriggerInfo, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return quartzDao.updateSchedulerTriggerInfo(quartzSchedulerTriggerInfo, clientAppDbJdbcTemplate);
	}

	@Override
	public List<QuartzSchedulerJobInfo> getScheduledJobsInfo(SchedulerFilterJobDetails schedulerFilterJobDetails,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return quartzDao.getScheduledJobsInfo(schedulerFilterJobDetails, clientAppDbJdbcTemplate);
	}

	@Override
	public List<QuartzSchedulerJobInfo> getScheduledJobsInfoById(long schedulerId,
			String timeZone,JdbcTemplate clientAppDbJdbcTemplate) {
		return quartzDao.getScheduledJobsInfoById(schedulerId,timeZone,clientAppDbJdbcTemplate);
	}

	@Override
	public List<QuartzSchedulerJobInfo> getTriggeredInfoByID(long jobId, String timeZone,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return quartzDao.getTriggeredInfoByID(jobId,timeZone,clientAppDbJdbcTemplate);
	}
	
	@Override
	public int updateSchedulerJobInfoStatusWithNextFireTime(QuartzSchedulerJobInfo quartzSchedulerJobInfo,
			JdbcTemplate clientAppDbJdbcTemplate) throws SqlNotFoundException {
		return quartzDao.updateSchedulerJobInfoStatusWithNextFireTime(quartzSchedulerJobInfo, clientAppDbJdbcTemplate);
	}

}
