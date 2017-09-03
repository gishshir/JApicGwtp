package fr.tsadeo.app.japicgwtp.shared.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VoHighlighText implements IVo {

	private static final long serialVersionUID = 1L;
	
	public static final VoToken HIGHLIGHTED_TOKEN = new VoToken(null, true);
	
	private boolean hasTokenBegin = true;
	private boolean hasTokenEnd = true;
	
	private boolean valueNotFound = true;
	
	// decoupage du text en token
	// les caracteres de la recherche ne sont pas repetes
	// chaque token est separe par la chaine 'search'
	private List<VoToken> listTokens;

	//----------------------------------- constructor
	public VoHighlighText() {}
    //------------------------- accessors	
	public List<VoToken> getListTokens() {
		return this.listTokens;
	}
	
	public boolean isValueNotFound() {
		return this.valueNotFound;
	}
	public void setValueNotFound(boolean valueNotFound) {
		this.valueNotFound = valueNotFound;
	}
	public void addToken(String text) {
		if(this.listTokens == null) {
			this.listTokens = new ArrayList<VoHighlighText.VoToken>();
		}
		this.listTokens.add(new VoToken(text));
	}
	public void addHighlightedToken(String text) {
		if(this.listTokens == null) {
			this.listTokens = new ArrayList<VoHighlighText.VoToken>();
		}
		this.listTokens.add(Objects.isNull(text)? HIGHLIGHTED_TOKEN:new VoToken(text, true));
	}
	public void addHighlightedToken() {
		this.addHighlightedToken(null);
	}
	
	public static class VoToken implements IVo {
		
		private static final long serialVersionUID = 1L;

		private boolean highlighted = false;
		private String text;
		
		
		
		public boolean isHighlighted() {
			return highlighted;
		}
		public void setHighlighted(boolean highlighted) {
			this.highlighted = highlighted;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public VoToken () {}
		public VoToken(String text, boolean highlighted ) {
			this(text);
			this.highlighted = highlighted;
		}
		public VoToken(String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			
			return this.highlighted?"#" + text + "#":text;
		}
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		if (this.listTokens != null) {
			
			for (VoToken voToken : listTokens) {
				sb.append(voToken);
			}
		}
		return sb.toString();
	}
	
	
}
