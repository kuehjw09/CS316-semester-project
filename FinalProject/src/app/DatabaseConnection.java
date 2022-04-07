package app;

/**
 * DatabaseConnection class represents the database connection for an application session.
 *  - Connects to the database and provides methods for interacting with the database within the application
 * 
 * @author kuehjw09
 *
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.Account;
import classes.User;
import classes.UserSession;

public class DatabaseConnection {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	// declare private static UserSession
	private static UserSession currentUserSession; 
	private User currentUser;

	// final static hostname for use in AccountDatabase methods
	final static String DATABASE_URL = "jdbc:mysql://project-database.cfkat6ss9oqw.us-east-2.rds.amazonaws.com/Project_Database?";

	// keep track of database connection status
	private boolean connectedToDatabase = false; // use this as a flag when executing database methods to ensure
													// connection
	
	// no-argument constructor initializes database connection
	public DatabaseConnection() throws SQLException {
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "admin", "adminpassword");

			connectedToDatabase = true;
			System.out.printf("Connected to database%n%n");
			callUpdateTotalsProcedure();

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * When called, this method will call the UPDATE_TOTALS() stored procedure
	 * defined in the schema for DB2. The stored procedure simulates
	 * the update of pending funds to available funds in a bank database. 
	 * 
	 */
	public void callUpdateTotalsProcedure() {
		try {
			// establish the database connection with credentials 
			connection = DriverManager.getConnection(DATABASE_URL, "admin", "adminpassword");
			
			// call a stored procedure to update the totals in the database
			CallableStatement cs = connection.prepareCall("CALL DB2.UPDATE_TOTALS;");
																					
			cs.executeUpdate();
			System.out.printf("executed update totals procedure%n%n");

		} catch (SQLException exception) {
			System.out.printf("Stored procedure call failed.");
			exception.printStackTrace();
		}
	}

	/**
	 * This method executes a query to select the row in the Users table of DB2
	 * database with a username attribute matching the string passed to this method.
	 * If a row matching the criteria is returned, this method will instatiate a
	 * User object with the data returned and then return the object.
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	private User getUser(String username) throws SQLException {
		String queryString = "SELECT * FROM DB2.Users WHERE username = ? ";
		try (PreparedStatement queryStatement = connection.prepareStatement(queryString)) {
			queryStatement.setString(1, username);

			resultSet = queryStatement.executeQuery();

			if (resultSet.next()) { // if the query returned a value
				User user = new User(resultSet); // create a User object
				return user; // return User object to caller

			}

			queryStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null; // if no matching username was found, return null
	}
	
	/**
	 * This method calls the stored procedure that checks whether the password
	 * entered matches the password column of a row in the Users table with user_id
	 * equal to the user_id passed into this method. Returns true if the passwords
	 * match; returns false if otherwise.
	 * 
	 * @param user_id
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	private boolean verifyPassword(int user_id, String password) throws SQLException {
		String callString = "CALL DB2.CHECK_PASS(?, MD5(?), ?)";
		try (CallableStatement cs = connection.prepareCall(callString)) {
			cs.setInt(1, user_id);
			cs.setString(2, password);
			cs.registerOutParameter(3, java.sql.Types.BOOLEAN);
			cs.execute();

			boolean verified = cs.getBoolean(3);

			if (verified) {
				return true;
			}

			cs.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}

		return false;
	}

	/*
	 * Searches the database for an occurance of the account number passed into this
	 * method. Returns true if an account was found matching the parameter.
	 */
	public boolean search(int accountNumber) throws SQLException {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM DB2.Accounts WHERE account_number = " + accountNumber);

			if (resultSet.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; // if no matching account was found
	}

	/**
	 * Called from LoginController when a user attempts to sign into the
	 * application.
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public boolean authenticateUser(String username, String password) throws SQLException {
		// attempt to retrieve the account with provided username
		currentUser = getUser(username);

		// if user exists, return result of verifyPassword
		if (currentUser != null) {
			return verifyPassword(currentUser.getUser_id(), password);
		}

		return false;
	}

	/**
	 * This method will return a UserSession object with the
	 * validated user information obtained during successful login procedure.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public UserSession getUserSession() throws SQLException {
		currentUserSession = new UserSession(currentUser, getAccounts(currentUser));
		
		return currentUserSession;
	}

	/**
	 * Populate an ArrayList with Account objects associated with a given user.
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Account> getAccounts(User user) throws SQLException {
		ArrayList<Account> accounts = new ArrayList<Account>();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT * FROM DB2.Accounts WHERE user_id = " + user.getUser_id() + " ORDER BY date_created DESC");
			while (resultSet.next()) {
				if (resultSet != null) { // must include to avoid null pointer exception
					accounts.add(new Account(resultSet));
				}
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}

		return accounts;
	}
	
	/**
	 * 
	 */
	public void addNewAccount(Account account, User user) {
		String createString = "CALL NEW_ACCOUNT( ? , ? , "
				+ "( SELECT user_id FROM Users WHERE username LIKE ? );";
		try(CallableStatement createStatement = connection.prepareCall(createString)) {
			createStatement.setString(1, account.getName());
			createStatement.setString(2, account.getType());
			createStatement.setString(3, user.getUsername());
			
			createStatement.executeUpdate();
			System.out.println("New Account added for user " + user.getUsername());
			createStatement.close();

		} catch (SQLException exception) {
			exception.printStackTrace();
		} 
	}

	/**
	 * Static method to obtain a Connection object reference to the application's
	 * database connection.
	 * 
	 * @return a Connection object
	 */
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

}
