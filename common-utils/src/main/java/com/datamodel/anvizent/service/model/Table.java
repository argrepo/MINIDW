/**
 * 
 */
package com.datamodel.anvizent.service.model;

import java.util.List;

/**
 * @author rakesh.gajula
 *
 */
public class Table {

	private Integer tableId;
	private String schemaName;
	private String tableName;
	private List<Column> columns;
	//key : original header name in file or query, value : modified by user
	private List<String> originalColumnNames;
	private List<String> targetTables;
	private Boolean isProcessed;
	private Integer noOfRecordsProcessed;
	private Integer noOfRecordsFailed;
	private Integer duplicateRecords;
	private Integer totalRecords;
	private Boolean isDirect;
	private Boolean isPrimaryTable;
	private String fileName;
	private String tableStructure;
	private Integer ilId;
	private Integer fileId;
	private String filePath;
	private WebServiceJoin webServiceJoin;
	private String ddlTables;
	private Integer wsconnId;;
	
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public Boolean getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	public Integer getNoOfRecordsProcessed() {
		return noOfRecordsProcessed;
	}
	public void setNoOfRecordsProcessed(Integer noOfRecordsProcessed) {
		this.noOfRecordsProcessed = noOfRecordsProcessed;
	}
	public Integer getNoOfRecordsFailed() {
		return noOfRecordsFailed;
	}
	public void setNoOfRecordsFailed(Integer noOfRecordsFailed) {
		this.noOfRecordsFailed = noOfRecordsFailed;
	}
	public Integer getDuplicateRecords() {
		return duplicateRecords;
	}
	public void setDuplicateRecords(Integer duplicateRecords) {
		this.duplicateRecords = duplicateRecords;
	}
	public Integer getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
	public Boolean getIsDirect() {
		return isDirect;
	}
	public void setIsDirect(Boolean isDirect) {
		this.isDirect = isDirect;
	}
	public Boolean getIsPrimaryTable() {
		return isPrimaryTable;
	}
	public void setIsPrimaryTable(Boolean isPrimaryTable) {
		this.isPrimaryTable = isPrimaryTable;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	 
	public List<String> getOriginalColumnNames() {
		return originalColumnNames;
	}
	public void setOriginalColumnNames(List<String> originalColumnNames) {
		this.originalColumnNames = originalColumnNames;
	}
 
	public List<String> getTargetTables() {
		return targetTables;
	}
	public void setTargetTables(List<String> targetTables) {
		this.targetTables = targetTables;
	}
	public String getTableStructure() {
		return tableStructure;
	}
	public void setTableStructure(String tableStructure) {
		this.tableStructure = tableStructure;
	}
 
	public Integer getIlId() {
		return ilId;
	}
	public void setIlId(Integer ilId) {
		this.ilId = ilId;
	}
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public WebServiceJoin getWebServiceJoin() {
		return webServiceJoin;
	}
	public void setWebServiceJoin(WebServiceJoin webServiceJoin) {
		this.webServiceJoin = webServiceJoin;
	}
	public String getDdlTables() {
		return ddlTables;
	}
	public void setDdlTables(String ddlTables) {
		this.ddlTables = ddlTables;
	}
	public Integer getWsconnId() {
		return wsconnId;
	}
	public void setWsconnId(Integer wsconnId) {
		this.wsconnId = wsconnId;
	}
	@Override
	public String toString() {
		return "Table [tableId=" + tableId + ", schemaName=" + schemaName + ", tableName=" + tableName + ", columns="
				+ columns + ", originalColumnNames=" + originalColumnNames + ", targetTables=" + targetTables
				+ ", isProcessed=" + isProcessed + ", noOfRecordsProcessed=" + noOfRecordsProcessed
				+ ", noOfRecordsFailed=" + noOfRecordsFailed + ", duplicateRecords=" + duplicateRecords
				+ ", totalRecords=" + totalRecords + ", isDirect=" + isDirect + ", isPrimaryTable=" + isPrimaryTable
				+ ", fileName=" + fileName + ", tableStructure=" + tableStructure + ", ilId=" + ilId + ", fileId="
				+ fileId + ", filePath=" + filePath + ", webServiceJoin=" + webServiceJoin + ", ddlTables=" + ddlTables
				+ ", wsconnId=" + wsconnId + "]";
	}
	
	
	
	
	
 
	 
}
