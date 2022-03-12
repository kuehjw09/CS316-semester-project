
// AccountDatabase class represents ATM account information database
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;

public class AccountDatabase {
	private ResultSet resultSet;
	private Connection connection;
	private Statement statement;
	final String DATABASE_URL = "jdbc:mysql://project-database.cfkat6ss9oqw.us-east-2.rds.amazonaws.com/Project_Database?";

	// keep track of database connection status
	private boolean connectedToDatabase = false;

	// no-argument AccountDatabse constructor initializes accounts
	public AccountDatabase() throws SQLException {
		try {
			// connect to database
			connection = DriverManager.getConnection(DATABASE_URL, "admin", "adminpassword");

			connectedToDatabase = true;
			System.out.printf("Connected to Project_Database.%n%n%n");

		} catch (SQLException exception) {
			exception.printStackTrace();
		}

		if (connectedToDatabase) {
			getAccountDatabase(resultSet);

		}
	}

	// retrieve Account object containing specified account number
	private Account getAccount(int accountNumber) throws SQLException {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Accounts WHERE accountID = " + accountNumber);

			resultSet.next();
			Account account = new Account(resultSet);
			if (account.getAccountNumber() == accountNumber) {
				return account;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null; // if no matching account was found, return null
	}

	public void getAccountDatabase(ResultSet resultSet) throws SQLException {
		if (!connectedToDatabase) {
			throw new IllegalStateException("Not Connected to Database.");
		}

		try {
			// create Statement to query database
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Accounts");
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberOfColumns = metaData.getColumnCount();

			System.out.printf("Accounts Table of Project-Database:%n%n");

			// display the names of the columns in the ResultSet
			for (int i = 1; i <= numberOfColumns; i++) {
				System.out.printf("%-20s\t", metaData.getColumnName(i));
			}
			System.out.println();

			// display query results
			while (resultSet.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					System.out.printf("%-20s\t", resultSet.getObject(i));
				}

				System.out.println();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public boolean authenticateUser(int userAccountNumber, int userPIN) throws SQLException {
		// attempt to retrieve the account with the account number
		Account userAccount = getAccount(userAccountNumber);

		// if account exists, return result of Account method validatePIN
		if (userAccount != null) {
			return userAccount.validatePIN(userPIN);
		}

		return false;
	}

	// return available balance of Account with specified account number
	public double getAvailableBalance(int userAccountNumber) throws SQLException {
		return getAccount(userAccountNumber).getAvailableBalance();
	}

	// return total balance of Account with specified account number
	public double getTotalBalance(int userAccountNumber) throws SQLException {
		return getAccount(userAccountNumber).getTotalBalance();
	}

	// credit amount to account having userAccountNumber
	public void credit(int userAccountNumber, double amount) throws SQLException {
		getAccount(userAccountNumber).credit(amount);
	}

	// debit amount from account having userAccountNumber
	public void debit(int userAccountNumber, double amount) throws SQLException {
		getAccount(userAccountNumber).debit(amount);
	}

}
