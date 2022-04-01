import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class DashboardController {
	private UserSession currentUserSession; // hold current user session for context

	public void setCurrentUserSession(UserSession currentUserSession) {
		this.currentUserSession = currentUserSession;
	}

	public UserSession getCurrentUserSession() {
		return this.currentUserSession;
	}
	
	@FXML
	private AnchorPane anchorPane;

	// Footer Buttons
	@FXML
	void notificationsButtonPressed(ActionEvent event) {
		System.out.println("Notifications button pressed");
	}

	@FXML
	void userButtonPressed(ActionEvent event) {
		System.out.println("User button pressed");

	}

	// Header Buttons
	@FXML
	void depositButtonPressed(ActionEvent event) throws IOException {
		// route the user to the transactions view
		TransactionViewController.setCurrentUserSession(currentUserSession);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionView.fxml"));
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
	void menuButtonPressed(ActionEvent event) {
		// route the user to the menu view
		System.out.println("menu button pressed");
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Alert Test");
		alert.setHeaderText("menu button pressed");
		alert.setContentText(currentUserSession.toString());

		alert.showAndWait();
	}

	@FXML
	void transferButtonPressed(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PayAndTransferView.fxml"));
		AnchorPane centerPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(centerPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Dashboard is initialized with a UserSession object obtained when a validated
	 * user signs in
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
