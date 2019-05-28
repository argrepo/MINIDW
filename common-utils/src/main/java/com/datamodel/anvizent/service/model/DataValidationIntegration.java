package com.datamodel.anvizent.service.model;

public class DataValidationIntegration {
	
	private Integer id;
	private String timezone;
	private String jobName;
	private String jobFileNames;
	private String clientSpecificJobName;
	private String clientSpecificJobfileNames;
	private String startDate;
	private String endDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobFileNames() {
		return jobFileNames;
	}
	public void setJobFileNames(String jobFileNames) {
		this.jobFileNames = jobFileNames;
	}
	public String getClientSpecificJobName() {
		return clientSpecificJobName;
	}
	public void setClientSpecificJobName(String clientSpecificJobName) {
		this.clientSpecificJobName = clientSpecificJobName;
	}
	public String getClientSpecificJobfileNames() {
		return clientSpecificJobfileNames;
	}
	public void setClientSpecificJobfileNames(String clientSpecificJobfileNames) {
		this.clientSpecificJobfileNames = clientSpecificJobfileNames;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "DataValidationIntegration [id=" + id + ", timezone=" + timezone + ", jobName=" + jobName
				+ ", jobFileNames=" + jobFileNames + ", clientSpecificJobName=" + clientSpecificJobName
				+ ", clientSpecificJobfileNames=" + clientSpecificJobfileNames + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}

}
