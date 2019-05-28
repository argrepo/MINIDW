package com.anvizent.scheduler.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;

import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.scheduler.commonUtil.MasterSchedulerQueueUtil;
import com.anvizent.scheduler.service.MasterService;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.PackageExecution;

public class PackageExecutionToQueuePusherJob implements Job {
	
	protected static final Log LOGGER = LogFactory.getLog(PackageExecutionToQueuePusherJob.class);
	
	MasterService masterService;
	protected Scheduler scheduler;
	CloudClient cloudClient;
	MasterSchedulerQueueUtil masterSchedulerQueueUtil;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

		try {
			cloudClient = (CloudClient) jobDataMap.get("clientCloud");
			masterService = (MasterService) jobDataMap.get("masterService");
			scheduler = (Scheduler) jobDataMap.get("scheduler");
			masterSchedulerQueueUtil = (MasterSchedulerQueueUtil) jobDataMap.get("schedulerSlavesUtil");
			
			List<PackageExecution> scheduledPackagesForExecuion = null;
			scheduledPackagesForExecuion = masterService
					.getScheduledPackagesForExecution(cloudClient.getId());
			String clientId = cloudClient.getId()+"";
			for (PackageExecution packageExecution : scheduledPackagesForExecuion) {
				packageExecution.setClientId(clientId);
				
				packageExecution.setExecutionStartDate(TimeZoneDateHelper.getFormattedDateString());
				masterSchedulerQueueUtil.addToExecutionQueue(packageExecution);
			}
			
		} catch (Throwable e) {
			LOGGER.error("Error while fetching to be execute packages of " + context.getJobDetail().getKey(),e);
		}
	}
	
}
