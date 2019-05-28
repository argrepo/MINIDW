package com.datamodel.anvizent.common.exception;

public class AmazonExeception extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public AmazonExeception() {
	}

	public AmazonExeception(String message) {
		super(message);
	}

	public AmazonExeception(Throwable cause) {
		super(cause);
	}

	public AmazonExeception(String message, Throwable cause) {
		super(message, cause);
	}

}
