package com.anvizent.client.scheduler.constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public class Constant {
	public static class General {
		public static final String PROPERTIES_PLACE_HOLDER = "PROPERTIES_PLACE_HOLDER";
		public static final String SCHEDULER = "SCHEDULER";
		public static final String SCHEDULER_REST_UTILITY = "SCHEDULER_REST_UTILITY";
		public static final String PACKAGE_REST_UTILITY = "PACKAGE_REST_UTILITY";
		public static final String LOGIN_REST_UTILITY = "LOGIN_REST_UTILITY";
		public static final String PACKAGE_GROUP = "PACKAGE_GROUP";
		public static final String STANDARD_PACKAGE_PROCESSOR = "STANDARD_PACKAGE_PROCESSOR";
		public static final String DATA = "DATA";
		public static final String USER_ID = "USER_ID";
		public static final String CLIENT_ID = "CLIENT_ID";
		public static final String DL_ID = "DL_ID";
		public static final String PLAIN_CLIENT_ID = "PLAIN_CLIENT_ID";
		public static final String BROWSER_DETAILS = "BROWSER_DETAILS";
		public static final String DEPLOYMENT_TYPE = "DEPLOYMENT_TYPE";
		public static final String S3_BUCKET_INFO = "S3_BUCKET_INFO";
		public static final String FILE_SETTINGS = "FILE_SETTINGS";
		public static final String PACKAGE_EXECUTION = "PACKAGE_EXECUTION";  
		public static final String MESSAGE_SOURCE = "MESSAGE_SOURCE"; 
		public static final String USER_INFO = "USER_INFO";
		public static final String PACKAGE_ID = "PACKAGE_ID";
		public static final String TIME_ZONE = "TIME_ZONE";
		public static final String SCHUDULE_ID = "SCHUDULE_ID";
		public static final String JOB_EXECUTION_RERQUIRED = "JOB_EXECUTION_RERQUIRED";
		public static final String CSV_FOLDER_PATH = "CSV_FOLDER_PATH";
		public static final String LOCALE = "LOCALE";
	}

	
	public static synchronized Date getTime(String date, String timeZoneString) throws ParseException {
		if ( StringUtils.isBlank(date) ) {
			return null;
		}
		TimeZone timeZone = (timeZoneString == null || timeZoneString.isEmpty()) ? TimeZone.getDefault() : TimeZone.getTimeZone(timeZoneString);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setTimeZone(timeZone);
		return dateFormat.parse(date);
	}
	public static void main(String[] args) throws ParseException {
		System.out.println(getTime("2017-10-06 03:00:85.996", "UTC"));
		//System.out.println(getTime("2017-10-06 03:00", "UTC"));
	}

}
