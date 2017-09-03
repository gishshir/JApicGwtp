package fr.tsadeo.app.japicgwtp.server.dispatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import fr.tsadeo.app.japicgwtp.server.service.IApiDataTypeService;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeForGridUpdateAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeForGridUpdateResult;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdHighlighText;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;

public class GetListDataTypeForGridUpdateActionHandler
		extends AbstractActionHandler<GetListDataTypeForGridUpdateAction, GetListDataTypeForGridUpdateResult> {

	private final static Log LOG = LogFactory.getLog(GetListDataTypeForGridUpdateActionHandler.class);

	@Autowired
	private IApiDataTypeService dataTypeService;

	// ------------------------------------- constructor
	public GetListDataTypeForGridUpdateActionHandler() {
		super(GetListDataTypeForGridUpdateAction.class);
	}

	// ------------------------------ overriding
	@Override
	public GetListDataTypeForGridUpdateResult execute(GetListDataTypeForGridUpdateAction action, ExecutionContext context)
			throws ActionException {

		VoSearchCriteria voSearchCriteria = action.getVoSearchCriteria();

		try {
			VoSearchResultDatas<VoIdHighlighText, VoItemProtection> list = this.dataTypeService.searchVoIdHighlighTexts(voSearchCriteria);
			GetListDataTypeForGridUpdateResult result = new GetListDataTypeForGridUpdateResult(list);

			return result;
		} catch (JApicException e) {

			throw new ActionException(e.getMessage());
		}
	}

	@Override
	public void undo(GetListDataTypeForGridUpdateAction arg0, GetListDataTypeForGridUpdateResult arg1, ExecutionContext arg2)
			throws ActionException {
		// TODO Auto-generated method stub

	}

}
