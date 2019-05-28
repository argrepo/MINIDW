package com.datamodel.anvizent.service.model;

import java.util.List;

public class AIJobUploadInfo{
	
	private Integer rid;
	private Integer businessId;
	private String businessName;
	private String modelName;
	private AIContextParameter aiGenericContextParam;
	private List<AIModel> aiModal;
	private String jobName;
	private String jobFileName;
	private Boolean isActive;
	private Modification modification;
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public Integer getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}
	public AIContextParameter getAiGenericContextParam() {
		return aiGenericContextParam;
	}
	public void setAiGenericContextParam(AIContextParameter aiGenericContextParam) {
		this.aiGenericContextParam = aiGenericContextParam;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobFileName() {
		return jobFileName;
	}
	public void setJobFileName(String jobFileName) {
		this.jobFileName = jobFileName;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public List<AIModel> getAiModal() {
		return aiModal;
	}
	public void setAiModal(List<AIModel> aiModal) {
		this.aiModal = aiModal;
	}
	public String getBusinessName()
	{
		return businessName;
	}
	public void setBusinessName(String businessName)
	{
		this.businessName = businessName;
	}
	public String getModelName()
	{
		return modelName;
	}
	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}
	@Override
	public String toString()
	{
		return "AIJobUploadInfo [rid=" + rid + ", businessId=" + businessId + ", businessName=" + businessName + ", modelName=" + modelName + ", aiGenericContextParam=" + aiGenericContextParam + ", aiModal=" + aiModal + ", jobName=" + jobName + ", jobFileName=" + jobFileName + ", isActive="
				+ isActive + ", modification=" + modification + ", getRid()=" + getRid() + ", getBusinessId()=" + getBusinessId() + ", getAiGenericContextParam()=" + getAiGenericContextParam() + ", getJobName()=" + getJobName() + ", getJobFileName()=" + getJobFileName() + ", getIsActive()="
				+ getIsActive() + ", getModification()=" + getModification() + ", getAiModal()=" + getAiModal() + ", getBusinessName()=" + getBusinessName() + ", getModelName()=" + getModelName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
}