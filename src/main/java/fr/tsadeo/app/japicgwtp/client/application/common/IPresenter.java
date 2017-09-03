package fr.tsadeo.app.japicgwtp.client.application.common;

import com.google.gwt.i18n.client.DateTimeFormat;

public interface IPresenter {
	public static final DateTimeFormat DTF = DateTimeFormat.getFormat("dd/MM/yy HH:mm");
	public static final String TARGET_API_FRAME = "apiFrame";
}
