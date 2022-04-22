package views;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

import classes.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class addAccountPageController {
	private static UserSession currentUserSession;

	public static void setCurrentUserSession(UserSession userSession) {
		currentUserSession = userSession;
	}

	//observable list to add options to the select account choice box
	ObservableList <String> accountTypeList = FXCollections.observableArrayList("Checking","Savings");

	@FXML
	private TextField accountNameTextField;

	@FXML 
	private AnchorPane anchorPane;

	@FXML
	private TextField accountNumberField;

	@FXML
	private ChoiceBox <String> accountTypeChoicebox;

	@FXML
	private TextField pinTextField;

	@FXML
	private Pane selectAccountChoiceBox;

	@FXML
	private Button createAccountButton;

	@FXML 
	private void returnButtonPressed() throws IOException {
		// return to AccountView
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountsView.fxml"));
		AnchorPane accountPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(accountPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	void createAccountButtonPressed(ActionEvent event) throws SQLException, IOException   {
		if (accountNameTextField.getLength()<= 25) {
			
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setHeaderText("Create Account");
			alert.setContentText("Are you sure you want to create a new account?");
			
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get() == null) {
				alert.close();
			} else if (option.get() == ButtonType.CANCEL) {
				alert.close();
			} else if (option.get() == ButtonType.OK) {
				currentUserSession.addNewAccount(accountNameTextField.getText(), accountTypeChoicebox.getValue());
				returnButtonPressed();
			} else {
			alert.close();
			}
		}
		
		else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Account Creation Failed");
			alert.setHeaderText("Account Creation Failed");
			alert.setContentText(
					"Unable to make new account. Cannot have more than 25 Characters for the name!");

			Optional<ButtonType> option = alert.showAndWait();

			if (option.get() == null) {
				alert.close();
			} else if (option.get() == ButtonType.OK) {
				alert.close();
			}
		}
	}
	
	//method to initialize 
	public void initialize(){
		//sets value to default savings for choicebox
		accountTypeChoicebox.setValue("Savings");
		accountTypeChoicebox.setItems(accountTypeList);
		
		
	}   
}

