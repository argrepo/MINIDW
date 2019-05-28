package com.datamodel.anvizent.service.model;

public class JobExecutionInfo {
	private String jobId;
	private String jobName;
	private String jobClass;
	private String dependencyJars;
	private String executionMessages;
	private String s3Path;
	private Integer statusCode;
    private Modification modification;
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	public String getDependencyJars() {
		return dependencyJars;
	}
	public void setDependencyJars(String dependencyJars) {
		this.dependencyJars = dependencyJars;
	}
	public String getExecutionMessages() {
		return executionMessages;
	}
	public void setExecutionMessages(String executionMessages) {
		this.executionMessages = executionMessages;
	}
	public String getS3Path() {
		return s3Path;
	}
	public void setS3Path(String s3Path) {
		this.s3Path = s3Path;
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	@Override
	public String toString() {
		return "JobExecutionInfo [jobId=" + jobId + ", jobName=" + jobName + ", jobClass=" + jobClass
				+ ", dependencyJars=" + dependencyJars + ", executionMessages=" + executionMessages + ", s3Path="
				+ s3Path + ", statusCode=" + statusCode + ", modification=" + modification + "]";
	}
 
	
}
