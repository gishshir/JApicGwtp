package fr.tsadeo.app.japicgwtp.client.application.site.siteitem;

import com.gwtplatform.mvp.client.UiHandlers;

public interface SiteItemUiHandlers extends UiHandlers {
	
	public void onSelectSiteItem();
	
	public void onStartScan();
	
	public void onEditSite();

	public void onDeleleSite();

}
