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

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;
import fr.tsadeo.app.japicgwtp.server.dao.IApiDataTypeDao.DataTypeCriteria;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDataType;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.ApiPackage;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.shared.domain.JavaType;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;

/**
 *
 * @author sylvie
 */
@Component
public class ApiDataTypeDaoTest extends AbstractDaoTestContext{

    public static int TYPE_COUNT = 0;
    private static Map<Long, Integer> MAP_PACK2TYPE = new HashMap<>();
     private static Map<Long, Integer> MAP_SITE2TYPE = new HashMap<>();
    
        
    @Autowired
    private ApiDomainDaoTest domainDaoTest;
    @Autowired
    private SiteDaoTest siteDaoTest;
    @Autowired
    private ApiPackageDaoTest packageDaoTest;
    @Autowired
    private IApiPackageDao packageDao;
    @Autowired
    private IApiDomainDao domainDao;
    @Autowired
    private IApiDataTypeDao dataTypeDao;
    
    private static Long domainId;
    private static Long siteId1;
    private static ApiPackage apiPackage1;
    
    @Before
    public void init() {

        if (apiPackage1 == null) {
            ApiDomain domain = domainDaoTest.createApiDomain("Maven", "Maven framework");
            domainId = domain.getId();
            Site site = siteDaoTest.createSite(domainId, "http://www.maven1.xxx/5.1", "Maven API", "5.1");
            siteId1 = site.getId();
            apiPackage1 = packageDaoTest.createApiPackage(siteId1, null, "org.maven1.xx", true);
        }
    }

    @Test
    public void testCreateApiDataType() {

        LOG.info("testCreateApiDataType()");
        this.createApiDataType(apiPackage1, "Collectors", JavaType.TClass);
    }

    @Test
    public void testListByPackage() {
        
        ApiPackage apiPackage2 = packageDaoTest.createApiPackage(siteId1, null, "org.maven2.xx", true);
        
        this.sessionManager.beginTransaction();
        this.packageDao.attachAndUpdate(apiPackage2);
        
        List<ApiDataType> list = this.dataTypeDao.listByPackage(apiPackage2);
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", this.getCountByPack(apiPackage2.getId()), list.size());
        
        this.sessionManager.commitTransaction();
        
        this.createApiDataType(apiPackage2, "MyCLass1", JavaType.TClass);
        this.createApiDataType(apiPackage2, "MyCLass2", JavaType.TClass);
        this.createApiDataType(apiPackage2, "MyInterface1", JavaType.TInterface);
        
        // le package a été modifier, il faut récuperer une bonne ref
        apiPackage2 = packageDaoTest.getProxyById(apiPackage2.getId());
        
        this.sessionManager.beginTransaction();
        this.packageDao.attachAndUpdate(apiPackage2);
        
        list = this.dataTypeDao.listByPackage(apiPackage2);
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", this.getCountByPack(apiPackage2.getId()), list.size());
        
        this.sessionManager.commitTransaction();
    }
    
    
    @Test
    public void testListByPackageAndType() {
        
        ApiPackage apiPackage3 = packageDaoTest.createApiPackage(siteId1, null, "org.maven3.xx", true);
        
        this.sessionManager.beginTransaction();
        this.packageDao.attachAndUpdate(apiPackage3);
        
        List<ApiDataType> list = this.dataTypeDao.listByPackageAndType(apiPackage3, JavaType.TEnum);
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", this.getCountByPack(apiPackage3.getId()), list.size());
        
        this.sessionManager.commitTransaction();
        
        this.createApiDataType(apiPackage3, "MyCLass1", JavaType.TClass);
        this.createApiDataType(apiPackage3, "MyCLass2", JavaType.TClass);
        this.createApiDataType(apiPackage3, "MyInterface1", JavaType.TInterface);
        
        // le package a été modifie, il faut récuperer une bonne ref
        apiPackage3 = packageDaoTest.getProxyById(apiPackage3.getId());
        
        
        this.sessionManager.beginTransaction();
        this.packageDao.attachAndUpdate(apiPackage3);
        
        list = this.dataTypeDao.listByPackageAndType(apiPackage3, JavaType.TEnum);
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", 0, list.size());
        
        
        list = this.dataTypeDao.listByPackageAndType(apiPackage3, JavaType.TClass);
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", 2, list.size());
        
        
        list = this.dataTypeDao.listByPackageAndType(apiPackage3, JavaType.TInterface);
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", 1, list.size());
        
        this.sessionManager.commitTransaction();
    }
    
    @Test
    public void testListBySite() {
        
        // site 2
        Site site2 = siteDaoTest.createSite(domainId, "http://www.maven4.xxx/3.3", "Maven API", "3.3");
        ApiPackage apiPackage4 = packageDaoTest.createApiPackage(site2.getId(), null, "org.maven4.xx", true);
        ApiPackage apiPackage5 = packageDaoTest.createApiPackage(site2.getId(), null, "org.maven5.xx", true);
        
        this.sessionManager.beginTransaction();
        this.packageDao.attachAndUpdate(apiPackage4);
        this.packageDao.attachAndUpdate(apiPackage5);
        
        List<ApiDataType> list = this.dataTypeDao.listByAvailableSite(site2,  null);
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", this.getCountBySite(site2.getId()), list.size());
        
        this.sessionManager.commitTransaction();
        
        
        this.createApiDataType(apiPackage4, "MyCLass1", JavaType.TClass);
        this.createApiDataType(apiPackage4, "MyCLass2", JavaType.TClass);
        this.createApiDataType(apiPackage4, "MyInterface1", JavaType.TInterface);
        
        this.createApiDataType(apiPackage5, "MyCLass3", JavaType.TClass);
        this.createApiDataType(apiPackage4, "MyEnum1", JavaType.TEnum);
        
        // le site a ete modifié. recuperer un nouvau proxy
        site2 = siteDaoTest.getProxyById(site2.getId());
      
        
         this.sessionManager.beginTransaction();
       
        list = this.dataTypeDao.listByAvailableSite(site2, null);
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", this.getCountBySite(site2.getId()), list.size());
        
        
        list = this.dataTypeDao.listByAvailableSite(site2, new DataTypeCriteria("My"));
        assertNotNull("list of data type cannot be null!", list);
        
        this.sessionManager.commitTransaction();
        
        
    }
    

    // test invalid car utilisation de procedures stockee, uniquement sur mysql
    //@Test
    public void testListContainsName() {
        
    	LOG.debug("testListContainsName()");
        Site site3 = siteDaoTest.createSite(domainId, "http://www.maven5.xxx/4.0", "Maven API", "4.0");
        ApiPackage apiPackage5 = packageDaoTest.createApiPackage(site3.getId(), null, "org.maven5.xx", true);
        ApiPackage apiPackage6 = packageDaoTest.createApiPackage(site3.getId(), apiPackage5, "org.maven5.xx.nn", false);
        
        Site site4 = siteDaoTest.createSite(domainId, "http://www.maven6.xxx/7.0", "Maven API", "7.0");
        ApiPackage apiPackage7 = packageDaoTest.createApiPackage(site4.getId(), null, "org.maven6.xx", true);
        
        this.sessionManager.beginTransaction();
        List<ApiDataType> list = this.dataTypeDao.listAllContainsNameForAvailableSites(new DataTypeCriteria("titi"));
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", 0, list.size());
        
        this.sessionManager.commitTransaction();
        
        this.createApiDataType(apiPackage5, "YourCLass1", JavaType.TClass);
        this.createApiDataType(apiPackage6, "YourCLass2", JavaType.TClass);
        this.createApiDataType(apiPackage7, "YourInterface1", JavaType.TInterface);
        
        this.createApiDataType(apiPackage5, "YourCLass3", JavaType.TClass);
        this.createApiDataType(apiPackage6, "YourEnum1", JavaType.TEnum);
        this.createApiDataType(apiPackage7, "YourEnum2", JavaType.TEnum);
        
        
        this.sessionManager.beginTransaction();
        
        list = this.dataTypeDao.listAllContainsNameForAvailableSites(new DataTypeCriteria("YourCLass"));
        assertNotNull("list of data type cannot be null!!", list);
        assertEquals("Wrong size of list!", 3, list.size());
        
        list = this.dataTypeDao.listAllContainsNameForAvailableSites(new DataTypeCriteria("Your", "Maven"));
        assertNotNull("list of data type cannot be null!!", list);
        assertEquals("Wrong size of list!", 6, list.size());
        
        list = this.dataTypeDao.listAllContainsNameForAvailableSites(new DataTypeCriteria("Your", "xxx"));
        assertNotNull("list of data type cannot be null!!", list);
        assertEquals("Wrong size of list!", 0, list.size());
        
        this.sessionManager.commitTransaction();
        
    }

    @Test
    public void testListIdsContainsName() {
        
    	LOG.debug("testListIdsContainsName()");
        Site site9 = siteDaoTest.createSite(domainId, "http://www.maven9.xxx/2.6", "Maven API", "2.6");
        ApiPackage apiPackage5 = packageDaoTest.createApiPackage(site9.getId(), null, "org.maven5.xx", true);
        ApiPackage apiPackage6 = packageDaoTest.createApiPackage(site9.getId(), apiPackage5, "org.maven5.xx.nn", false);
        
        Site site10 = siteDaoTest.createSite(domainId, "http://www.maven10.xxx/1.3", "Maven API", "1.3");
        ApiPackage apiPackage7 = packageDaoTest.createApiPackage(site10.getId(), null, "org.maven6.xx", true);
        
        this.sessionManager.beginTransaction();
        List<VoIdName> list = this.dataTypeDao.listVoIdNameContainsNameForAvailableSites(new DataTypeCriteria("titi"));
        assertNotNull("list of data type cannot be null!", list);
        assertEquals("Wrong size of list!", 0, list.size());
        
        this.sessionManager.commitTransaction();
        
        this.createApiDataType(apiPackage5, "YourCLass1", JavaType.TClass);
        this.createApiDataType(apiPackage6, "YourCLass2", JavaType.TClass);
        this.createApiDataType(apiPackage7, "YourInterface1", JavaType.TInterface);
        
        this.createApiDataType(apiPackage5, "YourCLass3", JavaType.TClass);
        this.createApiDataType(apiPackage6, "YourEnum1", JavaType.TEnum);
        this.createApiDataType(apiPackage7, "YourEnum2", JavaType.TEnum);
        
        
        this.sessionManager.beginTransaction();
        
        list = this.dataTypeDao.listVoIdNameContainsNameForAvailableSites(new DataTypeCriteria("YourCLass"));
        assertNotNull("list of data type cannot be null!!", list);
        assertEquals("Wrong size of list!", 3, list.size());
        
        list = this.dataTypeDao.listVoIdNameContainsNameForAvailableSites(new DataTypeCriteria("Your", "Maven"));
        assertNotNull("list of data type cannot be null!!", list);
        assertEquals("Wrong size of list!", 6, list.size());
        
        list = this.dataTypeDao.listVoIdNameContainsNameForAvailableSites(new DataTypeCriteria("Your", "xxx"));
        assertNotNull("list of data type cannot be null!!", list);
        assertEquals("Wrong size of list!", 0, list.size());
        
        list = this.dataTypeDao.listVoIdNameContainsNameForAvailableSites(new DataTypeCriteria("Your", "Maven", "1.3"));
        assertNotNull("list of data type cannot be null!!", list);
        assertEquals("Wrong size of list!", 2, list.size());
        
        list = this.dataTypeDao.listVoIdNameContainsNameForAvailableSites(new DataTypeCriteria("Enum", "Maven", "2.6"));
        assertNotNull("list of data type cannot be null!!", list);
        assertEquals("Wrong size of list!", 1, list.size());
        
        this.sessionManager.commitTransaction();
        
    }
    
  
    
    @Test
    public void testListByDomain() {
        
        // site 5
        Site site5 = siteDaoTest.createSite(domainId, "http://www.maven7.xxx/5.5", "Maven API", "5.5");
        ApiPackage apiPackage6 = packageDaoTest.createApiPackage(site5.getId(), null, "org.maven6.xx", true);
        
        // site 6
        Site site6 = siteDaoTest.createSite(domainId, "http://www.maven8.xxx/6.6", "Maven API", "6.6");
        ApiPackage apiPackage7 = packageDaoTest.createApiPackage(site6.getId(), null, "org.maven7.xx", true);
        
        this.sessionManager.beginTransaction();
        ApiDomain domain = this.domainDao.getById(domainId, false);
        this.packageDao.attachAndUpdate(apiPackage6);
        this.packageDao.attachAndUpdate(apiPackage7);
        
        List<ApiDataType> list = this.dataTypeDao.listByDomainAndAvailableSites(domain, null);
        assertNotNull("list of data type cannot be null!", list);
        int count = this.getCountBySite(site5.getId()) + this.getCountBySite(site6.getId());
        assertEquals("Wrong size of list!", count, list.size());
        
        this.sessionManager.commitTransaction();
        
        
        this.createApiDataType(apiPackage6, "MyCLass1", JavaType.TClass);
        this.createApiDataType(apiPackage6, "MyCLass2", JavaType.TClass);
        this.createApiDataType(apiPackage6, "MyInterface1", JavaType.TInterface);
        
        this.createApiDataType(apiPackage7, "MyCLass3", JavaType.TClass);
        this.createApiDataType(apiPackage6, "MyEnum1", JavaType.TEnum);
        
       
         this.sessionManager.beginTransaction();
        this.domainDao.attachAndUpdate(domain);
        list = this.dataTypeDao.listByDomainAndAvailableSites(domain, null);
        assertNotNull("list of data type cannot be null!", list);
        count = this.getCountBySite(site5.getId()) + this.getCountBySite(site6.getId());
        assertEquals("Wrong size of list!", count, list.size());
        
        list = this.dataTypeDao.listByDomainAndAvailableSites(domain, new DataTypeCriteria("My", "Maven"));
        assertNotNull("list of data type cannot be null!", list);
        
        this.sessionManager.commitTransaction();
        
        
    }
    
    
    @Test
    public void testGetById() {

        LOG.info("testGetById()");
        this.sessionManager.beginTransaction();
        packageDao.attachAndUpdate(apiPackage1);
        
        ApiDataType dataType = this.buildApiDataType( "Map", JavaType.TInterface, apiPackage1.getId(), siteId1);
        apiPackage1.addApiDataType(dataType);
        
        dataTypeDao.attachAndSave(dataType);
        
        ApiDataType dataTypeById = dataTypeDao.getById(dataType.getId(), true);
        Long id = dataTypeById.getId();
        assertNotNull("dataTypeById cannot be null!", dataTypeById);
        assertEquals("dataTypeById must be equal to dataType", dataType, dataTypeById);
        this.sessionManager.commitTransaction();
        
        
        Session session2 = this.sessionManager.beginTransaction();
        //get a proxy
        ApiDataType dataTypeProxy = dataTypeDao.getById(id, false);
        assertNotNull("dataTypeProxy cannot be null!", dataTypeProxy);
        assertTrue("session must contains dataTypeProxy!", session2.contains(dataTypeProxy));
        
        ApiDataType dataTypeBdd = dataTypeDao.getById(id, true);
        assertNotNull("dataTypeBdd cannot be null!", dataTypeBdd);
        assertTrue("session must contains datatTypeBdd!", session2.contains(dataTypeBdd));
        
        this.sessionManager.commitTransaction();

    }

    @Test
    public void testGetByNaturalId() throws MalformedURLException {
    	
    	apiPackage1 = packageDaoTest.getProxyById(apiPackage1.getId());

        LOG.info("testGetByNaturalId()");
        this.sessionManager.beginTransaction();
        packageDao.attachAndUpdate(apiPackage1);
        
        String shortName = "Override";
        ApiDataType dataType = this.buildApiDataType( shortName, JavaType.TAnnotation, apiPackage1.getId() , siteId1);
        apiPackage1.addApiDataType(dataType);
        
        dataTypeDao.attachAndSave(dataType);
        
        ApiDataType dataTypeByNatId = dataTypeDao.getByNaturalId(apiPackage1, shortName, true);
        assertNotNull("dataTypeByNatId cannot be null!", dataTypeByNatId);
        assertEquals("dataTypeByNatId must be equals to  dataType", dataType, dataTypeByNatId);
        this.sessionManager.commitTransaction();
        
        
        Session session2 = this.sessionManager.beginTransaction();
        //get a proxy
        ApiDataType dataTypeProxy = dataTypeDao.getByNaturalId(apiPackage1, shortName, false);
        assertNotNull("dataTypeProxy cannot be null!", dataTypeProxy);
        assertTrue("session must contains dataTypeProxy!", session2.contains(dataTypeProxy));
        
        ApiDataType datatTypeBdd = dataTypeDao.getByNaturalId(apiPackage1, shortName, true);
        assertNotNull("datatTypeBdd cannot be null!", datatTypeBdd);
        assertTrue("session must contains datatTypeBdd!", session2.contains(datatTypeBdd));
        
        this.sessionManager.commitTransaction();

    } 

        
  @Test
    public void testDeleteApiDataType() throws MalformedURLException {
        
        LOG.info("testDeleteApiDataType()");
        
        LOG.info("create dataType...");
        ApiDataType dataType = this.createApiDataType(apiPackage1, "Exception", JavaType.TClass);
        
        LOG.info("delete detached dataType...");
        // dataType detached >> then delete (without reattach it)
        Session session2 = this.sessionManager.beginTransaction();
        assertFalse("session cannot contains dataType!", session2.contains(dataType));
        dataTypeDao.delete(dataType);
        // il faut récupérer une nouvelle réference!
        apiPackage1 = packageDao.getById(apiPackage1.getId(), true);
        this.sessionManager.commitTransaction();
        
        this.manageCountType(siteId1, apiPackage1.getId(), false);
        
        LOG.info("verify dataType null...");
        //get a proxy
        ApiDataType dataTypeProxy = this.getProxyById(dataType.getId());
        assertNull("dataTypeProxy must be null!", dataTypeProxy);
    }

    //--------------------------------------- private methods
	private ApiDataType getProxyById(Long id) {

		this.sessionManager.beginTransaction();
		ApiDataType dataType = dataTypeDao.getById(id, false);
		this.sessionManager.commitTransaction();

		return dataType;
	}
    private  ApiDataType createApiDataType(ApiPackage apiPackage, String shortName, JavaType type) {

        LOG.info("createApiDataType()");
        Session session = this.sessionManager.beginTransaction();
        packageDao.attachUnmodifiedItem(apiPackage);
        
        ApiDataType dataType = this.buildApiDataType( shortName, type, apiPackage.getId(), apiPackage.getSite().getId());
        assertFalse("session cannot contains dataType!", session.contains(dataType));

        apiPackage.addApiDataType(dataType);

        dataTypeDao.attachAndSave(dataType);
        assertTrue("session must contains datatType!", session.contains(dataType));
        this.sessionManager.commitTransaction();
        
        return this.getProxyById(dataType.getId());

    }
    private ApiDataType buildApiDataType(String name, JavaType apiType, Long packId, Long siteId) {
        
        this.manageCountType(siteId, packId, true);
        ApiDataType dataType = new ApiDataType(apiType, name);
        dataType.setSince("2.2");
        
        return dataType;
    }
    private int getCountByPack(long packId) {
        Integer countForPackage = MAP_PACK2TYPE.get(packId);
        return countForPackage == null ? 0:countForPackage;
    }
    private int getCountBySite(long siteId) {
        Integer countForSite = MAP_SITE2TYPE.get(siteId);
        return countForSite == null ? 0:countForSite;
    }
    
    private void manageCountType(Long siteId, Long packId, boolean add) {
        
        int countForPackage = this.getCountByPack(packId);
        int countForSite = this.getCountBySite(siteId);
        if (add) {
            TYPE_COUNT++;
            countForPackage++;
            countForSite++;
        } else {
            TYPE_COUNT--;
            countForPackage--;
            countForSite--;
        }
        MAP_PACK2TYPE.put(packId, countForPackage);
        MAP_SITE2TYPE.put(siteId, countForSite);
    }
    

}
