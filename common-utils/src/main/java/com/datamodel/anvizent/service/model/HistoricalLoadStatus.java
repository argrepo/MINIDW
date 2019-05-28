package com.datamodel.anvizent.service.model;

public class HistoricalLoadStatus {

	private Integer id;
	private Integer historicalLoadId;
    private String fromDate;
    private String toDate;
    private String startDate;
    private String endDate;
    private Integer loadInterval;
    private String lastUpdatedDate;
    private String s3FileName;
    private Long fileSize;
    private String fileSizeToDisplay;
    private Long sourceRecordsCount;
	private boolean ilExecutionStatus;
    private Modification modification;
    private String comment;
    private String storageType;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getHistoricalLoadId() {
		return historicalLoadId;
	}
	public void setHistoricalLoadId(Integer historicalLoadId) {
		this.historicalLoadId = historicalLoadId;
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
	public Integer getLoadInterval() {
		return loadInterval;
	}
	public void setLoadInterval(Integer loadInterval) {
		this.loadInterval = loadInterval;
	}
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileSizeToDisplay() {
		return fileSizeToDisplay;
	}
	public void setFileSizeToDisplay(String fileSizeToDisplay) {
		this.fileSizeToDisplay = fileSizeToDisplay;
	}
	public Long getSourceRecordsCount() {
		return sourceRecordsCount;
	}
	public void setSourceRecordsCount(Long sourceRecordsCount) {
		this.sourceRecordsCount = sourceRecordsCount;
	}
	public boolean isIlExecutionStatus() {
		return ilExecutionStatus;
	}
	public void setIlExecutionStatus(boolean ilExecutionStatus) {
		this.ilExecutionStatus = ilExecutionStatus;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	
	public String getS3FileName() {
		return s3FileName;
	}
	public void setS3FileName(String s3FileName) {
		this.s3FileName = s3FileName;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

    public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	@Override
	public String toString() {
		return "HistoricalLoadStatus [id=" + id + ", historicalLoadId=" + historicalLoadId + ", fromDate=" + fromDate + ", toDate=" + toDate + ", startDate="
				+ startDate + ", endDate=" + endDate + ", loadInterval=" + loadInterval + ", lastUpdatedDate=" + lastUpdatedDate + ", s3FileName=" + s3FileName
				+ ", fileSize=" + fileSize + ", fileSizeToDisplay=" + fileSizeToDisplay + ", sourceRecordsCount=" + sourceRecordsCount + ", ilExecutionStatus="
				+ ilExecutionStatus + ", modification=" + modification + ", comment=" + comment + ", storageType=" + storageType + "]";
	}
	
	
}
