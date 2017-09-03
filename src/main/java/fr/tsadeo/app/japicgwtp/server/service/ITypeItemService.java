/*
 * service pour gerer les TypeItem
 */
package fr.tsadeo.app.japicgwtp.server.service;

import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.TypeItem;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;

/**
 *
 * @author sylvie
 */
public interface ITypeItemService extends IService {
    
    public <T extends VoItemForEdit> T getItemForEdit(long itemId, TypeItem typeItem, boolean atLeastManager) throws JApicException;
    
    public <T extends VoItemForEdit> VoDatasValidation validateItem(T itemToUpdate) throws JApicException;

    public <T extends VoItemForEdit> T createOrUpdateItem(T itemToUpdate) throws JApicException;

    public boolean deleteItem(long itemId, TypeItem typeItem) throws JApicException;

}
