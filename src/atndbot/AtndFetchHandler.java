package atndbot;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atndbot.model.Event;
import atndbot.twitter.EventType;
import atndbot.util.AtndFetch;
import atndbot.util.PMF;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;

@SuppressWarnings("serial")
public class AtndFetchHandler extends HttpServlet {
	private static Logger logger = 
        Logger.getLogger(AtndFetchHandler.class.getName());

    @SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
    	PersistenceManager pm = null;
    	try {
	    	pm = PMF.getInstance().getPersistenceManager();
			Query query = pm.newQuery("SELECT * FROM atndbot.model.Event");
			List<Event> oldList = (List<Event>) query.execute();
			
			// fetch only 1 day
	    	String yyyymmdd = req.getParameter("yyyymmdd");
			List<Event> eventList = AtndFetch.getEventList(yyyymmdd);
			
			// persistent new and/or update event
			pm.makePersistentAll(eventList);
			
			List<Event> newList = subtract(eventList, oldList);
			List<Event> deletedList = subtract(oldList, eventList);
			List<Event> updatedList = subtract(oldList, deletedList);
			
			// delete non exist event
			pm.deletePersistentAll(deletedList);
			
	    	logger.info(String.format(
	    			"Task Queue: fetch %d events, and new %d, updated %d, " +
	    			"deleted %d, started_at %s", eventList.size(),
	    			newList.size(), updatedList.size(), deletedList.size(),
	    			yyyymmdd));
			
	    	// no new event, quit this handler.
	    	if (newList.size() == 0)
	    	 	return;
	    	
	    	// make payload data only new event
	    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	ObjectOutputStream oos = new ObjectOutputStream(bos);
	    	oos.writeObject(EventType.NEW_EVENT);
	    	oos.writeObject(getEventIdList(newList));
	    	oos.close();
	    	
	    	// set task queue
	    	logger.log(Level.INFO, "payload data size: " + bos.size() + " bytes.");
			Queue queue = QueueFactory.getQueue("atnd-event-tweet-queue");
			queue.add(url("/twitterBotHandler").payload(bos.toByteArray(),
					"application/x-java-serialized-object"));
    	} finally {
    		if (pm != null && !pm.isClosed())
    			pm.close();
    	}
    }
    
    private static List<Long> getEventIdList(List<Event> eventList) {
    	List<Long> idList = new ArrayList<Long>();
    	for (Event event : eventList) {
    		idList.add(event.getId());
		}
		return idList;
	}
    
    private static <T> List<T> subtract(
    		final List<T> fromList, final List<T> toList) {
    	List<T> returnList = new ArrayList<T>(fromList);
    	returnList.removeAll(toList);
    	return returnList;
    }
}
