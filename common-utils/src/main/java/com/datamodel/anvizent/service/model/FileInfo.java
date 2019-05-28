package com.datamodel.anvizent.service.model;

import java.util.List;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class FileInfo {
	
	private String clientid;
	private Integer packageId;
	private Integer fileId;
	private String fileName;
	private String filePath;
	private String fileStatus;
	private String fileType;
	private String delimeter;
	private Boolean isFirstRowHasColumnNames;
	private String fileHeaders;
	private String selectedFileHeaders;
	private Boolean filesHavingSameColumns;
	private Modification modification;
	private Boolean isTempTableCreated;
	private int ilMappingId;
	private String fileColumnDataTypes;
	private List<String> headers;
	private List<String> columnDatatypes;
	private Integer sourceFileInfoId;
	

	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getDelimeter() {
		return delimeter;
	}
	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}
	public Boolean getIsFirstRowHasColumnNames() {
		return isFirstRowHasColumnNames;
	}
	public void setIsFirstRowHasColumnNames(Boolean isFirstRowHasColumnNames) {
		this.isFirstRowHasColumnNames = isFirstRowHasColumnNames;
	}
	public String getFileHeaders() {
		return fileHeaders;
	}
	public void setFileHeaders(String fileHeaders) {
		this.fileHeaders = fileHeaders;
	}
	public String getSelectedFileHeaders() {
		return selectedFileHeaders;
	}
	public void setSelectedFileHeaders(String selectedFileHeaders) {
		this.selectedFileHeaders = selectedFileHeaders;
	}
	
	public Boolean getFilesHavingSameColumns() {
		return filesHavingSameColumns;
	}
	public void setFilesHavingSameColumns(Boolean filesHavingSameColumns) {
		this.filesHavingSameColumns = filesHavingSameColumns;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public Boolean getIsTempTableCreated() {
		return isTempTableCreated;
	}
	public void setIsTempTableCreated(Boolean isTempTableCreated) {
		this.isTempTableCreated = isTempTableCreated;
	}
	public int getIlMappingId() {
		return ilMappingId;
	}
	public void setIlMappingId(int ilMappingId) {
		this.ilMappingId = ilMappingId;
	}	
	public String getFileColumnDataTypes() {
		return fileColumnDataTypes;
	}
	public void setFileColumnDataTypes(String fileColumnDataTypes) {
		this.fileColumnDataTypes = fileColumnDataTypes;
	}
	public List<String> getHeaders() {
		return headers;
	}
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
	public List<String> getColumnDatatypes() {
		return columnDatatypes;
	}
	public void setColumnDatatypes(List<String> columnDatatypes) {
		this.columnDatatypes = columnDatatypes;
	}
	public Integer getSourceFileInfoId() {
		return sourceFileInfoId;
	}
	public void setSourceFileInfoId(Integer sourceFileInfoId) {
		this.sourceFileInfoId = sourceFileInfoId;
	}
	@Override
	public String toString() {
		return "FileInfo [clientid=" + clientid + ", packageId=" + packageId + ", fileId=" + fileId + ", fileName="
				+ fileName + ", filePath=" + filePath + ", fileStatus=" + fileStatus + ", fileType=" + fileType
				+ ", delimeter=" + delimeter + ", isFirstRowHasColumnNames=" + isFirstRowHasColumnNames
				+ ", fileHeaders=" + fileHeaders + ", selectedFileHeaders=" + selectedFileHeaders
				+ ", filesHavingSameColumns=" + filesHavingSameColumns + ", modification=" + modification
				+ ", isTempTableCreated=" + isTempTableCreated + ", ilMappingId=" + ilMappingId
				+ ", fileColumnDataTypes=" + fileColumnDataTypes + ", headers=" + headers + ", columnDatatypes="
				+ columnDatatypes + ", sourceFileInfoId=" + sourceFileInfoId + "]";
	}
}
