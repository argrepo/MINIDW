package com.datamodel.anvizent.service.model;

public class MiddleLevelManager {
	private long id;
	private String contextPath;
	private String uploadListEndPoint;
	private String writeEndPoint;
	private String deleteEndPoint;
	private String upgradeEndPoint;
	private String userAuthToken;
	private String clientAuthToken;
	private String encryptionPrivateKey;
	private String encryptionIV;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public String getUploadListEndPoint() {
		return uploadListEndPoint;
	}
	public void setUploadListEndPoint(String uploadListEndPoint) {
		this.uploadListEndPoint = uploadListEndPoint;
	}
	public String getWriteEndPoint() {
		return writeEndPoint;
	}
	public void setWriteEndPoint(String writeEndpoint) {
		this.writeEndPoint = writeEndpoint;
	}
	public String getDeleteEndPoint() {
		return deleteEndPoint;
	}
	public void setDeleteEndPoint(String deleteEndPoint) {
		this.deleteEndPoint = deleteEndPoint;
	}
	public String getUpgradeEndPoint() {
		return upgradeEndPoint;
	}
	public void setUpgradeEndPoint(String upgradeEndPoint) {
		this.upgradeEndPoint = upgradeEndPoint;
	}
	public String getUserAuthToken() {
		return userAuthToken;
	}
	public void setUserAuthToken(String userAuthToken) {
		this.userAuthToken = userAuthToken;
	}
	
	
	public String getClientAuthToken() {
		return clientAuthToken;
	}
	public void setClientAuthToken(String clientAuthToken) {
		this.clientAuthToken = clientAuthToken;
	}
	public String getEncryptionPrivateKey() {
		return encryptionPrivateKey;
	}
	public void setEncryptionPrivateKey(String encryptionPrivateKey) {
		this.encryptionPrivateKey = encryptionPrivateKey;
	}
	public String getEncryptionIV() {
		return encryptionIV;
	}
	public void setEncryptionIV(String encryptionIV) {
		this.encryptionIV = encryptionIV;
	}
	
	
	
	
	
	
	
}
