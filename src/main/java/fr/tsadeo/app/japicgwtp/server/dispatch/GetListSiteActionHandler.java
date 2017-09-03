package fr.tsadeo.app.japicgwtp.server.dispatch;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import fr.tsadeo.app.japicgwtp.server.manager.ProfileManager;
import fr.tsadeo.app.japicgwtp.server.service.ISiteService;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListSiteAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListSiteResult;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.IVo;
import fr.tsadeo.app.japicgwtp.shared.vo.VoListItems;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteProtection;

public class GetListSiteActionHandler extends AbstractActionHandler<GetListSiteAction, GetListSiteResult> {


	private final static Log LOG = LogFactory.getLog(GetListSiteActionHandler.class);

	@Autowired
	private ISiteService siteService;
	
	@Autowired
	private ProfileManager profileManager;

	
	@Inject
	Provider<HttpServletRequest> requestProvider;
	
	//------------------------------------------- constructor
	public GetListSiteActionHandler() {
		super(GetListSiteAction.class);
	}

	@Override
	public GetListSiteResult execute(GetListSiteAction action, ExecutionContext context) throws ActionException {
		VoSearchCriteria voSearchCriteria = action.getVoSearchCriteria();
		

		try {

			VoSearchResultDatas<VoSiteForGrid, VoSiteProtection>  searchResultDatas = 
                     this.siteService.searchSites(voSearchCriteria, this.profileManager.isSessionAtLeastManager(this.getSession(false)));
			GetListSiteResult result = new GetListSiteResult(searchResultDatas);

			return result;
		} catch (JApicException e) {
			throw new ActionException(e.getMessage());
		}

	}

	@Override
	public void undo(GetListSiteAction action, GetListSiteResult result, ExecutionContext context)
			throws ActionException {
		// TODO Auto-generated method stub
		
	}
	
	//---------------------------------------- private methods
	private HttpSession getSession(boolean b) {
		return this.requestProvider.get().getSession(b);
	}

}
