package com.datamodel.anvizent.common.exception;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class CSVConversionException extends Exception {
	
	private static final long serialVersionUID = -5299176862957494444L;

	public CSVConversionException() {
	}

	public CSVConversionException(String message) {
		super(message);
	}

	public CSVConversionException(Throwable cause) {
		super(cause);
	}

	public CSVConversionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}