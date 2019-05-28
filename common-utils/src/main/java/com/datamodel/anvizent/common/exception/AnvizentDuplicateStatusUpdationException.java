package com.datamodel.anvizent.common.exception;

/**
 * ApgDuplicateFileNameException is for indicating an Exception occurred during
 * a file save and an existing file allready exists at that path.
 * 
 * @author rakesh.gajula
 * 
 */
public class AnvizentDuplicateStatusUpdationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AnvizentDuplicateStatusUpdationException() {
	}

	public AnvizentDuplicateStatusUpdationException(String message) {
		super(message);
	}

	public AnvizentDuplicateStatusUpdationException(Throwable cause) {
		super(cause);
	}

	public AnvizentDuplicateStatusUpdationException(String message, Throwable cause) {
		super(message, cause);
	}
}