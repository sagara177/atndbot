package atndbot.util.datastore;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atndbot.model.Event;
import atndbot.util.PMF;

/**
 * Datastore data deletion class.
 * 
 * this handler delete *all* Event data.
 * you SHOULD delete one, and servlet settings in web.xml,
 * for production release
 * 
 * @author sagara
 *
 */
@SuppressWarnings("serial")
public class EventDeleteHandler extends HttpServlet {
	private static Logger logger = 
        Logger.getLogger(EventDeleteHandler.class.getName());

    @SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    	PersistenceManager pm = null;
    	try {
	    	pm = PMF.getInstance().getPersistenceManager();
			
			// query all event
	    	Query query = pm.newQuery(Event.class);
			List<Event> eventList = (List<Event>) query.execute();
			
			// delete all
			pm.deletePersistentAll(eventList);
			
	    	logger.info(String.format(
	    			"delete all event, %d events deleted",
	    			eventList.size()));
    	} finally {
    		if (pm != null && !pm.isClosed())
    			pm.close();
    	}
    }
}
