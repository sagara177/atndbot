package atndbot;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import atndbot.model.Event;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Text;

public class EventFactory {

	public static Event getEvent(Map<String, String> info) {
		Event e = new Event();
		e.setId(Long.parseLong(info.get("event_id")));
		e.setTitle(getString(info, "title"));
		e.setTitleCatch(getString(info, "catch"));
		e.setDescription(getText(info, "description"));
		e.setEvent_url(getText(info, "event_url"));
		e.setStarted_at(getDate(info, "started_at"));
		e.setLimit(getInt(info, "limit"));
		e.setAddress(getString(info, "address"));
		e.setPlace(getString(info, "place"));
		e.setLat_lon(getGeoPt(info, "lat", "lon"));
		e.setAccepted(getInt(info, "accepted"));
		e.setWaiting(getInt(info, "waiting"));
		e.setUpdated_at(getDate(info, "updated_at"));
		return e;
	}

	private static String getString(Map<String, String> info, String key) {
		String value = null;
		if (info.containsKey(key))
			value = info.get(key);
		if (value == null)
			value = "";
		return value;
	}

	private static Text getText(Map<String, String> info, String key) {
		Text value = new Text(getString(info, key));
		return value;
	}
	
	/**
	 * 
	 * @param info
	 * @param key
	 * @return int value, or -1
	 */
	private static int getInt(Map<String, String> info, String key) {
		int value = -1;
		try {
			String valueStr = getString(info, key);
			if (valueStr == "")
				return value;
			value = Integer.parseInt(valueStr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 
	 * @param info
	 * @param key
	 * @return Float object, or -1
	 */
	private static Float getFloat(Map<String, String> info, String key) {
		float value = -1;
		try {
			String valueStr = getString(info, key);
			if (valueStr == "")
				return value;
			value = Float.parseFloat(valueStr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 
	 * @param info
	 * @param key
	 * @return java.util.Date object, or null
	 */
	private static Date getDate(Map<String, String> info, String key) {
		Date value = null;
		String dateStr = getString(info, key);
		if (dateStr == null || dateStr.equals(""))
			return null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("yyyy-MM-dd'T'HH:mm:ssZ");
			// escape SimpleDateFormat unsupported ISO8601 problem
			String noColon = "";
			noColon += dateStr.substring(0, 22);
			noColon += dateStr.substring(23);
			value = sdf.parse(noColon);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 
	 * @param info
	 * @param keyLat
	 * @param keyLon
	 * @return GeoPt object, or null
	 */
	private static GeoPt getGeoPt(Map<String, String> info, String keyLat, String keyLon) {
		GeoPt value = null;
		try {
			float latitude = getFloat(info, "lat");
			float longitude = getFloat(info, "lon");
			if (latitude != -1 && longitude != -1)
				value = new GeoPt(latitude, longitude);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}
}
