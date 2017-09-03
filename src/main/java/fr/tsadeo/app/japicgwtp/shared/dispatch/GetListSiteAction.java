package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;

public class GetListSiteAction extends UnsecuredActionImpl<GetListSiteResult> {
	
	private VoSearchCriteria voCriteria;
	
	public GetListSiteAction() {}
	public GetListSiteAction(VoSearchCriteria voCriteria) {
		this.voCriteria = voCriteria;
	}
	
	public VoSearchCriteria getVoSearchCriteria() {
		return this.voCriteria;
	}
	

}
