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

import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.scheduler.service.MasterService;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.helper.minidw.MinidwJobState;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerTriggerInfo;

public class ILRunnerTriggerListener implements TriggerListener {

	public static final String LISTENER_NAME = "dummyJobListenerName";
	private Logger logger = LoggerFactory.getLogger(ILRunnerTriggerListener.class);
	private String description;
	private long schedulerId = QuartzMasterSchedulerListener.schedulerId;
	private Map<JobKey, Long> jobIds = QuartzMasterSchedulerListener.jobIds;
	private Map<JobKey, Long> triggerIds = new HashMap<>();
	private MasterService masterService;
	private Scheduler scheduler;
	
	public ILRunnerTriggerListener(MasterService masterService,String name, String description,Scheduler scheduler ) {
		this.masterService = masterService;
		this.description = description;
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
		logger.info("Package trigger execution " + trigger.getJobKey());
		try {
			logger.info("Current Running thread Count " + scheduler.getCurrentlyExecutingJobs().size());
		} catch (SchedulerException e1) {
			logger.error("Error",e1);
		}
		boolean isTriggerFound = false;
		try {
			JobKey jobKey = trigger.getJobKey();
			Date nextFireTime = trigger.getNextFireTime();
			QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo = new QuartzSchedulerTriggerInfo();

			QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
			quartzSchedulerJobInfo.setJobId(jobIds.get(jobKey));
			quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(schedulerId));
			quartzSchedulerJobInfo.setJobDescription(description);
			quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			quartzSchedulerJobInfo.setStatus(MinidwJobState.RUNNING.toString());

			quartzSchedulerTriggerInfo.setQuartzSchedulerJobInfo(quartzSchedulerJobInfo);
			quartzSchedulerTriggerInfo.setDescription(description);
			quartzSchedulerTriggerInfo.setFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			quartzSchedulerTriggerInfo.setStartTime(TimeZoneDateHelper.getFormattedDateString());

			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			String clientId = (String) jobDataMap.get(Constants.Config.HEADER_CLIENT_ID);

			if (triggerIds.containsKey(jobKey)) {
				isTriggerFound = true;
				quartzSchedulerJobInfo.setStatus(MinidwJobState.READY.toString());
				quartzSchedulerTriggerInfo.setStatus(MinidwJobState.IGNORED.toString());
				quartzSchedulerTriggerInfo.setEndTime(TimeZoneDateHelper.getFormattedDateString());
				quartzSchedulerTriggerInfo.setDescription("Already a job executing for the same");
				logger.warn("Previous " + jobKey + " job still running");
			} else {
				quartzSchedulerTriggerInfo.setEndTime(null);
				quartzSchedulerTriggerInfo.setStatus(MinidwJobState.READY.toString());
			}

			long triggerId = masterService.addSchedulerTriggerInfo(quartzSchedulerTriggerInfo, clientId);
			if (!isTriggerFound) {
				getTriggerIds().put(jobKey, triggerId);
			}
		} catch (Exception e) {
			logger.error("error in execution method", e);
		}
		return isTriggerFound;
	}


	@Override
	public void triggerMisfired(Trigger trigger) {
		logger.error("Package Jobs: Trigger misfired " + trigger.getJobKey() + " Time -> " + trigger.getPreviousFireTime());
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
		} catch (Exception e) {
			logger.error("Misfire updation failed ",e);
		}
	}


	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
		JobKey jobKey = trigger.getJobKey();
		if ( getTriggerIds().containsKey(jobKey)) {
			try {

				JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
				String clientId = (String)jobDataMap.get(Constants.Config.HEADER_CLIENT_ID);
				Date nextFireTime = trigger.getNextFireTime();
				QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo = new QuartzSchedulerTriggerInfo();
				quartzSchedulerTriggerInfo.setTriggerId(getTriggerIds().get(jobKey));

				QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
				quartzSchedulerJobInfo.setJobId(jobIds.get(jobKey));
				quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(schedulerId));
				if ( nextFireTime != null ) {
	            	quartzSchedulerJobInfo.setStatus(MinidwJobState.READY.toString());
	            } else {
	            	quartzSchedulerJobInfo.setStatus(MinidwJobState.COMPLETED.toString());
	            }
				quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
				
				quartzSchedulerTriggerInfo.setQuartzSchedulerJobInfo(quartzSchedulerJobInfo);
				quartzSchedulerTriggerInfo.setStatus(MinidwJobState.COMPLETED.toString());
				quartzSchedulerTriggerInfo.setEndTime(TimeZoneDateHelper.getFormattedDateString());
				try {
					masterService.updateSchedulerTriggerInfo(quartzSchedulerTriggerInfo, clientId);
				} catch (Exception e) {
					logger.error("Updation failed ",e);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
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