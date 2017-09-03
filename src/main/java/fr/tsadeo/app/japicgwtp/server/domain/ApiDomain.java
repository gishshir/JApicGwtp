/*
 * Nom d'une API (ex Spring, Java, Hibernate, etc...)
 */
package fr.tsadeo.app.japicgwtp.server.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author sylvie
 */
@Entity
@Table (name = "t_domain")
public class ApiDomain extends AbstractVersionIdAndName {

	private static final long serialVersionUID = 1L;
    
    private final static Comparator<Site> SITE_COMPARATOR = (s1, s2) -> s1.getApiVersion().compareTo(s2.getApiVersion());
    
    
    @Basic (optional = true)
    private String description;
    
    // bi-directional lazy
    @OneToMany (mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY )
    private  Set<Site> sites;
    
    //------------------------ constructor
    public ApiDomain() {super(null);}
    public ApiDomain(String name) {
        super(name);
    }
 
    //-------------------------- accessor
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public void setName(String name) {
        super.setName(name);
                
    }
    //-------------------------public methods
    public List<Site> getListSites() {
        if (this.sites == null) {
            return Collections.emptyList();
        }

        List<Site> list = this.sites.stream()
                .sorted(SITE_COMPARATOR)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(list);
    }

    public void addSite(Site site) {

        if (Objects.nonNull(site)) {
            this.verifySites();
            this.sites.add(site);
            site.setDomain(this);
        }
    }

    public void removeSite(Site site) {
        if (Objects.nonNull(site)) {
            this.verifySites();
            this.sites.remove(site);
            site.setDomain(null);
        }
    }

    //------------------------ private methods
    private void verifySites() {
        if (this.sites == null) {
            this.sites = new HashSet<>();
        }
    }
    //------------------------ overriding Object
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.getName());
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
        final ApiDomain other = (ApiDomain) obj;
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        return true;
    }
    
    
}
