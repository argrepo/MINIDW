package com.datamodel.anvizent.common.exception;

public class PackageExecutionException  extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PackageExecutionException() {
	}

	public PackageExecutionException(String message) {
		super(message);
	}

	public PackageExecutionException(Throwable cause) {
		super(cause);
	}

	public PackageExecutionException(String message, Throwable cause) {
		super(message, cause);
	}
}