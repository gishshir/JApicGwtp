/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.shared.vo;

/**
 *
 * @author sylvie
 */
public class VoDomainForGrid extends VoIdName {
	
	private static final long serialVersionUID = 1L;
    
    private String description;

    //-------------------------- constructor
    public VoDomainForGrid() {}
    public VoDomainForGrid(VoIdName voIdName) {
        super(voIdName);
    }

    //---------------------------- accessors
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
