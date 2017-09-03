package fr.tsadeo.app.japicgwtp.shared.dispatch;

import java.io.Serializable;

import com.gwtplatform.dispatch.rpc.shared.Result;

import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemForEdit;

public class CreateOrUpdateItemResult<T extends VoItemForEdit> implements Result, Serializable {

	private static final long serialVersionUID = 1L;
	
	private T voItemForEdit;
	private VoDatasValidation voDatasValidation;
	
	public VoDatasValidation get() {
		return this.voDatasValidation;
	}
	
	

	public T getVoItemForEdit() {
		return voItemForEdit;
	}



	public void setVoItemForEdit(T voItemForEdit) {
		this.voItemForEdit = voItemForEdit;
	}




	//---------------------------------------- constructor
	public CreateOrUpdateItemResult() {}

	public CreateOrUpdateItemResult(T voItemForEdit, VoDatasValidation voDatasValidation) {
		this.voItemForEdit = voItemForEdit;
		this.voDatasValidation = voDatasValidation;
	}
}
