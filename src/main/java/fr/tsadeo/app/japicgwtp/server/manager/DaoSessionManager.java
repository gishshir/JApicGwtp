/*
 * Factory de Session Hibernate
 * Singleton crée automatiquement au chargement de l'ApplicationContext de Spring
 */
package fr.tsadeo.app.japicgwtp.server.manager;

import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Convenient class to wrapp hibernate session utilisation
 * 
 * @author sylvie
 */
@Service
public class DaoSessionManager {

	private static Log LOG = LogFactory.getLog(DaoSessionManager.class);

	// Bean de HibernateAppConfig
	@Autowired
	private SessionFactory sessionFactory;

	@PostConstruct
	public void logPostProcessing() {
		LOG.info("@PostConstruct for DaoSessionManager!");
	}

	// ------------------------------------------ public methods
	// maintient une session unique dans un thread or conversation
	public Session getCurrentSession() {

		this.verifySessionFactory();
		return this.sessionFactory.getCurrentSession();
	}

	public Session beginTransaction() {

		Session session = this.getCurrentSession();
		session.getTransaction().begin();
		return session;
	}


	public Session beginManualTransaction() {

		return this.beginTransaction(true);
	}

	public void endReadOnlyTransaction() {
		this.commitTransaction(true);
	}

	public void commitTransaction() {

         this.commitTransaction(false);
	}

	public void rollbackTransaction() {
		this.getCurrentSession().getTransaction().rollback();
	}

	public Query createQuery(String query) {
		return this.getCurrentSession().createQuery(query);
	}

	// --------------------------------------- private methods
	private void commitTransaction(boolean readOnly) {
		if (!readOnly && this.getCurrentSession().getFlushMode() == FlushMode.MANUAL) {
			this.getCurrentSession().flush();
		}
		this.getCurrentSession().getTransaction().commit();
	}
	private Session beginTransaction(boolean manual) {

		Session session = this.getCurrentSession();
		if (manual) {
			session.setFlushMode(FlushMode.MANUAL);
		}
		session.getTransaction().begin();

		return session;
	}
	void verifySessionFactory() {

		if (Objects.isNull(this.sessionFactory)) {
			throw new RuntimeException("SessionFactory not initialized!");
		}
	}

	// ----------------------------------- SessionFactory init and destroy
	@PreDestroy
	private void destroy() {

		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

}
