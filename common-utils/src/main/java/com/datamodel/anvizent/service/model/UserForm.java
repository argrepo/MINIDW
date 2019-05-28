package com.datamodel.anvizent.service.model;

import java.util.List;

public class UserForm {
	private String clientId;
	private String userName;
	private String userId;
	private String roleId;
	private String roleName;
	private String emailId;
	private String mobileNo;
	private List<Industry> industries;
	private String schemaName;
	private Modification modification;
	
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	public List<Industry> getIndustries() {
		return industries;
	}
	public void setIndustries(List<Industry> industries) {
		this.industries = industries;
	}
	
	public Modification getModification() {
		return modification;
	}
	public void setModification(Modification modification) {
		this.modification = modification;
	}
	
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
	public String toString() {
		return "UserForm [clientId=" + clientId + ", userName=" + userName + ", userId=" + userId + ", roleId=" + roleId
				+ ", roleName=" + roleName + ", emailId=" + emailId + ", mobileNo=" + mobileNo + ", industries="
				+ industries + ", schemaName=" + schemaName + ", modification=" + modification + "]";
	}
}
