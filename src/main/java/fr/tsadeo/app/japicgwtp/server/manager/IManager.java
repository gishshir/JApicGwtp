/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.manager;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author sylvie
 */
public interface IManager {

    public DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    public DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    public static final String KEY_SESSION_USER = "CurrentUser";

}
