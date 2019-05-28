package com.datamodel.anvizent.common.exception;

public class ETLJobExecutionFailedException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ETLJobExecutionFailedException() {
	}

	public ETLJobExecutionFailedException(String message) {
		super(message);
	}

	public ETLJobExecutionFailedException(Throwable cause) {
		super(cause);
	}

	public ETLJobExecutionFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ETLJobExecutionFailedException(String message, boolean useErrorMessage, Throwable cause) {
		super(message + (useErrorMessage ? "<br /><b>Error Details:</b> "+cause.getMessage():""), cause);
	}

}
