package derbyTests;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import database.DBtasks;

public class derbyBasics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			DBtasks db = new DBtasks();
			Connection connection = db.connectToDB();
//			connection;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//			db.insertion("daily_tasks", "Bulaşıklar", "Bulaşıkların makinaya koyulup çalıştırılması", "'"+Date.valueOf(java.time.LocalDate.now())+"'", null);
//			db.printTable("DAILY_TASKS");
//			db.showAllTables();
//			db.createTable();
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
//			db.showAllTables();
			db.printTable("DAILY_TASKS");
			
//			db.alterTable("DAILY_TASKS", "completion_percentage", "INT");
//			System.out.println(now);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println("connection unsuccesful");
		}
		
		
	}

}
