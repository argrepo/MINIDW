package com.datamodel.anvizent.service.model;

public class AIJobExecutionInfo{
	
	private Integer runId;
	private String businessProblem;
	private String aiModel;
	private Integer sourceCount;
	private Integer stagingCount;
	private Integer aILCount;
	private Integer aOLCount;
	private String errorMsg;
	private String jobStartTime;
	private String jobEndTime;
	private String jobRunStatus;
	private String jobLogFileName;
	private Modification modification;
	private Long jobDuration;
	public Integer getRunId() {
		return runId;
	}
	public void setRunId(Integer runId) {
		this.runId = runId;
	}
	public String getBusinessProblem() {
		return businessProblem;
	}
	public void setBusinessProblem(String businessProblem) {
		this.businessProblem = businessProblem;
	}
	public String getAiModel() {
		return aiModel;
	}
	public void setAiModel(String aiModel) {
		this.aiModel = aiModel;
	}
	public Integer getSourceCount() {
		return sourceCount;
	}
	public void setSourceCount(Integer sourceCount) {
		this.sourceCount = sourceCount;
	}
	public Integer getStagingCount() {
		return stagingCount;
	}
	public void setStagingCount(Integer stagingCount) {
		this.stagingCount = stagingCount;
	}
	public Integer getaILCount() {
		return aILCount;
	}
	public void setaILCount(Integer aILCount) {
		this.aILCount = aILCount;
	}
	public Integer getaOLCount() {
		return aOLCount;
	}
	public void setaOLCount(Integer aOLCount) {
		this.aOLCount = aOLCount;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getJobStartTime() {
		return jobStartTime;
	}
	public void setJobStartTime(String jobStartTime) {
		this.jobStartTime = jobStartTime;
	}
	public String getJobEndTime() {
		return jobEndTime;
	}
	public void setJobEndTime(String jobEndTime) {
		this.jobEndTime = jobEndTime;
	}
	public String getJobRunStatus() {
		return jobRunStatus;
	}
	public void setJobRunStatus(String jobRunStatus) {
		this.jobRunStatus = jobRunStatus;
	}
	public String getJobLogFileName() {
		return jobLogFileName;
	}
	public void setJobLogFileName(String jobLogFileName) {
		this.jobLogFileName = jobLogFileName;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public Long getJobDuration()
	{
		return jobDuration;
	}
	public void setJobDuration(Long jobDuration)
	{
		this.jobDuration = jobDuration;
	}
	@Override
	public String toString()
	{
		return "AIJobExecutionInfo [runId=" + runId + ", businessProblem=" + businessProblem + ", aiModel=" + aiModel + ", sourceCount=" + sourceCount + ", stagingCount=" + stagingCount + ", aILCount=" + aILCount + ", aOLCount=" + aOLCount + ", errorMsg=" + errorMsg + ", jobStartTime="
				+ jobStartTime + ", jobEndTime=" + jobEndTime + ", jobRunStatus=" + jobRunStatus + ", jobLogFileName=" + jobLogFileName + ", modification=" + modification + ", jobDuration=" + jobDuration + "]";
	}
	
}