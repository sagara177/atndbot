package atndbot;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atndbot.model.Event;
import atndbot.util.AtndCalendar;
import atndbot.util.AtndFetch;

@SuppressWarnings("serial")
public class AtndbotServlet extends HttpServlet {
	private static Logger logger = 
        Logger.getLogger(AtndbotServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		long start = System.currentTimeMillis();
		
		resp.setContentType("text/plain; charset=utf-8");
		// resp.getWriter().println("Hello, world");
		
		// fetch 1 months event data
		List<String> yyyymmddList = AtndCalendar.getYYYYMMDDList(30);
		
		// debug: fetch only today
		List<Event> eventList = AtndFetch.getEventList(yyyymmddList.get(0));
		for (Event event : eventList) {
			resp.getWriter().println(event);
		}
		
		long end = System.currentTimeMillis();
		logger.info("Timings: " + (end - start) + " ms");
	}
}
