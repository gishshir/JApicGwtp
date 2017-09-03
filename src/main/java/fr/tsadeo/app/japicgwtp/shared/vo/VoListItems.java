/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.shared.vo;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author sylvie
 * @param <T>
 */
public class VoListItems<T extends IVo, P extends VoItemProtection> implements IVo {
	private static final long serialVersionUID = 1L;

    private P protection;
    private List<T> listItems;
    
    public boolean isEmpty() {
    	return Objects.isNull(listItems) || listItems.isEmpty();
    }
    
    public int size() {
    	return this.isEmpty()?0:this.listItems.size();
    }
    
    public T get(int index) {
    	return this.isEmpty()?null:this.listItems.get(index);
    }

    //-------------------- accessors
    public P getProtection() {
        return protection;
    }

    public void setProtection(P protection) {
        this.protection = protection;
    }

    public List<T> getListItems() {
        return listItems;
    }

    public void setListItems(List<T> listItems) {
        this.listItems = listItems;
    }

}
