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
public class VoDomainForEdit extends VoItemForEdit {
	
	private static final long serialVersionUID = 1L;
    

	private String description;

    //---------------------- constructor
    public VoDomainForEdit() {super();}
    public VoDomainForEdit(VoIdName voIdName) {
        super(TypeItem.domain, voIdName);
    }
//------------------------- accessors

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
   
    
    
}
