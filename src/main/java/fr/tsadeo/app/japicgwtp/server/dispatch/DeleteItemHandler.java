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
import fr.tsadeo.app.japicgwtp.server.service.ITypeItemService;
import fr.tsadeo.app.japicgwtp.shared.dispatch.DeleteItemAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.DeleteItemResult;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;

public class DeleteItemHandler extends AbstractActionHandler<DeleteItemAction, DeleteItemResult>{
	
	@Inject
	Provider<HttpServletRequest> requestProvider;

	@Autowired
	private ITypeItemService typeItemService;

	@Autowired
	private ProfileManager profileManager;


	//---------------------------------- constructor
	public DeleteItemHandler() {
		super(DeleteItemAction.class);
	}

	//----------------------------------------- overriding AbstractActionHandler
	@Override
	public DeleteItemResult execute(DeleteItemAction action, ExecutionContext context) throws ActionException {
		try {
			
			if (!this.profileManager.isSessionAtLeastManager(this.getSession(false))) {
				throw new JApicException("User must be at least manager!");
			}
			
			boolean success = this.typeItemService.deleteItem(action.getItemId(), action.getTypeItem());
			return new DeleteItemResult(success);
		} catch (JApicException e) {
		   throw new ActionException(e.getMessage());
		}
	}

	@Override
	public void undo(DeleteItemAction action, DeleteItemResult result, ExecutionContext context)
			throws ActionException {
		
	}
	
	// ---------------------------------------- private methods
		private HttpSession getSession(boolean b) {
			return this.requestProvider.get().getSession(b);
		}

}
