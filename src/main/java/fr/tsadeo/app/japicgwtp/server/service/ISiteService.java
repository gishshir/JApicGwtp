/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.service;

import java.util.List;

import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;
import fr.tsadeo.app.japicgwtp.shared.vo.VoListItems;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForEdit;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteProtection;

/**
 *
 * @author sylvie
 */
public interface ISiteService extends IItemService<VoSiteForEdit> {
	
	public final static String LINK_SEPARATOR = "/";
	public final static char LINK_CHAR_SLASH = '/';
    
    public VoListItems<VoSiteForGrid, VoSiteProtection> listSitesByDomain(long domainId,boolean availableOnly,  boolean atLeastManager) throws JApicException;
    
    public List<VoIdName> listIdNameAvailableSiteByDomain(long domainId) throws JApicException;
    
    public void updateStatus(long siteId, UrlState urlState, ScanStatus scanStatus) ;
    
    public void deleteAllPackages(long site);
    
    public VoSearchResultDatas<VoSiteForGrid, VoSiteProtection> searchSites(VoSearchCriteria criteria, boolean atLeastManager) throws JApicException;
    
   
}
