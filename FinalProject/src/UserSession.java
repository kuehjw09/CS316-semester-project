
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

	public UserSession(User user, ArrayList<Account> accounts) throws SQLException {
		this.user = user;
		this.accounts = accounts;
	}
	
	public int getCountOfAccounts() {
		return accounts.size();
	}
	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
 	
	@Override
	public String toString() {
		String accountString = "Summary of accounts:\n";
		for (Account account : accounts) {
			accountString += account;
		}
		return String.format("UserSession for user %s:%n%snumber of accounts: %d%n%n%s", user.getUsername(), 
				user.toString(), getCountOfAccounts(), accountString);
	}
}
