package com.datamodel.anvizent.service.model;

public class ServerConfigurations {
	private int id;
	private String serverName;
	private String shortName;
	private String description;
	private String ipAddress;
	private String portNumber;
	private String minidwSchemaName;
	private String anvizentSchemaName;
	private String userName;
	private String serverPassword;
	private boolean activeStatus; 
	private String clientDbDetailsEndPoint;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}
	public String getMinidwSchemaName() {
		return minidwSchemaName;
	}
	public void setMinidwSchemaName(String minidwSchemaName) {
		this.minidwSchemaName = minidwSchemaName;
	}
	public String getAnvizentSchemaName() {
		return anvizentSchemaName;
	}
	public void setAnvizentSchemaName(String anvizentSchemaName) {
		this.anvizentSchemaName = anvizentSchemaName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getServerPassword() {
		return serverPassword;
	}
	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}
	public boolean isActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getClientDbDetailsEndPoint() {
		return clientDbDetailsEndPoint;
	}
	public void setClientDbDetailsEndPoint(String clientDbDetailsEndPoint) {
		this.clientDbDetailsEndPoint = clientDbDetailsEndPoint;
	}
	@Override
	public String toString() {
		return "ServerConfigurations [id=" + id + ", serverName=" + serverName + ", shortName=" + shortName + ", description=" + description + ", ipAddress="
				+ ipAddress + ", portNumber=" + portNumber + ", minidwSchemaName=" + minidwSchemaName + ", anvizentSchemaName=" + anvizentSchemaName
				+ ", userName=" + userName + ", serverPassword=" + serverPassword + ", activeStatus=" + activeStatus + ", clientDbDetailsEndPoint="
				+ clientDbDetailsEndPoint + "]";
	}
	
}
