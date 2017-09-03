/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;
import fr.tsadeo.app.japicgwtp.server.dao.IUserDao;
import fr.tsadeo.app.japicgwtp.server.domain.User;
import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUserForEdit;

/**
 *
 * @author sylvie
 */
public class UserServiceTest extends AbstractDaoTestContext {
    
    private final static Log LOG = LogFactory.getLog(UserServiceTest.class);
    
    @Autowired
    private IUserService service;
    @Autowired
    private IUserDao userDao;
    
    @Test
    public void testAdminDefault() throws Exception {
    	
    	this.service.verifyDefaultItems();
    	
    	this.sessionManager.beginTransaction();
    	List<User> listAdmin = this.userDao.listByProfile(UserProfile.Admin);
    	assertNotNull ("listAdmin cannot be null!", listAdmin);
    	this.sessionManager.commitTransaction();
    	
    	boolean result =
    	listAdmin.stream()
    	    .filter(user -> user.getLogin().equals("admin"))
    	    .findAny().isPresent();
    	
    	assertTrue("There must be admin default!", result);
    	
    }
    
    
    @Test
    public void testCreateUser () throws Exception {
        
        VoUserForEdit voUser = new VoUserForEdit();
        voUser.setLogin("toto");
        voUser.setPwd("pwdToto");
        voUser.setProfile(UserProfile.Manager);
        
        voUser = service.createOrUpdateItem(voUser, true);
        assertNotNull("user cannot be null!", voUser);
        assertFalse("User id must be defined!", voUser.isUndefined());
        
    }
   
    // L'aspect ne fonctionne plus !!!
    @Test (expected = JApicException.class)
    public void testCreateUserAndRollback () throws Exception {
    	VoUserForEdit voUser1 = new VoUserForEdit();
        voUser1.setLogin("titi");
        voUser1.setPwd("pwdTiti");
        voUser1.setProfile(UserProfile.Manager);
        
        voUser1 = service.createOrUpdateItem(voUser1, true);
        assertNotNull("user cannot be null!", voUser1);
        
        // login must be unique --> exception & rollback
        try {
        	VoUserForEdit voUser2 = new VoUserForEdit();
            voUser2.setLogin("titi");
            voUser2.setPwd("xxxx");
            voUser2.setProfile(UserProfile.Admin);
            
            
            voUser2 = service.createOrUpdateItem(voUser2, true);
        }
        catch(JApicException ex) {
            LOG.error("EX: " + ex.getErrorMessage());
            throw ex;
        }
        
    }
    
}
