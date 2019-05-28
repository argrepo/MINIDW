package com.anvizent.client.scheduler.job;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
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
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.util.LinkedMultiValueMap;

import com.anvizent.client.scheduler.constant.Constant;
import com.anvizent.client.scheduler.util.EmailSender;
import com.anvizent.client.scheduler.util.PropertiesPlaceHolder;
import com.anvizent.minidw.service.utils.processor.StandardPackageProcessor;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.MasterSchedulerConstants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.HybridClientsGrouping;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HybridClientAccessKeyAdder implements Job {
	private Logger logger = Logger.getLogger(HybridClientAccessKeyAdder.class);
	
	public static boolean isWebserviceStopped = false;
	public static boolean isManualIntervention = false;
	public static String clientIds = "";
	private RestTemplateUtilities packageRestUtilities;
	private RestTemplateUtilities loginRestUtilities;
	private StandardPackageProcessor standardPackageProcessor;
	Scheduler scheduler;
	public static EmailSender emailSender = null;
	static HybridClientsGrouping hybridClientsGrouping = null;
	String plainAccessKey = null;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			PropertiesPlaceHolder propertiesPlaceHolder = (PropertiesPlaceHolder)jobDataMap.get(Constant.General.PROPERTIES_PLACE_HOLDER);
			packageRestUtilities = (RestTemplateUtilities) jobDataMap.get(Constant.General.PACKAGE_REST_UTILITY);
			loginRestUtilities = (RestTemplateUtilities) jobDataMap.get(Constant.General.LOGIN_REST_UTILITY);
			scheduler = (Scheduler) jobDataMap.get(Constant.General.SCHEDULER);
			standardPackageProcessor = (StandardPackageProcessor) jobDataMap.get(Constant.General.STANDARD_PACKAGE_PROCESSOR);
			
			String accessKey = propertiesPlaceHolder.getAccessKey();
			try {
				plainAccessKey = EncryptionServiceImpl.getInstance().decrypt(accessKey);
			} catch (Exception e) {
				throw new JobExecutionException("Unable to decrypt client id " + accessKey);
			}
			
			List<CloudClient> clientsList = getClientsList(plainAccessKey);
			sendActivationMail();
			if (clientsList != null ) {
				// 001,002,003,004
				clientIds = "";
				for (CloudClient cloudClient : clientsList) {
					clientIds += cloudClient.getId() + ", ";
					addClientsToScheduler(cloudClient);
				}
				removeClientIfDoesNotExists(clientsList);
				// 001,002,003,007
				 
			}
		} catch (Throwable ex) {
			logger.error("Error in client retrieval page",ex);
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			sendClientRetrievalErrorMail(errors.toString());
		}

	}
	
	private void sendActivationMail() {
		// TODO Auto-generated method stub
		if (isWebserviceStopped) {
			isWebserviceStopped = false;
			if ( emailSender != null ) {
				try {
					String accessKey = plainAccessKey;
					if ( hybridClientsGrouping != null ) {
						accessKey = hybridClientsGrouping.getName() + " - " + hybridClientsGrouping.getAccessKey();
					}
					emailSender.sendSchedulerRestartActivation(accessKey, clientIds, scheduler.getMetaData().getSummary());
				} catch (Exception e) {
					emailSender = null;
					e.printStackTrace();
					logger.error("error while sending mail ",e);
				}
			}
			try {
				resumeJobs();
			} catch (Exception e) {
				logger.error("Error while resuming the jobs ", e);
			}
		}
	}

	private void sendClientRetrievalErrorMail(String errors) {
		if ( !isWebserviceStopped) {
			isWebserviceStopped = true;
			if ( emailSender != null ) {
				try {
					String accessKey = plainAccessKey;
					if ( hybridClientsGrouping != null ) {
						accessKey = hybridClientsGrouping.getName() + " - " + hybridClientsGrouping.getAccessKey();
					}
					emailSender.sendSchedulersInterruption(errors, accessKey, clientIds, scheduler.getMetaData().getSummary());
				} catch (Exception e) {
					emailSender = null;
					e.printStackTrace();
					logger.error("error while sending mail ",e);
				}
			}
			try {
				pauseJobs();
			} catch (Exception e) {
				logger.error("Error while pausing the jobs ", e);
			}
		}
	}

	private List<CloudClient> getClientsList(String clientId) {
		List<CloudClient> clientsList = null;
		String getHybridGroupDetailsUrl = "/getHybridClientGroupingDetailsByAccessKey?accessKey={accessKey}";
		
		if ( isManualIntervention ) {
			getHybridGroupDetailsUrl = "/getHybridClientGroupingDetailsByAccessKeyManualIntervention?accessKey={accessKey}";
		}
		DataResponse dataResponseGetClientDetail = loginRestUtilities.postRestObject(new CustomRequest(),
				getHybridGroupDetailsUrl, new LinkedMultiValueMap<>(), DataResponse.class, clientId);
		
		if (dataResponseGetClientDetail != null && dataResponseGetClientDetail.getHasMessages() ) {

			if ( dataResponseGetClientDetail.getMessages().get(0).getCode().equals("SUCCESS")) {

				@SuppressWarnings("unchecked")
				LinkedHashMap<String, Object> clientDetails = (LinkedHashMap<String, Object>) dataResponseGetClientDetail
						.getObject();
				ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				HybridClientsGrouping hybridClientsGroupingData = mapper1.convertValue(clientDetails,
						new TypeReference<HybridClientsGrouping>() {
						});

				if (!hybridClientsGroupingData.isActive() || hybridClientsGroupingData.getClientIds().size() == 0) {
					// TODO cleanup all scheduled jobs of this client
					// TODO scheaduler.clear()
				}
				hybridClientsGrouping = hybridClientsGroupingData;
				clientsList = hybridClientsGroupingData.getClientIds();
			} else {
				throw new RuntimeException("client ids not found for access key: " + clientId +";Reason: " + dataResponseGetClientDetail.getMessages().get(0).getText() );
			}
		} else {
			throw new RuntimeException("unable to connect services for access key: " + clientId);
		}
		return clientsList;
	}
	private void addClientsToScheduler(CloudClient cloudClient) {
		JobKey clientJobKey = new JobKey(cloudClient.getId()+"", MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name());
		try {
			if (!scheduler.checkExists(clientJobKey)) {
				TriggerKey clientTriggerKey = new TriggerKey(cloudClient.getId()+"",MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name());
				JobDataMap clientJobData = getJobDataMap(cloudClient.getId());
				JobDetail clientJobDetails = JobBuilder.newJob(HybridSourcePackageAdder.class)
						.withDescription("Client scheduler for " + cloudClient.getClientName())
						.withIdentity(clientJobKey).setJobData(clientJobData).build();
				Trigger clientTrigger = TriggerBuilder.newTrigger().withIdentity(clientTriggerKey)
						.startNow()
						.withSchedule(CronScheduleBuilder.cronSchedule("0 0/"+(int)hybridClientsGrouping.getIntervalTime()+" * * * ?").withMisfireHandlingInstructionFireAndProceed()).build();
				scheduler.scheduleJob(clientJobDetails, clientTrigger);
			}
		} catch (SchedulerException e) {
			logger.error("Error while adding client to schedule ", e);
		}
	}
	
	private void removeClientIfDoesNotExists(List<CloudClient> clientsList) throws SchedulerException {
		Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name()));
		List<String> clientIds = new ArrayList<>();
		for (CloudClient cloudClient : clientsList) {
			clientIds.add(cloudClient.getId()+"");
		}
		for (JobKey jobKey : jobKeys) {
			String schedulerClientId = jobKey.getName();
			if ( !clientIds.contains(schedulerClientId) ) {
				scheduler.deleteJob(jobKey);
				scheduler.deleteJobs(new ArrayList<>(scheduler.getJobKeys(GroupMatcher.groupEquals(Constant.General.PACKAGE_GROUP+"_"+schedulerClientId))));
			}
		}
	}
	
	private JobDataMap getJobDataMap(long id) throws SchedulerException {
		JobDataMap jobdata = new JobDataMap();
		jobdata.put(Constant.General.SCHEDULER, scheduler);
		jobdata.put(Constant.General.PACKAGE_REST_UTILITY, packageRestUtilities);
		jobdata.put(Constant.General.STANDARD_PACKAGE_PROCESSOR, standardPackageProcessor);
		try {
			jobdata.put(Constant.General.CLIENT_ID, EncryptionServiceImpl.getInstance().encrypt(id+""));
		} catch (Exception e) {
			throw new SchedulerException(e.getMessage());
		}
		setS3BucketInfoAndFileSettings(id+"",jobdata);
		
		return jobdata;
	}
	
	@SuppressWarnings({ "unchecked" })
	private void setS3BucketInfoAndFileSettings(String clientID,JobDataMap jobdata) throws SchedulerException {
		CustomRequest customRequest = new CustomRequest(clientID, clientID, "", "hybrid");
		FileSettings fileSettings = null;
		S3BucketInfo s3BucketInfo = null;
		String encClientId = jobdata.getString(Constant.General.CLIENT_ID);
		// file setting info
		DataResponse fileSettingsDataResponse = packageRestUtilities.getRestObject(customRequest, "/getFileSettingsInfo",encClientId);
		if (fileSettingsDataResponse != null && fileSettingsDataResponse.getHasMessages()) {
			if (fileSettingsDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) fileSettingsDataResponse.getObject();
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			    fileSettings  = mapper.convertValue(map, new TypeReference<FileSettings>() { });
			} else {
				fileSettings = new  FileSettings();
				fileSettings.setMultiPartEnabled(false);
				fileSettings.setNoOfRecordsPerFile(10000);
			}
		}
		// s3 bucket info
		DataResponse s3BucketInfoDataResponse = packageRestUtilities.getRestObject(customRequest, "/getS3BucketInfoByClientId",encClientId);
		if (s3BucketInfoDataResponse != null && s3BucketInfoDataResponse.getHasMessages()) {
			if (s3BucketInfoDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) s3BucketInfoDataResponse.getObject();
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			    s3BucketInfo  = mapper.convertValue(map, new TypeReference<S3BucketInfo>() { });
			}else{
				throw new SchedulerException("s3 bucket info not found for client id:"+clientID);
			}
		}
		jobdata.put(Constant.General.S3_BUCKET_INFO, s3BucketInfo);
		jobdata.put(Constant.General.FILE_SETTINGS, fileSettings);
	}
	
	
	private void pauseJobs() throws SchedulerException {
		Set<JobKey> list = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name()));
		logger.info("active clients -> "  + list);
		//scheduler.deleteJobs(new ArrayList<>(list));
		scheduler.pauseJobs(GroupMatcher.jobGroupEquals(MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name()));
		
		Set<JobKey> listPackageJobs = scheduler.getJobKeys(GroupMatcher.jobGroupStartsWith(MasterSchedulerConstants.SCHEDULED_PACKAGES.name()));
		logger.info("active jobs -> "  + listPackageJobs);
		//scheduler.deleteJobs(new ArrayList<>(listPackageJobs));
		scheduler.pauseJobs(GroupMatcher.jobGroupStartsWith(MasterSchedulerConstants.SCHEDULED_PACKAGES.name()));
		logger.info("Jobs paused");
		
	}
	
	private void resumeJobs() throws SchedulerException {
		Set<JobKey> list = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name()));
		logger.info("paused clients -> "  + list);
		scheduler.resumeJobs(GroupMatcher.jobGroupEquals(MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name()));
		
		Set<JobKey> listPackageJobs = scheduler.getJobKeys(GroupMatcher.jobGroupStartsWith(MasterSchedulerConstants.SCHEDULED_PACKAGES.name()));
		logger.info("paused jobs -> "  + listPackageJobs);
		scheduler.resumeJobs(GroupMatcher.jobGroupStartsWith(MasterSchedulerConstants.SCHEDULED_PACKAGES.name()));
		logger.info("Jobs resumes");
	}
	
}