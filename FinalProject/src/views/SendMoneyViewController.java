package views;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Optional;

import app.DatabaseConnection;
import classes.Account;
import classes.User;
import classes.UserSession;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SendMoneyViewController {
	private static UserSession currentUserSession; // state
	private User selectedRecipient; // selected recipient
	private Account selectedAccount; // account to debit
	private BigDecimal transactionAmount; // amount to debit
	
	private static final NumberFormat currency = NumberFormat.getCurrencyInstance(); // currency formatter

	// allows a the controller to be set with a UserSession object
	public static void setCurrentUserSession(UserSession userSession) {
		 currentUserSession = userSession;
	}
	
	private void setSelectedAccount(Account account) {
		this.selectedAccount = account;
	}
	
	private void setSelectedRecipient(User user) {
		this.selectedRecipient = user;
	}
	
	private void setTransactionAmount(BigDecimal amount) {
		this.transactionAmount = amount;
	}

    @FXML
    private TextField amountTextField;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button submitButton;

    @FXML
    private ChoiceBox<Account> accountChoiceBox;
    
    @FXML
    private final ObservableList<Account> accounts = FXCollections.observableArrayList();

    @FXML
    private Label typeLabel;

    @FXML
    private ChoiceBox<User> userChoiceBox;
    
    @FXML
    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    void cancelButtonPressed(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Cancel Transaction");
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

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

    @FXML
    void confirmAmountButtonPressed(ActionEvent event) {
    	try {
    		BigDecimal input = new BigDecimal(amountTextField.getText());
    		
    		if (!(input.compareTo(BigDecimal.ZERO) > 0.0)) {
				errorMessageLabel.setText("Please enter a valid amount");
			} else if (selectedAccount.getAvailableBalance().subtract(input).compareTo(BigDecimal.ZERO) < 0.0) {
        		errorMessageLabel.setText("Amount exeeds available balance");
        	} else {
        		setTransactionAmount(input);
				amountTextField.setText(currency.format(transactionAmount));
				amountTextField.setDisable(true);
				submitButton.setDisable(false);
				errorMessageLabel.setText(null);
        	}

    	} catch (NumberFormatException exception) {
			errorMessageLabel.setText("Please enter a valid amount");
		}
    }

    @FXML
    void submitButtonPressed(ActionEvent event) {
    	System.out.printf("Selected Recipient: %n%s%nSelected Account: %n%s", selectedRecipient, selectedAccount);
    	
    	try {
    		
    		currentUserSession.performExternalTransfer(selectedAccount, selectedRecipient, transactionAmount);
    		
    		switchToAccountView(event); // return to dashboard
    		
    	} catch (SQLException | IOException exception) {
    		exception.printStackTrace();
    	}
    }
    
    public void initialize() {
		submitButton.setDisable(true);
		errorMessageLabel.setText(null);
		
		for (Account account : currentUserSession.getAccounts()) {
			accounts.add(account);
		}
		
		accountChoiceBox.setItems(accounts);
		
		try {
			for (User user : currentUserSession.getDatabaseConnection().getUsers()) {
				users.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		userChoiceBox.setItems(users);
		
		
		accountChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
			@Override
			public void changed(ObservableValue<? extends Account> ov, Account oldAccount, Account newAccount) {
				setSelectedAccount(newAccount);
			}
		});
		
		userChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
			@Override
			public void changed(ObservableValue<? extends User> ov, User oldUser, User newUser) {
				setSelectedRecipient(newUser);
			}
		});
    }

}
