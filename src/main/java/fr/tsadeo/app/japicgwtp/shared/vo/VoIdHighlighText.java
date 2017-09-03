package fr.tsadeo.app.japicgwtp.shared.vo;

public class VoIdHighlighText extends AbstractVoId {

	private static final long serialVersionUID = 1L;
	
	private VoHighlighText voHighlighText;

	//------------------------------constructor
	public VoIdHighlighText() {
	}
	public VoIdHighlighText(long id) {
		super(id);
	}
	
	//---------------------------accessors
	public VoHighlighText getVoHighlighText() {
		return voHighlighText;
	}

	public void setVoHighlighText(VoHighlighText voHighlighText) {
		this.voHighlighText = voHighlighText;
	}
	
	

}
