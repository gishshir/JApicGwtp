package fr.tsadeo.app.japicgwtp.client.application.home;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import fr.tsadeo.app.japicgwtp.client.application.ApplicationPresenter;
import fr.tsadeo.app.japicgwtp.client.event.MenuChangeEvent;
import fr.tsadeo.app.japicgwtp.client.place.NameTokens;

public class HomePagePresenter extends Presenter<HomePagePresenter.MyView, HomePagePresenter.MyProxy> {

	private static final Logger LOG = Logger.getLogger("HomePresenter");

	interface MyView extends View {
	}

	@ProxyStandard
	@NameToken(NameTokens.home)
	interface MyProxy extends ProxyPlace<HomePagePresenter> {
	}

	@Inject
	HomePagePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT);

	}

	// ------------------------------- override gwtp interfaces
	@Override
	protected void onBind() {
		super.onBind();

	}

	@Override
	protected void onReveal() {
		LOG.config("HomePresenter onReveal()");
		MenuChangeEvent.fire(this, NameTokens.Token.home);
		super.onReveal();
	}

}