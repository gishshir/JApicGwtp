/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.List;

import fr.tsadeo.app.japicgwtp.server.domain.User;
import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;
/**
 *
 * @author sylvie
 */
public interface IUserDao extends IITemDao<User> {

     public static final String COL_LOGIN = "login";
    public static final String COL_PROFILE = "profile";
   
    
    public static final String LIST_ALL = SELECT + "u from User u";
    public static final String LIST_BY_PROFILE = LIST_ALL + WHERE 
            + COL_PROFILE + " = :" + COL_PROFILE;
    
   public User getByNaturalId(String login, boolean withDatas);

   public List<User> listByProfile(UserProfile profile);
  
}
