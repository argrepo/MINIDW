package com.datamodel.anvizent.common.exception;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class ClientWebserviceRequestException extends RuntimeException {
	
	private static final long serialVersionUID = -5299176862957494444L;

	public ClientWebserviceRequestException() {
	}

	public ClientWebserviceRequestException(String message) {
		super(message);
	}

	public ClientWebserviceRequestException(Throwable cause) {
		super(cause);
	}

	public ClientWebserviceRequestException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}