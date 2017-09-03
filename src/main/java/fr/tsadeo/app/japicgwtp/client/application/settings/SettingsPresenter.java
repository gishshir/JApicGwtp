package fr.tsadeo.app.japicgwtp.client.application.settings;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import fr.tsadeo.app.japicgwtp.client.application.ApplicationPresenter;
import fr.tsadeo.app.japicgwtp.client.event.MenuChangeEvent;
import fr.tsadeo.app.japicgwtp.client.place.NameTokens;
public class SettingsPresenter extends Presenter<SettingsPresenter.MyView, SettingsPresenter.MyProxy>  {

	private static final Logger LOG = Logger.getLogger("SettingPresenter");
	interface MyView extends View  {
    }
    public static final NestedSlot SLOT_Settings = new NestedSlot();

    @NameToken(NameTokens.settings)
    @ProxyCodeSplit
    interface MyProxy extends ProxyPlace<SettingsPresenter> {
    }

    @Inject
    SettingsPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT);
        
    }
    
    
 // ------------------------------- override gwtp interfaces
 	@Override
 	protected void onBind() {
 		super.onBind();

 	}

 	@Override
 	protected void onReveal() {
 		LOG.config("SettingsPresenter onReveal()");
 		MenuChangeEvent.fire(this, NameTokens.Token.settings);
 		super.onReveal();
 	}
    
}