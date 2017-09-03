/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.service;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.AppTransactional;
import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.TransactionMode;
import fr.tsadeo.app.japicgwtp.server.dao.IUserDao;
import fr.tsadeo.app.japicgwtp.server.domain.User;
import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUserForEdit;

/**
 *
 * @author sylvie
 */
@Service
public class UserService extends AbstractService implements IUserService {

    private final static Log LOG = LogFactory.getLog(UserService.class);

    @Autowired
    private IUserDao userDao;


    //------------------------------- implementing ILoggable
    @Override
    public Log getLog() {
        return LOG;
    }
    
  //------------------------------- implementing IItemService
    


	@Override
	public VoUserForEdit getItemForEdit(long itemId, boolean create, boolean atLeastManager) throws JApicException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean deleteItem(long itemId) throws JApicException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public VoDatasValidation validateItem(VoUserForEdit itemToUpdate) throws JApicException {
		// TODO Auto-generated method stub
		return null;
	}


	 @AppTransactional(TransactionMode.auto)
	@Override
	public VoUserForEdit createOrUpdateItem(VoUserForEdit userToUpdate, boolean createItem) throws JApicException {

	        User user;
	        if (createItem) {
	            user = new User(userToUpdate.getLogin());
	            
	        } else {
	            user = this.userDao.getById(userToUpdate.getId(), true);
	        }
	        this.updateUserWithVo(user, userToUpdate);
	        if (createItem) {
	        	this.userDao.attachAndSave(user);
	        } else {
	        this.userDao.attachAndUpdate(user);
	        }

	        VoUserForEdit voUser = this.buildVoUserForEdit(user);
	        voUser.setProtection(userToUpdate.getProtection());
	        return voUser;
	}




	/**
	 * Vérifie l'existence d'un admin par defaut. Si non le cree automatiquement
	 * @throws JApicException
	 */
    @AppTransactional(TransactionMode.auto)
    @Override
    public void verifyDefaultItems()  throws JApicException {

    	final String login = "admin";
    	// si aucun utilisateur admin on en cree un par defaut
    	User userAdmin = this.userDao.getByNaturalId(login, false);
    	if (Objects.isNull(userAdmin)) {
    		
    		this._createUser(login, login, UserProfile.Admin);
    		LOG.info("Create default admin user...");
    	}
    	
    }

    //------------------------------- implementing IUserService

	
    @Override
    @AppTransactional(TransactionMode.readonly)
    public VoUser authenticateUser(String login, String pwd) throws JApicException{
        
        User user = this.userDao.getByNaturalId(login, true);
        if (user != null && Objects.equals(user.getPassword(), pwd)) {
            LOG.debug("Authentication for " + login + " in success!");
            
            VoUser voUser = new VoUser(login, user.getProfile());
            return voUser;
        }
        
        throw new JApicException("login or password are wrong!");
    }
     //------------------------------- private methods
    private User _createUser(String login, String pwd, UserProfile profile) {

        User user = new User(login);
        user.setPassword(pwd);
        user.setProfile(profile);

        userDao.attachAndSave(user);

        return user;

    }

    private VoUserForEdit buildVoUserForEdit(User user) {

    	VoUserForEdit vo = new VoUserForEdit(user.toVoIdName());
    	
    	vo.setLogin(user.getLogin());
    	vo.setPwd(user.getPassword());
    	vo.setProfile(user.getProfile());
    	return vo;
	}


	private void updateUserWithVo(User user, VoUserForEdit userToUpdate) {

		user.setPassword(userToUpdate.getPwd());
		user.setProfile(userToUpdate.getProfile());
	}

}
