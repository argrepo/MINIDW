package com.anvizent.minidw.service.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;
import com.datamodel.anvizent.service.model.SchedulerJobDetails;


public abstract class QuartzSchedulerServiceImpl implements SchedulerService {

	final Logger logger = LoggerFactory.getLogger(QuartzSchedulerServiceImpl.class);
	
	protected Map<String, Properties> propertiesConfiguration = new LinkedHashMap<>();

	protected Scheduler scheduler;
	
	public Map<String, Properties> getPropertiesConfiguration() {
		return propertiesConfiguration;
	}
	
	public void addPropertiesConfiguration(String propertiesName, Properties propertiesFile) {
		propertiesConfiguration.put(propertiesName, propertiesFile);
	}
	
	public void setSchedulerContext() throws SchedulerException {
		
		Set<String> propertiesList = propertiesConfiguration.keySet();
		for (String properties : propertiesList) {
			Properties propertiesFile = propertiesConfiguration.get(properties);
			if (propertiesFile != null) {
				for (Entry<Object, Object> property : propertiesFile.entrySet()) {
					scheduler.getContext().put(property.getKey().toString(), property.getValue().toString());
				}
			}
		}
		
		
		
	}

	@Override
	public boolean isSchedulerRunning() {
		try {
			if (scheduler != null && scheduler.isStarted() && !scheduler.isShutdown()) {
				return true;
			}
		} catch (SchedulerException e) {
		}
		return false;
	}

	@Override
	public boolean startScheduler() throws SchedulerException {
		if (!isSchedulerRunning()) {
			scheduler = new StdSchedulerFactory().getScheduler();
			setSchedulerContext();
			scheduler.start();
			return true;
		} else {
			throw new SchedulerException("Already Scheduler has Started");
		}
	}

	@Override
	public boolean stopScheduler() throws SchedulerException {
		if (isSchedulerRunning()) {
			scheduler.clear();
			scheduler.shutdown();
			logger.info("Scheduler stopped");
			return false;
		} else {
			throw new SchedulerException("Scheduler not yet started");
		}
	}
	
	@Override
	public List<SchedulerJobDetails> getScheduledJobs() throws SchedulerException {
		List<SchedulerJobDetails> scheduledJobDetailsList = new ArrayList<>();
		
		for (String groupName : scheduler.getJobGroupNames()) {
			scheduledJobDetailsList.addAll(getScheduledJobs(groupName));
		}
		return scheduledJobDetailsList;
	}
	
	@Override
	public List<SchedulerJobDetails> getScheduledJobs(String groupName) throws SchedulerException {
		List<SchedulerJobDetails> scheduledJobDetailsList = new ArrayList<>();
		
		for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
			scheduledJobDetailsList.add(getScheduledJobDetails(jobKey, groupName));
		}
		return scheduledJobDetailsList;
	}
	
	@Override
	public List<SchedulerJobDetails> getFilterScheduledJobs(SchedulerFilterJobDetails schedulerFilterJobDetails) throws SchedulerException {
		List<SchedulerJobDetails> groupNamesDetailsList = new ArrayList<>();
		for (String groupNamesList : schedulerFilterJobDetails.getGroupNames()) {
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupNamesList))) {
				groupNamesDetailsList.add(getScheduledJobDetails(jobKey, groupNamesList));
			}
		}
		return groupNamesDetailsList;
	}
	
	@Override
	public SchedulerJobDetails getScheduledJobDetails(JobKey jobKey,String groupName) throws SchedulerException {
		SchedulerJobDetails schedulerJobDetails = new SchedulerJobDetails();
		
		String jobName = jobKey.getName();
		String jobGroup = jobKey.getGroup();
		JobDetail jobDetails = scheduler.getJobDetail(jobKey);
		JobDataMap jMap = jobDetails.getJobDataMap();
		
		// get job's trigger
		@SuppressWarnings("unchecked")
		List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
		Trigger trigger = triggers.get(0);
		Date nextFireTime = trigger.getNextFireTime();
		Date startTime = trigger.getStartTime();
		Date endTime = trigger.getEndTime();
		
		TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
		logger.info("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);
		schedulerJobDetails.setJobName(jobName);
		schedulerJobDetails.setDescription(jobDetails.getDescription());
		schedulerJobDetails.setGroupName(groupName);
		schedulerJobDetails.setPaused(TriggerState.PAUSED.equals(triggerState));
		schedulerJobDetails.setNextFireTime(nextFireTime.toString());
		schedulerJobDetails.setStartTime(startTime.toString());
		if(endTime != null){
			schedulerJobDetails.setEndTime(endTime.toString());
		}else{
			schedulerJobDetails.setEndTime("");
		}
		return schedulerJobDetails;
	}
	
	
	@Override
	public SchedulerJobDetails getFilterScheduledJobDetails(JobKey jobKey,String groupName,String fromDate,String toDate) throws SchedulerException {
		SchedulerJobDetails schedulerJobDetails = new SchedulerJobDetails();
		
		String jobName = jobKey.getName();
		String jobGroup = jobKey.getGroup();
		JobDetail jobDetails = scheduler.getJobDetail(jobKey);
		// get job's trigger
		@SuppressWarnings("unchecked")
		List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
		Trigger trigger = triggers.get(0);
		Date nextFireTime = trigger.getNextFireTime();
		TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
		logger.info("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);
		schedulerJobDetails.setJobName(jobName);
		schedulerJobDetails.setDescription(jobDetails.getDescription());
		schedulerJobDetails.setGroupName(groupName);
		schedulerJobDetails.setPaused(TriggerState.PAUSED.equals(triggerState));
		schedulerJobDetails.setNextFireTime(nextFireTime.toString());
		return schedulerJobDetails;
	}


	@Override
	public boolean pauseScheduler(String jobName,String groupName) throws SchedulerException {
		if (isSchedulerRunning()) {
			JobKey jobKeyValue = new JobKey(jobName, groupName);
			scheduler.pauseJob(jobKeyValue);
			return true;
		}
		else {
			throw new SchedulerException("Scheduler not paused");
		}
	}

	@Override
	public List<String> getGroupNames() throws SchedulerException {
		List<String> groupList = new ArrayList<>();
		for (String groupName : scheduler.getJobGroupNames()) {
			groupList.add(groupName);
		}
		return groupList;
	}

	@Override
	public boolean resumeScheduler(String jobName, String groupName) throws SchedulerException {
		if (isSchedulerRunning()) {
			JobKey jobKeyValue = new JobKey(jobName, groupName);
			scheduler.resumeJob(jobKeyValue);
			return true;
		}
		else {
			throw new SchedulerException("Scheduler not resumed");
		}
	}

}
