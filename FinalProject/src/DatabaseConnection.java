/**
 * DatabaseConnection class represents the database connection for an application session
 * 
 * @author owner
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

public class DatabaseConnection {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	
	private User currentUser;
	
	// final static hostname for use in AccountDatabase methods
	final static String DATABASE_URL = "jdbc:mysql://project-database.cfkat6ss9oqw.us-east-2.rds.amazonaws.com/Project_Database?";

	// keep track of database connection status
	private boolean connectedToDatabase = false;

	
	// connect to database and provide methods for interacting with the database
	
	public DatabaseConnection() throws SQLException {
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "admin", "adminpassword");

			connectedToDatabase = true;
			System.out.printf("Connected to database%n%n%n");

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
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
	
	
	public boolean search(int accountNumber) throws SQLException {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM DB2.Accounts WHERE accountID = " + accountNumber);

			if (resultSet.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; // if no matching account was found
	}
	
	public boolean authenticateUser(String username, String password) throws SQLException {
		// attempt to retrieve the account with provided username
		currentUser = getUser(username);

		// if user exists, return result of verifyPassword
		if (currentUser != null) {
			return verifyPassword(currentUser.getUser_id(), password);
		}

		return false;
	}
	
	public UserSession getUserSession() throws SQLException {
		UserSession currentUserSession = new UserSession(currentUser, getAccounts(currentUser));
		return currentUserSession;
	}
	
	public ArrayList<Account> getAccounts(User user) throws SQLException {
		ArrayList<Account> accounts = new ArrayList<Account>();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM DB2.Accounts WHERE user_id = " + user.getUser_id()
					+" ORDER BY date_created DESC");
			while (resultSet.next()) { 
				if (resultSet!= null) { // must include to avoid null pointer exception
					accounts.add(new Account(resultSet));
				}
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return accounts;
	}

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
}	

	


