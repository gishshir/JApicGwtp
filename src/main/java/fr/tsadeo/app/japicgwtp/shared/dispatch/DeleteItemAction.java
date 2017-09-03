package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

import fr.tsadeo.app.japicgwtp.shared.vo.IVoId;
import fr.tsadeo.app.japicgwtp.shared.vo.TypeItem;

public class DeleteItemAction extends UnsecuredActionImpl<DeleteItemResult> {

	private long itemId;
	private TypeItem typeItem;

    //----------------------------- constructor
	public DeleteItemAction() {
      this(IVoId.ID_UNDEFINED, null);
	}

	public DeleteItemAction(long itemId, TypeItem typeItem) {
		this.itemId = itemId;
		this.typeItem = typeItem;
	}

	//---------------------------------- accessor
	public long getItemId() {
		return itemId;
	}

	public TypeItem getTypeItem() {
		return typeItem;
	}
	
	
}
