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

public class DepositController {
	private int currentAccountNumber;
	private AccountDatabase accountDatabase;
	private double depositAmount;
	private double totalBalance;

	// fields to be used in scene change methods
	private Stage stage;
	private Scene scene;

	// formatter for currency
	private static final NumberFormat currency = NumberFormat.getCurrencyInstance();

	@FXML
	private Label pendingDepositLabel;

	@FXML
	private Label previewBalanceLabel;

	@FXML
	private Label totalBalanceLabel;

	@FXML
	private TextField amountTextField;

	@FXML
	void clearFormButtonPressed(ActionEvent event) {
		amountTextField.clear();
		pendingDepositLabel.setText("$0.00");
		previewBalanceLabel.setText(currency.format(totalBalance));
		depositAmount = 0;
	}

	@FXML
	void confirmDepositButtonPressed(ActionEvent event) throws SQLException, IOException {
		// perform deposit transaction with selected options

		if (depositAmount == 0) {
			throw new IllegalArgumentException("Must enter/select a value for deposit.");
		} else {
			try {
				accountDatabase.credit(currentAccountNumber, depositAmount);
				Transaction transaction = new Transaction(currentAccountNumber, depositAmount, "credit");
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
		depositAmount = 80;
	}

	@FXML
	void enterSelectionButtonPressed(ActionEvent event) {
		depositAmount = Double.valueOf(amountTextField.getText());
		amountTextField.setText(currency.format(depositAmount));
		pendingDepositLabel.setText(amountTextField.getText());
		previewBalanceLabel.setText(currency.format(totalBalance + depositAmount));
	}

	@FXML
	void fourtyButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(40));
		depositAmount = 40;
	}

	@FXML
	void hundredButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(100));
		depositAmount = 100;
	}

	@FXML
	void sixtyButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(60));
		depositAmount = 60;
	}

	@FXML
	void twentyButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(20));
		depositAmount = 20;
	}

	@FXML
	void twoHundredButtonPressed(ActionEvent event) {
		amountTextField.setText(currency.format(200));
		depositAmount = 200;
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
		this.totalBalance = accountDatabase.getTotalBalance(accountNumber);

		String formatted = String.format("%05d", currentAccountNumber);
		System.out.printf("Deposit form loaded with accountNo. %s%n", formatted);
		totalBalanceLabel.setText(currency.format(totalBalance));
		previewBalanceLabel.setText(currency.format(totalBalance));
	}
}
