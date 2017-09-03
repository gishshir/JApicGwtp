package fr.tsadeo.app.japicgwtp.server.domain;

import java.time.Instant;

import javax.persistence.Version;

public abstract class AbstractModelVersionItem extends AbstractModelItem {

	
	private static final long serialVersionUID = 1L;
	
	// Hibernate internal : optimistic locking
	@Version
	private Instant ts;
}
