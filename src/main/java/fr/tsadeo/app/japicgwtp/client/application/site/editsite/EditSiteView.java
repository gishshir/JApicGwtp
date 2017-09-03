package fr.tsadeo.app.japicgwtp.client.application.site.editsite;

import javax.inject.Inject;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import fr.tsadeo.app.japicgwtp.client.application.site.editsite.EditSiteUiHandlers.InputData;
import fr.tsadeo.app.japicgwtp.client.util.WidgetUtils;
import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;
import fr.tsadeo.app.japicgwtp.shared.util.StatusUtils;

class EditSiteView extends ViewWithUiHandlers<EditSiteUiHandlers> implements EditSitePresenter.MyView {
	interface Binder extends UiBinder<Widget, EditSiteView> {
	}

	@UiHandler("btValidate")
	void onClickButtonValider(ClickEvent event) {
		this.getUiHandlers().onUpdateSite();
	}

	@UiHandler("btReset")
	void onClickButtonReset(ClickEvent event) {
		this.getUiHandlers().onResetDatas();
	}

	@UiHandler("formName")
	void onBlurInputName(BlurEvent event) {
		this.getUiHandlers().onModifyDatas(InputData.name);
	}

	@UiHandler("formUrl")
	void onBlurInputUrl(BlurEvent event) {
		this.getUiHandlers().onModifyDatas(InputData.url);
	}

	@UiHandler("formScanUrl")
	void onBlurInputScanUrl(BlurEvent event) {
		this.getUiHandlers().onModifyDatas(InputData.scanUrl);
	}

	@UiHandler("formVersion")
	void onBlurInputVersion(BlurEvent event) {
		this.getUiHandlers().onModifyDatas(InputData.version);
	}

	@UiHandler("formEnabled")
	void onClickEnabled(ClickEvent event) {
		this.getUiHandlers().onModifyDatas(InputData.enabled);
	}

	@UiField
	Form form;

	@UiField
	Heading editSiteTitle;

	@UiField
	Icon iconEditing;

	@UiField
	Label lbValidationState;

	@UiField
	Button btValidate;

	@UiField
	Button btReset;

	@UiField
	FormGroup gpName;

	@UiField
	FormGroup gpUrl;

	@UiField
	FormGroup gpVersion;

	@UiField
	TextBox formName;

	@UiField
	TextBox formUrl;

	@UiField
	TextBox formScanUrl;

	@UiField
	TextBox formVersion;

	@UiField
	TextBox formLastScan;

	@UiField
	Label lbUrlState;

	@UiField
	Label lbScanStatus;

	@UiField
	CheckBox formEnabled;

	@UiField
	HTMLPanel errors;

	// --------------------------- constructor
	@Inject
	EditSiteView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		super.bindSlot(EditSitePresenter.SLOT_ERRORS, this.errors);
	}

	// ------------------------------ public methods
	public void displayRule(int ruleId) {
		this.setValidationState(ValidationState.NONE);
		// this.doGetSite(siteId);
	}

	// -------------------------------- implementing MyView
	@Override
	public void clearDatas() {

		this.form.reset();
		this.focusName();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.formName.setReadOnly(readOnly);
		this.formUrl.setReadOnly(readOnly);
		this.formScanUrl.setReadOnly(readOnly);
		this.formVersion.setReadOnly(readOnly);
		this.formEnabled.setEnabled(!readOnly);
		if (readOnly) {
			this.btValidate.setEnabled(false);
			this.btReset.setEnabled(false);
		}
	}

	@Override
	public void setTitle(String title) {
		this.editSiteTitle.setText(title);
	}

	@Override
	public void setSiteName(String name) {

		this.formName.setText(name);
	}

	@Override
	public void setSiteUrl(String url) {

		this.formUrl.setText(url);
	}

	@Override
	public void setSiteScanUrl(String scanUrl) {

		this.formScanUrl.setText(scanUrl);
	}

	@Override
	public void setSiteVersion(String version) {

		this.formVersion.setText(version);
	}

	@Override
	public void setUrlState(UrlState urlState) {
		StatusUtils.buildLabelUrlState(this.lbUrlState, urlState);
	}

	@Override
	public void setScanStatus(ScanStatus scanStatus) {
		StatusUtils.buildLabelScanStatus(this.lbScanStatus, scanStatus);
	}

	@Override
	public void setLastScan(String lastScan) {
		this.formLastScan.setText(lastScan);
	}

	@Override
	public void setSiteEnabled(boolean enabled) {
		this.formEnabled.setValue(enabled);
	}

	@Override
	public String getSiteName() {
		return this.formName.getText();
	}

	@Override
	public String getSiteUrl() {
		return this.formUrl.getText();
	}

	@Override
	public String getSiteScanUrl() {
		return this.formScanUrl.getText();
	}

	@Override
	public String getSiteVersion() {
		return this.formVersion.getText();
	}

	@Override
	public boolean isSiteEnabled() {
		return this.formEnabled.getValue();
	}

	@Override
	public void setEditing(boolean editing) {

		this.iconEditing.setVisible(editing);
	}

	@Override
	public void setValidationState(ValidationState validationState) {

		StatusUtils.buildLabelValidationState(this.lbValidationState, validationState);

		this.gpName.setValidationState(validationState);
		this.gpUrl.setValidationState(validationState);
		this.gpVersion.setValidationState(validationState);

	}

	@Override
	public void focusName() {
		WidgetUtils.focusWidget(this.formName);
	}

	@Override
	public void enableButtonValidate(boolean enabled) {
		this.btValidate.setEnabled(enabled);
	}

	@Override
	public void enableButtonReset(boolean enabled) {
		this.btReset.setEnabled(enabled);
	}

	@Override
	public boolean isFormValide() {
		return this.form.validate();
	}

	// ---------------------------------- private methods

}