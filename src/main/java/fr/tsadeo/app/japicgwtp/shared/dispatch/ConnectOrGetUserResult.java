package fr.tsadeo.app.japicgwtp.shared.dispatch;

import java.io.Serializable;

import com.gwtplatform.dispatch.rpc.shared.Result;

import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;

public class ConnectOrGetUserResult implements Result, Serializable {

	private static final long serialVersionUID = 1L;
	
	private VoUser voUser;
	
	public VoUser get() {
		return this.voUser;
	}
	
	public ConnectOrGetUserResult() {}

	public ConnectOrGetUserResult(VoUser voUser) {
		this.voUser = voUser;
	}
}
