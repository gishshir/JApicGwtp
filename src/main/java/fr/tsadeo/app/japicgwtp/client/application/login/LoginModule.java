package fr.tsadeo.app.japicgwtp.client.application.login;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class LoginModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindSingletonPresenterWidget(LoginPresenter.class, LoginPresenter.MyView.class, LoginView.class);
    }
}