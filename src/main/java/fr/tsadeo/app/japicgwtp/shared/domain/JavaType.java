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
public enum JavaType implements IEnumConvertable{

  TClass('C', "class"), //..
  TInterface('I', "interface"), //...
  TEnum('E', "enum"), //....
  TAnnotation('A', "annotation");
  

	private final String text;
	public String getText() {
		return this.text;
	}

	
  private final char code;
  private JavaType(char code, String text) {
      this.code = code;
      this.text = text;
  }
  @Override
  public char getCode() {
      return this.code;
  }
 
}
