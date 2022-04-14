package views;
import java.io.IOException;

import classes.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashboardController {
	private UserSession currentUserSession; // contains a UserSession object

	public void setCurrentUserSession(UserSession currentUserSession) {
		this.currentUserSession = currentUserSession;
	}

	public UserSession getCurrentUserSession() {
		return this.currentUserSession;
	}

	@FXML
	private AnchorPane anchorPane;
	
	@FXML 
	void logOutButtonPressed(ActionEvent event) throws IOException {
		Stage stage;
		Scene scene;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Login.fxml"));
		Parent root = loader.load();

		scene = new Scene(root);
		

		stage = (Stage) anchorPane.getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	void manageAccountsButtonPressed(ActionEvent event) throws IOException {
		// route user to accounts management
		ManageAccountsViewController.setCurrentUserSession(currentUserSession);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccountsView.fxml"));
		AnchorPane centerPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(centerPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@FXML
	void depositButtonPressed(ActionEvent event) throws IOException {
		// route the user to the transactions view
		TransactionViewController.setCurrentUserSession(currentUserSession);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionView.fxml"));
		TransactionViewController.setTransactionType("DEPOSIT");
		AnchorPane centerPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(centerPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@FXML
	void withdrawalButtonPressed(ActionEvent event) throws IOException {
		TransactionViewController.setCurrentUserSession(currentUserSession);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionView.fxml"));
		TransactionViewController.setTransactionType("WITHDRAWAL");
		AnchorPane centerPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(centerPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@FXML
	void homeButtonPressed(ActionEvent event) throws IOException {
		// route the user to the dashboard
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountsView.fxml"));
		AccountsViewController.setCurrentUserSession(getCurrentUserSession());
		AnchorPane centerPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(centerPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@FXML
	void transferButtonPressed(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TransferView.fxml"));
		TransferViewController.setCurrentUserSession(getCurrentUserSession());
		AnchorPane centerPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(centerPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@FXML
	void menuButtonPressed(ActionEvent event) {
		// route the user to the menu view
		System.out.println("menu button pressed");
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Alert Test");
		alert.setHeaderText("menu button pressed");
		alert.setContentText(currentUserSession.toString());

		alert.showAndWait();
	}
	
	/**
	 * Dashboard is initialized with a UserSession object when a validated user
	 * succesfully signs in
	 * 
	 * @param currentUserSession
	 * @throws IOException
	 */
	public void initializeData(UserSession currentUserSession) throws IOException {
		this.currentUserSession = currentUserSession;

		// Start a user session at the accounts view
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountsView.fxml"));
		AccountsViewController.setCurrentUserSession(getCurrentUserSession()); // set the current user session in the
																				// accountsViewController
		AnchorPane centerPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(centerPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
