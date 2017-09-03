package fr.tsadeo.app.japicgwtp.server.manager.scan;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class FileParser {
	
	private final static Log LOG = LogFactory.getLog(FileParser.class);


    public Optional<TypeInfo> parseHtml(PathInfo pathInfo,  String basePath) {

        String url = basePath + "/" + pathInfo.getPath();
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException ex) {
            LOG.error("url parsing error: " + ex.getMessage());
            return Optional.empty();
        }

        Optional<TypeInfo> result = null;
        try {
            result = getTypeInfo(document, pathInfo);
        } catch (Exception e) {
            result = Optional.empty();
        }
        return result;
    }

   
    
    private String getSinceVersion(Element body) {
        
       String version = null;
       try {
       List<String> versions = body.select(".contentContainer .description dl dt")
                .stream()
                .filter((Element dt) ->                  
                    
                    dt.getAllElements()
                         .stream()
                             .anyMatch((Element elt) -> elt.text().equals("Since:"))

                
                    )
                    .map ((Element dt) -> dt.nextElementSibling().text())
                    .collect(Collectors.toList());
       
          version = (Objects.nonNull(versions) && versions.size() == 1)?versions.get(0):null;
       


       }
       catch (RuntimeException ex) {
           LOG.warn("unable to retrieve version: " + ex.getMessage());
       }
        return version;       
    }
    

    private Optional<TypeInfo> getTypeInfo(Document document, PathInfo pathInfo) {

        TypeInfo typeInfo = new TypeInfo();

        Element body = document.body();

        typeInfo.setSinceVersion(this.getSinceVersion(body));
        FileType fileType = pathInfo.getFileType();
               typeInfo.setFileType(fileType);

        // on decrit tous les details
        String title = document.title();
        String typeName = StringUtils.substringBefore(title, " ");


        //<div class="subTitle">java.util.stream</div>
        String packageName = body
                .select(".header > .subTitle")
                .last()
                .text();

        //<pre>public interface <span class="typeNameLabel">Stream&lt;T&gt;</span>
        String declaration = body
                .select(".description > ul > li > pre")
                .first()
                .html();

        typeInfo.setPackageName(packageName);
        typeInfo.setPath(pathInfo.getPath());
        typeInfo.setDeclaration(declaration);

        typeInfo.setName(typeName);
        



        return Optional.of(typeInfo);
    }
}
