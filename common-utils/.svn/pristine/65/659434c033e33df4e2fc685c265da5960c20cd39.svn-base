package com.datamodel.anvizent.service.model;

public class CustomRequest {

	
	private String clientId;
	private String userClientId;
	private String encUserId;
	private String userId;
	private String browserDetails;
	private String deploymentType;
	private String timeZone;
	private String webServiceContextUrl;
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUserClientId() {
		return userClientId;
	}

	public void setUserClientId(String userClientId) {
		this.userClientId = userClientId;
	}

	public String getBrowserDetails() {
		return browserDetails;
	}

	public void setBrowserDetails(String browserDetails) {
		this.browserDetails = browserDetails;
	}

	public String getDeploymentType() {
		return deploymentType;
	}

	public void setDeploymentType(String deploymentType) {
		this.deploymentType = deploymentType;
	}
	
	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	public String getWebServiceContextUrl() {
		return webServiceContextUrl;
	}

	public void setWebServiceContextUrl(String webServiceContextUrl) {
		this.webServiceContextUrl = webServiceContextUrl;
	}


	public String getEncUserId() {
		return encUserId;
	}

	public void setEncUserId(String encUserId) {
		this.encUserId = encUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "CustomRequest [clientId=" + clientId + ", userClientId=" + userClientId + ", encUserId=" + encUserId
				+ ", userId=" + userId + ", browserDetails=" + browserDetails + ", deploymentType=" + deploymentType
				+ ", timeZone=" + timeZone + ", webServiceContextUrl=" + webServiceContextUrl + "]";
	}
	
	public CustomRequest() {
	}

	public CustomRequest(String clientId, String userClientId, String browserDetails, String deploymentType) {
		this.clientId = clientId;
		this.userClientId = userClientId;
		this.browserDetails = browserDetails;
		this.deploymentType = deploymentType;
	}
	public CustomRequest(String webServiceContextUrl,String clientId, String userClientId, String browserDetails, String deploymentType,String timeZone) {
		this(webServiceContextUrl, clientId, userClientId, browserDetails, deploymentType, timeZone, null);
	}
	
	public CustomRequest(String webServiceContextUrl,String clientId, String userClientId, String browserDetails, String deploymentType,String timeZone,String encUserId) {
		this(webServiceContextUrl, clientId, userClientId, browserDetails, deploymentType, timeZone, encUserId, encUserId);
	}
	public CustomRequest(String webServiceContextUrl,String clientId, String userClientId, String browserDetails, String deploymentType,String timeZone,String encUserId,String userId) {
		this.webServiceContextUrl = webServiceContextUrl;
		this.clientId = clientId;
		this.userClientId = userClientId;
		this.browserDetails = browserDetails;
		this.deploymentType = deploymentType;
		this.timeZone = timeZone;
		this.encUserId = encUserId;
		this.userId = userId;
		
	}
	
	
	
}
