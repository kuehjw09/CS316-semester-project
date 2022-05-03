package views;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import app.DatabaseConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;

public class CreateAccountController
{
	private DatabaseConnection databaseConnection;

	private Stage stage;

	private Scene scene;

	@FXML
	private TextField firstnameTextField;

	@FXML
	private TextField lastnameTextField;

	@FXML
	private TextField visible_ssnTextField;

	@FXML
	private TextField usernameTextField;

	@FXML
	private TextField visible_passwordTextField;

	@FXML
	private PasswordField hiddenPasswordField;

	@FXML
	private PasswordField hiddenSSNPasswordField;

	@FXML
	private DatePicker birthdayDatePicker;

	@FXML
	private Label errorLabel;

	@FXML
	private CheckBox passwordToggleCheckBox;

	@FXML
	private CheckBox ssnToggleCheckBox;

	@FXML
	private Button createButton;

	@FXML
	void onCreateAccount(ActionEvent event) throws IOException, SQLException
	{
		if (createAccount()) //Calls the createAccount method and returns a boolean based on whether account creation was successful
		{
			switchToLoginSuccess(event); //If true is returned, this method is called to switch to the login screen with a message confirming the success
		}
		else
		{
			errorLabel.setText("Username already exists!"); //If false is returned, the error label changes to reflect this
		}

	}

	@FXML
	void onReset(ActionEvent event)
	{
		errorLabel.setText(null);
		clearValues(); //Method to clear all fields

	}

	@FXML
	void onReturn(ActionEvent event) throws IOException, SQLException
	{
		switchToLoginCancel(event); //Method to return to login without creating an account

	}

	@FXML
	void toggleVisiblePassword(ActionEvent event) //Toggles between a visible and invisible password field
	{
		if (passwordToggleCheckBox.isSelected())
		{
			visible_passwordTextField.setText(hiddenPasswordField.getText());
			visible_passwordTextField.setVisible(true);
			hiddenPasswordField.setVisible(false);
			return;
		}
		hiddenPasswordField.setText(visible_passwordTextField.getText());
		hiddenPasswordField.setVisible(true);
		visible_passwordTextField.setVisible(false);
	}

	@FXML
	void toggleVisibleSSN(ActionEvent event) //Toggles between a visible and invisible SSN field
	{
		if (ssnToggleCheckBox.isSelected())
		{
			visible_ssnTextField.setText(hiddenSSNPasswordField.getText());
			visible_ssnTextField.setVisible(true);
			hiddenSSNPasswordField.setVisible(false);
			return;
		}
		hiddenSSNPasswordField.setText(visible_ssnTextField.getText());
		hiddenSSNPasswordField.setVisible(true);
		visible_ssnTextField.setVisible(false);

	}

	public void initialize()
	{
		setToolTips(); //Assigns tool tips 

		this.toggleVisiblePassword(null); //Ensures that the invisible password field is selected by default
		this.toggleVisibleSSN(null); //Ensures that the invisible ssn field is selected by default

		formatBirthDate(); //Initializes the birthdate formatter

		setSSNListeners(); //Initializes SSN Listeners for SSN auto-formatting

		createButton.setDefaultButton(true); //Sets the create account button to default, can use the "Enter" key to activate

	}

	class FieldListener implements ChangeListener<Boolean>
	{
		private final TextField textField;
		private final PasswordField passwordField;

		FieldListener(TextField textField, PasswordField passwordField)
		{
			this.textField = textField;
			this.passwordField = passwordField;
		}

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
		{
			if (!newValue) // check if focus gained or lost
			{
				set_ssnValue((getFormattedText(ssnValue())));
			}
		}

		public String getFormattedText(String str)
		{

			String formattedSSN = ssnValue().replaceFirst("(\\d{3})(\\d{2})(\\d+)", "$1-$2-$3");

			clearErrorLabel();

			if (!isValidSSN(formattedSSN))
			{
				formattedSSN = str;
				errorLabel.setText("Please enter a valid 9 digit SSN!");
			}

			return formattedSSN;
		}
	}

	private static Pattern SSN_NUM_PATTERN = Pattern
			.compile("^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$"); //Compiles a regex for ssn validation

	private static Pattern USERNAME_PATTERN = Pattern
			.compile("^(?=.{3,15}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$"); //Compiles a regex for username validation
	private static Pattern PASSWORD_PATTERN = Pattern
			.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=?!()])(?=\\S+$).{8,20}$"); //Compiles a regex for password validation

	private static Pattern NAME_PATTERN = Pattern.compile("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}"); //Compiles a regex for name validation

	
	public static boolean addUser(String firstName, String lastName, Date birthDate, String SSN, String username,
			String password)
	{
		boolean added = false;

		Connection connection = DatabaseConnection.getConnection();

		String callProcedure = "CALL DB2.NEW_USER(?, ?, ?, ?, ?, ?)";

		if (!usernameExists(username, connection))
		{

			try (CallableStatement cs = connection.prepareCall(callProcedure))
			{
				cs.setString(1, firstName);
				cs.setString(2, lastName);
				cs.setDate(3, birthDate);
				cs.setString(4, SSN);
				cs.setString(5, username);
				cs.setString(6, password);

				cs.execute();

				added = true;
				System.out.println("User successfully added!");

			}
			catch (SQLException e)
			{
				e.printStackTrace();
				System.out.println("User creation unsuccessful");
			}
		}
		else
		{
			System.out.println("Username already exists in database.");
		}

		return added;

	}

	private static boolean isValidName(String name)
	{
		return NAME_PATTERN.matcher(name).matches();
	}

	private static boolean isValidPassword(String password)
	{
		return PASSWORD_PATTERN.matcher(password).matches();
	}

	private static boolean isValidSSN(String ssn)
	{
		return SSN_NUM_PATTERN.matcher(ssn).matches();
	}

	private static boolean isValidUsername(String username)
	{
		return USERNAME_PATTERN.matcher(username).matches();
	}

	public static boolean usernameExists(String user, Connection connection)
	{
		boolean exists = true;

		String username = user;

		PreparedStatement search = null;
		ResultSet results = null;

		try
		{
			String query = "SELECT username FROM DB2.Users where username=?";
			search = connection.prepareStatement(query);
			search.setString(1, username);

			results = search.executeQuery();

			if (!results.next())
			{
				exists = false;
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (search != null)
			{
				try
				{
					search.close();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (results != null)
			{
				try
				{
					results.close();
				}
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return exists;
	}

	public void clearErrorLabel()
	{
		errorLabel.setText("");
	}

	private void clearValues()
	{
		firstnameTextField.clear();
		lastnameTextField.clear();
		visible_passwordTextField.clear();
		hiddenPasswordField.clear();
		visible_ssnTextField.clear();
		hiddenSSNPasswordField.clear();
		birthdayDatePicker.setValue(null);
		usernameTextField.clear();

	}

	private boolean createAccount()
	{
		boolean success = false;

		if (!isEmpty())
		{
			if (isValidName(firstnameTextField.getText()) && isValidName(lastnameTextField.getText()))
			{
				if (isValidSSN(ssnValue()))
				{
					if (isValidUsername(usernameTextField.getText()))
					{
						if (isValidPassword(passwordValue()))
						{
							String firstName = firstnameTextField.getText();
							String lastName = lastnameTextField.getText();
							LocalDate birthDay = birthdayDatePicker.getValue();
							Date birthDate = Date.valueOf(birthDay);
							String SSN = ssnValue();
							String username = usernameTextField.getText();
							String password = passwordValue();

							if (addUser(firstName, lastName, birthDate, SSN, username, password))
							{

								System.out.println("User Successfully Added");

								success = true;

							}
							else
							{
								System.out.println("User not added");
							}

						}
						else
						{
							errorLabel.setText("Please enter a valid password!");
						}
					}
					else
					{
						errorLabel.setText("Please enter a valid username!");
					}
				}
				else
				{
					errorLabel.setText("Please enter a valid 9 digit SSN!");
				}
			}
			else
			{
				errorLabel.setText("Please enter a valid first and last name!");
			}
		}
		else
		{
			errorLabel.setText("Please complete all fields!");
		}

		return success;

	}

	public void formatBirthDate()
	{
		birthdayDatePicker.setConverter(new StringConverter<LocalDate>()
		{
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			@Override
			public LocalDate fromString(String dateString)
			{
				if (dateString == null || dateString.trim().isEmpty())
				{
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}

			@Override
			public String toString(LocalDate localDate)
			{
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

		});
	}

	public void initializeData(DatabaseConnection databaseConnection)
	{
		this.databaseConnection = databaseConnection; // obtaining reference to database connection
	}

	private boolean isEmpty()
	{
		if (usernameTextField.getText() == null || birthdayDatePicker.getValue() == null
				|| firstnameTextField.getText() == null || lastnameTextField.getText() == null || ssnValue() == null
				|| passwordValue() == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private String passwordValue()
	{
		return passwordToggleCheckBox.isSelected() ? visible_passwordTextField.getText()
				: hiddenPasswordField.getText();
	}

	private void set_ssnValue(String formattedSSN)
	{
		visible_ssnTextField.setText(formattedSSN);
		hiddenSSNPasswordField.setText(formattedSSN);
	}

	public void setSSNListeners()
	{
		visible_ssnTextField.focusedProperty()
				.addListener(new FieldListener(visible_ssnTextField, hiddenSSNPasswordField));
		hiddenSSNPasswordField.focusedProperty()
				.addListener(new FieldListener(visible_ssnTextField, hiddenSSNPasswordField));
	}

	public void setToolTips()
	{
		final Tooltip passwordTip = new Tooltip();
		final Tooltip usernameTip = new Tooltip();
		final Tooltip ssnTip = new Tooltip();

		ssnTip.setText("Must be a valid Social Security Number: XXX-XX-XXXX");

		hiddenSSNPasswordField.setTooltip(ssnTip);
		visible_ssnTextField.setTooltip(ssnTip);

		usernameTip.setText("Username must contain:\n" + "3-15 characters\n" + "No special characters or spaces\n");

		usernameTextField.setTooltip(usernameTip);

		passwordTip.setText("Password must contain:\n" + "8-20 characters\n" + "At least one upper case character\n"
				+ "At least one lower case character\n" + "At least one special character\n" + "No spaces");

		hiddenPasswordField.setTooltip(passwordTip);
		visible_passwordTextField.setTooltip(passwordTip);

	}

	private String ssnValue()
	{
		return ssnToggleCheckBox.isSelected() ? visible_ssnTextField.getText() : hiddenSSNPasswordField.getText();
	}

	public void switchToLoginCancel(ActionEvent event) throws IOException, SQLException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Login.fxml"));
		Parent root = loader.load();

		scene = new Scene(root);

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public void switchToLoginSuccess(ActionEvent event) throws IOException, SQLException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Login.fxml"));
		Parent root = loader.load();

		scene = new Scene(root);

		// access the controller and call a method
		LoginController controller = loader.getController();
		controller.creationSuccess(usernameTextField.getText());
		;// notifies user that account creation was successful

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

}
