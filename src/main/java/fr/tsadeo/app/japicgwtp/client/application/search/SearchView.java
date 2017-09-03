package fr.tsadeo.app.japicgwtp.client.application.search;


import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import fr.tsadeo.app.japicgwtp.client.util.WidgetUtils;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;

class SearchView extends ViewWithUiHandlers<SearchUIHandlers> implements SearchPresenter.MyView {
    
	private static final Logger LOG = Logger.getLogger("SearchView");
	
	interface Binder extends UiBinder<Widget, SearchView> {
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
		LOG.config("on key up.........");
		LOG.config("native key code: " + event.getNativeEvent());
		LOG.config("any modifier key down: " + event.isAnyModifierKeyDown());
		if (!event.isAnyModifierKeyDown()) {
			this.searchCriterias();
		}
	}

//	@UiHandler("tbSearch")
//	void onTest(KeyDownEvent event) {
//		LOG.config("test..................");
//		if (this.tbSearch.getText() == null || this.tbSearch.getText().length() == 0) {
//			LOG.config("clear search..................");
//			this.getUiHandlers().onClearSearch();
//		}
//	}

	@UiField
	Container eframe;
	
	//---------------------------------- constructor
    @Inject
    SearchView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        super.bindSlot(SearchPresenter.SLOT_JAVADOC, eframe);
        super.bindSlot(SearchPresenter.SLOT_LIST_DATATYPE, list);
    }
    
	//---------------------------------------- overriding ViewImpl
    @Override
	public void addToSlot(Object slot, IsWidget content) {
		if (slot == SearchPresenter.SLOT_LIST_DATATYPE) {
			if (content != null) {
				list.add(content);
			}
		} else {
			super.addToSlot(slot, content);
		}
	}

    //----------------------------------------- implementing MyView
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
	
	//-------------------------------------- private methods

	private void searchCriterias() {
		VoSearchCriteria voCriteria = new VoSearchCriteria();
		voCriteria.setSearch(this.tbSearch.getValue());
		this.getUiHandlers().onChangeSearch(voCriteria);
	}
	
}