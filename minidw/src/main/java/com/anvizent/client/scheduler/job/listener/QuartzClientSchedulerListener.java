package com.anvizent.client.scheduler.job.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.listeners.SchedulerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.client.scheduler.constant.Constant;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.MasterSchedulerConstants;
import com.datamodel.anvizent.helper.minidw.MinidwJobState;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;

public class QuartzClientSchedulerListener extends SchedulerListenerSupport {

	final Logger LOGGER = LoggerFactory.getLogger(QuartzClientSchedulerListener.class);
	private RestTemplateUtilities restTemplateUtilities;
	private String name, description;
	public static long schedulerId;
	private Scheduler scheduler;
	public static Map<JobKey, Long> jobIds = new HashMap<>();
	private String adminUserId,adminClientId;
	CustomRequest customRequest;
	

	public QuartzClientSchedulerListener(RestTemplateUtilities restTemplateUtilities, String name, String description,
			String adminUserId,
			String adminClientId,
			Scheduler scheduler) {
		this.restTemplateUtilities = restTemplateUtilities;
		this.name = name;
		this.description = description;
		this.scheduler = scheduler;
		try {
			this.adminUserId = EncryptionServiceImpl.getInstance().encrypt(adminUserId);
			this.adminClientId = EncryptionServiceImpl.getInstance().encrypt(adminClientId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void jobAdded(JobDetail jobDetail) {
		// TODO Auto-generated method stub
		//
		try {

			String jobKeyName = jobDetail.getKey().getName();
			String jobKeyGroup = jobDetail.getKey().getGroup();
			String clientId = jobDetail.getJobDataMap().get(Constant.General.CLIENT_ID) + "";
			List<Trigger> trigger = (List<Trigger>) scheduler.getTriggersOfJob(jobDetail.getKey());
			Trigger triggers = trigger.get(0);
			Date nextFireTime = triggers.getNextFireTime();

			QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
			quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(getSchedulerId()));
			quartzSchedulerJobInfo.setJobDescription(jobDetail.getDescription());
			quartzSchedulerJobInfo
					.setJobStartTime( TimeZoneDateHelper.getFormattedDateString(trigger.get(0).getStartTime()) );
			quartzSchedulerJobInfo.setJobEndTime(TimeZoneDateHelper.getFormattedDateString(trigger.get(0).getEndTime()));
			quartzSchedulerJobInfo.setJobKeyName(jobKeyName);
			quartzSchedulerJobInfo.setGroupName(jobKeyGroup);
			quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));

			MinidwJobState state = MinidwJobState.READY;
			String cronExpression = null;
			quartzSchedulerJobInfo.setStatus(state.name());
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				cronExpression = cronTrigger.getCronExpression();
			}
			quartzSchedulerJobInfo.setCronExpression(cronExpression);

			DataResponse dataResponse = restTemplateUtilities.postRestObject(getCustomRequest(clientId),
					"/schedule/addSchedulerJobInfo", quartzSchedulerJobInfo, clientId);
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getObject() != null) {
					getJobIds().put(jobDetail.getKey(), Long.valueOf(dataResponse.getObject().toString()));
				}

			}

		} catch (Throwable e) {
			LOGGER.error("error", e);
		}

		// use scheduler object to fetch next firetime

	}

	@SuppressWarnings("unchecked")
	@Override
	public void jobPaused(JobKey jobKey) {
		QuartzSchedulerJobInfo quartzSchedulerInfo = new QuartzSchedulerJobInfo();
		LOGGER.info(jobKey + " jobPaused");
		try {
			String clientId = scheduler.getJobDetail(jobKey).getJobDataMap().get(Constant.General.CLIENT_ID) + "";
			quartzSchedulerInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(getSchedulerId()));
			quartzSchedulerInfo.setJobId(getJobIds().get(jobKey));
			List<Trigger> trigger = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
			Trigger triggers = trigger.get(0);
			Date nextFireTime = triggers.getNextFireTime();
			quartzSchedulerInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			MinidwJobState state = MinidwJobState.PAUSE;
			quartzSchedulerInfo.setStatus(state.name());
			restTemplateUtilities.postRestObject(getCustomRequest(clientId), "/schedule/updateSchedulerJobInfo",
					quartzSchedulerInfo, clientId);
		} catch (Throwable e) {
			LOGGER.error("error", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void jobResumed(JobKey jobKey) {
		LOGGER.info(jobKey + " jobResumed");
		QuartzSchedulerJobInfo quartzSchedulerInfo = new QuartzSchedulerJobInfo();
		try {
			String clientId = scheduler.getJobDetail(jobKey).getJobDataMap().get(Constant.General.CLIENT_ID) + "";
			quartzSchedulerInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(getSchedulerId()));
			quartzSchedulerInfo.setJobId(getJobIds().get(jobKey));
			List<Trigger> trigger = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
			Trigger triggers = trigger.get(0);
			Date nextFireTime = triggers.getNextFireTime();
			quartzSchedulerInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			MinidwJobState state = MinidwJobState.READY;
			quartzSchedulerInfo.setStatus(state.name());
			restTemplateUtilities.postRestObject(getCustomRequest(clientId), "/schedule/updateSchedulerJobInfo",
					quartzSchedulerInfo, clientId);
		} catch (Throwable e) {
			LOGGER.error("error", e);
		}
	}
	
	@Override
	public void jobsPaused(String jobGroup) {
		super.jobsPaused(jobGroup);
		LOGGER.info("Group of jobs paused " + jobGroup);
	}
	
	@Override
	public void jobsResumed(String jobGroup) {
		super.jobsResumed(jobGroup);
		LOGGER.info("Group of jobs resumed " + jobGroup);
	}

	@Override
	public void schedulerStarted() {
		QuartzSchedulerInfo quartzSchedulerInfo = new QuartzSchedulerInfo();
		quartzSchedulerInfo.setName(name);
		quartzSchedulerInfo.setDescription(description);
		quartzSchedulerInfo.setTimezone(TimeZone.getDefault().getID());
		try {
			quartzSchedulerInfo.setStartTime(TimeZoneDateHelper.getFormattedDateString());
			DataResponse dataResponse = restTemplateUtilities.postRestObject(getCustomRequest(adminClientId),
					"/schedule/startSchedulerInfo", quartzSchedulerInfo, adminUserId);
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getObject() != null) {
					schedulerId = Long.valueOf(dataResponse.getObject().toString());
				} else {
					LOGGER.error("Insertion failed for schduler instance");
				}
			}
		} catch (Throwable e) {
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
		restTemplateUtilities.postRestObject(getCustomRequest(adminClientId), "/schedule/updateSchedulerInfo",
				quartzSchedulerInfo, adminUserId);
		LOGGER.info("Schduler Shutdown");
	}
	
	

	public long getSchedulerId() {
		return schedulerId;
	}

	public Map<JobKey, Long> getJobIds() {
		return jobIds;
	}

	@Override
	public void jobUnscheduled(TriggerKey triggerKey) {
		
		super.jobUnscheduled(triggerKey);
	}

	@Override
	public void jobDeleted(JobKey jobKey) {
		QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();

		try {
			String clientId = "";
			if (jobKey.getGroup().startsWith(MasterSchedulerConstants.SCHEDULED_PACKAGES.name())) {
				clientId = StringUtils.substringAfterLast(jobKey.getGroup(), "_");
				if (StringUtils.isNotBlank(clientId)) {
					clientId = EncryptionServiceImpl.getInstance().encrypt(clientId);
					quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(getSchedulerId()));
					quartzSchedulerJobInfo.setJobId(getJobIds().get(jobKey));
					quartzSchedulerJobInfo.setNextFireTime(null);
					MinidwJobState state = MinidwJobState.RESCHEDULED;
					quartzSchedulerJobInfo.setStatus(state.name());
					restTemplateUtilities.postRestObject(getCustomRequest(clientId), "/schedule/updateSchedulerJobInfo",
							quartzSchedulerJobInfo, clientId);
				}
			}
			
		} catch (Throwable e) {
			LOGGER.error("Error while deleting the job", e);
		}
		super.jobDeleted(jobKey);
	}
	

	@SuppressWarnings("deprecation")
	CustomRequest getCustomRequest(String clientId) {
		String plainClientId = "";
		try {
			plainClientId = EncryptionServiceImpl.getInstance().decrypt(clientId);
		} catch (Exception e) {
			LOGGER.error("Unable to decrypt client id");
		}
		return new CustomRequest(plainClientId, plainClientId, "", "hybrid");
	}

}
