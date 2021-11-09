package main.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ValidatePasswordController {
	public TextField usernameField;
	public PasswordField passwordField;
	public Label info;
	String username;

	public String getUsername() {
		System.out.println("Retrieving the username typed in");
		return usernameField.getText();
	}

	public String getPassword() {
		return passwordField.getText();
	}

	String password;

	public Button validateButton;

	public void initialize() {

	}

	public void getAuthenticate(ActionEvent e) {
		boolean authenticationState = true; 
//		    	When get started pressed, check for credentials. 
//		    	If something goes wrong, bring pop up screen 
//		    	if correct, load user data. 
//		    	open the main screen. 
//		    	to change the scene to scene two, load new fxml 

//		    	search database for username if exists validate, lookup the password

//		    	change the static values later.
		String temporaryUsername = "admin";
		String temporaryPassword = "admin";

//		Delete here later 
		if (temporaryUsername.equals(getUsername()) && temporaryPassword.equals(getPassword())) {
			System.out.println("Authenticated");
			authenticationState = true; 
		}
		
		if ( authenticationState) { 
			info.setText("Connected!");
			changeSceneToMainMenu(e);
		}
	}
	private void changeSceneToMainMenu(ActionEvent e) {
		
		try {
			MainMenuController menuCaller = new MainMenuController();
			menuCaller.initialize(e);
			
			
//			Parent MainMenuparent = FXMLLoader.load(getClass().getClassLoader().getResource("main/gui/fx/MainMenu.fxml"));
//			Scene mainMenuScene = new Scene(MainMenuparent,900,900);
//			Stage mainMenuStage = (Stage)((Node)e.getSource()).getScene().getWindow();
//			mainMenuStage.setResizable(false);
//			mainMenuStage.setTitle("MainMenu");
//			mainMenuStage.setScene(mainMenuScene);
//			mainMenuStage.show();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	private boolean dbValidateQuery() {
		boolean validateResult = false;

		return validateResult;
	}

}
