/**
 * 
 */
package com.datamodel.anvizent.service.model;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CommunicatorRequestBean  {
	
	private String reqId;
	
	private List<String> communicationModes;
	
	private String componentName;

	private String entityId;

	private Long userId;

	private String purpose;
	
	private String comments;

	private Integer priority;

	private Map<String, String> userInfoMap;
	
	private String subject;
	
	private String title;
	
	private List<File> attachments;
	
	private Map<String,List<String>> deviceInfoMap;
	
	private List<String> mobileNo;
	
	private String requestFrom;
	
	private String notificationContent;
	
	private String notificationContentForDashobard;
	
	private Map<String,Object> dynamicNotificationContentMap;
	
	private String dynamicNotificationContentJson;
	
	private String locale;

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public List<String> getCommunicationModes() {
		return communicationModes;
	}

	public void setCommunicationModes(List<String> communicationModes) {
		this.communicationModes = communicationModes;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Map<String, String> getUserInfoMap() {
		return userInfoMap;
	}

	public void setUserInfoMap(Map<String, String> userInfoMap) {
		this.userInfoMap = userInfoMap;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	public Map<String, List<String>> getDeviceInfoMap() {
		return deviceInfoMap;
	}

	public void setDeviceInfoMap(Map<String, List<String>> deviceInfoMap) {
		this.deviceInfoMap = deviceInfoMap;
	}

	public List<String> getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(List<String> mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}

	public String getNotificationContent() {
		return notificationContent;
	}

	public void setNotificationContent(String notificationContent) {
		this.notificationContent = notificationContent;
	}

	public String getNotificationContentForDashobard() {
		return notificationContentForDashobard;
	}

	public void setNotificationContentForDashobard(
			String notificationContentForDashobard) {
		this.notificationContentForDashobard = notificationContentForDashobard;
	}

	public Map<String, Object> getDynamicNotificationContentMap() {
		return dynamicNotificationContentMap;
	}

	public void setDynamicNotificationContentMap(
			Map<String, Object> dynamicNotificationContentMap) {
		this.dynamicNotificationContentMap = dynamicNotificationContentMap;
	}

	public String getDynamicNotificationContentJson() {
		return dynamicNotificationContentJson;
	}

	public void setDynamicNotificationContentJson(
			String dynamicNotificationContentJson) {
		this.dynamicNotificationContentJson = dynamicNotificationContentJson;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
	public String toString() {
		return "CommunicatorRequestBean [reqId=" + reqId + ", communicationModes=" + communicationModes
				+ ", componentName=" + componentName + ", entityId=" + entityId + ", userId=" + userId + ", purpose="
				+ purpose + ", comments=" + comments + ", priority=" + priority + ", userInfoMap=" + userInfoMap
				+ ", subject=" + subject + ", title=" + title + ", attachments=" + attachments + ", deviceInfoMap="
				+ deviceInfoMap + ", mobileNo=" + mobileNo + ", requestFrom=" + requestFrom + ", notificationContent="
				+ notificationContent + ", notificationContentForDashobard=" + notificationContentForDashobard
				+ ", dynamicNotificationContentMap=" + dynamicNotificationContentMap
				+ ", dynamicNotificationContentJson=" + dynamicNotificationContentJson + ", locale=" + locale + "]";
	}

}