package com.anvizent.scheduler.master;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.listeners.SchedulerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.scheduler.service.MasterService;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.helper.MasterSchedulerConstants;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.helper.minidw.MinidwJobState;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;

public class QuartzMasterSchedulerListener extends SchedulerListenerSupport {

	final Logger LOGGER = LoggerFactory.getLogger(QuartzMasterSchedulerListener.class);
	private String name, description;
	private long masterId;
	public static long schedulerId ;
	private Scheduler scheduler;
	public static Map<JobKey, Long> jobIds = new HashMap<>();
	private MasterService masterService;
	public QuartzMasterSchedulerListener(MasterService masterService,String name, String description,Long masterId, Scheduler scheduler) {
		this.masterService = masterService;		
		this.name = name;
		this.description = description;
		this.masterId = masterId;
		this.scheduler = scheduler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void jobAdded(JobDetail jobDetail) {
		// TODO Auto-generated method stub
		// 
		try {
			
			String jobKeyName = jobDetail.getKey().getName();
			String jobKeyGroup = jobDetail.getKey().getGroup();
			List<Trigger> trigger = (List<Trigger>) scheduler.getTriggersOfJob(jobDetail.getKey());
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			String clientId = (String)jobDataMap.get(Constants.Config.HEADER_CLIENT_ID);
			Trigger triggers = trigger.get(0);
			Date nextFireTime = triggers.getNextFireTime();
			
			QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
			//quartzSchedulerJobInfo.setMasterId(masterId);
			quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(getSchedulerId()));
			quartzSchedulerJobInfo.setJobDescription(description);
			quartzSchedulerJobInfo.setJobStartTime(TimeZoneDateHelper.getFormattedDateString(trigger.get(0).getStartTime()));
			quartzSchedulerJobInfo.setJobEndTime(TimeZoneDateHelper.getFormattedDateString(trigger.get(0).getEndTime()));
			quartzSchedulerJobInfo.setJobKeyName(jobKeyName);
			quartzSchedulerJobInfo.setGroupName(jobKeyGroup);
			quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			
			MinidwJobState state = MinidwJobState.READY;
			String cronExpression = null;
			quartzSchedulerJobInfo.setStatus(state.name());
			if ( trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				cronExpression = cronTrigger.getCronExpression();
			}
			quartzSchedulerJobInfo.setCronExpression(cronExpression);
			
			getJobIds().put(jobDetail.getKey(), masterService.addSchedulerJobInfo(quartzSchedulerJobInfo, clientId));
			
		} catch (Exception e) {
			LOGGER.error("error",e);
		}
		
		// use scheduler object to fetch next firetime 
			
	}

	@SuppressWarnings("unchecked")
	@Override
	public void jobPaused(JobKey jobKey) {
		QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
		try {
			quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(getSchedulerId()));
			JobDataMap jobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
			String clientId = (String)jobDataMap.get(Constants.Config.HEADER_CLIENT_ID);
			quartzSchedulerJobInfo.setJobId(getJobIds().get(jobKey));
			List<Trigger> trigger = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
			Trigger triggers = trigger.get(0);
			Date nextFireTime = triggers.getNextFireTime();
			quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			MinidwJobState state = MinidwJobState.PAUSE;
			quartzSchedulerJobInfo.setStatus(state.name());
			masterService.updateSchedulerJobInfo(quartzSchedulerJobInfo, clientId);
		} catch (SchedulerException | AnvizentCorewsException e) {
			// TODO Auto-generated catch block
			LOGGER.error("error",e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void jobResumed(JobKey jobKey) {
		QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
		try {
			JobDataMap jobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
			String clientId = (String)jobDataMap.get(Constants.Config.HEADER_CLIENT_ID);
			quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(getSchedulerId()));
			quartzSchedulerJobInfo.setJobId(getJobIds().get(jobKey));
			List<Trigger> trigger = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
			Trigger triggers = trigger.get(0);
			Date nextFireTime = triggers.getNextFireTime();
			quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			MinidwJobState state = MinidwJobState.READY;
			quartzSchedulerJobInfo.setStatus(state.name());
			masterService.updateSchedulerJobInfo(quartzSchedulerJobInfo, clientId);
		} catch (Exception e) {
			LOGGER.error("Updation failed ",e);
		}
	}
	
	@Override
	public void jobUnscheduled(TriggerKey triggerKey) {
		QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
		try {
			JobKey jobKey = scheduler.getTrigger(triggerKey).getJobKey();
			JobDataMap jobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
			String clientId = (String)jobDataMap.get(Constants.Config.HEADER_CLIENT_ID);
			quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(getSchedulerId()));
			quartzSchedulerJobInfo.setJobId(getJobIds().get(jobKey));
			MinidwJobState state = MinidwJobState.UNSCHEDULED;
			quartzSchedulerJobInfo.setStatus(state.name());
			masterService.updateSchedulerJobInfo(quartzSchedulerJobInfo, clientId);
		} catch (SchedulerException | AnvizentCorewsException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Updation failed ",e);
		}
		
		super.jobUnscheduled(triggerKey);
	}

	@Override
	public void schedulerStarted() {
		QuartzSchedulerInfo quartzSchedulerInfo = new QuartzSchedulerInfo();
		quartzSchedulerInfo.setName(name);
		quartzSchedulerInfo.setDescription(description);
		quartzSchedulerInfo.setTimezone(TimeZone.getDefault().getID());
		quartzSchedulerInfo.setMasterId(masterId);
		try {
			quartzSchedulerInfo.setStartTime(TimeZoneDateHelper.getFormattedDateString());
			schedulerId = masterService.startSchedulerInfo(quartzSchedulerInfo);
		} catch (Exception e) {
			LOGGER.error("Insertion failed for schduler instance;", e);
		}
		LOGGER.info("Schduler started");
		
	}

	@Override
	public void schedulerStarting() {
		LOGGER.info("Schduler starting");
	}

	@Override
	public void schedulerShutdown() {
		QuartzSchedulerInfo quartzSchedulerInfo = new QuartzSchedulerInfo(getSchedulerId());
		quartzSchedulerInfo.setShutdownTime(TimeZoneDateHelper.getFormattedDateString());
		masterService.updateSchedulerInfo(quartzSchedulerInfo);
		LOGGER.info("Schduler Shutdown");
	}

	
	public long getSchedulerId() {
		return schedulerId;
	}


	
	public Map<JobKey, Long> getJobIds() {
		return jobIds;
	}

	@Override
	public void jobDeleted(JobKey jobKey) {
		QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
		try {
			String clientId = "";
			if (jobKey.getGroup().startsWith(MasterSchedulerConstants.SCHEDULED_PACKAGES.name())) {
				clientId = StringUtils.substringAfterLast(jobKey.getGroup(), "_");
				if (StringUtils.isNotBlank(clientId)) {
					quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(getSchedulerId()));
					quartzSchedulerJobInfo.setJobId(getJobIds().get(jobKey));
					MinidwJobState state = MinidwJobState.RESCHEDULED;
					quartzSchedulerJobInfo.setStatus(state.name());
					masterService.updateSchedulerJobInfo(quartzSchedulerJobInfo, clientId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//super.jobDeleted(jobKey);
	}
	
}
