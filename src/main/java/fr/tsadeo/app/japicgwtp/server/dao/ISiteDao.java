/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.List;

import org.springframework.core.io.Resource;

import fr.tsadeo.app.japicgwtp.server.dao.IApiDataTypeDao.DataTypeCriteria;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDataType;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;

/**
 *
 * @author sylvie
 */
public interface ISiteDao extends IITemDao<Site> {

    public static String COL_URL = "mainPage";
    public static String COL_SCAN_URL = "scanUrl";
    public static String COL_DOMAIN = "domain";
    public static String COL_NAME = "sname";
    public static String COL_SCAN_STATUS = "scanStatus";
    public static String COL_ENABLED  = "senabled";
    public static String COL_VERSION = "apiVersion";
    
    public static final String CONTAINS_SITE_NAME = COL_NAME + " like :" + COL_NAME + " ";
    
    public static final String CONTAINS_SITE_VERSION = COL_VERSION + " like :"
			+ COL_VERSION + " ";
    
    public static final String ORDER_BY_SITE = ORDER_BY +
    		COL_NAME + ", " + COL_VERSION;

    public static final String LIST_ALL = SELECT + " s from Site s";
    public static final String LIST_BY_DOMAIN = LIST_ALL + WHERE
            + COL_DOMAIN + " = :"+ COL_DOMAIN + " ";

    public static final String SITE_AVAILABLE =
    		COL_SCAN_STATUS + " =:" + COL_SCAN_STATUS //... 
    		+ AND + COL_ENABLED + " = true ";
    
    public static final String LIST_ALL_AVAILABLE = LIST_ALL +
    		WHERE + SITE_AVAILABLE;

    public static final String LIST_BY_DOMAIN_AND_AVAILABLE = LIST_BY_DOMAIN +
    		AND + SITE_AVAILABLE;

    public Site getByNaturalId(Resource mainPage, boolean withDatas);

    public List<Site> listByDomain(ApiDomain domain);
    
    /**
     * Liste des sites d'un domain avec scan status DONE et site.enabled
     * @param domain
     * @return
     */
    public List<Site> listByDomainAndAvailable(ApiDomain domain);
    
    
    /**
     * Liste des sites  avec scan status DONE et site.enabled
     * @param domain
     * @return
     */
    public List<Site> listSiteAvailables();
    
    
    /**
     * Liste des sites pour les criteres de recherche
     * @param criteria
     * @return
     */
    public List<Site> listWithCriteria(SiteCriteria criteria);

	// ===================================================== INNER CLASS
	public static final class SiteCriteria {

		private boolean onlyAvalaible = true;
		private final String siteName;
		private final String apiVersion;

		// --------------------- accessors

		public String getSiteName() {
			return siteName;
		}

		public String getApiVersion() {
			return this.apiVersion;
		}

		public boolean isOnlyAvailable() {
			return this.onlyAvalaible;
		}

		// ----------------------------- constructor
		public SiteCriteria(boolean onlyAvalaible) {
			this(onlyAvalaible, null);
		}

		public SiteCriteria(boolean onlyAvalaible, String siteName) {
			this(onlyAvalaible, siteName, null);
		}

		public SiteCriteria(boolean onlyAvalaible, String siteName, String apiVersion) {
			this.onlyAvalaible = onlyAvalaible;
			this.siteName = siteName;
			this.apiVersion = apiVersion;
		}
	}

}
