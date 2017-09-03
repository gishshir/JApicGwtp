package fr.tsadeo.app.japicgwtp.shared.dispatch;

import java.io.Serializable;

import com.gwtplatform.dispatch.rpc.shared.Result;

import fr.tsadeo.app.japicgwtp.shared.vo.VoDataTypeForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;

public class GetListDataTypeResult implements Result, Serializable {

	private static final long serialVersionUID = 1L;
	
	private VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> voSearchResultDatas;
	
	public VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> get() {
		return this.voSearchResultDatas;
	}
	
	public GetListDataTypeResult() {}

	public GetListDataTypeResult(VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> voSearchResultDatas) {
		this.voSearchResultDatas = voSearchResultDatas;
	}
}
