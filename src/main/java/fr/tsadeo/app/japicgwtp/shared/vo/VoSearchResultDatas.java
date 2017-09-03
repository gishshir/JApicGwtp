package fr.tsadeo.app.japicgwtp.shared.vo;

public class VoSearchResultDatas<T extends IVo, P extends VoItemProtection> extends VoListItems<T, P> {


	private static final long serialVersionUID = 1L;
	
	private String search;

	//-------------------------------- accessors
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	

}
