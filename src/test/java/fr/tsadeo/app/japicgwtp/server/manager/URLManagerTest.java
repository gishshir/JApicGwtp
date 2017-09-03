package fr.tsadeo.app.japicgwtp.server.manager;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.core.io.Resource;

import fr.tsadeo.app.japicgwtp.server.domain.converter.ResourceConverter;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;

public class URLManagerTest  {
	
	private final static Log LOG = LogFactory.getLog(URLManagerTest.class);
	
	static final String HOST_OK = "https://jsoup.org/apidocs/";
    static final String PAGE_OK = HOST_OK + "allclasses-frame.html";
	
	static final String HOST_WRONG = "https://xxx.org/apidocs/";
	static final String PAGE_WRONG = HOST_OK + "toto.html";
	static final String HOST_PAGE_WRONG = HOST_WRONG + "toto.html";
	
	private static final String HOST_403 = "http://static.javadoc.io/org.powermock/powermock-api-mockito/1.6.2/index.html";
	
	private static final String HOST_OPENCSV = "http://www.atetric.com/atetric/javadoc/net.sf.opencsv/opencsv/2.3/index.html";
	

	private static URLManager manager = new URLManager();
	private static ResourceConverter resourceConverter = new ResourceConverter();
	
	 @Test
	    public void testURLHostOpenCsv() throws Exception{
	    	
	    	LOG.info("testURLHostOpenCsv()");
	    	
	    	Resource resource = resourceConverter.toResource(HOST_OPENCSV);
	    	boolean result = manager.isURLAlive(resource.getURL());
	    	
	    	assertTrue("the URL must be alive!", result);
	    	
	    }
	

    @Test
    public void testURLHostOK() throws Exception{
    	
    	LOG.info("testURLHostOK()");
    	
    	Resource resource = resourceConverter.toResource(HOST_OK);
    	boolean result = manager.isURLAlive(resource.getURL());
    	
    	assertTrue("the URL must be alive!", result);
    	
    }
    
    @Test
    public void testURLHost403OK() throws Exception{
    	
    	LOG.info("testURLHost403OK()");
    	
    	Resource resource = resourceConverter.toResource(HOST_403);
    	boolean result = manager.isURLAlive(resource.getURL());
    	
    	assertTrue("the URL must be alive!", result);
    	
    }
    
    @Test
    public void testURLPageOK() throws Exception{
    	LOG.info("testURLPageOK()");
    	Resource resource = resourceConverter.toResource(PAGE_OK);
    	boolean result = manager.isURLAlive(resource.getURL());
    	
    	assertTrue("the URL must be alive!", result);
    	
    }

    @Test
    public void testURLPageWrong() throws Exception{
    	LOG.info("testURLPageWrong()");
    	Resource resource = resourceConverter.toResource(PAGE_WRONG);
    	boolean result = manager.isURLAlive(resource.getURL());
    	
    	assertFalse("the URL cannot be alive!", result);
    	
    }


    @Test
    public void testUrlHostWrong() throws Exception{
    	LOG.info("testUrlHostHostWrong()");
    	Resource resource = resourceConverter.toResource(HOST_WRONG);
    	boolean result = manager.isURLAlive(resource.getURL());
    	
    	assertFalse("the URL cannot be alive!", result);
    	
    }

        
    @Test
    public void testUrlHostAndPageWrong() throws Exception{
    	LOG.info("testUrlHostAndPageWrong()");
    	Resource resource = resourceConverter.toResource(HOST_PAGE_WRONG);
    	boolean result = manager.isURLAlive(resource.getURL());
    	
    	assertFalse("the URL cannot be alive!", result);
    	
    }

    
    
    //===============================
    //      URL STATE
    //===============================
    
    @Test
    public void testGetUrlStateURLHostOK() throws Exception{
    	
    	LOG.info("testGetUrlStateURLHostOK()");
    	
    	Resource resource = resourceConverter.toResource(HOST_OK);
    	UrlState result = manager.getUrlState(resource);
    	
    	assertNotNull("urlState cannot be null!", result);
    	assertEquals("Wrong urlState!", UrlState.Alive, result);
    	
    }
    
    @Test
    public void testGetUrlStateURLPageOK() throws Exception{
    	LOG.info("testGetUrlStateURLPageOK()");
    	Resource resource = resourceConverter.toResource(PAGE_OK);
    	UrlState result = manager.getUrlState(resource);
    	
    	assertNotNull("urlState cannot be null!", result);
    	assertEquals("Wrong urlState!", UrlState.Alive, result);
    	
    }

    @Test
    public void testGetUrlStateURLPageWrong() throws Exception{
    	LOG.info("testGetUrlStateURLPageWrong()");
    	Resource resource = resourceConverter.toResource(PAGE_WRONG);
    	UrlState result = manager.getUrlState(resource);
    	
    	assertNotNull("urlState cannot be null!", result);
    	assertEquals("Wrong urlState!", UrlState.PageNoFound, result);
    	
    }


    @Test
    public void testGetUrlStateUrlHostWrong() throws Exception{
    	LOG.info("testGetUrlStateUrlHostHostWrong()");
    	Resource resource = resourceConverter.toResource(HOST_WRONG);
    	UrlState result = manager.getUrlState(resource);
    	
    	assertNotNull("urlState cannot be null!", result);
    	assertEquals("Wrong urlState!", UrlState.WrongHost, result);
    	
    }

        
    @Test
    public void testGetUrlStateUrlHostAndPageWrong() throws Exception{
    	LOG.info("testGetUrlStateUrlHostAndPageWrong()");
    	Resource resource = resourceConverter.toResource(HOST_PAGE_WRONG);
    	UrlState result = manager.getUrlState(resource);
    	
    	assertNotNull("urlState cannot be null!", result);
    	assertEquals("Wrong urlState!", UrlState.WrongHost, result);
    	
    }

	
	
}
