package fr.tsadeo.app.japicgwtp.client.application.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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
import fr.tsadeo.app.japicgwtp.client.application.common.IPresenter;
import fr.tsadeo.app.japicgwtp.client.application.javadoc.JavadocPresenter;
import fr.tsadeo.app.japicgwtp.client.application.search.datatype.DataTypePresenter;
import fr.tsadeo.app.japicgwtp.client.application.site.SitePresenter;
import fr.tsadeo.app.japicgwtp.client.event.ItemSelectEvent;
import fr.tsadeo.app.japicgwtp.client.event.ItemSelectEvent.ItemSelectHandler;
import fr.tsadeo.app.japicgwtp.client.event.MenuChangeEvent;
import fr.tsadeo.app.japicgwtp.client.event.NotificationEvent;
import fr.tsadeo.app.japicgwtp.client.place.NameTokens;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeForGridUpdateAction;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeForGridUpdateResult;
import fr.tsadeo.app.japicgwtp.shared.dispatch.GetListDataTypeResult;
import fr.tsadeo.app.japicgwtp.shared.vo.VoDataTypeForGrid;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdHighlighText;
import fr.tsadeo.app.japicgwtp.shared.vo.VoItemProtection;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchCriteria;
import fr.tsadeo.app.japicgwtp.shared.vo.VoSearchResultDatas;

public class SearchPresenter extends Presenter<SearchPresenter.MyView, SearchPresenter.MyProxy>
		implements SearchUIHandlers, ItemSelectHandler, IPresenter {

	private static final Logger LOG = Logger.getLogger("SearchPresenter");

	private static final int MAX_ITEMS = 5;

	interface MyView extends View, HasUiHandlers<SearchUIHandlers> {
		public void focusSearch();

		public void clearSearch();

		public void clearDatas();

		public void setServerResponse(String message);
	}

	@NameToken(NameTokens.search)
	@ProxyCodeSplit
	interface MyProxy extends ProxyPlace<SearchPresenter> {
	}

	static final NestedSlot SLOT_JAVADOC = new NestedSlot();
	static final Slot<DataTypePresenter> SLOT_LIST_DATATYPE = new Slot<>();
	private final JavadocPresenter javadocPresenter;
	private final DispatchAsync dispatcher;

	private final Map<Long, DataTypePresenter> mapIdToPresenter = new HashMap<>();
	// l'ordre est important, il faut le conserver
	private final Map<Long, VoDataTypeForGrid> mapIdToDataTypeForGrid = new LinkedHashMap<>();

	// factory of DataTypeWidgetPresenter
	private IndirectProvider<DataTypePresenter> dataTypePresenterFactory;

	private boolean searchInProgress = false;
	private VoSearchCriteria searchDatas;

	private boolean searchToDo = false;
	private VoSearchCriteria searchDatasToDo;
	private boolean pasteFromScratch = false;

	@Inject
	SearchPresenter(EventBus eventBus, MyView view, MyProxy proxy, JavadocPresenter javadocPresenter,
			DispatchAsync dispatcher, Provider<DataTypePresenter> datatypeProvider) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT);

		this.getView().setUiHandlers(this);
		this.javadocPresenter = javadocPresenter;
		this.dispatcher = dispatcher;

		this.dataTypePresenterFactory = new StandardProvider<DataTypePresenter>(datatypeProvider);

		super.addVisibleHandler(ItemSelectEvent.TYPE, this);
	}

	// ------------------------------- override gwtp interfaces
	@Override
	protected void onBind() {
		super.onBind();
		LOG.config("SearchPresenter onBind()");
		super.setInSlot(SLOT_JAVADOC, this.javadocPresenter);
	}

	@Override
	protected void onHide() {
		LOG.config("SearchPresenter onHide()");
		super.onHide();
	}

	@Override
	protected void onReveal() {
		LOG.config("SearchPresenter onReveal()");
		MenuChangeEvent.fire(this, NameTokens.Token.search);
		this.getView().focusSearch();
		super.onReveal();
	}

	@Override
	protected void onReset() {
		LOG.config("SearchPresenter onReset()");
		super.onReset();
	}

	// ---------------------------------- implementing SearchUIHandlers
	@Override
	public void onClearSearch() {
		LOG.config("onClearSearch()");
		this.getView().clearSearch();
		this.getView().clearDatas();
		this.getView().focusSearch();
		this.reinitSearch();
	}

	@Override
	public void onChangeSearch(VoSearchCriteria searchCriteria) {

		this.search(searchCriteria);
	}

	// ------------------------------ overriding ItemSelectHandler
	@Override
	public void onSelect(ItemSelectEvent event) {

		LOG.config("onSelectItem");
		long itemId = event.getItemId();
		for (DataTypePresenter dataTypePresenter : this.mapIdToPresenter.values()) {
			dataTypePresenter.unSelectIfSelected(itemId);
		}

		this.showPageInIFrame(itemId);
	}

	// --------------------------- private methods
	private void reinitSearch() {
		this.mapIdToPresenter.clear();
		this.mapIdToDataTypeForGrid.clear();
		this.searchDatas = null;
		this.searchDatasToDo = null;
		this.searchInProgress = false;
		this.searchToDo = false;
	}

	private void search(VoSearchCriteria searchCriteria) {

		if (Objects.isNull(searchCriteria) || Objects.isNull(searchCriteria.getSearch())) {
			this.onClearSearch();
			return;
		}
		LOG.config("search: " + searchCriteria.getSearch() + " already inProgress: " + this.searchInProgress);
		if (this.searchInProgress) {
			// une recherche est déjà en cours.
			// on attend qu'elle soit terminée pour lancer une nouvelle
			// recherche
			this.searchToDo = true;
			this.searchDatasToDo = searchCriteria;
			return;
		}

		// la dernière recherche avait les memes criteres
		if (Objects.nonNull(searchDatas) && searchDatas.equals(searchCriteria)) {
			return;
		}

		// controle at least 3 character
		int lenght = searchCriteria.getSearch().trim().length();
		if (lenght < 3) {
			this.getView().clearDatas();
			this.searchDatas = null;
			this.pasteFromScratch = false;
			this.getView().clearDatas();
			this.reinitSearch();
			return;
		}
		// si paste from scratch dans la recherche precedente
		if (this.pasteFromScratch) {
			this.mapIdToPresenter.clear();
		}

		LOG.config("search...");
		this.searchInProgress = true;
		this.searchDatas = searchCriteria;

		if (this.mapIdToPresenter.isEmpty()) {

			this.pasteFromScratch = lenght > 3;
			this.doGetDataTypes(searchCriteria, false);

		} else {
			this.doGetVoIds(searchCriteria);
		}
	}

	/**
	 * Recherche uniquement des ids
	 * 
	 * @param voCriteria
	 */
	private void doGetVoIds(VoSearchCriteria voCriteria) {

		LOG.config("doGetVoIds...");
		dispatcher.execute(new GetListDataTypeForGridUpdateAction(voCriteria),
				new AsyncCallback<GetListDataTypeForGridUpdateResult>() {

					@Override
					public void onFailure(Throwable caught) {
						executeInFailure(caught);
					}

					@Override
					public void onSuccess(GetListDataTypeForGridUpdateResult result) {

						if (Objects.isNull(result)) {
							searchDatas = null;
							return;
						}
						if (!isSearchToDo()) {

							VoSearchResultDatas<VoIdHighlighText, VoItemProtection> voResult = result.get();
							String search = voResult.getSearch();

							LOG.config("SearchPresenter - filtering input - update presenter....");
							filteringAndUpdatingPresenters(voResult);

							searchInProgress = false;
							getView().setServerResponse("get list ids success!");
						}
					}
				});
	}

	/**
	 * Premiere recherche complete - retourne les objets VoDataTypeForGrid
	 * 
	 * @param voCriteria
	 */
	private void doGetDataTypes(VoSearchCriteria voCriteria, boolean skipDisplay) {

		LOG.config("doGetDataTypes... " + voCriteria.getSearch() + " - skipDisplay: " + skipDisplay);
		dispatcher.execute(new GetListDataTypeAction(voCriteria), new AsyncCallback<GetListDataTypeResult>() {

			@Override
			public void onFailure(Throwable caught) {
				executeInFailure(caught);
			}

			@Override
			public void onSuccess(GetListDataTypeResult result) {

				if (Objects.isNull(result)) {
					searchDatas = null;
					return;
				}

				VoSearchResultDatas<VoDataTypeForGrid, VoItemProtection> voResult = result.get();
				String search = voResult.getSearch();

				int maxDisplay = Math.min(MAX_ITEMS, voResult.getListItems().size());
				final CountPresenterHandler handler = new CountPresenterHandler(maxDisplay, () -> {

					if (!skipDisplay) {
						displayPresentersUntil(MAX_ITEMS);
					}
					searchInProgress = false;
					isSearchToDo();
				});

				int count = 0;
				// creation des presenters, affichage géré par handler lors ttes creations terminees.
				for (VoDataTypeForGrid voDataType : voResult.getListItems()) {

					mapIdToDataTypeForGrid.put(voDataType.getId(), voDataType);
					// ne pas tout afficher d'un seul coup pour accelerer la
					// premiere requete
					if (count < maxDisplay) {
						addDataTypePresenter(voDataType, search, handler);
					}
					count++;
				}
				getView().setServerResponse("get list datas success!");

			}

		});
	}

	private void displayPresentersUntil(int maxCount) {

		this.clearSlot(SLOT_LIST_DATATYPE);
		int count = 0;
		for (Long id : this.mapIdToDataTypeForGrid.keySet()) {

			if (count >= maxCount) {
				break;
			}
			DataTypePresenter presenter = this.mapIdToPresenter.get(id);
			if (Objects.nonNull(presenter) && presenter.isActif()) {
				LOG.config("DisplayPresenters - id: " + id);
				count++;
				this.addToSlot(SLOT_LIST_DATATYPE, presenter);
			}
		}

	}

	private boolean isSearchToDo() {

		if (searchToDo && Objects.nonNull(searchDatasToDo)) {
			searchToDo = false;
			searchInProgress = false;
			onChangeSearch(searchDatasToDo);
			return true;
		}
		return false;

	}

	private void executeInFailure(Throwable caught) {
		getView().setServerResponse("An error occured: " + caught.getMessage());
		searchInProgress = false;
		searchDatas = null;
		NotificationEvent.fire(this, "An error occured:", caught.getMessage(), NotificationEvent.Level.error);
	}

	/**
	 * Le slot est vidé On cree les presenter manquants, et on determine leur
	 * visibilité Ils ne sont pas encore affiches
	 * 
	 * @param voResult
	 */
	private void filteringAndUpdatingPresenters(VoSearchResultDatas<VoIdHighlighText, VoItemProtection> voResult) {

		// recuperation des id du resultat de la nouvelle recherche
		List<Long> newlistIds = new ArrayList<>();
		for (VoIdHighlighText voIdHighlighText : voResult.getListItems()) {
			newlistIds.add(voIdHighlighText.getId());

			// mise à jour du Highlighted text
			VoDataTypeForGrid voDataType = this.mapIdToDataTypeForGrid.get(voIdHighlighText.getId());
			voDataType.setVoHighlightedName(voIdHighlighText.getVoHighlighText());
		}

		String search = voResult.getSearch();

		List<VoDataTypeForGrid> listToBeCreated = new ArrayList<>();

		int maxDisplay = Math.min(MAX_ITEMS, newlistIds.size());
		int count = 0;
		// on itere sur la map des VoDataTypeForGrid (resultat de la premiere
		// recherche)
		for (Long oldId : this.mapIdToDataTypeForGrid.keySet()) {

			VoDataTypeForGrid voDataType = this.mapIdToDataTypeForGrid.get(oldId);
			boolean toBeDisplayed = newlistIds.contains(oldId);

			// on récupere le presenter si existe
			DataTypePresenter presenter = this.mapIdToPresenter.get(oldId);

			if (Objects.nonNull(presenter)) {
				presenter.setActif(toBeDisplayed);

				if (toBeDisplayed) {
					count++;
					presenter.updateHighlighedName(voDataType.getVoHighlightedName(), search);
				}
			} else {

				if (toBeDisplayed && count < maxDisplay) {
					count++;
					listToBeCreated.add(voDataType);
				}
			}
		}

		// Callback d'affichage
		CountPresenterHandler handler = new CountPresenterHandler(listToBeCreated.size(),
				() -> displayPresentersUntil(maxDisplay));

		// maintenant on cree les Presenter manquant et on affiche les n premier
		for (VoDataTypeForGrid voDataType : listToBeCreated) {
			// il faut creer un nouveau presenter (dans la map)
			this.addDataTypePresenter(voDataType, search, handler);
		}

	}

	// ajout du DataTypePresenter dans la map
	// il n'est pas encore affiché
	private void addDataTypePresenter(final VoDataTypeForGrid voDataType, final String search,
			CountPresenterHandler handler) {

		dataTypePresenterFactory.get(new AsyncCallback<DataTypePresenter>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().setServerResponse("get list error!");
			}

			@Override
			public void onSuccess(DataTypePresenter presenter) {

				LOG.config("DataTypePresenter created! - id: " + voDataType.getId());
				mapIdToPresenter.put(voDataType.getId(), presenter);
				presenter.setApiDataTypeResult(voDataType, search);
				if (Objects.nonNull(handler)) {
					handler.addCreated();
				}

			}
		});

	}

	private void showPageInIFrame(long dataTypeId) {
		DataTypePresenter presenter = this.mapIdToPresenter.get(dataTypeId);
		if (presenter == null) {
			LOG.severe("Presenter " + dataTypeId + " cannot be found!");
			return;
		}

		this.javadocPresenter.showHtmlPage(presenter.getName(), presenter.getLink(), presenter.isEframeAllowed());
	}

	// ============================== INNER CLASS
	private static class CountPresenterHandler {

		private final int totalToCreate;
		private int created = 0;
		private final Command callback;
		private boolean ended = false;

		private CountPresenterHandler(int totalToCreate, Command callback) {
			this.totalToCreate = totalToCreate;
			this.callback = callback;
			this.process();
		}

		private void addCreated() {
			this.created++;
			this.process();
		}

		private void process() {
			if (!ended && this.created == this.totalToCreate && Objects.nonNull(this.callback)) {
				this.ended = true;
				this.callback.execute();
			}
		}
	}

}