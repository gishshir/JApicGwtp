/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.service;

import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUserForEdit;

/**
 *
 * @author sylvie
 */
public interface IUserService extends IItemService<VoUserForEdit> {
    
	
    
     //public User createUser(String login, String pwd, UserProfile profile) throws JApicException;

     /**
      * authentification de l'utilisateur
      * @param login
      * @param pwd
      * @return
      * @throws JApicException si erreur d'authentification
      */
    public VoUser authenticateUser(String login, String pwd)throws JApicException;
    
        // =========================================== INNER CLASS
    public static class ProfilCredential {

        private final String login;
        private final String pwd;

        public ProfilCredential(String login, String pwd) {
            this.login = login;
            this.pwd = pwd;
        }

        public boolean validate(String loginToControl, String pwdToControl) {
            return (this.login.equals(loginToControl) && this.pwd.equals(pwdToControl));
        }
    }

    
}
