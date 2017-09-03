package fr.tsadeo.app.japicgwtp.client.application.site.siteitem;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SiteItemModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindPresenterWidget(SiteItemPresenter.class, SiteItemPresenter.MyView.class, SiteItemView.class);
    }
}