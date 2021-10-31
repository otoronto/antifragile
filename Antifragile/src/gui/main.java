package gui;
import database.DBtasks;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
	
public class main extends Application{

	 
	  
	    @Override  
	    public void start(Stage primaryStage) throws Exception {  
	        // TODO Auto-generated method stub  
	         new DBtasks();
	         
	         System.out.println("Hello my sweet painful start");
	  
	} 
	    public static void main (String[] args) {
	    	launch(args);
	    	System.out.println("yeter");
	    	
	    	
	    }
	    		
	    
	
}
