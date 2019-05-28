package com.datamodel.anvizent.service.model;

import java.util.List;

public class ClientDLMappingForm {
	private Integer clientId;
	private List<String> dLs;
	private boolean isActive;
	private String pageMode;
	private List<DLInfo> dLInfo;
	
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public List<String> getdLs() {
		return dLs;
	}
	public void setdLs(List<String> dLs) {
		this.dLs = dLs;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getPageMode() {
		return pageMode;
	}
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	public List<DLInfo> getdLInfo() {
		return dLInfo;
	}
	public void setdLInfo(List<DLInfo> dLInfo) {
		this.dLInfo = dLInfo;
	}
	@Override
	public String toString() {
		return "ClientDLMappingForm [clientId=" + clientId + ", dLs=" + dLs + ", isActive=" + isActive + ", pageMode="
				+ pageMode + ", dLInfo=" + dLInfo + "]";
	}
	
	 
	
}
