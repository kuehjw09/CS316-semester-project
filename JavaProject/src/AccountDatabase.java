// AccountDatabase class interacts with the account information database and supports CRUD operations on the database.
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
	private ResultSet resultSet;
	private Connection connection;
	private Statement statement;
	final String DATABASE_URL = "jdbc:mysql://project-database.cfkat6ss9oqw.us-east-2.rds.amazonaws.com/Project_Database?";

	// keep track of database connection status
	private boolean connectedToDatabase = false;
	
	// keep track of times loaded in a session
	private static int count = 0; 

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
		
		count++;
		// only display Accounts table once 
		if (connectedToDatabase && count == 1) {
			getAccountDatabase(resultSet); // display Accounts table for development and testing
			System.out.printf("%n%n_____________________________________________________________________________%n%n");

		}
	}

	/**
	 * This method queries the database to find a row in Accounts table with accountID equal to the 
	 * integer passed into the method. If an account with that number exists in the table, the method
	 * returns an instance of Account with the data obtained from the row in Accounts.
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
	 * This method is used primarily on account creation to ensure that the account number entered
	 * does not match any account numbers currently in the database.
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
	
	public ArrayList<Transaction> getTransactions(int accountNumber) throws SQLException {
		try {
			ArrayList<Transaction> list = new ArrayList<>();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Transactions"
					+ " WHERE (accountID = " + accountNumber + ")");

			
			while (resultSet.next() && resultSet != null) {
				list.add(new Transaction(resultSet));
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * This method will diplay a table view of Project_Database's Accounts table in the console.
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
					}
					else {
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
	 * This method is called when a user logs in to verify the account information they entered.
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
	 * This method performs an INSERT query on the database. Called when creating a new Account from the signup page.
	 * 
	 * @param accountNumber
	 * @param accountPIN
	 * @param totalBalance
	 * @param availableBalance
	 * @throws SQLException
	 */
	public void addRowToAccounts(int accountNumber, int accountPIN, 
			double totalBalance, double availableBalance) throws SQLException {
		String createString = "INSERT INTO Accounts "
				+ "(accountID, accountPIN, availableBalance, totalBalance )"
				+ " VALUES (?, ?, ?, ?)";

		try (PreparedStatement createStatement = connection.prepareStatement(createString)) {
		      createStatement.setInt(1, accountNumber);
		      createStatement.setInt(2, accountPIN);
		      createStatement.setDouble(3, availableBalance);
		      createStatement.setDouble(4, totalBalance);
		      

		      createStatement.executeUpdate();
		      System.out.printf("Project_Database updated successfully.%n%n");
		      createStatement.close();

		} catch (SQLException e) {
		     System.out.printf("%nINSERT Query failed.%n");

			e.printStackTrace();
		} 
		
	}
	
	/**
	 * 
	 * @param accountNumber
	 * @param availableBalance
	 * @throws SQLException
	 */
	public void withdrawal(int accountNumber, double availableBalance, double totalBalance) throws SQLException {
		String createString = "UPDATE Accounts SET availableBalance = ? "
				+ "SET totalBalance = ? "
				+ "WHERE accountID = ? ";
		
		try (PreparedStatement createStatement = connection.prepareStatement(createString)) {
			createStatement.setInt(1, accountNumber);
			createStatement.setDouble(2, availableBalance);
			createStatement.setDouble(3, totalBalance);
			
			createStatement.executeUpdate();
		    System.out.printf("Project_Database updated successfully.%n%n");
		    createStatement.close();

			
		} catch (SQLException e) {
			System.out.printf("UPDATE Query failed.%n");
			e.printStackTrace();
		}
	} 
	
	/**
	 * 
	 * @param accountNumber
	 * @param availableBalance
	 * @throws SQLException
	 */
	public void deposit(int accountNumber, double totalBalance) throws SQLException {
		String createString = "UPDATE Accounts SET totalBalance = ? WHERE accountID = ? ";

		try (PreparedStatement createStatement = connection.prepareStatement(createString)) {
			createStatement.setInt(1, accountNumber);
			createStatement.setDouble(2, totalBalance);
			
			createStatement.executeUpdate();
		    System.out.printf("Project_Database updated successfully.%n%n");
		    createStatement.close();
		    
		} catch (SQLException e) {
			System.out.printf("UPDATE Query failed.%n");
			e.printStackTrace();
		}
	} 
	
	
	public void transfer() throws SQLException {
		// transfer funds from one account to another
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
