package fr.tsadeo.app.japicgwtp.shared.dispatch;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;

public class ConnectOrGetUserAction extends UnsecuredActionImpl<ConnectOrGetUserResult> {

	public enum Type {connect, disconnect, getUser}

	private Type type;
	private String login;
	private String password;
	
	
	
	public Type getType() {
		return type;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public ConnectOrGetUserAction() {}
	
	public ConnectOrGetUserAction(String login, String password) {
		this.login = login;
		this.password = password;
		this.type = Type.connect;
	}
	
	public ConnectOrGetUserAction(Type type) {
		this.type = type;
	}
}
