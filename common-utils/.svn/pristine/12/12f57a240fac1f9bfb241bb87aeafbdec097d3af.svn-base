package com.datamodel.anvizent.common.exception;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class EltJobExecutionException extends RuntimeException {
	
	private static final long serialVersionUID = -5299176862957494444L;

	public EltJobExecutionException() {
	}

	public EltJobExecutionException(String message) {
		super(message);
	}

	public EltJobExecutionException(Throwable cause) {
		super(cause);
	}

	public EltJobExecutionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EltJobExecutionException(String message, boolean useErrorMessage, Throwable cause) {
		super(message + (useErrorMessage ? "<br /><b>Error Details:</b> "+cause.getMessage():""), cause);
	}
}