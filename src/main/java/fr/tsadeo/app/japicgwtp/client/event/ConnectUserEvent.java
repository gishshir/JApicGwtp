package fr.tsadeo.app.japicgwtp.client.event;

import java.util.Objects;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import fr.tsadeo.app.japicgwtp.client.event.ConnectUserEvent.ConnectUserHandler;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;

public class ConnectUserEvent extends GwtEvent<ConnectUserHandler> {

	public interface ConnectUserHandler extends EventHandler {
		public void onUserConnect(VoUser user);

		public void onUserDisconnect();
	}

	public static final Type<ConnectUserEvent.ConnectUserHandler> TYPE = new Type<>();

	public static void fire(HasHandlers source, VoUser voUser) {
		source.fireEvent(new ConnectUserEvent(voUser));
	}

	private final VoUser voUser;

	public VoUser getVoUser() {
		return voUser;
	}

	// ----------------------------- constructor
	public ConnectUserEvent() {
		this(null);
	}

	public ConnectUserEvent(VoUser voUser) {
		this.voUser = voUser;
	}

	// ------------------------------- overriding GwtEvent
	@Override
	public Type<ConnectUserHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ConnectUserHandler handler) {

		if (Objects.nonNull(this.voUser)) {
			handler.onUserConnect(this.voUser);
		} else {
			handler.onUserDisconnect();
		}
	}

}
