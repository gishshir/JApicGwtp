/*
 * web Site for an java API 
 */
package fr.tsadeo.app.japicgwtp.server.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.NaturalId;
import org.springframework.core.io.Resource;

import fr.tsadeo.app.japicgwtp.server.domain.converter.ResourceConverter;
import fr.tsadeo.app.japicgwtp.server.domain.converter.ScanStatusConverter;
import fr.tsadeo.app.japicgwtp.server.domain.converter.UrlStateConverter;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;

/**
 *
 * @author sylvie
 */
@Entity
@Table(name = "t_site")
public class Site extends AbstractModelVersionItem implements IIdAndName {
	
	private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog(Site.class);

    private static final Comparator<ApiPackage> PACK_COMPARATOR = (p1, p2)
            -> p1.getLongName().compareTo(p2.getLongName());

   
    @Basic(optional = false)
    private String sname;

    // url de la page principale du site de l'API
    @NaturalId(mutable = true)
    @Convert(converter = ResourceConverter.class)
    private Resource mainPage;
    
    // url complémentaire pour accès à la page des classes
    // dans la plus part des cas null,
    // mais si existe doit etre utilisee pour le scan
    @Convert(converter = ResourceConverter.class)
    private Resource scanPage;

//    //nom de la page donnant la liste de toutes les classes de l'API
//    @Basic(optional = false)
//    private String indexPageName;

    @Basic(optional = true)
    private Instant lastScan;

    @Basic(optional = false)
    private String apiVersion;

    @Basic
    private boolean senabled = true;

    @Convert(converter = UrlStateConverter.class)
    private UrlState urlState = UrlState.NoTested;

    @Basic
    @Convert(converter = ScanStatusConverter.class)
    private ScanStatus scanStatus = ScanStatus.New;

    // bi-directional
    @ManyToOne
    @JoinColumn(name = "domain_id", foreignKey = @ForeignKey(name = "DOMAIN_ID_FK"))
    private ApiDomain domain;

    // unidirectional
    @OneToOne
    @JoinColumn(name = "main_pack_id", foreignKey = @ForeignKey(name = "MAINPACK_ID_FK"))
    private ApiPackage mainPackage;

    //bi-directional lazy fetching
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ApiPackage> apiPackages;

    //--------------------- constructor
    public Site() {
        this(null);
    }

    public Site(Resource mainPage) {
        this.mainPage = mainPage;
    }
    public Site(Resource mainPage, String name, String version) {
    	this(mainPage);
    	this.sname = name;
    	this.apiVersion = version;
    }
    //------------------------------ implementing IIdAndName

    @Override
    public VoIdName toVoIdName() {
        return new VoIdName(this.getId(), this.sname);
    }

    //---------------------- accessors
    public void setMainPage(Resource mainPage) {
        this.mainPage = mainPage;
    }

    public void setScanPage(Resource scanPage) {
    	this.scanPage = scanPage;
    }
    public UrlState getUrlState() {
        return urlState;
    }

    public void setUrlState(UrlState urlState) {
        this.urlState = urlState;
    }

    public ScanStatus getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(ScanStatus scanStatus) {
        this.scanStatus = scanStatus;
    }

    public boolean isEnabled() {
        return senabled;
    }

    public void setEnabled(boolean enabled) {
        this.senabled = enabled;
    }

    public ApiDomain getDomain() {
        return domain;
    }

    // ne doit pas etre appellé directement
    void setDomain(ApiDomain domain) {
        this.domain = domain;
    }

    @Override
    public String getName() {
        return this.sname;
    }
    public void setName(String name) {
        this.sname = name;
    }

    public Instant getLastScan() {
        return lastScan;
    }

    public void setLastScan(Instant lastScan) {
        this.lastScan = lastScan;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Resource getMainPage() {
        return mainPage;
    }

     public Resource getScanPage() {
    	 return this.scanPage;
     }
    public ApiPackage getMainPackage() {
        return mainPackage;
    }

//------------------------------ public methods
    public List<ApiPackage> getListApiPackages() {
        if (this.apiPackages == null) {
            return Collections.emptyList();
        }

        List<ApiPackage> list = this.apiPackages.stream()
                .sorted(PACK_COMPARATOR)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(list);
    }

    public void addApiPackage(ApiPackage apiPackage) {
        this.addApiPackage(apiPackage, false);
    }

    public void addApiPackage(ApiPackage apiPackage, boolean main) {

        if (Objects.nonNull(apiPackage)) {
            this.verifyApiPackages();
            apiPackage.setSite(this);
            this.apiPackages.add(apiPackage);

            // main package
            if (main) {
                this.mainPackage = apiPackage;
            }
        }
    }

    public void removeApiPackage(ApiPackage apiPackage) {
    	
    	if(this.apiPackages == null) {
    		return;
    	}
    	
    	if (this.mainPackage != null && this.mainPackage.getId() == apiPackage.getId()){
    		this.mainPackage = null;
    	}
        if (Objects.nonNull(apiPackage)) {
            this.apiPackages.remove(apiPackage);
            apiPackage.setSite(null);
        }
    }

    //------------------------------ private methods
    private void verifyApiPackages() {
        if (this.apiPackages == null) {
            this.apiPackages = new HashSet<>();
        }
    }

    //------------------------------ overriding Object
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.mainPage);
        return hash;
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
        final Site other = (Site) obj;
        if (!Objects.equals(this.mainPage, other.mainPage)) {
            return false;
        }
        return true;
    }

}
