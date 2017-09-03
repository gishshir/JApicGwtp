package fr.tsadeo.app.japicgwtp.client.application;

import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import fr.tsadeo.app.japicgwtp.client.event.NotificationEvent.Level;

class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {
	interface Binder extends UiBinder<Widget, ApplicationView> {
	}

	@UiField
	SimplePanel menu;

	@UiField
	SimplePanel body;

	@UiField
	Element loadingMessage;

	@Inject
	ApplicationView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		super.bindSlot(ApplicationPresenter.SLOT_MENU, menu);
		super.bindSlot(ApplicationPresenter.SLOT_CONTENT, body);
	}

	// ------------------------------------ implementing MyView

	@Override
	public void showLoading(boolean visible) {
		loadingMessage.getStyle().setVisibility(visible ? Visibility.VISIBLE : Visibility.HIDDEN);
	}

	@Override
	public void showNotification(String title, String errorMessage, Level level) {
		
		NotifyType type = NotifyType.INFO;
		IconType icon = IconType.SMILE_O;
		
		switch (level) {
		case success: type = NotifyType.SUCCESS;
			break;

		case info: type = NotifyType.INFO;
		break;

		case warn: type = NotifyType.WARNING;
			icon = IconType.WARNING;
		break;

		case error: type = NotifyType.DANGER;
				icon = IconType.AMBULANCE;
		break;

		}

		NotifySettings settings = NotifySettings.newSettings();
		settings.setType(type);
		settings.setAllowDismiss(false);
		Notify.notify(title, "<br/>" +errorMessage, icon, settings);
	}

}