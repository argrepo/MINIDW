package com.datamodel.anvizent.common.spring.email;

import java.util.Map;


/**
 * Email services.
 * 
 * @author bryanross
 *
 */
public interface EmailService {
	public void alertAdministrator(String message) throws EmailException;
	public void alertAdministrator(String message, Throwable ex) throws EmailException;
	
	/**
	 * Send and email based on the velocity email template
	 * @param message
	 * @param modelMap
	 * @throws EmailException
	 */
	public void sendVelocityEmail(final VelocityEmailMessage message, final Map<String, Object> modelMap) throws EmailException;
}
