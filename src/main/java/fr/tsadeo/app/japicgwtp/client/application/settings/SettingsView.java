package fr.tsadeo.app.japicgwtp.client.application.settings;


import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

class SettingsView extends ViewImpl implements SettingsPresenter.MyView {
    interface Binder extends UiBinder<Widget, SettingsView> {
    }


    @Inject
    SettingsView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}