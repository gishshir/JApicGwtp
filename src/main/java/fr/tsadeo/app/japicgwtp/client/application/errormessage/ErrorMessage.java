package fr.tsadeo.app.japicgwtp.client.application.errormessage;

import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;

public class ErrorMessage extends Composite {

    /**
     * The Interface Binder.
     */
    public interface Binder extends UiBinder<Row, ErrorMessage> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    @UiField
    Label lbTitle;

    @UiField
    Paragraph lbMessage;

    @Inject
    public ErrorMessage() {
        initWidget(BINDER.createAndBindUi(this));
    }

    public void setMessage (String title, String message) {
    	this.lbTitle.setText(title);
    	this.lbMessage.setText(message);
    }
}
