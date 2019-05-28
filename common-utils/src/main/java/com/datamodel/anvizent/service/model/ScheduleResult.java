package com.datamodel.anvizent.service.model;

/**
 * @author Rajesh.anthari
 *
 */
public class ScheduleResult {

	private int count;
	private String scheduleStartTime;
	private String clientSchedulerStatus;
	private String serverSchedulerStatus;
	private String clientSchedulerStatusDetails;
	private String serverSchedulerStatusDetails;
	private String dlName;
	private String ilName;
	private int id;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getScheduleStartTime() {
		return scheduleStartTime;
	}
	public void setScheduleStartTime(String scheduleStartTime) {
		this.scheduleStartTime = scheduleStartTime;
	}
	public String getClientSchedulerStatus() {
		return clientSchedulerStatus;
	}
	public void setClientSchedulerStatus(String clientSchedulerStatus) {
		this.clientSchedulerStatus = clientSchedulerStatus;
	}
	public String getServerSchedulerStatus() {
		return serverSchedulerStatus;
	}
	public void setServerSchedulerStatus(String serverSchedulerStatus) {
		this.serverSchedulerStatus = serverSchedulerStatus;
	}
	public String getClientSchedulerStatusDetails() {
		return clientSchedulerStatusDetails;
	}
	public void setClientSchedulerStatusDetails(String clientSchedulerStatusDetails) {
		this.clientSchedulerStatusDetails = clientSchedulerStatusDetails;
	}
	public String getServerSchedulerStatusDetails() {
		return serverSchedulerStatusDetails;
	}
	public void setServerSchedulerStatusDetails(String serverSchedulerStatusDetails) {
		this.serverSchedulerStatusDetails = serverSchedulerStatusDetails;
	}
	public String getDlName() {
		return dlName;
	}
	public void setDlName(String dlName) {
		this.dlName = dlName;
	}
	public String getIlName() {
		return ilName;
	}
	public void setIlName(String ilName) {
		this.ilName = ilName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "ScheduleResult [count=" + count + ", scheduleStartTime=" + scheduleStartTime
				+ ", clientSchedulerStatus=" + clientSchedulerStatus + ", serverSchedulerStatus="
				+ serverSchedulerStatus + ", clientSchedulerStatusDetails=" + clientSchedulerStatusDetails
				+ ", serverSchedulerStatusDetails=" + serverSchedulerStatusDetails + ", dlName=" + dlName + ", ilName="
				+ ilName + ", id=" + id + "]";
	}
	
	
	
	
}
