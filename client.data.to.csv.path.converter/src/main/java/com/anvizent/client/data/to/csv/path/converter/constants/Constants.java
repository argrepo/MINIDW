package com.anvizent.client.data.to.csv.path.converter.constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public class Constants {
	public static class General {
		public static final String PROPERTIES_PLACE_HOLDER = "PROPERTIES_PLACE_HOLDER";
		public static final String SCHEDULER = "SCHEDULER";
		public static final String PACKAGE_GROUP = "PACKAGE_GROUP";
		public static final String DATA = "DATA";
		public static final String USERID = "USERID";
	}

	

	public static class Config {
		public static final String APP_ID = "MINIDW_WEBAPP";
		public static final String MINIDW_SERVICE_CONFIG = "C:/_MINIDW/minidw_webservice.config";

		public static final String ENCRYPTION_KEY = "anvizent";
		public static final String RESOURCES = "/resources/";
		public static final int BUFFER_SIZE = 4096;
		public static final String WEBAPP_TRUE = "true";
		public static final String WEBAPP_FALSE = "false";
		public static final int PACKAGE_NAME_MIN = 3;
		public static final int PACKAGE_NAME_MAX = 255;
		public static final String DEFAULT_DATE = "1900-01-01 00:00:00";
		public static final String XREF = "X-";
		public static final String CSV_ENCODING_TYPE = "UTF-8";
		public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
		public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
		public static final String SUCCESS = "SUCCESS";
		public static final String ERROR = "ERROR";
		public static final String FAILED = "FAILED";
		public static final String ERROR_XREF = "ERROR_XREF";
		public static final String DUPL_FILE_NAME = "DUPL_FILE_NAME";
		public static final String PROCEED_FOR_MAPPING = "PROCEED_FOR_MAPPING";

	}

	public static synchronized Date getTime(String date, String timeZoneString) throws ParseException {
		if ( StringUtils.isBlank(date) ) {
			return null;
		}
		TimeZone timeZone = (timeZoneString == null || timeZoneString.isEmpty()) ? TimeZone.getDefault() : TimeZone.getTimeZone(timeZoneString);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(timeZone);
		return sdf.parse(date);
	}
}
