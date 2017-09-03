package fr.tsadeo.app.japicgwtp.shared.dispatch;

import java.io.Serializable;

import com.gwtplatform.dispatch.rpc.shared.Result;

import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;

public class GetItemForEditResult<T extends VoItemForEdit> implements Result, Serializable {

	private static final long serialVersionUID = 1L;
	
	private T itemForEdit;
	public T getItemForEdit() {
		return this.itemForEdit;
	}
	
	public GetItemForEditResult() {}
	public GetItemForEditResult(T itemForEdit) {
		this.itemForEdit = itemForEdit;
	}

}
