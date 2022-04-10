package views;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Optional;

import classes.Account;
import classes.Notification;
import classes.NotificationType;
import classes.Transaction;
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

public class TransferViewController {
	private static UserSession currentUserSession;
	private Account currentToAccount;
	private Account currentFromAccount;
	private BigDecimal transactionAmount;
	
	
	private static final NumberFormat currency = NumberFormat.getCurrencyInstance();

	
	public static void setCurrentUserSession(UserSession userSession) {
		 currentUserSession = userSession;
	}
	
	public Account getCurrentToAccount() {
		return currentToAccount;
	}

	public void setCurrentToAccount(Account currentAccount) {
		this.currentToAccount = currentAccount;
		errorMessageLabel.setText(null);
	}
	
	public Account getCurrentFromAccount() {
		return currentFromAccount;
	}

	public void setCurrentFromAccount(Account currentAccount) {
		this.currentFromAccount = currentAccount;
		errorMessageLabel.setText(null);
	}
	
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	
	public void setTransactionAmount(BigDecimal amount) {
		this.transactionAmount = amount;
	}


    @FXML
    private TextField amountTextField;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private ChoiceBox<Account> fromAccountChoiceBox;
    
	@FXML
	private final ObservableList<Account> accounts = FXCollections.observableArrayList();

    @FXML
    private Button submitButton;

    @FXML
    private ChoiceBox<Account> toAccountChoiceBox;

    @FXML
    private Label typeLabel;

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

    @FXML
    void confirmAmountButtonPressed(ActionEvent event) {
    	
    	if (currentToAccount.equals(currentFromAccount)) {
    		errorMessageLabel.setText("Please select a valid transfer option.");
    	}  else {
        	try {
        		BigDecimal input = new BigDecimal(amountTextField.getText());
        		
        		if (getCurrentFromAccount().getAvailableBalance().subtract(input).compareTo(BigDecimal.ZERO) < 0.0) {
            		errorMessageLabel.setText("Amount exeeds available balance");
            	} else {
            		setTransactionAmount(input);
					amountTextField.setText(currency.format(getTransactionAmount()));
					submitButton.setDisable(false);
            	}

        	} catch (NumberFormatException exception) {
    			errorMessageLabel.setText("Please enter a valid amount");
    		}
    	}
    }

    @FXML
    void submitButtonPressed(ActionEvent event) {
    	try {
			System.out.println("submitting transfer for " + currency.format(getTransactionAmount()) + " from account "
					+ getCurrentFromAccount().getAccountNumber() + " to " + getCurrentToAccount().getAccountNumber());
			currentUserSession.debit(currentFromAccount, transactionAmount);
			currentUserSession.credit(currentToAccount, transactionAmount);
			Transaction fromTransaction = new Transaction(getCurrentFromAccount().getAccountNumber(),
					"Transfer", "debit", getTransactionAmount());
			Transaction toTransaction = new Transaction(getCurrentToAccount().getAccountNumber(),
					"Transfer", "credit", getTransactionAmount());
			toTransaction.addTransaction();
			fromTransaction.addTransaction();
			Notification notification = new Notification(NotificationType.TRANSFER,
					String.format("Transfer submitted from account %s to account %s", getCurrentFromAccount().getName(), 
							getCurrentToAccount().getName()));
			currentUserSession.addNotification(notification);
    	} catch (SQLException exception) {
    		System.out.printf("Transaction failed.%n");
			exception.printStackTrace();
    	}
    	
		submitButton.setDisable(true);

		try {
			switchToAccountView(event);
		} catch (IOException exception) {
			exception.printStackTrace();
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
    
    public void initialize() {
    	// set the toAccount and fromAccount choice boxes
    	errorMessageLabel.setText(null);
    	
		for (Account account : currentUserSession.getAccounts()) {
			accounts.add(account);
		}
		
		fromAccountChoiceBox.setItems(accounts);
		toAccountChoiceBox.setItems(accounts);
		
		toAccountChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
			@Override
			public void changed(ObservableValue<? extends Account> ov, Account oldAccount, Account newAccount) {
				setCurrentToAccount(newAccount);
			}
		});
		
		fromAccountChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
			@Override
			public void changed(ObservableValue<? extends Account> ov, Account oldAccount, Account newAccount) {
				setCurrentFromAccount(newAccount);
			}
		});
    }

}
