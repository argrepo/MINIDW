package com.anvizent.client.scheduler.job;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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

import com.anvizent.client.scheduler.constant.Constant;
import com.anvizent.client.scheduler.job.listener.QuartzClientSchedulerListener;
import com.anvizent.client.scheduler.util.PropertiesPlaceHolder;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.processor.StandardPackageProcessor;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.MasterSchedulerConstants;
import com.datamodel.anvizent.helper.minidw.MinidwJobState;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HybridSourcePackageAdder implements Job {
	private Logger logger = LoggerFactory.getLogger(HybridSourcePackageAdder.class);
	private RestTemplateUtilities packageRestUtilities;
	private StandardPackageProcessor standardPackageProcessor;
	FileSettings fileSettings;
	S3BucketInfo s3BucketInfo;
	String plainClientId = null;
	CustomRequest customRequest ;
	String clientId = null;
	Scheduler scheduler;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			packageRestUtilities = (RestTemplateUtilities) jobDataMap.get(Constant.General.PACKAGE_REST_UTILITY);
			clientId = (String) jobDataMap.get(Constant.General.CLIENT_ID);
			scheduler = (Scheduler) jobDataMap.get(Constant.General.SCHEDULER);
			s3BucketInfo = (S3BucketInfo) jobDataMap.get(Constant.General.S3_BUCKET_INFO);
			fileSettings = (FileSettings) jobDataMap.get(Constant.General.FILE_SETTINGS);
			standardPackageProcessor = (StandardPackageProcessor) jobDataMap.get(Constant.General.STANDARD_PACKAGE_PROCESSOR);
			try {
				plainClientId = EncryptionServiceImpl.getInstance().decrypt(clientId);
			} catch (Exception e) {
				throw new JobExecutionException("Unable to decrypt client id " + clientId);
			}
			customRequest = new CustomRequest(plainClientId, plainClientId, "", "hybrid");
			addSchedules(clientId);
		} catch (Throwable e) {
			logger.error("",e);
		}

	}

	public void addSchedules(String clientID) throws Exception {
		List<Schedule> data = getCurrentSchedules(clientID);
		//logger.info( "Number of schedules to add: " + ((data == null) ? 0 : data.size()));
		if (data != null && data.size() >  0 ) {
			
			addSchedules(data, clientID);
		}
	}

	private void addSchedules(List<Schedule> data, String userID)
			throws JobExecutionException {
		try {
			for (Schedule packageSchedule : data) {
				//logger.info("In ILSchedulerAdder.");
				addSchedule(packageSchedule, userID);
			}
		} catch (SchedulerException | ParseException schedulerOrParseException) {
			throw new JobExecutionException(schedulerOrParseException.getMessage(), schedulerOrParseException);
		}

	}

	private void addSchedule(Schedule packageSchedule, String userID)
			throws SchedulerException, ParseException {
		//Schedule schedule = packageSchedule.getSchedule();

		//ClientServerScheduler clientServerScheduler = packageSchedule.getClientServerScheduler();
		//ILConnectionMapping iLConnectionMapping = packageSchedule.getIlConnectionMapping();

		String packageID = packageSchedule.getPackageId().toString();
		String dlID = packageSchedule.getDlId().toString();
		//Integer clientServerSchedulerID = clientServerScheduler.getId();
		//String connectionMappingId = iLConnectionMapping.getConnectionMappingId().toString();

		/*logger.info( "in ILSchedulerAdder,addSchedule method clientServerSchedulerID:" + clientServerSchedulerID);*/
		/*String startDateAsString = packageSchedule.getCurrentScheduleStartTime();
		Date startDate = Constant.getTime(startDateAsString, (String) packageSchedule.getTimeZone());
*/
		/*logger.info( "in ILSchedulerAdder, addSchedule method packageId: " + packageID + " clientServerSchedulerID: " + clientServerSchedulerID
				+ ", connectionMappingId: " + connectionMappingId);*/
		
		String startDateAsString = packageSchedule.getScheduleStartDate();
		Date startDate = Constant.getTime(startDateAsString, (String) packageSchedule.getTimeZone());
		String triggerEndTimeAsString = packageSchedule.getScheduleEndDate();
		Date triggerEndTime = Constant.getTime(triggerEndTimeAsString, packageSchedule.getTimeZone());
		
		if ( triggerEndTime != null && triggerEndTime.compareTo(new Date()) < 0 ) {
			return;
		}
		
		String keyString = null;
		if (packageSchedule.getUserPackage().getIsStandard()) {
			keyString = "D"+dlID+"_"+packageSchedule.getUserPackage().getPackageName();
		} else {
			keyString = "P"+packageID;
		}
		
		JobKey ilRunnerAdderJobKey = new JobKey(keyString, MasterSchedulerConstants.SCHEDULED_PACKAGES.name()+"_"+plainClientId);
		
		TriggerKey ilRunnerAdderTriggerKey = new TriggerKey(MinidwServiceUtil.framePackageTriggerKeyName(startDateAsString, triggerEndTimeAsString
				, packageSchedule.getRecurrencePattern(), packageSchedule.getCronExpression()
				, packageSchedule.getTimeZone(), packageID,dlID), MasterSchedulerConstants.SCHEDULED_PACKAGES.name());
	
		
		if (scheduler.checkExists(ilRunnerAdderJobKey)) {
			try {
				if ( !packageSchedule.getUserPackage().getIsActive()) {
					updateJobDetails(MinidwJobState.ARCHIVED, ilRunnerAdderJobKey);
				} else if ( packageSchedule.getUserPackage().getIsAdvanced()) {
					updateJobDetails(MinidwJobState.MAPPING_PENDING, ilRunnerAdderJobKey);
				} else if ( packageSchedule.getScheduleType().equals(MinidwJobState.UNSCHEDULED.name())) {
					updateJobDetails(MinidwJobState.UNSCHEDULED, ilRunnerAdderJobKey);
				} else if (!scheduler.getTriggersOfJob(ilRunnerAdderJobKey).get(0).getKey()
						.equals(ilRunnerAdderTriggerKey)) {
					updateJobDetails(MinidwJobState.RESCHEDULED, ilRunnerAdderJobKey);
				} else {
					return;
				}
			} catch (Exception e) {
				logger.info("unable to update job status ", e);
			}
			scheduler.deleteJob(ilRunnerAdderJobKey);
		}
		
		/* should not schedule the archived/unscheduled packages */
		if (!packageSchedule.getUserPackage().getIsActive() || packageSchedule.getScheduleType().equals(MinidwJobState.UNSCHEDULED.name()) || packageSchedule.getUserPackage().getIsAdvanced() ) {
			return;
		}

		JobDataMap newJobDataMap = getJobDataMap(userID, scheduler, packageSchedule);

		
		JobDetail ilRunnerAdder = JobBuilder.newJob(ILRunner.class).withIdentity(ilRunnerAdderJobKey).withDescription("Scheduling package "+packageSchedule.getPackageId()+" for uploading").setJobData(newJobDataMap).build();
		
		
		Trigger ilRunnerAdderTrigger = null; 
				
		if (packageSchedule.getRecurrencePattern().equals("once")) {
			Schedule onceSchedule = new Schedule();
			startDate = DateBuilder.nextGivenMinuteDate(new Date(), 1);
			String[] dateTimeZones = TimeZoneDateHelper.getFormattedDateString(startDate).split(" ");
			onceSchedule.setScheduleStartDate(dateTimeZones[0]);
			onceSchedule.setScheduleStartTime(dateTimeZones[1]);
			onceSchedule.setTimeZone(TimeZone.getDefault().getID());
			onceSchedule.setScheduleId(packageSchedule.getScheduleId());
			try {
				packageRestUtilities.postRestObject(customRequest, "/updateDatesForOnceRecurrence", onceSchedule, EncryptionServiceImpl.getInstance().encrypt(packageSchedule.getUserId()));
			} catch (Exception e) {
				logger.error("",e);
			}
			ilRunnerAdderTrigger = TriggerBuilder.newTrigger().
					withIdentity(ilRunnerAdderTriggerKey)
					.startAt(startDate)
					.build();
			
		}  else {
			ilRunnerAdderTrigger = TriggerBuilder.newTrigger().withIdentity(ilRunnerAdderTriggerKey)
					//.startAt(startDate)
					.endAt(triggerEndTime)
					.withSchedule(CronScheduleBuilder.cronSchedule(packageSchedule.getCronExpression())
							.withMisfireHandlingInstructionFireAndProceed()
							.inTimeZone(TimeZone.getTimeZone(packageSchedule.getTimeZone()))).build();
		}
		
		/*logger.info( "Adding schedule for ilRunner job packageId: " + packageID + ", clientServerSchedulerID: " + clientServerSchedulerID
				+ ", connectionMappingId: " + connectionMappingId);*/
		try {
			Date fd = scheduler.scheduleJob(ilRunnerAdder, ilRunnerAdderTrigger);
			logger.info("pkg id -- > " + packageID +"fd  --> " + fd);
		} catch (Exception e) {
			logger.error("",e);
			if (!(e.getMessage().trim().contains("Unable to store Job"))) {
				throw new JobExecutionException(e.getMessage(), e);
			} else {
				scheduler.unscheduleJob(ilRunnerAdderTriggerKey);
			}
		}
	} 
	
	void updateJobDetails(MinidwJobState state, JobKey jobKey) throws Exception {
		QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
		quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(QuartzClientSchedulerListener.schedulerId));
		quartzSchedulerJobInfo.setJobId(QuartzClientSchedulerListener.jobIds.get(jobKey));
		quartzSchedulerJobInfo.setNextFireTime(null);
		quartzSchedulerJobInfo.setStatus(state.name());
		packageRestUtilities.postRestObject(customRequest, "/schedule/updateSchedulerJobInfo",
				quartzSchedulerJobInfo, clientId);
	}

	@SuppressWarnings("unchecked")
	private List<Schedule> getCurrentSchedules(String clientID) throws JobExecutionException {
		
		try {
			
			DataResponse dataResponse = packageRestUtilities.getRestObject(customRequest,  "/schedule/getCronExpression",clientID);
			List<Schedule> clientDataList = null;
			if (dataResponse != null && dataResponse.getMessages().size() > 0 && dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("SUCCESS")) {
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<?> list = (List<Map<String, Object>>) dataResponse.getObject();
				clientDataList = mapper.convertValue(list, new TypeReference<List<Schedule>>() {
				});
				
				return clientDataList;
				
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new JobExecutionException(e.getMessage(), e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private List<String> getUserIDsList(PropertiesPlaceHolder propertiesPlaceHolder) throws JobExecutionException {
		try {
			String clientID = propertiesPlaceHolder.getAccessKey();
			DataResponse dataResponse = packageRestUtilities.getRestObject(customRequest, "/getClientUserIds", clientID);
			if (dataResponse != null && dataResponse.getMessages().size() > 0 && dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("SUCCESS")) {
				List<String> list = (List<String>)  dataResponse.getObject();
				return list;
			} else {
				logger.info("unable to fetch user ids");
				throw new JobExecutionException("unable to fetch user ids");
			}
		} catch (Exception e) {
			throw new JobExecutionException(e.getMessage(), e);
		}
	}

	private JobDataMap getJobDataMap(String userID, Scheduler scheduler, Schedule packageSchedule) {
		
		User user = new User();
		try {
			user.setUserId(EncryptionServiceImpl.getInstance().encrypt(packageSchedule.getUserId()));
		} catch (Exception e) {
			logger.error("",e);
		}
		user.setUserName(clientId);
		user.setClientId(plainClientId);
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(Constant.General.SCHEDULER, scheduler);
		jobDataMap.put(Constant.General.BROWSER_DETAILS, "");
		jobDataMap.put(Constant.General.PACKAGE_REST_UTILITY, packageRestUtilities);
		jobDataMap.put(Constant.General.S3_BUCKET_INFO, s3BucketInfo);
		jobDataMap.put(Constant.General.FILE_SETTINGS, fileSettings);
		jobDataMap.put(Constant.General.CSV_FOLDER_PATH, Constants.Temp.getTempFileDir());
		jobDataMap.put(Constant.General.USER_INFO,user);
		jobDataMap.put(Constant.General.PACKAGE_ID, packageSchedule.getPackageId().toString());
		jobDataMap.put(Constant.General.TIME_ZONE, packageSchedule.getTimeZone());
		jobDataMap.put(Constant.General.SCHUDULE_ID, packageSchedule.getScheduleId());
		jobDataMap.put(Constant.General.CLIENT_ID, clientId);
		jobDataMap.put(Constant.General.DL_ID, packageSchedule.getDlId());
		jobDataMap.put(Constant.General.STANDARD_PACKAGE_PROCESSOR, standardPackageProcessor);
		
		return jobDataMap;
	}
}