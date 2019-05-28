/**
 * 
 */
package com.anvizent.scheduler.service;



import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.anvizent.scheduler.master.MasterQuartzScheduler;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.MasterDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.QuartzDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.model.PackageExecution;
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
	
	@Autowired
    MasterQuartzScheduler masterScheduler;
    
    
	
	@Override
	public long startSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo) {
		return quartzDao.startSchedulerInfo(quartzSchedulerInfo, null);
	}

	@Override
	public int updateSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo) {
		return quartzDao.updateScheduledInfo(quartzSchedulerInfo, null);
	}

	@Override
	public long addSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo, String clientId) throws AnvizentCorewsException {
		JdbcTemplate jdbcTemplate = null;
		if (StringUtils.isNotBlank(clientId)) {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
			jdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
		}
		return quartzDao.addSchedulerJobInfo(quartzSchedulerJobInfo, jdbcTemplate);
	}

	@Override
	public int updateSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo,String clientId) throws AnvizentCorewsException {
		JdbcTemplate jdbcTemplate = null;
		if (StringUtils.isNotBlank(clientId)) {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
			jdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
		}
		return quartzDao.updateSchedulerJobInfo(quartzSchedulerJobInfo, jdbcTemplate);
	}

	@Override
	public long addSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo,String clientId) throws AnvizentCorewsException {
		JdbcTemplate jdbcTemplate = null;
		if (StringUtils.isNotBlank(clientId)) {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
			jdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
		}
		return quartzDao.addSchedulerTriggerInfo(quartzSchedulerTriggerInfo, jdbcTemplate);
	}

	@Override
	public int updateSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo, String clientId) throws AnvizentCorewsException {
		JdbcTemplate jdbcTemplate = null;
		if (StringUtils.isNotBlank(clientId)) {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
			jdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
		}
		return quartzDao.updateSchedulerTriggerInfo(quartzSchedulerTriggerInfo, jdbcTemplate);
	}

	@Override
	public List<QuartzSchedulerJobInfo> getScheduledJobsInfo(SchedulerFilterJobDetails schedulerFilterJobDetails) {
		return quartzDao.getScheduledJobsInfo(schedulerFilterJobDetails, null);
	}
	
	@Override
	public SchedulerMaster getSchedulerServerMasterById(long id) {
		return commonDao.getSchedulerMasterById(id);
	}
	
	@Override
	public SchedulerSlave getServerSlaveById(long id) {
		return commonDao.getServerSlaveById(id);
	}
	
	@Override
	public List<Schedule> getPackagesForSchedulingByClientIdWithCronExpression(long clientId) throws AnvizentCorewsException {
		ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
		return scheduleDao.getPackagesForSchedulingByClientIdWithCronExpression(clientId +"", clientJdbcInstance.getClientAppdbJdbcTemplate());
	}

	@Override
	public List<PackageExecution> getScheduledPackagesForExecution(long clientId)  throws AnvizentCorewsException {
		ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
		return packageDao.getScheduledPackagesForExecution(clientJdbcInstance.getClientAppdbJdbcTemplate());
	}
	@Override
	public int updateDatesForOnceRecurrence(Schedule schedule, String clientId) throws AnvizentCorewsException {
		ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
		return packageDao.updateDatesForOnceRecurrence(schedule,clientJdbcInstance.getClientAppdbJdbcTemplate());
	}
	
	@Override
	public int saveSchedulerSourceUploadQueue(SchedulerMaster master, List<PackageExecution> uploadQ)
			throws SqlNotFoundException {
		return quartzDao.saveSchedulerSourceUploadQueue(master, uploadQ);
	}
	@Override
	public List<PackageExecution> getStoredUploadQueueList(long masterId) throws SqlNotFoundException, AnvizentCorewsException {
		// TODO Auto-generated method stub
		List<PackageExecution> jobsList = quartzDao.getStoredUploadQueueList(masterId);
		for (PackageExecution packageExecution : jobsList) {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(packageExecution.getClientId()));
			Schedule packageSchedule = scheduleDao.getScheduledPackageInfo(packageExecution, clientJdbcInstance.getClientAppdbJdbcTemplate());
			packageExecution.setUserId(packageSchedule.getUserId());
			packageExecution.setPackageId(packageSchedule.getPackageId());
			packageExecution.setUserPackage(packageSchedule.getUserPackage());
			packageExecution.setScheduleId(packageSchedule.getScheduleId());
		}
		return jobsList;
	}

	@Override
	public Map<String, String> getJobExecutionLimitByClient(String clientId) throws AnvizentCorewsException {
		ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId+""));
		return masterDao.getJobExecutionLimitByClient(clientId, clientJdbcInstance.getClientAppdbJdbcTemplate());
	}
}
