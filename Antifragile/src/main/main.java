package main;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.Counter;
	
public class main extends Application {
static Stage stage;
	 
	    @Override  
	    public void start(Stage primaryStage) throws IOException  {  
	    	stage = primaryStage;
	    initGui(stage);
	         
	}
	    void initGui(Stage primaryStage) throws IOException { 
	    	Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main/gui/fx/ValidatePassword.fxml"));
	    	
		      primaryStage.setTitle("login");
		      primaryStage.setScene(new Scene(root,700,500));
		      primaryStage.show();
	    }
	    public static void main(String[] args) throws InterruptedException {
	    	launch(args);
	    	
	    	
}
}
