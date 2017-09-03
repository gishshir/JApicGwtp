/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.domain.converter;

import javax.persistence.Converter;

import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;

/**
 *
 * @author sylvie
 */
@Converter
public class ScanStatusConverter extends AbstractCodeConverter<ScanStatus> {

    @Override
    public ScanStatus convertToEntityAttribute(Character code) {
        return super.convertToEntityAttribute(code, ScanStatus.values(), ScanStatus.New);
    }
    
}
