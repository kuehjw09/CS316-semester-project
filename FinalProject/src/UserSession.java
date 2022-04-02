
/**
 * UserSession class represents all data required to accommodate a user session
 * 	- holds all objects associated with a given user session
 * 
 * @author kuehjw09
 *
 */

import java.sql.SQLException;
import java.util.ArrayList;

public class UserSession { 
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	private User user; // validated user
	private ArrayList<Account> accounts; // accounts associated with validated user

	// constructor
	public UserSession(User user, ArrayList<Account> accounts) throws SQLException {
		this.user = user;
		this.accounts = accounts;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	/*
	 * return a count of Accounts associated with a validated User
	 */
	public int getCountOfAccounts() {
		return accounts.size();
	}
	
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
	public void credit(Account account, double creditAmount) throws SQLException {
		account.credit(creditAmount);

	}
	
	public void debit(Account account, double debitAmount) throws SQLException {
		account.debit(debitAmount);
	}
	
	public void updateUserSession() {
		try {
			setAccounts(databaseConnection.getAccounts(getUser()));
			System.out.println("UserSession Accounts updated");
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * return a String representation of a UserSession object
	 */
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
