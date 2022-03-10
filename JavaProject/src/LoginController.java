import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
	private boolean userAuthenticated; // whether user is authenticated
	private int currentAccountNumber; // current user's account number
	private AccountDatabase accountDatabase; // account information database

	@FXML
	private TextField AccountTextField;

	@FXML
	private Button LoginButton;

	@FXML
	private TextField PINTextField;

	@FXML
	private Label MessageLabel;

	@FXML
	void loginButtonPressed(ActionEvent event) {
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
