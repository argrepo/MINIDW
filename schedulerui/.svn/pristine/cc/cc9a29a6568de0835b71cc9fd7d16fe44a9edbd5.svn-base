package com.anvizent.schedulers.quartzJobScheduler;
	import java.util.Date;

	import org.quartz.Job;
	import org.quartz.JobDataMap;
	import org.quartz.JobExecutionContext;
	import org.quartz.JobExecutionException;
	import org.quartz.JobKey;

	public class MyQuartzjob implements Job {

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			// TODO Auto-generated method stub
			JobKey jobKey = context.getJobDetail().getKey();
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			System.out.println("the key is " + jobKey.getName() + " and the map is " + jobDataMap.getString("name")
					+ " date -> " + new Date());

			
				System.out.println("Started-the JobName is: "+ jobKey.getName()+"The trigger name is: "+context.getTrigger().getKey());
				/*try {
				  Thread.sleep(7000);
				System.out.println("in middle - the JobName is:  "+ jobKey.getName()+"The Next Fire Time is: "+context.getTrigger().getNextFireTime());
				Thread.sleep(7000);
				System.out.println("in stopped - the JobName is: "+ jobKey.getName()+"The Trigger Final Fire Time is: "+context.getTrigger().getFinalFireTime());
				*/
			/*} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			
			
		}

	}



