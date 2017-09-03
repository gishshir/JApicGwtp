package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

public class ScanSiteAction extends UnsecuredActionImpl<ScanSiteResult>  {


	private long siteId;

	public long getSiteId() {
		return siteId;
	}
	
	//---------------------------------- constructor

    public ScanSiteAction() {
    }
	public ScanSiteAction(long siteId) {
		this.siteId = siteId;
	}
}
