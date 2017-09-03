/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.domain;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import fr.tsadeo.app.japicgwtp.server.domain.converter.UserProfileConverter;
import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;

/**
 *
 * @author sylvie
 */
@Entity
@Table (name = "t_user")
public class User extends AbstractModelVersionItem implements IIdAndName  {
    
	private static final long serialVersionUID = 1L;
    
    @NaturalId
    private final String login;
    private String password;
    
    @Basic (optional = false)
    @Convert (converter = UserProfileConverter.class)
    private UserProfile profile;
    

    //------------------ constructor
    public User() {this(null);}
    public User(String login) {
        this.login = login;
    }
    //------------------ accessors

    public String getLogin() {
        return login;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
    
    //------------------------------ implementing IIdAndName

	@Override
	public String getName() {
		return null;
	}

    @Override
    public VoIdName toVoIdName() {
        return new VoIdName(this.getId(), null);
    }

    
    //=================================== overriding Object
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.login);
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
        final User other = (User) obj;
        return Objects.equals(this.login, other.login);
    }
    
    
}
