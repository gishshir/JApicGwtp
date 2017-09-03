package fr.tsadeo.app.japicgwtp.client.application.search.datatype;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class DataTypeModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(DataTypePresenter.class, DataTypePresenter.MyView.class, DataTypeView.class);
    }
}