package com.datamodel.anvizent.helper;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

final public class DateHelper {
	private DateHelper() {
	}

	private static Interval getInterval(DateTime endDate) {
		if (endDate == null) {
			return null;
		}
		DateTime startDate = (endDate.getMonthOfYear() == DateTimeConstants.JANUARY) ? endDate.minusMonths(13) : endDate.minusMonths(12);
		return new Interval(startDate, endDate);
	}

	public static Interval getEditableInterval(DateTime endDate) {
		return getInterval(endDate);
	}

	public static Interval getEditableInterval() {
		return getInterval(DateTime.now(DateTimeZone.UTC));
	}

	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance(Constants.UTC_TIMEZONE);
		return cal.get(Calendar.YEAR);
	}

	public static int getCurrentMonth() {
		Calendar cal = Calendar.getInstance(Constants.UTC_TIMEZONE);
		return cal.get(Calendar.MONTH) + 1;
	}

	public static String getCurrentMonthString() {
		return String.format("%02d", getCurrentMonth());
	}

	public static DateTime now() {
		return DateTime.now(DateTimeZone.UTC);
	}

	public static int getOffsetInMinutes(DateTime datetime) {
		if (datetime == null)
			return 0;
		return datetime.getZone().getOffset(datetime.getMillis()) / 1000 / 60;
	}

	public static DateTime getDateTimeFromUtcOffset(DateTime utc, int offsetInMinutes) {
		if (utc == null)
			return null;
		return utc.toDateTime(DateTimeZone.forOffsetMillis(offsetInMinutes * 60 * 1000));
	}

	public static DateTime getDateTimeFromDateAndTime(DateTime date, DateTime time, int offsetInMinutes) {
		if (date == null || time == null)
			return null;
		DateTime dateTime = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), time.getHourOfDay(), time.getMinuteOfHour(), 00,
				DateTimeZone.forOffsetMillis(offsetInMinutes * 60 * 1000));
		return dateTime;
	}
}
