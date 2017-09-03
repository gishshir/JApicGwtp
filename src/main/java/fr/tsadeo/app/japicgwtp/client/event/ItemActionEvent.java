package fr.tsadeo.app.japicgwtp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent.ItemActionHandler;


public class ItemActionEvent  extends GwtEvent<ItemActionHandler>{
	
	public enum ItemAction {scanSite, edit, delete, update}
	
	public enum StateAction {start, inProgress, done, canceled, error}
	
	public interface ItemActionHandler extends EventHandler {
		public void onAction(ItemActionEvent event);
	}
	
	public static final Type<ItemActionEvent.ItemActionHandler> TYPE = new Type<ItemActionEvent.ItemActionHandler>();

	public static void fire(HasHandlers source, long itemId, ItemAction action, StateAction state){
		source.fireEvent(new ItemActionEvent(itemId, action, state));
	}
	
	public static void fire(HasHandlers source, long itemId,String itemName, ItemAction action, StateAction state){
		ItemActionEvent event = new ItemActionEvent(itemId, action, state);
		event.itemName = itemName;
		source.fireEvent(event);
	}

	private final long itemId;
	private String itemName;
	private final ItemAction action;
	private final StateAction state;
	
	public long getItemId() {
		return this.itemId;
	}
	
	public ItemAction getAction() {
		return action;
	}
	

	public StateAction getState() {
		return state;
	}

	
	public String getItemName() {
		return itemName;
	}


	//------------------------------ constructor
	public ItemActionEvent(long id, ItemAction action, StateAction state) {
		this.itemId = id;
		this.action = action;
		this.state = state;
	}
	
	//------------------------------ overriding GwtEvent
	@Override
	public Type<ItemActionHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ItemActionHandler handler) {

       handler.onAction(this);		
	}
	

}
