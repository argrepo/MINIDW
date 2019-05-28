package com.anvizent.scheduler.master;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.scheduler.service.MasterService;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.helper.minidw.MinidwJobState;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;

public class OtherJobsTriggerListener implements TriggerListener {

	public static final String LISTENER_NAME = "otherJobsTriggerListener";
	private Logger logger = LoggerFactory.getLogger(OtherJobsTriggerListener.class);
	private long schedulerId = QuartzMasterSchedulerListener.schedulerId;
	private Map<JobKey, Long> jobIds = QuartzMasterSchedulerListener.jobIds;
	private Map<JobKey, Long> triggerIds = new HashMap<>();
	private MasterService masterService;
	private Scheduler scheduler;
	
	private Map<JobKey, Long> stillRunningJobsList = new HashMap<>();
	
	public OtherJobsTriggerListener(MasterService masterService,String name, Scheduler scheduler ) {
		this.masterService = masterService;
		this.scheduler = scheduler;
	}

	
	@Override
	public String getName() {
		return LISTENER_NAME; // must return a name
	}


	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		
	}


	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		boolean isTriggerFound = false;
		try {

			JobKey jobKey = trigger.getJobKey();
			Date nextFireTime = trigger.getNextFireTime();
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			String clientId = (String)jobDataMap.get(Constants.Config.HEADER_CLIENT_ID);
			
			QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
			quartzSchedulerJobInfo.setJobId(jobIds.get(jobKey));
			quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			quartzSchedulerJobInfo.setStatus(MinidwJobState.RUNNING.toString());
			
			if (getTriggerIds().containsKey(jobKey)) {
				if (stillRunningJobsList.get(jobKey) != null && stillRunningJobsList.get(jobKey) >= 3) {
					logger.warn("Previous " + jobKey + " running job is overriden");
					stillRunningJobsList.remove(jobKey);
					getTriggerIds().remove(jobKey);
				} else {
					isTriggerFound = true;
					quartzSchedulerJobInfo.setStatus(MinidwJobState.READY.toString());
					logger.warn("Previous " + jobKey + " job still running");
					Long runningCount = stillRunningJobsList.get(jobKey);
					stillRunningJobsList.put(jobKey, runningCount != null ? runningCount + 1 : 1);
				}
			} else {
				if (stillRunningJobsList.get(jobKey) != null) {
					stillRunningJobsList.remove(jobKey);
				}
			}
			try {
				masterService.updateSchedulerJobInfo(quartzSchedulerJobInfo, clientId);
			} catch (Exception e) {
				logger.error(jobKey.toString() +" job updation failed", e );
			}
			if ( !isTriggerFound ) {
				getTriggerIds().put(jobKey, 0L);
			}
		
		} catch (Throwable e) {
			logger.error(MinidwServiceUtil.getErrorMessageString(e),e);
		}
		return isTriggerFound;
	}


	@Override
	public void triggerMisfired(Trigger trigger) {
		logger.warn("Other Jobs: Trigger misfired " + trigger.getJobKey() + " Time -> " + trigger.getPreviousFireTime());
		QuartzSchedulerJobInfo quartzSchedulerInfo = new QuartzSchedulerJobInfo();
		try {
			logger.info("Current Running thread Count " + scheduler.getCurrentlyExecutingJobs().size());
		} catch (SchedulerException e1) {
			logger.error("Error",e1);
		}
		try {
			
        	JobKey jobKey = trigger.getJobKey();
			String clientId = scheduler.getJobDetail(jobKey).getJobDataMap().get(Constants.Config.HEADER_CLIENT_ID) + "";
			quartzSchedulerInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(schedulerId));
			Long jobId = jobIds.get(jobKey);
			if ( jobId == null) {
				Thread.sleep(1000);
				jobId = jobIds.get(jobKey);
				logger.info("job id for "+jobKey+" : " + jobId );
			}
			quartzSchedulerInfo.setJobId(jobId);
			Date nextFireTime = trigger.getFireTimeAfter(new Date());
			logger.info("Trigger misfired next date --> " + jobKey + " - " + TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			quartzSchedulerInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			MinidwJobState state = MinidwJobState.READY;
			quartzSchedulerInfo.setStatus(state.name());
			masterService.updateSchedulerJobInfo(quartzSchedulerInfo, clientId);
		} catch (Throwable e) {
			logger.error("Updation failed " + trigger.getKey() + " JobKey " +trigger.getJobKey() , e );
		}
	}


	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
		JobKey jobKey = trigger.getJobKey();
		if ( getTriggerIds().containsKey(jobKey)) {
			try {
				Date nextFireTime = trigger.getNextFireTime();

				JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
				String clientId = (String) jobDataMap.get(Constants.Config.HEADER_CLIENT_ID);

				QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
				quartzSchedulerJobInfo.setJobId(jobIds.get(jobKey));
				quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(schedulerId));
				if (nextFireTime != null) {
					quartzSchedulerJobInfo.setStatus(MinidwJobState.READY.toString());
				} else {
					quartzSchedulerJobInfo.setStatus(MinidwJobState.COMPLETED.toString());
				}
				quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
				masterService.updateSchedulerJobInfo(quartzSchedulerJobInfo, clientId);
			} catch (Throwable e) {
				logger.error("",e);
			}finally {
				getTriggerIds().remove(jobKey);
			}
			
		}
	}
	
	public Map<JobKey, Long> getTriggerIds() {
		return triggerIds;
	}


	public void setTriggerIds(Map<JobKey, Long> triggerIds) {
		this.triggerIds = triggerIds;
	}

	
}