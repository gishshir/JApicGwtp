package fr.tsadeo.app.japicgwtp.server.service;

import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;

public interface IScanService extends IService {

	public UrlState defineAndSaveUrlState(long siteId);
	
	public boolean scanSite(long siteId);
}
