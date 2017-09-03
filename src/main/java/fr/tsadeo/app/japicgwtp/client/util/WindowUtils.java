package fr.tsadeo.app.japicgwtp.client.util;



import java.util.Objects;

import org.gwtbootstrap3.client.ui.Panel;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;

public class WindowUtils {
	
	public static void openWindow(String name, String url) {
		Window.open(url, name, "");
	}

	public static void openPopup(String name, String url, int height, int width) {

		String features = buildPopupFeatures(new PopupProps(height, width));
		Window.open(url, name, features);
	}

	public static void setSrcToIFrame(String target, String url) {
		
		if (Objects.isNull(target) || Objects.isNull(url)) {
			return;
		}
		Document doc = Document.get();

		Element elt = doc.getElementById(target);
		IFrameElement iframe = Objects.nonNull(elt) ? IFrameElement.as(elt) : null;
		if (Objects.nonNull(iframe)) {
			iframe.setSrc(url);
		}
	}

	private static String buildPopupFeatures(PopupProps popupProps) {

		String V = ", ";
		String line = "location=no,directories=no,status=no,menubar=no,toolbar=no";

		int left = ((Window.getClientWidth() - popupProps.width) / 2) >> 0;
		int top = ((Window.getClientHeight() - popupProps.height) / 2) >> 0;
		String sProp = popupProps.getPropsWidth().concat(V).concat(popupProps.getPropsHeight()).concat(V)
				.concat(popupProps.getPropsTop(top)).concat(V).concat(popupProps.getPropsLeft(left)).concat(V)
				.concat(line).concat(V).concat(popupProps.getPropsResizable()).concat(V)
				.concat(popupProps.getPropsScrolbars());
		return sProp;
	}

//	public static Panel buildLinkPopupPanel(String url, Element child) {
//
//		Panel linkPanel = new SimplePanel();
//		Element linkElement = buildLinkPopup(url, child);
//		linkPanel.getElement().appendChild(linkElement);
//
//		return linkPanel;
//	}

	public static Element buildLinkPopup(String url, Element child) {

		Element linkElement = null;
		if (Objects.nonNull(url)) {
			linkElement = DOM.createElement("a");
			String popupJs = "javascript:imPopUpWin('" + url + "',900,600,'no','yes');";
			linkElement.setAttribute("href", popupJs);

			linkElement.appendChild(child);
		}

		return Objects.nonNull(linkElement) ? linkElement : child;
	}

//	public static Panel buildLinkPanel(String target, String url, Element child) {
//
//		Panel linkPanel = new SimplePanel();
//		Element linkElement = buildLinkElement(target, url, child);
//		linkPanel.getElement().appendChild(linkElement);
//
//		return linkPanel;
//
//	}

	public static Element buildLinkElement(String target, String url, Element child) {

		Element linkElement = null;
		if (Objects.nonNull(url) && Objects.nonNull(target)) {
			linkElement = DOM.createElement("a");
			linkElement.setAttribute("href", url);
			linkElement.setAttribute("target", target);

			linkElement.appendChild(child);
		}

		return Objects.nonNull(linkElement) ? linkElement : child;
	}

	// ========================================= INNER CLASS
	public static final class PopupProps {

		private final int height;
		private final int width;
		private boolean scrolbars = false;
		private boolean resizable = false;

		public PopupProps(int height, int width) {
			this.height = height;
			this.width = width;
		}

		// ------------------------- accessors
		public void setScrolbars(boolean scrolbars) {
			this.scrolbars = scrolbars;
		}

		public void setResizable(boolean resizable) {
			this.resizable = resizable;
		}

		public String getPropsWidth() {
			return "width=" + this.width;
		}

		public String getPropsHeight() {
			return "height=" + this.height;
		}

		public String getPropsScrolbars() {
			return "scrollbars=" + ((this.scrolbars) ? "yes" : "no");
		}

		public String getPropsResizable() {
			return "resizable=" + ((this.resizable) ? "yes" : "no");
		}

		public String getPropsTop(int top) {
			return "top=" + top;
		}

		public String getPropsLeft(int left) {
			return "left=" + left;
		}

	}
}
