package com.anvizent.minidw.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeZoneDateHelper {
	
	protected static final Log LOG = LogFactory.getLog(TimeZoneDateHelper.class);
	
	public static void main(String[] args) throws ParseException {
		System.out.println(getConvertedDateStringByTimeZone("Mon Oct 30 00:18:16 UTC 2017","EST"));;
		System.out.println(getConvertedDateStringByTimeZone(new Date().toString(),"EST"));;
		System.out.println(getConvertedDateByTimeZone("Mon Oct 30 00:18:16 UTC 2017"));;
		System.out.println(getFormattedDateString(new Date()));
	}
	
	
	public static Date getConvertedDateByTimeZone(String sourceDateStr) {
		if ( StringUtils.isBlank(sourceDateStr) ) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		try {
			return sdf.parse(sourceDateStr);
		} catch (Exception e) {
			LOG.error("Unable to Convert the date",e);
		}
		return null;
	}
	public static String getConvertedDateStringByTimeZone(String sourceDateStr,String timeZoneStr) throws ParseException {
		if ( StringUtils.isBlank(sourceDateStr) ) {
			return null;
		}
		TimeZone timeZone = StringUtils.isNotBlank(timeZoneStr) ? TimeZone.getTimeZone(timeZoneStr) : TimeZone.getDefault();
		SimpleDateFormat sdfAs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		Date date = sdfAs.parse(sourceDateStr);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		sdf.setTimeZone(timeZone);
		
		try {
			return sdf.format(date).toString();
		} catch (Exception e) {
			LOG.error("Unable to Convert the date " + sourceDateStr,e);
		}
		return sourceDateStr;
	}
	
	public static String getFormattedDateString(Date date) {
		if ( date == null ) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		return sdf.format(date).toString();
	}
	public static String getFormattedDateString() {
		return getFormattedDateString(new Date());
	}
	
	
	
}
