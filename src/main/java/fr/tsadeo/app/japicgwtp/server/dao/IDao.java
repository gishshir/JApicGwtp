/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import fr.tsadeo.app.japicgwtp.server.ILoggable;
import fr.tsadeo.app.japicgwtp.server.util.IConstants;

/**
 *
 * @author sylvie
 */
public interface IDao extends ILoggable, IConstants {
	
	

    public static final String SELECT = "select ";    
    public static final String WHERE = " where ";    
    public static final String AND = " and ";
    public static final String OR = " or ";
    public static final String ORDER_BY = " order by ";
    public static final String IS_NOT_NULL = " is not null ";
}
