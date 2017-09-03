/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.shared.domain;

import java.util.Objects;

import fr.tsadeo.app.japicgwtp.shared.domain.converter.IEnumConvertable;

/**
 *
 * @author sylvie
 */
public enum UserProfile implements IEnumConvertable {
    
    Anybody('B'), // utilisateur non authentifié
    
    // utilisateur authentifié
    Authenticated('U'), 
    Manager('M'), 
    Admin('A');
    
    private final char code;

    @Override
    public char getCode() {
        return this.code;
    }
    
    /**
     * Determine si une fonctionnalite est accessible pour un utilisateur ayant le profile userProfile
     * @param userProfile
     * @return
     */
    public boolean canAccess(UserProfile userProfile) {
    	UserProfile profile = Objects.isNull(userProfile)?UserProfile.Anybody:userProfile;
        return profile.ordinal() >= this.ordinal();
    }

    private UserProfile(char code) {
        this.code = code;
    }
    
}
