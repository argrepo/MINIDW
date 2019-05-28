package com.datamodel.anvizent.common.exception;

public class UploadandExecutionFailedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UploadandExecutionFailedException() {
	}

	public UploadandExecutionFailedException(String message) {
		super(message);
	}

	public UploadandExecutionFailedException(Throwable cause) {
		super(cause);
	}

	public UploadandExecutionFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadandExecutionFailedException(String message, boolean useErrorMessage, Throwable cause) {
		super(message + (useErrorMessage ? "<br /><b>Error Details:</b> "+cause.getMessage():""), cause);
	}

}
