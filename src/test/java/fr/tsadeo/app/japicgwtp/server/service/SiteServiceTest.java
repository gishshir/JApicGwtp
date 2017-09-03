/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;
import fr.tsadeo.app.japicgwtp.server.dao.ApiDomainDaoTest;
import fr.tsadeo.app.japicgwtp.server.dao.ApiPackageDaoTest;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao;
import fr.tsadeo.app.japicgwtp.server.dao.SiteDaoTest;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.ApiPackage;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.IVoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoListItems;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForEdit;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteProtection;

/**
 *
 * @author sylvie
 */
public class SiteServiceTest extends AbstractDaoTestContext {

    private final static Log LOG = LogFactory.getLog(SiteServiceTest.class);

    @Autowired
    private ISiteService siteService;
    @Autowired
    private ApiDomainDaoTest domainDaoTest;
    @Autowired
    private ISiteDao siteDao;

    @Autowired
    private SiteDaoTest siteDaoTest;
    @Autowired
    private ApiPackageDaoTest packageDaoTest;

    private static Long domainId1;
    private static Long domainId2;

    private static Long siteId1;

    @Before
    public void init() {

        if (domainId1 == null) {
            ApiDomain domain1 = domainDaoTest.createApiDomain("SiteService1", "SiteService1 framework");
            domainId1 = domain1.getId();

            ApiDomain domain2 = domainDaoTest.createApiDomain("SiteService2", "SiteService2 framework");
            domainId2 = domain2.getId();

            Site site1 = siteDaoTest.createSite(domainId1, "http://org.siteservice1.xx/2.5/", "Spring API", "2.5");
            siteId1 = site1.getId();
        }
    }
    
    @Test
    public void testSearchSites() throws JApicException {
    	
    	VoSearchCriteria criteria = new VoSearchCriteria();
    	criteria.setSearch("n");
    	
    	VoSearchResultDatas<VoSiteForGrid, VoSiteProtection>  result = this.siteService.searchSites(criteria, true);
    	assertNotNull("result cannot be null!", result);
    	assertNotNull("result.listItems cannot be null!", result.getListItems());
    	assertEquals("Wrong result.search!", "n", result.getSearch());
    	assertTrue("list cannot be empty!", !result.getListItems().isEmpty());
    	
    	for (VoSiteForGrid voSite : result.getListItems()) {
			LOG.info(voSite.getName() + " - " + voSite.getVoHighlightedName());
		}
    }

    @Test
    public void testSearchAllSites() throws JApicException {
    	
    	VoSearchCriteria criteria = new VoSearchCriteria();
    	criteria.setSearch("*");
    	
    	VoSearchResultDatas<VoSiteForGrid, VoSiteProtection>  result = this.siteService.searchSites(criteria, true);
    	assertNotNull("result cannot be null!", result);
    	assertNotNull("result.listItems cannot be null!", result.getListItems());
    	assertEquals("Wrong result.search!", null, result.getSearch());
    	assertTrue("list cannot be empty!", !result.getListItems().isEmpty());
    	
    	for (VoSiteForGrid voSite : result.getListItems()) {
			LOG.info(voSite.getName() + " - " + voSite.getVoHighlightedName());
		}
    }
    @Test
    public void testListSitesByDomain() throws JApicException {

        this.siteDaoTest.createSite(domainId1, "http://siteservice1/4.3", "Siteservice API", "4.3");
        this.siteDaoTest.createSite(domainId1, "http://siteservice1/2.0", "Siteservice API", "2.0");
        this.siteDaoTest.createSite(domainId1, "http://siteservice1/6.3", "Siteservice API", "6.3");

        this.siteDaoTest.createSite(domainId2, "http://siteservice2/4.3", "Siteservice API", "4.3");
        this.siteDaoTest.createSite(domainId2, "http://siteservice2/9.8", "Siteservice API", "9.8");

        VoListItems<VoSiteForGrid, VoSiteProtection> voSites1 = this.siteService.listSitesByDomain(domainId1, false, true);
        assertNotNull("voList cannot be null!", voSites1);
        assertNotNull("voList.listSites cannot be null!", voSites1.getListItems());
        assertEquals("Wrong count of sites for domain1!", this.siteDaoTest.getCount(domainId1),
                voSites1.getListItems().size());

        VoListItems<VoSiteForGrid, VoSiteProtection> voSites2 = this.siteService.listSitesByDomain(domainId2, false, false);
        assertNotNull("voList cannot be null!", voSites2);
        assertNotNull("voList.listSites cannot be null!", voSites2.getListItems());
        assertEquals("Wrong count of sites for domain1!", this.siteDaoTest.getCount(domainId2),
                voSites2.getListItems().size());
        
        // available only
        VoListItems<VoSiteForGrid, VoSiteProtection> voSites3 = this.siteService.listSitesByDomain(domainId2, true, false);
        assertNotNull("voList cannot be null!", voSites3);
        assertNotNull("voList.listSites cannot be null!", voSites3.getListItems());
        assertEquals("Wrong count of sites for domain1!", 0,
                voSites2.getListItems().size());
    }

  
    @Test
    public void testGetNewSiteForEdit() throws JApicException {

        VoSiteForEdit voSite = this.siteService.getItemForEdit(IVoId.ID_UNDEFINED, true, true);

        assertNotNull("voSite cannot be null!", voSite);
        assertNotNull("site protection cannot be null!", voSite.getProtection());
        assertNull("site name must be null!", voSite.getName());
        assertTrue("site must be undefined!", voSite.isUndefined());


    }

    @Test
    public void testGetSiteForEdit() throws JApicException {

        VoSiteForEdit voSite = this.siteService.getItemForEdit(siteId1, false, true);

        assertNotNull("voSite cannot be null!", voSite);
        assertNotNull("site protection cannot be null!", voSite.getProtection());
        assertNotNull("site name cannot be null!", voSite.getName());
        assertFalse("site cannot be undefined!", voSite.isUndefined());
        
                
        // wrong id
        voSite = this.siteService.getItemForEdit(999L, false, true);
        assertNull("voSite must be null!", voSite);

    }

    @Test
    public void testValidateSite() throws JApicException {

        VoSiteForEdit voSite = this.siteService.getItemForEdit(siteId1, false, true);
        VoDatasValidation validation = this.siteService.validateItem(voSite);

        assertNotNull("validation cannot be null!", validation);
        assertTrue("validation must be valid!", validation.isValid());

        voSite.setName(null);
        validation = this.siteService.validateItem(voSite);

        assertNotNull("validation cannot be null!", validation);
        assertFalse("validation cannot be valid!", validation.isValid());
        assertNotNull("errorMessages cannot be null!", validation.getErrorMessages());
        assertEquals("wrong count of messages!", 1, validation.getErrorMessages().length);
    }

    @Test
    public void testCreateSite() throws JApicException {

        VoSiteForEdit voSite = new VoSiteForEdit();
        voSite.setName("API service test 2");
        voSite.setVersion("5.5");
        String url = "http://siteservice2/5.5";
        voSite.setUrl(url);
        voSite.setDomainId(domainId2);

        VoSiteForEdit result = this.siteService.createOrUpdateItem(voSite, true);
        this.logAndAssert(voSite, result);

        result = this.siteService.getItemForEdit(result.getId(), false, true);
        this.logAndAssert(voSite, result);

        this.siteDaoTest.manageCountSites(domainId2, true);
    }

    @Test
    public void testUpdateSite() throws JApicException {

        VoSiteForEdit voSite = this.siteService.getItemForEdit(siteId1, false, true);
        voSite.setName(voSite.getName() + " modified");
        voSite.setUrl(voSite.getUrl() + "/modified");
        voSite.setVersion("9.9.9.9");


        VoSiteForEdit result = this.siteService.createOrUpdateItem(voSite, false);
        this.logAndAssert(voSite, result);

        result = this.siteService.getItemForEdit(result.getId(), false, true);
        this.logAndAssert(voSite, result);
    }

    @Test
    public void testDeleteSite() throws JApicException {

        VoSiteForEdit voSite = new VoSiteForEdit();
        voSite.setName("API service test 3");
        voSite.setVersion("6.6");
        String url = "http://siteservice3/6.6";
        voSite.setUrl(url);
        voSite.setDomainId(domainId2);

        VoSiteForEdit voSiteCreated = this.siteService.createOrUpdateItem(voSite, true);
        voSiteCreated = this.siteService.getItemForEdit(voSiteCreated.getId(), false, true);
        assertNotNull("result cannot be null!", voSiteCreated);
        assertFalse("voSiteCreated cannot be transient !", voSiteCreated.isUndefined());
        
        boolean result = this.siteService.deleteItem(voSiteCreated.getId());
        assertTrue("result must be true!", result);
        
        this.sessionManager.beginTransaction();
        Site siteForControle = this.siteDao.getById(voSiteCreated.getId(), false);
        assertNull("voSiteForControle must be null!", siteForControle);
        this.sessionManager.commitTransaction();
        
    }
    
    @Test
    public void testDeleteAllPackage() throws JApicException {
    	
    	 ApiPackage main = packageDaoTest.createApiPackage(siteId1, null, "org.hibernate.ggg", true);
         packageDaoTest.createApiPackage(siteId1, main, "org.hibernate.ggg.bb", false);
         packageDaoTest.createApiPackage(siteId1, main, "org.hibernate.ggg.cc", false);
         
         this.sessionManager.beginTransaction();
         Site site = this.siteDao.getById(siteId1, true);
         assertEquals("Wrong cout of ApiPackage!", 3, site.getListApiPackages().size());
         this.sessionManager.commitTransaction();
         
         this.siteService.deleteAllPackages(siteId1);
         
         this.sessionManager.beginTransaction();
         site = this.siteDao.getById(siteId1, true);
         assertEquals("Wrong cout of ApiPackage!", 0, site.getListApiPackages().size());
         this.sessionManager.commitTransaction();
         
         
    	
    }

    private void logAndAssert(VoSiteForEdit voSite, VoSiteForEdit result) {

        assertNotNull("result cannot be null!", result);
        assertFalse("result cannot be transient!", result.isUndefined());
        assertEquals("Wrong name!", voSite.getName(), result.getName());
        assertEquals("Wrong version!", voSite.getVersion(), result.getVersion());
        assertEquals("Wrong url!", voSite.getUrl(), result.getUrl());
    }
}
