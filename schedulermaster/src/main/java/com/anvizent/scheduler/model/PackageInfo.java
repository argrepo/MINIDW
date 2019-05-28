package com.anvizent.scheduler.model;

public class PackageInfo {
	private Long scheduleId;
	private Long packageId;
	private Long userId;
	private Long clientId;

	public PackageInfo(Long scheduleId, Long packageId, Long userId, Long clientIdId) {
		this.scheduleId = scheduleId;
		this.packageId = packageId;
		this.userId = userId;
		this.clientId = clientIdId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getClientId() {
		return clientId;
	}
	
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "PackageInfo [scheduleId=" + scheduleId + ", packageId=" + packageId + ", userId=" + userId + ", clientIdId=" + clientId + "]";
	}

}
