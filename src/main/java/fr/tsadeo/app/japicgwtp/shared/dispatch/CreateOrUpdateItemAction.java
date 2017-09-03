package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;

public abstract class CreateOrUpdateItemAction<T extends VoItemForEdit>
                       extends  UnsecuredActionImpl<CreateOrUpdateItemResult<T>> {
	
	private T itemToUpdate;
	
	//---------------------------------- accessor
	public T getItemToUpdate() {
		return itemToUpdate;
	}

	//-------------------------------- constructor
	public CreateOrUpdateItemAction() {}

	public CreateOrUpdateItemAction(T itemToUpdate) {
		this.itemToUpdate = itemToUpdate;
	}
}
