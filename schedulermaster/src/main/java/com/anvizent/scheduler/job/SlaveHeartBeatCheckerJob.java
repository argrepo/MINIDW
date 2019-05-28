package com.anvizent.scheduler.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.scheduler.commonUtil.MasterSchedulerQueueUtil;

public class SlaveHeartBeatCheckerJob implements Job {

	final Logger LOGGER = LoggerFactory.getLogger(SlaveUploadQueueJob.class);
	
	MasterSchedulerQueueUtil schedulerSlavesUtil;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			schedulerSlavesUtil = (MasterSchedulerQueueUtil) jobDataMap.get("schedulerSlavesUtil");
			schedulerSlavesUtil.slavesHeartBeatChecker();
		} catch (Exception e) {
			LOGGER.error("Error in slave heartbeat checking ",e);
		}
	}
	
}
