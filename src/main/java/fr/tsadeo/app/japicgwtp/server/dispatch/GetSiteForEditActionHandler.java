package fr.tsadeo.app.japicgwtp.server.dispatch;

import fr.tsadeo.app.japicgwtp.shared.dispatch.GetSiteForEditAction;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForEdit;

public class GetSiteForEditActionHandler extends GetItemForEditActionHandler<GetSiteForEditAction, VoSiteForEdit> {	
	//-------------------------------------- constructor
	public GetSiteForEditActionHandler() {
		super(GetSiteForEditAction.class);
	}



}
