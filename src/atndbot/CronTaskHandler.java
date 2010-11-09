package atndbot;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atndbot.model.Event;
import atndbot.util.AtndCalendar;
import atndbot.util.PMF;

@SuppressWarnings("serial")
public class CronTaskHandler extends HttpServlet {
	private static Logger logger = 
        Logger.getLogger(CronTaskHandler.class.getName());
	
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    	PersistenceManager pm = null;
    	try {
	    	pm = PMF.getInstance().getPersistenceManager();
	    	
			// fetch 1 months event data
			List<String> yyyymmddList = AtndCalendar.getYYYYMMDDList(30);
			
			// debug: fetch only today
			List<Event> eventList = AtndParser.getEventList(yyyymmddList.get(0));
			
			// persistent process
			for (Event event : eventList) {
				pm.makePersistent(event);
			}

	    	logger.info("" + eventList.size());
    	} finally {
    		if (pm != null && !pm.isClosed())
    			pm.close();
    	}
    }

}
