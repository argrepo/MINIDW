package com.datamodel.anvizent.service.model;

import java.util.Date;

import com.datamodel.anvizent.helper.CommonDateHelper;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class Modification {

	private boolean isActive;
	private String ipAddress;
	private String createdBy;
	private String createdTime;
	private String modifiedBy;
	private String modifiedTime;
	
	
	public Modification() {
		super();
	}
	/**
	 * format the date in yyyy-MM-dd HH:mm:ss format 
	 * @param createdTime
	 */
	public Modification(Date createdTime) {
		super();
		this.createdTime = CommonDateHelper.formatDateAsString(createdTime);
	}
	/**
	 * format the date in yyyy-MM-dd HH:mm:ss format
	 * @param modifiedTime
	 */
	public void setModifiedDateTime(Date modifiedTime){
		if ( modifiedTime != null) {
			this.modifiedTime = CommonDateHelper.formatDateAsString(modifiedTime);
		} 
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getipAddress() {
		return ipAddress;
	}
	public void setipAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	@Override
	public String toString() {
		return "Modification [isActive=" + isActive + ", ipAddress=" + ipAddress + ", createdBy=" + createdBy
				+ ", createdTime=" + createdTime + ", modifiedBy=" + modifiedBy + ", modifiedTime=" + modifiedTime
				+ "]";
	}
	
	
	
}
