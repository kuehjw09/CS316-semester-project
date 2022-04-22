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
import java.util.function.Function;

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
	
	public Account getDefaultAccount() {
		for (Account account : accounts) {
			if (account.isDefault) {
				return account;
			}
		}
		return null;
	}
	
	/**
	 * Call a DatabaseConnection method for adding a new account.
	 * 
	 * @param accountName
	 * @param accountType
	 * @throws SQLException
	 */
	public void addNewAccount(String accountName, String accountType) throws SQLException {
		// call the method to add a user
		databaseConnection.addNewAccount(accountName, accountType, user.getUser_id());
	
		// create a new notififcation
		Notification notification = new Notification(NotificationType.CREATE,
				String.format("New account created for %s", user.getUsername()));
		
		// add the notification to the ArrayList
		addNotification(notification);
	}
	
	/**
	 * Perform a transfer and call DatabaseConnection method for account deletion.
	 * 
	 * @param account
	 * @throws SQLException
	 */
	public void deleteAccount(Account account) throws SQLException {
		
		// credit the default account 
		credit(getDefaultAccount(), account.getAvailableBalance());
		
		// add pending deposits to ensure accurate balance
		getDefaultAccount().setTotalBalance(getDefaultAccount().getTotalBalance().add(account.getPendingDepositsAmount()));
		
		// execute the update associated with account deletion
		databaseConnection.deleteAccount(account.getAccountNumber(), getDefaultAccount().getAccountNumber());
		
		// create a new notification 
		Notification notification = new Notification(NotificationType.DELETE,
				String.format("Deleted Account %s", account.getName()));
		
		// add the notification to the ArrayList
		addNotification(notification);
	}
	
	public void credit(Account account, BigDecimal creditAmount) throws SQLException {
		account.credit(creditAmount);
		Transaction transaction = new Transaction(account.getAccountNumber(),
				"Deposit Transaction", "credit", creditAmount);
		transaction.addTransaction();
		Notification notification = new Notification(NotificationType.CREDIT,
				String.format("Deposit submitted for account %s", account.getName()));
		addNotification(notification);
	}
	
	public void debit(Account account, BigDecimal debitAmount) throws SQLException {
		account.debit(debitAmount);
		Transaction transaction = new Transaction(account.getAccountNumber(),
				"Withdrawal Transaction", "debit", debitAmount);
		transaction.addTransaction();
		Notification notification = new Notification(NotificationType.DEBIT,
				String.format("Withdrawal submitted for account %s", account.getName()));
		addNotification(notification);
	}
	
	/**
	 * Debit the amount from the first Account passed to this method, credit the second.
	 * 
	 * @param fromAccount
	 * @param toAccount
	 * @param amount
	 * @throws SQLException
	 */
	public void transfer(Account fromAccount, Account toAccount, BigDecimal amount) throws SQLException {
		debit(fromAccount, amount);
		credit(toAccount, amount);
		
		Notification notification = new Notification(NotificationType.TRANSFER,
				String.format("Transfer submitted from account %s to account %s", fromAccount.getName(), 
						toAccount.getName()));
		addNotification(notification);
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
