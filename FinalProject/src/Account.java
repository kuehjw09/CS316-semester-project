
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
import java.util.stream.Stream;

public class Account {
	private String name;
	private int accountNumber;
	// private enum Type {CHECKING, SAVINGS}; // may become subclasses of Account
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

	/**
	 * 
	 * @return size of the ArrayList transactions
	 */
	public int getTransactionsCount() {
		return transactions.size();
	}

	public ArrayList<Transaction> getTransactions() throws SQLException {
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		Connection connection = DatabaseConnection.getConnection();

		String selectString = "SELECT * FROM DB2.Transactions WHERE account_number = ?";

		try (PreparedStatement selectStatement = connection.prepareStatement(selectString)) {
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

	public BigDecimal getPendingWithdrawalsAmount() {
		// count and return the total amount of pending withdrawals with debit status
		Double amount = transactions.stream().filter((transaction) -> {
			return transaction.getStatus().equalsIgnoreCase("pending");}).filter((transaction) -> {
			return transaction.getType().equalsIgnoreCase("debit");
		}).map(Transaction::getAmount).reduce(0.0, (a, b) -> a + b);

		return new BigDecimal(amount);

	}

	public BigDecimal getPendingDepositsAmount() {
		// count and return the total amount of pending deposits with credit status
		Double amount = transactions.stream().filter((transaction) -> {
				return transaction.getStatus().equalsIgnoreCase("pending");}).filter((transaction) -> {
				return transaction.getType().equalsIgnoreCase("credit");
			}).map(Transaction::getAmount).reduce(0.0, (a, b) -> a + b);
	

		return new BigDecimal(amount);
	}

	public void credit(double amount) throws SQLException {
		setTotalBalance(totalBalance.add(new BigDecimal(amount))); // add amount to totalBalance
		updateTotals(); // update Accounts table to reflect changes

	}

	public void debit(double amount) throws SQLException {
		setAvailableBalance(availableBalance.subtract(new BigDecimal(amount))); // subtract amount from availableBalance
		updateTotals(); // update Accounts table to reflect changes
	}

	/**
	 * This method calls AccountDatabase static method getConnection() to obtain a
	 * connection to the project database. When called, the method will select the
	 * row in the Accounts table of the project database and update the appropriate
	 * columns representing the available balance and total balance of the row
	 * matching the corresponding accountNumber of the Account object that was
	 * called.
	 * 
	 * @throws SQLException
	 */
	public void updateTotals() throws SQLException {
		Connection connection = DatabaseConnection.getConnection();

		String createString = "UPDATE DB2.Accounts SET available_balance = ?, total_balance = ? "
				+ "WHERE account_number = ? ;";

		try (PreparedStatement createStatement = connection.prepareStatement(createString)) {
			createStatement.setBigDecimal(1, availableBalance);
			createStatement.setBigDecimal(2, totalBalance);
			createStatement.setInt(3, accountNumber);

			createStatement.executeUpdate();
			System.out.printf("Project_Database updated successfully.%n%n");
			createStatement.close();

			setTransactions(getTransactions()); // get most current transaction data
		} catch (SQLException e) {
			System.out.printf("UPDATE Query failed.%n");
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	@Override
	public String toString() {
		return String.format("%s...%s%n" + "Available Balance: $%.2f%n", getName(), getAccountNumber() % 110000,
				getAvailableBalance());
	}

}
