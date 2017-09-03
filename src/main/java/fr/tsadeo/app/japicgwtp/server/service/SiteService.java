/*
 * Service transactionnel pour la gestion des web Sites.
 */
package fr.tsadeo.app.japicgwtp.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.AppTransactional;
import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.TransactionMode;
import fr.tsadeo.app.japicgwtp.server.dao.IApiDomainDao;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao.SiteCriteria;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.server.domain.converter.IResourceConverter;
import fr.tsadeo.app.japicgwtp.server.util.AbstractValidator;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;
import fr.tsadeo.app.japicgwtp.server.util.ModelUtils;
import fr.tsadeo.app.japicgwtp.server.util.VoUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.IVo;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdUtils;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;
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
@Service
public class SiteService extends AbstractService implements ISiteService {

	private final static Log LOG = LogFactory.getLog(SiteService.class);
	private static final String SEARCH_ALL = "*";

	@Autowired
	private IApiDomainDao domainDao;
	@Autowired
	private ISiteDao siteDao;

	@Autowired
	private IResourceConverter resourceConverter;

	@Autowired
	private SiteValidator siteValidator;

	// --------------------------------- implementing ILoggable
	@Override
	public Log getLog() {
		return LOG;
	}

	// -------------------------------- implementing ISiteService
	@AppTransactional(TransactionMode.manual)
	@Override
	public void deleteAllPackages(long siteId) {

		Site site = this.siteDao.getById(siteId, true);
		if (Objects.nonNull(site)) {

			site.getListApiPackages().stream().forEach(p -> site.removeApiPackage(p));
		}

	}

	@Override
	@AppTransactional(TransactionMode.readonly)
	public List<VoIdName> listIdNameAvailableSiteByDomain(long domainId) throws JApicException {

		return ModelUtils.getListVoIdName(this._listSites(domainId, true));

	}

	@Override
	@AppTransactional(TransactionMode.readonly)
	public VoListItems<VoSiteForGrid, VoSiteProtection> listSitesByDomain(long domainId, boolean availableOnly,
			boolean atLeastManager) throws JApicException {

		VoListItems<VoSiteForGrid, VoSiteProtection> voResult = new VoListItems<>();
		voResult.setProtection(this.buildProtection(domainId, atLeastManager, null));

		ApiDomain domain = this.domainDao.getById(domainId, false);
		final List<Site> listSites = this._listSites(domainId, availableOnly);

		if (listSites != null) {

			voResult.setListItems(listSites.stream().map(site -> {
				VoSiteForGrid voSiteForGrid = this.buildVoSiteForGrid(site, null);
				if (voSiteForGrid.getScanStatus() == ScanStatus.Scanning) {
					voSiteForGrid.setProtection(buildProtection(domainId, atLeastManager, ScanStatus.Scanning));
				}
				return voSiteForGrid;
			}).collect(Collectors.toList()));
		}

		return voResult;
	}

	@AppTransactional(TransactionMode.readonly)
	@Override
	public VoSearchResultDatas<VoSiteForGrid, VoSiteProtection> searchSites(VoSearchCriteria criteria,
			boolean atLeastManager) throws JApicException {

		VoSearchResultDatas<VoSiteForGrid, VoSiteProtection> voResult = new VoSearchResultDatas<>();
		if (Objects.isNull(criteria)) {
			return voResult;
		}
		voResult.setProtection(this.buildProtection(IVo.ID_UNDEFINED, atLeastManager, null));

		boolean all = SEARCH_ALL.equals(criteria.getSearch());
		boolean byId = criteria.getSiteId() != VoId.ID_UNDEFINED;

		SiteCriteria siteCriteria = this.buildSiteCriteria(criteria.getSearch(), false);
		Site siteById = byId ? this.siteDao.getById(criteria.getSiteId(), true) : null;
		final List<Site> listSites = byId ? CollectUtils.buildListWithOneItem(siteById)
				: (all ? this.siteDao.listAll() : this.siteDao.listWithCriteria(siteCriteria));
		voResult.setSearch(Objects.isNull(siteCriteria) || all ? null : siteCriteria.getSiteName().trim());

		if (listSites != null) {

			voResult.setListItems(listSites.stream().map(site -> {
				VoSiteForGrid voSiteForGrid = this.buildVoSiteForGrid(site, voResult.getSearch());
				if (voSiteForGrid.getScanStatus() == ScanStatus.Scanning) {
					voSiteForGrid.setProtection(buildProtection(IVo.ID_UNDEFINED, atLeastManager, ScanStatus.Scanning));
				} else {
					voSiteForGrid.setProtection(voResult.getProtection());
				}
				return voSiteForGrid;
			}).collect(Collectors.toList()));
		}

		return voResult;
	}

	@AppTransactional(TransactionMode.auto)
	@Override
	public void updateStatus(long siteId, UrlState urlState, ScanStatus scanStatus) {

		Site site = this.siteDao.getById(siteId, true);
		if (Objects.nonNull(site)) {
			boolean changeUrlState = Objects.nonNull(urlState) && site.getUrlState() != urlState;
			boolean changeScanStatus = Objects.nonNull(scanStatus) && site.getScanStatus() != scanStatus;

			if (changeScanStatus) {
				site.setScanStatus(scanStatus);
			}
			if (changeUrlState) {
				site.setUrlState(urlState);
			}
		}
	}

	// -------------------------------- implementing IItemService
	@AppTransactional(TransactionMode.auto)
	@Override
	public void verifyDefaultItems() throws JApicException {

		final String MAIN_HIBERNATE = "http://docs.jboss.org/hibernate/orm/3.2/api/";
		final String MAIN_JAVA_8 = "http://docs.oracle.com/javase/8/docs/api/";
		final String MAIN_JUNIT = "http://junit.sourceforge.net/javadoc/";
		final String MAIN_JSOUP = "https://jsoup.org/apidocs/";
		final String MAIN_SPRING = "http://docs.spring.io/spring/docs/current/javadoc-api/";
		final String MAIN_SLF4J = "http://www.slf4j.org/api/";
		final String MAIN_ASPECTJ = "http://www.eclipse.org/aspectj/doc/released/runtime-api/";
		final String MAIN_DOM4J = "http://dom4j.sourceforge.net/dom4j-1.6.1/apidocs/";
		final String MAIN_GWT = "http://www.gwtproject.org/javadoc/latest/";
		final String MAIN_GWTP = "http://arcbees.github.io/GWTP/javadoc/apidocs/";

		if (this.siteDao.listAll().isEmpty()) {

			ApiDomain domainTest = this.domainDao.getByNaturalId(ApiDomainService.DEFAULT_DOMAIN_NAME, false);
			if (domainTest != null) {

				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_HIBERNATE), "Hibernate", "5.1"));
				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_JAVA_8), "Java 8", "1.8"));
				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_JUNIT), "JUnit 4", "4.0"));
				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_JSOUP), "JSoup", "1.9"));
				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_SPRING), "Spring", "3.6"));
				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_SLF4J), "Slf4j", "1.7"));
				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_DOM4J), "Dom4j", "1.6.1"));
				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_ASPECTJ), "AspectJ", "1.8.9"));
				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_GWT), "GWT", "1.7"));
				domainTest.addSite(new Site(this.resourceConverter.toResource(MAIN_GWTP), "GWTP", "1.4"));
			}
		}
	}

	@Override
	@AppTransactional(TransactionMode.readonly)
	public VoSiteForEdit getItemForEdit(long siteId, boolean create, boolean atLeastManager) throws JApicException {
		VoSiteForEdit voSite;
		if (create) {
			voSite = new VoSiteForEdit();
			ApiDomain defaultDomain = this.domainDao.getByNaturalId(IApiDomainService.DEFAULT_DOMAIN_NAME, false);
			voSite.setDomainId(defaultDomain.getId());
		} else {
			Site site = this.siteDao.getById(siteId, true);
			if (Objects.isNull(site)) {
				throw new JApicException("Site id " + siteId + " doesn't exists!");
			}
			voSite = this.buildVoSiteForEdit(site);
		}

		voSite.setProtection(this.buildProtection(voSite.getDomainId(), atLeastManager, voSite.getScanStatus()));
		return voSite;
	}

	@Override
	@AppTransactional(TransactionMode.auto)
	public boolean deleteItem(long siteId) throws JApicException {

		Site site = this.siteDao.getById(siteId, false);
		if (Objects.isNull(site)) {
			LOG.warn("Site id " + siteId + " does'nt exist!");
			return false;
		}

		this.siteDao.delete(site);
		return true;
	}

	@Override
	@AppTransactional(TransactionMode.readonly)
	public VoDatasValidation validateItem(VoSiteForEdit itemForEdit) throws JApicException {

		VoSiteForEdit siteToUpdate = (VoSiteForEdit) itemForEdit;
		VoDatasValidation voValidation = this.siteValidator.validate(siteToUpdate, this.resourceConverter);
		if (voValidation.isValid()) {
			Site site = this.siteDao.getById(siteToUpdate.getId(), false);
			voValidation = this.siteValidator.validateIds(site, siteToUpdate);
		}

		return voValidation;
	}

	@Override
	@AppTransactional(TransactionMode.auto)
	public VoSiteForEdit createOrUpdateItem(VoSiteForEdit siteToUpdate, boolean createItem) {

		Site site;
		if (createItem) {
			ApiDomain domain = this.domainDao.getById(siteToUpdate.getDomainId(), false);
			site = new Site();

			// le site sera sauvegardé lors du commit
			domain.addSite(site);
		} else {
			site = this.siteDao.getById(siteToUpdate.getId(), true);

		}
		this.updateSiteWithVo(site, siteToUpdate);
		if (createItem) {
			this.siteDao.attachAndSave(site);
		} else {
			this.siteDao.attachAndUpdate(site);
		}

		VoSiteForEdit voSite = this.buildVoSiteForEdit(site);
		voSite.setProtection(siteToUpdate.getProtection());
		return voSite;
	}

	// ----------------------------------- private methods

	private VoSiteForEdit buildVoSiteForEdit(Site site) {

		VoSiteForEdit voSite = new VoSiteForEdit(site.getDomain().getId(), site.toVoIdName());

		voSite.setEnabled(site.isEnabled());

		voSite.setLastScan(Objects.isNull(site.getLastScan()) ? -1 : site.getLastScan().toEpochMilli());
		voSite.setScanStatus(site.getScanStatus());
		voSite.setUrl(this.resourceConverter.toUrl(site.getMainPage()));
		voSite.setScanUrl(this.resourceConverter.toUrl(site.getScanPage()));
		voSite.setUrlState(site.getUrlState());
		voSite.setVersion(site.getApiVersion());

		return voSite;
	}

	private VoSiteForGrid buildVoSiteForGrid(Site site, String search) {

		VoSiteForGrid vo = new VoSiteForGrid(site.toVoIdName());

		vo.setEnabled(site.isEnabled());

		vo.setLastScan(Objects.isNull(site.getLastScan()) ? -1 : site.getLastScan().toEpochMilli());
		vo.setScanStatus(Objects.isNull(site.getScanStatus()) ? ScanStatus.New : site.getScanStatus());

		vo.setUrl(this.resourceConverter.toUrl(site.getMainPage()));
		vo.setUrlState(Objects.isNull(site.getUrlState()) ? UrlState.NoTested : site.getUrlState());
		vo.setVersion(site.getApiVersion());

		vo.setVoHighlightedName(VoUtils.buildVoHighlighText(search, vo.getName()));

		return vo;
	}

	private VoSiteProtection buildProtection(long domainId, boolean atLeastManager, ScanStatus scanStatus) {

		VoSiteProtection protection = new VoSiteProtection();
		protection.setCanCreate(atLeastManager && !VoIdUtils.isIdUndefined(domainId));
		protection.setCanDelete(atLeastManager);
		protection.setCanEdit(true);
		protection.setCanUpdate(atLeastManager);
		protection.setCanScan(Objects.isNull(scanStatus) || scanStatus != ScanStatus.Scanning);

		return protection;
	}

	private <T extends VoItemForEdit> void updateSiteWithVo(Site site, VoSiteForEdit voSite) {

		// cas update site
		if (!voSite.isUndefined()) {
			// si url ou index a changé alors reinitialiser le UrlState
			if (!StringUtils.equals(this.resourceConverter.toUrl(site.getMainPage()), voSite.getUrl())
					|| !StringUtils.equals(this.resourceConverter.toUrl(site.getScanPage()), voSite.getScanUrl())) {
				site.setUrlState(UrlState.NoTested);
				site.setScanStatus(ScanStatus.New);
			}
		}

		site.setName(voSite.getName());
		site.setApiVersion(voSite.getVersion());
		site.setEnabled(voSite.isEnabled());

		if (!voSite.getUrl().endsWith(LINK_SEPARATOR)) {
			voSite.setUrl(voSite.getUrl() + LINK_SEPARATOR);
		}
		site.setMainPage(this.resourceConverter.toResource(voSite.getUrl()));

		if (StringUtils.isNotEmpty(voSite.getScanUrl())) {
			if (!voSite.getScanUrl().endsWith(LINK_SEPARATOR)) {
				voSite.setScanUrl(voSite.getUrl() + LINK_SEPARATOR);
			}
			site.setScanPage(this.resourceConverter.toResource(voSite.getScanUrl()));
		} else {
			site.setScanPage(null);
		}

	}

	private List<Site> _listSites(long domainId, boolean availableOnly) throws JApicException {

		ApiDomain domain = this.domainDao.getById(domainId, false);
		List<Site> listSites = null;

		if (domain != null) {
			listSites = availableOnly ? siteDao.listByDomainAndAvailable(domain) : siteDao.listByDomain(domain);
		} else {
			listSites = availableOnly ? this.siteDao.listSiteAvailables() : this.siteDao.listAll();
		}
		return listSites;
	}

	/*
	 * si la recherche est composée d'un seul mot alors il s'agit du nom du site
	 * recherché Si la recherche est composée de 2 mots alors le deuxième est un
	 * filtre sur la version du site
	 */
	private SiteCriteria buildSiteCriteria(String search, boolean onlyAvalaible) {

		String[] splits = StringUtils.isEmpty(search) ? null : search.split(SPACE);

		SiteCriteria criteria = null;
		if (Objects.nonNull(splits)) {
			// cas nominal
			if (splits.length == 1) {
				criteria = new SiteCriteria(onlyAvalaible, search);
			} else if (splits.length == 2) {
				criteria = new SiteCriteria(onlyAvalaible, splits[0], splits[1]);
			} else {
				// not available
				criteria = new SiteCriteria(onlyAvalaible);
			}
		}

		return criteria;
	}

	// ==================================== INNER CLASS
	@Component
	private static class SiteValidator extends AbstractValidator {

		@Autowired
		private ISiteDao siteDao;

		private VoDatasValidation validate(VoSiteForEdit voSite, IResourceConverter converter) {

			VoDatasValidation voValidation = new VoDatasValidation();

			if (super.validateNotNull(voSite, "Site", voValidation)) {

				super.validateNotTransient(voSite.getDomainId(), "domain", voValidation);
				super.validateString(voSite.getName(), 3, "Site name", voValidation);

				super.validateUrl(voSite.getUrl(), "Site url", converter, voValidation);

				if (StringUtils.isNotEmpty(voSite.getScanUrl())) {
					super.validateUrl(voSite.getScanUrl(), "Site scan url", converter, voValidation);
				}

				super.validateString(voSite.getVersion(), 1, "Site version", voValidation);

				// verification d'unicite
				Site siteSameMainPage = this.siteDao.getByNaturalId(converter.toResource(voSite.getUrl()), false);
				super.validateUnicity(siteSameMainPage, voSite, "The url: " + voSite.getUrl(), voValidation);
			}
			return voValidation;
		}
	}

}
