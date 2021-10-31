package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBtasks {
	Connection connection; 
	public DBtasks() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated constructor stub
		connection=connectToDB();
		}
	
	Connection connectToDB() throws ClassNotFoundException, SQLException {
		final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
		final String JDBC_URL = "jdbc:derby:chonkerDB;create=true";
		Class.forName(DRIVER);
		Connection connection = DriverManager.getConnection(JDBC_URL);
		System.out.println("Connection created");
		return connection;

	}
}
