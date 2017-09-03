package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

import fr.tsadeo.app.japicgwtp.shared.vo.TypeItem;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;

public abstract class GetItemForEditAction<T extends VoItemForEdit> extends UnsecuredActionImpl<GetItemForEditResult<T>> {
	
	private long itemId;
	private TypeItem type;

	//------------------------------ accessors
	public long getItemId() {
		return itemId;
	}
	public TypeItem getType() {
		return type;
	}
	
	//----------------------------- constructor
	public GetItemForEditAction() {}
	public GetItemForEditAction(long itemId, TypeItem type) {
		this.itemId = itemId;
		this.type = type;
	}

}
