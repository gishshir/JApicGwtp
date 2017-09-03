package fr.tsadeo.app.japicgwtp.server.dispatch;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import fr.tsadeo.app.japicgwtp.server.service.ITypeItemService;
import fr.tsadeo.app.japicgwtp.shared.dispatch.CreateOrUpdateItemAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.CreateOrUpdateItemResult;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;

public abstract class CreateOrUpdateItemActionHandlers<A extends CreateOrUpdateItemAction<T>, T extends VoItemForEdit>
		extends AbstractActionHandler<A, CreateOrUpdateItemResult<T>> {

	@Autowired
	private ITypeItemService typeItemService;

	// ------------------------------------------- constructor
	public CreateOrUpdateItemActionHandlers(Class<A> actionType) {
		super(actionType);
	}

	@Override
	public CreateOrUpdateItemResult<T> execute(A action, ExecutionContext context) throws ActionException {

		VoDatasValidation validation;
		try {
			T itemToUpdate = null;
			validation = this.typeItemService.validateItem(action.getItemToUpdate());

			if (validation.isValid()) {

				try {
					itemToUpdate = this.typeItemService.createOrUpdateItem(action.getItemToUpdate());
				} catch (Exception ex) {
					validation.setErrorMessages(ex.getMessage());
				}
			}
			return new CreateOrUpdateItemResult<T>(itemToUpdate,validation);

		} catch (JApicException e) {
			throw new ActionException(e.getMessage());
		}
	}

	@Override
	public void undo(A action, CreateOrUpdateItemResult<T> result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub

	}

}
