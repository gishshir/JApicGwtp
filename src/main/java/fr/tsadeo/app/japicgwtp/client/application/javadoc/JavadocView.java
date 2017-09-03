package fr.tsadeo.app.japicgwtp.client.application.javadoc;


import org.gwtbootstrap3.client.ui.gwt.HTMLPanel;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

class JavadocView extends ViewImpl implements JavadocPresenter.MyView {
	
    interface Binder extends UiBinder<Widget, JavadocView> {
    }

    


    @Inject
    JavadocView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    //------------------------------------ implementing MyView

}