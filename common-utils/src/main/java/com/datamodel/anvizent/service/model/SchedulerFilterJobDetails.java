package com.datamodel.anvizent.service.model;

import java.util.List;

public class SchedulerFilterJobDetails {
	private List<Long> schedulerId; 
	private List<String> status;
	private List<String> groupNames;
	private String fromDate;
	private String toDate;
	private String timeZone;
	
	private SchedulerMaster schedulerMaster;
	public List<Long> getSchedulerId() {
		return schedulerId;
	}
	public void setSchedulerId(List<Long> schedulerId) {
		this.schedulerId = schedulerId;
	}
	public List<String> getStatus() {
		return status;
	}
	public void setStatus(List<String> status) {
		this.status = status;
	}
	public List<String> getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(List<String> groupNames) {
		this.groupNames = groupNames;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public SchedulerMaster getSchedulerMaster() {
		return schedulerMaster;
	}
	public void setSchedulerMaster(SchedulerMaster schedulerMaster) {
		this.schedulerMaster = schedulerMaster;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	@Override
	public String toString() {
		return "SchedulerFilterJobDetails [schedulerId=" + schedulerId + ", status=" + status + ", groupNames="
				+ groupNames + ", fromDate=" + fromDate + ", toDate=" + toDate + ", timeZone=" + timeZone
				+ ", schedulerMaster=" + schedulerMaster + "]";
	}
	
}
