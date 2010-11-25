package atndbot.util;

import java.util.Date;

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
}
