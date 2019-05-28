package com.datamodel.anvizent.service.model;

import java.util.List;
import java.util.Map;

public class ILConnection {
	
	private String clientId;
	private Database database;
	private String connectionType;
	private String server;
	private String username;
	private String password;
	private String connectionName;
	private int connectionId;
	Modification modification;
	private String dateFormat;
	private String timeZone;
	private boolean availableInCloud;
	private boolean isWebApp;
	private String dataSourceName;
	private String dataSourceNameOther;
	private boolean ddLayout=false;
	private boolean apisData;
	private List<User> user;
	private String dbVariables;
	private boolean sslEnable=false;
	private String sslTrustKeyStoreFilePaths;
	 
	
	private Map<String,String> dbVariablesList;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Database getDatabase() {
		return database;
	}
	public void setDatabase(Database database) {
		this.database = database;
	}
	public String getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConnectionName() {
		return connectionName;
	}
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}
	public int getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public boolean isAvailableInCloud() {
		return availableInCloud;
	}
	public void setAvailableInCloud(boolean availableInCloud) {
		this.availableInCloud = availableInCloud;
	}
	public boolean isWebApp() {
		return isWebApp;
	}
	public void setWebApp(boolean isWebApp) {
		this.isWebApp = isWebApp;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getDataSourceNameOther() {
		return dataSourceNameOther;
	}
	public void setDataSourceNameOther(String dataSourceNameOther) {
		this.dataSourceNameOther = dataSourceNameOther;
	}
	public boolean isDdLayout() {
		return ddLayout;
	}
	public void setDdLayout(boolean ddLayout) {
		this.ddLayout = ddLayout;
	}
	public List<User> getUser() {
		return user;
	}
	public void setUser(List<User> user) {
		this.user = user;
	}
	public boolean isApisData() {
		return apisData;
	}
	public void setApisData(boolean apisData) {
		this.apisData = apisData;
	}
	public String getDbVariables()
	{
		return dbVariables;
	}
	public void setDbVariables(String dbVariables)
	{
		this.dbVariables = dbVariables;
	}
	public Map<String, String> getDbVariablesList()
	{
		return dbVariablesList;
	}
	public void setDbVariablesList(Map<String, String> dbVariablesList)
	{
		this.dbVariablesList = dbVariablesList;
	}
 	public boolean isSslEnable()
	{
		return sslEnable;
	}
	public void setSslEnable(boolean sslEnable)
	{
		this.sslEnable = sslEnable;
	}
	public String getSslTrustKeyStoreFilePaths()
	{
		return sslTrustKeyStoreFilePaths;
	}
	public void setSslTrustKeyStoreFilePaths(String sslTrustKeyStoreFilePaths)
	{
		this.sslTrustKeyStoreFilePaths = sslTrustKeyStoreFilePaths;
	}
	@Override
	public String toString() {
		return "ILConnection [clientId=" + clientId + ", database=" + database + ", connectionType=" + connectionType
				+ ", server=" + server + ", username=" + username + ", password=" + password + ", connectionName="
				+ connectionName + ", connectionId=" + connectionId + ", modification=" + modification + ", dateFormat="
				+ dateFormat + ", timeZone=" + timeZone + ", availableInCloud=" + availableInCloud + ", isWebApp="
				+ isWebApp + ", dataSourceName=" + dataSourceName + ", dataSourceNameOther=" + dataSourceNameOther
				+ ", ddLayout=" + ddLayout + ", apisData=" + apisData + ", user=" + user + ", dbVariables="
				+ dbVariables + ", sslEnable=" + sslEnable + ", sslTrustKeyStoreFilePaths=" + sslTrustKeyStoreFilePaths
				+ ", dbVariablesList=" + dbVariablesList + "]";
	} 
 
	 
}
