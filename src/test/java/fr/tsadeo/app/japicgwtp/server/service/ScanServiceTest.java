package fr.tsadeo.app.japicgwtp.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;
import fr.tsadeo.app.japicgwtp.server.dao.ApiDomainDaoTest;
import fr.tsadeo.app.japicgwtp.server.dao.IApiPackageDao;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao;
import fr.tsadeo.app.japicgwtp.server.dao.SiteDaoTest;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDataType;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.ApiPackage;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.server.domain.converter.ResourceConverter;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;

public class ScanServiceTest extends AbstractDaoTestContext {

	private final static Log LOG = LogFactory.getLog(ScanServiceTest.class);

	private static final String MAIN_JSOUP_OK = "https://jsoup.org/apidocs/";
//	private static final String INDEX_OK = "allclasses-frame.html";

	private static final String MAIN_SPRING_OK = "http://docs.spring.io/spring/docs/current/javadoc-api/";

	private static final String MAIN_WRONG = "https://jsoup.org/xxxx/";
	private static final String INDEX_WRONG = "toto.html";
	
	private static final String MAIN_SLF4J_OK = "http://www.slf4j.org/api/";
	
	private static final String MAIN_GWT_OK = "http://www.gwtproject.org/javadoc/latest/";
	
	private static final String MAIN_JASPECT_OK = "http://www.eclipse.org/aspectj/doc/released/runtime-api/";
	
	private static final String MAIN_POWERMOCK_403= "http://www.javadoc.io/doc/org.powermock/powermock-api-mockito/1.6.2/";
	private static final String SCAN_URL_POWERMOCK_403= "http://static.javadoc.io/org.powermock/powermock-api-mockito/1.6.2/";
	
	private static final String MAIN_OPENCSV = "http://www.atetric.com/atetric/javadoc/net.sf.opencsv/opencsv/2.3/";
	
	@Autowired
	private IScanService service;
	@Autowired
	private ResourceConverter resourceConverter;

	@Autowired
	private ApiDomainDaoTest domainDaoTest;

	@Autowired
	private SiteDaoTest siteDaoTest;

	@Autowired
	private ISiteDao siteDao;
	@Autowired
	private IApiPackageDao packageDao;

	private static Long domainId1;
	private static Long domainId2;

	private static Long siteIdJSoup;
	private static Long siteIdSfl4j;
	private static Long siteIdSpring;
	private static Long siteIdGwt;
	private static Long siteIdJAspect;
	private static Long siteIdPowerMock;
	private static Long siteIdOpenCsv;

	@Before
	public void init() {

		if (domainId1 == null) {
			ApiDomain domain1 = domainDaoTest.createApiDomain("ScanSiteService1", "ScanSiteService1 framework");
			domainId1 = domain1.getId();

			ApiDomain domain2 = domainDaoTest.createApiDomain("ScanSiteService2", "ScanSiteService2 framework");
			domainId2 = domain2.getId();

			Site siteJSoup = siteDaoTest.createSite(domainId1, MAIN_JSOUP_OK,  "JSoup API 1.9.2", "1.9.2");
			siteIdJSoup = siteJSoup.getId();
			
			Site siteSfl4j = siteDaoTest.createSite(domainId1, MAIN_SLF4J_OK,  "Slf4j API 1.7.21", "1.7.21");
			siteIdSfl4j= siteSfl4j.getId();
			
			Site siteSpring = siteDaoTest.createSite(domainId2, MAIN_SPRING_OK,  "Spring API 4.2.6", "4.2.6");
			siteIdSpring = siteSpring.getId();
			
			Site siteGwt = siteDaoTest.createSite(domainId2, MAIN_GWT_OK,  "GWT API 1.7", "1.7");
			siteIdGwt = siteGwt.getId();
			
			Site siteJAspect= siteDaoTest.createSite(domainId2, MAIN_JASPECT_OK, "JAspect", "1.8.9");
			siteIdJAspect = siteJAspect.getId();
			
			Site sitePowerMock= siteDaoTest.createSite(domainId2, MAIN_POWERMOCK_403, SCAN_URL_POWERMOCK_403,
					"PowerMock", "1.6.2");
			siteIdPowerMock = sitePowerMock.getId();
			
			Site siteOpenCsv= siteDaoTest.createSite(domainId2, MAIN_OPENCSV, "OpenCsv", "2.3");
			siteIdOpenCsv = siteOpenCsv.getId();
			
		}
	}

	@Test
	public void testInjectionService() {

		assertNotNull("service cannot be null!", service);
	}

	@Test
	public void testDefineAndSaveUrlStateAlive() throws Exception {

		this.getAndAssertUrlState(siteIdJSoup, UrlState.NoTested);

		UrlState siteUrlState = service.defineAndSaveUrlState(siteIdJSoup);
		assertNotNull("urlState cannot be null!", siteUrlState);
		assertEquals("Wrong url state!", UrlState.Alive, siteUrlState);

		this.getAndAssertUrlState(siteIdJSoup, UrlState.Alive);

	}
	
	@Test
	public void testDefineAndSaveUrlStatePowerMock() throws Exception {

		this.getAndAssertUrlState(siteIdPowerMock, UrlState.NoTested);

		UrlState siteUrlState = service.defineAndSaveUrlState(siteIdPowerMock);
		assertNotNull("urlState cannot be null!", siteUrlState);
		assertEquals("Wrong url state!", UrlState.Alive, siteUrlState);

		this.getAndAssertUrlState(siteIdPowerMock, UrlState.Alive);

	}

	@Test
	public void testDefineAndSaveUrlStateAliveRestricted() throws Exception {

		this.getAndAssertUrlState(siteIdJAspect, UrlState.NoTested);

		UrlState siteUrlState = service.defineAndSaveUrlState(siteIdJAspect);
		assertNotNull("urlState cannot be null!", siteUrlState);
		assertEquals("Wrong url state!", UrlState.AliveRestricted, siteUrlState);

		this.getAndAssertUrlState(siteIdJAspect, UrlState.AliveRestricted);

	}

	@Test
	public void testDefineAndSaveUrlStateWrongMainPage() throws Exception {

		Site site2 = siteDaoTest.createSite(domainId2, MAIN_WRONG, "JSoup API 1.8.0", "1.8.0");

		this.getAndAssertUrlState(site2.getId(), UrlState.NoTested);

		UrlState siteUrlState = service.defineAndSaveUrlState(site2.getId());
		assertNotNull("urlState cannot be null!", siteUrlState);
		assertEquals("Wrong url state!", UrlState.PageNoFound, siteUrlState);

		this.getAndAssertUrlState(site2.getId(), UrlState.PageNoFound);

	}

	@Test
	public void testScanGWT() throws Exception {

		this._testScan(siteIdGwt, "org.google");

	}


	@Test
	public void testScanJsoup() throws Exception {

		this._testScan(siteIdJSoup, "org.jsoup");

	}

	@Test
	public void testScanSlf4j() throws Exception {

		
		this._testScan(siteIdSfl4j, "org");

	}
	
	@Test
	public void testScanSpring() throws Exception {

		
		this._testScan(siteIdSpring, "org");

	}

	@Test
	public void testScanPowerMock() throws Exception {

		
		this._testScan(siteIdPowerMock, "org.powermock.api");

	}
	

	@Test
	public void testScanOpenCsv() throws Exception {

		
		this._testScan(siteIdOpenCsv, "au.com.bytecode.opencsv");

	}
	// --------------------------------------------- private methods

	private void _testScan(long siteId, String mainPackageName) throws Exception {

		this.getAndAssertScanStatus(siteId, ScanStatus.New);

		boolean result = this.service.scanSite(siteId);
		this.getAndAssertScanStatus(siteId, ScanStatus.Done);

		this.sessionManager.beginTransaction();
		Site site = this.siteDao.getById(siteId, true);
		List<ApiPackage> listPackages = site.getListApiPackages();

		assertNotNull("list of packages cannot be null!", listPackages);
		assertFalse("list of packages cannot be empty!", listPackages.isEmpty());
		
		IntStream.range(0, listPackages.size())
		              .forEachOrdered(index -> this.logAndAssert(listPackages.get(index), index));
		
		ApiPackage mainPackage = site.getMainPackage();
		LOG.info("main package: " + mainPackage.toString());
		assertNotNull("main package cannot be null!", mainPackage);
		assertEquals("wrong name of main package!", mainPackageName, mainPackage.getLongName());
		
		this.sessionManager.commitTransaction();

	}
	
	private void logAndAssert(ApiPackage apiPackage, int index) {
		assertNotNull("package cannot be null!", apiPackage);
		this.logWithIndex(apiPackage.toString(), index);
		
		List<ApiDataType> listApiDataType = apiPackage.getListApiDataTypes();
		assertNotNull("list of ApiDataType cannot be null!", listApiDataType);
		IntStream.range(0, listApiDataType.size())
		          .forEachOrdered(j -> this.logAndAssert(listApiDataType.get(j), j));
		
		
	}
	
	private void logAndAssert(ApiDataType apiDataType, int index) {
		
		assertNotNull("dataType cannot be null!", apiDataType);
		this.logWithIndex(apiDataType.toString(), index);
	}
	

	private void getAndAssertScanStatus(long siteId, ScanStatus expected) {

		this.sessionManager.beginTransaction();
		Site site = this.siteDao.getById(siteId, true);
		assertEquals("Wrong scan status!", expected, site.getScanStatus());

		if (expected == ScanStatus.Done) {
			assertNotNull("site.lastscan cannot be null!", site.getLastScan());
		}

		this.sessionManager.commitTransaction();

	}

	private void getAndAssertUrlState(long siteId, UrlState expected) {

		this.sessionManager.beginTransaction();
		Site site = this.siteDao.getById(siteId, true);
		assertEquals("Wrong url state!", expected, site.getUrlState());
		this.sessionManager.commitTransaction();
	}

}
