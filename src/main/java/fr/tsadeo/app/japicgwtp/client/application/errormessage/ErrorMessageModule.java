package fr.tsadeo.app.japicgwtp.client.application.errormessage;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ErrorMessageModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindPresenterWidget(ErrorMessagePresenter.class, ErrorMessagePresenter.MyView.class, ErrorMessageView.class);
    }
}