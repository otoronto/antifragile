package main.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import utilities.Counter;

public class MainMenuController {

	public ToggleButton timerButton;
	public TextField timerTextField;
	Counter counter;
	MainMenuController menuControllerInstance; // static yap�nca her�ey d�zeldi
	{
		System.out.println("Selam");
		counter = new Counter();
	}

	public MainMenuController() {
		System.out.println("contructor");
	}

	public void initialize(ActionEvent e) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main/gui/fx/MainMenu.fxml"));

		Parent MainMenuparent = loader.load();
//		menuControllerInstance = loader.getController();
		Scene mainMenuScene = new Scene(MainMenuparent, 900, 900);
		Stage mainMenuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		mainMenuStage.setResizable(false);
		mainMenuStage.setTitle("MainMenu");
		mainMenuStage.setScene(mainMenuScene);
		mainMenuStage.show();

	}

	public void toggleOnAction(ActionEvent e) {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("main/gui/fx/MainMenu.fxml"));
		try {
			Parent MainMenuparent = loader.load();
			Scene mainMenuScene = new Scene(MainMenuparent, 900, 900);
			mainMenuScene.getRoot().setStyle("-fx-font-family: 'serif'");
			Stage mainMenuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			mainMenuStage.setResizable(false);
			mainMenuStage.setTitle("MainMenu");
			mainMenuStage.setScene(mainMenuScene);
			mainMenuStage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		menuControllerInstance = loader.getController();
		
		
		
		boolean isSelected = timerButton.isSelected();
		if (isSelected) {
			counter.startTimer(menuControllerInstance);
		} else {
			counter.stopTimer(menuControllerInstance);
		}
		
	}
}