package fr.tsadeo.app.japicgwtp.server.service;

import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDataTypeForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdHighlighText;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;

public interface IApiDataTypeService extends IService {

	
	public VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> searchDataTypes(VoSearchCriteria criteria) throws JApicException;
	
	public VoSearchResultDatas<VoIdHighlighText, VoItemProtection> searchVoIdHighlighTexts(VoSearchCriteria criteria) throws JApicException;
}
