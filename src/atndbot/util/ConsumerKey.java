package atndbot.util;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public class ConsumerKey {
    private static Logger logger =
    	Logger.getLogger(ConsumerKey.class.getName());
    
    public static String key;
    public static String secret;
    public static String token;
    public static String tokenSecret;
    
    public static void init(HttpServlet servlet){
        if (key != null) 
            return;
        ServletContext ctx = servlet.getServletConfig().getServletContext();
        key =    ctx.getInitParameter("consumerKey");
        secret = ctx.getInitParameter("consumerSecret");
        token = ctx.getInitParameter("accessToken");
        tokenSecret = ctx.getInitParameter("accessTokenSecret");

        if (key == null || secret == null ||
        		token == null || tokenSecret == null) { 
            logger.severe("failed to get consumer keys " +
                        "as init-param in the web.xml. " +
                        "Check the file again.");
        }
    }
}
