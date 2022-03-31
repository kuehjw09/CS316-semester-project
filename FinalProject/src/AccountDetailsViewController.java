import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
	
	@FXML
	void viewAllTransactionsClicked() {
		// view all transactions clicked
	}
	
	@FXML 
	private AnchorPane anchorPane;
	
	@FXML
	void returnButtonPressed(ActionEvent event) throws IOException {
		// return to AccountView
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountsView.fxml"));
		AnchorPane accountPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(accountPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
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