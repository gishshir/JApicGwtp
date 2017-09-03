package fr.tsadeo.app.japicgwtp.client.util;

import java.util.Objects;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Focusable;

import fr.tsadeo.app.japicgwtp.shared.vo.VoHighlighText;
import fr.tsadeo.app.japicgwtp.shared.vo.VoHighlighText.VoToken;

public class WidgetUtils {

	public static void focusWidget(final Focusable widget) {

		if (widget == null) {
			return;
		}
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				widget.setFocus(true);
			}
		});
	}

	// <token1><delimit><token2><delimit>....<tokenN>
	public static String buildHighlightedHtml(final String labelText, final VoHighlighText voText,
			final String delimitToHighlight) {

		StringBuilder sb = new StringBuilder();

		if (Objects.nonNull(voText)) { // seuls delimiter surlignes

			int size = voText.getListTokens().size();
			for (int i = 0; i < size; i++) {

				VoToken token = voText.getListTokens().get(i);

				if (token.isHighlighted()) {
					sb.append ("<mark>").append(token.getText()).append("</mark>");
				} else {
					sb.append(token.getText());
				}

			}

		} else {
			sb.append(labelText);
		}

		return sb.toString();
	}

}
