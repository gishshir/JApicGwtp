package fr.tsadeo.app.japicgwtp.client.application;


import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.LockInteractionEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;

import fr.tsadeo.app.japicgwtp.client.application.menu.MenuPresenter;
import fr.tsadeo.app.japicgwtp.client.event.NotificationEvent;
import fr.tsadeo.app.japicgwtp.client.event.NotificationEvent.Level;
import fr.tsadeo.app.japicgwtp.client.event.NotificationEvent.NotificationHandler;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy> 
              implements NotificationHandler {
	
    interface MyView extends View {
    	public void showLoading(boolean visible);
    	public void showNotification(String title, String errorMessage, Level level);
    }

    static final PermanentSlot<MenuPresenter> SLOT_MENU = new PermanentSlot<>();
  public static final NestedSlot SLOT_CONTENT = new NestedSlot();

    /*
     * A Proxy alone is often used on a Presenter that is not a Place and is not a PresenterWidget. For instance,
     *  a root Presenter used as a layout for an application would use a Proxy instead of a ProxyPlace.
     */
    @ProxyStandard
    interface MyProxy extends Proxy<ApplicationPresenter> {
    }

	private MenuPresenter menuPresenter;


	
	//-------------------------------- constructor
    @Inject
    ApplicationPresenter(EventBus eventBus,
                         MyView view,
                         MyProxy proxy,
                         MenuPresenter menuPresenter) {
    	/*
    	 * RevealType.Root will fire a RevealRootContentEvent. 
    	 * This event is fired by a Presenter that desires to reveal itself at the root of the application.
    	 *  This type of content is usually meant to use the browser like a regular webpage,
    	 *  adding a vertical scrollbar as the content overflows.
    	 */
        super(eventBus, view, proxy, RevealType.Root);
        super.addVisibleHandler(NotificationEvent.TYPE, this);
        this.menuPresenter = menuPresenter;
        
    }
    
    @Override
    protected void onBind() {
    
      super.setInSlot(SLOT_MENU, this.menuPresenter);
    }
    
    /**
     * We display a short lock message whenever navigation is in progress.
     *
     * @param event The {@link LockInteractionEvent}.
     */
    @ProxyEvent
    public void onLockInteraction(LockInteractionEvent event) {
        getView().showLoading(event.shouldLock());
    }

    //---------------------------------- implementing NotificationHandler
    @Override
	public void onNotify(String title, String message, Level level) {
		this.getView().showNotification(title, message, level);
	}
   
}