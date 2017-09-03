package fr.tsadeo.app.japicgwtp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ItemSelectEvent  extends GwtEvent<ItemSelectEvent.ItemSelectHandler>{
	
	public interface ItemSelectHandler extends EventHandler {
		void onSelect(ItemSelectEvent event);
	}
	
	public static final Type<ItemSelectHandler> TYPE = new Type<ItemSelectEvent.ItemSelectHandler>();

	public static void fire(HasHandlers source, long itemId){
		source.fireEvent(new ItemSelectEvent(itemId));
	}

	private long itemId;
	public long getItemId() {
		return this.itemId;
	}
	
	//------------------------------ constructor
	public ItemSelectEvent(long id) {
		this.itemId = id;
	}
	
	//------------------------------ overriding GwtEvent
	@Override
	public Type<ItemSelectHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ItemSelectHandler handler) {

       handler.onSelect(this);		
	}
	
	

}
