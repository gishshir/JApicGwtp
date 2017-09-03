package fr.tsadeo.app.japicgwtp.shared.dispatch;

import fr.tsadeo.app.japicgwtp.shared.vo.TypeItem;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForEdit;

public class GetSiteForEditAction extends GetItemForEditAction<VoSiteForEdit> {
	
	
	public GetSiteForEditAction() {}
	
	public GetSiteForEditAction(long siteId) {
		super(siteId, TypeItem.site);
	}

}
