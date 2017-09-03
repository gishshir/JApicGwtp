package fr.tsadeo.app.japicgwtp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.GwtEvent.Type;

import fr.tsadeo.app.japicgwtp.client.place.NameTokens;

public class MenuChangeEvent extends GwtEvent<MenuChangeEvent.MenuChangeHandler> {

	public interface MenuChangeHandler extends EventHandler {
		void onChange(MenuChangeEvent event);
	}

	public static final Type<MenuChangeHandler> TYPE = new Type<MenuChangeEvent.MenuChangeHandler>();
	
	public static void fire(HasHandlers source, NameTokens.Token place){
		source.fireEvent(new MenuChangeEvent(place));
	}

	private final NameTokens.Token place;

	public NameTokens.Token getPlace() {
		return place;
	}

	// ------------------------------ constructor
	public MenuChangeEvent(NameTokens.Token place) {
		this.place = place;
	}

	// ------------------------------------- overriding GwtEvent
	@Override
	public Type<MenuChangeEvent.MenuChangeHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MenuChangeEvent.MenuChangeHandler handler) {
		handler.onChange(this);
	}

}
