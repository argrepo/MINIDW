
/**
 * 
 */
package com.anvizent.schedulers.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
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

	long addSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo);

	int updateSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo);

	long addSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo);

	int updateSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo);

	List<QuartzSchedulerJobInfo> getScheduledJobsInfo(SchedulerFilterJobDetails schedulerFilterJobDetails,String clientId) throws AnvizentCorewsException;
	
	SchedulerMaster getSchedulerServerMasterById(long id);
	
	SchedulerSlave getServerSlaveById(int id);
	
	List<Schedule> getPackagesForSchedulingByClientIdWithCronExpression(long clientId)  throws AnvizentCorewsException ;
	
	List<QuartzSchedulerJobInfo> getTriggeredInfoByID(long jobId, String timeZone, String clientId) throws AnvizentCorewsException;
	
}
