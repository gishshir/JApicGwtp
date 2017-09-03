/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.domain;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.NaturalId;

import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;

/**
 *
 * @author sylvie
 */
@MappedSuperclass
public abstract class AbstractVersionIdAndName extends AbstractModelVersionItem implements IIdAndName {

	private static final long serialVersionUID = 1L;
	
	@NaturalId(mutable = true)
    private  String name;

    //------------------------------------------ constructor
    protected AbstractVersionIdAndName() {
        this(null);
    }

    protected AbstractVersionIdAndName(String name) {
        this.name = name;
    }

    //----------------------------------------------- implementing IIdAndName
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public VoIdName toVoIdName() {
        return new VoIdName(this.getId(), this.name);
    }
    
    //---------------------------------------- protected methods
    protected void setName(String name) {
        this.name = name;
    }

}
