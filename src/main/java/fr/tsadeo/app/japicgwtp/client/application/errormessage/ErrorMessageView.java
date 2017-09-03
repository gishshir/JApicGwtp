package fr.tsadeo.app.japicgwtp.client.application.errormessage;

import javax.inject.Inject;

import org.gwtbootstrap3.client.ui.Well;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class ErrorMessageView extends ViewImpl implements ErrorMessagePresenter.MyView {
    interface Binder extends UiBinder<Widget, ErrorMessageView> {
    }


	@UiField
	Well listErrors;

    @Inject
    ErrorMessageView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    
    //-------------------------------- implementing MyView
    @Override
	public void addErrorMessage(ErrorMessage errorMessage) {
    	if (!this.listErrors.isVisible()) {
    		this.listErrors.setVisible(true);
    	}
		this.listErrors.add(errorMessage);	
	}
    
    @Override
    public void clearDatas() {
    	this.listErrors.clear();
    	this.listErrors.setVisible(false);
    }
}