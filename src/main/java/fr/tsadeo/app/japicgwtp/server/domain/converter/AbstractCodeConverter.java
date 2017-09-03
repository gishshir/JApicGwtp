/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.domain.converter;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

import fr.tsadeo.app.japicgwtp.shared.domain.converter.IEnumConvertable;

/**
 *
 * @author sylvie
 * @param <T>
 */
public  abstract class AbstractCodeConverter<T extends IEnumConvertable> implements AttributeConverter<T ,Character> {

    @Override
    public Character convertToDatabaseColumn(T item) {
       return item.getCode();
    }


    protected T convertToEntityAttribute(Character code, T[] values, T defaultValue) {
       
        return Stream.of(values)
              .filter(s -> s.getCode() == code)
              .findFirst()
              .orElse(defaultValue);
    }

    
}
