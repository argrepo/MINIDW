package com.anvizent.minidw.service.utils;

import java.util.List;

import org.quartz.JobKey;
import org.quartz.SchedulerException;

import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;
import com.datamodel.anvizent.service.model.SchedulerJobDetails;

public interface SchedulerService {
	
	boolean isSchedulerRunning();
	
	boolean startScheduler()  throws SchedulerException;
	
	boolean stopScheduler() throws SchedulerException;
	
	List<SchedulerJobDetails> getScheduledJobs() throws SchedulerException;
	
	List<SchedulerJobDetails> getScheduledJobs(String groupName) throws SchedulerException;
	
	SchedulerJobDetails getScheduledJobDetails(JobKey jobKey,String groupName) throws SchedulerException;
	
	public boolean pauseScheduler(String jobName,String groupName) throws SchedulerException;
	
	public List<String> getGroupNames() throws SchedulerException;
	
	public boolean resumeScheduler(String jobName,String groupName) throws SchedulerException;
	
	public List<SchedulerJobDetails> getFilterScheduledJobs(SchedulerFilterJobDetails groupName) throws SchedulerException;
	
	public List<QuartzSchedulerJobInfo> getFilterScheduledJobsWithFilter(SchedulerFilterJobDetails groupName) throws SchedulerException;
	
	public SchedulerJobDetails getFilterScheduledJobDetails(JobKey jobKey,String groupName,String fromDate,String toDate) throws SchedulerException;

}
