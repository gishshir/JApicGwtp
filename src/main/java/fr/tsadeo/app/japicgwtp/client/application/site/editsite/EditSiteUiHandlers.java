package fr.tsadeo.app.japicgwtp.client.application.site.editsite;

import com.gwtplatform.mvp.client.UiHandlers;

import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForEdit;

public interface EditSiteUiHandlers extends UiHandlers {
	
	enum InputData {name, url, scanUrl, version, enabled}
	
	public void onUpdateSite();

	public void onResetDatas();
	
	public void onModifyDatas(InputData inputData);
}
