package com.datamodel.anvizent.service.model;

import java.util.Date;

public class WebServiceTemplateAuthRequestparams {
	private Long id;
	private Long wsTemplateId;
	private String paramName;
	private boolean mandatory;
	private boolean passwordType;
	private String createdBy;
	private Date createdTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getWsTemplateId() {
		return wsTemplateId;
	}
	public void setWsTemplateId(Long wsTemplateId) {
		this.wsTemplateId = wsTemplateId;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public boolean isPasswordType() {
		return passwordType;
	}
	public void setPasswordType(boolean passwordType) {
		this.passwordType = passwordType;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@Override
	public String toString() {
		return "WebServiceTemplateAuthRequestparams [id=" + id + ", wsTemplateId=" + wsTemplateId + ", paramName="
				+ paramName + ", mandatory=" + mandatory + ", passwordType=" + passwordType + ", createdBy=" + createdBy
				+ ", createdTime=" + createdTime + "]";
	}
	
}
