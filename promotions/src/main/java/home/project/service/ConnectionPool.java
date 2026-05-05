package home.project.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;
import home.project.logger.ManagerLogger;

public class ConnectionPool {

	private static ConnectionPool connectionPool;
	
	private String jdbcURL;
	private String userName;
	private String password;
	private int preconnectCount;
	private int connectCount;
	private int maxIdleConnections;
	private int maxConnections;
	private Vector<Connection> usedConnections;
	private Vector<Connection> freeConnections;
	
	public static ConnectionPool getConnectionPool() {
		return connectionPool;
	}
	
	static {
	    try {
	        Properties properties = new Properties();
	        
	        try (InputStream input = ConnectionPool.class.getClassLoader().getResourceAsStream("config.properties")) {
	        	properties.load(input);
	        }

	        String jdbcURL = properties.getProperty("jdbcURL");
	        String username = properties.getProperty("username");
	        String password = properties.getProperty("password");
	        String driver = properties.getProperty("driver");
	        int preconnectCount = Integer.parseInt(properties.getProperty("preconnectCount", "0"));
	        int maxIdleConnections = Integer.parseInt(properties.getProperty("maxIdleConnections", "10"));
	        int maxConnections = Integer.parseInt(properties.getProperty("maxConnections", "10"));
	        
	        Class.forName(driver);
	        
	        connectionPool = new ConnectionPool(jdbcURL, username, password, preconnectCount, maxIdleConnections, maxConnections);
	        System.out.println("Connection pool successfully initialized!");

	    } catch (ClassNotFoundException e) {
	    	ManagerLogger.logError("Error loading driver: " + e.getMessage());
	    } catch (SQLException e) {
	    	ManagerLogger.logError("Error connecting to database: " + e.getMessage());
	    } catch (IOException e) {
	    	ManagerLogger.logError("Error loading configuration file: " + e.getMessage());
	    }
	}
	  
	protected ConnectionPool(String aJdbcURL, String aUsername, String aPassword, int aPreconnectCount,int aMaxIdleConnections,
				int aMaxConnections) throws ClassNotFoundException, SQLException {

		freeConnections = new Vector<Connection>();
	    usedConnections = new Vector<Connection>();
	    jdbcURL = aJdbcURL;
	    userName = aUsername;
	    password = aPassword;
	    preconnectCount = aPreconnectCount;
	    maxIdleConnections = aMaxIdleConnections;
	    maxConnections = aMaxConnections;
	    
	    for (int i = 0; i < preconnectCount; i++) {
	    	Connection conn = DriverManager.getConnection(jdbcURL, userName, password);
			conn.setAutoCommit(true);
			freeConnections.addElement(conn);
	    }
	    connectCount = preconnectCount;
	}
	
	public synchronized Connection checkOut() throws SQLException {
		Connection conn = null;
		if (freeConnections.size() > 0) {
			conn = (Connection)freeConnections.elementAt(0);
			freeConnections.removeElementAt(0);      
			usedConnections.addElement(conn);	    
		} else {      
			if (connectCount < maxConnections) {
				conn = DriverManager.getConnection(jdbcURL, userName, password);    
				usedConnections.addElement(conn);    
				connectCount++;
			} else {
				try {
					wait(); 
					conn = (Connection)freeConnections.elementAt(0);
			        freeConnections.removeElementAt(0);
			        usedConnections.addElement(conn);  
				} catch (InterruptedException ex) {
			    	ManagerLogger.logError("Error loading configuration file: " + ex.getMessage()); 
				}    
			}	    
		}	 
		return conn;	
	}
	
	public synchronized void checkIn(Connection aConn) {
	
		if (aConn ==  null)
			return;
		
	    if (usedConnections.removeElement(aConn)) {
	    	freeConnections.addElement(aConn);
	    	while (freeConnections.size() > maxIdleConnections) {
	    		int lastOne = freeConnections.size() - 1;
	    		Connection conn = (Connection)
	    		freeConnections.elementAt(lastOne);
	        try { conn.close(); } catch (SQLException ex) { }
	        freeConnections.removeElementAt(lastOne);
	    	}
	    	notify();
	    }  
	}
}