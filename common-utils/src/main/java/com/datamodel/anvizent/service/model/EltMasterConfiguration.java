package com.datamodel.anvizent.service.model;

import java.util.List;

public class EltMasterConfiguration {
	private long id;
	private String name;
	private String sparkJobPath;
	private String eltClassPath;
	private String eltLibraryPath;
	private String master;
	private String deployMode;
	private String sparkMaster;
	private boolean active;
	private Modification modification;
	private List<ELTConfigTags> environmentVariables;
	private Boolean masterDefault;
	private String sourceType;
	private String sparkSubmitMode;
	private String host;
	private int port;
	private String jobSubmitMode;
	private String authenticationType;
	private String password;
	private String ppkFile;
	private String userName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSparkJobPath() {
		return sparkJobPath;
	}
	public void setSparkJobPath(String sparkJobPath) {
		this.sparkJobPath = sparkJobPath;
	}
	public String getEltClassPath() {
		return eltClassPath;
	}
	public void setEltClassPath(String eltClassPath) {
		this.eltClassPath = eltClassPath;
	}
	public String getEltLibraryPath() {
		return eltLibraryPath;
	}
	public void setEltLibraryPath(String eltLibraryPath) {
		this.eltLibraryPath = eltLibraryPath;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getDeployMode() {
		return deployMode;
	}
	public void setDeployMode(String deployMode) {
		this.deployMode = deployMode;
	}
	public String getSparkMaster() {
		return sparkMaster;
	}
	public void setSparkMaster(String sparkMaster) {
		this.sparkMaster = sparkMaster;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public List<ELTConfigTags> getEnvironmentVariables() {
		return environmentVariables;
	}
	public void setEnvironmentVariables(List<ELTConfigTags> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}
	public Boolean getMasterDefault() {
		return masterDefault;
	}
	public void setMasterDefault(Boolean masterDefault) {
		this.masterDefault = masterDefault;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getSparkSubmitMode() {
		return sparkSubmitMode;
	}
	public void setSparkSubmitMode(String sparkSubmitMode) {
		this.sparkSubmitMode = sparkSubmitMode;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getJobSubmitMode() {
		return jobSubmitMode;
	}
	public void setJobSubmitMode(String jobSubmitMode) {
		this.jobSubmitMode = jobSubmitMode;
	}
	public String getAuthenticationType() {
		return authenticationType;
	}
	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPpkFile() {
		return ppkFile;
	}
	public void setPpkFile(String ppkFile) {
		this.ppkFile = ppkFile;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "EltMasterConfiguration [id=" + id + ", name=" + name + ", sparkJobPath=" + sparkJobPath
				+ ", eltClassPath=" + eltClassPath + ", eltLibraryPath=" + eltLibraryPath + ", master=" + master
				+ ", deployMode=" + deployMode + ", sparkMaster=" + sparkMaster + ", active=" + active
				+ ", modification=" + modification + ", environmentVariables=" + environmentVariables
				+ ", masterDefault=" + masterDefault + ", sourceType=" + sourceType + ", sparkSubmitMode="
				+ sparkSubmitMode + ", host=" + host + ", port=" + port + ", jobSubmitMode="
				+ jobSubmitMode + ", authenticationType=" + authenticationType + ", password=" + password + ", ppkFile="
				+ ppkFile + ", userName=" + userName + "]";
	}

	
	
}
