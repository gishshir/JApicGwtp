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
public class VoSiteForEdit extends VoItemForEdit {
	
	private static final long serialVersionUID = 1L;

    private long domainId = IVoId.ID_UNDEFINED;
    
    private String version;
    private String url;
    private String scanUrl;
    private long lastScan; // readonly
    private ScanStatus scanStatus; // readonly
    private UrlState urlState; // readonly
    private boolean enabled = true;
    

    //---------------------- constructor
    public VoSiteForEdit() {
       this(IVoId.ID_UNDEFINED, new VoIdName());
    }
    public VoSiteForEdit(long domainId, VoIdName voIdName) {
        super(TypeItem.site, voIdName);
        this.domainId = domainId;
    }
    //---------------------------------- public methods
    public VoSiteProtection getSiteProtection() {
    	return (VoSiteProtection) super.getProtection();
    }
    
//------------------------- accessors
    

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getDomainId() {
        return domainId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }

    
    public String getScanUrl() {
		return scanUrl;
	}
	public void setScanUrl(String scanUrl) {
		this.scanUrl = scanUrl;
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

    public UrlState getUrlState() {
        return urlState;
    }

    public void setUrlState(UrlState urlState) {
        this.urlState = urlState;
    }

    
}
