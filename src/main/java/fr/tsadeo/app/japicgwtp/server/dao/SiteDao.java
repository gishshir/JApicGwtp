/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;

/**
 *
 * @author sylvie
 */
@Repository
public class SiteDao extends AbstractDao<Site> implements ISiteDao {

    private static final Log LOG = LogFactory.getLog(SiteDao.class);

    //---------------------------- overriding ILoggable
    @Override
    public Log getLog() {
        return LOG;
    }

    @Override
    protected String getHQLListAll() {
        return LIST_ALL + ORDER_BY_SITE;
    }

    //---------------------------- overriding AbstractDao
    @Override
    protected Class<Site> getClassItem() {
        return Site.class;
    }

    //----------------------------- overriding ISiteDao
    @Override
    public Site getByNaturalId(Resource mainPage, boolean withDatas) {
        return super.getByNaturalId(COL_URL, mainPage, withDatas);
    }

    @Override
    public List<Site> listByDomain(ApiDomain domain) {
     
        return super.listItems(LIST_BY_DOMAIN + ORDER_BY_SITE,
                CollectUtils.buildMapWithOneItem(COL_DOMAIN, domain));
    }

	@Override
	public List<Site> listByDomainAndAvailable(ApiDomain domain) {

		Map<String, Object> bindings = CollectUtils.buildMapWithFirsItem(2, COL_DOMAIN, domain);
		bindings.put(COL_SCAN_STATUS, ScanStatus.Done);

        return super.listItems(LIST_BY_DOMAIN_AND_AVAILABLE + ORDER_BY_SITE, bindings);
	}

	@Override
	public List<Site> listSiteAvailables() {
		
		Map<String, Object> bindings = CollectUtils.buildMapWithOneItem(COL_SCAN_STATUS, ScanStatus.Done);
		return super.listItems(LIST_ALL_AVAILABLE + ORDER_BY_SITE, bindings);
	}

	@Override
	public List<Site> listWithCriteria(SiteCriteria criteria) {

		if (Objects.isNull(criteria)) {
			return super.listAll();
		}
		
		Map<String, Object> bindings = new HashMap<>();
		
		boolean clauseWhere = false;
		StringBuilder hql = new StringBuilder();
		if (criteria.isOnlyAvailable()) {
			hql.append(LIST_ALL_AVAILABLE);
			bindings.put(COL_SCAN_STATUS, ScanStatus.Done);
			clauseWhere = true;
		} else {
			hql.append(LIST_ALL);
		}
		

		if (Objects.nonNull(criteria.getSiteName())) {
			hql.append(clauseWhere?AND:WHERE);
			hql.append(CONTAINS_SITE_NAME);
			bindings.put(COL_NAME, likeContent(criteria.getSiteName()));
			clauseWhere = true;
		}
		
		if (Objects.nonNull(criteria.getApiVersion())) {
			hql.append(clauseWhere?AND:WHERE);
			hql.append(CONTAINS_SITE_VERSION);
			bindings.put(COL_VERSION, criteria.getApiVersion());
			clauseWhere = true;
		}
		
		hql.append(ORDER_BY_SITE);
		
		return super.listItems(hql.toString(), bindings);
	}

}
