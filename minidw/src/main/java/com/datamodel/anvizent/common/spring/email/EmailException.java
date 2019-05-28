package com.datamodel.anvizent.common.spring.email;

import org.springframework.mail.MailException;

/**
 * Generic email exception.
 * 
 * @author bryanross
 *
 */
public class EmailException extends Exception {
	private static final long serialVersionUID = 1L;
	private MailException mailEx;

	public EmailException(MailException e) {
		super();
		this.mailEx = e;
	}

	public EmailException(String message, MailException e) {
		super(message);
		this.mailEx = e;
	}

	public MailException getMailException() {
		return mailEx;
	}
}
