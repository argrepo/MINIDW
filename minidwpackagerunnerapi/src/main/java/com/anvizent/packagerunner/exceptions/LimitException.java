package com.anvizent.packagerunner.exceptions;

public class LimitException extends Exception {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 3771495428973767460L;

	public LimitException() {
	}
	public LimitException(String message) {
		super(message);
	}

	public LimitException(Throwable cause) {
		super(cause);
	}

	public LimitException(String message, Throwable cause) {
		super(message, cause);
	}
}
