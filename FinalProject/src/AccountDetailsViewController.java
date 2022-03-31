import java.sql.SQLException;
import java.text.NumberFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class AccountDetailsViewController {
	private static Account currentAccount;
	
	// formatter for currency
	private static final NumberFormat currency = NumberFormat.getCurrencyInstance();

	public static void setCurrentAccount(Account account) {
		currentAccount = account;
	}
	
	public Account getCurrentAccount() {
		return currentAccount;
	}
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label numberLabel;
	
	@FXML 
	private Label balanceLabel;
	
	@FXML
	private ListView<Transaction> transactionsListView;

	@FXML
	private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
	
	public void initialize() throws SQLException {
		// populate transactionsListView with ArrayList of currentAccount transactions
		for (Transaction transaction : getCurrentAccount().getTransactions()) {
			transactions.add(transaction);
		}
		
		transactionsListView.setItems(transactions);
		
		nameLabel.setText(getCurrentAccount().getName());
		numberLabel.setText("..." + (currentAccount.getAccountNumber() % 11000 ));
		balanceLabel.setText(currency.format(currentAccount.getAvailableBalance()));
	}
}
