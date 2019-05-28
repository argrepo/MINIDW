package com.datamodel.anvizent.service.model;

import java.util.List;

public class ManualJobUpload {
	private int masterId;
	private int clientId;
	private List<Long> packageIds;
	public int getMasterId() {
		return masterId;
	}
	public void setMasterId(int masterId) {
		this.masterId = masterId;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public List<Long> getPackageIds() {
		return packageIds;
	}
	public void setPackageIds(List<Long> packageIds) {
		this.packageIds = packageIds;
	}
	@Override
	public String toString() {
		return "ManualJobUpload [masterId=" + masterId + ", clientId=" + clientId + ", packageIds=" + packageIds + "]";
	}

}
