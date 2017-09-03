package fr.tsadeo.app.japicgwtp.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.tsadeo.app.japicgwtp.server.AbstractDaoTestContext;
import fr.tsadeo.app.japicgwtp.server.dao.ApiDomainDaoTest;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao;
import fr.tsadeo.app.japicgwtp.server.dao.SiteDaoTest;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.vo.IVoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDataTypeForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSimpleListItems;

public class ApiDataTypeServiceTest extends AbstractDaoTestContext {

	private static final String MAIN_HIBERNATE = "http://docs.jboss.org/hibernate/orm/3.2/api/";
	private static final String MAIN_JAVA_8 = "http://docs.oracle.com/javase/8/docs/api/";
	private static final String MAIN_JUNIT = "http://junit.sourceforge.net/javadoc/";
	private static final String INDEX_OK = "allclasses-frame.html";

	@Autowired
	private IScanService scanService;
	@Autowired
	private IApiDataTypeService service;

	@Autowired
	private ISiteDao siteDao;

	@Autowired
	private ApiDomainDaoTest domainDaoTest;

	@Autowired
	private SiteDaoTest siteDaoTest;

	private static Long domainId1;
	private static Long domainId2;

	private static Long siteIdJUnit;
	private static Long siteIdJava8;
	private static Long siteIdHibernate;
	
	
	

	@Before
	public void init() {

		if (domainId1 == null) {
			ApiDomain domain1 = domainDaoTest.createApiDomain("ScanSiteService1", "ScanSiteService1 framework");
			domainId1 = domain1.getId();

			ApiDomain domain2 = domainDaoTest.createApiDomain("ScanSiteService2", "ScanSiteService2 framework");
			domainId2 = domain2.getId();

			Site siteJUnit = siteDaoTest.createSite(domainId1, MAIN_JUNIT,  "JUnit API", "4.0");
			siteIdJUnit = siteJUnit.getId();

			Site siteSfl4j = siteDaoTest.createSite(domainId1, MAIN_JAVA_8,  "JAVA API", "1.8.0");
			siteIdJava8 = siteSfl4j.getId();

			Site siteHibernate = siteDaoTest.createSite(domainId2, MAIN_HIBERNATE,  "Hibernate API",
					"3.2");
			siteIdHibernate = siteHibernate.getId();

		}
	}

	@Test
	public void testGetAllDataType() throws Exception {

		this.verifyScan(siteIdJUnit);
		
		VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> result = service.searchDataTypes(new VoSearchCriteria());

		assertNotNull("list of datatype cannot be null!", result);
		assertFalse("list of datatype cannot be empty!", result.isEmpty());

		IntStream.range(0, result.size()).forEachOrdered(index -> this.logAndAssert(result.get(index), index));
	}

	@Test
	public void testListDataTypeBySite() throws Exception {

		this.verifyScan(siteIdJUnit);
		this.verifyScan(siteIdHibernate);

		VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> listForJUnit = service.searchDataTypes(new VoSearchCriteria(domainId1, siteIdJUnit, "Is"));

		assertNotNull("list of datatype cannot be null!", listForJUnit);
		assertFalse("list of datatype cannot be empty!", listForJUnit.isEmpty());

		Site siteJUnit = this.getSite(siteIdJUnit);
		IntStream.range(0, listForJUnit.size()).forEachOrdered(index -> this.logAndAssert(listForJUnit.get(index), index, siteJUnit));

		
		final VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> listForHibernate = service.searchDataTypes(
				new VoSearchCriteria(domainId2, siteIdHibernate, "Criter"));

		assertNotNull("list of datatype cannot be null!", listForHibernate);
		assertFalse("list of datatype cannot be empty!", listForHibernate.isEmpty());

	    Site siteHibernate = this.getSite(siteIdHibernate);
		IntStream.range(0, listForHibernate.size()).forEachOrdered(index -> this.logAndAssert(listForHibernate.get(index), index, siteHibernate));
		assertEquals("wrong value for search", "Criter", listForHibernate.getSearch());
		
		final VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> listForHibernate2 = service.searchDataTypes(new VoSearchCriteria(domainId2, siteIdHibernate, "Criter Hiber"));

		assertNotNull("list of datatype cannot be null!", listForHibernate2);
		assertFalse("list of datatype cannot be empty!", listForHibernate2.isEmpty());
		
		assertEquals("wrong value for search", "Criter", listForHibernate2.getSearch());

		final VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> listForHibernate3 = service.searchDataTypes(new VoSearchCriteria(domainId2, siteIdHibernate, "TOTO Hiber"));

		assertNotNull("list of datatype cannot be null!", listForHibernate3);
		assertTrue("list of datatype must be empty!", listForHibernate3.isEmpty());

	}
	
	@Test
	public void testSiteJava8() throws Exception {
		this.verifyScan(siteIdJava8);
	}
	
	@Test
	public void testListDataTypeForDomain() throws Exception {
		
		//domain1
		this.verifyScan(siteIdJUnit);
		this.verifyScan(siteIdJava8);
		//domain2
		this.verifyScan(siteIdHibernate);
		
		VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> listForDomain1 = service.searchDataTypes(new VoSearchCriteria(domainId1, IVoId.ID_UNDEFINED, "Is"));

		assertNotNull("list of datatype cannot be null!", listForDomain1);
		assertFalse("list of datatype cannot be empty!", listForDomain1.isEmpty());
		
		final List<String> listSiteNames1 = new ArrayList<>();
		listSiteNames1.add(this.getSite(siteIdJUnit).getName());
		listSiteNames1.add(this.getSite(siteIdJava8).getName());
		IntStream.range(0, listForDomain1.size())
		       .forEachOrdered(index -> this.logAndAssert(listForDomain1.get(index), index, listSiteNames1));

		
		VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> listForDomain2 = service.searchDataTypes(new VoSearchCriteria(domainId2, IVoId.ID_UNDEFINED, "Is"));

		assertNotNull("list of datatype cannot be null!", listForDomain2);
		assertFalse("list of datatype cannot be empty!", listForDomain2.isEmpty());
		
		final List<String> listSiteNames2 = new ArrayList<>();
		listSiteNames2.add(this.getSite(siteIdHibernate).getName());
		IntStream.range(0, listForDomain2.size())
		       .forEachOrdered(index -> this.logAndAssert(listForDomain2.get(index), index, listSiteNames2));

	}

	// ------------------------------------------ private methods
	private void logAndAssert(VoDataTypeForGrid vo, int index) {
		this.logAndAssert(vo, index, null, null);
	}

	private void logAndAssert(VoDataTypeForGrid vo, int index, Site site) {
		this.logAndAssert(vo, index, site, null);
	}
	private void logAndAssert(VoDataTypeForGrid vo, int index,  List<String> listSitesNames) {
		this.logAndAssert(vo, index, null, listSitesNames);
	}
	private void logAndAssert(VoDataTypeForGrid vo, int index, Site site, List<String> listSitesNames) {

		assertNotNull("dataType cannot be null!", vo);
		this.logWithIndex(vo.toString(), index);

		if (Objects.nonNull(site)) {
			assertEquals("Wrong siteName!", site.getName() + " " + site.getApiVersion(), vo.getSiteName());
		}
		if(Objects.nonNull(listSitesNames)) {
			
			if (!CollectUtils.isNullOrEmpty(listSitesNames)) {
				assertTrue("wrong sitenames", listSitesNames.stream()
				         .anyMatch(n -> n.equals(vo.getSiteName())));
				         
			}
		}
	}



	private void verifyScan(long siteId) {

		this.sessionManager.beginTransaction();
		Site site = this.siteDao.getById(siteId, true);
		if (site.getScanStatus() == ScanStatus.Done) {
			return;
		}
		this.sessionManager.commitTransaction();

		this.scanService.scanSite(siteId);
	}
}
