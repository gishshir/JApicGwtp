package fr.tsadeo.app.japicgwtp.client.application.search;

import com.gwtplatform.mvp.client.UiHandlers;

import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;

//  describes what actions the current Presenter can do. 
public interface SearchUIHandlers extends UiHandlers {

	public void onChangeSearch(VoSearchCriteria searchCriteria);
	
	public void onClearSearch();
}
