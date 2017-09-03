package fr.tsadeo.app.japicgwtp.server.dispatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import fr.tsadeo.app.japicgwtp.server.service.IApiDataTypeService;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeResult;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDataTypeForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;

public class GetListDataTypeActionHandler
		extends AbstractActionHandler<GetListDataTypeAction, GetListDataTypeResult> {

	private final static Log LOG = LogFactory.getLog(GetListDataTypeActionHandler.class);

	@Autowired
	private IApiDataTypeService dataTypeService;

	// ------------------------------------- constructor
	public GetListDataTypeActionHandler() {
		super(GetListDataTypeAction.class);
	}

	// ------------------------------ overriding
	@Override
	public GetListDataTypeResult execute(GetListDataTypeAction action, ExecutionContext context)
			throws ActionException {

		VoSearchCriteria voSearchCriteria = action.getVoSearchCriteria();

		try {
			VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> list = this.dataTypeService.searchDataTypes(voSearchCriteria);
			GetListDataTypeResult result = new GetListDataTypeResult(list);

			return result;
		} catch (JApicException e) {

			throw new ActionException(e.getMessage());
		}
	}

	@Override
	public void undo(GetListDataTypeAction arg0, GetListDataTypeResult arg1, ExecutionContext arg2)
			throws ActionException {
		// TODO Auto-generated method stub

	}

}
