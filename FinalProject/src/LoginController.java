import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController 
{

	@FXML
	private TextField usernameTextField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Label errorLabel;
	
	@FXML
	void onLoginPress(ActionEvent event) 
	{
		if (usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty()) 
		{
			errorLabel.setText("Please enter a valid username or password!");
		}
		else {
		try {
			DatabaseConnection connection = new DatabaseConnection(); // create a DatabaseConnection object
			boolean authenticated = connection.authenticateUser(usernameTextField.getText(), passwordField.getText());
			if (authenticated == true) {
				errorLabel.setText("Login Success!");
				UserSession currentUserSession = connection.getUserSession();
				System.out.printf("%s", currentUserSession);
			} else {
				errorLabel.setText("Failure");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	
	}

	@FXML
	void onCreateAccountPress(ActionEvent event) 
	{

	}

	
}
