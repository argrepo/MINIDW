package com.datamodel.anvizent.service.model;

public class QuartzSchedulerTriggerInfo {
	private long triggerId;
	private QuartzSchedulerJobInfo quartzSchedulerJobInfo;
	private String description;
	private String fireTime;
	private String startTime;
	private String endTime;
	private String status;
	public long getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(long triggerId) {
		this.triggerId = triggerId;
	}
	public QuartzSchedulerJobInfo getQuartzSchedulerJobInfo() {
		return quartzSchedulerJobInfo;
	}
	public void setQuartzSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo) {
		this.quartzSchedulerJobInfo = quartzSchedulerJobInfo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFireTime() {
		return fireTime;
	}
	public void setFireTime(String fireTime) {
		this.fireTime = fireTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "QuartzSchedulerTriggerInfo [triggerId=" + triggerId + ", quartzSchedulerJobInfo="
				+ quartzSchedulerJobInfo + ", description=" + description + ", fireTime=" + fireTime + ", startTime="
				+ startTime + ", endTime=" + endTime + ", status=" + status + "]";
	}
	
}
