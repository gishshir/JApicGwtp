/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;

/**
 *
 * @author sylvie
 */
@Repository
public class ApiDomainDao extends AbstractDao<ApiDomain> implements IApiDomainDao {
    
    private static final Log LOG = LogFactory.getLog(UserDao.class);
    


    //------------------------------------- implementing ILoggable
    @Override
    public Log getLog() {
        return LOG;
    }
   @Override
    public String getHQLListAll() {
     return LIST_DOMAINS;
    }
    // --------------------------------- implementing AbstractDao
        @Override
    protected Class<ApiDomain> getClassItem() {
        return ApiDomain.class;
    }

     // ------------------------------------- implementing IItemDao

    @Override
    public ApiDomain getByNaturalId(String name, boolean withDatas) {
       return super.getByNaturalId(KEY_NAME, name, withDatas);
    }
    
}
