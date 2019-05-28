package com.datamodel.anvizent.service.model;

public class QuartzSchedulerInfo {
	private long id;
	private long masterId;
	private String name;
	private String description;
	private String startTime;
	private String shutdownTime;
	private String timezone;
	private String ipAddress;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getShutdownTime() {
		return shutdownTime;
	}
	public void setShutdownTime(String shutdownTime) {
		this.shutdownTime = shutdownTime;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public long getMasterId() {
		return masterId;
	}
	public void setMasterId(long masterId) {
		this.masterId = masterId;
	}
	@Override
	public String toString() {
		return "QuartzSchedulerInfo [id=" + id + ", masterId=" + masterId + ", name=" + name + ", description="
				+ description + ", startTime=" + startTime + ", shutdownTime=" + shutdownTime + ", timezone=" + timezone
				+ ", ipAddress=" + ipAddress + "]";
	}
	public QuartzSchedulerInfo(long id) {
		this.id = id;
	}
	
	public QuartzSchedulerInfo() {
	}
	
	
	
	
	
	
	
}
