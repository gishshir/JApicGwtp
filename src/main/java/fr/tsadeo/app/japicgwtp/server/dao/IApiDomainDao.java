/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;

/**
 *
 * @author sylvie
 */
public interface IApiDomainDao extends IITemDao<ApiDomain> {

    public static final String KEY_NAME = "name";    

    public static final String LIST_DOMAINS = SELECT + " d from ApiDomain d";
     
    public ApiDomain getByNaturalId(String name, boolean withDatas);
}
