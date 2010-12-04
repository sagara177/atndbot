package atndbot;

import java.io.IOException;
import java.io.ObjectInputStream;
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
import atndbot.twitter.EventMessage;
import atndbot.twitter.EventMessageNew;
import atndbot.twitter.EventMessageToday;
import atndbot.twitter.EventType;
import atndbot.twitter.TwitterBot;
import atndbot.util.ConsumerKey;
import atndbot.util.PMF;

@SuppressWarnings("serial")
public class TwitterBotHandler extends HttpServlet {
    private static Logger logger =
    	Logger.getLogger(TwitterBotHandler.class.getName());
    private static TwitterBot twitterBot = null;
    private int remain_tweet = 0;
	
    @Override
    public void init() throws ServletException {
    	super.init();
    	
    	// initialize twitter bot
    	ConsumerKey.init(this);
    	twitterBot = new TwitterBot();
    }
    
    @SuppressWarnings("unchecked")
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    	// get List object from task payload
    	ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
		try {
			EventType tweetType = (EventType) ois.readObject();
			List<Long> idList = (List<Long>) ois.readObject();
			if (idList.size() == 0)
				return;
			
			// get new event info
			PersistenceManager pm = PMF.getInstance().getPersistenceManager();
			Query query = pm.newQuery(Event.class);
			query.setFilter("id == :idList");
			List<Event> eventList = (List<Event>) query.execute(idList);
			
			// tweet events
			List<String> messageList = new ArrayList<String>();
			EventMessage tweetEvent = null;
			switch (tweetType) {
			case NEW_EVENT:
				tweetEvent = new EventMessageNew();
				break;
			case TODAY_EVENT:
				tweetEvent = new EventMessageToday();
				break;
			default:
				logger.warning("unknown event type: " + tweetType.name());
			}
			if (tweetEvent == null)
				return;
			
			messageList = tweetEvent.getTweetMessages(eventList);
			remain_tweet = messageList.size();
			for (String message : messageList) {
				twitterBot.tweet(message);
				// logger.info(message);
			}
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "failed to read task payload, " +
            		"remain " + remain_tweet + " tweets", e);
		}
    }
}
