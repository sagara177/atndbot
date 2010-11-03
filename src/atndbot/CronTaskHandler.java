package atndbot;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atndbot.model.Event;

public class CronTaskHandler {
	private static Logger logger = 
        Logger.getLogger(CronTaskHandler.class.getName());
	
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    	List<Event> eventList = AtndParser.getEventList();
    	logger.info("" + eventList.size());
    }

}
