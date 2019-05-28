package com.datamodel.anvizent.service.model;


public class JobResult {

	private int packageId;
	private int userId;
	private String batchId;
	private String jobName;
	private String startDate;
	private String endDate;
	private int insertedRecords;
	private int totalRecordsFromSource;
	private String runStatus;
	private int updatedRecords;
	private int errorId;
	private String errorCode;
	private String errorType;
	private String errorMessage;
	private String errorSyntax;
	private String dataSetValue;
	private String errorRowsCount;
	private Integer ilId;
	private long hierarchicalId;
	private Integer scriptId;
	private int xrefConditionId;
	
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
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
	public int getInsertedRecords() {
		return insertedRecords;
	}
	public void setInsertedRecords(int insertedRecords) {
		this.insertedRecords = insertedRecords;
	}
	public int getUpdatedRecords() {
		return updatedRecords;
	}
	public void setUpdatedRecords(int updatedRecords) {
		this.updatedRecords = updatedRecords;
	}

	public int getTotalRecordsFromSource() {
		return totalRecordsFromSource;
	}
	public void setTotalRecordsFromSource(int totalRecordsFromSource) {
		this.totalRecordsFromSource = totalRecordsFromSource;
	}
	public String getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	public int getErrorId() {
		return errorId;
	}
	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorSyntax() {
		return errorSyntax;
	}
	public void setErrorSyntax(String errorSyntax) {
		this.errorSyntax = errorSyntax;
	}
	public String getDataSetValue() {
		return dataSetValue;
	}
	public void setDataSetValue(String dataSetValue) {
		this.dataSetValue = dataSetValue;
	}
	public String getErrorRowsCount() {
		return errorRowsCount;
	}
	public void setErrorRowsCount(String errorRowsCount) {
		this.errorRowsCount = errorRowsCount;
	}
	public Integer getIlId() {
		return ilId;
	}
	public void setIlId(Integer ilId) {
		this.ilId = ilId;
	}
	public long getHierarchicalId() {
		return hierarchicalId;
	}
	public void setHierarchicalId(long hierarchicalId) {
		this.hierarchicalId = hierarchicalId;
	}
	public Integer getScriptId() {
		return scriptId;
	}
	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}
	public int getXrefConditionId()
	{
		return xrefConditionId;
	}
	public void setXrefConditionId(int xrefConditionId)
	{
		this.xrefConditionId = xrefConditionId;
	}
	@Override
	public String toString() {
		return "JobResult [packageId=" + packageId + ", userId=" + userId + ", batchId=" + batchId + ", jobName="
				+ jobName + ", startDate=" + startDate + ", endDate=" + endDate + ", insertedRecords=" + insertedRecords
				+ ", totalRecordsFromSource=" + totalRecordsFromSource + ", runStatus=" + runStatus
				+ ", updatedRecords=" + updatedRecords + ", errorId=" + errorId + ", errorCode=" + errorCode
				+ ", errorType=" + errorType + ", errorMessage=" + errorMessage + ", errorSyntax=" + errorSyntax
				+ ", dataSetValue=" + dataSetValue + ", errorRowsCount=" + errorRowsCount + ", ilId=" + ilId
				+ ", hierarchicalId=" + hierarchicalId + ", scriptId=" + scriptId + ", xrefConditionId="
				+ xrefConditionId + "]";
	}
	
	
}
