package fr.tsadeo.app.japicgwtp.server.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class TotoBean {

	  public TotoBean () {
	    	System.out.println("create new TotoBean !");
	    }
	    

		@PostConstruct
		public void logPostProcessing() {
			System.out.println("@PostConstruct for TotoBean !");
		}

}
