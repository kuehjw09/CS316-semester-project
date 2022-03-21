
/**
 * Class AccountDatabase acts as the database connection for the application. This class establishes 
 * a connection to `Project_Database`, provides a static method for obtaining a connection outside of the
 * class, and supports CRUD operations on the database.
 * 
 * @author jkuehl
 */

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;

import javafx.collections.ObservableList;

public class AccountDatabase {
	// for interacting with sql
	private ResultSet resultSet;
	private Connection connection;
	private Statement statement;

	// final static hostname for use in AccountDatabase methods
	final static String DATABASE_URL = "jdbc:mysql://project-database.cfkat6ss9oqw.us-east-2.rds.amazonaws.com/Project_Database?";

	// keep track of database connection status
	private boolean connectedToDatabase = false;

	// keep track of times loaded in a session
	private static int count = 0;

	// provide static method to obtain a Connection object reference to a database
	// connection
	public static Connection getConnection() {
		Connection connection;
		try {
			// connect to database
			connection = DriverManager.getConnection(DATABASE_URL, "admin", "adminpassword");
			return connection;

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * When called, this method will call the UPDATE_TOTALS() stored procedure
	 * defined in the schema for `Project_Database`. The stored procedure simulates
	 * the update of pending funds to available funds in a bank database. The
	 * totalBalance of an Account object will match its availableBalance after this
	 * method is executed succesfully.
	 */
	public static void callUpdateTotalsProcedure() {
		Connection connection;

		try {
			connection = DriverManager.getConnection(DATABASE_URL, "admin", "adminpassword");
			// call the stored procedure UPDATE_TOTALS to update availableBalance to match
			// totalBalance on each account
			CallableStatement cs = connection.prepareCall("CALL UPDATE_TOTALS;"); // execute database update on first
																					// load
			cs.executeUpdate();
			System.out.printf("Successfully executed stored procedure call%n%n");

		} catch (SQLException exception) {
			System.out.printf("Stored procedure call failed.");
			exception.printStackTrace();
		}
	}

	// no-argument AccountDatabase constructor initializes database connection
	public AccountDatabase() throws SQLException {
		System.out.printf("_____________________________________________________________________________%n%n");
		System.out.printf("Connecting to database %s ...%n%n%n", DATABASE_URL);

		try {
			// connect to database
			connection = DriverManager.getConnection(DATABASE_URL, "admin", "adminpassword");

			connectedToDatabase = true;
			System.out.printf("Connected to `Project_Database`%n%n%n");

		} catch (SQLException exception) {
			exception.printStackTrace();
		}

		count++; // increment static session counter
		// only display Accounts table once
		if (connectedToDatabase && count == 1) {
			callUpdateTotalsProcedure();
			getAccountDatabase(resultSet); // display Accounts table for development and testing
			System.out.printf("%n%n_____________________________________________________________________________%n%n");
		}
	}

	/**
	 * This method performs a SELECT query on the database to return the row in the
	 * Accounts table with accountID equal to the integer passed into the method. If
	 * an account with that number exists in the table, the method returns an
	 * instance of Account with the data obtained from a ResultSet object.
	 * 
	 * @param accountNumber
	 * @return Account account
	 * @throws SQLException
	 */
	private Account getAccount(int accountNumber) throws SQLException {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Accounts WHERE accountID = " + accountNumber);
			if (resultSet.next()) { // if the query returned a value
				Account account = new Account(resultSet); // create an account object
				if (account.getAccountNumber() == accountNumber) {
					return account; // return Account object to caller
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null; // if no matching account was found, return null
	}

	/**
	 * This method is used primarily on account creation to ensure that the account
	 * number entered does not match any account numbers currently in the database.
	 * 
	 * @param accountNumber
	 * @return either true or false
	 * @throws SQLException
	 */
	public boolean search(int accountNumber) throws SQLException {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Accounts WHERE accountID = " + accountNumber);

			if (resultSet.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; // if no matching account was found
	}

	/**
	 * This method will create and return an ArrayList of all transactions
	 * associated with a given account.
	 * 
	 * @author jkuehl
	 * @param accountNumber
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Transaction> getTransactions(int accountNumber) throws SQLException {
		try {
			ArrayList<Transaction> list = new ArrayList<>();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Transactions" + " WHERE (accountID = " + accountNumber
					+ ") ORDER BY processed DESC");

			while (resultSet.next() && resultSet != null) { // while there is a row that is not null to read
				list.add(new Transaction(resultSet)); // add the row to the ArrayList
			}

			return list; // return the ArrayList

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null; // if nothing was read from the query, return null
	}

	/**
	 * This method will diplay a table view of Project_Database's Accounts table in
	 * the console for project development and testing.
	 * 
	 * @param resultSet
	 * @throws SQLException
	 */
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

			System.out.printf("Accounts Table of Project-Database:%n");

			// display the names of the columns in the ResultSet
			for (int i = 1; i <= numberOfColumns; i++) {
				System.out.printf("%-20s\t", metaData.getColumnName(i));
			}
			System.out.println();

			// display query results
			while (resultSet.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					if (i == 1 || i == 2) {
						String formatted = String.format("%05d", resultSet.getInt(i));
						System.out.printf("%-18s\t", formatted);

					} else if (i == 5) {
						System.out.printf("$%-18s\t", resultSet.getDate(i).toString());
					} else if (i == 6) {
						System.out.printf("$%-18s\t", resultSet.getTimestamp(i).toString());
					} else {
						System.out.printf("$%-18s\t", resultSet.getObject(i).toString());

					}
				}

				System.out.println();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * This method is called to verify the account information. If an row with the
	 * accountID entered exists in the database, call validatePIN of the Account
	 * class to validate the PIN matches the Account. Otherwise, return false.
	 * 
	 * @param userAccountNumber
	 * @param userPIN
	 * @return
	 * @throws SQLException
	 */
	public boolean authenticateUser(int userAccountNumber, int userPIN) throws SQLException {
		// attempt to retrieve the account with the account number
		Account userAccount = getAccount(userAccountNumber);

		// if account exists, return result of Account method validatePIN
		if (userAccount != null) {
			return userAccount.validatePIN(userPIN);
		}

		return false;
	}

	/**
	 * This method performs an INSERT query on the database. Called when creating a
	 * new Account from the signup page.
	 * 
	 * @param accountNumber
	 * @param accountPIN
	 * @param totalBalance
	 * @param availableBalance
	 * @throws SQLException
	 */
	public void addRowToAccounts(Account account)
			throws SQLException {
		String createString = "INSERT INTO Accounts " + "(accountID, accountPIN, availableBalance, totalBalance )"
				+ " VALUES (?, ?, ?, ?)";

		try (PreparedStatement createStatement = connection.prepareStatement(createString)) {
			createStatement.setInt(1, account.getAccountNumber());
			createStatement.setInt(2, account.getPin());
			createStatement.setDouble(3, account.getAvailableBalance());
			createStatement.setDouble(4, account.getTotalBalance());

			createStatement.executeUpdate();
			System.out.printf("Project_Database updated successfully.%n%n");
			createStatement.close();

		} catch (SQLException e) {
			System.out.printf("%nINSERT Query failed.%n");

			e.printStackTrace();
		}

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
