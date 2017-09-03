package fr.tsadeo.app.japicgwtp.client.application.site;

import com.gwtplatform.mvp.client.UiHandlers;

import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;

public interface SiteUiHandlers extends UiHandlers {

	public void onChangeSearch(VoSearchCriteria searchCriteria);

	public void onClearSearch();
	
	public void onAddSite();
}
