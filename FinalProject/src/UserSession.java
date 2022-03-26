
/**
 * UserSession class represents all data required to accommodate a user session
 * 
 * @author owner
 *
 */

import java.sql.SQLException;
import java.util.ArrayList;

public class UserSession { // holds all objects associated with a given user session

	private User user;
	private ArrayList<Account> accounts;

	private DatabaseConnection databaseConnection;

	public UserSession(User user) throws SQLException {
		this.user = user;
		this.accounts = databaseConnection.getAccounts();
	}
	
	@Override
	public String toString() {
		return String.format("UserSession for user %s:%n%s", user.getUsername(), 
				user.toString());
	}
}
