package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;

public class GetListDataTypeForGridUpdateAction extends UnsecuredActionImpl<GetListDataTypeForGridUpdateResult> {
	
	private VoSearchCriteria voCriteria;
	
	public GetListDataTypeForGridUpdateAction() {}
	public GetListDataTypeForGridUpdateAction(VoSearchCriteria voCriteria) {
		this.voCriteria = voCriteria;
	}
	
	public VoSearchCriteria getVoSearchCriteria() {
		return this.voCriteria;
	}
	

}
