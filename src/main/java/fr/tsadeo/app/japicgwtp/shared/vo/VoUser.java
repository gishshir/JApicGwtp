/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.shared.vo;

import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;

/**
 *
 * @author sylvie
 */
public class VoUser extends AbstractVoId implements IVoId {
	
	private static final long serialVersionUID = 1L;
    
    private String login;
    private UserProfile profile;
    
    //----------------------- constructor
    public VoUser() {}
    public VoUser(String login, UserProfile profile) {
        this.login = login;
        this.profile = profile;
    }
    //-------------------------- accessors
    public String getLogin() {
        return login;
    }

    public UserProfile getProfile() {
        return profile;
    }
    
}
