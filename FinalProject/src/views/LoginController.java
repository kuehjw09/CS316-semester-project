package views;

import java.io.IOException;
import java.sql.SQLException;

import app.DatabaseConnection;
import classes.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
	private Button loginButton;

	@FXML
	void onLoginPress(ActionEvent event)
	{
		if (usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty())
		{
			errorLabel.setText("Please enter a valid username and password!");
		} 
		else
		{
			try
			{
				DatabaseConnection connection = databaseConnection;
				boolean authenticated = connection.authenticateUser(usernameTextField.getText(),
						passwordField.getText());
				if (authenticated == true)
				{
					errorLabel.setText("Login Success!");
					UserSession currentUserSession = connection.getUserSession();
					userSession = currentUserSession;
					System.out.printf("%s", currentUserSession);
					switchToDashboard(event);
				} 
				else
				{
					errorLabel.setText("Unable to authenticate username or password! Please try again.");
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	@FXML
	void onCreateAccountPress(ActionEvent event)
	{
		try
		{
			switchToCreateAccount(event);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	public void switchToDashboard(ActionEvent event) throws IOException, SQLException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Dashboard.fxml"));
		Parent root = loader.load();

		// access the controller and call a method
		DashboardController controller = loader.getController();
		controller.initializeData(userSession); // passing current account number and database

		scene = new Scene(root);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public void switchToCreateAccount(ActionEvent event) throws IOException, SQLException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("CreateAccount.fxml"));
		Parent root = loader.load();

		scene = new Scene(root);

		// access the controller and call a method
		CreateAccountController controller = loader.getController();
		controller.initializeData(databaseConnection); // passing current account number and database

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public void creationSuccess(String username)
	{

		Alert alert = new Alert(AlertType.NONE, "Account " + username + " Created!", ButtonType.OK);

		alert.show();
	}

	public void initialize()
	{
		connectDatabase();

		loginButton.setDefaultButton(true);

	}

	public void connectDatabase()
	{
		try
		{
			databaseConnection = new DatabaseConnection();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

}
