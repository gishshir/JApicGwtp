/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.AppTransactional;
import fr.tsadeo.app.japicgwtp.server.aspect.TransactionAspect.TransactionMode;
import fr.tsadeo.app.japicgwtp.server.dao.IApiDomainDao;
import fr.tsadeo.app.japicgwtp.server.dao.ISiteDao;
import fr.tsadeo.app.japicgwtp.server.domain.ApiDomain;
import fr.tsadeo.app.japicgwtp.server.util.AbstractValidator;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;
import fr.tsadeo.app.japicgwtp.server.util.ModelUtils;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDomainForEdit;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDomainForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoInitSearchDatas;
import fr.tsadeo.app.japicgwtp.shared.vo.VoInitSiteDatas;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoListItems;

/**
 *
 * @author sylvie
 */
@Service
public class ApiDomainService extends AbstractService implements IApiDomainService {

    private final static Log LOG = LogFactory.getLog(ApiDomainService.class);
    

    @Autowired
    private IApiDomainDao domainDao;
    @Autowired
    private ISiteDao siteDao;
    @Autowired
    private DomainValidator domainValidator;
    

    //-------------------------------------- implementing ILoggable
    @Override
    public Log getLog() {
        return LOG;
    }

    //--------------------------------------- implementing IApiDomainService
	@Override
	@AppTransactional(TransactionMode.readonly)
	public VoInitSearchDatas getVoInitSearchDatas() throws JApicException {

		
		VoInitSearchDatas datas = new VoInitSearchDatas();
		final List<ApiDomain> listDomains = this.domainDao.listAll();
		if (CollectUtils.isNullOrEmpty(listDomains)){
			return datas;
		}
        
		datas.getListDomains().addAll(ModelUtils.getListVoIdName(listDomains));
		datas.getListSites().addAll(ModelUtils.getListVoIdName(this.siteDao.listSiteAvailables()));
		
		return datas;
	}


	@Override
	@AppTransactional(TransactionMode.readonly)
	public VoInitSiteDatas getVoInitSiteDatas() throws JApicException {

		VoInitSiteDatas datas = new VoInitSiteDatas();
		final List<ApiDomain> listDomains = this.domainDao.listAll();

		datas.setListDomains(ModelUtils.getListVoIdName(listDomains));

		return datas;
	}


    @Override
    @AppTransactional(TransactionMode.readonly)
    public VoListItems<VoDomainForGrid, VoItemProtection> listDomains(boolean atLeastManager) throws JApicException {

        VoListItems<VoDomainForGrid, VoItemProtection> voResult = new VoListItems<>();
        voResult.setProtection(this.buildProtection(atLeastManager));

        final List<ApiDomain> listDomains = this.domainDao.listAll();

        if (listDomains != null) {

            voResult.setListItems(
                    listDomains.stream()
                    .map(domain -> this.buildVoDomainForGrid(domain))
                    .collect(Collectors.toList()));
        }

        return voResult;
    }

    //--------------------------------------- implementing IItemService
    @Override
    @AppTransactional(TransactionMode.readonly)
    public VoDomainForEdit getItemForEdit(long domainId, boolean create, boolean atLeastManager) throws JApicException {

        VoDomainForEdit voDomain;
        if (create) {
            voDomain = new VoDomainForEdit();
        } else {
            ApiDomain domain = this.domainDao.getById(domainId, true);
            if (domain == null) {
                return null;
            }
            voDomain = this.buildVoDomainForEdit(domain);
        }

        voDomain.setProtection(this.buildProtection(atLeastManager));
        return voDomain;
    }

    @Override
    @AppTransactional(TransactionMode.auto)
    public boolean deleteItem(long domainId) throws JApicException {

        ApiDomain domain = this.domainDao.getById(domainId, false);
        if (Objects.isNull(domain)) {
            LOG.warn("Domain id " + domainId + " does'nt exist!");
            return false;
        }

        this.domainDao.delete(domain);
        return true;
    }

    @Override
    @AppTransactional(TransactionMode.readonly)
    public VoDatasValidation validateItem(VoDomainForEdit itemForEdit) throws JApicException {

    	VoDomainForEdit domainToUpdate = (VoDomainForEdit) itemForEdit;
        VoDatasValidation voValidation = this.domainValidator.validate(domainToUpdate);
        if (voValidation.isValid()) {
            ApiDomain domain = this.domainDao.getById(domainToUpdate.getId(), false);
            voValidation = this.domainValidator.validateIds(domain, domainToUpdate);
        }

        return voValidation;
    }

    @Override
    @AppTransactional(TransactionMode.auto)
    public VoDomainForEdit createOrUpdateItem(VoDomainForEdit itemForEdit, boolean createItem) throws JApicException {

    	VoDomainForEdit domainToUpdate = (VoDomainForEdit) itemForEdit;
        ApiDomain domain;
        if (createItem) {
            domain = new ApiDomain();
        } else {
            domain = this.domainDao.getById(domainToUpdate.getId(), true);
        }
        this.updateDomainWithVo(domain, domainToUpdate);
        if (createItem) {
          this.domainDao.attachAndSave(domain);	
        } else {
        this.domainDao.attachAndUpdate(domain);
        }

        VoDomainForEdit voDomain = this.buildVoDomainForEdit(domain);
        voDomain.setProtection(domainToUpdate.getProtection());
        return voDomain;
    }

    @Override
    @AppTransactional(TransactionMode.auto)
	public void verifyDefaultItems() throws JApicException {
		
    	ApiDomain domainTest = this.domainDao.getByNaturalId(DEFAULT_DOMAIN_NAME, false);
    	if (Objects.isNull(domainTest)) {
    		
    		// creation d'un domain de test par défaut
    		ApiDomain domain = new ApiDomain(DEFAULT_DOMAIN_NAME);
    		this.domainDao.attachAndSave(domain);
    		
    		LOG.info("Create default Api Domain!");
    	}
		
	}
    //---------------------------------------private methods
    private VoItemProtection buildProtection(boolean atLeastManager) {
        VoItemProtection protection = new VoItemProtection();

        protection.setCanCreate(atLeastManager);
        protection.setCanDelete(atLeastManager);
        protection.setCanEdit(true);
        protection.setCanUpdate(atLeastManager);

        return protection;
    }

    private VoDomainForGrid buildVoDomainForGrid(ApiDomain domain) {

        VoDomainForGrid voDomain = new VoDomainForGrid(domain.toVoIdName());
        voDomain.setDescription(domain.getDescription());
        return voDomain;
    }

    private VoDomainForEdit buildVoDomainForEdit(ApiDomain domain) {

        VoDomainForEdit voDomain = new VoDomainForEdit(domain.toVoIdName());
        voDomain.setDescription(domain.getDescription());
        return voDomain;
    }

    private void updateDomainWithVo(ApiDomain domain, VoDomainForEdit voDomain) {

        domain.setName(voDomain.getName());
        domain.setDescription(voDomain.getDescription());
    }

    //============================= INNER CLASS
    @Component
    private static class DomainValidator extends AbstractValidator {

        private VoDatasValidation validate(VoDomainForEdit voDomain) {

            VoDatasValidation voValidation = new VoDatasValidation();

            if (super.validateNotNull(voDomain, "API Domain", voValidation)) {

                super.validateString(voDomain.getName(), 5, "Domain name", voValidation);

                if (voDomain.getDescription() != null) {
                    super.validateString(voDomain.getDescription(), 5, "Domain description", voValidation);
                }
            }

            return voValidation;
        }

    }


	
}
