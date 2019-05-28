/**
 * 
 */
package com.anvizent.schedulers.service;



import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.MasterDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.QuartzDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerTriggerInfo;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;




@Service
public class MasterServiceImpl implements MasterService {

	protected static final Log LOG = LogFactory.getLog(MasterServiceImpl.class);
	
	@Autowired
	private MasterDao masterDao;

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private PackageDao packageDao;

	@Autowired
	private FileDao fileDao;

	@Autowired
	private ScheduleDao scheduleDao;

	@Autowired
	private QuartzDao quartzDao;
	
	
	@Override
	public long startSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo) {
		return quartzDao.startSchedulerInfo(quartzSchedulerInfo, null);
	}

	@Override
	public int updateSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo) {
		return quartzDao.updateScheduledInfo(quartzSchedulerInfo, null);
	}

	@Override
	public long addSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo) {
		return quartzDao.addSchedulerJobInfo(quartzSchedulerJobInfo, null);
	}

	@Override
	public int updateSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo) {
		return quartzDao.updateSchedulerJobInfo(quartzSchedulerJobInfo, null);
	}

	@Override
	public long addSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo) {
		return quartzDao.addSchedulerTriggerInfo(quartzSchedulerTriggerInfo, null);
	}

	@Override
	public int updateSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo) {
		return quartzDao.updateSchedulerTriggerInfo(quartzSchedulerTriggerInfo, null);
	}
	
	@Override
	public List<QuartzSchedulerJobInfo> getScheduledJobsInfo(SchedulerFilterJobDetails schedulerFilterJobDetails,String clientId) throws AnvizentCorewsException {
		JdbcTemplate jdbcTemplate = null;
		if (StringUtils.isNotBlank(clientId)) {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
			jdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
		}
		long masterId = schedulerFilterJobDetails.getSchedulerMaster().getId(); 
		List<QuartzSchedulerJobInfo> jobDetails = quartzDao.getScheduledJobsInfo(schedulerFilterJobDetails, jdbcTemplate);
		if ( jobDetails != null && jobDetails.size() >0  ) {
			for (QuartzSchedulerJobInfo job: jobDetails) {
				job.setClientId(StringUtils.isBlank(clientId) ? "1":clientId);
				job.setMasterId(masterId);
			}
		}
		return jobDetails;
	}
	
	@Override
	public SchedulerMaster getSchedulerServerMasterById(long id) {
		return commonDao.getSchedulerMasterById(id);
	}
	
	@Override
	public SchedulerSlave getServerSlaveById(int id) {
		return commonDao.getServerSlaveById(id);
	}
	
	@Override
	public List<Schedule> getPackagesForSchedulingByClientIdWithCronExpression(long clientId) throws AnvizentCorewsException {
		ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
		return scheduleDao.getPackagesForSchedulingByClientIdWithCronExpression(clientId +"", clientJdbcInstance.getClientAppdbJdbcTemplate());
	}
	
	@Override
	public List<QuartzSchedulerJobInfo> getTriggeredInfoByID(long jobId, String timeZone,String clientId) throws AnvizentCorewsException {
		JdbcTemplate jdbcTemplate = null;
		if (StringUtils.isNotBlank(clientId) && !clientId.equals("1")) {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId));
			jdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
		}
		return quartzDao.getTriggeredInfoByID(jobId, timeZone, jdbcTemplate);
	}
	
}
