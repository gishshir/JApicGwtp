package fr.tsadeo.app.japicgwtp.server.manager;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import fr.tsadeo.app.japicgwtp.shared.domain.UserProfile;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoUser;

@Service
public class ProfileManager implements IManager {

	private final static Log LOG = LogFactory.getLog(ProfileManager.class);

	@PostConstruct
	public void logPostConstruct() throws JApicException {
		LOG.info("@PostConstruct of ProfileManager");
	}

	// ---------------------------------- public methods
	public void setSessionUser(VoUser user, HttpSession session) {
		session.setAttribute(KEY_SESSION_USER, user);
	}

	/*
	 * Récupère le UserProfile stocké en session si existe sinon null
	 */
	public VoUser getSessionUser(HttpSession session) {

		return (session == null) ? null : (VoUser) session.getAttribute(KEY_SESSION_USER);
	}

	public boolean isSessionAtLeastAdmin(HttpSession session) {
		return this.isSessionAtLeastUserProfile(UserProfile.Admin, session);
	}

	public boolean isSessionAtLeastManager(HttpSession session) {
		return this.isSessionAtLeastUserProfile(UserProfile.Manager, session);
	}

	public boolean isSessionAtLeastUser(HttpSession session) {
		return this.isSessionAtLeastUserProfile(UserProfile.Authenticated, session);
	}

	/*
	 * Verifie si la session existe avec profil de droit >= profil
	 */
	public boolean isSessionAtLeastUserProfile(UserProfile minProfile, HttpSession session) {

		final VoUser voUser = this.getSessionUser(session);
		return voUser == null?false:minProfile.canAccess(voUser.getProfile());
	}

}
