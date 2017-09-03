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
public abstract class AbstractVoId implements IVoId{

    private static final long serialVersionUID = 1L;
    
    private Long id;
    private VoItemProtection protection;
    
    //-------------------------- overriding IVoId
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isUndefined() {
        return VoIdUtils.isIdUndefined(this.id);
    }
    //--------------------------- accessors
    
    public VoItemProtection getProtection() {
        return protection;
    }

    public void setProtection(VoItemProtection protection) {
        this.protection = protection;
    }

    //----------------------------- constructor
    public AbstractVoId() {
        this(ID_UNDEFINED);
    }
    public AbstractVoId(Long id) {
      this.id = id == null?ID_UNDEFINED:id;
    }

    
}
