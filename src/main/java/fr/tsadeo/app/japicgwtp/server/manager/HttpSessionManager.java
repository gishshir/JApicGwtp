package fr.tsadeo.app.japicgwtp.server.manager;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Cree auto par le container de servlet
 * @author sylvie
 */
public class HttpSessionManager implements HttpSessionListener {

	private final static Log LOG = LogFactory.getLog(HttpSessionManager.class);

	// -------------------------- implementing HttpSessionListener
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		final String sessionId = event.getSession().getId();
		LOG.info("Session created: " + sessionId);
		SessionSubscription.get().sessionCreated(sessionId);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		final String sessionId = event.getSession().getId();
		LOG.info("Session destroyed: " + sessionId);

		SessionSubscription.get().sessionDestroyed(sessionId);
	}

	// --------------------------------------- private methods

	// ======================================== INNER CLASS
        
        /**
         * Propagate session event to all listeners
         */
	public static class SessionSubscription implements IJApicSessionListener {

		// ----------------------------- singleton
		private static SessionSubscription instance;

		public static SessionSubscription get() {
			if (instance == null) {
				instance = new SessionSubscription();
			}
			return instance;
		}

		private SessionSubscription() {
		}

		// --------------------------------------

		private final Set<IJApicSessionListener> sessionListeners = new HashSet<>();

		// --------------------------------------- public method
		public void addSessionListener(IJApicSessionListener listener) {
			this.sessionListeners.add(listener);
		}

		public void removeSessionListener(IJApicSessionListener listener) {
			this.sessionListeners.remove(listener);
		}

		// ------------------- implementing JunitHistorySessionListener
		@Override
		public void sessionCreated(String sessionId) {

                    sessionListeners.stream()
                            .forEach(l -> l.sessionCreated(sessionId));
		}

		@Override
		public void sessionDestroyed(String sessionId) {
                    sessionListeners.stream()
                            .forEach(l -> l.sessionDestroyed(sessionId));
		}

	}

	public interface IJApicSessionListener {

		public void sessionCreated(String sessionId);

		public void sessionDestroyed(String sessionId);
	}

}
