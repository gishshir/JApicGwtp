package fr.tsadeo.app.japicgwtp.server.service;

import javax.annotation.PostConstruct;

import fr.tsadeo.app.japicgwtp.shared.util.JApicException;

public abstract class AbstractService implements IService {

	
	  @PostConstruct
	    public void logPostConstruct()  throws JApicException {
	    	getLog().info("@PostConstruct of " + this.getClass().getName());
	    }

	
	
}
