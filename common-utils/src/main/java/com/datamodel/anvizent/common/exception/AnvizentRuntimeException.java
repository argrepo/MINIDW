package com.datamodel.anvizent.common.exception;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datamodel.anvizent.service.model.Message;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class AnvizentRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	 protected static final Log LOG = LogFactory.getLog(AnvizentRuntimeException.class);
	private List<Message> errorMessges;
	public AnvizentRuntimeException() {
	}
	public AnvizentRuntimeException(RuntimeException ex, List<Message> messages) {
		  if (messages != null && messages.isEmpty()) {
		   LOG.debug("Anvizent Runtime error: " + ex.getLocalizedMessage());
		   Message errorMessage = new Message();
		   errorMessage.setCode("");
		   errorMessage.setText(ex.getLocalizedMessage());
		   errorMessges = new ArrayList<Message>();
		   errorMessges.add(errorMessage);
		  } else {
		   this.errorMessges = messages;
		  }
		  
		 }
	public AnvizentRuntimeException(String message) {
		super(message);
	}

	public AnvizentRuntimeException(Throwable cause) {
		super(cause);
	}

	public AnvizentRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public List<Message> getErrorMessges() {
		return errorMessges;
	}

	public void setErrorMessges(List<Message> errorMessges) {
		this.errorMessges = errorMessges;
	}
	
}
