/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.shared.util;

/**
 *
 * @author sylvie
 */
public class JApicException extends Exception {
	
	private static final long serialVersionUID = 1L;
    
    
	private String errorMessage;

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public JApicException() {
	}

	public JApicException(String message) {
		super(message);
		this.errorMessage = message;
	}

	public JApicException(Throwable exception) {

		super(exception.getMessage());
		this.buildErrorMessage(exception);
	}

	private void buildErrorMessage(Throwable exception) {

		this.errorMessage = exception.getMessage();

		if (exception.getCause() != null) {
			this.errorMessage += " (" + exception.getCause().getMessage() + ")";
		}
	}
}
