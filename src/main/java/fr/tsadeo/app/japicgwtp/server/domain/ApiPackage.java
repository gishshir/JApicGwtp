/*
 * Represente un Package Java
 */
package fr.tsadeo.app.japicgwtp.server.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NaturalId;

/**
 *
 * @author sylvie
 */
@Entity
@Table (name = "t_package")
public class ApiPackage extends AbstractModelItem {
	
	private static final long serialVersionUID = 1L;

    private final static Comparator<ApiDataType> DATATYPE_COMPARATOR =
            (t1, t2) ->  t1.getName().compareTo(t2.getName());
            
    // profondeur du package
    // utilisé lors du scan pour trouver le main package
    @Transient        
    private int level;

    //FIXME l'unicite doit etre au niveau du site!!
    @NaturalId
    private final String longName;
    
    // unidirectional
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "parent_id", foreignKey = @ForeignKey(name = "PARENT_ID_FK"))
    private final ApiPackage parent;
    
    // bi-directional
    @NaturalId
    @ManyToOne
    @JoinColumn (name = "site_id", foreignKey = @ForeignKey(name = "SITE_ID_FK"))
    private  Site site;
    
    @OneToMany (mappedBy = "apiPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ApiDataType> apiDataTypes;
        
    //----------------------------------- constructor
    public ApiPackage() {this(null, null);}
    public ApiPackage(ApiPackage parent, String longName) {
        this.longName = longName;
        this.parent = parent;
        
        // seul root a un parent null
        level = (this.parent == null)?-1:this.parent.level + 1;
    }

    //----------------------------- accessor
    public int getLevel() {
    	return this.level;
    }

    public List<ApiDataType> getListApiDataTypes() {
        if (this.apiDataTypes == null) {
            return Collections.emptyList();
        }
        
        List<ApiDataType> list = this.apiDataTypes.stream()
                .sorted(DATATYPE_COMPARATOR)
                .collect(Collectors.toList());
        
        return Collections.unmodifiableList(list);
    }
    public Site getSite() {
        return site;
    }

    // Attention ne pas y acceder directement (géré par la classe Site!)
    void setSite(Site site) {
        this.site = site;
    }
    
    
    public ApiPackage getParent() {
        return parent;
    }

    public String getLongName() {
        return longName;
    }

    //---------------------------------- public methods
    public void addApiDataType(ApiDataType apiDataType) {
        this.verifyApiDataTypes();

        if (Objects.nonNull(apiDataType)) {
            this.apiDataTypes.add(apiDataType);
            apiDataType.setApiPackage(this);
        }
    }

    public void removeApiDataType(ApiDataType apiDataType) {
        this.verifyApiDataTypes();

        if (Objects.nonNull(apiDataType)) {
            this.apiDataTypes.remove(apiDataType);
            apiDataType.setApiPackage(null);
        }
    }

    //------------------------------ private methods
    private void verifyApiDataTypes() {
        if (this.apiDataTypes == null) {
            this.apiDataTypes = new HashSet<>();
        }
    }
    // ---------------------------------overriding Object
    @Override
    public String toString() {
    	return this.longName + " parent[" + (this.parent != null?"YES":"NO") + "]" + ( this.parent != null?" - parentName: " + this.parent.getLongName():"");
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.longName);
        if (this.site != null) {
         hash = 83 * hash + Objects.hashCode(this.site.getId());
        }
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
        final ApiPackage other = (ApiPackage) obj;

        if (this.site != null && other.site != null) {
            if (!Objects.equals(this.site.getId(), other.site.getId())) {
                return false;
            }
        }
        if (!Objects.equals(this.longName, other.longName)) {
            return false;
        }

        return true;
    }


    
}
