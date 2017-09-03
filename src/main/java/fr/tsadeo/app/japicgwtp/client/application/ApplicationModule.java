package fr.tsadeo.app.japicgwtp.client.application;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

import fr.tsadeo.app.japicgwtp.client.application.admin.AdminModule;
import fr.tsadeo.app.japicgwtp.client.application.home.HomeModule;
import fr.tsadeo.app.japicgwtp.client.application.javadoc.JavadocModule;
import fr.tsadeo.app.japicgwtp.client.application.menu.MenuModule;
import fr.tsadeo.app.japicgwtp.client.application.search.SearchModule;
import fr.tsadeo.app.japicgwtp.client.application.settings.SettingsModule;
import fr.tsadeo.app.japicgwtp.client.application.site.SiteModule;
import fr.tsadeo.app.japicgwtp.client.application.login.LoginModule;
import fr.tsadeo.app.japicgwtp.client.application.errormessage.ErrorMessageModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
    	install(new ErrorMessageModule());
		install(new LoginModule());
		install(new JavadocModule());
        install(new AdminModule());
		install(new SettingsModule());
		install(new SiteModule());
		install(new MenuModule());
		install(new SearchModule());
		install(new HomeModule());

        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
    }
}