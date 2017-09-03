/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import fr.tsadeo.app.japicgwtp.server.dao.IApiDataTypeDao.DataTypeCriteria;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDataType;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.domain.ApiPackage;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.JavaType;
import fr.tsadeo.app.japicgwtp.shared.vo.VoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;

/**
 *
 * @author sylvie
 */
@Repository
public class ApiDataTypeDao extends AbstractDao<ApiDataType> implements IApiDataTypeDao {

	private static final Log LOG = LogFactory.getLog(ApiDataTypeDao.class);

	// ------------------------------------ implementing ILoggable
	@Override
	public Log getLog() {
		return LOG;
	}

	@Override
	protected String getHQLListAll() {
		return LIST_ALL;
	}

	// ------------------------------------ overriding AbstractDao
	@Override
	protected Class<ApiDataType> getClassItem() {
		return ApiDataType.class;
	}

	// ------------------------------------ implementing IApiDataTypeDao

	@Override
	public ApiDataType getByNaturalId(ApiPackage apiPackage, String shortName, boolean withDatas) {

		Map<String, Object> keyvalues = CollectUtils.buildMapWithFirsItem(2, COL_PACK, apiPackage);
		keyvalues.put(COL_SHORTNAME, shortName);

		return super.getByNaturalId(keyvalues, withDatas);
	}

	@Override
	public List<ApiDataType> listByPackage(ApiPackage apiPackage) {

		return super.listItems(LIST_BY_PACKAGE, CollectUtils.buildMapWithOneItem(COL_PACK, apiPackage));
	}

	@Override
	public List<ApiDataType> listByPackageAndType(ApiPackage apiPackage, JavaType type) {

		Map<String, Object> bindings = CollectUtils.buildMapWithFirsItem(2, COL_PACK, apiPackage);
		bindings.put(COL_TYPE, type);
		return super.listItems(LIST_BY_PACKAGE_AND_TYPE, bindings);
	}

	@Override
	public List<ApiDataType> listByAvailableSite(Site site, DataTypeCriteria criteria) {

		if (Objects.isNull(criteria)) {
			String hql = LIST_BY_SITE + AND + SITE_AVAILABLE + ORDER_BY_NAME_LENGTH_AND_PACK;
			return super.listItems(hql, CollectUtils.buildMapWithOneItem(IApiPackageDao.COL_SITE, site));
		} else {
			Map<String, Object> bindings = CollectUtils.buildMapWithFirsItem(2, IApiPackageDao.COL_SITE, site);
			bindings.put(COL_SHORTNAME, likeContent(criteria.getDtName()));
			return super.listItems(
					LIST_BY_SITE + AND + SITE_AVAILABLE + AND + CONTAINS_DT_NAME + ORDER_BY_NAME_LENGTH_AND_PACK,
					bindings);
		}
	}

	@Override
	public List<ApiDataType> listByDomainAndAvailableSites(ApiDomain domain, DataTypeCriteria criteria) {

		if (Objects.isNull(criteria)) {
			return super.listItems(LIST_BY_DOMAIN + AND + SITE_AVAILABLE + ORDER_BY_NAME_LENGTH_AND_PACK,
					CollectUtils.buildMapWithOneItem(ISiteDao.COL_DOMAIN, domain));
		} else {

			// search with domain and datatype name
			Map<String, Object> bindings = CollectUtils.buildMapWithFirsItem(3, ISiteDao.COL_DOMAIN, domain);
			bindings.put(COL_SHORTNAME, likeContent(criteria.getDtName()));
			if (Objects.isNull(criteria.getSiteName())) {

				return super.listItems(
						LIST_BY_DOMAIN + AND + SITE_AVAILABLE + AND + CONTAINS_DT_NAME + ORDER_BY_NAME_LENGTH_AND_PACK,
						bindings);
			} else {

				// search with site name
				bindings.put(ISiteDao.COL_NAME, likeContent(criteria.getSiteName()));

				if (Objects.isNull(criteria.getApiVersion())) {

					return super.listItems(LIST_BY_DOMAIN + AND + SITE_AVAILABLE + AND + CONTAINS_DT_NAME + AND
							+ CONTAINS_SITE_NAME + ORDER_BY_NAME_LENGTH_AND_PACK, bindings);
				} else {

					// search with version
					bindings.put(ISiteDao.COL_VERSION, likeContent(SPACE + criteria.getApiVersion()));
					return super.listItems(LIST_BY_DOMAIN + AND + SITE_AVAILABLE + AND + CONTAINS_DT_NAME + AND
							+ CONTAINS_SITE_NAME + AND + CONTAINS_SITE_VERSION + ORDER_BY_NAME_LENGTH_AND_PACK,
							bindings);
				}
			}

		}
	}

	@Override
	public List<VoIdName> listVoIdNameContainsNameForAvailableSites(DataTypeCriteria criteria) {

		if (Objects.isNull(criteria)) {
			return super.listIdNames(LIST_ALL_IDNAME, null);
		}
		String hql = null;
		Map<String, Object> bindings = CollectUtils.buildMapWithFirsItem(3, COL_SHORTNAME,
				likeContent(criteria.getDtName()));
		if (Objects.isNull(criteria.getSiteName())) {

			hql = LIST_ALL_IDNAME + WHERE + SITE_AVAILABLE + AND + CONTAINS_DT_NAME;

		} else {
			// search with site name
			bindings.put(ISiteDao.COL_NAME, likeContent(criteria.getSiteName()));

			if (Objects.isNull(criteria.getApiVersion())) {

				hql = LIST_ALL_IDNAME + WHERE + SITE_AVAILABLE + AND + // ...
						CONTAINS_DT_NAME + AND + // ...
						CONTAINS_SITE_NAME;

			} else {
				// search with version
				bindings.put(ISiteDao.COL_VERSION, likeContent(SPACE + criteria.getApiVersion()));
				hql = LIST_ALL_IDNAME + WHERE + SITE_AVAILABLE + AND + // ...
						CONTAINS_DT_NAME + AND + // ...
						CONTAINS_SITE_NAME + AND + // ...
						CONTAINS_SITE_VERSION;

			}

		}
		return super.listIdNames(hql, bindings);
	}


	@Override
	public List<ApiDataType> listAllContainsNameForAvailableSites(DataTypeCriteria criteria) {

		if (Objects.isNull(criteria)) {
			return super.listItems(LIST_ALL_ORDERED_FOR_SITE_AVAILABLE, null);
		}
		String procName = null;
		String hql = null;
		// search with datatype name
		Map<String, Object> bindings = CollectUtils.buildMapWithFirsItem(3, COL_SHORTNAME,
				likeContent(criteria.getDtName()));
		if (Objects.isNull(criteria.getSiteName())) {

			// hql = LIST_ALL + WHERE + SITE_AVAILABLE + AND + CONTAINS_DT_NAME
			// + AND + COND_DOMAIN + " is not null "
			// + ORDER_BY_NAME_LENGTH_AND_PACK;

			procName = PROC_LIST_CONTAINS_NAME;

		} else {
			// search with site name
			bindings.put(ISiteDao.COL_NAME, likeContent(criteria.getSiteName()));

			if (Objects.isNull(criteria.getApiVersion())) {

				// hql = LIST_ALL + WHERE + SITE_AVAILABLE + AND + // ...
				// CONTAINS_DT_NAME + AND + // ...
				// CONTAINS_SITE_NAME + AND + // ...
				// COND_DOMAIN + " is not null " // ...
				// + ORDER_BY_NAME_LENGTH_AND_PACK;
				procName = PROC_LIST_CONTAINS_NAME_SITENAME;

			} else {
				// search with version
				bindings.put(ISiteDao.COL_VERSION, likeContent(SPACE + criteria.getApiVersion()));
				// hql = LIST_ALL + WHERE + SITE_AVAILABLE + AND + // ...
				// CONTAINS_DT_NAME + AND + // ...
				// CONTAINS_SITE_NAME + AND + // ...
				// CONTAINS_SITE_VERSION + AND + // ...
				// COND_DOMAIN + " is not null " // ...
				// + ORDER_BY_NAME_LENGTH_AND_PACK;

				procName = PROC_LIST_CONTAINS_NAME_SITENAME_VERSION;

			}

		}
		return super.listItemsWithProcedure(procName, bindings);
		// super.listItems(hql, bindings);
	}

}
