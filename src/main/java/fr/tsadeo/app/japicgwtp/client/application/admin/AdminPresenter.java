package fr.tsadeo.app.japicgwtp.client.application.admin;

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
public class AdminPresenter extends Presenter<AdminPresenter.MyView, AdminPresenter.MyProxy>  {
	private static final Logger LOG = Logger.getLogger("AdminPresenter");
    interface MyView extends View  {
    }
    public static final NestedSlot SLOT_Admin = new NestedSlot();

    @NameToken(NameTokens.admin)
    @ProxyCodeSplit
    interface MyProxy extends ProxyPlace<AdminPresenter> {
    }

    @Inject
    AdminPresenter(
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
    		LOG.config("AdminPresenter onReveal()");
    		MenuChangeEvent.fire(this, NameTokens.Token.admin);
    		super.onReveal();
    	}

}