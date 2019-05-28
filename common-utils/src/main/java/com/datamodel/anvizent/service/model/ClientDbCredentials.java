package com.datamodel.anvizent.service.model;

public class ClientDbCredentials {
	private String hostname,portnumber, clientDbSchema,clientStagingDbSchema, clientAppDbSchema,
	databaseVendor,clientDbUsername,clientDbPassword, clientAppDbUserName,clientAppDbPassword;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPortnumber() {
		return portnumber;
	}

	public void setPortnumber(String portnumber) {
		this.portnumber = portnumber;
	}

	public String getClientDbSchema() {
		return clientDbSchema;
	}

	public void setClientDbSchema(String clientDbSchema) {
		this.clientDbSchema = clientDbSchema;
	}

	public String getClientStagingDbSchema() {
		return clientStagingDbSchema;
	}

	public void setClientStagingDbSchema(String clientStagingDbSchema) {
		this.clientStagingDbSchema = clientStagingDbSchema;
	}

	public String getClientAppDbSchema() {
		return clientAppDbSchema;
	}

	public void setClientAppDbSchema(String clientAppDbSchema) {
		this.clientAppDbSchema = clientAppDbSchema;
	}

	public String getDatabaseVendor() {
		return databaseVendor;
	}

	public void setDatabaseVendor(String databaseVendor) {
		this.databaseVendor = databaseVendor;
	}

	public String getClientDbUsername() {
		return clientDbUsername;
	}

	public void setClientDbUsername(String clientDbUsername) {
		this.clientDbUsername = clientDbUsername;
	}

	public String getClientDbPassword() {
		return clientDbPassword;
	}

	public void setClientDbPassword(String clientDbPassword) {
		this.clientDbPassword = clientDbPassword;
	}

	public String getClientAppDbUserName() {
		return clientAppDbUserName;
	}

	public void setClientAppDbUserName(String clientAppDbUserName) {
		this.clientAppDbUserName = clientAppDbUserName;
	}

	public String getClientAppDbPassword() {
		return clientAppDbPassword;
	}

	public void setClientAppDbPassword(String clientAppDbPassword) {
		this.clientAppDbPassword = clientAppDbPassword;
	}

	@Override
	public String toString() {
		return "ClientDbCredentials [hostname=" + hostname + ", portnumber=" + portnumber + ", clientDbSchema="
				+ clientDbSchema + ", clientStagingDbSchema=" + clientStagingDbSchema + ", clientAppDbSchema="
				+ clientAppDbSchema + ", databaseVendor=" + databaseVendor + ", clientDbUsername=" + clientDbUsername
				+ ", clientDbPassword=" + clientDbPassword + ", clientAppDbUserName=" + clientAppDbUserName
				+ ", clientAppDbPassword=" + clientAppDbPassword + "]";
	}
	
	
	
}
