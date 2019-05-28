package com.datamodel.anvizent.service.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class IlInfoForm {
	private Integer iLId;
	private String iLName;
	private String iLType;
	private String iLDescription;
	private String iLTableName;
	private String xrefILTableName;
	private String jobName;
	private String iLPurgeScript;
	private List<String> contextParamsList;
	private String version;
	private boolean active;
	private String uploadedJobFileNames;
	private List<MultipartFile> jobFile;
	private String pageMode;
	private List<String> jobFileNames;
	private String jobExecutionType;
	private long jobTagId;
	private long masterParameterId;
	private long loadParameterId;
	
	public List<String> getJobFileNames() {
		return jobFileNames;
	}
	public void setJobFileNames(List<String> jobFileNames) {
		this.jobFileNames = jobFileNames;
	}
	public Integer getiLId() {
		return iLId;
	}
	public void setiLId(Integer iLId) {
		this.iLId = iLId;
	}
	public String getiLName() {
		return iLName;
	}
	public void setiLName(String iLName) {
		this.iLName = iLName;
	}
	public String getiLType() {
		return iLType;
	}
	public void setiLType(String iLType) {
		this.iLType = iLType;
	}
	public String getiLDescription() {
		return iLDescription;
	}
	public void setiLDescription(String iLDescription) {
		this.iLDescription = iLDescription;
	}
	public String getiLTableName() {
		return iLTableName;
	}
	public void setiLTableName(String iLTableName) {
		this.iLTableName = iLTableName;
	}
	public String getXrefILTableName() {
		return xrefILTableName;
	}
	public void setXrefILTableName(String xrefILTableName) {
		this.xrefILTableName = xrefILTableName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getiLPurgeScript() {
		return iLPurgeScript;
	}
	public void setiLPurgeScript(String iLPurgeScript) {
		this.iLPurgeScript = iLPurgeScript;
	}
	public List<String> getContextParamsList() {
		return contextParamsList;
	}
	public void setContextParamsList(List<String> contextParamsList) {
		this.contextParamsList = contextParamsList;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean isActive) {
		this.active = isActive;
	}
	public String getUploadedJobFileNames() {
		return uploadedJobFileNames;
	}
	public void setUploadedJobFileNames(String uploadedJobFileNames) {
		this.uploadedJobFileNames = uploadedJobFileNames;
	}
	public List<MultipartFile> getJobFile() {
		return jobFile;
	}
	public void setJobFile(List<MultipartFile> jobFile) {
		this.jobFile = jobFile;
	}
	public String getPageMode() {
		return pageMode;
	}
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	public String getJobExecutionType() {
		return jobExecutionType;
	}
	public void setJobExecutionType(String jobExecutionType) {
		this.jobExecutionType = jobExecutionType;
	}
	public long getJobTagId() {
		return jobTagId;
	}
	public void setJobTagId(long jobTagId) {
		this.jobTagId = jobTagId;
	}
	public long getMasterParameterId() {
		return masterParameterId;
	}
	public void setMasterParameterId(long masterParameterId) {
		this.masterParameterId = masterParameterId;
	}
	public long getLoadParameterId() {
		return loadParameterId;
	}
	public void setLoadParameterId(long loadParameterId) {
		this.loadParameterId = loadParameterId;
	}
	
}
