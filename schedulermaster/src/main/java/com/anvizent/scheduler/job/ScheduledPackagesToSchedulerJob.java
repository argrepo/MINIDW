package com.anvizent.scheduler.job;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.client.data.to.csv.path.converter.constants.Constants;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.scheduler.commonUtil.MasterSchedulerQueueUtil;
import com.anvizent.scheduler.master.QuartzMasterSchedulerListener;
import com.anvizent.scheduler.service.MasterService;
import com.datamodel.anvizent.helper.MasterSchedulerConstants;
import com.datamodel.anvizent.helper.minidw.MinidwJobState;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.Schedule;

public class ScheduledPackagesToSchedulerJob implements Job {

	MasterService masterService;
	protected Scheduler scheduler;
	CloudClient cloudClient;
	MasterSchedulerQueueUtil schedulerSlavesUtil;
	
	private Logger logger = LoggerFactory.getLogger(ScheduledPackagesToSchedulerJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//JobKey jobKey = context.getJobDetail().getKey();
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		

		List<Schedule> packageScheduleDetailsForClientID = null;
		try {
			cloudClient = (CloudClient) jobDataMap.get("clientCloud");
			masterService = (MasterService) jobDataMap.get("masterService");
			scheduler = (Scheduler) jobDataMap.get("scheduler");
			schedulerSlavesUtil = (MasterSchedulerQueueUtil) jobDataMap.get("schedulerSlavesUtil");
			
			packageScheduleDetailsForClientID = masterService
					.getPackagesForSchedulingByClientIdWithCronExpression(cloudClient.getId());
			for (Schedule schd : packageScheduleDetailsForClientID) {
				try {
					addSchedule(schd);
				} catch (SchedulerException | ParseException e) {
					logger.error("Error while adding new packages to scheduler",e);
				}
			}
		} catch (Throwable e) {
			logger.error("Error while fetching the packages List for uploading ",e);
		}
	}

	private void addSchedule(Schedule packageSchedule) throws SchedulerException, ParseException {

		String packageID = packageSchedule.getPackageId().toString();
		String dlID = packageSchedule.getDlId().toString();

		String startDateAsString = packageSchedule.getScheduleStartDate();
		String triggerEndTimeAsString = packageSchedule.getScheduleEndDate();

		String keyString = null;
		if (packageSchedule.getUserPackage().getIsStandard()) {
			keyString = "D"+dlID+"_"+packageSchedule.getUserPackage().getPackageName();
		} else {
			keyString = "P"+packageID;
		}
		JobKey schedulerPackageAdderJobKey = new JobKey(keyString,MasterSchedulerConstants.SCHEDULED_PACKAGES.name()+"_"+cloudClient.getId());

		String triggerKeyName = MinidwServiceUtil.framePackageTriggerKeyName(startDateAsString, triggerEndTimeAsString, packageSchedule.getRecurrencePattern()
				, packageSchedule.getCronExpression(), packageSchedule.getTimeZone(), packageID, dlID);
		
		TriggerKey schedulerPackageAdderTriggerKey = new TriggerKey(triggerKeyName,
				MasterSchedulerConstants.SCHEDULED_PACKAGES.name());
		
		Date startDate = Constants.getTime(startDateAsString, (String) packageSchedule.getTimeZone());
		Date triggerEndTime = Constants.getTime(triggerEndTimeAsString, (String) packageSchedule.getTimeZone());

		
		if ( triggerEndTime != null && triggerEndTime.compareTo(new Date()) < 0 ) {
			return;
		}

		if (scheduler.checkExists(schedulerPackageAdderJobKey)) {
			try {
				if (!packageSchedule.getUserPackage().getIsActive()) {
					updateJobDetails(MinidwJobState.ARCHIVED, schedulerPackageAdderJobKey);
				} else if ( packageSchedule.getUserPackage().getIsAdvanced()) {
					updateJobDetails(MinidwJobState.MAPPING_PENDING, schedulerPackageAdderJobKey);
				} else if (packageSchedule.getScheduleType().equals(MinidwJobState.UNSCHEDULED.name())) {
					updateJobDetails(MinidwJobState.UNSCHEDULED, schedulerPackageAdderJobKey);
				} else if (!scheduler.getTriggersOfJob(schedulerPackageAdderJobKey).get(0).getKey()
						.equals(schedulerPackageAdderTriggerKey)) {
					updateJobDetails(MinidwJobState.RESCHEDULED, schedulerPackageAdderJobKey);
				} else {
					return;
				}
			} catch (Exception e) {
				logger.info("unable to update job status ", e);
			}
			scheduler.deleteJob(schedulerPackageAdderJobKey);
		}
		
		if (!packageSchedule.getUserPackage().getIsActive() || ( StringUtils.isNotBlank(packageSchedule.getScheduleType()) && packageSchedule.getScheduleType().equals(MinidwJobState.UNSCHEDULED.name())) || packageSchedule.getUserPackage().getIsAdvanced()) {
			return;
		}



		JobDataMap newJobDataMap = getJobDataMap(packageSchedule);

		JobDetail schedulerPackageAdder = JobBuilder.newJob(SourcesPackagesToUploadQueueJob.class)
				.withIdentity(schedulerPackageAdderJobKey).setJobData(newJobDataMap).build();

		Trigger schedulerPackageAdderTrigger = null;

		if (packageSchedule.getRecurrencePattern().equals("once")) {
			Schedule onceSchedule = new Schedule();
			startDate = DateBuilder.nextGivenMinuteDate(new Date(), 1);
			String[] dateTimeZones = TimeZoneDateHelper.getFormattedDateString(startDate).split(" ");
			onceSchedule.setScheduleStartDate(dateTimeZones[0]);
			onceSchedule.setScheduleStartTime(dateTimeZones[1]);
			onceSchedule.setTimeZone(TimeZone.getDefault().getID());
			onceSchedule.setScheduleId(packageSchedule.getScheduleId());
			try {
				masterService.updateDatesForOnceRecurrence(onceSchedule, cloudClient.getId()+"");
			} catch (Exception e) {
				logger.error("",e);
			}
			schedulerPackageAdderTrigger = TriggerBuilder.newTrigger().withIdentity(schedulerPackageAdderTriggerKey)
					.startAt(startDate).build();
		} else {
			schedulerPackageAdderTrigger = TriggerBuilder.newTrigger().withIdentity(schedulerPackageAdderTriggerKey)
					//.startAt(startDate)
					.endAt(triggerEndTime)
					.withSchedule(CronScheduleBuilder.cronSchedule(packageSchedule.getCronExpression())
							//.withMisfireHandlingInstructionIgnoreMisfires() // running since starting date
							//.withMisfireHandlingInstructionDoNothing()  // wont run at least once 
							.withMisfireHandlingInstructionFireAndProceed() // fires the 1st occurrence and ignores others
							.inTimeZone(TimeZone.getTimeZone(packageSchedule.getTimeZone()))).build();
		}

		try {
			scheduler.scheduleJob(schedulerPackageAdder, schedulerPackageAdderTrigger);
		} catch (Exception e) {
			if (!(e.getMessage().trim().contains("Unable to store Job"))) {
				throw new JobExecutionException(e.getMessage(), e);
			} else {
				scheduler.unscheduleJob(schedulerPackageAdderTriggerKey);
			}
		}
	}
	
	void updateJobDetails(MinidwJobState state, JobKey jobKey) throws Exception {
		QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
		quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(QuartzMasterSchedulerListener.schedulerId));
		quartzSchedulerJobInfo.setJobId(QuartzMasterSchedulerListener.jobIds.get(jobKey));
		quartzSchedulerJobInfo.setNextFireTime(null);
		quartzSchedulerJobInfo.setStatus(state.name());
		masterService.updateSchedulerJobInfo(quartzSchedulerJobInfo, cloudClient.getId()+"");
	}

	private JobDataMap getJobDataMap(Schedule packageSchedule) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("cloudClient", cloudClient);
		jobDataMap.put(com.datamodel.anvizent.helper.minidw.Constants.Config.HEADER_CLIENT_ID, cloudClient.getId()+"");
		
		jobDataMap.put("scheduler", scheduler);
		jobDataMap.put("packageSchedule", packageSchedule);
		jobDataMap.put("schedulerSlavesUtil", schedulerSlavesUtil);
		return jobDataMap;
	}
	

}
