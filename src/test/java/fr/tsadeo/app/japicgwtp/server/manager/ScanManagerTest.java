package fr.tsadeo.app.japicgwtp.server.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.core.io.Resource;

import fr.tsadeo.app.japicgwtp.server.AbstractTest;
import fr.tsadeo.app.japicgwtp.server.domain.converter.ResourceConverter;
import fr.tsadeo.app.japicgwtp.server.manager.scan.PathInfo;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;

public class ScanManagerTest extends AbstractTest {

	private final static Log LOG = LogFactory.getLog(ScanManagerTest.class);
	
	private final static String MAIN_SLF4J = "http://www.slf4j.org/api/";
	private static final String INDEX_OK = "allclasses-frame.html";

	private static final ScanManager manager = new ScanManager();

	private static ResourceConverter resourceConverter = new ResourceConverter();

	@Test
	public void testGetPathInfosFromIndex() throws Exception {

		Resource resource = resourceConverter.toResource(URLManagerTest.PAGE_OK);
		List<PathInfo> result = manager.getPathInfosFromIndex(resource);
		logAndAssertListPathInfos(result, false);

	}

	@Test
	public void testGetPathInfosFromIndexNotAPI() throws Exception {

		Resource resource = resourceConverter.toResource("https://jsoup.org/cookbook/input/load-document-from-url");
		List<PathInfo> result = manager.getPathInfosFromIndex(resource);
		logAndAssertListPathInfos(result, true);

	}
	
	@Test
	public void testtestGetPathInfosFromIndexSlf4j() throws Exception {
		
		Resource resource = resourceConverter.toResource("http://www.slf4j.org/api/allclasses-frame.html");
		List<PathInfo> result = manager.getPathInfosFromIndex(resource);
		logAndAssertListPathInfos(result, false);

	}

	@Test(expected = JApicException.class)
	public void testGetPathInfosFromWrongIndex() throws Exception {

		Resource resource = resourceConverter.toResource(URLManagerTest.PAGE_WRONG);
		List<PathInfo> result = manager.getPathInfosFromIndex(resource);

	}

//	@Test
//	public void testBuildListTypeInfos() throws Exception {
//
//		Resource resource = resourceConverter.toResource(URLManagerTest.PAGE_OK);
//		List<PathInfo> listPaths = manager.getPathInfosFromIndex(resource);
//		
//		Resource mainPage = resourceConverter.toResource(URLManagerTest.HOST_OK);
//		List<TypeInfo> result = manager.buildListTypeInfos(mainPage, listPaths);
//		
//		this.logAndAssertListTypeInfos(result, false);
//		
//	}
//	
//	@Test
//	public void testBuildListTypeInfosSlf4j() throws Exception {
//
//		Resource resource = resourceConverter.toResource(MAIN_SLF4J + INDEX_OK);
//		List<PathInfo> listPaths = manager.getPathInfosFromIndex(resource);
//		
//		Resource mainPage = resourceConverter.toResource(MAIN_SLF4J);
//		List<TypeInfo> result = manager.buildListTypeInfos(mainPage, listPaths);
//		
//		this.logAndAssertListTypeInfos(result, false);
//		
//	}

	// -------------------------------------- private methods

	
//	private void logAndAssertListTypeInfos(List<TypeInfo> list, boolean empty) {
//		assertNotNull("list of TypeInfo cannot be null!", list);
//
//		assertEquals("List must be " + (empty ? "empty!" : "not empty!"), empty, list.isEmpty());
//
//		IntStream.range(0, list.size())
//		          .forEachOrdered(index -> this.logAndAssert(list.get(index), index));
//	}
//	private void logAndAssert(TypeInfo typeInfo, int index) {
//
//		assertNotNull("typeInfo cannot be null!", typeInfo);
//		LOG.info("[" + index + "] " + typeInfo);
//	}
	
	
	private void logAndAssertListPathInfos(List<PathInfo> list, boolean empty) {
		assertNotNull("list of PathInfo cannot be null!", list);

		assertEquals("List must be " + (empty ? "empty!" : "not empty!"), empty, list.isEmpty());
		
		IntStream.range(0, list.size())
		          .forEachOrdered(index -> this.logAndAssert(list.get(index), index));
	}

	private void logAndAssert(PathInfo pathInfo, int index) {

		assertNotNull("pathInfo cannot be null!", pathInfo);
		super.logWithIndex(pathInfo.toString(), index);
	}

}
