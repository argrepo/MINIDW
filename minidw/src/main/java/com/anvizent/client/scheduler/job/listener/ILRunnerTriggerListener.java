
package com.anvizent.client.scheduler.job.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.client.scheduler.constant.Constant;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.MinidwJobState;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerTriggerInfo;

public class ILRunnerTriggerListener implements TriggerListener {

    public static final String LISTENER_NAME = "dummyJobListenerName";
    private Logger logger = LoggerFactory.getLogger(ILRunnerTriggerListener.class);

    private RestTemplateUtilities restTemplateUtilities;
    private String name, description;
	private long schedulerId = QuartzClientSchedulerListener.schedulerId;
    private Scheduler scheduler;
    private Map<JobKey, Long> jobIds = QuartzClientSchedulerListener.jobIds;
    private Map<JobKey, Long> triggerIds = new HashMap<>();

    //clientId,plainClientId
    //CustomRequest customRequest;



    public ILRunnerTriggerListener(RestTemplateUtilities restTemplateUtilities,String name, String description,Scheduler scheduler) {
        this.restTemplateUtilities = restTemplateUtilities;
        this.name = name;
        this.description = description;
        this.scheduler = scheduler;
    }


    @Override
    public String getName() {
        return LISTENER_NAME; // must return a name
    }


    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
    	//logger.info("Trigger triggerFired " + trigger.getKey() );
    }


    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        logger.error("Trigger vetoJobExecution " + trigger.getKey() );
        String clientId = context.getJobDetail().getJobDataMap().get(Constant.General.CLIENT_ID)+"";
        JobKey jobKey = trigger.getJobKey();
        Date nextFireTime = trigger.getNextFireTime();
        QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo = new QuartzSchedulerTriggerInfo();

        QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
        quartzSchedulerJobInfo.setJobId(jobIds.get(jobKey));
        quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(schedulerId));
        quartzSchedulerJobInfo.setJobDescription("Execution for the job " + jobKey.getName());
        quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
		quartzSchedulerJobInfo.setStatus(MinidwJobState.RUNNING.toString());

		quartzSchedulerTriggerInfo.setQuartzSchedulerJobInfo(quartzSchedulerJobInfo);
        quartzSchedulerTriggerInfo.setDescription(description);
        quartzSchedulerTriggerInfo.setFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
        quartzSchedulerTriggerInfo.setStartTime(TimeZoneDateHelper.getFormattedDateString());

        boolean isTriggerFound = false;
        if ( triggerIds.containsKey(jobKey) ) {
            isTriggerFound = true;
            quartzSchedulerJobInfo.setStatus(MinidwJobState.READY.toString());
            quartzSchedulerTriggerInfo.setStatus( MinidwJobState.IGNORED.toString() );
            quartzSchedulerTriggerInfo.setEndTime(TimeZoneDateHelper.getFormattedDateString());
            quartzSchedulerTriggerInfo.setDescription("Already a job executing for the same");
        } else {
            quartzSchedulerTriggerInfo.setEndTime(null);
            quartzSchedulerTriggerInfo.setStatus( MinidwJobState.READY.toString() );
        }


        try {
        	 DataResponse dataResponse = restTemplateUtilities.postRestObject(getCustomRequest(clientId), "/schedule/addSchedulerTriggerInfo", quartzSchedulerTriggerInfo, clientId);
             if (dataResponse != null && dataResponse.getHasMessages()) {
                 if(dataResponse.getObject() != null && !isTriggerFound){
                     getTriggerIds().put(jobKey, Long.valueOf(dataResponse.getObject().toString()));
                 }
             }
		} catch (Exception e) {
			logger.error("error", e);
		}
       
        return isTriggerFound;
    }


    @Override
    public void triggerMisfired(Trigger trigger) {
        logger.info("Trigger misfired " + trigger.getKey() );
        QuartzSchedulerJobInfo quartzSchedulerInfo = new QuartzSchedulerJobInfo();
        
        try {
        	JobKey jobKey = trigger.getJobKey();
			String clientId = scheduler.getJobDetail(jobKey).getJobDataMap().get(Constant.General.CLIENT_ID) + "";
			quartzSchedulerInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(schedulerId));
			Long jobId = jobIds.get(jobKey);
			if ( jobId == null) {
				Thread.sleep(1000);
				jobId = jobIds.get(jobKey);
				logger.info("job id for "+jobKey+" : " + jobId );
			}
			quartzSchedulerInfo.setJobId(jobId);
			Date nextFireTime = trigger.getFireTimeAfter(new Date());
			logger.info("Trigger misfired next date --> " + jobKey + " - " + TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			quartzSchedulerInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));
			MinidwJobState state = MinidwJobState.READY;
			quartzSchedulerInfo.setStatus(state.name());
			restTemplateUtilities.postRestObject(getCustomRequest(clientId), "/schedule/updateSchedulerJobInfo",
					quartzSchedulerInfo, clientId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("Trigger misfired " ,e);
		}
    }


    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
        JobKey jobKey = trigger.getJobKey();
        String clientId = context.getJobDetail().getJobDataMap().get(Constant.General.CLIENT_ID)+"";
        if ( getTriggerIds().containsKey(jobKey)) {
            Date nextFireTime = trigger.getNextFireTime();
            QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo = new QuartzSchedulerTriggerInfo();
        	quartzSchedulerTriggerInfo.setTriggerId(getTriggerIds().get(jobKey));

            QuartzSchedulerJobInfo quartzSchedulerJobInfo = new QuartzSchedulerJobInfo();
            quartzSchedulerJobInfo.setJobId(jobIds.get(jobKey));
            quartzSchedulerJobInfo.setQuartzSchedulerInfo(new QuartzSchedulerInfo(schedulerId));
            if ( nextFireTime != null ) {
            	quartzSchedulerJobInfo.setStatus(MinidwJobState.READY.toString());
            } else {
            	quartzSchedulerJobInfo.setStatus(MinidwJobState.COMPLETED.toString());
            }
            quartzSchedulerJobInfo.setNextFireTime(TimeZoneDateHelper.getFormattedDateString(nextFireTime));

            quartzSchedulerTriggerInfo.setQuartzSchedulerJobInfo(quartzSchedulerJobInfo);
            quartzSchedulerTriggerInfo.setStatus(MinidwJobState.COMPLETED.toString());
            quartzSchedulerTriggerInfo.setEndTime(TimeZoneDateHelper.getFormattedDateString());

            restTemplateUtilities.postRestObject(getCustomRequest(clientId), "/schedule/updateSchedulerTriggerInfo", quartzSchedulerTriggerInfo, clientId);
            getTriggerIds().remove(jobKey);
        }
    }

    public Map<JobKey, Long> getTriggerIds() {
        return triggerIds;
    }


    public void setTriggerIds(Map<JobKey, Long> triggerIds) {
        this.triggerIds = triggerIds;
    }

    CustomRequest getCustomRequest(String clientId) {
        String plainClientId = "";
        try {
            plainClientId = EncryptionServiceImpl.getInstance().decrypt(clientId);
        } catch (Exception e) {
            logger.error("Unable to decrypt client id");
        }
        return new CustomRequest(plainClientId,plainClientId,"","hybrid");
    }
    
    


}


