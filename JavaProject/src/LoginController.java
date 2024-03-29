/**
 * FXML Application Controller Class
 *  - Handles user login and authentication, then initializes the Dashboard component 
 *  with validated account information.
 * @author jkuehl
 *
 */

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	private boolean userAuthenticated; // whether user is authenticated
	private int currentAccountNumber; // current user's account number
	private AccountDatabase accountDatabase; // account information database
	
	// FXML fields
	@FXML
	private TextField AccountTextField;

	@FXML
	private Button LoginButton;
	
	@FXML 
	private Button SignupButton;

	@FXML
	private TextField PINTextField;

	@FXML
	private Label MessageLabel;
	
	// fields to be used in scene change methods
	private Stage stage;
	private Scene scene;
	
	/**
	 * This method attaches a new scene graph to the stage to display the Dashboard when called
	 * @param event
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void switchToDashboard(ActionEvent event) throws IOException, SQLException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Dashboard.fxml"));
		Parent root = loader.load();
		
		scene = new Scene(root);
		
		// access the controller and call a method
		DashboardController controller = loader.getController();
		controller.initializeData(currentAccountNumber, accountDatabase); // passing current account number and database

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This method attaches a new scene graph to the stage to display the Signup page when called
	 * @param event
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void switchToSignUp(ActionEvent event) throws IOException, SQLException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Signup.fxml"));
		Parent root = loader.load();
		
		scene = new Scene(root);
		
		// access the controller and call a method
		SignupController controller = loader.getController();
		controller.initializeData(accountDatabase); // passing current account number and database

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}


	/**
	 * This method is called when a user attempts to log in. It verifies the account information
	 * provided by the user. If account info is valid, it sets the currentAccountNumber to the 
	 * appropriate account and calls method switchToDashboard to change the scene to the dashboard.
	 * 
	 * @param event
	 * @throws IOException
	 * @throws SQLException 
	 */
	@FXML
	void loginButtonPressed(ActionEvent event) throws IOException, SQLException {
		if (AccountTextField.getText().length() > 5 || PINTextField.getText().length() > 5) {
			MessageLabel.setText("Please enter an integer having 5 digits or less.");
		} else if (AccountTextField.getText().isEmpty() || PINTextField.getText().isEmpty()) {
			MessageLabel.setText("One or more fields is empty.");
		} else {
			int accountNumber = Integer.valueOf(AccountTextField.getText()); // input account number
			int accountPIN = Integer.valueOf(PINTextField.getText()); // input PIN

			// attempt to authenticate user against AccountDatabase
			try {
				// set userAuthenticated to boolean value returned by database
				userAuthenticated = accountDatabase.authenticateUser(accountNumber, accountPIN);

			} catch (Exception exception) {
				MessageLabel.setText("Invalid account number or PIN.");
				System.out.print(exception.getMessage());
			}

			// check whether authentication succeeded
			if (userAuthenticated) {
				currentAccountNumber = accountNumber; // save user's account
				switchToDashboard(event); 
			} else {
				MessageLabel.setText("Invalid account number or PIN.");
			}
		}
	}
	
	@FXML
	void signUpButtonPressed(ActionEvent event) throws IOException, SQLException {
		try {
			switchToSignUp(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initialize() throws SQLException {
		userAuthenticated = false;
		currentAccountNumber = 0;
		accountDatabase = new AccountDatabase(); // initialize account information database
	}
}
