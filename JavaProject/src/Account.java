import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

	public void credit(double amount) {
		totalBalance += amount; // add amount to totalBalance
	}

	public void debit(double amount) {
		availableBalance -= amount; // subtract amount from availableBalance
		totalBalance -= amount; // subtract amount from totalBalance
	}

}
