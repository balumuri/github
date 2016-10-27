package com.edlore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author Sowjanya B
 *
 */
public class ConnectionFactory {

	private final static ConnectionFactory factory = new ConnectionFactory();
	private static ResourceBundle bundle = null;
	private static Connection connection = null;
	
	
	private ConnectionFactory() {
		//default constructor
	}
	static{
		try {
			bundle = ResourceBundle.getBundle("com/edlore/resourses/applicationProperties");
			// loading the oracle jdbc driver class
			Class.forName(bundle.getString("MsDriverClass"));
			System.out.println("Driver loaded-----------");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("driver class unable to load ", e);
		}
	}

	 private Connection createConnection(){
		
		try {
			// getting the connection object by providing the required
			// parameters
			String url = null;
			if(connection == null){
				url = bundle.getString("MsUrl")+";databaseName="+bundle.getString("MsdatabaseName")+";user="+bundle.getString("MsUserName")+";password="+bundle.getString("MsPassword");
			connection = DriverManager.getConnection(url);
			System.out.println("from if connection");
			}	
		} catch (SQLException e) {
			throw new RuntimeException("Connection filed exception by factory ", e);
		}
		return connection;
	}

	/*
	 * This method is used to get the connection object to perform actions with
	 * the Database
	 */
	public static synchronized Connection getConnection() {
		System.out.println("from return");
		return factory.createConnection();
	}
	
}
