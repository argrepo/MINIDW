package com.datamodel.anvizent.service.model;

import java.util.List;

public class HistoricalLoadForm {

	private Integer id;
	private List<Integer> ids;
	private Integer ilId;
    private	Integer connectorId;
    private String historicalFromDate;
    private String historicalToDate;
    private String startDate;
    private String endDate;
    private Integer loadInterval;
    private String histiricalQueryScript;
    private Integer clientId;
    private String pageMode;
    private String IlName;
    private String connectorName;
	private boolean isRunning;
	private boolean isExecuted;
	private String historicalLastUpdatedTime;
    private ILConnection ilConnection;
    private String dataSourceName;
	private String dataSourceNameOther;
	private boolean multipartEnabled;
	private long noOfRecordsPerFile;
    
	public Integer getIlId() {
		return ilId;
	}
	public void setIlId(Integer ilId) {
		this.ilId = ilId;
	}
	public Integer getConnectorId() {
		return connectorId;
	}
	public void setConnectorId(Integer connectorId) {
		this.connectorId = connectorId;
	}
	public String getHistoricalFromDate() {
		return historicalFromDate;
	}
	public void setHistoricalFromDate(String historicalFromDate) {
		this.historicalFromDate = historicalFromDate;
	}
	public String getHistoricalToDate() {
		return historicalToDate;
	}
	public void setHistoricalToDate(String historicalToDate) {
		this.historicalToDate = historicalToDate;
	}
	public Integer getLoadInterval() {
		return loadInterval;
	}
	public void setLoadInterval(Integer loadInterval) {
		this.loadInterval = loadInterval;
	}
	public String getHistiricalQueryScript() {
		return histiricalQueryScript;
	}
	public void setHistiricalQueryScript(String histiricalQueryScript) {
		this.histiricalQueryScript = histiricalQueryScript;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getPageMode() {
		return pageMode;
	}
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	public String getIlName() {
		return IlName;
	}
	public void setIlName(String ilName) {
		IlName = ilName;
	}
	public boolean isExecuted() {
		return isExecuted;
	}
	public void setExecuted(boolean isExecuted) {
		this.isExecuted = isExecuted;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<Integer> getIds() {
		return ids;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public String getConnectorName() {
		return connectorName;
	}
	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}
	
	public String getHistoricalLastUpdatedTime() {
		return historicalLastUpdatedTime;
	}
	public void setHistoricalLastUpdatedTime(String historicalLastUpdatedTime) {
		this.historicalLastUpdatedTime = historicalLastUpdatedTime;
	}
	
	public ILConnection getIlConnection() {
		return ilConnection;
	}
	public void setIlConnection(ILConnection ilConnection) {
		this.ilConnection = ilConnection;
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
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getDataSourceNameOther() {
		return dataSourceNameOther;
	}
	public void setDataSourceNameOther(String dataSourceNameOther) {
		this.dataSourceNameOther = dataSourceNameOther;
	}
	public boolean isMultipartEnabled() {
		return multipartEnabled;
	}
	public void setMultipartEnabled(boolean multipartEnabled) {
		this.multipartEnabled = multipartEnabled;
	}
	public long getNoOfRecordsPerFile() {
		return noOfRecordsPerFile;
	}
	public void setNoOfRecordsPerFile(long noOfRecordsPerFile) {
		this.noOfRecordsPerFile = noOfRecordsPerFile;
	}
	@Override
	public String toString() {
		return "HistoricalLoadForm [id=" + id + ", ids=" + ids + ", ilId=" + ilId + ", connectorId=" + connectorId
				+ ", historicalFromDate=" + historicalFromDate + ", historicalToDate=" + historicalToDate
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", loadInterval=" + loadInterval
				+ ", histiricalQueryScript=" + histiricalQueryScript + ", clientId=" + clientId + ", pageMode="
				+ pageMode + ", IlName=" + IlName + ", connectorName=" + connectorName + ", isRunning=" + isRunning
				+ ", isExecuted=" + isExecuted + ", historicalLastUpdatedTime=" + historicalLastUpdatedTime
				+ ", ilConnection=" + ilConnection + ", dataSourceName=" + dataSourceName + ", dataSourceNameOther="
				+ dataSourceNameOther + ", multipartEnabled=" + multipartEnabled + ", noOfRecordsPerFile="
				+ noOfRecordsPerFile + "]";
	}
 	 
}
