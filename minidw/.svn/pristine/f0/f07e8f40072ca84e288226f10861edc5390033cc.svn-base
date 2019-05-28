package com.anvizent.client.scheduler.listner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.anvizent.client.scheduler.constant.Constant;
import com.anvizent.client.scheduler.job.HybridClientAccessKeyAdder;
import com.anvizent.client.scheduler.job.listener.ILRunnerTriggerListener;
import com.anvizent.client.scheduler.job.listener.QuartzClientSchedulerListener;
import com.anvizent.client.scheduler.util.EmailSender;
import com.anvizent.client.scheduler.util.PropertiesPlaceHolder;
import com.anvizent.minidw.service.utils.GroupKeyMatcherForOtherJobs;
import com.anvizent.minidw.service.utils.GroupKeyMatcherForScheduledJobs;
import com.anvizent.minidw.service.utils.QuartzSchedulerServiceImpl;
import com.anvizent.minidw.service.utils.processor.StandardPackageProcessor;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.MasterSchedulerConstants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ClientScheduler extends QuartzSchedulerServiceImpl {

	private Logger logger = LoggerFactory.getLogger(ClientScheduler.class);

	//private static final String PACKAGE_GROUP = "PACKAGE_GROUP";
	private static final String CLIENT_ACCESS_KEY_UPDATE_CHECKING = "CLIENT_ACCESS_KEY_UPDATE_CHECKING";
	private PropertiesPlaceHolder propertiesPlaceHolder = null;
	private boolean webApp = false;
	QuartzClientSchedulerListener clientSchedulerListener;
	private RestTemplateUtilities packageRestUtilities;
	private StandardPackageProcessor standardPackageProcessor;

	private RestTemplateUtilities loginRestUtilities;

	public StandardPackageProcessor getStandardPackageProcessor() {
		return standardPackageProcessor;
	}
	
	public void setStandardPackageProcessor(StandardPackageProcessor standardPackageProcessor) {
		this.standardPackageProcessor = standardPackageProcessor;
	}
	
	public RestTemplateUtilities getLoginRestUtilities() {
		return loginRestUtilities;
	}

	public void setLoginRestUtilities(RestTemplateUtilities loginRestUtilities) {
		this.loginRestUtilities = loginRestUtilities;
	}

	public void setPackageRestUtilities(RestTemplateUtilities packageRestUtilities) {
		this.packageRestUtilities = packageRestUtilities;
	}

	public ClientScheduler() {
		try {
			propertiesPlaceHolder = new PropertiesPlaceHolder();
		} catch (Exception e) {
			logger.error("Occured while initiating the ClientScheduler");
		}
	}

	private void addSchedulerAdder() throws SchedulerException {
		JobKey clientCheckerJobKey = new JobKey(CLIENT_ACCESS_KEY_UPDATE_CHECKING, CLIENT_ACCESS_KEY_UPDATE_CHECKING);
		JobDataMap newJobDataMap = getJobDataMap();
		JobDetail ilSchedulerAdder = JobBuilder.newJob(HybridClientAccessKeyAdder.class).withIdentity(clientCheckerJobKey)
				.setJobData(newJobDataMap).storeDurably().withDescription("Fetch the schedule the packages").build();
		Trigger ilSchedulerAdderTrigger = TriggerBuilder.newTrigger()
				.withIdentity("ilSchedulerAdderTrigger", CLIENT_ACCESS_KEY_UPDATE_CHECKING)
				.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1).withMisfireHandlingInstructionFireNow())
				.build();
		logger.info("in App, addSchedulerAdder method,scheduling IlSchedulerAdder job");
		scheduler.scheduleJob(ilSchedulerAdder, ilSchedulerAdderTrigger);
	}

	public void addSchedulerContext() {
		//addPropertiesConfiguration("quartzproperties", propertiesPlaceHolder.getQuartzProperties());
	}

	private JobDataMap getJobDataMap() {
		JobDataMap newJobDataMap = new JobDataMap();
		newJobDataMap.put(Constant.General.PROPERTIES_PLACE_HOLDER, propertiesPlaceHolder);
		newJobDataMap.put(Constant.General.SCHEDULER, scheduler);
		newJobDataMap.put(Constant.General.PACKAGE_REST_UTILITY, packageRestUtilities);
		newJobDataMap.put(Constant.General.LOGIN_REST_UTILITY, loginRestUtilities);
		newJobDataMap.put(Constant.General.STANDARD_PACKAGE_PROCESSOR, standardPackageProcessor);
		try {
			newJobDataMap.put(Constant.General.CLIENT_ID, EncryptionServiceImpl.getInstance().encrypt("-1"));
		} catch (Exception e) {
			logger.error("",e);
		}
		return newJobDataMap;
	}

	public boolean startScheduler() throws SchedulerException {
		if (!isWebApp()) {
			if (!isSchedulerRunning()) {
				String accessKey = null;
				try {
					if (StringUtils.isBlank(propertiesPlaceHolder.getAccessKey())) {
						throw new Exception("Invalid access key");
					}
					accessKey = EncryptionServiceImpl.getInstance().decrypt(propertiesPlaceHolder.getAccessKey());
				} catch (Exception e) {
					logger.error("Access error " + accessKey,e);
					return false;
				}
				
				FileReader fr = null;
				try {
					
					fr = new FileReader(new ClassPathResource("mail.properties").getFile());
					Properties mailProperties = new Properties();
					mailProperties.load(fr);;
					EmailSender emailSender = new EmailSender(mailProperties);
					HybridClientAccessKeyAdder.emailSender = emailSender;
				} catch (IOException e) {
					logger.error("",e);
				} finally {
					if ( fr != null ) {
						try {
							fr.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error("",e);
						}
					}
				}
				
				
				
				HybridClientAccessKeyAdder.isWebserviceStopped = false;
				addSchedulerContext(); 
				scheduler = new StdSchedulerFactory().getScheduler();
				clientSchedulerListener = new QuartzClientSchedulerListener(
						packageRestUtilities, 
						accessKey + " Hybrid scheduler", 
						accessKey + " Client description", 
						"1","-1", scheduler
						);
				scheduler.getListenerManager().addSchedulerListener(clientSchedulerListener);
				setSchedulerContext();
				scheduler.start();
				scheduler.getListenerManager().addTriggerListener(new ILRunnerTriggerListener(
						packageRestUtilities, 
						accessKey+" Client scheduler", 
						accessKey+" Client description", scheduler)
						,new GroupKeyMatcherForScheduledJobs()
						);
				scheduler.getListenerManager()
				.addTriggerListener(
						new OtherJobsTriggerListener(
								packageRestUtilities, 
								accessKey+" Client scheduler", 
								accessKey+" Client description", scheduler)
						,new GroupKeyMatcherForOtherJobs()
						);
				
				
				addSchedulerAdder();
				return isSchedulerRunning();
			} else {
				throw new SchedulerException("Already Scheduler has Started");
			}
		} else {
			throw new SchedulerException("Scheduler not avaible for cloud clients");
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
		
		try {
			checkForNullJSONObject("");
			
			//int i = 10/0;
		} catch (Exception ex) {	
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			//sendMailCode(errors.toString(), "ASDFu", "1009000,1009001", "metaInfoTxt of schedulers");
		}
	}
	
	public static Object checkForNullJSONObject(String param) {
		if (param != null
				&& (!"".equalsIgnoreCase(param.trim()) && /* !param.isEmpty() */ param instanceof String)) {
			System.out.println("if");
		} else {
			System.out.println("else");
		}
		return null;
	}

	public List<QuartzSchedulerJobInfo> getTriggeredInfoByID(String clientId,long job_id,String webServiceUrl,String timeZone) throws SchedulerException {
		   
		DataResponse dataResponse = new DataResponse();
		String plainClientId = null;
	    CustomRequest customRequest;
	    QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
		try {
			plainClientId = EncryptionServiceImpl.getInstance().decrypt(clientId);
			//long scheduleId = clientSchedulerListener.getSchedulerId();
			quartzSchedulerJobInfo.setJobId(job_id);
			customRequest = new CustomRequest(webServiceUrl,plainClientId,plainClientId,"Client scheduler","hybrid",timeZone);
		} catch (Exception e) {
			throw new SchedulerException("Unable to decrypt client id " + propertiesPlaceHolder.getAccessKey());
		}
		dataResponse = packageRestUtilities.postRestObject(customRequest, "/schedule/getTriggeredInfoByID", quartzSchedulerJobInfo, clientId);

		List<QuartzSchedulerJobInfo> scheduleDetails = null;
		if (dataResponse != null && dataResponse.getMessages().size() > 0 && dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("SUCCESS")) {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			@SuppressWarnings("unchecked")
			List<?> list = (List<Map<String, Object>>) dataResponse.getObject();
			scheduleDetails = mapper.convertValue(list, new TypeReference<List<QuartzSchedulerJobInfo>>() {
			});
			
			return scheduleDetails;
			
		} else {
			return null;
		}
		
	}
	
	@Override
	public List<QuartzSchedulerJobInfo> getFilterScheduledJobsWithFilter(SchedulerFilterJobDetails schedulerFilterJobDetails) throws SchedulerException {
	   
		DataResponse dataResponse = new DataResponse();
		String clientId = null;
	    CustomRequest customRequest;
		try {
			List<Long> scheduleList = Arrays.asList(clientSchedulerListener.getSchedulerId());
			schedulerFilterJobDetails.setSchedulerId(scheduleList);
			clientId = EncryptionServiceImpl.getInstance().decrypt(propertiesPlaceHolder.getAccessKey());
			customRequest = new CustomRequest(clientId,clientId,"Client scheduler","hybrid");
		} catch (Exception e) {
			throw new SchedulerException("Unable to decrypt client id " + propertiesPlaceHolder.getAccessKey());
		}
		dataResponse = packageRestUtilities.postRestObject(customRequest, "/schedule/getScheduledJobsInfo", schedulerFilterJobDetails, propertiesPlaceHolder.getAccessKey());

		List<QuartzSchedulerJobInfo> scheduleDetails = null;
		if (dataResponse != null && dataResponse.getMessages().size() > 0 && dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("SUCCESS")) {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			@SuppressWarnings("unchecked")
			List<?> list = (List<Map<String, Object>>) dataResponse.getObject();
			scheduleDetails = mapper.convertValue(list, new TypeReference<List<QuartzSchedulerJobInfo>>() {
			});
			
			return scheduleDetails;
			
		} else {
			return null;
		}
		
	}
	
	public List<QuartzSchedulerJobInfo> getScheduledInfoJob(String clientId,String webServiceUrl,String timeZone) throws SchedulerException {
	   
		DataResponse dataResponse = new DataResponse();
		String plainClientId = null;
	    CustomRequest customRequest;
	    QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
		try {
			if (!isSchedulerRunning()) {
				return null;
			}
			long scheduleId = clientSchedulerListener.getSchedulerId();
			quartzSchedulerJobInfo.setSchedulerId(scheduleId);
			plainClientId = EncryptionServiceImpl.getInstance().decrypt(clientId);
			customRequest = new CustomRequest(webServiceUrl,plainClientId,plainClientId,"Client scheduler","hybrid",timeZone);
		} catch (Exception e) {
			throw new SchedulerException("Unable to decrypt client id " + clientId);
		}
		dataResponse = packageRestUtilities.postRestObject(customRequest, "/schedule/getScheduledJobsInfoById", quartzSchedulerJobInfo, clientId);

		List<QuartzSchedulerJobInfo> scheduleDetails = null;
		if (dataResponse != null && dataResponse.getMessages().size() > 0 && dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("SUCCESS")) {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			@SuppressWarnings("unchecked")
			List<?> list = (List<Map<String, Object>>) dataResponse.getObject();
			scheduleDetails = mapper.convertValue(list, new TypeReference<List<QuartzSchedulerJobInfo>>() {
			});
			
			return scheduleDetails;
			
		} else {
			return null;
		}
		
	}
	
	public List<String> getScheduledClientIds() throws SchedulerException {
		List<String> clientIds = new ArrayList<>();
		try {
			
			Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name()));
			for (JobKey jobKey : jobKeys) {
				clientIds.add(jobKey.getName());
			}
		} catch (Exception e) {
			throw new SchedulerException("Unable to decrypt client id " + propertiesPlaceHolder.getAccessKey());
		}
		return clientIds;
		
	}
	
	
	public void refreshProperties() throws FileNotFoundException, IOException {
		propertiesPlaceHolder.refreshProperties();
	}
	
	public boolean isWebApp() {
		return webApp;
	}
	
	public void setWebApp(boolean webApp) {
		this.webApp = webApp;
	}

	public PropertiesPlaceHolder getPropertiesPlaceHolder() {
		return propertiesPlaceHolder;
	}

	public void setPropertiesPlaceHolder(PropertiesPlaceHolder propertiesPlaceHolder) {
		this.propertiesPlaceHolder = propertiesPlaceHolder;
	}

}
