package fr.tsadeo.app.japicgwtp.shared.vo;

public class VoSiteProtection extends VoItemProtection {

	
	private static final long serialVersionUID = 1L;
	private boolean canScan = true;

	public boolean canScan() {
		return canScan;
	}

	public void setCanScan(boolean canScan) {
		this.canScan = canScan;
	}
	
}
