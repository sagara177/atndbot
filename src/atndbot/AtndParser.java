package atndbot;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import atndbot.model.Event;
import atndbot.util.AtndCalendar;

public class AtndParser {
	
	public static List<Event> getEventList() {
		List<Event> eventList = new ArrayList<Event>();
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		
		// fetch 3 months event data
		List<String> calendar = AtndCalendar.getYYYYMMList(3);
		for (String yyyymm : calendar) {
			int start = 0;
			int results_returned = 0;
			while (results_returned < 100) {
				URL url;
				InputSource is;
				NodeList events = null;
				try {
					url = new URL("http://api.atnd.org/events/?ym=" + yyyymm +
							"&start=" + start + "&count=100");
					System.out.println(url);
					is = new InputSource(url.openStream());
					
					// parse event info
					events = (NodeList) xPath.evaluate("/hash/events/event",
							is, XPathConstants.NODESET);
					for (int i = 0; i < events.getLength(); i++) {
						Element event = (Element) events.item(i);
						
						Map<String, String> info = new HashMap<String, String>();
						info.put("event_id", xPath.evaluate("event_id", event));
						info.put("title", xPath.evaluate("title", event));
						info.put("catch", xPath.evaluate("catch", event));
						info.put("description", xPath.evaluate("description", event));
						info.put("event_url", xPath.evaluate("event_url", event));
						info.put("started_at", xPath.evaluate("started_at", event));
						info.put("limit", xPath.evaluate("limit", event));
						info.put("address", xPath.evaluate("address", event));
						info.put("place", xPath.evaluate("place", event));
						info.put("lat", xPath.evaluate("lat", event));
						info.put("lon", xPath.evaluate("lon", event));
						info.put("accepted", xPath.evaluate("accepted", event));
						info.put("waiting", xPath.evaluate("waiting", event));
						info.put("updated_at", xPath.evaluate("updated_at", event));
						Event e = EventFactory.getEvent(info);
						eventList.add(e);

						String eventTitle = xPath.evaluate("title", event);
						String eventStarted_at = xPath.evaluate("started_at", event);
						System.out.println(eventTitle + ", " + eventStarted_at);
					}
					
					// update results_returned, and increment start position
					Double result = (Double) xPath.evaluate("/hash/results_returned", 
							is, XPathConstants.NUMBER);
					results_returned = result.intValue();
					start += 100;
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (XPathExpressionException e1) {
					e1.printStackTrace();
				}
			}
		}
		return eventList;
	}
	
	public static void main(String[] args) throws Exception {
		getEventList();
	}
}
