package com.anvizent.scheduler.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.scheduler.commonUtil.MasterSchedulerQueueUtil;

public class SlaveUploadQueueJob implements Job {

	MasterSchedulerQueueUtil schedulerSlavesUtil;
	final Logger LOGGER = LoggerFactory.getLogger(SlaveUploadQueueJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		//LOGGER.info("calling upload queue");
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			schedulerSlavesUtil = (MasterSchedulerQueueUtil) jobDataMap.get("schedulerSlavesUtil");
			schedulerSlavesUtil.checkUploadNodeAvailability();
		} catch (Throwable e) {
			LOGGER.error("Error in slave upload Queue Job",e);
		}
	}

}
