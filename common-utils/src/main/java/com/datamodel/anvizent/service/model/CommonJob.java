package com.datamodel.anvizent.service.model;

public class CommonJob {
	private Integer id;
	private String jobType;
	private String jobFileName;
	private Boolean isActive;
	Modification modification;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
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
	public void setModificaion(Modification modification) {
		this.modification = modification;
	}
	@Override
	public String toString() {
		return "CommonJob [id=" + id + ", jobType=" + jobType + ", jobFileName=" + jobFileName + ", isActive="
				+ isActive + "]";
	}
}
