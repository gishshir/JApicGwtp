package fr.tsadeo.app.japicgwtp.server.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.SSLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.shared.domain.UrlState;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;

@Service
public class URLManager implements IManager {

	private final static Log LOG = LogFactory.getLog(URLManager.class);
	
	private final static String HEADER_XFRAME_OPTION = "X-Frame-Options";
	private final static String XFRAME_OPTION_DEBY = "DENY";
	private final static String XFRAME_OPTION_SAME_ORIGIN = "SAMEORIGIN";
	private final static String XFRAME_OPTION_ALLOW_FROM = "ALLOW-FROM";
	
	public final static String HEADER_USER_AGENT_KEY = "User-Agent";
	public final static String HEADER_USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
	
	/*
	 * Using X-Frame-Options

There are three possible values for X-Frame-Options:

DENY
    The page cannot be displayed in a frame, regardless of the site attempting to do so.
SAMEORIGIN
    The page can only be displayed in a frame on the same origin as the page itself.
ALLOW-FROM uri
    The page can only be displayed in a frame on the specified origin. 
	 */
	
	
	public UrlState getUrlState(Resource resource) throws JApicException {

		UrlState urlState = UrlState.NoTested;
		if (resource == null) {
			return UrlState.Error;
		}

		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection con = null;
		try {
			LOG.info("URL to Test: " + resource.getURL().toExternalForm());
			con = (HttpURLConnection) resource.getURL().openConnection();
			con.setConnectTimeout(200);
			con.addRequestProperty(HEADER_USER_AGENT_KEY, HEADER_USER_AGENT_VALUE);

			con.setRequestMethod("HEAD");
			

			int responseCode = con.getResponseCode();
			LOG.info("Respons code: " + responseCode);

			switch (responseCode) {
			case HttpURLConnection.HTTP_NOT_FOUND:
				urlState = UrlState.PageNoFound;
				break;

			case HttpURLConnection.HTTP_OK:
				String xFrameOption = con.getHeaderField(HEADER_XFRAME_OPTION);
				urlState = Objects.nonNull(xFrameOption)?UrlState.AliveRestricted:UrlState.Alive;
				break;
				
				default: urlState = UrlState.Error;
				break;
			}


		} catch (FileNotFoundException e) {
			LOG.warn("PAGE NOT FOUND:" + e.toString());
			urlState = UrlState.PageNoFound;
		}
		catch (SSLException | SocketTimeoutException e) {
			LOG.warn("HOST NOT FOUND:" + e.toString());
			urlState = UrlState.WrongHost;
		}
		catch (IOException e) {
			LOG.error("URL CONNECT ERROR:" + e.toString());
			urlState = UrlState.Error;
		} finally {
			if (Objects.nonNull(con)) {
				con.disconnect();
			}
		}

		return urlState;
	}
	
	
	

	public boolean isURLAlive(URL url) {

		boolean result = false;
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection con = null;
		InputStream in = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(200);
			con.addRequestProperty(HEADER_USER_AGENT_KEY, HEADER_USER_AGENT_VALUE);

			LOG.info("HEAD request...");
			con.setRequestMethod("HEAD");
			//
			int responseCode = con.getResponseCode();
			LOG.info("Respons code: " + responseCode);
			//
			// in = con.getInputStream();
			result = responseCode == HttpURLConnection.HTTP_OK;

			// return true;

		} catch (FileNotFoundException e) {
			LOG.warn("PAGE NOT FOUND:" + e.toString());
		} catch (IOException e) {
			LOG.warn("HOST NOT FOUND:" + e.toString());
		} finally {

			if (Objects.nonNull(in)) {
				try {
					in.close();
				} catch (IOException ignored) {
				}
			}
			if (Objects.nonNull(con)) {
				con.disconnect();
			}
		}

		return result;
	}

}
