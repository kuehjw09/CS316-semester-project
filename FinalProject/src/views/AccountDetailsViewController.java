package views;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import classes.Account;
import classes.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import resources.AccountListCell;

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
	
	public BigDecimal accountBalance = BigDecimal.ZERO;
	
	ArrayList<Transaction> historicalTransactions = new ArrayList<>();

	@FXML
	private Label nameLabel;

	@FXML
	private Label numberLabel;

	@FXML
	private Label balanceLabel;

	@FXML
	private Label totalBalanceLabel;

	@FXML
	private Label pendingDebitsLabel;

	@FXML
	private Label pendingCreditsLabel;
	
	@FXML
	private Label infoNameLabel;
	
	@FXML
	private Label infoTypeLabel;
	
	@FXML 
	private Label infoNumberLabel;
	
	@FXML
	private Label infoWithdrawalLabel;
	
	@FXML
	private Label infoDepositLabel;

	@FXML
	private ListView<Transaction> transactionsListView;

	@FXML
	private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
	
	@FXML
	private LineChart<String, BigDecimal> accountLineChart;
	
	@FXML
	void viewAllTransactionsClicked() {
		// not implemented 
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

	public void initialize() {
		// set labels
		totalBalanceLabel.setText(currency.format(currentAccount.getTotalBalance()));
		pendingDebitsLabel.setText(currency.format(currentAccount.getPendingWithdrawalsAmount().negate()));
		pendingCreditsLabel.setText("+" + currency.format(currentAccount.getPendingDepositsAmount()));
		
		infoNameLabel.setText(currentAccount.getName());
		infoTypeLabel.setText(currentAccount.getType());
		infoNumberLabel.setText(String.valueOf(currentAccount.getAccountNumber()));
 		infoDepositLabel.setText(currency.format(currentAccount.getAverageDepositAmount()));
 		infoWithdrawalLabel.setText(currency.format(currentAccount.getAverageWithdrawalAmount()));

		nameLabel.setText(getCurrentAccount().getName());
		numberLabel.setText("..." + String.format("%04d", currentAccount.getAccountNumber() % 11000));
		balanceLabel.setText(currency.format(currentAccount.getAvailableBalance()));
		
		try {
			buildTransactions();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		buildSeries();
	}
	
	// custom TimeStampComparator for sorting transactions by Timestamp
	public class TimeStampComparator implements Comparator<Transaction> {
		@Override
		public int compare(Transaction a, Transaction b) {
			Timestamp t1 = a.getTrueTimestamp();
			Timestamp t2 = b.getTrueTimestamp();
			
			return (t1.compareTo(t2) > 0) ? 1 : (t2.compareTo(t1) > 0) ? -1 : 0;
		}
	}
	
	public void buildSeries() {
		XYChart.Series<String, BigDecimal> series = new XYChart.Series<String, BigDecimal>();
		series.setName("Transactions");
		
		
		// sort the historicalTransactions for sequential processing
		Collections.sort(historicalTransactions, new TimeStampComparator());
		series.getData().add(new XYChart.Data<String, BigDecimal>(String.valueOf(BigDecimal.ZERO), BigDecimal.ZERO));
		int count = 1;
		for (Transaction transaction : historicalTransactions) {
			if (transaction.getType().equalsIgnoreCase("credit")) {
				accountBalance = accountBalance.add(transaction.getAmount());
			} else {
				accountBalance = accountBalance.subtract(transaction.getAmount());
			}
			series.getData().add(new XYChart.Data<String, BigDecimal>(String.valueOf(count), accountBalance));
			count++;
		}
		
		accountLineChart.getData().add(series);
	}
	
	public void buildTransactions() throws SQLException {
		// populate transactionsListView with ArrayList of currentAccount transactions
		for (Transaction transaction : getCurrentAccount().getTransactions()) {
			transactions.add(transaction);
			historicalTransactions.add(transaction);
		}

		Collections.reverse(transactions); // descending order

		transactionsListView.setItems(transactions);
	}
}
