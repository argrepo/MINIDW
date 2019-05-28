package com.datamodel.anvizent.service.model;

public class ErrorLog {
	private String id;
	private String errorCode;
	private String errorMessage;
	private String errorBody;
	private String errorDatetime;
	private String requestedService;
	private String receivedParameters;
	private String userId;
	private String clientDetails;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorBody() {
		return errorBody;
	}
	public void setErrorBody(String errorBody) {
		this.errorBody = errorBody;
	}
	public String getErrorDatetime() {
		return errorDatetime;
	}
	public void setErrorDatetime(String errorDatetime) {
		this.errorDatetime = errorDatetime;
	}
	public String getRequestedService() {
		return requestedService;
	}
	public void setRequestedService(String requested_service) {
		this.requestedService = requested_service;
	}
	public String getReceivedParameters() {
		return receivedParameters;
	}
	public void setReceivedParameters(String received_parameters) {
		this.receivedParameters = received_parameters;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String user_id) {
		this.userId = user_id;
	}
	public String getClientDetails() {
		return clientDetails;
	}
	public void setClientDetails(String client_details) {
		this.clientDetails = client_details;
	}
	
	@Override
	public String toString() {
		return "ErrorLog [id=" + id + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", errorBody="
				+ errorBody + ", errorDatetime=" + errorDatetime + ", requestedService=" + requestedService
				+ ", receivedParameters=" + receivedParameters + ", userId=" + userId + ", clientDetails="
				+ clientDetails + "]";
	}
	
}
