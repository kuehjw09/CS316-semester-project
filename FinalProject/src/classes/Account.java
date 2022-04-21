/**
 * CS316 Final Project
 * Account.java
 * 
 * Account class represents a banking application account. 
 * 
 * @author owner
 *
 */

package classes;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Stream;

import app.DatabaseConnection;

public class Account {
	private String name; // account name
	private int accountNumber; // account number
	private String type; // checking or savings
	private BigDecimal availableBalance;
	private BigDecimal totalBalance;
	private ArrayList<Transaction> transactions; // transactions associated with account
	public boolean isDefault; // whether this is a default account or not

	// constructor
	public Account(String name, int accountNumber, BigDecimal availableBalance, BigDecimal totalBalance)
			throws SQLException {
		this.name = name;
		this.accountNumber = accountNumber;
		this.availableBalance = availableBalance;
		this.totalBalance = totalBalance;
		this.transactions = getTransactions();
		
		isDefault();
	}

	// alternate constructor accepts a ResultSet object
	public Account(ResultSet resultSet) throws SQLException {
		this.name = resultSet.getString(2);
		this.accountNumber = resultSet.getInt(5);
		this.availableBalance = resultSet.getBigDecimal(6);
		this.totalBalance = resultSet.getBigDecimal(7);
		this.transactions = getTransactions();
		
		isDefault();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	 * Returns an ArrayList of Transaction objects associated with this Account object.
	 * 
	 * @return ArrayList<Transaction>
	 * @throws SQLException
	 */
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

	/**
	 * Determine whether this is a default account; Call setIsDefault to reflect this Account's default status.
	 * 
	 * @throws SQLException
	 */
	public void isDefault() throws SQLException {
		Connection connection = DatabaseConnection.getConnection();

		String selectString = "SELECT default_account FROM DB2.Users where default_account = ? ";

		try (PreparedStatement selectStatement = connection.prepareStatement(selectString)) {
			selectStatement.setInt(1, accountNumber);
			ResultSet resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				setIsDefault(true);
			} else {
				setIsDefault(false);
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public String getIsDefault() {
		return (isDefault) ? "(default)" : "";
	}
	
	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * This method filters the ArrayList<Transaction> and reduces the total pending withdrawals 
	 * to a BigDecimal amount.
	 *  
	 * @return BigDecimal amount
	 */
	public BigDecimal getPendingWithdrawalsAmount() {
		// count and return the total amount of pending withdrawals with debit status
		BigDecimal amount = transactions.stream().filter((transaction) -> {
			return transaction.getStatus().equalsIgnoreCase("pending");
		}).filter((transaction) -> {
			return transaction.getType().equalsIgnoreCase("debit");
		}).map(Transaction::getAmount).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

		return amount;

	}

	/**
	 * This method filters the ArrayList<Transaction> and reduces the total pending deposits 
	 * to a BigDecimal amount.
	 *  
	 * @return BigDecimal amount
	 */
	public BigDecimal getPendingDepositsAmount() {
		// count and return the total amount of pending deposits with credit status
		BigDecimal amount = transactions.stream().filter((transaction) -> {
			return transaction.getStatus().equalsIgnoreCase("pending");
		}).filter((transaction) -> {
			return transaction.getType().equalsIgnoreCase("credit");
		}).map(Transaction::getAmount).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

		return amount;
	}

	/**
	 * Credit this Account with the amount passed to this method; Update the database by calling UpdateTotals method.
	 * 
	 * @param amount
	 * @throws SQLException
	 */
	public void credit(BigDecimal amount) throws SQLException {
		setTotalBalance(totalBalance.add(amount)); // add amount to totalBalance
		updateTotals(); // update Accounts table to reflect changes

	}

	/**
	 * Debit this Account with the amount passed to this method; Update the database by calling UpdateTotals method.
	 * @param amount
	 * @throws SQLException
	 */
	public void debit(BigDecimal amount) throws SQLException {
		setAvailableBalance(availableBalance.subtract(amount)); // subtract amount from availableBalance
		updateTotals(); // update Accounts table to reflect changes
	}

	/**
	 * This method calls AccountDatabase static method getConnection() to obtain a
	 * connection to the project database. When called, the method will select the
	 * row in the Accounts table of the project database and update the appropriate
	 * columns representing the available balance and total balance to match the
	 * corresponding Account object.
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
			System.out.printf("Database updated successfully.%n%n");
			createStatement.close();

			setTransactions(getTransactions()); // get most current transaction data
		} catch (SQLException e) {
			System.out.printf("UPDATE Query failed.%n");
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
	/**
	 * This method changes the name associated with this Account object and updates it's corresponding row in the Accounts table 
	 * accordingly . 
	 * 
	 * @param name
	 * @throws SQLException
	 */
	public void renameAccount(String name) throws SQLException {
		setName(name);
		
		Connection connection = DatabaseConnection.getConnection();

		String createString = "UPDATE DB2.Accounts SET account_name = ? "
				+ "WHERE account_number = ? ;";

		try (PreparedStatement createStatement = connection.prepareStatement(createString)) {
			createStatement.setString(1, name);
			createStatement.setInt(2, accountNumber);

			createStatement.executeUpdate();
			System.out.printf("Database updated successfully.%n%n");
			createStatement.close();

			setTransactions(getTransactions()); // get most current transaction data
		} catch (SQLException e) {
			System.out.printf("Update failed.%n");
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/**
	 * Return a String representation of an Account object
	 */
	@Override
	public String toString() {
		return String.format("%s...%04d\t%s%n" + "Available Balance: $%.2f", getName(), getAccountNumber() % 110000,
				getIsDefault(), getAvailableBalance());
	}

}
