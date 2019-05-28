package com.datamodel.anvizent.service.model;

import java.util.List;

public class DataValidationType{
	
	private int validationTypeId;
	private String validationTypeName;
	private String validationTypeDesc;
	private String validationName;
	private int validationId;
	private boolean active;
	private String jobName;
	private String dependencyJars;
	private List<String> contextParamList;
	private Modification modification;
	
	public int getValidationTypeId()
	{
		return validationTypeId;
	}
	public void setValidationTypeId(int validationTypeId)
	{
		this.validationTypeId = validationTypeId;
	}
	public String getValidationTypeName()
	{
		return validationTypeName;
	}
	public void setValidationTypeName(String validationTypeName)
	{
		this.validationTypeName = validationTypeName;
	}
	public String getValidationTypeDesc()
	{
		return validationTypeDesc;
	}
	public void setValidationTypeDesc(String validationTypeDesc)
	{
		this.validationTypeDesc = validationTypeDesc;
	}
	public boolean isActive()
	{
		return active;
	}
	public void setActive(boolean active)
	{
		this.active = active;
	}
	public String getValidationName() {
		return validationName;
	}
	public void setValidationName(String validationName) {
		this.validationName = validationName;
	}
	public void setValidationId(int validationId) {
		this.validationId = validationId;
	}
	
	public int getValidationId() {
		return validationId;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getDependencyJars() {
		return dependencyJars;
	}
	public void setDependencyJars(String dependencyJars) {
		this.dependencyJars = dependencyJars;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	
	public List<String> getContextParamList() {
		return contextParamList;
	}
	public void setContextParamList(List<String> contextParamList) {
		this.contextParamList = contextParamList;
	}
	@Override
	public String toString() {
		return "DataValidationType [validationTypeId=" + validationTypeId + ", validationTypeName=" + validationTypeName
				+ ", validationTypeDesc=" + validationTypeDesc + ", validationName=" + validationName
				+ ", validationId=" + validationId + ", active=" + active + ", jobName=" + jobName + ", dependencyJars="
				+ dependencyJars + ", contextParamList=" + contextParamList + ", modification=" + modification + "]";
	}
	
	
	
	
	
	
	
	
}