package atndbot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AtndCalendar {
	/**
	 * return comma separated YYYYMMDD string.
	 * date start from today to parameter 'days' future
	 * 
	 * example; 20101103,20101104,20101105,...
	 * 
	 * @param days range
	 * @return comma separated YYYYMMDD string
	 */
	public static String getYYYYMMDDString(int days) {
		return concat(getYYYYMMDDList(days));
	}
	
	/**
	 * return YYYYMMDD string list.
	 * date start from today to parameter 'days' future
	 * 
	 * example; "20101103", "20101104", "20101105",...
	 * 
	 * @param days range
	 * @return comma separated YYYYMMDD string
	 */
	public static List<String> getYYYYMMDDList(int days) {
		List<String> yyyymmddList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < days; i++) {
			yyyymmddList.add(getYYYYMMDD(cal));
			cal.add(Calendar.DATE, 1);
		}
		return yyyymmddList;	
	}
	
	private static String concat(List<String> strList) {
		String ret = "";
		for (String s : strList) {
			ret += (s + ",");
		}
		return ret.substring(0, ret.length() - 1);
	}
	
	private static String getYYYYMMDD(final Calendar cal, String format) {
		// JST needs to add 9 hours
		Calendar c = (Calendar) cal.clone();
		c.add(Calendar.HOUR, 9);
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DATE);
		return String.format(format, year, month + 1, date);
	}
	
	private static String getYYYYMMDD(final Calendar cal) {
		return getYYYYMMDD(cal, "%04d%02d%02d");
	}
	
	/**
	 * get YYYYMMDD string from java.util.Date object.
	 * 
	 * @param date 
	 * @return YYYYMMDD string
	 */
	public static String getYYYYMMDD(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return getYYYYMMDD(cal);
	}
	
	/**
	 * get YYYY/MM/DD string from java.util.Date object.
	 * 
	 * @param date 
	 * @return YYYY/MM/DD string
	 */
	public static String getYYYYMMDDwithSlash(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return getYYYYMMDD(cal, "%04d/%02d/%02d");
	}
	
	/**
	 * get 'YYYY/MM/DD 00:00:00' date object from YYYYMMDD string.
	 * 
	 * @param YYYYMMDD string 
	 * @return 'YYYY/MM/DD 00:00:00' date object
	 */
	public static Date getDate(String yyyymmdd) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyyMMdd");
		Date date = null;
		try {
			date = sdf.parse(yyyymmdd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * get utc 'YYYY/MM/(DD - 1) 15:00:00' date object
	 * from jst 'YYYY/MM/DD 00:00:00' date object
	 * 
	 * @param jst 'YYYY/MM/DD 00:00:00' date object
	 * @return utc 'YYYY/MM/(DD - 1) 15:00:00' date object
	 */
	public static Date jstToUtc(final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, -9);
		return cal.getTime();
	}
	
	/**
	 * get 'next date' date object from date one.
	 * 
	 * @param 'YYYY/MM/DD 00:00:00' date object
	 * @return 'YYYY/MM/(DD + 1) 00:00:00' date object
	 */
	public static Date getNext(final Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getYYYYMMDDString(30));
		System.out.println(getDate("20101205"));
		System.out.println(getNext(getDate("20101205")));
	}

}
