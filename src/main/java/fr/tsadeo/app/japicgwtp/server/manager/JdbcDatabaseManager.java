package fr.tsadeo.app.japicgwtp.server.manager;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.tsadeo.app.japicgwtp.shared.util.JApicException;

/**
 * UNIQUEMENT POUR TESTER LA CONNECTION JDBC - NON UTILISE DANS L'APPLICATION
 * @author sylvie
 *
 */
public class JdbcDatabaseManager {

	private static final Logger log = Logger.getLogger(JdbcDatabaseManager.class.getName());

	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; //"com.mysql.jdbc.Driver"
	private static final String DB_URL = "jdbc:mysql://localhost/japicollector?useLegacyDatetimeCode=false&serverTimezone=Europe/Paris";

	// Database credentials
	private static final String USER = "japic";
	private static final String PASS = "japic"; 

	private String jdbcDriver;
	private String bddUrl;
	private String user;
	private String pwd;

	private boolean init = false;

	private static JdbcDatabaseManager instance;

	private Map<String, Connection> mapToken2Connection = new HashMap<>();

	public static final JdbcDatabaseManager get() {
		if (instance == null) {
			instance = new JdbcDatabaseManager();
		}
		return instance;
	}

	private JdbcDatabaseManager() {

	}

	// --------------------------------------- package methods
	void init(String jdbcDriver, String bddUrl, String user, String pwd) {

		this.jdbcDriver = jdbcDriver;
		this.bddUrl = bddUrl;
		this.user = user;
		this.pwd = pwd;

		// STEP 2: Register JDBC driver
		try {
			Class.forName(this.jdbcDriver);
			this.init = true;
		} catch (ClassNotFoundException e) {
			log.severe("init Class.forName( " + this.jdbcDriver + "): " + e.getMessage());
		}

	}

	void unregister() throws SQLException {

		Driver driver = DriverManager.getDriver(this.bddUrl);
		if (this.init && driver != null) {
			log.info("Deregistering JDBJ driver " + driver.toString());
			this.init = false;
			DriverManager.deregisterDriver(driver);
		}

	}

	private void initDefault() {
		this.init(JDBC_DRIVER, DB_URL, USER, PASS);
	}

	// --------------------------------------- public methods
	/**
	 * Quand la transaction est terminée appeller
	 * closeAndCommitTransaction()
	 * 
	 * @return
	 * @throws SQLException
	 */
	public void beginTransaction(String token) throws JApicException {

		Connection con = this.mapToken2Connection.get(token);

		try {
			if (con != null && !con.isClosed()) {
				this.rollbackTransaction(token);
				this.mapToken2Connection.remove(token);
				throw new JApicException("Connection already in use!");
			}

			// creation d'une connection dans le cadre d"une transaction
			con = this.openConnection(false);
			this.mapToken2Connection.put(token, con);
			log.info("Connection for transaction " + token + " openned...");
		} catch (SQLException ex) {
			log.severe(ex.getMessage());
			throw new JApicException("Error in beginTransaction(" + token + "): " + ex.getMessage());
		}
	}

	/*
	 * retourne une nouvelle connection ou une connection en cours si token non null
	 */
	public Connection getConnection(String token) throws SQLException, JApicException {

		// cas standard hors transaction
		if (token == null) {
			return this.openConnection(true);
		}

		Connection con = this.mapToken2Connection.get(token);
		if (con == null) {
			throw new JApicException("Unable to retrieve an existing connection!");
		}
		if (con.isClosed()) {
			this.mapToken2Connection.remove(token);
			throw new JApicException("Connection already closed!");
		}

		// connection existante dans le cadre d'une transaction
		return con;
	}

	private Connection openConnection(boolean autoCommit) throws SQLException {

		if (!init) {
			this.initDefault();
		}

		// STEP 3: Open a connection
		try {
			Connection con = DriverManager.getConnection(this.bddUrl, this.user, this.pwd);
			con.setAutoCommit(autoCommit);
			return con;
		} catch (SQLException ex) {
			log.finest("openConnection(): " + ex.getMessage());
			throw ex;
		}
	}

	// rollback d'une connection dans le cadre d'une transaction
	public void rollbackTransaction(String token) throws JApicException {

		try {
			Connection con = this.getAndVerifyConnection(token);
			con.rollback();
			con.close();
			log.warning("...transaction " + token + " rollbacked!");
			this.mapToken2Connection.remove(token);
		} catch (SQLException ex) {
			throw new JApicException("Error in rollbackTransaction(" + token + "): " + ex.getMessage());
		}
	}

	/*
	 * Fermerture d'une connection seulement hors contexte transactionnel (token null)
	 */
	public void closeConnection(Connection con, String token) {

		if (token == null && con != null) {

			try {
				if (con != null) {
					con.close();
					log.finest("...connection closed.");
				}

			} catch (SQLException ignored) {
				ignored.printStackTrace();
			}
		}
	}

	/*
	 * Commit d'une transaction en cours
	 */
	public void closeAndCommitTransaction(String token) throws JApicException {

		try {
			Connection con = this.getAndVerifyConnection(token);
			con.commit();
			con.close();
			log.info("...connection for transaction " + token + " closed.");
			this.mapToken2Connection.remove(token);
		} catch (SQLException ex) {
			throw new JApicException("Error in closeAndCommitTransaction(" + token + "): " + ex.getMessage());
		}
	}

	private Connection getAndVerifyConnection(String token) throws SQLException, JApicException {
		Connection con = this.mapToken2Connection.get(token);
		if (con == null || con.isClosed()) {
			this.mapToken2Connection.remove(token);
			throw new JApicException("Transaction null or already closed!");
		}
		return con;
	}
}
