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

import java.util.List;

import org.hibernate.Session;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.Site;

/**
 *
 * @author sylvie
 */
@Component
public class ApiDomainDaoTest extends AbstractDaoTestContext {
    
    public static int DOMAIN_COUNT = 0;

    @Autowired
    private IApiDomainDao domainDao;
    @Autowired
    private SiteDaoTest siteDaoTest;

    @Test
    public void testCreateApiDomain() {
        this.createApiDomain("SpringXX", "Spring framework");
    }
    
    
    @Test
    public void testListAll() {
        
        this.createApiDomain("Domain1", "Domain1 API");
        this.createApiDomain("Domain2", "Domain1 API");
        this.createApiDomain("Domain3", "Domain1 API");
        
        this.sessionManager.beginTransaction();
        List<ApiDomain> list = this.domainDao.listAll();
        assertNotNull("list of domain cannot be null!", list);
        assertTrue("wrong size of list!", list.size() >= 3);
        this.sessionManager.commitTransaction();
    }
    
    
    @Test
    public void testGetById() {

        this.sessionManager.beginTransaction();

        ApiDomain domain1  = this.buildApiDomain("HibernateXX", "Hibernate API");
        domainDao.attachAndSave(domain1);
        
        ApiDomain domain2 = domainDao.getById(domain1.getId(), true);
        Long id = domain2.getId();
        assertNotNull("domain2 cannot be null!", domain2);
        assertEquals("domain1 must be domain2", domain1, domain2);
        this.sessionManager.commitTransaction();
        
        
        Session session2 = this.sessionManager.beginTransaction();
        //get a proxy
        ApiDomain domainProxy = domainDao.getById(id, false);
        assertNotNull("domainProxy cannot be null!", domainProxy);
        assertTrue("session must contains domainProxy!", session2.contains(domainProxy));
        
        ApiDomain domainBdd = domainDao.getById(id, true);
        assertNotNull("domainBdd cannot be null!", domainBdd);
        assertTrue("session must contains domainBdd!", session2.contains(domainBdd));
        
        this.sessionManager.commitTransaction();

    }    

     @Test
    public void testGetByNaturalId() {

        this.sessionManager.beginTransaction();
        String name = "GWTxx";
        ApiDomain domain1  = this.buildApiDomain(name, "GWT framework");
        domainDao.attachAndSave(domain1);
        
        ApiDomain  domainByNatId = domainDao.getByNaturalId(name, true);
        assertNotNull("domainByNatId cannot be null!", domainByNatId);
        assertEquals("domain1 must be domainByNatId", domain1, domainByNatId);
        this.sessionManager.commitTransaction();
        
        
        Session session2 = this.sessionManager.beginTransaction();
        //get a proxy
        ApiDomain domainProxy = domainDao.getByNaturalId(name, false);
        assertNotNull("domainProxy cannot be null!", domainProxy);
        assertTrue("session must contains domainProxy!", session2.contains(domainProxy));
        
        ApiDomain domainBdd = domainDao.getByNaturalId(name, true);
        assertNotNull("domainBdd cannot be null!", domainBdd);
        assertTrue("session must contains domainBdd!", session2.contains(domainBdd));
        
        this.sessionManager.commitTransaction();

    } 

  @Test
    public void testDeleteApiDomain() {
        
        LOG.info("testDeleteApiDomain()");
        
        LOG.info("create domain...");
        ApiDomain domain1 = this.createApiDomain("MavenXX", "description MavenXX");
        
        LOG.info("delete detached domain...");
       this.sessionManager.beginTransaction();
        domainDao.delete(domain1);
        this.sessionManager.commitTransaction();
        
        this.manageCountDomains(false);
        
        LOG.info("verify domain null...");
        ApiDomain domainProxy = this.getProxyById(domain1.getId());
        assertNull("domainProxy must be null!", domainProxy);
    }
  
  @Test
  public void testDeleteApiDomainCascading() {

      LOG.info("testDeleteApiDomainCascading()");
      ApiDomain domain2 = this.createApiDomain("MavenVVVV", "description MavenVVVV");

      LOG.info("create site ...");
      Site site  = siteDaoTest.createSite(domain2.getId(), "http://org.spring.xx/2.0.1","Spring API",   "2.0.1");

      // on supprime le domain, le site doit aussi etre supprime
      this.sessionManager.beginTransaction();
      this.domainDao.delete(domain2);
      this.sessionManager.commitTransaction();

      this.manageCountDomains(false);
      this.siteDaoTest.manageCountSites(domain2.getId(), false);
      
      // on s'assure que le domain n'existe plus
      ApiDomain domainProxy = this.getProxyById(domain2.getId());
      assertNull("domainProxy must be null!", domainProxy);              
  }

    //------------------------------------------------
     public ApiDomain createApiDomain(String name, String description) {

        Session session = this.sessionManager.getCurrentSession();
        session.getTransaction().begin();

        ApiDomain domain  = this.buildApiDomain(name, description);
        assertFalse("session cannot contains domain!", session.contains(domain));
        domainDao.attachAndSave(domain);
        assertTrue("session must contains domain!", session.contains(domain));

        //Hibernate also flushes and closes the current
        //Session and its persistence context
        //if you commit or roll back the transaction.
        session.getTransaction().commit();
        
        return this.getProxyById(domain.getId());
     }
    //-------------------------------------------------private methods
    private ApiDomain getProxyById(Long id) {
    	
    	this.sessionManager.beginTransaction();
    	ApiDomain domain = domainDao.getById(id, false);
    	this.sessionManager.commitTransaction();
    	
    	return domain;
    }
    private ApiDomain buildApiDomain(String name, String description) {

        this.manageCountDomains(true);
        ApiDomain domain = new ApiDomain(name);
        domain.setDescription(description);
        return domain;
    }
    
    private void manageCountDomains(boolean add) {
        if (add){
            DOMAIN_COUNT++;
        } else {
            DOMAIN_COUNT--;
        }
    }
}
