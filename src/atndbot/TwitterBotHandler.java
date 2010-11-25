package atndbot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import atndbot.model.Event;
import atndbot.util.AtndCalendar;
import atndbot.util.ConsumerKey;
import atndbot.util.EventFactory;
import atndbot.util.PMF;

import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class TwitterBotHandler extends HttpServlet {
	private static final int TWEET_MAX_LEN = 140;
	private static final String TWEET_ITEM_SEPARATOR = ", ";
	
    private static Logger logger =
    	Logger.getLogger(TwitterBotHandler.class.getName());
    private static TwitterFactory factory = new TwitterFactory();
    
    private AccessToken accessToken = null;
    private Twitter twitter = null;
    private int remain_tweet = 0;
	
    public TwitterBotHandler() {
	}
	
    @Override
    public void init() throws ServletException {
        super.init();
        
		// initialize twitter bot
        ConsumerKey.init(this);
		accessToken = new AccessToken(ConsumerKey.token,
				ConsumerKey.tokenSecret);
		twitter = factory.getOAuthAuthorizedInstance(ConsumerKey.key,
				ConsumerKey.secret, accessToken);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    	// get List object from task payload
    	ObjectInputStream ois = new ObjectInputStream(req.getInputStream());
		try {
			List<Long> idList = (List<Long>) ois.readObject();
			if (idList.size() == 0)
				return;
			
			// get new event info
			PersistenceManager pm = PMF.getInstance().getPersistenceManager();
			Query query = pm.newQuery(Event.class);
			query.setFilter("id == :idList");
			List<Event> newEvents = (List<Event>) query.execute(idList);
			remain_tweet = newEvents.size();
			
			// tweet events
			for (Event event : newEvents) {
				tweet(event);
			}
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "failed to read task payload, " +
            		"remain " + remain_tweet + " tweets", e);
		}
    }
    
    private void tweet(Event event) {
		// TODO: event filtering with keywords
    	
        String tweet = getTweetMessage(event);
        if (tweet.length() > 140)
            tweet = tweet.substring(0, 140);
        
		// tweet event info, contains keywords
        try {
	        twitter.updateStatus(tweet);
	        logger.info("tweet: \"" + tweet + "\"");
	        remain_tweet -= 1;
		} catch (TwitterException e) {
	        logger.warning("tweet failed: \"" + tweet + "\"");
			if (e.exceededRateLimitation()) {
				logger.warning("failed to twitter API call, " +
						"because of exceeded rate limitation, " +
						"remain " + remain_tweet + " tweets");
	        } else {
				logger.log(Level.WARNING, "failed to twitter API call, " +
						"remain " + remain_tweet + " tweets", e);	        	
	        }
		}
	}
    
    private static String getTweetMessage(Event event) {
    	String message = "";
    	Date started_at = event.getStarted_at();
    	if (started_at != null) {
    		message += AtndCalendar.getYYYYMMDDwithSlash(started_at) +
    			TWEET_ITEM_SEPARATOR;
    	}
    	String address = event.getAddress();
    	if (address != null && !address.equals("")) {
    		message += address + TWEET_ITEM_SEPARATOR;
    	}
    	Text event_url = event.getEvent_url();
    	String event_url_str = event_url.getValue();
    	if (event_url_str != null && !event_url_str.equals("")) {
    		message += event_url_str;
    	}
    	if (message.length() > 140) {
    		logger.log(Level.INFO,
    				"tweet message size is over 140 chars: " +
    				event.getTitle() + "," + 
    				event.getEvent_url());
    		return event.getTitle() + event.getEvent_url();
    	} else {
        	String title = event.getTitle() + TWEET_ITEM_SEPARATOR +
        		event.getTitleCatch();
        	int len = min(title.length(), TWEET_MAX_LEN - message.length() -
        			TWEET_ITEM_SEPARATOR.length());
        	return title.substring(0, len) + TWEET_ITEM_SEPARATOR + message;
    	}
    }
    
    private static int min(int x, int y) {
    	return (x < y) ? x : y;
	}
    
    public static void main(String[] args) throws Exception {    	
    	Map<String, String> info = new HashMap<String, String>();
    	info.put("event_id", "10188");
    	info.put("title", "Scalaを遊ぶ!会 第5回");
    	info.put("catch", "「Play! frameworkで遊ぼう(3)」");
    	info.put("event_url", "http://atnd.org/events/10188");
    	info.put("started_at", "2010-12-09T19:30:00+09:00");
    	Event event = EventFactory.getEvent(info);
    	
    	ConsumerKey.key = "********************";
    	ConsumerKey.secret = "****************************************";
    	ConsumerKey.token = "**************************************************";
    	ConsumerKey.tokenSecret = "****************************************";
    	
    	TwitterBotHandler tb = new TwitterBotHandler();
    	tb.tweet(event);
	}
}
