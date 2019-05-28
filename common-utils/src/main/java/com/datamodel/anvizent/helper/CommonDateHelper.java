package com.datamodel.anvizent.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Helper for converting Data Model dates and timestamps.
 * 
 * @author
 *
 */
final public class CommonDateHelper {
	static final private String LAWSON_DATE = "yyyyMMdd";
	static final private String LAWSON_TIME = "hhmmss";
	static final private String TIMESTAMP = "yyyyMMddHHmmss";

	private CommonDateHelper() {
	}

	/**
	 * Convert a Data modal timestamp (LMTS) column to a date/time.
	 * 
	 * @param timestamp
	 * @return
	 */
	static public Date timestampToDate(long timestamp) {
		return new Date(timestamp);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	static public long dateToTimetamp(Date date) {
		if (date == null)
			throw new IllegalArgumentException("Date is required.");
		return date.getTime();
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	static public int dateToInt(Date date) {
		if (date == null)
			throw new IllegalArgumentException("Date is required.");

		return Integer.parseInt(new SimpleDateFormat(LAWSON_DATE).format(date));
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	static public int timeToInt(Date date) {
		if (date == null)
			throw new IllegalArgumentException("Date is required.");

		return Integer.parseInt(new SimpleDateFormat(LAWSON_TIME).format(date));
	}

	/**
	 * format the date in yyyyMMddHHmmss format
	 * 
	 * @param date
	 * @return
	 */
	static public String formatDateAsTimeStamp(Date date) {
		if (date == null)
			throw new IllegalArgumentException("Date is required.");

		return new SimpleDateFormat(TIMESTAMP).format(date);
	}

	/**
	 * format the date in yyyy-MM-dd HH:mm:ss format
	 * 
	 * @param date
	 * @return
	 */
	static public String formatDateAsString(Date date) {
		if (date == null)
			throw new IllegalArgumentException("Date is required.");
		String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return dateString;
	}
	static public String formatDateWithZoneAsString(Date date) {
		if (date == null)
			throw new IllegalArgumentException("Date is required.");
		String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);
		return dateString;
	}

	/**
	 * Used for unit testing.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS").format(CommonDateHelper.timestampToDate(1212670750644L)));
			System.out.println(CommonDateHelper.dateToTimetamp(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS").parse("2013-01-01 00:00:00.000")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
