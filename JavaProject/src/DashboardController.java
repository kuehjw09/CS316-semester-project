/**
 * FXML Controller Class for Dashboard.fxml
 * 
 * @author jkuehl
 *
 */
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class DashboardController {
	private int currentAccountNumber;
	private AccountDatabase accountDatabase;
	
	// fields to be used in scene change methods
	private Stage stage;
	private Scene scene;
	
	@FXML
	private Label AvailableBalanceLabel;
	
	@FXML
	private Label TotalBalanceLabel;
	
	@FXML
	private Label PendingFundsLabel;
	
	@FXML 
	private Label AccountNumberLabel;
	
	@FXML
	private ListView<Transaction> transactionsListView;
	
	// stores the list of Transaction objects
	private final ObservableList<Transaction> transactions =
			FXCollections.observableArrayList();
	
	/**
	 * This method will switch back to the login scene when a user selects the log out button.
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void switchToLogin(ActionEvent event) throws IOException {
		String formatted = String.format("%05d", currentAccountNumber);
		System.out.printf("%nAccountNo. %s logging out...%n", formatted);
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This method will display to the withdrawal form when the appropriate button is pressed. 
	 * The form controller will be initialized with the current account number and database connection.
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	public void withdrawalButtonPressed(ActionEvent event) throws IOException, SQLException {
		// withdrawal button pressed
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Withdrawal.fxml"));
		Parent root = loader.load();
		
		scene = new Scene(root);
		
		// access the controller and call a method
		WithdrawalController controller = loader.getController();
		controller.initializeData(currentAccountNumber, accountDatabase); // passing current account number and database

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This method will display to the deposit form when the appropriate button is pressed. 
	 * The form controller will be initialized with the current account number and database connection.
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	public void depositButtonPressed(ActionEvent event) throws IOException, SQLException {
		// depositButtonPressed
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Deposit.fxml"));
		Parent root = loader.load();
		
		scene = new Scene(root);
		
		// access the controller and call a method
		DepositController controller = loader.getController();
		controller.initializeData(currentAccountNumber, accountDatabase); // passing current account number and database

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This method will display to the transfer form when the appropriate button is pressed. 
	 * The form controller will be initialized with the current account number and database connection.
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	public void transferButtonPressed(ActionEvent event) throws IOException, SQLException {
		// transfer button pressed
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Transfer.fxml"));
		Parent root = loader.load();
		
		scene = new Scene(root);
		
		// access the controller and call a method
		TransferController controller = loader.getController();
		controller.initializeData(currentAccountNumber, accountDatabase); // passing current account number and database

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
 	
	/**
	 * This method accepts an account number and database to initialize the view
	 * 
	 * @param account
	 * @throws SQLException 
	 */
	public void initializeData(int accountNumber, AccountDatabase database) throws SQLException {
		currentAccountNumber = accountNumber;
		accountDatabase = database;
		
		double availableBalance = accountDatabase.getAvailableBalance(currentAccountNumber);
		double totalBalance = accountDatabase.getTotalBalance(currentAccountNumber);
		double pendingFunds = totalBalance - availableBalance;
		
		String formatted = String.format("%05d", currentAccountNumber);
		AccountNumberLabel.setText("AccountNo. " + formatted);
		AvailableBalanceLabel.setText(String.format("$%.2f", availableBalance));
		TotalBalanceLabel.setText(String.format("$%.2f", totalBalance));
		PendingFundsLabel.setText(String.format("+$%.2f", pendingFunds));
		
		// Display account number when login is successful
		System.out.printf("%nDashboard loaded for AccountNo. %s%n", formatted);
		
		for (Transaction transaction : accountDatabase.getTransactions(accountNumber)) {
				transactions.add(transaction);
			
		}

		transactionsListView.setItems(transactions);
	}
}
