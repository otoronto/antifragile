package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DBtasks {
	public DBtasks() throws ClassNotFoundException, SQLException {
	}

	public Connection connectToDB() throws ClassNotFoundException, SQLException {
		final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
		final String JDBC_URL = "jdbc:derby:chonkerDB;create=true";
		Class.forName(DRIVER);
		Connection connection = DriverManager.getConnection(JDBC_URL);
//		System.out.println("Connection created");

		return connection;

	}

	public void printTable(String tableName) {

		String sql = "SELECT * FROM " + tableName;

		try {
			Connection c = connectToDB();
			Statement st = c.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			
//			String query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName +"' ORDER BY ORDINAL_POSITION";
//			ResultSet columnNames = st.executeQuery(query);
			ResultSetMetaData rsmd= rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[]names = new String[columnCount];
			System.out.println("Column count: " + columnCount);
			for (int i=0; i <columnCount ; i ++  ) {
				names [i] = rsmd.getColumnName(i+1);
			}
			
			System.out.println("Column names of the table " + tableName + ": ");
			for ( String columnName: names) {
				System.out.print(","+columnName );
			}
			
			System.out.println();
			
			if(rs.next()) {
			while (rs.next()) {

				System.out.println("id: " + rs.getString("Id"));
				System.out.println("Task name: " + rs.getString("task_name"));
				System.out.println("Task description: " + rs.getString("task_description"));
				System.out.println("Task creation date: " + rs.getString("task_creation_date"));
				System.out.println("Task due date: " + rs.getString("task_end_date"));
			}
			}
			else {
				System.out.println("Requested table is empty");
				
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
//	This method will be adjusted to function dynamically later... To be revised...
	public void createTable() {
		try {
			Connection con = connectToDB();
			Statement st = con.createStatement();
			
			
			String createTableQuery = "CREATE TABLE weekly_tasks( " + "Id INT NOT NULL GENERATED ALWAYS AS IDENTITY, " + "task_name VARCHAR(255), " + "task_description VARCHAR(255), "
					+"task_creation_date DATE, " +"task_end_date DATE, " +" completion_percentage INT"+ "PRIMARY KEY (Id))";
			
			st.execute(createTableQuery);
			System.out.println("Table creation succesful");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void insertion(String tableName, String task, String taskDescription, String creationDate, String endDate)
			throws ClassNotFoundException, SQLException {
		Connection connection = connectToDB();
		Statement st1 = connection.createStatement();
		String insertionQueryRealDeal = " INSERT INTO " + tableName + "(" + "task_name, task_description, task_creation_date, task_end_date) VALUES " + "('"
				+ task + "', '" + taskDescription + "', " + creationDate +","+endDate+ ")";
		
		System.out.println("insertion query: " + insertionQueryRealDeal);
		st1.execute(insertionQueryRealDeal);
		System.out.println("insertion Successful");

	}

	public void showAllTables() {
		try {
			Connection c = new DBtasks().connectToDB();
			String[] types = { "TABLE" };
			
			DatabaseMetaData metaData = c.getMetaData();
			ResultSet rs = metaData.getTables(null, null, null, types);
			while (rs.next()) {
				System.out.println(rs.getString(3));
				
			}
			

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void alterTable(String tableName,String columnName, String columnValue ) {
		String addColumn ="";
		String query = "ALTER TABLE "+ tableName+ " ADD " + columnName +" "+  columnValue; 
		
		Connection c;
		try {
			c = connectToDB();
			Statement st = c.createStatement();
			st.execute(query);
			System.out.println("Table "+ tableName+ " altered succesfully\nColumn "+ columnName + " added with type: "+ columnValue);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}











