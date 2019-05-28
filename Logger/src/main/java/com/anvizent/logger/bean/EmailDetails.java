package com.anvizent.logger.bean;

import java.util.Properties;

public class EmailDetails {
	private String fromAddress;
	private String replyToAddress;
	private String password;
	private Properties properties;
	private EmailMessageDetails messageDetails;

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getReplyToAddress() {
		return replyToAddress;
	}

	public void setReplyToAddress(String replyToAddress) {
		this.replyToAddress = replyToAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public EmailMessageDetails getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(EmailMessageDetails messageDetails) {
		this.messageDetails = messageDetails;
	}
}
