package com.anvizent.schedulers.quartzschd.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

import com.anvizent.schedulers.model.SchdDetails;
import com.anvizent.schedulers.quartzJobScheduler.MyQuartzjob;

@Component
public class MinidwSchedulerUtilities {

	Scheduler schd = null;

	public boolean isScheduleRunning() throws SchedulerException {
		if (schd != null && schd.isStarted() && !schd.isShutdown()) {
			return true;
		}
		return false;
	}

	public boolean startScheduler() throws SchedulerException {
		if (!isScheduleRunning()) {

			schd = new StdSchedulerFactory().getScheduler();
			schd.start();
			addNewJob("sree", "sg1", "0/30 * * * * ?");
			addNewJob("new", "sg2", "20 * * * * ?");
			System.out.println("Scheduler is Started");
			return true;
		} else {
			throw new SchedulerException("Already Scheduler has Started");
		}
	}

	public boolean stopScheduler() throws SchedulerException {
		
		if (isScheduleRunning()) {
			schd.clear();
			schd.shutdown();
			System.out.println("Scheduler stopped");
			return true;
		} else {
			System.out.println("Scheduler not yet started");
			return false;
		}

	}

	public void addNewJob(String groupId, String printerName, String cronExp) throws SchedulerException {

		JobDataMap jdmap1 = getJobData(printerName);
		JobKey jobKey = new JobKey("job_" + printerName, groupId);
		JobDetail jobDetail1 = JobBuilder.newJob(MyQuartzjob.class).withIdentity(jobKey).setJobData(jdmap1)
				.storeDurably().build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_" + printerName, groupId)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExp)).forJob(jobDetail1).build();

		Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger2_" + "sree", "trigger2_" + groupId)
				.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).forJob(jobDetail1).build();

		schd.addJob(jobDetail1, true);
		schd.scheduleJob(trigger);
		schd.scheduleJob(trigger2);
	}

	public static JobDataMap getJobData(String name) {
		JobDataMap jobData = new JobDataMap();
		jobData.put("name", name);
		return jobData;
	}

	public List<SchdDetails> getTotalNoOfJobs() throws SchedulerException {

		List<SchdDetails> schdDetailslist = new ArrayList<>();
		for (String groupnames : schd.getJobGroupNames()) {
			for (JobKey jobnames : schd.getJobKeys(GroupMatcher.jobGroupEquals(groupnames))) {
				SchdDetails schdDetails = new SchdDetails();
				String jobName = jobnames.getName();

				@SuppressWarnings("unchecked")
				List<Trigger> trigger = (List<Trigger>) schd.getTriggersOfJob(jobnames);
				Date nextFireTime = trigger.get(0).getNextFireTime();
				System.out.println("the JobName is" + jobName + "The GroupName is" + groupnames
						+ "The Next Fire Time is" + nextFireTime);

				schdDetails.setGrpName(groupnames);
				schdDetails.setJobName(jobName);
				schdDetails.setNextTriggerFireTime(nextFireTime);
				schdDetailslist.add(schdDetails);
			}
		}

		return schdDetailslist;
	}

}
