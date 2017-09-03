/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.domain.converter;

import javax.persistence.Converter;

import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;

/**
 *
 * @author sylvie
 */
@Converter
public class UrlStateConverter extends AbstractCodeConverter<UrlState>{
    
     @Override
    public UrlState convertToEntityAttribute(Character code) {
        return super.convertToEntityAttribute(code, UrlState.values(), UrlState.NoTested);
    }
}
