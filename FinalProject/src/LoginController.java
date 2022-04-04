import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController 
{
	private Stage stage;
	private Scene scene;
	private UserSession userSession;
	private DatabaseConnection databaseConnection;

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
			DatabaseConnection connection = databaseConnection;
			boolean authenticated = connection.authenticateUser(usernameTextField.getText(), passwordField.getText());
			if (authenticated == true) {
				errorLabel.setText("Login Success!");
				UserSession currentUserSession = connection.getUserSession();
				userSession = currentUserSession;
				System.out.printf("%s", currentUserSession);
				switchToDashboard(event);
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
		try {
			switchToCreateAccount(event);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	

	
	public void switchToDashboard(ActionEvent event) throws IOException, SQLException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Dashboard.fxml"));
		Parent root = loader.load();
		
		// access the controller and call a method
		DashboardController controller = loader.getController();
		controller.initializeData(userSession); // passing current account number and database
		
		scene = new Scene(root);

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToCreateAccount(ActionEvent event) throws IOException, SQLException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("CreateAccount.fxml"));
		Parent root = loader.load();
		
		scene = new Scene(root);
		
		// access the controller and call a method
		CreateAccountController controller = loader.getController();
		controller.initializeData(databaseConnection); // passing current account number and database

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public void creationSuccess()
	{
		errorLabel.setText("Account creation successful!");
	}
	
	public void initialize()
	{
		try {
			databaseConnection = new DatabaseConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
