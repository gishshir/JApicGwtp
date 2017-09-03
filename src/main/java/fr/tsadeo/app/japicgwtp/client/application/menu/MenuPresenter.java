package fr.tsadeo.app.japicgwtp.client.application.menu;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

import fr.tsadeo.app.japicgwtp.client.application.login.LoginPresenter;
import fr.tsadeo.app.japicgwtp.client.event.ConnectUserEvent;
import fr.tsadeo.app.japicgwtp.client.event.ConnectUserEvent.ConnectUserHandler;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent.ItemAction;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent.ItemActionHandler;
import fr.tsadeo.app.japicgwtp.client.event.MenuChangeEvent;
import fr.tsadeo.app.japicgwtp.client.event.MenuChangeEvent.MenuChangeHandler;
import fr.tsadeo.app.japicgwtp.client.place.NameTokens;
import fr.tsadeo.app.japicgwtp.client.util.RefreshTimer;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ConnectOrGetUserAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ConnectOrGetUserResult;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;

public class MenuPresenter extends Presenter<MenuPresenter.MyView, MenuPresenter.MyProxy>
		implements MenuUIHandlers, ConnectUserHandler, ItemActionHandler, MenuChangeHandler {

	private static final Logger LOG = Logger.getLogger("MenuPresenter");
	
	private static final int USER_REFRESH = 1000 * 60 * 2; // 1000 * 60 * 15;

	interface MyView extends View, HasUiHandlers<MenuUIHandlers> {

		public void setConnected(boolean connected);

		public void activeDisconnectConfirmation(boolean active);

		public void setLogin(String login);
		
		public void activeButton(NameTokens.Token place);
	}

	@ProxyStandard
	interface MyProxy extends Proxy<MenuPresenter> {
	}

	private final DispatchAsync dispatcher;
	private final LoginPresenter loginPresenter;
	private VoUser currentUser;
	private RefreshTimer userTimer;

	// --------------------------------- constructor
	@Inject
	MenuPresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatcher,
			LoginPresenter loginPresenter) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.loginPresenter = loginPresenter;
		this.getView().setUiHandlers(this);

		this.addVisibleHandler(ConnectUserEvent.TYPE, this);
		this.addVisibleHandler(ItemActionEvent.TYPE, this);
		this.addVisibleHandler(MenuChangeEvent.TYPE, this);
	}
	
	//----------------------------------- overriding PresenterWidget
	@Override
	protected void onBind() {
		super.onBind();
		this.doGetCurrentUser(false);
		
		this.userTimer = new RefreshTimer(USER_REFRESH, USER_REFRESH, new Command() {
			
			@Override
			public void execute() {
				doGetCurrentUser(true);
			}
		});
		this.userTimer.doScheduleRepeting(Integer.MAX_VALUE);
	}

	
	// -------------------------------- implementing ConnectUserHandler
	@Override
	public void onUserConnect(VoUser user) {
		this.currentUser = user;
		this.getView().setConnected(true);
		this.getView().setLogin(user.getLogin());
	}

	@Override
	public void onUserDisconnect() {
		this.currentUser = null;
		this.getView().setConnected(false);
		this.getView().setLogin(null);
	}

	// ---------------------------------- implementing MenuUIHandlers
	@Override
	public void onClickButtonConnection() {

		if (Objects.nonNull(this.currentUser)) {
			this.doDisconnectUser();
		} else {
			// display login popup
			this.displayLoginPresenter();
		}
	}
	
	//-------------------------------- implementing MenuChangeHandler
	public void onChange(MenuChangeEvent event) {
		this.getView().activeButton(event.getPlace());
	}

	//--------------------------------- implementing ItemActionHandler
	@SuppressWarnings("incomplete-switch")
	@Override
	public void onAction(ItemActionEvent event) {

		if (Objects.isNull(event)) {
			return;
		}
		ItemAction action = event.getAction();
		LOG.config("MenuPresenter ItemAction: " + action.name());

		if (action == ItemAction.edit) {

			switch (event.getState()) {
			case inProgress:
				this.getView().activeDisconnectConfirmation(true);
				break;

			default:
				this.getView().activeDisconnectConfirmation(false);
				break;
			}
		}
	}

	// -------------------------- private methods
	private void displayLoginPresenter() {

		super.addToPopupSlot(this.loginPresenter);
	}

	private void doGetCurrentUser(boolean fireEvent) {

		this.dispatcher.execute(new ConnectOrGetUserAction(ConnectOrGetUserAction.Type.getUser),
				new AsyncCallback<ConnectOrGetUserResult>() {

					@Override
					public void onFailure(Throwable caught) {
						LOG.severe("Get user error: " + caught.getMessage());
					}

					@Override
					public void onSuccess(ConnectOrGetUserResult result) {
						
						if (fireEvent) {
							fireChangeUser(result.get());
						}
						if (Objects.nonNull(result.get())) {
							onUserConnect(result.get());
						} else {
							onUserDisconnect();
						}
					}
				});
	}
	
	private void fireChangeUser(VoUser voUser){
		
		if (Objects.isNull(this.currentUser) && Objects.isNull(voUser)) {
			return;
		}
		
		if (Objects.nonNull(this.currentUser) && Objects.nonNull(voUser)) {
			
			if (this.currentUser.getId() == voUser.getId()) {
				return;
			}
		}
		
		ConnectUserEvent.fire(this, voUser);
		
		
	}

	private void doDisconnectUser() {

		this.dispatcher.execute(new ConnectOrGetUserAction(ConnectOrGetUserAction.Type.disconnect),
				new AsyncCallback<ConnectOrGetUserResult>() {

					@Override
					public void onFailure(Throwable caught) {
						LOG.severe("Disconnect user error: " + caught.getMessage());
					}

					@Override
					public void onSuccess(ConnectOrGetUserResult result) {
						ConnectUserEvent.fire(MenuPresenter.this, null);
					}
				});

	}

}