/*
 * Value Objet for site in grid
 */
package fr.tsadeo.app.japicgwtp.shared.vo;

import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;

/**
 *
 * @author sylvie
 */
public class VoSiteForGrid extends VoIdName {

	private static final long serialVersionUID = 1L;
	
    private String version;
    private String url;
    private UrlState urlState;
    private long lastScan;  
    private ScanStatus scanStatus;
    private boolean enabled = true;
    
    private VoHighlighText voHighlightedName;
    
    
    public VoSiteForGrid() {
        super();
    }
    public VoSiteForGrid(VoIdName voIdName) {
        super(voIdName);
    }

//------------------------- accessors
    
    public VoSiteProtection getSiteProtection() {
    	return (VoSiteProtection) super.getProtection();
    }
    
    public VoHighlighText getVoHighlightedName() {
		return voHighlightedName;
	}
	public void setVoHighlightedName(VoHighlighText voHighlightedName) {
		this.voHighlightedName = voHighlightedName;
	}
	public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public UrlState getUrlState() {
        return urlState;
    }

    public void setUrlState(UrlState urlState) {
        this.urlState = urlState;
    }

  
    public boolean isIframeAllowed() {
    	return this.urlState == UrlState.Alive;
    }
    public long getLastScan() {
        return lastScan;
    }

    public void setLastScan(long lastScan) {
        this.lastScan = lastScan;
    }

    public ScanStatus getScanStatus() {
        return scanStatus;
    }

    
    public void setScanStatus(ScanStatus scanStatus) {    
        this.scanStatus = scanStatus;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
}
