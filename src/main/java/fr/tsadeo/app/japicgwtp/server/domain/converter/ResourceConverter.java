/*
 * Permet le stockage d'objet Resource en base sous forme de String
 */
package fr.tsadeo.app.japicgwtp.server.domain.converter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

/**
 *
 * @author sylvie
 */
@Component
@Converter
public class ResourceConverter implements AttributeConverter<Resource, String>, IResourceConverter {

    private static final Log LOG = LogFactory.getLog(ResourceConverter.class);

    //------------------------------------ implementing IResourceConverter
    @Override
    public boolean valideUrl(String url) {
        return this.toResource(url) != null;
    }

    @Override
    public String toUrl(Resource resource) {
    	
        String url = null;
        try {
            url = Objects.isNull(resource)?null: this.getUrl(resource);
        } catch (IOException ex) {
            LOG.error("Error getting url from Resource!", ex);
        }
        return url;
    }
    @Override
    public Resource toResource(String url) {
        Resource resource = null;
        try {
            // FIXME use Spring ResourceLoader
            resource = StringUtils.isEmpty(url)?null:new UrlResource(url);

        } catch (MalformedURLException ex) {
            LOG.error("Error building UrlResource from url!", ex);
        }
        return resource;
    }

    //---------------------------------- implementing AttributeConverter
    @Override
    public String convertToDatabaseColumn(Resource resource) {

        return this.toUrl(resource);

    }

    @Override
    public Resource convertToEntityAttribute(String url) {
    	 if (StringUtils.isEmpty(url)) {
         	return null;
         }
        return this.toResource(url);
    }

//------------------------------------- private methods
    private String getUrl(Resource resource) throws IOException {

        return resource == null ? null : resource.getURL().toExternalForm();
    }


}
