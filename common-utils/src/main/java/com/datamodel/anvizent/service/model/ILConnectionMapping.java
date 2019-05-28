/**
 * 
 */
package com.datamodel.anvizent.service.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author rakesh.gajula
 *
 */
public class ILConnectionMapping {

	private Integer connectionMappingId;
	private Boolean isFlatFile;
	private String originalFileName;
	private String fileType;
	private String filePath;
	private String delimeter;
	private Boolean isFirstRowHasColoumnNames;
	private String typeOfCommand;
	private String iLquery;
	private String databaseName;
	private String procedureParameters;
	private Integer iLId;
	private Integer dLId;
	private Integer packageId;
	private Integer targetTableId;
	private String clientId;
	private ILConnection iLConnection;
	private Boolean isMapped;
	private Boolean isDLMapped;
	private String iLJobStatusForRunNow;
	private MultipartFile multipartFile;
	private Modification modification;
	private Boolean isHavingParentTable;
	private String parent_table_name;
	private List<String> ilquery;
	private List<Integer> connectionMappingIds;	
	private Boolean isIncrementalUpdate;
	private String ilSourceName;
	private Boolean isWebservice;
	private String webserviceMappingHeaders;
	private int webserviceId;
	private String iLIncrementalUpdateQuery;
	private String historicalLoad;
	private Boolean isHistoricalLoad=false;
	private String historicalFromDate;
	private String historicalToDate;
	private Integer loadInterval;
	private String historicalLastUpdatedTime;
	private boolean joinWebService=false;
	private String webserviceJoinUrls;
	private Integer wsConId;
	private WebService webService;
	private String incrementalDateValue;
	private String fileSize;
	private int rowCount;
	private String maxDateQuery;
	private String storageType;
	private String dataSourceNameOther;
	private long s3BucketId;
	private boolean multipartEnabled;
	private long sourceFileInfoId;
    private long executionMappingId;
    private long packageExecutionId;
	private String deploymentType;
    private String timeZone;
    private boolean isActiveRequired;
    private boolean encryptionRequired;
	private int wsConnectionRequestTimeout;
	private int wsConnectTimeout;
	private int wsReadTimeout;
	
	public String getiLIncrementalUpdateQuery() {
		return iLIncrementalUpdateQuery;
	}
	public void setiLIncrementalUpdateQuery(String iLIncrementalUpdateQuery) {
		this.iLIncrementalUpdateQuery = iLIncrementalUpdateQuery;
	}
	public WebService getWebService() {
		return webService;
	}
	public void setWebService(WebService webService) {
		this.webService = webService;
	}
	 
	public Boolean getIsWebservice() {
		return isWebservice;
	}
	public void setIsWebservice(Boolean isWebservice) {
		this.isWebservice = isWebservice;
	}
	public String getWebserviceMappingHeaders() {
		return webserviceMappingHeaders;
	}
	public void setWebserviceMappingHeaders(String webserviceMappingHeaders) {
		this.webserviceMappingHeaders = webserviceMappingHeaders;
	}
	public int getWebserviceId() {
		return webserviceId;
	}
	public void setWebserviceId(int webserviceId) {
		this.webserviceId = webserviceId;
	}
	public Integer getConnectionMappingId() {
		return connectionMappingId;
	}
	public void setConnectionMappingId(Integer connectionMappingId) {
		this.connectionMappingId = connectionMappingId;
	}
	public Boolean getIsFlatFile() {
		return isFlatFile;
	}
	public void setIsFlatFile(Boolean isFlatFile) {
		this.isFlatFile = isFlatFile;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDelimeter() {
		return delimeter;
	}
	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}
	public Boolean getIsFirstRowHasColoumnNames() {
		return isFirstRowHasColoumnNames;
	}
	public void setIsFirstRowHasColoumnNames(Boolean isFirstRowHasColoumnNames) {
		this.isFirstRowHasColoumnNames = isFirstRowHasColoumnNames;
	}
	public String getTypeOfCommand() {
		return typeOfCommand;
	}
	public void setTypeOfCommand(String typeOfCommand) {
		this.typeOfCommand = typeOfCommand;
	}
	public String getiLquery() {
		return iLquery;
	}
	public void setiLquery(String iLquery) {
		this.iLquery = iLquery;
	}
	public String getProcedureParameters() {
		return procedureParameters;
	}
	public void setProcedureParameters(String procedureParameters) {
		this.procedureParameters = procedureParameters;
	}
	public Integer getiLId() {
		return iLId;
	}
	public void setiLId(Integer iLId) {
		this.iLId = iLId;
	}
	public Integer getdLId() {
		return dLId;
	}
	public void setdLId(Integer dLId) {
		this.dLId = dLId;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public ILConnection getiLConnection() {
		return iLConnection;
	}
	public void setiLConnection(ILConnection iLConnection) {
		this.iLConnection = iLConnection;
	}
	public Boolean getIsMapped() {
		return isMapped;
	}
	public void setIsMapped(Boolean isMapped) {
		this.isMapped = isMapped;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public Integer getTargetTableId() {
		return targetTableId;
	}
	public void setTargetTableId(Integer targetTableId) {
		this.targetTableId = targetTableId;
	}
	public Boolean getIsDLMapped() {
		return isDLMapped;
	}
	public void setIsDLMapped(Boolean isDLMapped) {
		this.isDLMapped = isDLMapped;
	}
	public String getiLJobStatusForRunNow() {
		return iLJobStatusForRunNow;
	}
	public void setiLJobStatusForRunNow(String iLJobStatusForRunNow) {
		this.iLJobStatusForRunNow = iLJobStatusForRunNow;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}
	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}
	public Boolean getIsHavingParentTable() {
		return isHavingParentTable;
	}
	public void setIsHavingParentTable(Boolean isHavingParentTable) {
		this.isHavingParentTable = isHavingParentTable;
	}
	public String getParent_table_name() {
		return parent_table_name;
	}
	public void setParent_table_name(String parent_table_name) {
		this.parent_table_name = parent_table_name;
	}
	public List<String> getIlquery() {
		return ilquery;
	}
	public void setIlquery(List<String> ilquery) {
		this.ilquery = ilquery;
	}		 	 
	public List<Integer> getConnectionMappingIds() {
		return connectionMappingIds;
	}
	public void setConnectionMappingIds(List<Integer> connectionMappingIds) {
		this.connectionMappingIds = connectionMappingIds;
	}
	public Boolean getIsIncrementalUpdate() {
		return isIncrementalUpdate;
	}
	public void setIsIncrementalUpdate(Boolean isIncrementalUpdate) {
		this.isIncrementalUpdate = isIncrementalUpdate;
	}
	public String getIlSourceName() {
		return ilSourceName;
	}
	public void setIlSourceName(String ilSourceName) {
		this.ilSourceName = ilSourceName;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getHistoricalLoad() {
		return historicalLoad;
	}
	public void setHistoricalLoad(String historicalLoad) {
		this.historicalLoad = historicalLoad;
	}
	public Boolean getIsHistoricalLoad() {
		return isHistoricalLoad;
	}
	public void setIsHistoricalLoad(Boolean isHistoricalLoad) {
		this.isHistoricalLoad = isHistoricalLoad;
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
	public String getHistoricalLastUpdatedTime() {
		return historicalLastUpdatedTime;
	}
	public void setHistoricalLastUpdatedTime(String historicalLastUpdatedTime) {
		this.historicalLastUpdatedTime = historicalLastUpdatedTime;
	}
	public boolean isJoinWebService() {
		return joinWebService;
	}
	public void setJoinWebService(boolean joinWebService) {
		this.joinWebService = joinWebService;
	}
	public String getWebserviceJoinUrls() {
		return webserviceJoinUrls;
	}
	public void setWebserviceJoinUrls(String webserviceJoinUrls) {
		this.webserviceJoinUrls = webserviceJoinUrls;
	}
	public Integer getWsConId() {
		return wsConId;
	}
	public void setWsConId(Integer wsConId) {
		this.wsConId = wsConId;
	}
	public String getIncrementalDateValue() {
		return incrementalDateValue;
	}
	public void setIncrementalDateValue(String incrementalDateValue) {
		this.incrementalDateValue = incrementalDateValue;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public String getMaxDateQuery() {
		return maxDateQuery;
	}
	public void setMaxDateQuery(String maxDateQuery) {
		this.maxDateQuery = maxDateQuery;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public String getDataSourceNameOther() {
		return dataSourceNameOther;
	}
	public void setDataSourceNameOther(String dataSourceNameOther) {
		this.dataSourceNameOther = dataSourceNameOther;
	}
	public long getS3BucketId() {
		return s3BucketId;
	}
	public void setS3BucketId(long s3BucketId) {
		this.s3BucketId = s3BucketId;
	}
	public boolean isMultipartEnabled() {
		return multipartEnabled;
	}
	public void setMultipartEnabled(boolean multipartEnabled) {
		this.multipartEnabled = multipartEnabled;
	}
	public long getSourceFileInfoId() {
		return sourceFileInfoId;
	}
	public void setSourceFileInfoId(long sourceFileInfoId) {
		this.sourceFileInfoId = sourceFileInfoId;
	}
	public long getExecutionMappingId() {
		return executionMappingId;
	}
	public void setExecutionMappingId(long executionMappingId) {
		this.executionMappingId = executionMappingId;
	}
	public long getPackageExecutionId() {
		return packageExecutionId;
	}
	public void setPackageExecutionId(long packageExecutionId) {
		this.packageExecutionId = packageExecutionId;
	}
	public String getDeploymentType() {
		return deploymentType;
	}
	public void setDeploymentType(String deploymentType) {
		this.deploymentType = deploymentType;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	public boolean isActiveRequired() {
		return isActiveRequired;
	}
	public void setActiveRequired(boolean isActiveRequired) {
		this.isActiveRequired = isActiveRequired;
	}
	public boolean isEncryptionRequired() {
		return encryptionRequired;
	}
	public void setEncryptionRequired(boolean encryptionRequired) {
		this.encryptionRequired = encryptionRequired;
	}
	public int getWsConnectionRequestTimeout()
	{
		return wsConnectionRequestTimeout;
	}
	public void setWsConnectionRequestTimeout(int wsConnectionRequestTimeout)
	{
		this.wsConnectionRequestTimeout = wsConnectionRequestTimeout;
	}
	public int getWsConnectTimeout()
	{
		return wsConnectTimeout;
	}
	public void setWsConnectTimeout(int wsConnectTimeout)
	{
		this.wsConnectTimeout = wsConnectTimeout;
	}
	public int getWsReadTimeout()
	{
		return wsReadTimeout;
	}
	public void setWsReadTimeout(int wsReadTimeout)
	{
		this.wsReadTimeout = wsReadTimeout;
	}
	@Override
	public String toString()
	{
		return "ILConnectionMapping [connectionMappingId=" + connectionMappingId + ", isFlatFile=" + isFlatFile + ", originalFileName=" + originalFileName + ", fileType=" + fileType + ", filePath=" + filePath + ", delimeter=" + delimeter + ", isFirstRowHasColoumnNames=" + isFirstRowHasColoumnNames
				+ ", typeOfCommand=" + typeOfCommand + ", iLquery=" + iLquery + ", databaseName=" + databaseName + ", procedureParameters=" + procedureParameters + ", iLId=" + iLId + ", dLId=" + dLId + ", packageId=" + packageId + ", targetTableId=" + targetTableId + ", clientId=" + clientId
				+ ", iLConnection=" + iLConnection + ", isMapped=" + isMapped + ", isDLMapped=" + isDLMapped + ", iLJobStatusForRunNow=" + iLJobStatusForRunNow + ", multipartFile=" + multipartFile + ", modification=" + modification + ", isHavingParentTable=" + isHavingParentTable
				+ ", parent_table_name=" + parent_table_name + ", ilquery=" + ilquery + ", connectionMappingIds=" + connectionMappingIds + ", isIncrementalUpdate=" + isIncrementalUpdate + ", ilSourceName=" + ilSourceName + ", isWebservice=" + isWebservice + ", webserviceMappingHeaders="
				+ webserviceMappingHeaders + ", webserviceId=" + webserviceId + ", iLIncrementalUpdateQuery=" + iLIncrementalUpdateQuery + ", historicalLoad=" + historicalLoad + ", isHistoricalLoad=" + isHistoricalLoad + ", historicalFromDate=" + historicalFromDate + ", historicalToDate="
				+ historicalToDate + ", loadInterval=" + loadInterval + ", historicalLastUpdatedTime=" + historicalLastUpdatedTime + ", joinWebService=" + joinWebService + ", webserviceJoinUrls=" + webserviceJoinUrls + ", wsConId=" + wsConId + ", webService=" + webService + ", incrementalDateValue="
				+ incrementalDateValue + ", fileSize=" + fileSize + ", rowCount=" + rowCount + ", maxDateQuery=" + maxDateQuery + ", storageType=" + storageType + ", dataSourceNameOther=" + dataSourceNameOther + ", s3BucketId=" + s3BucketId + ", multipartEnabled=" + multipartEnabled
				+ ", sourceFileInfoId=" + sourceFileInfoId + ", executionMappingId=" + executionMappingId + ", packageExecutionId=" + packageExecutionId + ", deploymentType=" + deploymentType + ", timeZone=" + timeZone + ", isActiveRequired=" + isActiveRequired + ", encryptionRequired="
				+ encryptionRequired + ", wsConnectionRequestTimeout=" + wsConnectionRequestTimeout + ", wsConnectTimeout=" + wsConnectTimeout + ", wsReadTimeout=" + wsReadTimeout + "]";
	}
	 
}
