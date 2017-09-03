/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import fr.tsadeo.app.japicgwtp.server.domain.User;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;

/**
 *
 * @author sylvie
 */
@Repository
public class UserDao extends AbstractDao<User> implements IUserDao {

    private static final Log LOG = LogFactory.getLog(UserDao.class);
    
  
    //------------------------------------- implementing ILoggable
    @Override
    public Log getLog() {
        return LOG;
    }
    

    //---------------------------- implementing AbstractDao

    @Override
    protected Class<User> getClassItem() {
        return User.class;
    }
   @Override
    public String getHQLListAll() {
     return LIST_ALL;
    }
    //---------------------------- implementing IUserDao
    @Override
    public User getByNaturalId(String login, boolean withDatas) {
        return super.getByNaturalId(COL_LOGIN, login, withDatas);
    }

    @Override
    public List<User> listByProfile(UserProfile profile) {
        
        return super.listItems(LIST_BY_PROFILE, CollectUtils.buildMapWithOneItem(COL_PROFILE, profile));
    }
    
    

 

}
