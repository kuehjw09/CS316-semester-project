import java.io.IOException;
import java.text.NumberFormat;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class DepositViewController {
	private static UserSession currentUserSession;
	private Account currentAccount;
	private Double depositAmount;

	private static final NumberFormat currency = NumberFormat.getCurrencyInstance();

	public static void setCurrentUserSession(UserSession userSession) {
		currentUserSession = userSession;
	}

	public UserSession getCurrentUserSession() {
		return currentUserSession;
	}

	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccout) {
		this.currentAccount = currentAccout;
		errorMessageLabel.setText(null);
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}
	

    @FXML
    private ChoiceBox<Account> accountChoiceBox;
    
	@FXML
	private final ObservableList<Account> accounts = FXCollections.observableArrayList();

    @FXML
    private TextField amountTextField;

    @FXML
    private Button submitButton;
    
    @FXML 
    private Label errorMessageLabel;
    
    @FXML
    void confirmAmountButtonPressed() {
		try {
			Double input = Double.valueOf(amountTextField.getText());
			if (!(input > 0)) {
				errorMessageLabel.setText("Please enter a valid amount");
			} else {
				setDepositAmount(input);
				errorMessageLabel.setText(null);

				amountTextField.setText(currency.format(getDepositAmount()));
				submitButton.setDisable(false);
			}
		} catch (NumberFormatException exception ) {
			errorMessageLabel.setText("Please enter a valid amount");
		}
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Cancel Deposit");
		alert.setContentText("Are you sure you wish to cancel?");

		Optional<ButtonType> option = alert.showAndWait();

		if (option.get() == ButtonType.OK) {
			try {
				switchToAccountView(event);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		} else {
			alert.close();
		}
    }

    @FXML
    void submitButtonPressed(ActionEvent event) {
		try {
			System.out.println("submitting deposit for " + currency.format(getDepositAmount()) + " from account "
					+ getCurrentAccount().getAccountNumber());

			submitButton.setDisable(true);
			try {
				switchToAccountView(event);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		} catch (NullPointerException exception) {
			errorMessageLabel.setText("Please select an account for deposit.");
		}
    }
    
	void switchToAccountView(ActionEvent event) throws IOException {
		Stage stage;
		Scene scene;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Dashboard.fxml"));
		Parent root = loader.load();
		
		scene = new Scene(root);
		
		// access the controller and call a method
		DashboardController controller = loader.getController();
		controller.initializeData(currentUserSession);

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
    
    public void initialize() {
		errorMessageLabel.setText(null);
		for (Account account : currentUserSession.getAccounts()) {
			accounts.add(account);
		}

		accountChoiceBox.setItems(accounts);

		accountChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
			@Override
			public void changed(ObservableValue<? extends Account> ov, Account oldAccount, Account newAccount) {
				setCurrentAccount(newAccount);
			}
		});
		
    }

}
