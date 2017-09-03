package fr.tsadeo.app.japicgwtp.shared.util;

import java.util.Objects;

import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.ValidationState;


import fr.tsadeo.app.japicgwtp.shared.domain.ScanStatus;
import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;

public class StatusUtils {

	public static void buildLabelValidationState(Label label, ValidationState validationState) {

		LabelType labelType = LabelType.DEFAULT;
		String text = "";

		switch (validationState) {
		case ERROR:
			labelType = LabelType.DANGER;
			text = "error";
			break;

		case WARNING:
			labelType = LabelType.WARNING;
			text = "warning";
			break;

		case SUCCESS:
			labelType = LabelType.SUCCESS;
			text = "Updated";
			break;

		case NONE:
			labelType = LabelType.PRIMARY;
			break;
		}

		label.setType(labelType);
		label.setText(text);
	}

	public static void buildLabelUrlState(Label label, UrlState urlState) {

		urlState = Objects.isNull(urlState)?UrlState.NoTested:urlState; 
		LabelType labelType = LabelType.DEFAULT;
		label.setText(urlState.getText());

		switch (urlState) {
		case Alive:
		case AliveRestricted:
			labelType = LabelType.SUCCESS;
			break;
		case PageNoFound:
		case IndexNoFound:
		case WrongHost:
			labelType = LabelType.WARNING;
			break;
		case NoTested:
			labelType = LabelType.PRIMARY;
			break;
		case Error:
			labelType = LabelType.DANGER;
			break;

		}
		label.setType(labelType);
	}

	public static void buildLabelScanStatus(Label label, ScanStatus scanStatus) {

		scanStatus = Objects.isNull(scanStatus)?ScanStatus.New:scanStatus;
		LabelType labelType = LabelType.DEFAULT;
		label.setText(scanStatus.getText());

		switch (scanStatus) {

		case Canceled:
			labelType = LabelType.PRIMARY;
			break;

		case Done:
			labelType = LabelType.SUCCESS;
			break;

		case Scanning:
			labelType = LabelType.WARNING;
			break;

		case Error:
			labelType = LabelType.DANGER;
			break;

		case New:
			labelType = LabelType.PRIMARY;
		}
		label.setType(labelType);

	}
}
