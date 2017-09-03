/*
 * AOP transactional
 */
package fr.tsadeo.app.japicgwtp.server.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import fr.tsadeo.app.japicgwtp.server.manager.DaoSessionManager;

/**
 *
 * @author sylvie
 */
@Aspect
@Component
public class TransactionAspect implements Ordered {

	private static final Log LOG = LogFactory.getLog(TransactionAspect.class);

	public enum TransactionMode {
		auto, manual, readonly
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface AppTransactional {
		TransactionMode value();
	}

	@Autowired
	private DaoSessionManager sessionManager;

	// ------------------------ implementing Ordered
	@Override
	public int getOrder() {
		return 500;
	}
	// =============================================
	// ADVICES
	// =============================================

	@Around("fr.tsadeo.app.japicgwtp.server.aspect.SystemArchitecture.inServiceLayer() &&"
			+ "@annotation(transactional)")
	public Object doManageTransaction(ProceedingJoinPoint jp, AppTransactional transactional) throws Throwable {

		TransactionMode mode = transactional.value();
		LOG.info("BEGIN transaction ...." + mode);
		switch (mode) {
		case readonly:
		case manual:
			this.sessionManager.beginManualTransaction();
			break;

		default:
			this.sessionManager.beginTransaction();
			break;
		}

		try {
			Object retValue = jp.proceed();

			switch (mode) {
			case readonly:
				this.sessionManager.endReadOnlyTransaction();
				break;

			default:
				this.sessionManager.commitTransaction();
				break;
			}

			LOG.info("... COMMIT transaction.");
			return retValue;

		} catch (Throwable ex) {
			LOG.error("ROLLBACK in method: " + jp.getSignature());
			this.sessionManager.rollbackTransaction();
			throw ex;
		}

	}

}
