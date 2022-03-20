import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

// Account class represents an online ATM account

public class Account {
	private int accountNumber; // account number
	private int pin; // PIN for authentication
	private double availableBalance; // funds available for withdrawal
	private double totalBalance; // funds available + pending deposits
	private Date dateCreated; // sql Date object corresponding to table Accounts
	private Timestamp lastUpdated; // sql Timestamp object corresponding to table Accounts

	// constructor
	public Account(int accountNumber, int pin, double availableBalance, double totalBalance) {
		this.accountNumber = accountNumber;
		this.pin = pin;
		this.availableBalance = availableBalance;
		this.totalBalance = totalBalance;
	}

	// Overloaded constructor when passed a Result Set
	public Account(ResultSet resultSet) throws SQLException {
		this.accountNumber = resultSet.getInt(1);
		this.pin = resultSet.getInt(2);
		this.availableBalance = resultSet.getDouble(3);
		this.totalBalance = resultSet.getDouble(4);
//		this.dateCreated = resultSet.getDate(5);
//		this.lastUpdated = resultSet.getTimestamp(6);
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public boolean validatePIN(int userPIN) {
		if (userPIN == pin) {
			return true;
		}
		return false;
	}

	public void credit(double amount) throws SQLException {
		totalBalance += amount; // add amount to totalBalance
		updateTotals(); // update Accounts table to reflect changes
	}

	public void debit(double amount) throws SQLException {
		availableBalance -= amount; // subtract amount from availableBalance
		totalBalance -= amount; // subtract amount from totalBalance
		updateTotals(); // update Accounts table to reflect changes

	}

	/**
	 * This method calls AccountDatabase static method getConnection() to obtain a connection to the 
	 * project database. When called, the method will select the row in the Accounts table of the project 
	 * database and update the appropriate columns representing the available balance and total balance of the 
	 * row matching the accountNumber of the Account object that was called. 
	 * 
	 * @throws SQLException
	 */
	public void updateTotals() throws SQLException {
		Connection connection = AccountDatabase.getConnection();

		String createString = "UPDATE Accounts SET availableBalance = ?, totalBalance = ? " 
								+ "WHERE accountID = ? ;";

		try (PreparedStatement createStatement = connection.prepareStatement(createString)) {
			createStatement.setDouble(1, availableBalance);
			createStatement.setDouble(2, totalBalance);
			createStatement.setInt(3, accountNumber);

			createStatement.executeUpdate();
			System.out.printf("Project_Database updated successfully.%n%n");
			createStatement.close();

		} catch (SQLException e) {
			System.out.printf("UPDATE Query failed.%n");
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
}
