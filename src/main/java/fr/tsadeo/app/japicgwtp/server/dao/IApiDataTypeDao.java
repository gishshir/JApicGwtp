/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.List;

import fr.tsadeo.app.japicgwtp.server.domain.ApiDataType;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.ApiPackage;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.shared.domain.JavaType;
import fr.tsadeo.app.japicgwtp.shared.vo.VoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;

/**
 *
 * @author sylvie
 */
public interface IApiDataTypeDao extends IITemDao<ApiDataType> {

	public static String COL_PACK = "apiPackage";
	public static String COL_SHORTNAME = "dname";
	public static String COL_TYPE = "javaType";
	
	public static final String PROC_LIST_CONTAINS_NAME = "callsearchbyname";
	public static final String PROC_LIST_CONTAINS_NAME_SITENAME = "callsearchbynameandsite";
	public static final String PROC_LIST_CONTAINS_NAME_SITENAME_VERSION = "callsearchbynameandsiteandversion";

	public static final String COND_SITE = "t." + COL_PACK + "." + IApiPackageDao.COL_SITE;
	public static final String COND_DOMAIN = COND_SITE + "." + ISiteDao.COL_DOMAIN;

	public static final String CONTAINS_DT_NAME = COL_SHORTNAME + " like :" + COL_SHORTNAME + " ";
	public static final String CONTAINS_SITE_NAME = COND_SITE + "." + ISiteDao.COL_NAME + " like :" + ISiteDao.COL_NAME
			+ " ";
	public static final String CONTAINS_SITE_VERSION = COND_SITE + "." + ISiteDao.COL_VERSION + " like :"
			+ ISiteDao.COL_VERSION + " ";

	public static final String SITE_AVAILABLE = COND_SITE + "." + ISiteDao.COL_ENABLED + " = true ";

	public static final String LIST_ALL = SELECT + "t from ApiDataType t";
	public static final String LIST_ALL_IDNAME = SELECT + //...
			"new fr.tsadeo.app.japicgwtp.shared.vo.VoIdName(t." + COL_ID + //...
			", t." + COL_SHORTNAME + //...
			") from ApiDataType t";

	public static final String LIST_BY_PACKAGE = LIST_ALL + WHERE + COL_PACK + " = :" + COL_PACK;

	public static final String LIST_BY_PACKAGE_AND_TYPE = LIST_BY_PACKAGE + AND + COL_TYPE + " = :" + COL_TYPE;

	public static final String LIST_BY_SITE = LIST_ALL + WHERE + COND_SITE + " = :" + IApiPackageDao.COL_SITE + " ";

	public static final String ORDER_BY_NAME_LENGTH_AND_PACK = ORDER_BY + " length(" + COL_SHORTNAME + "), " // ...
			+ COL_SHORTNAME + ", " // ...
			+ IApiPackageDao.COL_LONGNAME;

	public static final String ORDER_BY_PACK_AND_NAME = ORDER_BY + IApiPackageDao.COL_LONGNAME + ", " + COL_SHORTNAME;

	public static final String LIST_BY_DOMAIN = LIST_ALL + WHERE + COND_DOMAIN + " = :" + ISiteDao.COL_DOMAIN + " ";

	public static final String ORDER_BY_SITE_AND_PACK_AND_NAME = ORDER_BY + ISiteDao.COL_NAME + ", "
			+ IApiPackageDao.COL_LONGNAME + ", " + COL_SHORTNAME;

	// on force les jointures implicites pour order by
	public static final String LIST_ALL_ORDERED_FOR_SITE_AVAILABLE = LIST_ALL + WHERE + COND_DOMAIN + IS_NOT_NULL // ...
			+ AND + SITE_AVAILABLE + // ...
			ORDER_BY_NAME_LENGTH_AND_PACK;



	public ApiDataType getByNaturalId(ApiPackage apiPackage, String shortName, boolean withDatas);

	public List<ApiDataType> listByPackage(ApiPackage apiPackage);

	public List<ApiDataType> listByPackageAndType(ApiPackage apiPackage, JavaType type);

	public List<ApiDataType> listAllContainsNameForAvailableSites(DataTypeCriteria criteria);
	
	public List<VoIdName> listVoIdNameContainsNameForAvailableSites(DataTypeCriteria criteria);

	public List<ApiDataType> listByAvailableSite(Site site, DataTypeCriteria criteria);

	public List<ApiDataType> listByDomainAndAvailableSites(ApiDomain domain, DataTypeCriteria criteria);

	// ===================================================== INNER CLASS
	public static final class DataTypeCriteria {

		private boolean available = true;
		private final String dtName;
		private final String siteName;
		private final String apiVersion;

		// --------------------- accessors
		public String getDtName() {
			return dtName;
		}

		public String getSiteName() {
			return siteName;
		}

		public String getApiVersion() {
			return this.apiVersion;
		}

		public boolean isAvailable() {
			return this.available;
		}

		// ----------------------------- constructor
		public DataTypeCriteria(boolean available) {
			this(null, null);
			this.available = available;
		}

		public DataTypeCriteria(String dtName) {
			this(dtName, null);
		}

		public DataTypeCriteria(String dtName, String siteName) {
			this(dtName, siteName, null);
		}

		public DataTypeCriteria(String dtName, String siteName, String apiVersion) {
			this.dtName = dtName;
			this.siteName = siteName;
			this.apiVersion = apiVersion;
		}
	}

}
