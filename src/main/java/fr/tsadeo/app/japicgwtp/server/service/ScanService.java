package fr.tsadeo.app.japicgwtp.server.service;

import java.time.Clock;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.AppTransactional;
import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.TransactionMode;
import fr.tsadeo.app.japicgwtp.server.dao.IApiPackageDao;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDataType;
import fr.tsadeo.app.japicgwtp.server.domain.ApiPackage;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.server.domain.converter.ResourceConverter;
import fr.tsadeo.app.japicgwtp.server.manager.DaoSessionManager;
import fr.tsadeo.app.japicgwtp.server.manager.ScanManager;
import fr.tsadeo.app.japicgwtp.server.manager.URLManager;
import fr.tsadeo.app.japicgwtp.server.manager.scan.FileType;
import fr.tsadeo.app.japicgwtp.server.manager.scan.PathInfo;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.JavaType;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;

@Service
public class ScanService extends AbstractService implements IScanService {

	private final static Log LOG = LogFactory.getLog(ScanService.class);

	private final static String INDEX_NAME = "allclasses-frame.html";

	@Autowired
	private URLManager urlManager;
	@Autowired
	private ScanManager scanManager;

	@Autowired
	private ResourceConverter resourceConverter;

	@Autowired
	private ISiteDao siteDao;
	@Autowired
	private IApiPackageDao packageDao;

	@Autowired
	private DaoSessionManager sessionManager;

	private Set<Long> siteScanInProgress = Collections.synchronizedSet(new HashSet<>());

	// ------------------------------------ implementing ILoggable
	@Override
	public Log getLog() {
		return LOG;
	}

	// ------------------------------------ implementing IScanService
	@AppTransactional(TransactionMode.auto)
	@Override
	public UrlState defineAndSaveUrlState(long siteId) {

		Site site = this.siteDao.getById(siteId, true);
		if (Objects.isNull(site)) {
			return UrlState.Error;
		}

		UrlState scanUrlState = UrlState.NoTested;
		try {
			Resource mainPage = site.getMainPage();
			// url complementaire pour cas specifique où l'acces à la page d'index est different de l'url du site
			Resource scanPage = site.getScanPage();
			Resource indexPage = this.getResource(Objects.nonNull(scanPage)?scanPage:mainPage, INDEX_NAME);

			scanUrlState = urlManager.getUrlState(indexPage);

			if (!scanUrlState.isAlive()) {

				UrlState mainUrlState = urlManager.getUrlState(mainPage);
				if (mainUrlState.isAlive()) {
					scanUrlState = UrlState.IndexNoFound;
				} else {
					scanUrlState = mainUrlState;
				}
			}

			if (site.getUrlState() != scanUrlState) {
				site.setUrlState(scanUrlState);
			}

		} catch (Exception ex) {
			scanUrlState = UrlState.Error;
		}
		return scanUrlState;
	}

	// lance dans un thread separe
	@AppTransactional(TransactionMode.manual)
	@Override
	public boolean scanSite(long siteId) {

		if (this.isScanInProgress(siteId)) {
			return false;
		}
		final Site site = this.siteDao.getById(siteId, true);
		if (Objects.isNull(site)) {
			return false;
		}

		LOG.info("start scanning...");

		boolean success = false;
		try {

			this.siteScanInProgress.add(siteId);
			site.setScanStatus(ScanStatus.Scanning);

			// Lancement du scan proprement dit
			this.scanAndSave(site);

			LOG.debug("...END scanning");
			site.setScanStatus(ScanStatus.Done);
			site.setLastScan(Clock.systemUTC().instant());
			success = true;

		} catch (Exception e) {
			site.setScanStatus(ScanStatus.Error);
		} finally {
			this.siteScanInProgress.remove(siteId);
		}

		return success;
	}

	// ---------------------------------- private methods
	private boolean scanAndSave(Site site) throws JApicException {

		LOG.info("liste des types dans l'index....");
		Resource toScan = Objects.nonNull(site.getScanPage())?site.getScanPage():site.getMainPage();
		final List<PathInfo> listPathInfos = this.scanManager
				.getPathInfosFromIndex(this.getResource(toScan, INDEX_NAME));
		if (CollectUtils.isNullOrEmpty(listPathInfos)) {
			return false;
		}
		LOG.info("..." + listPathInfos.size() + " types.");

		// construire map [packageName / list of PathInfo]
		Map<String, List<PathInfo>> mapPackageNameToListPathInfo = listPathInfos.stream()
				.filter(t -> Objects.nonNull(t.getPackageName()))
				.collect(Collectors.groupingBy(t -> t.getPackageName()));
		if (mapPackageNameToListPathInfo.isEmpty()) {
			return false;
		}

		// liste des packageName répertories par la liste des PathInfo par ordre
		// alphabetique
		List<String> listOfOrderedPackageNames = mapPackageNameToListPathInfo.keySet().stream()
				.sorted((p1, p2) -> p1.compareTo(p2)).filter(Objects::nonNull).collect(Collectors.toList());
		LOG.info("Count of packages: " + listOfOrderedPackageNames.size());
		listOfOrderedPackageNames.stream().forEachOrdered(p -> LOG.info("PACK NAME: " + p.toString()));

		// creation de la liste des ApiPackage et association au site
		// determination du MainPackage
		LOG.info("build and save list ApiPackage...");
		this.buildAndSaveListApiPackage(site, listOfOrderedPackageNames);

		LOG.info("build and save list ApiDataType...");
		this.buildAndSaveListApiDatatype(site, mapPackageNameToListPathInfo);

		return true;

	}

	private void buildAndSaveListApiDatatype(Site site, Map<String, List<PathInfo>> mapPackageNameToListPathInfo) {

		site.getListApiPackages().stream()
				.filter((ApiPackage apiPackage) -> !CollectUtils
						.isNullOrEmpty(mapPackageNameToListPathInfo.get(apiPackage.getLongName())))
				.forEach(apiPackage -> {
					// pour garantir l'unicite dans un package
					final Map<String, ApiDataType> mapNameToDataTypes = new HashMap<>();
					mapPackageNameToListPathInfo.get(apiPackage.getLongName()).stream()
							.forEach(pathInfo -> this.buildApiDataType(pathInfo, apiPackage, mapNameToDataTypes));
				});

	}

	private ApiDataType buildApiDataType(PathInfo pathInfo, ApiPackage apiPackage,
			Map<String, ApiDataType> mapNameToDataTypes) {

		String name = pathInfo.getClassname();
		JavaType type = this.getApiType(pathInfo.getFileType());

		ApiDataType apiDataType = Objects.nonNull(type) ? new ApiDataType(type, name) : null;
		if (Objects.nonNull(apiDataType) && !mapNameToDataTypes.containsKey(name)) {
			apiPackage.addApiDataType(apiDataType);
			mapNameToDataTypes.put(name, apiDataType);
		}
		return apiDataType;
	}

	private JavaType getApiType(FileType fileType) {

		switch (fileType) {
		case ANNOTATION:
			return JavaType.TAnnotation;
		case CLASS:
			return JavaType.TClass;
		case ENUM:
			return JavaType.TEnum;
		case INTERFACE:
			return JavaType.TInterface;
		default:
			return null;
		}

	}

	private void buildAndSaveListApiPackage(Site site, List<String> listOfOrderedPackageNames) {

		Map<String, ApiPackage> mapPackageNameToApiPackage = new HashMap<>(listOfOrderedPackageNames.size());
		ApiPackage rootPackage = new ApiPackage(null, "root");
		mapPackageNameToApiPackage.put("root", rootPackage);

		// on construit la liste des ApiPackage sans les associer
		listOfOrderedPackageNames.stream()
				.forEachOrdered(packageName -> this.buildRecurApiPackage(packageName, mapPackageNameToApiPackage));

		Map<Integer, List<ApiPackage>> mapLevelByListApiPackage = mapPackageNameToApiPackage.values().stream()
				.collect(Collectors.groupingBy(p -> p.getLevel()));

		// on cherche le premier level avec un count ApiPackage > 1
		// le main level est juste le niveau inf
		int lastLevel = mapLevelByListApiPackage.size() - 2;
		int maxLevel = IntStream.range(0, mapLevelByListApiPackage.size()).filter((level) -> {
			
			if (mapLevelByListApiPackage.containsKey(level)) {
				int count = mapLevelByListApiPackage.get(level).size();
				if (count > 1) {
					return true;
				}
			}
			return false;
		}).findFirst().orElse(lastLevel);

		ApiPackage mainPackage = mapLevelByListApiPackage.get(maxLevel - 1).get(0);
		LOG.info("max level: " + maxLevel + " - main: " + mainPackage);

		// on associe les package au site
		mapPackageNameToApiPackage.values().forEach(p -> site.addApiPackage(p, p == mainPackage));

	}

	// fonction recursive de creation des packages
	private ApiPackage buildRecurApiPackage(String packageName, Map<String, ApiPackage> mapPackageNameToApiPackage) {

		if (mapPackageNameToApiPackage.containsKey(packageName)) {
			return mapPackageNameToApiPackage.get(packageName);
		}

		ApiPackage apiPackage = null;
		String parentName = this.getPackageParentName(packageName);

		ApiPackage parentPackage = parentName == null ? null : mapPackageNameToApiPackage.get(parentName);
		if (Objects.nonNull(parentName) && parentPackage == null) {
			// le parent n'existe pas on le cree
			parentPackage = this.buildRecurApiPackage(parentName, mapPackageNameToApiPackage);
		}

		// on cree le package
		apiPackage = new ApiPackage(parentPackage, packageName);
		LOG.debug("ApiPackage: " + apiPackage.toString());
		mapPackageNameToApiPackage.put(packageName, apiPackage);

		return apiPackage;
	}

	private String getPackageParentName(String packageName) {

		int pos = packageName.lastIndexOf('.');
		if (pos > -1) {
			return packageName.substring(0, pos);
		} else
			return "root";
	}

	private boolean isScanInProgress(long siteId) {

		return this.siteScanInProgress.contains(siteId);
	}

	private void autosleep(long time) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ignored) {
		}
	}

	private Resource getResource(Resource mainPage, String index) {

		String mainUrl = resourceConverter.toUrl(mainPage);

		String separator = mainUrl.endsWith("/") ? "" : "/";
		String indexUrl = mainUrl + separator + index;
		return resourceConverter.toResource(indexUrl);
	}

}
