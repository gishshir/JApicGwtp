package fr.tsadeo.app.japicgwtp.client.application.site.editsite;

import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.PermanentSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;

import fr.tsadeo.app.japicgwtp.client.application.common.IPresenter;
import fr.tsadeo.app.japicgwtp.client.application.errormessage.ErrorMessagePresenter;
import fr.tsadeo.app.japicgwtp.client.application.site.SitePresenter;
import fr.tsadeo.app.japicgwtp.client.event.ConnectUserEvent;
import fr.tsadeo.app.japicgwtp.client.event.ConnectUserEvent.ConnectUserHandler;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent;
import fr.tsadeo.app.japicgwtp.client.event.NotificationEvent;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent.ItemAction;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent.ItemActionHandler;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent.StateAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.CreateOrUpdateItemResult;
import fr.tsadeo.app.japicgwtp.shared.dispatch.CreateOrUpdateSiteAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetItemForEditResult;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetSiteForEditAction;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDatasValidation;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdUtils;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForEdit;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;

public class EditSitePresenter extends Presenter<EditSitePresenter.MyView, EditSitePresenter.MyProxy>
		implements EditSiteUiHandlers, ConnectUserHandler, ItemActionHandler, IPresenter {

	private static final Logger LOG = Logger.getLogger("EditSitePresenter");

	interface MyView extends View, HasUiHandlers<EditSiteUiHandlers> {
		public void clearDatas();

		public void setTitle(String title);

		public void setSiteName(String name);

		public String getSiteName();

		public void setSiteUrl(String url);
		
		public void setSiteScanUrl(String scanUrl);

		public String getSiteUrl();
		
		public String getSiteScanUrl();

		public void setSiteVersion(String version);

		public String getSiteVersion();

		public void setUrlState(UrlState urlState);

		public void setScanStatus(ScanStatus scanStatus);

		public void setLastScan(String lastScan);

		public void setSiteEnabled(boolean enabled);

		public boolean isSiteEnabled();

		public void setValidationState(ValidationState validationState);

		public void setEditing(boolean editing);

		public void focusName();

		public void setReadOnly(boolean readOnly);
		
		public void enableButtonValidate(boolean enable);
		
		public void enableButtonReset(boolean enable);
		
		public boolean isFormValide();
	}

	@ProxyCodeSplit
	interface MyProxy extends Proxy<EditSitePresenter> {
	}

	static final PermanentSlot<ErrorMessagePresenter> SLOT_ERRORS = new PermanentSlot<>();

	private final DispatchAsync dispatcher;
	private final ErrorMessagePresenter errorMessagePresenter;
	private VoSiteForEdit voSiteForEdit;
	private boolean readOnly = true;
	private boolean createMode = false;

	private ValidationState validationState = ValidationState.NONE;
	private ItemActionEvent.StateAction stateEdit = ItemActionEvent.StateAction.start;

	@Inject
	EditSitePresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatcher,
			ErrorMessagePresenter errorMessagePresenter) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.errorMessagePresenter = errorMessagePresenter;

		this.getView().setUiHandlers(this);
		super.addVisibleHandler(ConnectUserEvent.TYPE, this);
		super.addVisibleHandler(ItemActionEvent.TYPE, this);
	}

	// ------------------------------ public methods
	public void displaySite(long siteId) {
		this.setValidationState(ValidationState.NONE);
		this.doGetSite(siteId);
	}

	// ------------------------------ implementing PresenterWidget
	@Override
	protected void onBind() {
		LOG.config("EditSitePresenter onBind...");
		super.onBind();
		super.setInSlot(SLOT_ERRORS, this.errorMessagePresenter);
	};

	@Override
	protected void onHide() {
		LOG.config("EditSitePresenter onHide...");
		super.onHide();
	}

	@Override
	protected void onReveal() {
		LOG.config("EditSitePresenter onReveal...");
		super.onReveal();
		this.getView().focusName();
	}

	// ---------------------------------------- implementing EditSiteUiHandlers
	@Override
	public void onUpdateSite() {
		this.errorMessagePresenter.hideErrorMessages();
		this.doUpdateSite(this.getSiteForValidation());
	}

	@Override
	public void onResetDatas() {
		LOG.config("EditSitePresenter onResetDatas()");
		this.setStateAction(ItemActionEvent.StateAction.canceled, true);
		this.setValidationState(ValidationState.NONE);
		this.updateView();
	}

	@Override
	public void onModifyDatas(InputData inputData) {


		boolean modified = false;
		switch (inputData) {
		case name:
			modified = this.isNameModified();
			break;
		case url:
			modified = this.isUrlModified();
			break;

		case scanUrl:
			modified = this.isScanUrlModified();
			break;

		case version:
			modified = this.isVersionModified();
			break;

		case enabled:
			modified = this.isEnabledModified();
			break;

		}

		if (modified) {
			boolean fireEvent = this.stateEdit != StateAction.inProgress;
			this.setStateAction(ItemActionEvent.StateAction.inProgress, fireEvent);
			this.setValidationState(ValidationState.NONE);
		}
	}

	//--------------------------------------- ItemActionHandler

	@Override
	public void onAction(ItemActionEvent event) {
		switch (event.getAction()) {
		case update:
			  if (event.getState() == StateAction.done && event.getItemId() == this.voSiteForEdit.getId()) {
				  refreshSite();
			  }
			
			break;

		default: //nothing
			break;
		}
	}

	// ----------------------- implementing ConnectUserHandler
	@Override
	public void onUserConnect(VoUser user) {
		LOG.config("EditSitePresenter onConnectUser...");
		this.refreshSite();
	}

	@Override
	public void onUserDisconnect() {
		this.refreshSite();
	}

	// --------------------------------- private method
	private boolean isNameModified() {

		return !this.voSiteForEdit.getName().equals(this.getView().getSiteName());
	}

	private boolean isUrlModified() {
		return !this.voSiteForEdit.getUrl().equals(this.getView().getSiteUrl());
	}

	private boolean isScanUrlModified() {
		return !this.voSiteForEdit.getScanUrl().equals(this.getView().getSiteScanUrl());
	}

	private boolean isVersionModified() {
		return !this.voSiteForEdit.getVersion().equals(this.getView().getSiteVersion());
	}

	private boolean isEnabledModified() {
		return this.voSiteForEdit.isEnabled() != this.getView().isSiteEnabled();
	}

	private VoSiteForEdit getSiteForValidation() {

		VoSiteForEdit voSiteForValidation = new VoSiteForEdit(this.voSiteForEdit.getDomainId(), this.voSiteForEdit);
		voSiteForValidation.setName(this.getView().getSiteName());
		voSiteForValidation.setUrl(this.getView().getSiteUrl());
		voSiteForValidation.setScanUrl(this.getView().getSiteScanUrl());
		voSiteForValidation.setVersion(this.getView().getSiteVersion());
		voSiteForValidation.setEnabled(this.getView().isSiteEnabled());
		voSiteForValidation.setLastScan(this.voSiteForEdit.getLastScan());
		voSiteForValidation.setUrlState(this.voSiteForEdit.getUrlState());
		voSiteForValidation.setScanStatus(this.voSiteForEdit.getScanStatus());
		return voSiteForValidation;
	}


	private void doUpdateSite(VoSiteForEdit voSiteForUpdate) {

		this.dispatcher.execute(new CreateOrUpdateSiteAction(voSiteForUpdate),
				new AsyncCallback<CreateOrUpdateItemResult<VoSiteForEdit>>() {

					@Override
					public void onFailure(Throwable caught) {
						LOG.severe("Echec in update site ...: " + caught.getMessage());
						setValidationState(ValidationState.ERROR);
						setStateAction(StateAction.error, true);
						addErrorMessage("Error", caught.getMessage());
					}

					@Override
					public void onSuccess(CreateOrUpdateItemResult<VoSiteForEdit> result) {
						VoDatasValidation voDatasValidation = result.get();
						if (voDatasValidation.isValid()) {
							LOG.info("success when updating site!!");
							voSiteForEdit = result.getVoItemForEdit();
							createMode = false;
							setSiteTitle();
							setStateAction(StateAction.done, true);
							setValidationState(ValidationState.SUCCESS);
						} else {
							LOG.warning("validation warning when updating site!!");
							
							setValidationState(ValidationState.WARNING);
							
							if (Objects.nonNull(voDatasValidation.getErrorMessages())) {

								int i = 1;
								for (String error : voDatasValidation.getErrorMessages()) {
									addErrorMessage("Warning " + i++, error);
								}
							}

						}

					}
				});
	}

	private void addErrorMessage(String title, String message) {
		this.errorMessagePresenter.addErrorMessage(title, message);
	}

	private void doGetSite(long siteId) {

		this.getView().clearDatas();
		this.dispatcher.execute(new GetSiteForEditAction(siteId),
				new AsyncCallback<GetItemForEditResult<VoSiteForEdit>>() {

					@Override
					public void onFailure(Throwable caught) {
						LOG.severe("Echec in get site for edit...: " + caught.getMessage());
						setStateAction(StateAction.error, true);
						NotificationEvent.fire(EditSitePresenter.this, "Erreur lors du chargement du site!", caught.getMessage(), NotificationEvent.Level.error);
					}

					@Override
					public void onSuccess(GetItemForEditResult<VoSiteForEdit> result) {

						voSiteForEdit = result.getItemForEdit();
						createMode = VoIdUtils.isIdUndefined(voSiteForEdit.getId());
						applyProtection(voSiteForEdit.getSiteProtection());
						setStateAction(ItemActionEvent.StateAction.start, false);
						updateView();
					}
				});

	}

	private void applyProtection(VoSiteProtection voSiteProtection) {

		this.readOnly = this.createMode?!voSiteProtection.canCreate(): !voSiteProtection.canUpdate();
		this.getView().setReadOnly(this.readOnly);
		this.setSiteTitle();
	}

	private void setSiteTitle() {
		String action = this.createMode?"Create":(this.readOnly ? "Display" :"Edit");
		this.getView()
				.setTitle(action + " site " + 
		                 (this.createMode?"...":this.voSiteForEdit.getName() + " " + this.voSiteForEdit.getVersion()));
	}

	private void updateView() {

		this.getView().clearDatas();
		if (Objects.isNull(this.voSiteForEdit)) {
			return;
		}
		LOG.config("EditSitePresenter updateView()");

		this.getView().setSiteName(this.voSiteForEdit.getName());
		this.getView().setSiteUrl(this.voSiteForEdit.getUrl());
		this.getView().setSiteScanUrl(this.voSiteForEdit.getScanUrl());
		this.getView().setSiteVersion(this.voSiteForEdit.getVersion());
		this.getView().setUrlState(this.voSiteForEdit.getUrlState());
		this.getView().setScanStatus(this.voSiteForEdit.getScanStatus());
		this.getView().setLastScan(
				(this.voSiteForEdit.getLastScan() > 0 ? DTF.format(new Date(this.voSiteForEdit.getLastScan())) : ""));
		this.getView().setSiteEnabled(this.voSiteForEdit.isEnabled());
	}

	private void setValidationState(ValidationState validationState) {
		this.validationState = validationState;
		this.getView().setValidationState(validationState);

		if (validationState == ValidationState.NONE || validationState == ValidationState.SUCCESS) {
			this.errorMessagePresenter.hideErrorMessages();
		}
	}

	private void setStateAction(ItemActionEvent.StateAction state, boolean fireEvent) {
		this.stateEdit = state;
		
		switch (state) {
		case start:
		case canceled:
		case done:
			this.getView().enableButtonReset(false);
			this.getView().enableButtonValidate(false);
			break;
			
		case inProgress:
			this.getView().enableButtonReset(true);
			this.getView().enableButtonValidate(this.getView().isFormValide());
			break;
			
		case error:
			this.getView().enableButtonReset(true);
			this.getView().enableButtonValidate(false);
			break;

		default:
			break;
		}
		
		
		this.getView().setEditing(state == StateAction.inProgress);

		if (fireEvent) {
			ItemActionEvent.fire(EditSitePresenter.this, voSiteForEdit.getId(), this.voSiteForEdit.getName(),
					ItemAction.edit, this.stateEdit);
		}
	}

	private void refreshSite() {
		if (Objects.nonNull(this.voSiteForEdit)) {
           this.setStateAction(StateAction.start, true);
		}
	}

}