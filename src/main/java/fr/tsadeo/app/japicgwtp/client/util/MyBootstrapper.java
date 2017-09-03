package fr.tsadeo.app.japicgwtp.client.util;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class MyBootstrapper implements Bootstrapper {
	
	private static final Logger LOG = Logger.getLogger("MyBootstrapper");

	private final PlaceManager placeManager;

	@Inject
	public MyBootstrapper(PlaceManager placeManager) {
		this.placeManager = placeManager;
	}

	@Override
	public void onBootstrap() {

		String token = this.placeManager.getCurrentPlaceRequest().getNameToken();
		LOG.config("onBootstrap(" + token); 
		placeManager.revealCurrentPlace();
	}

}
