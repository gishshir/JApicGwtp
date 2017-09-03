package fr.tsadeo.app.japicgwtp.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.server.manager.ProfileManager;

@Service
public class ServiceContext {
	
	@Autowired
	private IUserService userService;
	@Autowired
	private ISiteService siteService;
	@Autowired
	private ITypeItemService itemService;
	@Autowired
	private ProfileManager profileManager;
	@Autowired
	private IApiDomainService domainService;
	@Autowired
	private IScanService scanService;
	@Autowired
	private IApiDataTypeService dataTypeService;
	
	
	
	public IApiDataTypeService getDataTypeService() {
		return dataTypeService;
	}
	public IScanService getScanService() {
		return scanService;
	}
	public IApiDomainService getApiDomainService() {
		return this.domainService;
	}
	public IUserService getUserService() {
		return userService;
	}
	public ISiteService getSiteService() {
		return siteService;
	}
	public ITypeItemService getItemService() {
		return itemService;
	}
	public ProfileManager getProfileManager() {
		return profileManager;
	}

	

}
