package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.SimpleResult;

public class DeleteItemResult extends SimpleResult<Boolean> {


	private static final long serialVersionUID = 1L;

	public DeleteItemResult(boolean success) {
		super(success);
	}
	public DeleteItemResult() {
		super(false);
	}
	

}
