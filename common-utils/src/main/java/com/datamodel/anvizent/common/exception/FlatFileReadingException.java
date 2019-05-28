package com.datamodel.anvizent.common.exception;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class FlatFileReadingException extends Exception {
	
	private static final long serialVersionUID = -5299176862957494444L;

	public FlatFileReadingException() {
	}

	public FlatFileReadingException(String message) {
		super(message);
	}

	public FlatFileReadingException(Throwable cause) {
		super(cause);
	}

	public FlatFileReadingException(String message, Throwable cause) {
		super(message, cause);
	}
	public FlatFileReadingException(String message, boolean useErrorMessage, Throwable cause) {
		super(message + (useErrorMessage ? "<br /><b>Error Details:</b> "+cause.getMessage():""), cause);
	}
	
	
}