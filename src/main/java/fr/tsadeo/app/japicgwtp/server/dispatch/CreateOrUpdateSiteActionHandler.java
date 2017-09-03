package fr.tsadeo.app.japicgwtp.server.dispatch;

import fr.tsadeo.app.japicgwtp.shared.dispatch.CreateOrUpdateSiteAction;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForEdit;

public class CreateOrUpdateSiteActionHandler extends CreateOrUpdateItemActionHandlers<CreateOrUpdateSiteAction, VoSiteForEdit> {

	public CreateOrUpdateSiteActionHandler() {
		super(CreateOrUpdateSiteAction.class);
	}

}
