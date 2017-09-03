/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.service;

import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
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
public interface IApiDomainService extends IItemService<VoDomainForEdit> {

	public final static String DEFAULT_DOMAIN_NAME = "test";
	
	public VoInitSiteDatas getVoInitSiteDatas() throws JApicException;
	
    public VoListItems<VoDomainForGrid, VoItemProtection> listDomains(boolean atLeastManager) throws JApicException;

    public VoInitSearchDatas getVoInitSearchDatas() throws JApicException;

}
