package fr.tsadeo.app.japicgwtp.server.manager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.server.manager.scan.FileType;
import fr.tsadeo.app.japicgwtp.server.manager.scan.PathInfo;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;

/**
 * Manager pour l'extraction des informations d'un site web API
 * 
 * @author sylvie
 *
 */
@Service
public class ScanManager implements IManager {

	private final static Log LOG = LogFactory.getLog(ScanManager.class);

//	/**
//	 * Parsing de chacune de pages html et construction d'une liste de TypeInfo
//	 * contenant les information du type
//	 * 
//	 * @param listPathInfos
//	 * @return
//	 */
//	public List<TypeInfo> buildListTypeInfos(Resource mainPage, List<PathInfo> listPathInfos) throws JApicException {
//
//		try {
//			String basePath = mainPage.getURL().toExternalForm();
//			String simpleBasePath = StringUtils.removeEnd(basePath, "/");
//
//			// pour chaque nom >> page html >> TypeInfo (class, package,
//			// declaration, members, etc..)
//			List<TypeInfo> typeInfos = new ArrayList<>();
//
//			FileParser parser = new FileParser();
//
//			ForkJoinPool commonPool = ForkJoinPool.commonPool();
//			LOG.info("commonPool.getParallelism(): " + commonPool.getParallelism());
//
//			listPathInfos.parallelStream()
//
//					.map((PathInfo pathInfo) -> {
////						System.out.println(Thread.currentThread().getName() + " - " + pathInfo.toString());
//						return parser.parseHtml(pathInfo, simpleBasePath);
//					}).filter((Optional<TypeInfo> o) -> o.isPresent())
//					 .limit(10) //POUR TESTS
//					.forEach(o -> {
//						typeInfos.add(o.get());
//					});
//
//			return typeInfos;
//
//		} catch (IOException e) {
//			LOG.error("Error in buildListTypeInfos(): " + e.toString());
//			throw new JApicException("Echec when parsing - " + e.getMessage());
//		}
//	}

	/**
	 * Récupère dans la page d'index toutes les classes/interfaces/enum et leur
	 * ref web
	 * 
	 * @param url
	 * @return list de PathInfo: information sur la page html représentant un
	 *         element de l'API
	 * @throws IOException
	 */
	public List<PathInfo> getPathInfosFromIndex(Resource resource) throws JApicException {

		final String baliseOld = ".indexContainer li a";
		final String baliseNew = "table tr td a";
		try {
			URL url = resource.getURL();
			Document document = Jsoup.connect(url.toExternalForm()).userAgent(URLManager.HEADER_USER_AGENT_VALUE).get();
			LOG.info("title of doc: " + document.title());

			List<PathInfo> list = this.process(document, baliseOld);
			if (list.isEmpty()) {
				list = this.process(document, baliseNew);
			}
			return list;

		} catch (IOException e) {
			LOG.error("Error in getPathInfosFromIndex(): " + e.toString());
			throw new JApicException("Echec when parsing " + resource.toString() + " - " + e.getMessage());
		}

	}
	
	private List<PathInfo> process(Document document, String balise) {
	
		return document.body().select(balise)
				.stream()
//				.filter((link) -> StringUtils.startsWith(link.attr("href"), "org"))
				.map((link) -> {
					String href = link.attr("href");
					String title = link.attr("title");
					FileType fileType = FileType.ofTitle(title);
					String packageName = getPackageName(fileType, title);
					String className = link.text();
					return new PathInfo(href, packageName, className, fileType);
				})
				.filter(p -> p.getFileType() != FileType.UNKNOWN)
				.collect(Collectors.toList());
	}

	// ------------------------------------------- private methods
	private String getPackageName(FileType fileType, String title) {

		switch (fileType) {

		case ANNOTATION:
		case CLASS:
		case ENUM:
		case INTERFACE:
			return title.substring(fileType.getLength() + 4);

		default:
			return title;
		}
	}
}
