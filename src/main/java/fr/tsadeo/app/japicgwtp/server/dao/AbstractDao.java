/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.hibernate.LockMode;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import fr.tsadeo.app.japicgwtp.server.domain.IModelItem;
import fr.tsadeo.app.japicgwtp.server.manager.DaoSessionManager;
import fr.tsadeo.app.japicgwtp.server.util.CollectUtils;
import fr.tsadeo.app.japicgwtp.shared.util.JApicException;
import fr.tsadeo.app.japicgwtp.shared.vo.VoId;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdName;
import fr.tsadeo.app.japicgwtp.shared.vo.VoIdUtils;

/**
 *
 * @author sylvie
 * @param <T>
 */
public abstract class AbstractDao<T extends IModelItem> implements IDao {

	@Autowired
	protected DaoSessionManager sessionManager;

	protected abstract Class<T> getClassItem();

	protected abstract String getHQLListAll();

	// ------------------------------------ implementing IItemDao
	/**
	 * Get a list of all items
	 * 
	 * @return
	 */
	public List<T> listAll() {

		return this.listItems(this.getHQLListAll(), null);
	}

	/**
	 * Get a persistent object from the current session
	 *
	 * @param id
	 * @param withDatas
	 *            (all EAGER datas initialized) never a proxy
	 * @return null if not exists!
	 */
	public T getById(Long id, boolean withDatas) {

		Class<T> clazz = this.getClassItem();
		T item;
		Session session = this.sessionManager.getCurrentSession();

		if (withDatas) {
			// return null if doesn't exist!
			item = session.byId(clazz).load(id);
		} else {

			try {
				// can be a proxy with un-initialized datas
				// throw an exception if doesn't exist!
				item = session.byId(clazz).getReference(id);
				item.getId(); // pour déclencher l'exception
			} catch (Exception ex) {
				getLog().warn("Item id:" + id + " doesn't exist!");
				item = null;
			}
		}
		return item;

	}

	// ===================================================
	// ATTACH IN SESSION
	// ==================================================

	public void attachUnmodifiedItem(T item) {

		Session session = this.sessionManager.getCurrentSession();
		if (Objects.isNull(item) || session.contains(item)) {
			// already attached and persistent
			return; // nothing to do
		}

		session.lock(item, LockMode.NONE);
	}

	public void attachAndSave(T item) {

		Session session = this.sessionManager.getCurrentSession();
		if (Objects.isNull(item) || session.contains(item)) {
			// already attached and persistent) {
			return; // nothing to do
		}

		if (!VoIdUtils.isIdUndefined(item.getId())) {
			throw new RuntimeException("Item must be transient for save!");
		}

		session.save(item);

	}

	public void attachAndUpdate(T item) {

		Session session = this.sessionManager.getCurrentSession();
		if (Objects.isNull(item) || session.contains(item)) {
			// already attached and persistent) {
			return; // nothing to do
		}

		if (VoIdUtils.isIdUndefined(item.getId())) {
			throw new RuntimeException("Item cannot be transient for update!");
		}

		session.update(item);

	}

	// public void mergeAndUpdateItem(T item) {
	//
	// Session session = this.sessionManager.getCurrentSession();
	// if (Objects.isNull(item) || session.contains(item)) {
	// // already attached and persistent) {
	// return; // nothing to do
	// }
	//
	// if (VoIdUtils.isIdUndefined(item.getId())) {
	// throw new RuntimeException("The item cannot be transient!");
	// }
	// session.merge(item);
	// }

	public void delete(T item) {
		this.sessionManager.getCurrentSession().delete(item);
	}

	// ------------------------------------ protected methods

	@PostConstruct
	public void logPostConstruct() throws JApicException {
		getLog().info("@PostConstruct of " + this.getClass().getName());

	}

	protected T getByNaturalId(String key, Object value, boolean withDatas) {

		return this.getByNaturalId(CollectUtils.buildMapWithOneItem(key, value), withDatas);
	}

	protected T getByNaturalId(Map<String, Object> keyvalues, boolean withDatas) {

		Class<T> clazz = this.getClassItem();
		T item = null;
		Session session = this.sessionManager.getCurrentSession();
		NaturalIdLoadAccess<T> natIdLoadAccess = session.byNaturalId(clazz);
		for (String key : keyvalues.keySet()) {
			natIdLoadAccess = natIdLoadAccess.using(key, keyvalues.get(key));
		}
		if (withDatas) {
			item = natIdLoadAccess.load();
		} else {

			try {
				// can be a proxy with un-initialized datas
				// throw an exception if doesn't exist!
				item = natIdLoadAccess.getReference();
			} catch (RuntimeException ex) {
				getLog().warn("Item [" + keyvalues.toString() + "] doesn't exist!");
			}
		}

		return item;
	}

	@SuppressWarnings("unchecked")
	protected List<T> listItemsWithProcedure(String procName, Map<String, Object> bindings) {
		getLog().info("PROC: " + procName);
		Query query = this.sessionManager.getCurrentSession().getNamedQuery(procName);

		if (bindings != null) {
			bindings.forEach((key, profile) -> query.setParameter(key, profile));
		}

		return query.list();
	}

	protected List<T> listItems(String hql, Map<String, Object> bindings) {

		return this.list(hql, bindings);
	}

	protected List<VoIdName> listIdNames(String hql, Map<String, Object> bindings) {

		return this.list(hql, bindings);
	}

	@SuppressWarnings("unchecked")
	private <V> List<V> list(String hql, Map<String, Object> bindings) {
		getLog().info("HQL: " + hql);

		Query query = this.sessionManager.createQuery(hql);

		if (bindings != null) {
			bindings.forEach((key, profile) -> query.setParameter(key, profile));
		}
		return query.list();

	}

	protected String likeContent(String content) {
		if (content.startsWith(SPACE)) {
			return content.trim().concat(POURCENT);
		}
		if (content.endsWith(" ")) {
			return POURCENT.concat(content.trim());
		}
		return POURCENT.concat(content).concat(POURCENT);
	}

}
