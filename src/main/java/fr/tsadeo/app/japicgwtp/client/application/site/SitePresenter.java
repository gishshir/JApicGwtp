package fr.tsadeo.app.japicgwtp.client.application.site;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.inject.Provider;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import fr.tsadeo.app.japicgwtp.client.application.ApplicationPresenter;
import fr.tsadeo.app.japicgwtp.client.application.javadoc.JavadocPresenter;
import fr.tsadeo.app.japicgwtp.client.application.site.editsite.EditSitePresenter;
import fr.tsadeo.app.japicgwtp.client.application.site.siteitem.SiteItemPresenter;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent;
import fr.tsadeo.app.japicgwtp.client.event.ItemActionEvent.ItemActionHandler;
import fr.tsadeo.app.japicgwtp.client.event.ItemSelectEvent;
import fr.tsadeo.app.japicgwtp.client.event.ItemSelectEvent.ItemSelectHandler;
import fr.tsadeo.app.japicgwtp.client.event.MenuChangeEvent;
import fr.tsadeo.app.japicgwtp.client.event.NotificationEvent;
import fr.tsadeo.app.japicgwtp.client.event.ConnectUserEvent;
import fr.tsadeo.app.japicgwtp.client.event.ConnectUserEvent.ConnectUserHandler;
import fr.tsadeo.app.japicgwtp.client.place.NameTokens;
import fr.tsadeo.app.japicgwtp.client.util.RefreshTimer;
import fr.tsadeo.app.japicgwtp.shared.dispatch.DeleteItemAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.DeleteItemResult;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListSiteAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListSiteResult;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ScanSiteAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.ScanSiteResult;
import fr.tsadeo.app.japicgwtp.shared.vo.IVo;
import fr.tsadeo.app.japicgwtp.shared.vo.IVoId;
import fr.tsadeo.app.japicgwtp.shared.vo.TypeItem;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSiteProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;

public class SitePresenter extends Presenter<SitePresenter.MyView, SitePresenter.MyProxy>
		implements SiteUiHandlers, ItemSelectHandler, ItemActionHandler, ConnectUserHandler {

	private static final Logger LOG = Logger.getLogger("SitePresenter");

	private static final String ALL = "*";
	private static final int MIN_SEARCH = 1;

	interface MyView extends View, HasUiHandlers<SiteUiHandlers> {
		public void focusSearch();

		public void clearSearch();

		public void clearDatas();

		public void setServerResponse(String message);
		
		public void setSearch(String search);

	}

	@NameToken(NameTokens.site)
	@ProxyCodeSplit
	interface MyProxy extends ProxyPlace<SitePresenter> {
	}

	// un slot pour deux presenter differents: JavadocPresenter ou
	// EditSitePresenter
	static final NestedSlot SLOT_DETAIL = new NestedSlot();
	static final Slot<SiteItemPresenter> SLOT_LIST_SITE = new Slot<>();

	private final JavadocPresenter javadocPresenter;
	private final EditSitePresenter editSitePresenter;
	private final DispatchAsync dispatcher;

	private final Map<Long, SiteItemPresenter> mapIdToPresenter = new HashMap<>();
	// factory of SiteItemModulePresenter
	private IndirectProvider<SiteItemPresenter> siteItemPresenterFactory;

	private boolean searchInProgress = false;
	private VoSearchCriteria searchDatas;

	private boolean searchToDo = false;
	private VoSearchCriteria searchDatasToDo;

	private RefreshTimer loadSiteTimer;

	private boolean editingSite = false;
	private long selectedSite = IVo.ID_UNDEFINED;

	// ------------------------------------------ constructor
	@Inject
	SitePresenter(EventBus eventBus, MyView view, MyProxy proxy, JavadocPresenter javadocPresenter,
			EditSitePresenter editSitePresenter, DispatchAsync dispatcher, Provider<SiteItemPresenter> provider) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT);

		super.getView().setUiHandlers(this);
		this.javadocPresenter = javadocPresenter;
		this.editSitePresenter = editSitePresenter;
		this.dispatcher = dispatcher;

		this.siteItemPresenterFactory = new StandardProvider<SiteItemPresenter>(provider);
		super.addVisibleHandler(ItemSelectEvent.TYPE, this);
		super.addVisibleHandler(ItemActionEvent.TYPE, this);
		super.addVisibleHandler(ConnectUserEvent.TYPE, this);
		this.buildLoadSiteTimer();
	}

	// ------------------------------- override gwtp interfaces
	@Override
	protected void onBind() {
		super.onBind();
		LOG.config("SitePresenter onBind()");
		super.setInSlot(SLOT_DETAIL, this.javadocPresenter);

	}

	@Override
	protected void onReveal() {
		LOG.config("SitePresenter onReveal()");
		MenuChangeEvent.fire(this, NameTokens.Token.site);
		this.getView().focusSearch();
		super.onReveal();
	}

	// --------------------------- implementing SiteUiHandlers
	@Override
	public void onAddSite() {
		this.createSite();
	}

	@Override
	public void onChangeSearch(VoSearchCriteria searchCriteria) {

		this.search(searchCriteria, false);
	}

	@Override
	public void onClearSearch() {
		this.getView().clearSearch();
		this.getView().clearDatas();
		this.getView().focusSearch();

	}

	// ------------------------- implementing ItemSelectHandler
	@Override
	public void onSelect(ItemSelectEvent event) {
		LOG.config("onSelectItem");

		long siteId = event.getItemId();
		this.selectSite(siteId);

		if (this.editingSite) {
			this.editingSite = false;
			super.setInSlot(SLOT_DETAIL, this.javadocPresenter);
		}
		this.showPageInIFrame(siteId);
	}

	// ------------------------- implementing ItemActionHandler
	@SuppressWarnings("incomplete-switch")
	@Override
	public void onAction(ItemActionEvent event) {

		if (Objects.isNull(event) || Objects.isNull(event.getAction())) {
			return;
		}

		switch (event.getAction()) {
		case scanSite:
			this.logItemActionEvent(event);
			this.doScanSite(event.getItemId());
			break;

		case delete:
			this.logItemActionEvent(event);
			this.doDeleteSite(event.getItemId());
			break;

		case edit:

			switch (event.getState()) {
			case start:
				this.logItemActionEvent(event);
				this.editSite(event.getItemId());
				break;

			case inProgress:
			case error: // do nothing
				break;

			case done:
				this.logItemActionEvent(event);
				this.editingSite = false;
				// rafraichir uniquement le site updated
				this.refreshSite(event.getItemId(), event.getItemName());
				break;

			case canceled:
				this.logItemActionEvent(event);
				this.editingSite = false;
				break;

			}
		}
	}

	private void logItemActionEvent(ItemActionEvent event) {
		LOG.config("SitePresenter onAction(): " + event.getAction() + " - state: " + event.getState());
	}

	// ---------------------------- implementing ConnectUserHandler
	@Override
	public void onUserConnect(VoUser user) {
		this.refresh();
	}

	@Override
	public void onUserDisconnect() {
		this.refresh();
	}

	// -------------------------------- private method
	private void selectSite(long siteId) {

		this.selectedSite = siteId;
		for (SiteItemPresenter siteItemPresenter : this.mapIdToPresenter.values()) {
			siteItemPresenter.selectSite(siteId);
		}
	}

	private void createSite() {
		LOG.config("SitePresenter createSite()");
		this.editSite(IVoId.ID_UNDEFINED);
	}

	private void editSite(long siteId) {
		LOG.config("SitePresenter editSite()");
		this.selectSite(siteId);
		this.editingSite = true;
		this.editSitePresenter.displaySite(siteId);
		super.setInSlot(SLOT_DETAIL, this.editSitePresenter);

	}

	private void search(VoSearchCriteria searchCriteria, boolean update) {

		if (Objects.isNull(searchCriteria) || Objects.isNull(searchCriteria.getSearch())) {
			this.getView().clearDatas();
			searchDatas = null;
			return;
		}
		// la dernière recherche avait les memes criteres
		if (!update && Objects.nonNull(searchDatas) && searchDatas.equals(searchCriteria)) {
			return;
		}

		if (this.searchInProgress) {
			// une recherche est déjà en cours.
			// on attend qu'elle soit terminée pour lancer une nouvelle
			// recherche
			LOG.config("searchInProgress...");
			this.searchToDo = true;
			this.searchDatasToDo = searchCriteria;
			return;
		}

		// controle at least 1 character
		if (searchCriteria.getSearch().length() < MIN_SEARCH) {
			this.getView().clearDatas();
			searchDatas = null;
			return;
		}

		LOG.config("search...");
		searchInProgress = true;
		searchDatas = searchCriteria;
		this.doGetSites(searchCriteria, update);

	}

	// search with same criteria
	private void refresh() {
		this.search(this.searchDatas, true);
	}

	private void refreshSite(long siteId, String name) {

		LOG.config("refreshSite id: " + siteId + " - name: " + name);
		if (this.mapIdToPresenter.containsKey(siteId)) {
			if (Objects.nonNull(this.searchDatas)) {
				this.searchDatas.setSiteId(siteId);
			}
			this.refresh();
		} else {
            this.getView().clearDatas();
            
			name = Objects.isNull(name) ? ALL : name;
			this.getView().setSearch(name);
			VoSearchCriteria voSearch = new VoSearchCriteria();
			voSearch.setSearch(name);
			this.search(voSearch, false);
		}
	}

	private void doDeleteSite(long siteId) {
		LOG.config("SitePresenter deleteSite()");
		this.dispatcher.execute(new DeleteItemAction(siteId, TypeItem.site), new AsyncCallback<DeleteItemResult>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().setServerResponse("An error occured: " + caught.getMessage());
				NotificationEvent.fire(SitePresenter.this, "Erreur lors de la suppression du site!",
						caught.getMessage(), NotificationEvent.Level.error);
			}

			@Override
			public void onSuccess(DeleteItemResult result) {
				LOG.config("SitePresenter deleteSite succeded!");
				SiteItemPresenter presenter = mapIdToPresenter.get(siteId);
				removeFromSlot(SLOT_LIST_SITE, presenter);
				mapIdToPresenter.remove(siteId);
			}
		});
	}

	private void doGetSites(VoSearchCriteria voCriteria, boolean update) {

		if (!update) {
			this.getView().clearDatas();
			this.mapIdToPresenter.clear();
		}
		dispatcher.execute(new GetListSiteAction(voCriteria), new AsyncCallback<GetListSiteResult>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().setServerResponse("An error occured: " + caught.getMessage());
				searchInProgress = false;
				NotificationEvent.fire(SitePresenter.this, "Echec de la récupération de la liste des sites!",
						caught.getMessage(), NotificationEvent.Level.error);
			}

			@Override
			public void onSuccess(GetListSiteResult result) {

				searchInProgress = false;
				voCriteria.setSiteId(IVo.ID_UNDEFINED);
				if (searchToDo) {
					// pas la peine d'afficher les resultats
					// si on lance aussitot une nouvelle recherche
					searchToDo = false;
					onChangeSearch(searchDatasToDo);
				} else if (result != null) {

					VoSearchResultDatas<VoSiteForGrid, VoSiteProtection> voResult = result.getSearchResultDatas();
					String search = voResult.getSearch();
					for (VoSiteForGrid voSite : voResult.getListItems()) {
						if (update) {
							updateSiteItemPresenter(voSite, search);
						} else {
							addSiteItemPresenter(voSite, search);
						}
					}

					getView().setServerResponse("get list sites success!");
				}

			}

		});

	}

	private void doScanSite(long siteId) {

		this.loadSiteTimer.doCancel();
		this.dispatcher.execute(new ScanSiteAction(siteId), new AsyncCallback<ScanSiteResult>() {

			@Override
			public void onFailure(Throwable caught) {
				LOG.severe("Echec of ScanSiteAction " + caught.getMessage());
				NotificationEvent.fire(SitePresenter.this, "Echec du scan!", caught.getMessage(),
						NotificationEvent.Level.error);
			}

			@Override
			public void onSuccess(ScanSiteResult result) {

				if (result.get()) {
					loadSiteTimer.doScheduleRepeting(5);
				}
			}
		});

	}

	private void updateSiteItemPresenter(final VoSiteForGrid voSite, final String search) {

		LOG.config("SitePresenter updateSiteItemPresenter(): " + voSite.getName());
		SiteItemPresenter siteItemPresenter = this.mapIdToPresenter.get(voSite.getId());
		if (Objects.nonNull(siteItemPresenter)) {
			siteItemPresenter.setVoSite(voSite, search);
		} else {
			this.addSiteItemPresenter(voSite, search);
		}
	}

	private void addSiteItemPresenter(final VoSiteForGrid voSite, final String search) {

		siteItemPresenterFactory.get(new AsyncCallback<SiteItemPresenter>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().setServerResponse("get presenter error!");
			}

			@Override
			public void onSuccess(SiteItemPresenter presenter) {

				LOG.config("add SiteItemPresenter");
				mapIdToPresenter.put(voSite.getId(), presenter);
				presenter.setVoSite(voSite, search);
				SitePresenter.this.addToSlot(SLOT_LIST_SITE, presenter);

			}
		});

	}

	private void showPageInIFrame(long siteId) {
		SiteItemPresenter presenter = this.mapIdToPresenter.get(siteId);
		if (presenter == null) {
			LOG.severe("Presenter " + siteId + " cannot be found!");
			return;
		}
		if (Objects.nonNull(presenter.getLink())) {
			this.javadocPresenter.showHtmlPage(presenter.getName(), presenter.getLink(), presenter.isEframeAllowed());
		} else {
			LOG.warning("Url state not alive!");
		}
	}

	private void buildLoadSiteTimer() {

		Command command = new Command() {

			@Override
			public void execute() {
				refresh();
			}

		};
		this.loadSiteTimer = new RefreshTimer(500, 2000, command);
	}

}