package fr.tsadeo.app.japicgwtp.client.application.site;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

import fr.tsadeo.app.japicgwtp.client.application.site.siteitem.SiteItemModule;
import fr.tsadeo.app.japicgwtp.client.application.site.editsite.EditSiteModule;

public class SiteModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new EditSiteModule());
		install(new SiteItemModule());
		bindPresenter(SitePresenter.class, SitePresenter.MyView.class, SiteView.class, SitePresenter.MyProxy.class);
    }
}