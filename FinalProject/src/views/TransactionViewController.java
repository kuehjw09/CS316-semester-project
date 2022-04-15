package views;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventObject;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TransactionViewController {
	private static UserSession currentUserSession;
	private Account currentAccount;
	private BigDecimal amount;
	private static TransactionType transactionType;

	public static enum TransactionType {
		DEPOSIT, WITHDRAWAL;
	}

	public static void setTransactionType(String type) {
		transactionType = (type.equalsIgnoreCase("DEPOSIT")) ? TransactionType.DEPOSIT : TransactionType.WITHDRAWAL;
	}

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

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
		errorMessageLabel.setText(null);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@FXML
	private Label typeLabel;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private ChoiceBox<Account> accountChoiceBox;

	@FXML
	private final ObservableList<Account> accounts = FXCollections.observableArrayList();

	@FXML
	private TextField amountTextField;

	@FXML
	private Label errorMessageLabel;

	@FXML
	private Button submitButton;

	@FXML
	void confirmAmountButtonPressed(ActionEvent event) {
		try {
			BigDecimal input = new BigDecimal(amountTextField.getText());

			if (!(input.compareTo(BigDecimal.ZERO) > 0.0)) {
				errorMessageLabel.setText("Please enter a valid amount");
			}

			else {
				// if transaction type is a withdrawal
				if (transactionType == TransactionType.WITHDRAWAL && (getCurrentAccount().getAvailableBalance()
						.subtract(input).compareTo(BigDecimal.ZERO) < 0.0)) {
					errorMessageLabel.setText("Withdrawal amount exceeds available balance.");
				} else {
					setAmount(input);

					amountTextField.setText(currency.format(getAmount()));

					errorMessageLabel.setText(null);
					submitButton.setDisable(false);
					amountTextField.setDisable(true);
				}

			}
		} catch (NumberFormatException exception) {
			errorMessageLabel.setText("Please enter a valid amount");
		} catch (NullPointerException exception) {
			errorMessageLabel.setText("Please select an account for withdrawal.");
		}
	}

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
	void submitButtonPressed(ActionEvent event) {
		String type = (transactionType == TransactionType.WITHDRAWAL) ? "withdrawal" : "deposit";
		try {
			System.out.println("submitting " + type + " for " + currency.format(amount) + " from account "
					+ getCurrentAccount().getAccountNumber());

			if (transactionType == TransactionType.WITHDRAWAL) {
				// perform withdraw transaction with validated amount
				try { // debit currentAccount
					currentUserSession.debit(getCurrentAccount(), getAmount());
					Transaction transaction = new Transaction(getCurrentAccount().getAccountNumber(),
							"Withdrawal Transaction", "debit", getAmount());
					transaction.addTransaction();
					Notification notification = new Notification(NotificationType.DEBIT,
							String.format("Withdrawal submitted for account %s", getCurrentAccount().getName()));
					currentUserSession.addNotification(notification);
				} catch (SQLException exception) {
					System.out.printf("Transaction failed.%n");
					exception.printStackTrace();
				}
			} else {
				// perform deposit transaction with validated amount
				try { // credit currentAccount
					currentUserSession.credit(getCurrentAccount(), getAmount());
					Transaction transaction = new Transaction(getCurrentAccount().getAccountNumber(),
							"Deposit Transaction", "credit", getAmount());
					transaction.addTransaction();
					Notification notification = new Notification(NotificationType.CREDIT,
							String.format("Deposit submitted for account %s", getCurrentAccount().getName()));
					currentUserSession.addNotification(notification);
				} catch (SQLException exception) {
					System.out.printf("Transaction failed.%n");
					exception.printStackTrace();
				}
			}

			submitButton.setDisable(true);

			try {
				switchToAccountView(event);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		} catch (NullPointerException exception) {
			errorMessageLabel.setText("Please select an account for withdrawal.");
		}
	}

	public void initialize() {

		typeLabel.setText((transactionType == TransactionType.DEPOSIT) ? "Deposit" : "Withdrawal");

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
}
