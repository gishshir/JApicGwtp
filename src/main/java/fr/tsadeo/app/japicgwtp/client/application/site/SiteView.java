package fr.tsadeo.app.japicgwtp.client.application.site;


import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import fr.tsadeo.app.japicgwtp.client.util.WidgetUtils;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;

class SiteView extends ViewWithUiHandlers<SiteUiHandlers> implements SitePresenter.MyView {
    interface Binder extends UiBinder<Widget, SiteView> {
    }

    @UiField
	Input tbSearch;
	
	@UiField
	Button btClear;
	
	@UiHandler ("btClear")
	void onClickClearSearch(ClickEvent event) {
		this.getUiHandlers().onClearSearch();
	}

	@UiField
	Label response;

	@UiField
	Panel list;

	@UiHandler("tbSearch")
	void onChange(KeyUpEvent event) {
		VoSearchCriteria voCriteria = new VoSearchCriteria();
		voCriteria.setSearch(this.tbSearch.getValue());
		this.getUiHandlers().onChangeSearch(voCriteria);
	}
	@UiHandler("btCreate")
	void onClickAddSite(ClickEvent event) {
        this.getUiHandlers().onAddSite();
	}

//	@UiField
//	Panel eframe;
	
	@UiField
	Container panelDetail;

	//--------------------------------------- constructor
    @Inject
    SiteView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        super.bindSlot(SitePresenter.SLOT_DETAIL, this.panelDetail);
        super.bindSlot(SitePresenter.SLOT_LIST_SITE, list);
    }

    //---------------------------------- implementing MyView
    @Override
    public void setSearch(String search) {
    	this.tbSearch.setText(search);
    }
    @Override
	public void setServerResponse(String message) {

		this.response.setText(message);
	}
	@Override
	public void clearDatas() {
	   this.list.clear();	
	}
	@Override
	public void clearSearch() {
		this.tbSearch.setValue(null);
	}

	@Override
	public void focusSearch() {
		WidgetUtils.focusWidget(this.tbSearch);		
	}
    
}