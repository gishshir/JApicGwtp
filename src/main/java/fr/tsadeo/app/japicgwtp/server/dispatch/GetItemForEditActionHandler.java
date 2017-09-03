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
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetItemForEditAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetItemForEditResult;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;

public abstract class GetItemForEditActionHandler<A extends GetItemForEditAction<T>, T extends VoItemForEdit>
		extends AbstractActionHandler<A, GetItemForEditResult<T>> {

	public GetItemForEditActionHandler(Class<A> actionType) {
		super(actionType);
	}

	@Inject
	Provider<HttpServletRequest> requestProvider;

	@Autowired
	private ITypeItemService typeItemService;

	@Autowired
	private ProfileManager profileManager;

	@Override
	public GetItemForEditResult<T> execute(A action, ExecutionContext context) throws ActionException {

		try {
			T voItemForEdit = typeItemService.getItemForEdit(action.getItemId(), action.getType(),
					this.profileManager.isSessionAtLeastManager(this.getSession(false)));
			return new GetItemForEditResult<T>(voItemForEdit);
		} catch (JApicException e) {
			throw new ActionException(e.getMessage());
		}

	}

	@Override
	public void undo(A action, GetItemForEditResult<T> result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub

	}

	// ---------------------------------------- private methods
	private HttpSession getSession(boolean b) {
		return this.requestProvider.get().getSession(b);
	}

}