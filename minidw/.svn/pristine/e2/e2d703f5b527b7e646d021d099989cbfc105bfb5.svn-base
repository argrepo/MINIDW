package com.datamodel.anvizent.service.model;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginForm {

	@NotEmpty(message = "Please enter username")
	private String username;
	@NotEmpty(message = "Please enter password")
	private String password;
	@NotEmpty(message = "Please enter client id")
	private String clientId;
	private String accessKey;
	private String locale;
	
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

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
	public String toString() {
		return "LoginForm [username=" + username + ", password=" + password + ", clientId=" + clientId + ", accessKey="
				+ accessKey + ", locale=" + locale + "]";
	}

}