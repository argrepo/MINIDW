package com.datamodel.anvizent.service.model;

public class SchedulerSlave {
	private long id;
	private String name;
	private int type;
	private String instanceId;
	private AwsCredentials aws;
	private String ipAddress;
	private int packageExecutionCount;
	private int fileUploadCount;
	private int historyLoadCount;
	private int historyExecutionCount;
	private boolean active;
	
	private Boolean serverState;
	private Boolean schdRunningState;
	private String stateMsg;
	private Boolean isCheckingCompleted;
	private boolean available;
	private String lastUpdatedDate;
	private String requestSchema;
	
	
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public AwsCredentials getAws() {
		return aws;
	}
	public void setAws(AwsCredentials aws) {
		this.aws = aws;
	}
	public int getPackageExecutionCount() {
		return packageExecutionCount;
	}
	public void setPackageExecutionCount(int packageExecutionCount) {
		this.packageExecutionCount = packageExecutionCount;
	}
	public int getFileUploadCount() {
		return fileUploadCount;
	}
	public void setFileUploadCount(int fileUploadCount) {
		this.fileUploadCount = fileUploadCount;
	}
	public int getHistoryLoadCount() {
		return historyLoadCount;
	}
	public void setHistoryLoadCount(int historyLoadCount) {
		this.historyLoadCount = historyLoadCount;
	}
	public int getHistoryExecutionCount() {
		return historyExecutionCount;
	}
	public void setHistoryExecutionCount(int historyExecutionCount) {
		this.historyExecutionCount = historyExecutionCount;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Boolean getServerState() {
		return serverState;
	}
	public void setServerState(Boolean serverState) {
		this.serverState = serverState;
	}
	public Boolean getSchdRunningState() {
		return schdRunningState;
	}
	public void setSchdRunningState(Boolean schdRunningState) {
		this.schdRunningState = schdRunningState;
	}
	public String getStateMsg() {
		return stateMsg;
	}
	public void setStateMsg(String stateMsg) {
		this.stateMsg = stateMsg;
	}
	public Boolean getIsCheckingCompleted() {
		return isCheckingCompleted;
	}
	public void setIsCheckingCompleted(Boolean isCheckingCompleted) {
		this.isCheckingCompleted = isCheckingCompleted;
	}

	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getRequestSchema() {
		return requestSchema;
	}
	public void setRequestSchema(String requestSchema) {
		this.requestSchema = requestSchema;
	}
	@Override
	public String toString() {
		return "SchedulerSlave [id=" + id + ", name=" + name + ", type=" + type + ", instanceId=" + instanceId
				+ ", aws=" + aws + ", ipAddress=" + ipAddress + ", packageExecutionCount=" + packageExecutionCount
				+ ", fileUploadCount=" + fileUploadCount + ", historyLoadCount=" + historyLoadCount
				+ ", historyExecutionCount=" + historyExecutionCount + ", active=" + active + ", serverState="
				+ serverState + ", schdRunningState=" + schdRunningState + ", stateMsg=" + stateMsg
				+ ", isCheckingCompleted=" + isCheckingCompleted + ", available=" + available + ", lastUpdatedDate="
				+ lastUpdatedDate + ", requestSchema=" + requestSchema + "]";
	}
	
		
	
}
