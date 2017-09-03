package fr.tsadeo.app.japicgwtp.client.application.site.siteitem;

import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

import fr.tsadeo.app.japicgwtp.client.application.common.IPresenter;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent;
import fr.tsadeo.app.japicgwtp.client.event.ItemSelectEvent;
import fr.tsadeo.app.japicgwtp.client.util.WidgetUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForGrid;

public class SiteItemPresenter extends PresenterWidget<SiteItemPresenter.MyView>
		implements SiteItemUiHandlers, IPresenter {

	private static final Logger LOG = Logger.getLogger("SiteItemPresenter");

	interface MyView extends View, HasUiHandlers<SiteItemUiHandlers> {

		public void setScanStatus(ScanStatus scanStatus);

		public void setLastScan(String lastScan);

		public void setEnabled(boolean enabled, boolean spin);

		public void setSiteName(String siteName);

		public void setSelected(boolean selected);
		
		public void showDeleteButton(boolean show);
	}

	private VoSiteForGrid voSite;
	private boolean selected = false;
	private String search;

	// ------------------------------ constructor
	@Inject
	SiteItemPresenter(EventBus eventBus, MyView view) {
		super(eventBus, view);
		this.getView().setUiHandlers(this);
	}

	// ---------------------------- implementing SiteItemUiHandler
	@Override
	public void onSelectSiteItem() {

		LOG.config("fire ItemSelectEvent");
		ItemSelectEvent.fire(this, this.voSite.getId());
	}

	@Override
	public void onStartScan() {
		this.voSite.setScanStatus(ScanStatus.Scanning);
		this.updateView();
		LOG.config("fire ItemActionEvent - scan site");
		ItemActionEvent.fire(this, this.voSite.getId(), ItemActionEvent.ItemAction.scanSite,
				ItemActionEvent.StateAction.start);
	}

	@Override
	public void onEditSite() {
		LOG.config("fire ItemActionEvent - edit");
		ItemActionEvent.fire(this, this.voSite.getId(), ItemActionEvent.ItemAction.edit,
				ItemActionEvent.StateAction.start);
	}
	@Override
	public void onDeleleSite() {
		LOG.config("fire ItemActionEvent - delete");
		ItemActionEvent.fire(this, this.voSite.getId(), ItemActionEvent.ItemAction.delete,
				ItemActionEvent.StateAction.start);
	}

	// ---------------------------- public methods
	public long getId() {
		return this.voSite.getId();
	}

	public String getLink() {
		return this.voSite.getUrlState().isAlive()?this.voSite.getUrl():null;
	}

	public boolean isEframeAllowed() {
		return this.voSite.isIframeAllowed();
	}

	public String getName() {
		return this.voSite.getName();
	}

	public void setVoSite(VoSiteForGrid voSite, String search) {
		this.voSite = voSite;
		this.search = search;
		this.updateView();
		ItemActionEvent.fire(this, this.voSite.getId(), ItemActionEvent.ItemAction.update,
				ItemActionEvent.StateAction.done);
	}

	public void selectSite(long siteId) {

		// c'est le site à selectionner
		if (siteId == this.getId()) {
			if (!this.selected) {
				this.selected = true;
				this.getView().setSelected(this.selected);
			}
		}
		// ce n'est pas le site à selectionner
		// s'il est selectionner, le deselectionner
		else if (this.selected) {
			this.selected = false;
			this.getView().setSelected(false);
		}
	}

	// ---------------------------------------- private methods
	private void updateView() {
		this.getView().setScanStatus(this.voSite.getScanStatus());
		this.getView()
				.setLastScan(this.voSite.getLastScan() > 0 ? DTF.format(new Date(this.voSite.getLastScan())) : "");
		boolean scanInProgress = this.voSite.getScanStatus() == ScanStatus.Scanning;
		this.getView().setEnabled(this.voSite.isEnabled(), scanInProgress);
		this.getView().showDeleteButton(this.voSite.getProtection().canDelete());

		if (Objects.nonNull(this.search)) {
			String htmlHighlightedName = WidgetUtils.buildHighlightedHtml(this.voSite.getName(),
					this.voSite.getVoHighlightedName(), this.search);
			this.getView().setSiteName(htmlHighlightedName + " " + this.voSite.getVersion());
		} else {
			this.getView().setSiteName(this.voSite.getName() + " " + this.voSite.getVersion());
		}

	}


}