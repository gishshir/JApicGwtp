package fr.tsadeo.app.japicgwtp.client.application.menu;

import java.util.Objects;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.NavbarButton;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import fr.tsadeo.app.japicgwtp.client.place.NameTokens.Token;

class MenuView extends ViewWithUiHandlers<MenuUIHandlers> implements MenuPresenter.MyView {
	interface Binder extends UiBinder<Widget, MenuView> {
	}


	// fonctionnement standard sans confirmation
	@UiHandler("btConnectDisconnect")
	void onClickLink(ClickEvent event) {
			this.getUiHandlers().onClickButtonConnection();
	}

	// disconnection after show confirmation popup
	@UiHandler("btDisconnect")
	void onClickButtonDisconnect(ClickEvent event) {
		if (this.modal.isVisible()){
			this.modal.hide();
		}
		this.getUiHandlers().onClickButtonConnection();
	}

	@UiField
	NavbarButton btConnectDisconnect;
	
	@UiField
	NavbarButton btConfirmDisconnect;
	@UiField
	AnchorListItem btHome;
	@UiField
	AnchorListItem btSearch;
	@UiField
	AnchorListItem btSite;
	@UiField
	AnchorListItem btSettings;
	@UiField
	AnchorListItem btAdmin;
	

	@UiField
	Button btDisconnect;
	
	@UiField
	Modal modal;

	// -------------------------------- constructor
	@Inject
	MenuView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	// -------------------------------------------- implementing MyView
	@Override
	public void activeButton(Token place) {
		if (Objects.isNull(place)){
			return;
		}
		AnchorListItem button = null;
		switch (place) {
		case home: button = this.btHome;
			break;

		case search: button = this.btSearch;
			break;
			
		case site: button = this.btSite;
			break;
			
		case settings: button = this.btSettings;
			break;
			
		case admin: button = this.btAdmin;
			break;
		}
		this.activeMenuButton(button);
	}

	@Override
	public void setConnected(boolean connected) {

		if (connected) {
			this.btConnectDisconnect.setIcon(IconType.SIGN_OUT);
			this.btConnectDisconnect.setTitle("disconnect user");

		} else {
			this.btConnectDisconnect.setIcon(IconType.SIGN_IN);
			this.btConnectDisconnect.setTitle("connection");
		}

	}

	@Override
	public void activeDisconnectConfirmation(boolean active) {
		
		this.btConfirmDisconnect.setVisible(active);
		this.btConnectDisconnect.setVisible(!active);
		
		
	}

	@Override
	public void setLogin(String login) {
		
		if(Objects.nonNull(login)) {
			this.btConnectDisconnect.setText(login);
			this.btConfirmDisconnect.setText(login);	
		} else {
			this.btConnectDisconnect.setText("Connection");
		}
	}
	
	//------------------------------------ private methods
	private void activeMenuButton(AnchorListItem item) {
		this.btHome.setActive(this.btHome == item);
		this.btSite.setActive(this.btSite == item);
		this.btSearch.setActive(this.btSearch == item);
		this.btSettings.setActive(this.btSettings == item);
		this.btAdmin.setActive(this.btAdmin == item);
	}


}