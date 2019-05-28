package com.datamodel.anvizent.service.model;

import java.util.List;

public class MultiClientInsertScriptsExecution {
	private List<String> clientIds;
	private List<String> tableNames;
	private Boolean truncateTbl;
	public List<String> getClientIds() {
		return clientIds;
	}
	public void setClientIds(List<String> clientIds) {
		this.clientIds = clientIds;
	}
	public List<String> getTableNames() {
		return tableNames;
	}
	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}
	public Boolean getTruncateTbl() {
		return truncateTbl;
	}
	public void setTruncateTbl(Boolean truncateTbl) {
		this.truncateTbl = truncateTbl;
	}
	@Override
	public String toString() {
		return "MultiClientInsertScriptsExecution [clientIds=" + clientIds + ", tableNames=" + tableNames
				+ ", truncateTbl=" + truncateTbl + "]";
	}
}
