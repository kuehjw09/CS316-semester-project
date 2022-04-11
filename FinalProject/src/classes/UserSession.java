package classes;

import java.math.BigDecimal;

/**
 * UserSession class represents all data required to accommodate a user session
 * 	- holds all objects associated with a given user session
 * 
 * @author kuehjw09
 *
 */

import java.sql.SQLException;
import java.util.ArrayList;

import app.DatabaseConnection;

public class UserSession { 
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	private User user; // validated user
	private ArrayList<Account> accounts; // accounts associated with validated user
	private ArrayList<Notification> notifications = new ArrayList<>();

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
	
	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
	
	public void addNotification(Notification notification) {
		System.out.println("adding 1 new notification");
		notifications.add(notification);
	}
	
	public ArrayList<Notification> getNotifications() {
		return notifications;
	}
	
	public void clearNotifications() {
		notifications.clear();
	}
	
	// called to insert a new account
	public void addNewAccount(Account account) throws SQLException {
		// call the method to add a user
		databaseConnection.addNewAccount(account, user);
		
		// create a new notififcation
		Notification notification = new Notification(NotificationType.CREATE,
				String.format("New account created for %s", user.getUsername()));
		
		// add the notification to the ArrayList
		addNotification(notification);
	}
	
	public void credit(Account account, BigDecimal creditAmount) throws SQLException {
		account.credit(creditAmount);

	}
	
	public void debit(Account account, BigDecimal debitAmount) throws SQLException {
		account.debit(debitAmount);
	}
	
	public void updateUserSession() {
		try {
			setAccounts(databaseConnection.getAccounts(getUser()));
			System.out.println("UserSession updated");
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
		return String.format("UserSession for user %s:%n%snumber of accounts: %d%nnumber of notifications: %d%n%n%s", user.getUsername(), 
				user.toString(), accounts.size(), notifications.size(), accountString);
	}
}