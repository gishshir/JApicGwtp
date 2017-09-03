package fr.tsadeo.app.japicgwtp.client.application.javadoc;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class JavadocModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
    	bindPresenterWidget(JavadocPresenter.class, JavadocPresenter.MyView.class, JavadocView.class);
    }
}