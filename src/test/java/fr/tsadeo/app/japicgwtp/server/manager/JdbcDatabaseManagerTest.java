package fr.tsadeo.app.japicgwtp.server.manager;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.Test;

public class JdbcDatabaseManagerTest {

	private static final JdbcDatabaseManager databaseManager = JdbcDatabaseManager.get();
	
	@Test
	public void testOpenConnection() throws Exception {
		
		Connection con = databaseManager.getConnection(null);
		assertNotNull("connection cannot be null", con);
		
		databaseManager.closeConnection(con, null);
	}
}
