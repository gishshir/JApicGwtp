package fr.tsadeo.app.japicgwtp.client.application.search.datatype;


import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import fr.tsadeo.app.japicgwtp.shared.domain.JavaType;

class DataTypeView extends ViewWithUiHandlers<DataTypeUiHandlers> implements DataTypePresenter.MyView {
    interface Binder extends UiBinder<Widget, DataTypeView> {
    }

    @UiHandler ("focusPanel")
    void onClick(ClickEvent event) {
    	this.getUiHandlers().onSelectDataType();
    }
    @UiField
    Panel dataTypePanel;
    
    @UiField
    Label lbJavaType;
    
    @UiField
    Label lbSiteName;
    @UiField
    Paragraph lbPackageName;
    @UiField
    Paragraph lbDatatypeName;
    
    //----------------------------- constructor
    @Inject
    DataTypeView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    //--------------------------------------- implementing MyView
    public void setDataTypeName(String htmlHighlightedName) {
    	this.lbDatatypeName.setHTML(htmlHighlightedName);
    }

	@Override
	public void setSelected(boolean selected) {
		if (selected) {
			this.dataTypePanel.addStyleName("selected");
		} else {
			this.dataTypePanel.removeStyleName("selected");
		}
	}

	@Override
	public void setJavaType(JavaType javaType) {
		
		LabelType labelType = LabelType.DEFAULT;
		this.lbJavaType.setText(javaType.getText());
		
		switch (javaType) {
		
		case TAnnotation: labelType = LabelType.WARNING;
			break;

		case TClass: labelType = LabelType.SUCCESS;
			break;

		case TEnum: labelType = LabelType.INFO;
			break;

		case TInterface: labelType = LabelType.PRIMARY;
			break;
		}
		this.lbJavaType.setType(labelType);
		
	}

	@Override
	public void setSiteName(String siteName) {
		this.lbSiteName.setText(siteName);
	}

	@Override
	public void setPackageName(String packageName) {
		this.lbPackageName.setText(packageName);
		this.lbPackageName.setTitle(packageName);
	}

	@Override
	public void setVisible(boolean visible) {
		this.dataTypePanel.setVisible(visible);
	}
}