package fr.tsadeo.app.japicgwtp.server.dispatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import fr.tsadeo.app.japicgwtp.server.service.IScanService;
import fr.tsadeo.app.japicgwtp.server.service.ISiteService;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ScanSiteAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ScanSiteResult;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;

public class ScanSiteActionHandler extends AbstractActionHandler<ScanSiteAction, ScanSiteResult> {

	private ExecutorService executor = Executors.newFixedThreadPool(3);
	@Autowired
	private ISiteService siteService;
	@Autowired
	private IScanService scanService;

	// ------------------------------------------ constructor
	public ScanSiteActionHandler() {
		super(ScanSiteAction.class);
	}

	// --------------------------------- overriding AbstractActionHandlers
	@Override
	public ScanSiteResult execute(ScanSiteAction action, ExecutionContext context) throws ActionException {
		

		// on change les status
		this.siteService.updateStatus(action.getSiteId(), UrlState.NoTested, ScanStatus.Scanning);

		final long siteId = action.getSiteId();
		Runnable task = () -> {

			UrlState siteUrlState = scanService.defineAndSaveUrlState(siteId);

			if (siteUrlState.isAlive()) {

				siteService.deleteAllPackages(siteId);
				scanService.scanSite(siteId);
			} else {
				
				siteService.updateStatus(siteId, siteUrlState, ScanStatus.Canceled);
			}

		};
		this.executor.submit(task);

		// on n'attend pas la fin de l'operation (thread)
		return new ScanSiteResult(true);

	}

	@Override
	public void undo(ScanSiteAction action, ScanSiteResult result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub

	}

}
