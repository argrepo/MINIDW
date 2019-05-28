package com.anvizent.scheduler.master;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.anvizent.minidw.service.utils.GroupKeyMatcherForOtherJobs;
import com.anvizent.minidw.service.utils.GroupKeyMatcherForScheduledJobs;
import com.anvizent.minidw.service.utils.QuartzSchedulerServiceImpl;
import com.anvizent.scheduler.commonUtil.MasterSchedulerQueueUtil;
import com.anvizent.scheduler.configuration.AppProperties;
import com.anvizent.scheduler.job.PackageExecutionToQueuePusherJob;
import com.anvizent.scheduler.job.ScheduledPackagesToSchedulerJob;
import com.anvizent.scheduler.job.SlaveExecutionQueueJob;
import com.anvizent.scheduler.job.SlaveHeartBeatCheckerJob;
import com.anvizent.scheduler.job.SlaveUploadQueueJob;
import com.anvizent.scheduler.service.MasterService;
import com.datamodel.anvizent.helper.MasterSchedulerConstants;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;
import com.datamodel.anvizent.service.model.SchedulerMaster;

@Import(AppProperties.class)
@Component
public class MasterQuartzScheduler extends QuartzSchedulerServiceImpl {

	private @Value("${master.scheduler.minimum.thread.count:25}") int minimumThreadCount;
	final Logger LOGGER = LoggerFactory.getLogger(MasterQuartzScheduler.class);
	@Autowired
	MasterService masterService;
	SchedulerMaster schedulerMaster;

	@Autowired
	MasterSchedulerQueueUtil schedulerSlavesUtil;

	public SchedulerMaster getSchedulerMaster() {
		return schedulerMaster;
	}

	public void setSchedulerMaster(SchedulerMaster schedulerMaster) {
		this.schedulerMaster = schedulerMaster;
	}

	QuartzMasterSchedulerListener masterSchedulerListener;

	@Override
	public List<QuartzSchedulerJobInfo> getFilterScheduledJobsWithFilter(SchedulerFilterJobDetails groupName)
			throws SchedulerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean startScheduler() throws SchedulerException {
		if (!isSchedulerRunning()) {
			// addSchedulerContext();
			// scheduler = new StdSchedulerFactory().getScheduler();
			scheduler = new StdSchedulerFactory(getQuartzProperties()).getScheduler();
			masterSchedulerListener = new QuartzMasterSchedulerListener(masterService, schedulerMaster.getId() + "",
					"Desc", schedulerMaster.getId(), scheduler);
			scheduler.getListenerManager().addSchedulerListener(masterSchedulerListener);
			// setSchedulerContext();
			LOGGER.info("Summary " + scheduler.getMetaData().getSummary());
			scheduler.start();
			scheduler.getListenerManager().addTriggerListener(
					new OtherJobsTriggerListener(masterService, " name", scheduler), new GroupKeyMatcherForOtherJobs());
			scheduler.getListenerManager().addTriggerListener(
					new ILRunnerTriggerListener(masterService, " name", "Desc", scheduler),
					new GroupKeyMatcherForScheduledJobs());

			schedulerSlavesUtil.setSchedulerSlaves(getSchedulerMaster().getSchedulerSlaves());

			for (CloudClient cloudClient : getSchedulerMaster().getClientIds()) {
				if (!cloudClient.isActive()) {
					LOGGER.error("Inactive Client " + cloudClient);
					continue;
				}
				String deploymentType = cloudClient.getDeploymentType();
				if (StringUtils.isNotBlank(deploymentType)
						&& (deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)
								|| deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_CLOUD))) {
					addNewJobForRespectiveClientID(cloudClient);
				} else {
					LOGGER.error("Client " + cloudClient.getId() + " skipped due to the deployment type" + deploymentType);
				}
				addJobForExecutionChecking(cloudClient);
			}
			addJobForUploadQueue();
			addJobForExecutionQueue();
			addJobForSlavesHeartBeat();

			return isSchedulerRunning();
		} else {
			throw new SchedulerException("Already Scheduler has Started");
		}

	}

	/**
	 * Creating a Quartz job to check the heart beat of each slave at configured
	 * interval of time
	 * 
	 * @throws SchedulerException
	 */
	private void addJobForSlavesHeartBeat() throws SchedulerException {
		String groupname = MasterSchedulerConstants.SLAVE_HEART_BEAT_CHECKER.name();
		JobDataMap jdmap1 = getJobDataMap();
		JobKey jobKey = new JobKey("SlavesHeartBeatChecking", groupname);
		JobDetail jobDetail1 = JobBuilder.newJob(SlaveHeartBeatCheckerJob.class).withIdentity(jobKey)
				.withDescription("Checking the status of slaves").setJobData(jdmap1).storeDurably().build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_SlavesHeartBeatChecking", groupname)
				.withSchedule(CronScheduleBuilder
						.cronSchedule(getMinuteCronExpression(getSchedulerMaster().getSlaveHeartBeatInterval()))
						.withMisfireHandlingInstructionFireAndProceed())
				.build();

		scheduler.scheduleJob(jobDetail1, trigger);

	}

	/**
	 * Creates a Quartz job for each mentioned client for fetching the scheduled
	 * packages
	 * 
	 * @param clientId
	 * @throws SchedulerException
	 */
	private void addNewJobForRespectiveClientID(CloudClient clientId) throws SchedulerException {
		String groupname = MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name();
		JobDataMap jdmap1 = getJobData(clientId);
		JobKey jobKey = new JobKey("job_" + clientId.getId(), groupname);
		JobDetail jobDetail1 = JobBuilder.newJob(ScheduledPackagesToSchedulerJob.class).withIdentity(jobKey)
				.withDescription("Source upload of " + clientId).setJobData(jdmap1).storeDurably().build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + clientId.getId(), groupname)
				.withSchedule(CronScheduleBuilder
						.cronSchedule(getMinuteCronExpression(schedulerMaster.getPackageUploadInterval()))
						.withMisfireHandlingInstructionFireAndProceed())
				.withPriority(9).build();

		scheduler.scheduleJob(jobDetail1, trigger);

	}

	/**
	 * Create a Quartz job to fetch the upload completed packages for each
	 * client
	 * 
	 * @param cloudClient
	 * @throws SchedulerException
	 */
	private void addJobForExecutionChecking(CloudClient cloudClient) throws SchedulerException {
		String groupname = MasterSchedulerConstants.CLIENTS_EXECUTION_GROUP.name();
		JobDataMap jdmap1 = getJobData(cloudClient);
		JobKey jobKey = new JobKey("job_" + cloudClient.getId(), groupname);
		JobDetail jobDetail1 = JobBuilder.newJob(PackageExecutionToQueuePusherJob.class).withIdentity(jobKey)
				.withDescription("Source upload of " + cloudClient).setJobData(jdmap1).storeDurably().build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + cloudClient.getId(), groupname)
				.withSchedule(CronScheduleBuilder
						.cronSchedule(getMinuteCronExpression(schedulerMaster.getPackageExecutionInterval()))
						.withMisfireHandlingInstructionFireAndProceed())
				.withPriority(8).build();

		scheduler.scheduleJob(jobDetail1, trigger);
	}

	private void addJobForUploadQueue() throws SchedulerException {
		String groupname = MasterSchedulerConstants.CLIENTS_UPLOAD_GROUP.name();

		JobDataMap jdmap1 = getJobDataMap();
		JobKey jobKey = new JobKey("UploadQueueRefresh", groupname);

		JobDetail jobDetail = JobBuilder.newJob(SlaveUploadQueueJob.class).withIdentity(jobKey)
				.withDescription("Source upload Pusher").setJobData(jdmap1).storeDurably().build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_UploadPusher", groupname)
				.withSchedule(CronScheduleBuilder
						.cronSchedule(getSecondsCronExpression(getSchedulerMaster().getSourceUploadInterval()))
						.withMisfireHandlingInstructionFireAndProceed())
				.withPriority(10).build();

		scheduler.scheduleJob(jobDetail, trigger);

	}

	private void addJobForExecutionQueue() throws SchedulerException {
		String groupname = MasterSchedulerConstants.CLIENTS_EXECUTION_GROUP.name();

		JobDataMap jdmap1 = getJobDataMap();
		JobKey jobKey = new JobKey("ExecutionQueueRefresh", groupname);

		JobDetail jobDetail = JobBuilder.newJob(SlaveExecutionQueueJob.class).withIdentity(jobKey)
				.withDescription("Package Execution Pusher").setJobData(jdmap1).storeDurably().build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_PackageExecution", groupname)
				.withSchedule(CronScheduleBuilder
						.cronSchedule(getSecondsCronExpression(getSchedulerMaster().getSourceExecutionInterval()))
						.withMisfireHandlingInstructionFireAndProceed())
				.withPriority(10).build();

		scheduler.scheduleJob(jobDetail, trigger);

	}

	public JobDataMap getJobData(CloudClient clientCloud) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("clientCloud", clientCloud);
		jobDataMap.put("scheduler", scheduler);
		jobDataMap.put("masterService", masterService);
		jobDataMap.put("schedulerSlavesUtil", schedulerSlavesUtil);
		jobDataMap.put(Constants.Config.HEADER_CLIENT_ID, clientCloud.getId() + "");

		return jobDataMap;
	}

	public JobDataMap getJobDataMap() {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("schedulerSlavesUtil", schedulerSlavesUtil);
		jobDataMap.put(Constants.Config.HEADER_CLIENT_ID, "");
		return jobDataMap;
	}

	public void addSchedulerContext() {
		addPropertiesConfiguration("quartzproperties", getQuartzProperties());
	}

	public Properties getQuartzProperties() {
		Properties properties = new Properties();
		properties.setProperty("org.quartz.scheduler.instanceName",
				"Master Scheduler " + getSchedulerMaster().getName());
		properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
		properties.setProperty("org.quartz.threadPool.threadCount",
				(getSchedulerMaster().getThreadCount() > minimumThreadCount ? getSchedulerMaster().getThreadCount()
						: 25) + "");
		properties.setProperty("org.quartz.threadPool.threadPriority", "5");
		return properties;
	}

	public long getSchedulerId() {
		if (isSchedulerRunning()) {
			return masterSchedulerListener.getSchedulerId();
		}

		return 0;
	}

	public String getMinuteCronExpression(long minute) {
		if (minute == 0) {
			minute = 3;
		}
		return "0 0/" + minute + " * * * ?";
	}

	public String getSecondsCronExpression(long seconds) {
		if (seconds == 0) {
			seconds = 15;
		}
		return seconds + "/" + seconds + " * * * * ?";
	}

}
