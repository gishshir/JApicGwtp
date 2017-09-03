package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.SimpleResult;

public class ScanSiteResult extends SimpleResult<Boolean> {

	private static final long serialVersionUID = 1L;

	public ScanSiteResult() {
		super(false);
	}

	public ScanSiteResult(Boolean success) {
		super(success);
	}
}
