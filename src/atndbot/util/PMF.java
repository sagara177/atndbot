package atndbot.util;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PMF {
	private static PersistenceManagerFactory instance = null;
	
	private PMF() {
	}
	
	public static PersistenceManagerFactory getInstance() {
		if (instance == null)
			instance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		return instance;
	}
}
