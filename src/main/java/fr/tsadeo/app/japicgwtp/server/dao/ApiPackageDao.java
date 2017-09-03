/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import fr.tsadeo.app.japicgwtp.server.domain.ApiPackage;
import fr.tsadeo.app.japicgwtp.server.domain.Site;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;

/**
 *
 * @author sylvie
 */
@Repository
public class ApiPackageDao extends AbstractDao<ApiPackage> implements IApiPackageDao {

    private static final Log LOG = LogFactory.getLog(ApiPackageDao.class);
    
    
    //--------------------------------------- implementing ILoggable
    @Override
    public Log getLog() {
        return LOG;
    }
        @Override
    protected String getHQLListAll() {
       return LIST_ALL;
    }

    //--------------------------------------- overriding AbstractDao
    @Override
    protected Class<ApiPackage> getClassItem() {
        return ApiPackage.class;
    }

    //------------------------------------- implementing IApiPackageDao
    @Override
    public ApiPackage getByNaturalId(Site site, String longName, boolean withDatas) {
        
        final Map<String, Object> keyvalues = CollectUtils.buildMapWithFirsItem(2,
                COL_SITE, site);
        keyvalues.put(COL_LONGNAME, longName);
        
        return super.getByNaturalId(keyvalues, withDatas);
    }

    @Override
    public List<ApiPackage> listBySite(Site site) {

        return super.listItems(LIST_BY_SITE, 
                CollectUtils.buildMapWithOneItem(COL_SITE, site));
    }

    
}
