package com.datamodel.anvizent.service.model;

public class S3BucketInfo {
	private Integer id;
	private int clientId;
	private String bucketName;
	private String secretKey;
	private String accessKey;
	private Boolean isActive;
	private Modification modification;
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	@Override
	public String toString() {
		return "S3BucketInfo [id=" + id + ", clientId=" + clientId + ", bucketName=" + bucketName + ", secretKey="
				+ secretKey + ", accessKey=" + accessKey + ", isActive=" + isActive + ", modification=" + modification
				+ "]";
	}
 
	
}
