package com.datamodel.anvizent.service.model;

public class ContextParameter {
	private Integer paramId;
	private String paramName;
	private String paramval;
	private String description;
	private Boolean isActive;
	private String pageMode;
	private Modification modification;
	
	public Integer getParamId() {
		return paramId;
	}
	public void setParamId(Integer param_id) {
		this.paramId = param_id;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String param_name) {
		this.paramName = param_name;
	}
	public String getParamval() {
		return paramval;
	}
	public void setParamval(String paramval) {
		this.paramval = paramval;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getPageMode() {
		return pageMode;
	}
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	

	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	@Override
	public String toString() {
		return "ContextParameterForm [param_id=" + paramId + ", param_name=" + paramName + ", paramval=" + paramval
				+ ", description=" + description + ", isActive=" + isActive + ", pageMode=" + pageMode + "]";
	}
	
}
