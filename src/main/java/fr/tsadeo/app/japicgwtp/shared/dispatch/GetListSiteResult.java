/**
 * 
 */
package fr.tsadeo.app.japicgwtp.shared.dispatch;

import java.io.Serializable;

import com.gwtplatform.dispatch.rpc.shared.Result;

import fr.tsadeo.app.japicgwtp.shared.vo.VoListItems;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteProtection;

/**
 * @author sylvie
 *
 */
public class GetListSiteResult implements Result, Serializable {

	private static final long serialVersionUID = 1L;
	
	private VoSearchResultDatas<VoSiteForGrid, VoSiteProtection>  searchResultDatas;
	
	public GetListSiteResult() {}
	public GetListSiteResult(VoSearchResultDatas<VoSiteForGrid, VoSiteProtection> searchResultDatas) {
		this.searchResultDatas = searchResultDatas;
	}
	
	public VoSearchResultDatas<VoSiteForGrid, VoSiteProtection> getSearchResultDatas() {
		return this.searchResultDatas;
	}
}
