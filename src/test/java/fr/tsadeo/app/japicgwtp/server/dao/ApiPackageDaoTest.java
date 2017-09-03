/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.ApiPackage;
import fr.tsadeo.app.japicgwtp.server.domain.Site;

/**
 *
 * @author sylvie
 */
@Component
public class ApiPackageDaoTest extends AbstractDaoTestContext {

	public static int PACK_COUNT = 0;
	private static Map<Long, Integer> MAP_SITE2COUNT = new HashMap<>();

	@Autowired
	private ApiDomainDaoTest domainDaoTest;
	@Autowired
	private SiteDaoTest siteDaoTest;
	@Autowired
	private ISiteDao siteDao;
	@Autowired
	private IApiPackageDao packageDao;

	private static Long domainId;
	private static Long siteId1;

	@Before
	public void init() {

		if (siteId1 == null) {
			ApiDomain domain = domainDaoTest.createApiDomain("Hibernate", "Hibernate framework");
			domainId = domain.getId();
			Site site = siteDaoTest.createSite(domainId, "http://www.hibernate1.xxx/5.1", "Hibernate API", "5.1");
			siteId1 = site.getId();
		}
	}

	@Test
	public void testCreateApiPackage() {

		LOG.info("testCreateApiPackage()");

		LOG.info("create main package...");
		ApiPackage main = this.createApiPackage(siteId1, null, "org.hibernate", true);

		this.sessionManager.beginTransaction();
		Site site = this.siteDao.getById(siteId1, true);
		assertNotNull("there must be a main package!", site.getMainPackage());
		assertEquals("main must be the main package!", main.getId(), site.getMainPackage().getId());
		this.sessionManager.commitTransaction();

		LOG.info("create common package...");
		ApiPackage other = this.createApiPackage(siteId1, main, "org.hibernate.yyy", false);

		this.sessionManager.beginTransaction();
		site = this.siteDao.getById(siteId1, true);
		assertNotNull("there must be a main package!", site.getMainPackage());
		assertNotEquals("other cannot be the main package", other.getId(), site.getMainPackage().getId());
		ApiPackage parent = other.getParent();
		assertNotNull("other.parent must be not null!", parent);
		assertEquals("parent must be the main package!", parent.getId(), site.getMainPackage().getId());
		this.sessionManager.commitTransaction();

	}

	@Test
	public void testListBySite() {

		Site site2 = siteDaoTest.createSite(domainId, "http://www.hibernate2.yyy/2.2", "Hibernate API", "2.2");
		Long siteId2 = site2.getId();

		this.sessionManager.beginTransaction();
		this.siteDao.attachAndUpdate(site2);

		List<ApiPackage> list = this.packageDao.listBySite(site2);
		assertNotNull("list of packages cannot be null!", list);
		assertEquals("Wrong size of list!", this.getCount(siteId2), list.size());

		this.sessionManager.commitTransaction();

		ApiPackage main = this.createApiPackage(siteId2, null, "org.hibernate.yyy", true);
		this.createApiPackage(siteId2, main, "org.hibernate.yyy.bb", false);
		this.createApiPackage(siteId2, main, "org.hibernate.yyy.cc", false);

		this.sessionManager.beginTransaction();
		list = this.packageDao.listBySite(site2);
		assertNotNull("list of packages cannot be null!", list);
		assertEquals("Wrong size of list!", this.getCount(siteId2), list.size());

		this.sessionManager.commitTransaction();

	}

	@Test
	public void testGetById() {

		LOG.info("testGetById()");
		this.sessionManager.beginTransaction();
		Site site = siteDao.getById(siteId1, true);

		ApiPackage apiPackage = this.buildApiPackage(site.getMainPackage(), "org.hibernate.xxx", siteId1);
		site.addApiPackage(apiPackage);

		packageDao.attachAndSave(apiPackage);

		ApiPackage packageById = packageDao.getById(apiPackage.getId(), true);
		Long id = packageById.getId();
		assertNotNull("packageById cannot be null!", packageById);
		assertEquals("packageById must be apiPackage", apiPackage, packageById);
		this.sessionManager.commitTransaction();

		Session session2 = this.sessionManager.beginTransaction();
		// get a proxy
		ApiPackage packageProxy = packageDao.getById(id, false);
		assertNotNull("packageProxy cannot be null!", packageProxy);
		assertTrue("session must contains packageProxy!", session2.contains(packageProxy));

		ApiPackage packageBdd = packageDao.getById(id, true);
		assertNotNull("packageBdd cannot be null!", packageBdd);
		assertTrue("session must contains packageBdd!", session2.contains(packageBdd));

		this.sessionManager.commitTransaction();

	}

	@Test
	public void testGetByNaturalId() throws MalformedURLException {

		LOG.info("testGetByNaturalId()");
		this.sessionManager.beginTransaction();
		Site site = siteDao.getById(siteId1, true);

		String longName = "org.hibernate.nnn";
		ApiPackage apiPackage = this.buildApiPackage(site.getMainPackage(), longName, siteId1);
		site.addApiPackage(apiPackage);

		packageDao.attachAndSave(apiPackage);

		ApiPackage packByNatId = packageDao.getByNaturalId(site, longName, true);
		assertNotNull("packByNatId cannot be null!", packByNatId);
		assertEquals("apiPackage must be packByNatId", apiPackage, packByNatId);
		this.sessionManager.commitTransaction();

		Session session2 = this.sessionManager.beginTransaction();
		// get a proxy
		ApiPackage packProxy = packageDao.getByNaturalId(site, longName, false);
		assertNotNull("packProxy cannot be null!", packProxy);
		assertTrue("session must contains packProxy!", session2.contains(packProxy));

		ApiPackage packBdd = packageDao.getByNaturalId(site, longName, true);
		assertNotNull("packBdd cannot be null!", packBdd);
		assertTrue("session must contains packBdd!", session2.contains(packBdd));

		this.sessionManager.commitTransaction();

	}

	@Test
	public void testDeleteApiPackage() throws MalformedURLException {

		LOG.info("testDeleteApiPackage()");

		LOG.info("create package...");
		ApiPackage apiPackage = this.createApiPackage(siteId1, null, "org.hibernate.kkk", false);

		LOG.info("delete detached package...");
		// package detached >> then delete (without reattach it)
		Session session2 = this.sessionManager.beginTransaction();
		assertFalse("session cannot contains site!", session2.contains(apiPackage));
		packageDao.delete(apiPackage);
		this.sessionManager.commitTransaction();

		this.manageCountPackages(siteId1, false);

		LOG.info("verify package null...");
        ApiPackage packProxy = this.getProxyById(apiPackage.getId());
		assertNull("packProxy must be null!", packProxy);
	}

	// ------------------------------------ package methods
	public ApiPackage createApiPackage(Long siteId, ApiPackage parent, String longName, boolean main) {

		Session session = this.sessionManager.beginTransaction();
		Site site = siteDao.getById(siteId, true);

		parent = main ? null : (parent == null ? site.getMainPackage() : parent);
		ApiPackage apiPackage = this.buildApiPackage(parent, longName, siteId);
		assertFalse("session cannot contains apiPackage!", session.contains(apiPackage));

		site.addApiPackage(apiPackage, main);

		packageDao.attachAndSave(apiPackage);
		assertTrue("session must contains apiPackage!", session.contains(apiPackage));
		this.sessionManager.commitTransaction();

		return this.getProxyById(apiPackage.getId());
	}

	// -------------------------------------------- private methods
	 ApiPackage getProxyById(Long id) {

		this.sessionManager.beginTransaction();
		ApiPackage apiPackage = packageDao.getById(id, false);
		this.sessionManager.commitTransaction();

		return apiPackage;
	}

	private ApiPackage buildApiPackage(ApiPackage parent, String longName, Long siteId) {

		this.manageCountPackages(siteId, true);
		ApiPackage apiPackage = new ApiPackage(parent, longName);
		return apiPackage;
	}

	private int getCount(Long siteId) {
		Integer countForSite = MAP_SITE2COUNT.get(siteId);
		return countForSite == null ? 0 : countForSite;
	}

	private void manageCountPackages(Long siteId, boolean add) {

		int countForSite = this.getCount(siteId);
		if (add) {
			PACK_COUNT++;
			countForSite++;
		} else {
			PACK_COUNT--;
			countForSite--;
		}
		MAP_SITE2COUNT.put(siteId, countForSite);
	}

}
