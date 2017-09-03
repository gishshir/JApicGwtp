package fr.tsadeo.app.japicgwtp.client.application.admin;


import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

class AdminView extends ViewImpl implements AdminPresenter.MyView {
    interface Binder extends UiBinder<Widget, AdminView> {
    }

    @Inject
    AdminView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}