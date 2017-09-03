package fr.tsadeo.app.japicgwtp.client.application.login;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.Input;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

import fr.tsadeo.app.japicgwtp.client.util.WidgetUtils;

class LoginView extends PopupViewWithUiHandlers<LoginUiHandlers> implements LoginPresenter.MyView {
	interface Binder extends UiBinder<Widget, LoginView> {
	}

	@UiHandler("btClose")
	void onClikButtonClose(ClickEvent event) {
		this.hide();
	}

	@UiHandler("btValidate")
	void onClickButtonValidate(ClickEvent event) {

		this.getUiHandlers().onValidate();
	}

	@UiHandler("btReset")
	void onClickButtonReset(ClickEvent event) {
		this.getUiHandlers().onReset();
	}
	
	@UiHandler("tbLogin")
	void onChangeInputLogin(KeyUpEvent event) {
		this.getUiHandlers().onChangeDatas();
	}
	@UiHandler("tbPassword")
	void onChangeInputPassword(KeyUpEvent event) {
		this.getUiHandlers().onChangeDatas();
	}
	@UiHandler("tbLogin")
    void onBlurInputLogin(BlurEvent event) {
		WidgetUtils.focusWidget(this.tbPassword);
	}

	@UiHandler("tbPassword")
	void onBlurInputPassword(BlurEvent event) {
		WidgetUtils.focusWidget(this.btValidate);
	}

	@UiField
	Form form;

	@UiField
	Input tbLogin;

	@UiField
	Input tbPassword;

	@UiField
	Button btValidate;

	@UiField
	HTMLPanel errors;

	// -------------------------------- constructor
	@Inject
	LoginView(EventBus eventBus, Binder uiBinder) {
		super(eventBus);
		initWidget(uiBinder.createAndBindUi(this));
		super.bindSlot(LoginPresenter.SLOT_ERRORS, this.errors);
	}

	// ---------------------------------- implementing MyView

	@Override
	public void focusLogin() {
		WidgetUtils.focusWidget(this.tbLogin);
	}

	@Override
	public String getLogin() {
		return this.tbLogin.getText();
	}

	@Override
	public String getPassword() {
		return this.tbPassword.getText();
	}

	@Override
	public void clearDatas() {
		this.tbLogin.setText(null);
		this.tbPassword.setText(null);
		this.focusLogin();
	}

	@Override
	public boolean isFormValid() {
		return this.form.validate();
	}

	// ------------------------------------ private methods


}