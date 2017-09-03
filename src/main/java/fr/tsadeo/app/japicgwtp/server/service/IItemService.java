/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.service;

import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;

/**
 *
 * @author sylvie
 * @param <T>
 */
public interface IItemService<T extends VoItemForEdit> extends IService {
    
    
     public T getItemForEdit(long itemId, boolean create, boolean atLeastManager) throws JApicException;

    public boolean deleteItem(long itemId) throws JApicException;
    
    public VoDatasValidation validateItem(T itemToUpdate) throws JApicException;

    public T createOrUpdateItem(T itemToUpdate, boolean createItem) throws JApicException;
    
    public void verifyDefaultItems() throws JApicException;
}
