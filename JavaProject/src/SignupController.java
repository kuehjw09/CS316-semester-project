
import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignupController {
	private AccountDatabase accountDatabase;
	// fields to be used in scene change methods
	private Stage stage;
	private Scene scene;

	@FXML
	private TextField AccountTextField;

	@FXML
	private Button LoginButton;

	@FXML
	private Label MessageLabel;

	@FXML
	private TextField PINTextField;

	@FXML
	private Button SubmitButton;

	@FXML
	void submitButtonPressed(ActionEvent event) throws SQLException, IOException {

		if (AccountTextField.getText().isEmpty() || PINTextField.getText().isEmpty()) {
			MessageLabel.setText("One or more fields is empty.");
		} else {
			try {
				boolean userExists; // whether user entered an existing account number
				int accountNumber = Integer.valueOf(AccountTextField.getText()); // input account number
				int accountPIN = Integer.valueOf(PINTextField.getText()); // input PIN

				userExists = accountDatabase.search(accountNumber);

				if (!userExists) {
					String formatted = String.format("%05d", accountNumber);
					System.out.printf("%nAdding accountNo. %s to Project_Database...%n%n...%n%n", formatted);

					accountDatabase.addRowToAccounts(accountNumber, accountPIN, 0.00, 0.00);
					switchToLogin(event);

				} else {
					MessageLabel.setText("Account already exists.");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method will switch back to the login scene when a user selects the log
	 * out button.
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void switchToLogin(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void initializeData(AccountDatabase database) {
		accountDatabase = database;
	}

}
