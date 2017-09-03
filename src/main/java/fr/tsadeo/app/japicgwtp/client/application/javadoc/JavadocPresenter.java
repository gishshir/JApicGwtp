package fr.tsadeo.app.japicgwtp.client.application.javadoc;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.google.web.bindery.event.shared.EventBus;

import fr.tsadeo.app.japicgwtp.client.application.common.IPresenter;
import fr.tsadeo.app.japicgwtp.client.util.WindowUtils;

public class JavadocPresenter extends Presenter<JavadocPresenter.MyView, JavadocPresenter.MyProxy>
		implements IPresenter {

	private static final Logger LOG = Logger.getLogger("JavadocPresenter");

	interface MyView extends View {
	}

	@ProxyStandard
	interface MyProxy extends Proxy<JavadocPresenter> {
	}

	// ------------------------------------ constructor
	@Inject
	JavadocPresenter(EventBus eventBus, MyView view,MyProxy proxy) {
		super(eventBus, view, proxy);

	}

	
	//------------------------------ implementing PresenterWidget

	@Override
	protected void onHide() {
		LOG.config("JavadocPresenter onHide...");
		super.onHide();
	}



	@Override
	protected void onReveal() {
		LOG.config("JavadocPresenter onReveal...");
		super.onReveal();
	}



	// ----------------------------------------- public methods
	public void showHtmlPage(String pageName, String link, boolean iframeAllowed) {

		if (iframeAllowed) {
			WindowUtils.setSrcToIFrame(TARGET_API_FRAME, link);
		} else {
			WindowUtils.openPopup(pageName, link, 600, 900);
		}
	}

}