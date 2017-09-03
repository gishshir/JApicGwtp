package fr.tsadeo.app.japicgwtp.client.application.login;

import com.gwtplatform.mvp.client.UiHandlers;

interface LoginUiHandlers extends UiHandlers {
	
	public void onValidate();
	
	public void onReset();
	
	public void onChangeDatas();
}