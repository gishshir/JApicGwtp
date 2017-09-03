package fr.tsadeo.app.japicgwtp.client.application.site.siteitem;


import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.util.StatusUtils;

class SiteItemView extends ViewWithUiHandlers<SiteItemUiHandlers> implements SiteItemPresenter.MyView {
    interface Binder extends UiBinder<Widget, SiteItemView> {
    }
    
    @UiHandler ("focusPanel")
    void onPanelClick(ClickEvent event) {
    	this.getUiHandlers().onSelectSiteItem();
    }
    
    @UiHandler ("btScan")
    void onScanButtonClick(ClickEvent event) {
    	event.stopPropagation();
    	this.getUiHandlers().onStartScan();
    }
  
    @UiHandler ("btEdit")
    void onEditButtonClick(ClickEvent event) {
    	event.stopPropagation();
    	this.getUiHandlers().onEditSite();
    }
    

    @UiHandler ("btDelete")
    void onDeleteButtonClick(ClickEvent event) {
    	event.stopPropagation();
    	if (this.modal.isVisible()){
			this.modal.hide();
		}
    	this.getUiHandlers().onDeleleSite();
    }
    
    @UiField
    Button btScan;
    
    @UiField
    Button btConfirmDelete;
    
    
    @UiField
    Panel siteItemPanel;

    @UiField
    Label lbScanStatus;
    
    @UiField
    Paragraph lbLastScan;
    
    @UiField
    Icon iconEnabled;
    
    @UiField
    Button btEdit;
    
    @UiField
    Paragraph lbSiteName;
    
    @UiField
	Modal modal;
    
    //----------------------------------- constructor
    @Inject
    SiteItemView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    //------------------------------------- implementing MyView
   
    @Override
	public void setSelected(boolean selected) {
		if (selected) {
			this.siteItemPanel.addStyleName("selected");
		} else {
			this.siteItemPanel.removeStyleName("selected");
		}
	}
    @Override
	public void setScanStatus(ScanStatus scanStatus) {
		
		StatusUtils.buildLabelScanStatus(this.lbScanStatus, scanStatus);
		
	}


	@Override
	public void setLastScan(String lastScan) {
		this.lbLastScan.setText(lastScan);
	}


	@Override
	public void setEnabled(boolean enabled, boolean spin) {
		
		if (enabled) {
			this.iconEnabled.setType(IconType.THUMBS_O_UP);
			this.iconEnabled.setColor("green");
			this.iconEnabled.setTitle("site enabled");
		} else {
			this.iconEnabled.setType(IconType.THUMBS_O_DOWN);
			this.iconEnabled.setColor("red");
			this.iconEnabled.setTitle("site disabled");
		}
		this.iconEnabled.setSpin(spin);
		
	}


	@Override
	public void setSiteName(String siteName) {
		this.lbSiteName.setHTML(siteName);
		
	}

	@Override
	public void showDeleteButton(boolean show) {
		this.btConfirmDelete.setVisible(show);
	}
    
}