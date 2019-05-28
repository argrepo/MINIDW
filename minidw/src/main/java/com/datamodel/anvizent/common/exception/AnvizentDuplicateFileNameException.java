package com.datamodel.anvizent.common.exception;

/**
 * ApgDuplicateFileNameException is for indicating an Exception occurred during
 * a file save and an existing file allready exists at that path.
 * 
 * @author rakesh.gajula
 * 
 */
public class AnvizentDuplicateFileNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AnvizentDuplicateFileNameException() {
	}

	public AnvizentDuplicateFileNameException(String message) {
		super(message);
	}

	public AnvizentDuplicateFileNameException(Throwable cause) {
		super(cause);
	}

	public AnvizentDuplicateFileNameException(String message, Throwable cause) {
		super(message, cause);
	}
}