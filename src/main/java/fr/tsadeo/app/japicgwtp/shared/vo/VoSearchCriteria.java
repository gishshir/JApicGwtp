package fr.tsadeo.app.japicgwtp.shared.vo;

import java.util.Objects;

public class VoSearchCriteria implements IVo {

	private static final long serialVersionUID = 1L;
	
	private long domainId = IVoId.ID_UNDEFINED;
	private long siteId = IVoId.ID_UNDEFINED;
	
	private String search;
	private int maxCount = Integer.MAX_VALUE;
	
	//--------------------------- constructor
	public VoSearchCriteria() {}
	public VoSearchCriteria(long domainId, long siteId, String search) {
		this.domainId = domainId;
		this.siteId = siteId;
		this.search= search;
	}
	
	//---------------------------- accessors
	public long getDomainId() {
		return domainId;
	}
	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}
	public long getSiteId() {
		return siteId;
	}
	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String dataTypeName) {
		this.search = dataTypeName;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}


    //------------------------------ overriding Object
	
	
    @Override
    public int hashCode() {
        int hash = 9;
        hash = 62 * hash + Objects.hashCode(this.domainId);
        hash = 62 * hash + Objects.hashCode(this.siteId);
        hash = 62 * hash + Objects.hashCode(this.search);
        hash = 62 * hash + Objects.hashCode(this.maxCount);
        return hash;
    }

	public VoSearchCriteria clone()  {
		
		return new VoSearchCriteria(this.domainId, this.siteId, this.search);
	}
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VoSearchCriteria other = (VoSearchCriteria) obj;
        if (!Objects.equals(this.domainId, other.domainId)) {
            return false;
        }
        if (!Objects.equals(this.siteId, other.siteId)) {
            return false;
        }
        if (!Objects.equals(this.maxCount, other.maxCount)) {
            return false;
        }
        if (!Objects.equals(this.search, other.search)) {
            return false;
        }
        return true;
    }
	
}
