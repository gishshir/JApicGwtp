package fr.tsadeo.app.japicgwtp.shared.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Datas init for search view
 * @author sylvie
 *
 */
public class VoInitSearchDatas implements IVo {

	private static final long serialVersionUID = 1L;

	private List<VoIdName> listDomains;
	
	private List<VoIdName> listSites;

	public List<VoIdName> getListDomains() {
		if (Objects.isNull(this.listDomains)) {
			this.listDomains = new ArrayList<>();
		}
		return this.listDomains;
	}
	public List<VoIdName> getListSites() {
		if (Objects.isNull(this.listSites)) {
			this.listSites = new ArrayList<>();
		}
		return this.listSites;
	}
	
}
