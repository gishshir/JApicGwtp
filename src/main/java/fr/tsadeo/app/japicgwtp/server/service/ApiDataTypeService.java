package fr.tsadeo.app.japicgwtp.server.service;

import static fr.tsadeo.app.japicgwtp.server.service.ISiteService.LINK_CHAR_SLASH;
import static fr.tsadeo.app.japicgwtp.server.service.ISiteService.LINK_SEPARATOR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.AppTransactional;
import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.TransactionMode;
import fr.tsadeo.app.japicgwtp.server.dao.IApiDataTypeDao;
import fr.tsadeo.app.japicgwtp.server.dao.IApiDataTypeDao.DataTypeCriteria;
import fr.tsadeo.app.japicgwtp.server.dao.IApiDomainDao;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDataType;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.server.domain.converter.ResourceConverter;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;
import fr.tsadeo.app.japicgwtp.server.util.VoUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDataTypeForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdHighlighText;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdUtils;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;

@Service
public class ApiDataTypeService extends AbstractService implements IApiDataTypeService {

	private final static Log LOG = LogFactory.getLog(ApiDataTypeService.class);

	private final static String LINK_PREFIX = "http://";
	private final static String LINK_SUFFIX = ".html";

	private final static char LINK_CHAR_POINT = '.';

	@Autowired
	private IApiDataTypeDao dataTypeDao;
	@Autowired
	private ISiteDao siteDao;
	@Autowired
	private IApiDomainDao domainDao;
	@Autowired
	private ResourceConverter resourceConverter;

	// ---------------------------------------------- implementing ILoggable
	@Override
	public Log getLog() {
		return LOG;
	}

	// --------------------------------------- implementing IApiDataTypeService
	@AppTransactional(TransactionMode.readonly)
	@Override
	public VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> searchDataTypes(VoSearchCriteria criteria)
			throws JApicException {

		boolean allSites = VoIdUtils.isIdUndefined(criteria.getSiteId());
		boolean allDomains = (!allSites) ? false : VoIdUtils.isIdUndefined(criteria.getDomainId());

		if (allDomains) {
			return this.listAllDataType(criteria);
		} else {
			if (allSites) {
				return this.listDataTypeForDomain(criteria);
			} else {
				return this.listDataTypeForSite(criteria);
			}
		}
	}

	@AppTransactional(TransactionMode.readonly)
	@Override
	public VoSearchResultDatas<VoIdHighlighText, VoItemProtection> searchVoIdHighlighTexts(VoSearchCriteria criteria)
			throws JApicException {

		// TODO on ne traite pas pour l'instant le filtre sur domain ou site
		return this.listAllVoIdHighlighTexts(criteria);
	}

	// -------------------------------------- private methods
	private VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> listDataTypeForSite(VoSearchCriteria voCriteria)
			throws JApicException {

		long siteId = voCriteria.getSiteId();
		final Site site = this.siteDao.getById(siteId, false);
		if (Objects.isNull(site)) {
			throw new JApicException("The site " + siteId + " doesn't exists!");
		}
		final List<ApiDataType> list = this.dataTypeDao.listByAvailableSite(site,
				new DataTypeCriteria(voCriteria.getSearch()));

		VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> voResult = new VoSearchResultDatas<>();
		voResult.setSearch(voCriteria.getSearch().trim());
		if (CollectUtils.isNullOrEmpty(list)) {
			voResult.setListItems(new ArrayList<>(0));
			return voResult;
		}

		String siteUrl = this.resourceConverter
				.toUrl(Objects.nonNull(site.getScanPage()) ? site.getScanPage() : site.getMainPage());

		voResult.setListItems(list.stream()
				.map(apiDataType -> this.buildVoDataForGrid(apiDataType, site, voResult.getSearch(), siteUrl))
				.limit(voCriteria.getMaxCount()).collect(Collectors.toList()));

		return voResult;
	}

	private VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> listDataTypeForDomain(VoSearchCriteria voCriteria)
			throws JApicException {

		final long domainId = voCriteria.getDomainId();
		final ApiDomain domain = this.domainDao.getById(domainId, false);
		if (Objects.isNull(domain)) {
			throw new JApicException("The domain " + domainId + " doesn't exists!");
		}
		DataTypeCriteria dtCriteria = this.buildDataTypeCriteria(voCriteria.getSearch());
		final List<ApiDataType> list = this.dataTypeDao.listByDomainAndAvailableSites(domain, dtCriteria);

		VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> voResult = new VoSearchResultDatas<>();
		voResult.setSearch(Objects.isNull(dtCriteria) ? "" : dtCriteria.getDtName().trim());
		if (CollectUtils.isNullOrEmpty(list)) {
			voResult.setListItems(new ArrayList<>(0));
			return voResult;
		}

		voResult.setListItems(
				list.stream().map(apiDataType -> this.buildVoDataForGrid(apiDataType, voResult.getSearch()))
						.limit(voCriteria.getMaxCount()).collect(Collectors.toList()));

		return voResult;
	}

	private VoSearchResultDatas<VoIdHighlighText, VoItemProtection> listAllVoIdHighlighTexts(
			VoSearchCriteria voCriteria) throws JApicException {

		DataTypeCriteria dtCriteria = this.buildDataTypeCriteria(voCriteria.getSearch());
		if (dtCriteria.getDtName().length() < 3) {
			return null;
		}
		final List<VoIdName> list = this.dataTypeDao.listVoIdNameContainsNameForAvailableSites(dtCriteria);

		VoSearchResultDatas<VoIdHighlighText, VoItemProtection> voResult = new VoSearchResultDatas<>();
		voResult.setSearch(Objects.isNull(dtCriteria) ? "" : dtCriteria.getDtName().trim());
		if (CollectUtils.isNullOrEmpty(list)) {
			voResult.setListItems(new ArrayList<>(0));
			return voResult;
		}

		voResult.setListItems(
				list.stream().map(idAndName -> this.buildVoIdHighlighText(idAndName, voResult.getSearch()))
						.limit(voCriteria.getMaxCount()).collect(Collectors.toList()));

		return voResult;

	}

	private VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> listAllDataType(VoSearchCriteria voCriteria)
			throws JApicException {

		DataTypeCriteria dtCriteria = this.buildDataTypeCriteria(voCriteria.getSearch());
		if (dtCriteria.getDtName().length() < 3) {
			return null;
		}
		final List<ApiDataType> list = this.dataTypeDao.listAllContainsNameForAvailableSites(dtCriteria);

		VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> voResult = new VoSearchResultDatas<>();
		voResult.setSearch(Objects.isNull(dtCriteria) ? "" : dtCriteria.getDtName().trim());
		if (CollectUtils.isNullOrEmpty(list)) {
			voResult.setListItems(new ArrayList<>(0));
			return voResult;
		}

		voResult.setListItems(
				list.stream().map(apiDataType -> this.buildVoDataForGrid(apiDataType, voResult.getSearch()))
						.limit(voCriteria.getMaxCount()).collect(Collectors.toList()));

		return voResult;
	}

	private VoDataTypeForGrid buildVoDataForGrid(ApiDataType apiDataType, String search) {
		return this.buildVoDataForGrid(apiDataType, null, search, null);
	}

	private VoDataTypeForGrid buildVoDataForGrid(ApiDataType apiDataType, Site site, String search, String siteUrl) {

		VoDataTypeForGrid vo = new VoDataTypeForGrid(new VoIdName(apiDataType.getId(), apiDataType.getName()));
		vo.setType(apiDataType.getType());

		site = Objects.isNull(site) ? apiDataType.getApiPackage().getSite() : site;
		siteUrl = Objects.isNull(siteUrl) ? 
				this.resourceConverter.toUrl(Objects.nonNull(site.getScanPage())?site.getScanPage():site.getMainPage()) : siteUrl;

		vo.setSiteName(site.getName() + " " + site.getApiVersion());
		vo.setPackageName(apiDataType.getApiPackage().getLongName());
		vo.setLink(this.buildLink(apiDataType, siteUrl));
		vo.setIFrameAllowed(site.getUrlState() == UrlState.Alive);

		vo.setVoHighlightedName(VoUtils.buildVoHighlighText(search, vo.getName()));

		return vo;
	}

	private VoIdHighlighText buildVoIdHighlighText(VoIdName voId, String search) {

		VoIdHighlighText vo = new VoIdHighlighText(voId.getId());
		vo.setVoHighlighText(VoUtils.buildVoHighlighText(search, voId.getName()));

		return vo;

	}

	/*
	 * si la recherche est composée d'un seul mot alors il s'agit du nom du
	 * datatype recherché Si la recherche est composée de 2 mots alors le
	 * deuxième est un filtre sur le nom du site Si la recherche est composée de
	 * 3 mots alors le troisième est un filtre sur la version du site
	 */
	private DataTypeCriteria buildDataTypeCriteria(String search) {

		String[] splits = Objects.isNull(search) ? null : search.split(SPACE);

		DataTypeCriteria criteria = null;
		if (Objects.nonNull(splits)) {
			// cas nominal
			if (splits.length == 1) {
				criteria = new DataTypeCriteria(search);
			} else if (splits.length == 2) {
				criteria = new DataTypeCriteria(splits[0], splits[1]);
			} else if (splits.length == 3) {
				criteria = new DataTypeCriteria(splits[0], splits[1], splits[2]);
			} else {
				// not available
				criteria = new DataTypeCriteria(false);
			}
		}

		return criteria;
	}

	private String buildLink(ApiDataType apiDataType, String siteUrl) {

		String link = apiDataType.getApiPackage().getLongName().replace(LINK_CHAR_POINT, LINK_CHAR_SLASH);
		return siteUrl.concat(link).concat(LINK_SEPARATOR).concat(apiDataType.getName()).concat(LINK_SUFFIX);
	}

}
