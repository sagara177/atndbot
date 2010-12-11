package atndbot.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import atndbot.model.Event;

public class EventFilter {
	public static boolean isOneDayEvent(Event event, String yyyymmdd) {
		if (event == null)
			return false;
		// Date.getTime() returns msec.
		Date start = event.getStarted_at();
		Date end = event.getEnded_at();
		long diff = 0;
		if (start != null && end != null) {
			diff = end.getTime() - start.getTime();
			double day = (double)diff / (24 * 60 * 60 * 1000);
			return day < 1;
		} else if (start != null && end == null) {
			return AtndCalendar.getYYYYMMDD(start).equals(yyyymmdd);
		} else if (start == null && end != null) {
			return false;
		} else { // start == null && end == null
			return false;
		}
	}
	
	public static boolean isStartToday(Event event, String yyyymmdd) {
		if (event == null)
			return false;
		// Date.getTime() returns msec.
		Date start = event.getStarted_at();
		Date today = AtndCalendar.jstToUtc(
				AtndCalendar.getDate(yyyymmdd));
		Date tomorrow = AtndCalendar.getNext(today);
		return today.before(start) && tomorrow.after(start);
	}
	
	public static void main(String[] args) {
		// test must be done with utc+0 timezone settings
    	Map<String, String> info = new HashMap<String, String>();
    	info.put("event_id", "9243");
    	info.put("started_at", "2010-12-31T23:50:00+09:00");
    	info.put("ended_at", "2011-01-01T00:05:00+09:00");
    	Event event = EventFactory.getEvent(info);
    	System.out.println(event.getStarted_at());
    	System.out.println(isStartToday(event, "20101231"));
    	System.out.println(isStartToday(event, "20110101"));
	}
}
