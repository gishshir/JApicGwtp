package fr.tsadeo.app.japicgwtp.server.dispatch;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import fr.tsadeo.app.japicgwtp.server.manager.ProfileManager;
import fr.tsadeo.app.japicgwtp.server.service.IUserService;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ConnectOrGetUserAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ConnectOrGetUserResult;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;

public class ConnectOrGetUserActionHandler extends AbstractActionHandler<ConnectOrGetUserAction, ConnectOrGetUserResult> {

	@Inject
	Provider<HttpServletRequest> requestProvider;

	@Autowired
	private ProfileManager profileManager;

	@Autowired
	private IUserService userService;

	// ----------------------------------- constructor
	public ConnectOrGetUserActionHandler() {
		super(ConnectOrGetUserAction.class);
	}

	// --------------------------------- overriding AbstractActionHandler
	@Override
	public ConnectOrGetUserResult execute(ConnectOrGetUserAction action, ExecutionContext context)
			throws ActionException {
		
		VoUser voUser = null;
		
		try {
			
			switch (action.getType()) {
			case connect: voUser = this.authenticateUser(action.getLogin(), action.getPassword());
				break;

			case disconnect: this.deconnectUser();
				break;
				
			case getUser: voUser = this.getCurrentUser();
				break;
			}
			
			return new ConnectOrGetUserResult(voUser);	
		} catch (JApicException e) {
			throw new ActionException(e.getMessage());
		}
		
		
	}

	@Override
	public void undo(ConnectOrGetUserAction action, ConnectOrGetUserResult result, ExecutionContext context)
			throws ActionException {
		// TODO Auto-generated method stub

	}

	// ===========================================
	// AUTHENTICATION
	// ===========================================
	private void deconnectUser() throws JApicException {

		this.profileManager.setSessionUser(null, this.getSession(true));
	}

	private VoUser authenticateUser(String login, String pwd) throws JApicException {
		VoUser user = this.userService.authenticateUser(login, pwd);
		// authentication in success...
		this.profileManager.setSessionUser(user, this.getSession(true));
		return user;
	}

	private VoUser getCurrentUser() throws JApicException {

		// si la session a expirée l'utilisateur est null
		VoUser user = this.profileManager.getSessionUser(this.getSession(false));
		return user;
	}
	
	private HttpSession getSession(boolean b) {
		return this.requestProvider.get().getSession(b);
	}
}
