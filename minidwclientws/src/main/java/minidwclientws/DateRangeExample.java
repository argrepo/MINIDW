package minidwclientws;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateRangeExample
{
	
	public static void main1(String[] args) throws Exception
	{
		String url = "http://anvizdevlogin.dwpractice.com/anvizentcoreapi";
		URL aURL = new URL(url);

		System.out.println("protocol = " + aURL.getProtocol()); //http
		System.out.println("authority = " + aURL.getAuthority()); //example.com:80
	
	    String enviromentURL = aURL.getProtocol()+"://"+aURL.getAuthority();
	    System.out.println(enviromentURL);
	
	
	}
	
	public static void main(String[] args) throws Exception
	{
		String format = "yyyy-MM-dd'T'HH:mm:ss";
		String formatdate = convertDateFormat(format,"2019-02-19","Asia/Kolkata");
		System.out.println(formatdate);
        String date = getEndDateByInterval(formatdate,format, 30);
        System.out.println(date);
	}

 	static String getEndDateByInterval(String fromDate, String dateFormat, int interval) throws java.text.ParseException
	{
		  DateFormat formatter = new SimpleDateFormat(dateFormat); 
		  Date  startDate = (Date) formatter.parse(fromDate); 
		  
		  Calendar c = Calendar.getInstance();
	      c.setTime(startDate);
	      c.add(Calendar.DATE, interval);
	      Date calculatedDateAndTime = c.getTime();
	      
          return formatter.format(calculatedDateAndTime);
	}
	
	static String convertDateFormat(String requiredDateFormat, String date, String timeZone) throws Exception, java.text.ParseException
	{

		int dayOfYear = LocalDate.parse(date).getDayOfYear();
		int year = LocalDate.parse(date).getYear();

		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		c.set(1, 0, 1, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, dayOfYear);

		SimpleDateFormat sdf = new SimpleDateFormat(requiredDateFormat);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

		return sdf.format(c.getTime()).toString();
	}
	
}
