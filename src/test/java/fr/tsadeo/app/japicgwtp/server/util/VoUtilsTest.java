package fr.tsadeo.app.japicgwtp.server.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import fr.tsadeo.app.japicgwtp.shared.vo.VoHighlighText;

public class VoUtilsTest {
	
	
	@Test
	public void testBuildVoHighlighText() {
	

		String value = "totoValueBototoBonjourxxx";
		System.out.println("VALUE : " + value);
		
		System.out.println("\nCAS start with search");
		System.out.println("split 'totoValueBotot'o: " +  CollectUtils.tabToString(value.split("totoValueBototo"), '|'));
		VoHighlighText result = VoUtils.buildVoHighlighText("totoValueBototo", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		System.out.println("\nCAS end with");
		System.out.println("split 'xxx': " +  CollectUtils.tabToString(value.split("xxx"), '|'));
	    result = VoUtils.buildVoHighlighText("xxx", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		
		System.out.println("\nCAS token pair start with");
		System.out.println("split 'toto': " +  CollectUtils.tabToString(value.split("toto"), '|'));
	    result = VoUtils.buildVoHighlighText("toto", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		System.out.println("\nCAS token impairs");
		System.out.println("split 'Bo': " + CollectUtils.tabToString(value.split("Bo"), '|'));
		result = VoUtils.buildVoHighlighText("Bo", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		System.out.println("\nCAS search not found");
		System.out.println("split 'titi': " + CollectUtils.tabToString(value.split("titi"), '|'));
		result = VoUtils.buildVoHighlighText("titi", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		System.out.println("\nCAS search == value");
		System.out.println("split 'totoValueBototoBonjourxxx': " + CollectUtils.tabToString(value.split("totoValueBototoBonjourxxx"), '|') );
		result = VoUtils.buildVoHighlighText("totoValueBototoBonjourxxx", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		System.out.println("\nCAS search with lower case");
		System.out.println("split 'bototo': " + CollectUtils.tabToString(value.split("Bototo"), '|') );
		result = VoUtils.buildVoHighlighText("bototo", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		//===========================================================
		value = "ValueBototoBonjourxxxtoto";
		System.out.println("\nVALUE : " + value);
		
		System.out.println("\nCAS token pair end with");
		System.out.println("split 'toto': " +  CollectUtils.tabToString(value.split("toto"), '|'));
	    result = VoUtils.buildVoHighlighText("toto", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		System.out.println("\nCAS token 1 caractere");
		System.out.println("split 'toto': " +  CollectUtils.tabToString(value.split("V"), '|'));
	    result = VoUtils.buildVoHighlighText("V", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
	}
	
	@Test
	public void testBuildVoHighlighText_1Character() {
	

		String value = "AspectJ";
		System.out.println("VALUE : " + value);
		
		
		System.out.println("\nCAS token 1 caractere");
		System.out.println("split 't': " +  CollectUtils.tabToString(value.split("t"), '|'));
		VoHighlighText result = VoUtils.buildVoHighlighText("t", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		value = "Hibernate";
		System.out.println("VALUE : " + value);
		
		System.out.println("\nCAS token 1 caractere");
		System.out.println("split 'e': " +  CollectUtils.tabToString(value.split("e"), '|'));
		 result = VoUtils.buildVoHighlighText("e", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
		
		value = "Java";
		System.out.println("VALUE : " + value);
		
		System.out.println("\nCAS token 1 caractere");
		System.out.println("split 'j': " +  CollectUtils.tabToString(value.split("j"), '|'));
		 result = VoUtils.buildVoHighlighText("j", value);
		assertNotNull("VoHighlighText cannot be null!!",result);
		System.out.println(result);
	}

}
