/**
 * Account class represents a bank account
 * @author owner
 *
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Account {
	private String name;
	private int accountNumber;
//	private enum Type {CHECKING, SAVINGS}; // may become subclasses of Account
	private BigDecimal availableBalance;
	private BigDecimal totalBalance;
	private ArrayList<Transaction> transactions;
	
	// need a reference to a database connection in order to call getTransactions()
	private DatabaseConnection databaseConnection;

	
	// constructor
	public Account(String name, int accountNumber, BigDecimal availableBalance, BigDecimal totalBalance) {
		this.name = name;
		this.accountNumber = accountNumber;
		this.availableBalance = availableBalance;
		this.totalBalance = totalBalance;
	}
	
	public Account(ResultSet resultSet) throws SQLException {
		this.name = resultSet.getString(2);
		this.accountNumber = resultSet.getInt(5);
		this.availableBalance = resultSet.getBigDecimal(6);
		this.totalBalance = resultSet.getBigDecimal(7);
		this.transactions = getTransactions(); 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public int getTransactionsCount() {
		return transactions.size();
	}
	
	public ArrayList<Transaction> getTransactions() throws SQLException {
		return databaseConnection.getTransactions(accountNumber);
	}

	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	
	@Override
	public String toString() {
		return String.format("Account Name: %s%nAccount Number: %s%n"
				+ "Available Balance: %.2f%nTotalBalance: %s%nNumber of Transactions: %d%n%n",
				getName(), getAccountNumber(), getAvailableBalance(), getTotalBalance(),
				getTransactionsCount());
	}
	
}
