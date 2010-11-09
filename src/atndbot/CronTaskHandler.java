package atndbot;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atndbot.util.AtndCalendar;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;

@SuppressWarnings("serial")
public class CronTaskHandler extends HttpServlet {
	@SuppressWarnings("unused")
	private static Logger logger = 
        Logger.getLogger(CronTaskHandler.class.getName());
	
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
		// fetch 2 months event data
		List<String> yyyymmddList = AtndCalendar.getYYYYMMDDList(30 * 2);
		
		// set task queue
		Queue queue = QueueFactory.getDefaultQueue();
		
		for (String yyyymmdd : yyyymmddList) {
			queue.add(url("/atndFetchHandler").param("yyyymmdd", yyyymmdd));
		}
    }
}
