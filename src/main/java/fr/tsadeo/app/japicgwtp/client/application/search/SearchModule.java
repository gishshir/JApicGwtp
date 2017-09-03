package fr.tsadeo.app.japicgwtp.client.application.search;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

import fr.tsadeo.app.japicgwtp.client.application.javadoc.JavadocModule;
import fr.tsadeo.app.japicgwtp.client.application.search.datatype.DataTypeModule;

public class SearchModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(SearchPresenter.class, SearchPresenter.MyView.class, SearchView.class, SearchPresenter.MyProxy.class);
    
		install(new DataTypeModule());
		
    }
}