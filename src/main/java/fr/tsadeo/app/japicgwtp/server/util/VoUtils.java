package fr.tsadeo.app.japicgwtp.server.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import fr.tsadeo.app.japicgwtp.shared.util.ValueHelper;
import fr.tsadeo.app.japicgwtp.shared.vo.VoHighlighText;
import fr.tsadeo.app.japicgwtp.shared.vo.VoHighlighText.VoToken;

public class VoUtils {

	public static VoHighlighText buildVoHighlighText(String search, String value) {
		
		VoHighlighText vo = _buildVoHighlighText(search, value);
		if (Objects.nonNull(vo) && vo.isValueNotFound()){
			
			// meme recherche with lowerCase
			vo = _buildVoHighlighText(search.toLowerCase(), value.toLowerCase());
			if (vo.isValueNotFound()) {
				return vo;
			} else {
				return replaceWithInitialText(vo, search, value);
			}
		}
		else {
			return replaceWithInitialText(vo, search, value);
		}
	}
	
	private static VoHighlighText replaceWithInitialText(VoHighlighText vo, String search, String value) {
		if (Objects.isNull(search)|| Objects.isNull(value)) {
			return vo;
		}
		
		// remettre les tokens d'origine avec la bonne casse
		VoHighlighText voFinal = new VoHighlighText();
		int searchLength = search.length();
		int index = 0;
		for (VoToken token : vo.getListTokens()) {
			if(token.isHighlighted()) {
				String tokenHighligthed = value.substring(index, index + searchLength);						
				voFinal.addHighlightedToken(tokenHighligthed);
				index += searchLength;
			} else {
				int length = token.getText().length();
				String tokenFinal = value.substring(index, index + length);						
				voFinal.addToken(tokenFinal);
				index += length;
			}
		}
		return voFinal;
	}
	private static VoHighlighText _buildVoHighlighText(String search, String value) {
		

		if (Objects.isNull(search) || Objects.isNull(value)) {
			return null;
		}

		VoHighlighText vo = new VoHighlighText();
		vo.setValueNotFound(false);
		
		// si search == value tout hightlight
		if (search.equals(value)) {
			vo.addHighlightedToken();
		} else {

			String[] tabTokens = value.split(search);
			List<String> tokens = Arrays.stream(tabTokens)
					  .filter(t -> ! ValueHelper.isStringEmptyOrNull(t))
					  .collect(Collectors.toList());
					   
			int size = tokens.size();
			if (tokens == null || size == 0) {
				vo.setValueNotFound(true);
				vo.addToken(value);
			} else {

				// un seul token (XX - token ou token - XX ou value (search not found))
				if (size == 1) {

					if (value.startsWith(search)) {
						vo.addHighlightedToken();
						vo.addToken(tokens.get(0));
					} else if (value.endsWith(search)) {
						vo.addToken(tokens.get(0));
						vo.addHighlightedToken();
					} else {
						// not found
						vo.setValueNotFound(true);
						vo.addToken(value);
					}
				} else { // nominale case
					
					
					if (value.startsWith(search)) {
						vo.addHighlightedToken();
					}
					// sequence standard
					IntStream.range(0, size)
					      .forEachOrdered(i -> {
								vo.addToken(tokens.get(i));
								boolean last = i == size - 1;
								if (!last) {
									vo.addHighlightedToken();
								}
					      });
		
					if (value.endsWith(search)) {
						vo.addHighlightedToken();
					}
					
				}

			}
		}
		return vo;
	}
}
