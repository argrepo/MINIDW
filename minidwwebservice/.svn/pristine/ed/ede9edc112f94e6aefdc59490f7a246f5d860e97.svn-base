package com.datamodel.anvizent.common.spring.email;

import org.springframework.mail.SimpleMailMessage;

public class VelocityEmailMessage extends SimpleMailMessage  {
 
	private static final long serialVersionUID = 1L;

	public VelocityEmailMessage (String template) {
		this.template = template;
	}
	
	private String template;
	private String encoding = "UTF-8";
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getTemplate() {
		return template;
	}

}
