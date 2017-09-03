package fr.tsadeo.app.japicgwtp.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTest {


	protected Log LOG = LogFactory.getLog("TEST");
	  
    //------------------------------------ protected methods
    protected void logWithIndex (String text, int index) {
		LOG.info("[" + index + "] " + text);
	}
}
