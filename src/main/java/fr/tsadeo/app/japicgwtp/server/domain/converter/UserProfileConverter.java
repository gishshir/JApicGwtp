/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.domain.converter;

import javax.persistence.Converter;

import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;

/**
 *
 * @author sylvie
 */
@Converter
public class UserProfileConverter extends AbstractCodeConverter<UserProfile>{

    @Override
    public UserProfile convertToEntityAttribute(Character code) {
        return super.convertToEntityAttribute(code, UserProfile.values(), UserProfile.Authenticated);
    }
    
}
