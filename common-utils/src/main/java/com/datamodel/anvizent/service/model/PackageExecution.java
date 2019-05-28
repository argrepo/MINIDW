package com.datamodel.anvizent.service.model;

import java.util.List;
import java.util.Map;

/**
 * @author mahender.alaveni
 *
 */
public class PackageExecution {
	private long executionId;
	private long packageId;
	private long scheduleId;
	private String initiatedFrom;
	private String runType;
	private String uploadStatus;
	private String uploadComments;
	private String uploadStartDate; 
	private String lastUploadedDate;  
	private String executionStatus;
	private String executionComments;
	private String executionStartDate;
	private String lastExecutedDate;
	private String druidStatus;
	private String druidComments;
	private String druidStartDate;
	private String druidEndDate;
	private String timeZone;
	private long mappingId;
	private boolean jobExecutionRequired;
	private List<String> derivedTablesList;
	private List<Map<String, Object>> incremtalUpdateList;
	private String userId;
	private User user;
	private Package userPackage;
	private Modification modification;
	private String uploadOrExecution;
	private boolean terminateAndStart = false;
	private String clientId;
	private String authenticationEndPointUrl;
	private String alertsThresholdsUrl;
	private Map<String,Object> havingSameColumsResultsMap;
	private String tagetTableName;
	private int incrementalLoadCount;
	private Integer dlId;
	private String ddlToRun;
	private int ilId;
	

	public long getExecutionId() {
		return executionId;
	}

	public void setExecutionId(long executionId) {
		this.executionId = executionId;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getInitiatedFrom() {
		return initiatedFrom;
	}

	public void setInitiatedFrom(String initiatedFrom) {
		this.initiatedFrom = initiatedFrom;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getUploadComments() {
		return uploadComments;
	}

	public void setUploadComments(String uploadComments) {
		this.uploadComments = uploadComments;
	}

	public String getUploadStartDate() {
		return uploadStartDate;
	}

	public void setUploadStartDate(String uploadStartDate) {
		this.uploadStartDate = uploadStartDate;
	}

	public String getLastUploadedDate() {
		return lastUploadedDate;
	}

	public void setLastUploadedDate(String lastUploadedDate) {
		this.lastUploadedDate = lastUploadedDate;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	public String getExecutionComments() {
		return executionComments;
	}

	public void setExecutionComments(String executionComments) {
		this.executionComments = executionComments;
	}

	public String getExecutionStartDate() {
		return executionStartDate;
	}

	public void setExecutionStartDate(String executionStartDate) {
		this.executionStartDate = executionStartDate;
	}

	public String getLastExecutedDate() {
		return lastExecutedDate;
	}

	public void setLastExecutedDate(String lastExecutedDate) {
		this.lastExecutedDate = lastExecutedDate;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public long getMappingId() {
		return mappingId;
	}

	public void setMappingId(long mappingId) {
		this.mappingId = mappingId;
	}

	public boolean isJobExecutionRequired() {
		return jobExecutionRequired;
	}

	public void setJobExecutionRequired(boolean jobExecutionRequired) {
		this.jobExecutionRequired = jobExecutionRequired;
	}

	public Modification getModification() {
		return modification;
	}

	public void setModification(Modification modification) {
		this.modification = modification;
	}

	public List<String> getDerivedTablesList() {
		return derivedTablesList;
	}

	public void setDerivedTablesList(List<String> derivedTablesList) {
		this.derivedTablesList = derivedTablesList;
	}

	public List<Map<String, Object>> getIncremtalUpdateList() {
		return incremtalUpdateList;
	}

	public void setIncremtalUpdateList(List<Map<String, Object>> incremtalUpdateList) {
		this.incremtalUpdateList = incremtalUpdateList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Package getUserPackage() {
		return userPackage;
	}

	public void setUserPackage(Package userPackage) {
		this.userPackage = userPackage;
	}

	public String getUploadOrExecution() {
		return uploadOrExecution;
	}

	public void setUploadOrExecution(String uploadOrExecution) {
		this.uploadOrExecution = uploadOrExecution;
	}

	public boolean isTerminateAndStart() {
		return terminateAndStart;
	}

	public void setTerminateAndStart(boolean terminateAndStart) {
		this.terminateAndStart = terminateAndStart;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getDruidStatus() {
		return druidStatus;
	}

	public void setDruidStatus(String druidStatus) {
		this.druidStatus = druidStatus;
	}

	public String getDruidComments() {
		return druidComments;
	}

	public void setDruidComments(String druidComments) {
		this.druidComments = druidComments;
	}

	public String getDruidStartDate() {
		return druidStartDate;
	}

	public void setDruidStartDate(String druidStartDate) {
		this.druidStartDate = druidStartDate;
	}

	public String getDruidEndDate() {
		return druidEndDate;
	}

	public void setDruidEndDate(String druidEndDate) {
		this.druidEndDate = druidEndDate;
	}

	public String getAuthenticationEndPointUrl() {
		return authenticationEndPointUrl;
	}

	public void setAuthenticationEndPointUrl(String authenticationEndPointUrl) {
		this.authenticationEndPointUrl = authenticationEndPointUrl;
	}

	public String getAlertsThresholdsUrl() {
		return alertsThresholdsUrl;
	}

	public void setAlertsThresholdsUrl(String alertsThresholdsUrl) {
		this.alertsThresholdsUrl = alertsThresholdsUrl;
	}

	 
	public Map<String, Object> getHavingSameColumsResultsMap() {
		return havingSameColumsResultsMap;
	}

	public void setHavingSameColumsResultsMap(Map<String, Object> havingSameColumsResultsMap) {
		this.havingSameColumsResultsMap = havingSameColumsResultsMap;
	}

	public String getTagetTableName() {
		return tagetTableName;
	}

	public void setTagetTableName(String tagetTableName) {
		this.tagetTableName = tagetTableName;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PackageExecution)) {
			return false;
		}
		PackageExecution objPe = (PackageExecution) obj;
		if (this.clientId != null && this.clientId.equals(objPe.getClientId()) 
				&& this.userId != null && this.userId.equals(objPe.getUserId()) 
				&& this.packageId == objPe.packageId 
				&& this.executionId == objPe.executionId
				&& this.dlId.equals(objPe.dlId)
				) {
			return true;
		}
		return false;
	}

	public int getIncrementalLoadCount() {
		return incrementalLoadCount;
	}

	public void setIncrementalLoadCount(int incrementalLoadCount) {
		this.incrementalLoadCount = incrementalLoadCount;
	}
	public Integer getDlId() {
		return dlId;
	}

	public void setDlId(Integer dlId) {
		this.dlId = dlId;
	}

	public String getDdlToRun() {
		return ddlToRun;
	}

	public void setDdlToRun(String ddlToRun) {
		this.ddlToRun = ddlToRun;
	}

	public int getIlId()
	{
		return ilId;
	}

	public void setIlId(int ilId)
	{
		this.ilId = ilId;
	}

	@Override
	public String toString()
	{
		return "PackageExecution [executionId=" + executionId + ", packageId=" + packageId + ", scheduleId=" + scheduleId + ", initiatedFrom=" + initiatedFrom + ", runType=" + runType + ", uploadStatus=" + uploadStatus + ", uploadComments=" + uploadComments + ", uploadStartDate=" + uploadStartDate
				+ ", lastUploadedDate=" + lastUploadedDate + ", executionStatus=" + executionStatus + ", executionComments=" + executionComments + ", executionStartDate=" + executionStartDate + ", lastExecutedDate=" + lastExecutedDate + ", druidStatus=" + druidStatus + ", druidComments="
				+ druidComments + ", druidStartDate=" + druidStartDate + ", druidEndDate=" + druidEndDate + ", timeZone=" + timeZone + ", mappingId=" + mappingId + ", jobExecutionRequired=" + jobExecutionRequired + ", derivedTablesList=" + derivedTablesList + ", incremtalUpdateList="
				+ incremtalUpdateList + ", userId=" + userId + ", user=" + user + ", userPackage=" + userPackage + ", modification=" + modification + ", uploadOrExecution=" + uploadOrExecution + ", terminateAndStart=" + terminateAndStart + ", clientId=" + clientId + ", authenticationEndPointUrl="
				+ authenticationEndPointUrl + ", alertsThresholdsUrl=" + alertsThresholdsUrl + ", havingSameColumsResultsMap=" + havingSameColumsResultsMap + ", tagetTableName=" + tagetTableName + ", incrementalLoadCount=" + incrementalLoadCount + ", dlId=" + dlId + ", ddlToRun=" + ddlToRun
				+ ", ilId=" + ilId + "]";
	}
	
	
 
}
