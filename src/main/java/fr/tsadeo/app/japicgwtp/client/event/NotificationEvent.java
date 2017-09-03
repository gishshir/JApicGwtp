package fr.tsadeo.app.japicgwtp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import fr.tsadeo.app.japicgwtp.client.event.NotificationEvent.NotificationHandler;

public class NotificationEvent extends GwtEvent<NotificationHandler> {
	
	public interface NotificationHandler extends EventHandler {
		public void onNotify(String title, String message, Level level);

	}

	public static final Type<NotificationHandler> TYPE = new Type<>();
	
	public enum Level {success, info, warn, error}
	

	public static void fire(HasHandlers source, String title, String message, Level level) {
		source.fireEvent(new NotificationEvent(title, message, level));
	}

	
	private final Level level;
	private final String message;
	private final String title;
	
	//------------------------------------ constructor
	public NotificationEvent(String title, String message, Level level) {
		this.title = title;
		this.message = message;
		this.level = level;
	}

	//------------------------------------------ overriding GwtEvent
	@Override
	public Type<NotificationHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NotificationHandler handler) {
        handler.onNotify(this.title,  this.message, this.level);		
	}

}
