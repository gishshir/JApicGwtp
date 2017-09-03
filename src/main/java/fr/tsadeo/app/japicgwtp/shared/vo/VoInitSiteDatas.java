/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.shared.vo;

import java.util.List;

/**
 *
 * @author sylvie
 */
public class VoInitSiteDatas implements IVo {
	
	private static final long serialVersionUID = 1L;
   
    private List<VoIdName> listDomains;

    //------------------------------ accessors
    public void setListDomains(List<VoIdName> listDomains) {
        this.listDomains = listDomains;
    }
    
    public List<VoIdName> getListDomains() {
        return this.listDomains;
    }
}
