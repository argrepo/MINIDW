package com.anvizent.scheduler.job;

import java.util.TimeZone;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.scheduler.commonUtil.MasterSchedulerQueueUtil;

public class SlaveExecutionQueueJob implements Job {

	MasterSchedulerQueueUtil schedulerSlavesUtil;
	final Logger LOGGER = LoggerFactory.getLogger(SlaveExecutionQueueJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		//LOGGER.info("calling execution queue");
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			schedulerSlavesUtil = (MasterSchedulerQueueUtil) jobDataMap.get("schedulerSlavesUtil");
			schedulerSlavesUtil.checkExectionNodeAvailability();
		} catch (Exception e) {
			LOGGER.error("execution queue pusher",e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(TimeZone.getAvailableIDs().length);
	}

}
