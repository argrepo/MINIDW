
/**
 * 
 */
package com.anvizent.scheduler.service;

import java.util.List;
import java.util.Map;

import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerTriggerInfo;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;

public interface MasterService {
	
	long startSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo);

	int updateSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo);

	long addSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo, String clientId) throws AnvizentCorewsException;

	int updateSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo, String clientId) throws AnvizentCorewsException;

	long addSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo, String clientId) throws AnvizentCorewsException;

	int updateSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo, String clientId) throws AnvizentCorewsException;

	List<QuartzSchedulerJobInfo> getScheduledJobsInfo(SchedulerFilterJobDetails schedulerFilterJobDetails) throws AnvizentCorewsException;
	
	SchedulerMaster getSchedulerServerMasterById(long id);
	
	SchedulerSlave getServerSlaveById(long id);
	
	List<Schedule> getPackagesForSchedulingByClientIdWithCronExpression(long clientId)  throws AnvizentCorewsException ;
	
	public List<PackageExecution> getScheduledPackagesForExecution(long clientId) throws AnvizentCorewsException;
	
	int updateDatesForOnceRecurrence(Schedule schedule,String clientId) throws AnvizentCorewsException ;
	
	int saveSchedulerSourceUploadQueue(SchedulerMaster master, List<PackageExecution> uploadQ) throws SqlNotFoundException; 
	
	List<PackageExecution> getStoredUploadQueueList(long masterId) throws SqlNotFoundException,AnvizentCorewsException;

	Map<String, String> getJobExecutionLimitByClient(String clientId) throws AnvizentCorewsException; 
	
}
