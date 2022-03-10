import java.io.IOException;

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

/**
 * FXML Controller Class
 * 
 * @author jkuehl
 *
 */
public class AppController {
	private boolean userAuthenticated; // whether user is authenticated
	private int currentAccountNumber; // current user's account number
	private AccountDatabase accountDatabase; // account information database
	
	// FXML fields
	@FXML
	private TextField AccountTextField;

	@FXML
	private Button LoginButton;

	@FXML
	private TextField PINTextField;

	@FXML
	private Label MessageLabel;
	
	// fields to be used in scene change methods
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	/**
	 * This method attaches a new scene graph to the stage to display the Dashboard when called
	 * @param event
	 * @throws IOException
	 */
	public void switchToDashboard(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Dashboard.fxml"));
		Parent root = loader.load();
		
		scene = new Scene(root);
		
		// access the controller and call a method
		DashboardController controller = loader.getController();
		controller.initializeData(currentAccountNumber, accountDatabase); // passing current account and database

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
	 */
	@FXML
	void loginButtonPressed(ActionEvent event) throws IOException {
		if (AccountTextField.getText().length() > 5 || PINTextField.getText().length() > 5) {
			MessageLabel.setText("Invalid account number or pin (too many digits).");
		} else if (AccountTextField.getText().length() < 0 || PINTextField.getText().length() < 0) {
			MessageLabel.setText("Invalid account number or pin (must enter a value).");
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
				System.out.printf("Success%n%s", accountDatabase.getPublicAccount(currentAccountNumber));
				switchToDashboard(event); 
			} else {
				MessageLabel.setText("Invalid account number or PIN.");
			}
		}

	}

	public void initialize() {
		userAuthenticated = false;
		currentAccountNumber = 0;
		accountDatabase = new AccountDatabase(); // create account information database
	}
}
