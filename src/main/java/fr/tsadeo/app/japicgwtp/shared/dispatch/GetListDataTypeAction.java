package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;

public class GetListDataTypeAction extends UnsecuredActionImpl<GetListDataTypeResult> {
	
	private VoSearchCriteria voCriteria;
	
	public GetListDataTypeAction() {}
	public GetListDataTypeAction(VoSearchCriteria voCriteria) {
		this.voCriteria = voCriteria;
	}
	
	public VoSearchCriteria getVoSearchCriteria() {
		return this.voCriteria;
	}
	

}
