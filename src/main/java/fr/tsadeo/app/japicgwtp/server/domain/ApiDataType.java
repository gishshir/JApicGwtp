/*
 * Represente un Type (class, enum, interface) dans un package 
*/
package fr.tsadeo.app.japicgwtp.server.domain;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NaturalId;

import fr.tsadeo.app.japicgwtp.server.domain.converter.JavaTypeConverter;
import fr.tsadeo.app.japicgwtp.shared.domain.JavaType;

@NamedNativeQueries({ 
	
	@NamedNativeQuery(

		name = "callsearchbyname",
		query = "CALL searchbyname (:dname)", 
		resultClass = ApiDataType.class

		), 
	
	@NamedNativeQuery(

		name = "callsearchbynameandsite", 
		query = "CALL searchbynameandsite (:dname, :sname)", 
		resultClass = ApiDataType.class

		), 
	
	@NamedNativeQuery(

		name = "callsearchbynameandsiteandversion", 
		query = "CALL searchbynameandsiteandversion (:dname, :sname, :apiVersion)", 
		resultClass = ApiDataType.class

		) })

/**
 *
 * @author sylvie
 */
@Entity
@Table(name = "t_datatype")
public class ApiDataType extends AbstractModelItem {

	private static final long serialVersionUID = 1L;

	@NaturalId
	// short name of the class
	private final String dname;

	@Basic(optional = false)
	@Convert(converter = JavaTypeConverter.class)
	private final JavaType javaType;

	@NaturalId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id", foreignKey = @ForeignKey(name = "PACK_ID_FK"))
	private ApiPackage apiPackage;

	@Basic
	private String since;

	// calculated
	@Transient
	private String fullName;

	// ----------------------------- constructor
	public ApiDataType() {
		this(null, null);
	}

	public ApiDataType(JavaType type, String name) {
		this.javaType = type;
		this.dname = name;
	}

	// ------------------------------ accessors

	public JavaType getType() {
		return javaType;
	}

	public String getName() {
		return dname;
	}

	public ApiPackage getApiPackage() {
		return apiPackage;
	}

	// ne pas utiliser sauf depuis ApiPackage!!
	void setApiPackage(ApiPackage apiPackage) {
		this.apiPackage = apiPackage;
	}

	public String getSince() {
		return since;
	}

	public void setSince(String since) {
		this.since = since;
	}

	// nom de la class avec le nom du package
	public String getFullName() {

		if (Objects.isNull(this.fullName)) {

			this.fullName = this.apiPackage.getLongName() + SEPARATOR + this.dname;
		}

		return this.fullName;
	}

	// ---------------------------------------- overriding Object
	@Override
	public String toString() {
		return this.dname + " - type: " + this.javaType;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + Objects.hashCode(this.dname);
		if (this.apiPackage != null) {
			hash = 89 * hash + Objects.hashCode(this.apiPackage.getId());
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
		final ApiDataType other = (ApiDataType) obj;
		if (!Objects.equals(this.dname, other.dname)) {
			return false;
		}
		if (this.apiPackage == null && other.apiPackage == null) {
			return true;
		}
		if (this.apiPackage == null || other.apiPackage == null) {
			return false;
		}
		if (!Objects.equals(this.apiPackage.getId(), other.apiPackage.getId())) {
			return false;
		}
		return true;
	}

}
