/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.shared.domain;

import fr.tsadeo.app.japicgwtp.shared.domain.converter.IEnumConvertable;

/**
 *
 * @author sylvie
 */
public enum UrlState implements IEnumConvertable {

    Alive('A', "Alive", true), // good formed and joinable
    AliveRestricted('D', "Restricted", true), // Alive but iframe not allowed
    NoTested('T', "no tested"), //  state not known
    WrongHost('H', "wrong host"),  // wrong host
    PageNoFound('P', "no found"), // good host but wrong page
    IndexNoFound('I', "wrong index"), // good host but wrong page
    Error('E', "Error");  // error
	
	
	private final boolean alive;
	public boolean isAlive() {
		return this.alive;
	}
	
	private final String text;
	public String getText() {
		return this.text;
	}

    private final char code;

    @Override
    public char getCode() {
        return this.code;
    }
    private UrlState(char code, String text) {
    	this(code, text, false);
    }
    private UrlState(char code, String text, boolean alive) {
        this.code = code;
        this.text = text;
        this.alive = alive;
    }

}
