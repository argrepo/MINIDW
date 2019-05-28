package com.datamodel.anvizent.service.model;



public class SchedulerClientConfig {
	private int threadCount;
	private String threadPriority;
	private String cronExpression;
	public int getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	public String getThreadPriority() {
		return threadPriority;
	}
	public void setThreadPriority(String threadPriority) {
		this.threadPriority = threadPriority;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	@Override
	public String toString() {
		return "SchedulerClientConfig [threadCount=" + threadCount + ", threadPriority=" + threadPriority
				+ ", cronExpression=" + cronExpression + "]";
	}
	
	
	
}
