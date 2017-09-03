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
public enum ScanStatus implements IEnumConvertable {

  New ('N', "New"),
  Scanning('S', "Scanning"),
  Done ('D', "Scanned"),
  Error ('E', "Scan error"),
  Canceled('C', "Canceled");
	
	
	private final String text;
	public String getText() {
		return this.text;
	}
  
  private final char code;
  @Override
  public char getCode() {
      return this.code;
  }
  
  private ScanStatus (char code, String text) {
      this.code = code;
      this.text = text;
  }
}
