import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WithdrawalController {
	private int currentAccountNumber;
	private AccountDatabase accountDatabase;
	private double withdrawAmount;
	private double availableBalance;

	// fields to be used in scene change methods
	private Stage stage;
	private Scene scene;

	// formatter for currency
	private static final NumberFormat currency = NumberFormat.getCurrencyInstance();

	@FXML
	private Label pendingWithdrawalLabel;

	@FXML
	private Label previewBalanceLabel;

	@FXML
	private Label totalBalanceLabel;
	
	@FXML 
	private Label messageLabel;

	@FXML
	private TextField amountTextField;

	@FXML
	void clearFormButtonPressed(ActionEvent event) {
		amountTextField.clear();
		messageLabel.setText("");
		pendingWithdrawalLabel.setText("$0.00");
		previewBalanceLabel.setText(currency.format(availableBalance));
		withdrawAmount = 0;
	}

	@FXML
	void confirmWithdrawalButtonPressed(ActionEvent event) throws SQLException, IOException {
		// perform deposit transaction with selected options

		if (withdrawAmount == 0) {
			messageLabel.setText("Must enter/select a value for deposit.");
		} else {
			try {
				accountDatabase.debit(currentAccountNumber, withdrawAmount); 
				Transaction transaction = new Transaction(currentAccountNumber, withdrawAmount, "debit");
				transaction.addTransaction();
			} catch (SQLException exception) {
				System.out.printf("Transaction failed.%n");
				exception.printStackTrace();
			}
			switchToDashboard(event);
		}
	}

	@FXML
	void eightyButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(80));
		withdrawAmount = 80;
	}

	@FXML
	void enterSelectionButtonPressed(ActionEvent event) {
		if (withdrawAmount == 0) {
			withdrawAmount = Double.valueOf(amountTextField.getText());
		}
		if (withdrawAmount > availableBalance) { // ammount cannot exceed available balance
			messageLabel.setText("Amount exceeds available balance.");
			amountTextField.clear();
			withdrawAmount = 0;
		} else {
			messageLabel.setText("");
			amountTextField.setText(currency.format(withdrawAmount));
			pendingWithdrawalLabel.setText(amountTextField.getText());
			previewBalanceLabel.setText(currency.format(availableBalance - withdrawAmount));
		}
	}

	@FXML
	void fourtyButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(40));
		withdrawAmount = 40;
	}

	@FXML
	void hundredButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(100));
		withdrawAmount = 100;
	}

	@FXML
	void sixtyButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(60));
		withdrawAmount = 60;
	}

	@FXML
	void twentyButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(20));
		withdrawAmount = 20;
	}

	@FXML
	void twoHundredButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(200));
		withdrawAmount = 200;
	}

	/**
	 * This method attaches a new scene graph to the stage to display the Dashboard
	 * when called
	 * 
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	public void switchToDashboard(ActionEvent event) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Dashboard.fxml"));
		Parent root = loader.load();

		scene = new Scene(root);

		// access the controller and call a method
		DashboardController controller = loader.getController();
		controller.initializeData(currentAccountNumber, accountDatabase); // passing current account number and database

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}

	public void initializeData(int accountNumber, AccountDatabase accountDatabase) throws SQLException {
		this.currentAccountNumber = accountNumber;
		this.accountDatabase = accountDatabase;
		this.availableBalance = accountDatabase.getAvailableBalance(accountNumber);

		String formatted = String.format("%05d", currentAccountNumber);
		System.out.printf("Deposit form loaded with accountNo. %s%n%n", formatted);
		totalBalanceLabel.setText(currency.format(availableBalance));
		previewBalanceLabel.setText(currency.format(availableBalance));
	}
}
