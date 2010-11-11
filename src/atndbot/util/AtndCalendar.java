package atndbot.util;

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
	
	private static String getYYYYMMDD(final Calendar cal) {
		// JST needs to add 9 hours
		Calendar c = (Calendar) cal.clone();
		c.add(Calendar.HOUR, 9);
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DATE);
		return String.format("%04d%02d%02d", year, month + 1, date);
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
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getYYYYMMDDString(30));
	}

}
