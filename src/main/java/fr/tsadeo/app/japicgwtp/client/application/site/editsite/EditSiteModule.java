package fr.tsadeo.app.japicgwtp.client.application.site.editsite;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class EditSiteModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(EditSitePresenter.class, EditSitePresenter.MyView.class, EditSiteView.class, EditSitePresenter.MyProxy.class);
    }
}