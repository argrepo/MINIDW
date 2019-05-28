package com.datamodel.anvizent.security;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;

/**
 * Represents a user is not authenticated.
 * 
 * @author
 *
 */
public class UserNotAuthenticatedException extends AnvizentRuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotAuthenticatedException() {

	}

	public UserNotAuthenticatedException(String message) {
		super(message);
	}

	public UserNotAuthenticatedException(Throwable cause) {
		super(cause);
	}

	public UserNotAuthenticatedException(String message, Throwable cause) {
		super(message, cause);
	}

}
