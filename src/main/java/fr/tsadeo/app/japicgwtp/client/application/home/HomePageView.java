package fr.tsadeo.app.japicgwtp.client.application.home;


import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

class HomePageView extends ViewImpl implements HomePagePresenter.MyView {
    interface Binder extends UiBinder<Widget, HomePageView> {
    }

    
    @Inject
    HomePageView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        

    }
    
}