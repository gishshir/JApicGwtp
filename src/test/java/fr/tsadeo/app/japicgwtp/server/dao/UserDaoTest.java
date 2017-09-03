/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;
import fr.tsadeo.app.japicgwtp.server.domain.User;
import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;

/**
 *
 * @author sylvie
 */
public class UserDaoTest extends AbstractDaoTestContext {
    
    private static int USER_COUNT = 0;
    private static Map<UserProfile, Integer> MAP_PROFILE2COUNT = new HashMap<>();

    @Autowired
    private IUserDao userDao;

    @Test
    public void testCreateUser() {

       this.createUser("toto", UserProfile.Admin);
    }
    @Test
    public void testModifyUser() {
    	
    	User user = this.createUser("nono", UserProfile.Manager);
    	
    	this.sessionManager.beginTransaction();
    	user = this.userDao.getById(user.getId(), true);
    	user.setPassword("pwd modified!");
    	user.setProfile(UserProfile.Authenticated);
    	
    	this.sessionManager.commitTransaction();
    }
    
  
    @Test
    public void testListAll() {
        
         this.sessionManager.beginTransaction();
        List<User> list = userDao.listAll();
        assertNotNull("list users cannot be null!", list);
        assertEquals ("wrong size!", USER_COUNT, list.size());
        this.sessionManager.commitTransaction();
        
        this.createUser("titi", UserProfile.Manager);
        this.createUser("tata", UserProfile.Authenticated);
        this.createUser("tutu", UserProfile.Admin);
        
        this.sessionManager.beginTransaction();
        list = userDao.listAll();
        assertNotNull("list users cannot be null!", list);
        assertEquals ("wrong size!", USER_COUNT, list.size());
        this.sessionManager.commitTransaction();
    }
    
    @Test
    public void testListByProfile() {
        
        this.createUser("bibi", UserProfile.Manager);
        this.createUser("baba", UserProfile.Authenticated);
        this.createUser("bubu", UserProfile.Authenticated);
        
         this.sessionManager.beginTransaction();
        List<User> list = userDao.listByProfile(UserProfile.Authenticated);
        assertNotNull("list users cannot be null!", list);
        assertEquals ("wrong size!", this.getCount(UserProfile.Authenticated), list.size());
        
        list = userDao.listByProfile(UserProfile.Manager);
        assertNotNull("list users cannot be null!", list);
        assertEquals ("wrong size!", this.getCount(UserProfile.Manager), list.size());
        
        list = userDao.listByProfile(UserProfile.Admin);
        assertNotNull("list users cannot be null!", list);
        assertEquals ("wrong size!", this.getCount(UserProfile.Admin), list.size());
        
        this.sessionManager.commitTransaction();
        
    }
    
    @Test
    public void testGetById() {

        this.sessionManager.beginTransaction();

        User user1 = this.buildUser("titi", "pwdtiti", UserProfile.Manager);
        userDao.attachAndSave(user1);

        User user2 = userDao.getById(user1.getId(), true);
        Long id = user2.getId();
        assertNotNull("user2 cannot be null!", user2);
        assertEquals("user1 must be user2", user1, user2);
        this.sessionManager.commitTransaction();

        Session session2 = this.sessionManager.beginTransaction();
        //get a proxy
        User userProxy = userDao.getById(id, false);
        assertNotNull("userProxy cannot be null!", userProxy);
        assertTrue("session must contains userProxy!", session2.contains(userProxy));

        User userBdd = userDao.getById(id, true);
        assertNotNull("userBdd cannot be null!", userBdd);
        assertTrue("session must contains userBdd!", session2.contains(userBdd));

        this.sessionManager.commitTransaction();

    }

    @Test
    public void testGetByNaturalId() {

        this.sessionManager.beginTransaction();
        String login = "tutu";
        User user1 = this.buildUser(login, "pwdtutu", UserProfile.Admin);
        userDao.attachAndSave(user1);

        User user2 = userDao.getByNaturalId(login, true);
        assertNotNull("user2 cannot be null!", user2);
        assertEquals("user1 must be user2", user1, user2);
        this.sessionManager.commitTransaction();

        Session session2 = this.sessionManager.beginTransaction();
        //get a proxy
        User userProxy = userDao.getByNaturalId(login, false);
        assertNotNull("user3 cannot be null!", userProxy);
        assertTrue("session must contains user!", session2.contains(userProxy));

        User userBdd = userDao.getByNaturalId(login, true);
        assertNotNull("user cannot be null!", userBdd);
        assertTrue("session must contains user!", session2.contains(userBdd));

        this.sessionManager.commitTransaction();

    }
    



	@Test
    public void testReattach() {

        LOG.info("testReattach() .......");

        LOG.info("create new User --> SQL INSERT");
        User user1 = this.createUser("wuwu", UserProfile.Manager);

        // user1 is detached
        LOG.info("reattach unmodified User (lock) --> No SQL");
        Session session1 = this.sessionManager.beginTransaction();
        assertFalse("session1 cannot contains user1!", session1.contains(user1));
        //lock (not in session & unmodified)
        userDao.attachUnmodifiedItem(user1);
        
        user1.setPassword("pwd modified with lock NONE");
        // user1 attached
        assertTrue("session1 must contains user1!", session1.contains(user1));
        this.sessionManager.commitTransaction();
        
        
        LOG.info("Reattach User modify and update --> SQL UPDATE");
        // on le reattache non modifie
        Session session2 = this.sessionManager.beginTransaction();
        assertFalse("session2 cannot contains user1!", session2.contains(user1));
        userDao.attachAndUpdate(user1);
        assertTrue("session2 must contains user1!", session2.contains(user1));
        // on le modifie dans la session
        user1.setPassword("new pwd for wuwu");
        // update auto à la fermeture de session
        this.sessionManager.commitTransaction();
        


        // LE MERGE NE FONCTIONNE PAS!!!!!!!!!!
//        
//        LOG.info("Merge User --> SQL UPDATE");
//        // on recupere l'utilisateur enregistre
//        Session session3 = this.sessionManager.beginTransaction();
//        // user2 is not in session
//        assertFalse("session3 cannot contains user2!", session3.contains(user2));
//        
//        // user3 est dans la session
//        User user3 = userDao.getById(user2.getId(), true);
//        assertTrue("session3 must contains user3!", session3.contains(user3));
//        
//        // on modifie user2 detached
//        user2.setPassword("modif2 for wuwu");
//        
//        // merge user2 and update
//        userDao.mergeAndUpdateItem(user2);
//        assertFalse("session3 cannot contains user2!", session3.contains(user2));
//        
//        this.sessionManager.commitTransaction();

        
        
        
        
  

    }

    // WARN delete detached user doesn't work with mysql!!!!!!!!...
    @Test
    public void testDeleteUser() {

        LOG.info("testDeleteUser()");
        
        LOG.info("create user...");
        User user =  this.createUser("bibi", UserProfile.Admin);
        
        
        LOG.warn("delete detached user...");
        // user detached >> then delete (without reattach it)
        this.sessionManager.beginTransaction();
        userDao.delete(user);
        this.sessionManager.commitTransaction();
        
        this.manageCountUser(user.getProfile(), false);
        
        LOG.info("verify user null...");
        this.sessionManager.beginTransaction();
        //get a proxy
        User userProxy = userDao.getById(user.getId(), false);
        assertNull("userProxy must be null!", userProxy);
        
        this.sessionManager.commitTransaction();
    }
    //-------------------------------------------------private methods

    private User getProxyById(Long id) {
    	
    	this.sessionManager.beginTransaction();
    	User user = userDao.getById(id, false);
    	this.sessionManager.commitTransaction();
    	
    	return user;
    }
     private User createUser(String login, UserProfile profile) {

        Session session = this.sessionManager.beginTransaction();
        User user = this.buildUser(login + USER_COUNT, "pwd" + login, profile);
        assertFalse("session cannot contains user!", session.contains(user));
        userDao.attachAndSave(user);
        assertTrue("session must contains user!", session.contains(user));

        //Hibernate also flushes and closes the current
        //Session and its persistence context
        //if you commit or roll back the transaction.
        this.sessionManager.commitTransaction();

        return this.getProxyById(user.getId());
    }
    private User buildUser(String login, String pwd, UserProfile profile) {

        this.manageCountUser(profile, true);
        User user = new User(login);
        user.setPassword(pwd);
        user.setProfile(profile);

        return user;
    }
    
    private int getCount(UserProfile profile) {
        Integer countForProfile = MAP_PROFILE2COUNT.get(profile);
        return countForProfile == null ? 0:countForProfile;
    }
    
    private void manageCountUser(UserProfile profile, boolean add) {
        
        int countForProfile = this.getCount(profile);
        if (add) {
            USER_COUNT++;
            countForProfile++;
        } else {
            USER_COUNT--;
            countForProfile--;
        }
        MAP_PROFILE2COUNT.put(profile, countForProfile);
    }
    

}
