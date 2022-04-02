/**
 * Account class represents a bank account
 * @author owner
 *
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Account {
	private String name;
	private int accountNumber;
	//	private enum Type {CHECKING, SAVINGS}; // may become subclasses of Account
	private BigDecimal availableBalance;
	private BigDecimal totalBalance;
	private ArrayList<Transaction> transactions;

	// constructor
	public Account(String name, int accountNumber, BigDecimal availableBalance, BigDecimal totalBalance)
			throws SQLException {
		this.name = name;
		this.accountNumber = accountNumber;
		this.availableBalance = availableBalance;
		this.totalBalance = totalBalance;
		this.transactions = getTransactions();
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
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		Connection connection = DatabaseConnection.getConnection();

		String selectString = "SELECT * FROM DB2.Transactions WHERE account_number = ?";

		try (PreparedStatement selectStatement = connection.prepareStatement(selectString)){
			selectStatement.setInt(1, accountNumber);
			ResultSet resultSet = selectStatement.executeQuery();
			while (resultSet.next()) {
				if (resultSet != null) {
					transactions.add(new Transaction(resultSet));
				}
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return transactions;
	}
	
	public void setTransactions(ArrayList<Transaction> transactions) throws SQLException {
		this.transactions = transactions;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s...%s%n"
				+ "Available Balance: $%.2f%n",
				getName(), getAccountNumber() % 110000, getAvailableBalance());
	}
	
}
