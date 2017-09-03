package fr.tsadeo.app.japicgwtp.shared.vo;

import fr.tsadeo.app.japicgwtp.shared.domain.JavaType;

public class VoDataTypeForGrid extends VoIdName {

	
	private static final long serialVersionUID = 1L;
    
    private String siteName;
    private String packageName;
    
    private JavaType type;
    private String link;
    
    private boolean iframeAllowed = true;
    
    private VoHighlighText voHighlightedName;

    //-------------------------- constructor
    public VoDataTypeForGrid() {}
    public VoDataTypeForGrid(VoIdName voIdName) {
        super(voIdName);
    }
    //------------------------------ accessor
    public boolean isIframeAllowed() {
    	return this.iframeAllowed;
    }
    public void setIFrameAllowed(boolean iframeAllowed) {
    	this.iframeAllowed = iframeAllowed;
    }
    
	public String getSiteName() {
		return siteName;
	}
	public VoHighlighText getVoHighlightedName() {
		return voHighlightedName;
	}
	public void setVoHighlightedName(VoHighlighText voHighlightedName) {
		this.voHighlightedName = voHighlightedName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public JavaType getType() {
		return type;
	}
	public void setType(JavaType type) {
		this.type = type;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
    
	@Override
    public String toString() {
    	return this.getName() + " - " + this.type + " - pack: " + this.packageName + " - site: " + this.siteName;
    }
}
