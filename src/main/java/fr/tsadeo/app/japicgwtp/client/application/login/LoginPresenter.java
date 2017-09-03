package fr.tsadeo.app.japicgwtp.client.application.login;

import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.view.CenterPopupPositioner;
import com.gwtplatform.mvp.client.view.PopupPositioner;

import fr.tsadeo.app.japicgwtp.client.application.errormessage.ErrorMessagePresenter;
import fr.tsadeo.app.japicgwtp.client.event.ConnectUserEvent;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ConnectOrGetUserAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ConnectOrGetUserResult;

public class LoginPresenter extends PresenterWidget<LoginPresenter.MyView> implements LoginUiHandlers {

	private static final Logger LOG = Logger.getLogger("LoginPresenter");

	interface MyView extends PopupView, HasUiHandlers<LoginUiHandlers> {

		public void focusLogin();

		public String getLogin();

		public String getPassword();

		public void clearDatas();

		public boolean isFormValid();
	}

	static final PermanentSlot<ErrorMessagePresenter> SLOT_ERRORS = new PermanentSlot<>();
	private final ErrorMessagePresenter errorMessagePresenter;

	private final DispatchAsync dispatcher;

	// ----------------------------- constructor
	@Inject
	LoginPresenter(EventBus eventBus, MyView view, DispatchAsync dispatcher,
			ErrorMessagePresenter errorMessagePresenter) {
		super(eventBus, view);
		this.dispatcher = dispatcher;
		this.errorMessagePresenter = errorMessagePresenter;

		getView().setUiHandlers(this);
		getView().setPopupPositioner(this.buildPopupPositionner());
	}

	// -------------------------------------- overriding PresenterWidget
	@Override
	protected void onReveal() {
		super.onReveal();
		this.getView().clearDatas();
//		this.getView().enableButtons(false);
	}

	@Override
	protected void onBind() {
		LOG.config("LoginPresenter onBind...");
		super.onBind();
		super.setInSlot(SLOT_ERRORS, this.errorMessagePresenter);
	};

	// ------------------------------------------ implementing LoginHandlers
	@Override
	public void onValidate() {

		this.doAuthenticate(this.getView().getLogin(), this.getView().getPassword());
	}

	@Override
	public void onReset() {
		this.getView().clearDatas();
		this.errorMessagePresenter.hideErrorMessages();
	}

	@Override
	public void onChangeDatas() {
		this.errorMessagePresenter.hideErrorMessages();
	}

	// ---------------------------------------- private methods
	private PopupPositioner buildPopupPositionner() {
		return new CenterPopupPositioner() {

			protected int getTop(int popupHeight) {
				return 100;
			}
		};
	}

	private void doAuthenticate(String login, String password) {

		this.errorMessagePresenter.hideErrorMessages();
		this.dispatcher.execute(new ConnectOrGetUserAction(login, password),
				new AsyncCallback<ConnectOrGetUserResult>() {

					@Override
					public void onFailure(Throwable caught) {
						LOG.severe("Error in ConnectOrGetUser authentication: " + caught.getMessage());
						addErrorMessage("Error", "login or password incorrect!");

					}

					@Override
					public void onSuccess(ConnectOrGetUserResult result) {
						getView().hide();
						ConnectUserEvent.fire(LoginPresenter.this, result.get());
					}
				});
	}

	private void addErrorMessage(String title, String message) {
		this.errorMessagePresenter.addErrorMessage(title, message);
	}


}