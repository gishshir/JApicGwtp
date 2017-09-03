package fr.tsadeo.app.japicgwtp.client.application.errormessage;

import java.util.logging.Logger;

import javax.inject.Provider;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class ErrorMessagePresenter extends PresenterWidget<ErrorMessagePresenter.MyView> {

	private static final Logger LOG = Logger.getLogger("ErrorMessagePresenter");

	interface MyView extends View {

		public void addErrorMessage(ErrorMessage errorMessage);

		public void clearDatas();
	}

	private final IndirectProvider<ErrorMessage> errorMessageFactory;
	private boolean displayed = false;

	// -------------------------------- constructor
	@Inject
	ErrorMessagePresenter(EventBus eventBus, MyView view, Provider<ErrorMessage> provider) {
		super(eventBus, view);
		this.errorMessageFactory = new StandardProvider<ErrorMessage>(provider);

	}

	// ------------------------------------- public method
	public void hideErrorMessages() {
		if (this.displayed) {
			this.getView().clearDatas();
			this.displayed = false;
		}
	}

	public void addErrorMessage(String title, String message) {

		this.displayed = true;
		this.errorMessageFactory.get(new AsyncCallback<ErrorMessage>() {

			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Echec in ErrorMessageFactory.get...: " + caught.getMessage());
			}

			@Override
			public void onSuccess(ErrorMessage result) {
				LOG.config("Success in getting new ErrorMessage...");
				result.setMessage(title, message);
				getView().addErrorMessage(result);
			}
		});

	}

}