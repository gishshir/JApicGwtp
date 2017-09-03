package fr.tsadeo.app.japicgwtp.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface ButtonResources extends ClientBundle {

	interface Style extends CssResource {
		
		String buttonAction();
		
		String backgroundScanSite();

		String backgroundEdit();
		
		String backgroundDelete();
		
		String buttonCreateSite();
	}

	
	@Source("css/button.gss")
	Style style();

	@Source("images/scan.png")
	ImageResource imgResScan();
	
	@Source("images/create.png")
	ImageResource imgResCreate();
	
	@Source("images/delete.png")
	ImageResource imgResDelete();
	
	@Source("images/edit.png")
	ImageResource imgResEdit();

}
