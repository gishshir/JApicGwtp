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

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;
import fr.tsadeo.app.japicgwtp.server.dao.ApiDomainDaoTest;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.IVoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDomainForEdit;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDomainForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoInitSiteDatas;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoListItems;

/**
 *
 * @author sylvie
 */
public class ApiDomainServiceTest extends AbstractDaoTestContext {

    @Autowired
    private IApiDomainService domainService;
    @Autowired
    private ApiDomainDaoTest domainDaoTest;

    private static Long domainId1;
    private static Long domainId2;

    @Before
    public void init() {

        if (domainId1 == null) {
            ApiDomain domain1 = domainDaoTest.createApiDomain("DomainService1", "DomainService1 framework");
            domainId1 = domain1.getId();

            ApiDomain domain2 = domainDaoTest.createApiDomain("DomainService2", "DomainService2 framework");
            domainId2 = domain2.getId();
        }
    }

    @Test
    public void testGetVoInitSiteDatas() throws JApicException {

        VoInitSiteDatas voInitSiteDatas = this.domainService.getVoInitSiteDatas();
        assertNotNull("voInitSiteDatas cannot be null!", voInitSiteDatas);
        assertNotNull("list of domains cannot be null!", voInitSiteDatas.getListDomains());
        assertEquals("wrong size for the list of domains!", ApiDomainDaoTest.DOMAIN_COUNT, voInitSiteDatas.getListDomains().size());
    }

    @Test
    public void testListDomains() throws JApicException {
    	
    	this.domainService.verifyDefaultItems();
    	ApiDomainDaoTest.DOMAIN_COUNT++;

        VoListItems<VoDomainForGrid, VoItemProtection> list = this.domainService.listDomains(true);

        assertNotNull("result cannot be null!", list);
        assertNotNull("list of domains cannot be null!", list.getListItems());
        

        list.getListItems().stream()
              .forEach(d -> LOG.info(d.getName()));
        
        assertEquals("wrong size for list of domains", ApiDomainDaoTest.DOMAIN_COUNT, list.getListItems().size());
    
    
    }

    public void testGetNemDomain() throws JApicException {

        VoDomainForEdit voDomain = this.domainService.getItemForEdit(IVoId.ID_UNDEFINED, true, true);
        assertNotNull("domain cannot be null!", voDomain);
        assertNotNull("domain protection cannot be null!", voDomain.getProtection());
        assertTrue("domain must be undefined!", voDomain.isUndefined());
    }

    @Test
    public void testGetDomain() throws JApicException {

        // wrong id
        VoDomainForEdit voDomain = this.domainService.getItemForEdit(999, false, true);
        assertNull("domain must be null", voDomain);

        // existing id
        voDomain = this.domainService.getItemForEdit(domainId2, false, true);
        assertNotNull("domain cannot be null!", voDomain);
        assertNotNull("domain protection cannot be null!", voDomain.getProtection());
        assertNotNull("domain name cannot be null!", voDomain.getName());
        assertNotNull("domain description cannot be null!", voDomain.getDescription());
        assertFalse("domain cannot be undefined!", voDomain.isUndefined());

    }

    @Test
    public void testValidateDomain() throws JApicException {

        VoDomainForEdit voDomain = this.domainService.getItemForEdit(domainId1, false, true);
        VoDatasValidation validation = this.domainService.validateItem(voDomain);

        assertNotNull("validation cannot be null!", validation);
        assertTrue("validation must be valid!", validation.isValid());

        voDomain.setName(null);
        validation = this.domainService.validateItem(voDomain);

        assertNotNull("validation cannot be null!", validation);
        assertFalse("validation cannot be valid!", validation.isValid());
        assertNotNull("errorMessages cannot be null!", validation.getErrorMessages());
        assertEquals("wrong count of messages!", 1, validation.getErrorMessages().length);
    }

    @Test
    public void testCreateDomain() throws JApicException {

        VoDomainForEdit voDomain = new VoDomainForEdit();
        voDomain.setName("API domain service test 2");
        voDomain.setDescription("description " + voDomain.getName());

        VoDomainForEdit result = this.domainService.createOrUpdateItem(voDomain, true);
        this.logAndAssert(voDomain, result);

        result = this.domainService.getItemForEdit(result.getId(), false, true);
        this.logAndAssert(voDomain, result);
        
        ApiDomainDaoTest.DOMAIN_COUNT++;

    }
    @Test
    public void testUpdateDomain() throws JApicException {
        
        
        VoDomainForEdit voDomain = this.domainService.getItemForEdit(domainId1, false, true);
        voDomain.setName(voDomain.getName() + " modified");
        voDomain.setDescription(voDomain.getDescription() + " modified");

        VoDomainForEdit result = this.domainService.createOrUpdateItem(voDomain, false);
        this.logAndAssert(voDomain, result);

        result = this.domainService.getItemForEdit(result.getId(), false, true);
        this.logAndAssert(voDomain, result);
    }
    
    @Test
    public void testDeleteDomain() throws JApicException {
        
        
        VoDomainForEdit voDomain = new VoDomainForEdit();
        voDomain.setName("API domain service test 3");
        
        VoDomainForEdit voDomainCreated = this.domainService.createOrUpdateItem(voDomain, true);
        voDomainCreated = this.domainService.getItemForEdit(voDomainCreated.getId(), false, true);
        assertNotNull("result cannot be null!", voDomainCreated);
        assertFalse("voDomainCreated cannot be transient !", voDomainCreated.isUndefined());
        
        ApiDomainDaoTest.DOMAIN_COUNT++;
        
        boolean result = this.domainService.deleteItem(voDomainCreated.getId());
        assertTrue("result must be true!", result);
        
        VoDomainForEdit voDomainForControle = this.domainService.getItemForEdit(voDomainCreated.getId(), false, true);
        assertNull("voDomainForControle must be null!", voDomainForControle);
        
        ApiDomainDaoTest.DOMAIN_COUNT--;
    }
    
    //--------------------------------- private methods
    private void logAndAssert(VoDomainForEdit voDomain, VoDomainForEdit result) {

        assertNotNull("result cannot be null!", result);
        assertFalse("result cannot be transient!", result.isUndefined());
        assertEquals("Wrong name!", voDomain.getName(), result.getName());
        assertEquals("Wrong description!", voDomain.getDescription(), result.getDescription());
    }

}
