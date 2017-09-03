/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.tsadeo.app.japicgwtp.server.domain.IIdAndName;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;

/**
 *
 * @author sylvie
 */
public class ModelUtils {

    public static <T extends IIdAndName> List<VoIdName> getListVoIdName(List<T> listItems) {
     
        if (CollectUtils.isNullOrEmpty(listItems)) {
            return new ArrayList<>(0);
        }
        
        return listItems.stream()
                .map(item -> item.toVoIdName())
                .collect(Collectors.toList());
                
    }    
}
