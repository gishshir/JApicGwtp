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
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.persistence.criteria.CriteriaBuilder;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao.SiteCriteria;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdUtils;

/**
 *
 * @author sylvie
 */
@Component
public class SiteDaoTest extends AbstractDaoTestContext {

	public static int SITE_COUNT = 0;
	private final static Map<Long, Integer> MAP_DOMAIN2SITE = new HashMap<>();

	@Autowired
	private ISiteDao siteDao;
	@Autowired
	private IApiDomainDao domainDao;
	@Autowired
	private ApiDomainDaoTest domainDaoTest;

	private static Long domainId1;

	@Before
	public void init() {

		if (domainId1 == null) {
			ApiDomain domain1 = domainDaoTest.createApiDomain("SiteDaoDomain1", "Spring1 framework");
			domainId1 = domain1.getId();
		}
	}

	@Test
	public void testCreateSite() {
		this.createSite(domainId1, "http://org.spring.xx/4.2.5", "Spring API", "4.2.5");
	}

	@Test
	public void testUpdateSite() {
		Site site = this.createSite(domainId1, "http://org.spring.xx/8.6.4", "Spring API", "8.6.4");

		this.sessionManager.beginTransaction();
		this.siteDao.attachAndUpdate(site);

		site.setLastScan(Clock.systemDefaultZone().instant());
		site.setScanStatus(ScanStatus.Done);
		this.sessionManager.commitTransaction();

		this.sessionManager.beginTransaction();
		Site savedSite = this.siteDao.getById(site.getId(), true);
		assertNotNull("savedSite cannot be null!", savedSite);
		assertEquals("Wrong scan status", ScanStatus.Done, savedSite.getScanStatus());
		assertNotNull("lastScan cannot be null!", site.getLastScan());

		ZonedDateTime dt = ZonedDateTime.ofInstant(site.getLastScan(), ZoneId.systemDefault());
		LOG.info("lastScan: " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(dt));

		List<Site> listSites = this.siteDao.listAll();
		assertNotNull("listSites cannot be null!", listSites);

		this.sessionManager.commitTransaction();

	}

	@Test
	public void testCreateSiteFromDomain() {

		LOG.info("testCreateSiteFromDomain()");

		LOG.info("create site from domain...");
		Session session = this.sessionManager.beginTransaction();

		Site site = this.buildSite("http://org.spring.xx/2.0.1", "Spring API", "2.0.1", domainId1);

		// le domain est dans la session
		ApiDomain domain = domainDao.getById(domainId1, true);
		domain.addSite(site);
		// le site n'est pas dans la session
		assertFalse("session cannot contains site!", session.contains(site));
		// le site est sauvegardé automatiquement lors de la fermeture de
		// session
		this.sessionManager.commitTransaction();

		LOG.info("verify site from domain...");
		this.sessionManager.beginTransaction();
		Site siteProxy = siteDao.getById(site.getId(), false);
		assertNotNull("siteProxy cannot be null!", siteProxy);
		this.sessionManager.commitTransaction();

	}

	@Test
	public void testListByDomain() {

		ApiDomain domain2 = domainDaoTest.createApiDomain("SiteDaoDomain2", "Spring2 framework");

		this.sessionManager.beginTransaction();

		this.domainDao.attachUnmodifiedItem(domain2);
		List<Site> list = this.siteDao.listByDomain(domain2);
		assertNotNull("list of sites cannot be null!", list);
		assertEquals("wrong size of list!", this.getCount(domain2.getId()), list.size());

		this.sessionManager.commitTransaction();

		this.createSite(domainId1, "http://org.spring.yy/4.4.4", "Spring API", "4.4.4");

		this.createSite(domain2.getId(), "http://org.spring.zz/2.2.2", "Spring API", "2.2.2");
		this.createSite(domain2.getId(), "http://org.spring.zz/3.3.3", "Spring API", "3.3.3");

		this.sessionManager.beginTransaction();
		this.domainDao.attachUnmodifiedItem(domain2);

		// list by domain
		list = this.siteDao.listByDomain(domain2);
		assertNotNull("list of sites cannot be null!", list);
		assertEquals("wrong size of list!", this.getCount(domain2.getId()), list.size());

		// list available by domain
		List<Site> listSiteAvailable = this.siteDao.listByDomainAndAvailable(domain2);
		assertNotNull("list of sites cannot be null!", listSiteAvailable);
		assertEquals("wrong size of list!", 0, listSiteAvailable.size());

		// list available all domains
		List<Site> listAllAvailable = this.siteDao.listSiteAvailables();
		assertNotNull("list of sites cannot be null!", listAllAvailable);
		assertEquals("wrong size of list!", 0, listAllAvailable.size());
		this.sessionManager.commitTransaction();

	}

	@Test
	public void testListWithCriteria() {

		ApiDomain domain3 = domainDaoTest.createApiDomain("SiteDaoDomain3", "Spring3 framework");

		this.createSite(domain3.getId(), "http://org.spring.uu/2.2.2", "Spring3 API3", "2.2.2");
		this.createSite(domain3.getId(), "http://org.spring.uu/3.3.3", "Spring3 API3", "3.3.3");
		this.createSite(domain3.getId(), "http://org.spring.uu/4.4.4", "Spring3 XXX", "4.4.4");

		this.sessionManager.beginTransaction();

		// all
		List<Site> list = this.siteDao.listWithCriteria(null);
		assertNotNull("list of sites cannot be null!", list);
		assertEquals("wrong size of list!", 3, list.size());

		// list available
		list = this.siteDao.listWithCriteria(new SiteCriteria(true));
		assertNotNull("list of sites cannot be null!", list);
		assertEquals("wrong size of list!", 0, list.size());

		// list available contains name
		list = this.siteDao.listWithCriteria(new SiteCriteria(true, "Spring3"));
		assertNotNull("list of sites cannot be null!", list);
		assertEquals("wrong size of list!", 0, list.size());

		// list contains name
		list = this.siteDao.listWithCriteria(new SiteCriteria(false, "Spring3"));
		assertNotNull("list of sites cannot be null!", list);
		assertEquals("wrong size of list!", 3, list.size());

		list = this.siteDao.listWithCriteria(new SiteCriteria(false, "API3"));
		assertNotNull("list of sites cannot be null!", list);
		assertEquals("wrong size of list!", 2, list.size());

		// list available contains name and version
		list = this.siteDao.listWithCriteria(new SiteCriteria(true, "API", "3.3.3"));
		assertNotNull("list of sites cannot be null!", list);
		assertEquals("wrong size of list!", 0, list.size());

		// list contains name and version
		list = this.siteDao.listWithCriteria(new SiteCriteria(false, "API", "3.3.3"));
		assertNotNull("list of sites cannot be null!", list);
		assertEquals("wrong size of list!", 1, list.size());
		this.sessionManager.commitTransaction();

	}

	@Test
	public void testGetById() {

		this.sessionManager.beginTransaction();
		ApiDomain domain = domainDao.getById(domainId1, true);

		Site site1 = this.buildSite("http://org.spring.xx/2.1/", "Spring API", "2.1", domainId1);
		domain.addSite(site1);

		siteDao.attachAndSave(site1);

		Site siteById = siteDao.getById(site1.getId(), true);
		Long id = siteById.getId();
		assertNotNull("siteById cannot be null!", siteById);
		assertEquals("siteById must be site1", site1, siteById);
		this.sessionManager.commitTransaction();

		Session session2 = this.sessionManager.beginTransaction();
		// get a proxy
		Site siteProxy = siteDao.getById(id, false);
		assertNotNull("siteProxy cannot be null!", siteProxy);
		assertTrue("session must contains siteProxy!", session2.contains(siteProxy));

		Site siteBdd = siteDao.getById(id, true);
		assertNotNull("siteBdd cannot be null!", siteBdd);
		assertTrue("session must contains siteBdd!", session2.contains(siteBdd));

		this.sessionManager.commitTransaction();

	}

	@Test
	public void testGetByNaturalId() throws MalformedURLException {

		this.sessionManager.beginTransaction();
		ApiDomain domain = domainDao.getById(domainId1, true);

		String url = "http://org.spring.xx/6.1/";
		Resource resource = new UrlResource(url);
		Site site1 = this.buildSite(url, "Spring API", "6.1", domainId1);
		domain.addSite(site1);

		siteDao.attachAndSave(site1);

		Site siteByNatId = siteDao.getByNaturalId(resource, true);
		assertNotNull("siteByNatId cannot be null!", siteByNatId);
		assertEquals("site1 must be siteByNatId", site1, siteByNatId);
		this.sessionManager.commitTransaction();

		Session session2 = this.sessionManager.beginTransaction();
		// get a proxy
		Site siteProxy = siteDao.getByNaturalId(resource, false);
		assertNotNull("siteProxy cannot be null!", siteProxy);
		assertTrue("session must contains siteProxy!", session2.contains(siteProxy));

		Site siteBdd = siteDao.getByNaturalId(resource, true);
		assertNotNull("siteBdd cannot be null!", siteBdd);
		assertTrue("session must contains siteBdd!", session2.contains(siteBdd));

		this.sessionManager.commitTransaction();

	}

	@Test
	public void testDeleteSite() throws MalformedURLException {

		LOG.info("testDeleteSite()");

		LOG.info("create site...");
		Site site = this.createSite(domainId1, "http://org.spring.xx/2.5/", "Spring API", "2.5");

		LOG.info("delete detached site...");
		Session session2 = this.sessionManager.beginTransaction();
		assertFalse("session cannot contains site!", session2.contains(site));
		site = this.siteDao.getById(site.getId(), false);
		siteDao.delete(site);
		this.sessionManager.commitTransaction();

		this.manageCountSites(domainId1, false);

		LOG.info("verify site null...");
		Site siteProxy = this.getProxyById(site.getId());
		assertNull("siteProxy must be null!", siteProxy);
	}

	@Test
	public void testDeleteSiteFromDomain() throws MalformedURLException {

		LOG.info("testDeleteSiteFromDomain()");

		LOG.info("create site...");
		String url = "http://org.spring.xx/1.1/";
		Site site = this.createSite(domainId1, url, "Spring API", "1.1");

		LOG.info("delete site from domain...");
		// site detached >> then delete (without reattach it)
		this.sessionManager.beginTransaction();
		ApiDomain domain = domainDao.getById(domainId1, true);

		// on cherche le site dans la liste des sites du domain et on l'enleve
		// de la liste.
		Site siteInList = domain.getListSites().stream().filter(s -> Objects.equals(s.getId(), site.getId()))
				.findFirst().get();
		assertNotNull("siteInList cannot be null!", siteInList);

		domain.removeSite(siteInList);
		this.sessionManager.commitTransaction();

		this.manageCountSites(domainId1, false);

		LOG.info("verify site null...");
		this.sessionManager.beginTransaction();
		// get a proxy
		Site siteProxy = siteDao.getById(site.getId(), false);
		assertNull("siteProxy must be null!", siteProxy);

		this.sessionManager.commitTransaction();
	}

	// ----------------------------------------------- package methods
	/**
	 * Creation en BDD d'un Site dans un Domain existant
	 * 
	 * @param domainId
	 * @param url
	 * @param name
	 * @param version
	 * @return
	 */
	public Site createSite(Long domainId, String url, String name, String version) {
		return this.createSite(domainId, url, null, name, version);
	}

	public Site createSite(Long domainId, String url, String scanUrl, String name, String version) {

		Session session = this.sessionManager.getCurrentSession();
		session.getTransaction().begin();

		Site site = this.buildSite(url, scanUrl, name, version, domainId);
		assertFalse("session cannot contains site!", session.contains(site));

		ApiDomain domain = domainDao.getById(domainId, true);
		domain.addSite(site);
		assertTrue("id cannot be defined!", VoIdUtils.isIdUndefined(site.getId()));

		siteDao.attachAndSave(site);
		assertTrue("session must contains site!", session.contains(site));
		assertFalse("id must be defined!", VoIdUtils.isIdUndefined(site.getId()));
		session.getTransaction().commit();

		return this.getProxyById(site.getId());
	}

	// ---------------------------------------------- private methods
	Site getProxyById(Long id) {

		this.sessionManager.beginTransaction();
		Site site = siteDao.getById(id, false);
		this.sessionManager.commitTransaction();

		return site;
	}

	private Site buildSite(String url, String name, String version, Long domainId) {
		return this.buildSite(url, null, name, version, domainId);
	}

	private Site buildSite(String url, String scanUrl, String name, String version, Long domainId) {
		this.manageCountSites(domainId, true);
		Site site = null;
		try {
			site = new Site(new UrlResource(url));
			if (Objects.nonNull(scanUrl)) {
				site.setScanPage(new UrlResource(scanUrl));
			}
			site.setName(name);
			site.setApiVersion(version);

		} catch (MalformedURLException ex) {
			Logger.getLogger(SiteDaoTest.class.getName()).severe(ex.toString());
		}
		return site;
	}

	public int getCount(Long domainId) {

		Integer countForDomain = MAP_DOMAIN2SITE.get(domainId);
		return countForDomain == null ? 0 : countForDomain;
	}

	public void manageCountSites(Long domainId, boolean add) {

		int countForDomain = getCount(domainId);
		if (add) {
			SITE_COUNT++;
			countForDomain = countForDomain + 1;
		} else {
			SITE_COUNT--;
			countForDomain = countForDomain - 1;
		}
		MAP_DOMAIN2SITE.put(domainId, countForDomain);
	}

}
