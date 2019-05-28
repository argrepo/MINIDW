package com.datamodel.anvizent.service.model;

import java.util.List;

public class ClientConnectorMappingForm {

	private Integer clientId;
	private Integer databaseId;
	private List<String> databases;
	private Integer connectorId;
	private List<String> connectors;
	private List<Database> databaseList;
	private String pageMode;
	
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public Integer getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(Integer databaseId) {
		this.databaseId = databaseId;
	}
	public List<String> getDatabases() {
		return databases;
	}
	public void setDatabases(List<String> databases) {
		this.databases = databases;
	}
	public Integer getConnectorId() {
		return connectorId;
	}
	public void setConnectorId(Integer connectorId) {
		this.connectorId = connectorId;
	}
	public List<String> getConnectors() {
		return connectors;
	}
	public void setConnectors(List<String> connectors) {
		this.connectors = connectors;
	}
	public String getPageMode() {
		return pageMode;
	}
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	public List<Database> getDatabaseList() {
		return databaseList;
	}
	public void setDatabaseList(List<Database> databaseList) {
		this.databaseList = databaseList;
	}
	@Override
	public String toString() {
		return "ClientConnectorMappingForm [clientId=" + clientId + ", databaseId=" + databaseId + ", databases="
				+ databases + ", connectorId=" + connectorId + ", connectors=" + connectors + ", databaseList="
				+ databaseList + ", pageMode=" + pageMode + "]";
	}
	
}
