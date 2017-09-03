/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.domain;

import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;

/**
 *
 * @author sylvie
 */
public interface IIdAndName extends IModelItem {
    
    public String getName();
    
    public VoIdName toVoIdName();
}
