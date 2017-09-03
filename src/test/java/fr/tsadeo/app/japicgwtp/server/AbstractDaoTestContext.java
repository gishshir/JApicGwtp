/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.tsadeo.app.japicgwtp.server.aspect.LogAspect;
import fr.tsadeo.app.japicgwtp.server.config.MainAppConfigH2;
import fr.tsadeo.app.japicgwtp.server.dao.IApiDomainDao;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.server.manager.DaoSessionManager;

/**
 *
 * @author sylvie
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:/fr/tsadeo/app/japicollector/aspect.xml")
@ContextConfiguration(classes = MainAppConfigH2.class)
public class AbstractDaoTestContext extends AbstractTest{

	@Autowired
    private ISiteDao siteDao;
	@Autowired
	private IApiDomainDao domainDao;
	
    
    @Autowired
    protected DaoSessionManager sessionManager;
    
    @Autowired
    protected LogAspect logAspect;
    
    
    //------------------------------------ protected methods
	protected Site getSite(long siteId) {
		this.sessionManager.beginTransaction();
		Site site = this.siteDao.getById(siteId, true);
		this.sessionManager.commitTransaction();

		return site;
	}


	protected ApiDomain getDomain(long domainId) {
		this.sessionManager.beginTransaction();
		ApiDomain domain = this.domainDao.getById(domainId, true);
		this.sessionManager.commitTransaction();

		return domain;
	}

  
    
}
