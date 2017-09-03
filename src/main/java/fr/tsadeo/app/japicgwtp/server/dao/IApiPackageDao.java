/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.List;

import fr.tsadeo.app.japicgwtp.server.domain.ApiPackage;
import fr.tsadeo.app.japicgwtp.server.domain.Site;

/**
 *
 * @author sylvie
 */
public interface IApiPackageDao extends IITemDao<ApiPackage> {

        
    public static String COL_SITE = "site";
    public static String COL_LONGNAME = "longName";

    
    public static final String LIST_ALL = SELECT + "p from ApiPackage p";    
    public static final String LIST_BY_SITE = LIST_ALL + WHERE +
            COL_SITE + "= :" + COL_SITE;    
    
    
    public ApiPackage getByNaturalId(Site site, String longName, boolean withDatas);    
    
    public List<ApiPackage> listBySite(Site site);
}
