package fr.tsadeo.app.japicgwtp.shared.dispatch;

import java.io.Serializable;

import com.gwtplatform.dispatch.rpc.shared.Result;

import fr.tsadeo.app.japicgwtp.shared.vo.VoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdHighlighText;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;

public class GetListDataTypeForGridUpdateResult implements Result, Serializable {

	private static final long serialVersionUID = 1L;
	
	private VoSearchResultDatas<VoIdHighlighText, VoItemProtection> voSearchResultDatas;
	
	public VoSearchResultDatas<VoIdHighlighText, VoItemProtection> get() {
		return this.voSearchResultDatas;
	}
	
	public GetListDataTypeForGridUpdateResult() {}

	public GetListDataTypeForGridUpdateResult(VoSearchResultDatas<VoIdHighlighText, VoItemProtection> voSearchResultDatas) {
		this.voSearchResultDatas = voSearchResultDatas;
	}
}
