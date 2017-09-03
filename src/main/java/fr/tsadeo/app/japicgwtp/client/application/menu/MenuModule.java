package fr.tsadeo.app.japicgwtp.client.application.menu;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class MenuModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(MenuPresenter.class, MenuPresenter.MyView.class, MenuView.class, MenuPresenter.MyProxy.class);
    }
}