package com.anvizent.scheduler.job;

import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;

import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.scheduler.commonUtil.MasterSchedulerQueueUtil;
import com.anvizent.scheduler.service.MasterServiceImpl;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.Schedule;

public class SourcesPackagesToUploadQueueJob implements Job {

	protected static final Log logger = LogFactory.getLog(MasterServiceImpl.class);
	Scheduler scheduler;
	Schedule packageSchedule;
	CloudClient cloudClient;
	Queue<String> packageIDQueue;
	MasterSchedulerQueueUtil schedulerSlavesUtil;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			cloudClient = (CloudClient) jobDataMap.get("cloudClient");
			packageSchedule = (Schedule) jobDataMap.get("packageSchedule");
			schedulerSlavesUtil = (MasterSchedulerQueueUtil) jobDataMap.get("schedulerSlavesUtil");

			PackageExecution packageExecution = new PackageExecution();
			packageExecution.setClientId(cloudClient.getId()+"");
			packageExecution.setUserId(packageSchedule.getUserId());
			packageExecution.setPackageId(packageSchedule.getPackageId());
			packageExecution.setUserPackage(packageSchedule.getUserPackage());
			packageExecution.setScheduleId(packageSchedule.getScheduleId());
			packageExecution.setDlId(packageSchedule.getDlId());
			packageExecution.setUploadStartDate(TimeZoneDateHelper.getFormattedDateString());
			logger.error("package uploaded to Queue started for " + context.getJobDetail().getKey());
			boolean addStatus = schedulerSlavesUtil.addToUploadQueue(packageExecution);
			if ( addStatus ) {
				logger.info("package uploaded to Queue completed for " + context.getJobDetail().getKey());
			} else {
				logger.info("Package already exist in quere for uploading ");
			}
		} catch (Exception e) {
			logger.error("Error while adding package to upload Queue",e);
		}
	}

}
