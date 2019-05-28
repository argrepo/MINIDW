package com.datamodel.anvizent.common.exception;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class TalendJobNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5299176862957494444L;

	public TalendJobNotFoundException() {
	}

	public TalendJobNotFoundException(String message) {
		super(message);
	}

	public TalendJobNotFoundException(Throwable cause) {
		super(cause);
	}

	public TalendJobNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public TalendJobNotFoundException(String message, String errorMessage) {
		super(message + "<br /><b>Error Details:</b> " + errorMessage);
	}
	
	public TalendJobNotFoundException(String message, String errorMessage, Throwable cause) {
		super(message + "<br /><b>Error Details:</b> " + errorMessage, cause);
	}
}