package com.datamodel.anvizent.service.model;

import java.util.Date;

public class WebServiceAuthenticationTypes {
	private Long id;
	private String authenticationType;
	private boolean active;
	private String createdBy;
	private Date createdTime;
	private String modifiedBy;
	private Date modifiedTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAuthenticationType() {
		return authenticationType;
	}
	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
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
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	@Override
	public String toString() {
		return "WebServiceAuthenticationTypes [id=" + id + ", authenticationType=" + authenticationType + ", active="
				+ active + ", createdBy=" + createdBy + ", createdTime=" + createdTime + ", modifiedBy=" + modifiedBy
				+ ", modifiedTime=" + modifiedTime + "]";
	}
	
	
	
	
}
