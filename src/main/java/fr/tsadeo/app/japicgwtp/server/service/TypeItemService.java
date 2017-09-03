/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.service;

import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.server.util.AbstractValidator;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.TypeItem;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdUtils;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;

/**
 * FIXME ce service ne doit pas etre transactionnal!!
 *
 * @author sylvie
 */
@Service
public class TypeItemService extends AbstractService implements ITypeItemService {

    private final static Log LOG = LogFactory.getLog(TypeItemService.class);
	
    @Autowired
    private ISiteService siteService;
    @Autowired
    private IApiDomainService domainService;
    @Autowired
    private ItemValidator itemValidator;
    
    //--------------------------------- implementing ILoggable 
    @Override
    public Log getLog() {
        return LOG;
    }


    //------------------------------ implementing IITemService
    @SuppressWarnings("unchecked")
	@Override
    public <T extends VoItemForEdit> T getItemForEdit(long itemId, TypeItem typeItem, boolean atLeastManager) throws JApicException {
        boolean createItem = VoIdUtils.isIdUndefined(itemId);
        this.itemValidator.validateItemType(typeItem);

        return (T)this.getServiceByType(typeItem).getItemForEdit(itemId, createItem, atLeastManager);
    }

    @Override
    public <T extends VoItemForEdit> VoDatasValidation validateItem(T itemToUpdate) throws JApicException {

        this.itemValidator.validateItem(itemToUpdate, false);
        TypeItem typeItem = itemToUpdate.getType();
        this.itemValidator.validateItemType(typeItem);
        VoDatasValidation validation = this.getServiceByType(typeItem).validateItem(itemToUpdate);
        if (validation.isValid()) {
        	itemToUpdate.setValidationDone(true);
        }
        return validation;
    }
    

    @SuppressWarnings("unchecked")
	@Override
    public <T extends VoItemForEdit> T createOrUpdateItem(T itemToUpdate) throws JApicException {

        this.itemValidator.validateItem(itemToUpdate, true);
        boolean createItem = itemToUpdate.isUndefined();

        TypeItem typeItem = itemToUpdate.getType();
        this.itemValidator.validateItemType(typeItem);
        return (T) this.getServiceByType(typeItem).createOrUpdateItem(itemToUpdate, createItem);
    }

    @Override
    public boolean deleteItem(long itemId, TypeItem typeItem) throws JApicException {
        if (VoIdUtils.isIdUndefined(itemId)) {
            throw new JApicException("Invalid itemId !");
        }
        this.itemValidator.validateItemType(typeItem);
        return this.getServiceByType(typeItem).deleteItem(itemId);
    }

    //-------------------------------- private methods
    @SuppressWarnings("unchecked")
	private <T extends VoItemForEdit> IItemService<T> getServiceByType(TypeItem type) {

        switch (type) {
            case domain:
                return (IItemService<T>) this.domainService;
            case site:
                return (IItemService<T>)this.siteService;
        }
        return null;
    }

    //=================================== INNER CLASS
    @Component
    private static final class ItemValidator extends AbstractValidator {

        private void validateItemType(TypeItem type) throws JApicException {
            if (Objects.isNull(type)) {
                throw new JApicException("Invalid TypeItem !");
            }

        }

        private void validateItem(VoItemForEdit item, boolean validationDone) throws JApicException {

            if (Objects.isNull(item)) {
                throw new JApicException("Item cannot be null !");
            }
            this.validateItemType(item.getType());
            if (validationDone && !item.isValidationDone()) {
                throw new JApicException("The validation must have been done before!");
            }

        }
    }

}
