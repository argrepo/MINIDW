package com.datamodel.anvizent.common.exception;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class ClassPathException extends Exception {
	
	private static final long serialVersionUID = -5299176862957494444L;

	public ClassPathException() {
	}

	public ClassPathException(String message) {
		super(message);
	}

	public ClassPathException(Throwable cause) {
		super(cause);
	}

	public ClassPathException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}