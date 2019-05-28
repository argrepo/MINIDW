package com.datamodel.anvizent.common.exception;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class AnvizentCorewsException extends Exception {
	
	private static final long serialVersionUID = -5299176862957494444L;

	public AnvizentCorewsException() {
	}

	public AnvizentCorewsException(String message) {
		super(message);
	}

	public AnvizentCorewsException(Throwable cause) {
		super(cause);
	}

	public AnvizentCorewsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AnvizentCorewsException(String message, boolean useErrorMessage, Throwable cause) {
		super(message + (useErrorMessage ? "<br /><b>Error Details:</b> "+cause.getMessage():""), cause);
	}
}