/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.manager;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import fr.tsadeo.app.japicgwtp.shared.util.JApicException;

/**
 *
 * @author sylvie
 */
@Component
public class ResourceManager {

	private final static Log LOG = LogFactory.getLog(ResourceManager.class);
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    
    @PostConstruct
	public void logPostConstruct() throws JApicException {
		LOG.info("@PostConstruct of ProfileManager");
    
    }
    public Resource getResource(String url) {
        return this.resourceLoader.getResource(url);
    }
}
