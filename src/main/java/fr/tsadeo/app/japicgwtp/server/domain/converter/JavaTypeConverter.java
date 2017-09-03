/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.domain.converter;

import javax.persistence.Converter;

import fr.tsadeo.app.japicgwtp.shared.domain.JavaType;

/**
 *
 * @author sylvie
 */
@Converter
public class JavaTypeConverter extends AbstractCodeConverter<JavaType>{

    @Override
    public JavaType convertToEntityAttribute(Character code) {

       return super.convertToEntityAttribute(code, JavaType.values(), JavaType.TClass);
    }
    
}
