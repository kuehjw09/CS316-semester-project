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
	private DatabaseConnection databaseConnection;
	private User user; // validated user
	private ArrayList<Account> accounts; // accounts associated with validated user
	private ArrayList<Notification> notifications = new ArrayList<>();

	// constructor
	public UserSession(User user, ArrayList<Account> accounts, DatabaseConnection connection) throws SQLException {
		this.user = user;
		this.accounts = accounts;
		this.databaseConnection = connection;
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

	public DatabaseConnection getDatabaseConnection() {
		return databaseConnection;
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
				String.format("Account %s (%s) created.", accountName, accountType));

		// add the notification to the ArrayList
		addNotification(notification);
	}

	/**
	 * Credit the default account with the avaialable balance of the Account passed
	 * to this method and call a DatabaseConnection method for account deletion.
	 * 
	 * @param account
	 * @throws SQLException
	 */
	public void deleteAccount(Account account) throws SQLException {

		// credit the default account
		credit(getDefaultAccount(), account.getAvailableBalance());

		// add pending deposits to ensure accurate balance
		getDefaultAccount()
				.setTotalBalance(getDefaultAccount().getTotalBalance().add(account.getPendingDepositsAmount()));

		// execute the update associated with account deletion
		databaseConnection.deleteAccount(account.getAccountNumber(), getDefaultAccount().getAccountNumber());

		// create a new notification
		Notification notification = new Notification(NotificationType.DELETE,
				String.format("Deleted Account %s", account.getName()));

		// add the notification to the ArrayList
		addNotification(notification);
	}

	public void credit(Account account, BigDecimal creditAmount) throws SQLException {
		// credit the account
		account.credit(creditAmount);

		// create a Transaction object and add it to the ArrayList<Transaction>
		Transaction transaction = new Transaction(account.getAccountNumber(), "Deposit Transaction", "credit",
				creditAmount);
		transaction.addTransaction();

		// create a Notifcation object and add it to the ArrayList<Notification>
		Notification notification = new Notification(NotificationType.CREDIT,
				String.format("Deposit submitted for account %s", account.getName()));
		addNotification(notification);
	}

	// overloaded method does not add a Notification
	public void credit(Account account, BigDecimal creditAmount, boolean isTransfer) throws SQLException {
		// credit the account
		account.credit(creditAmount);
		// create a Transaction object and add it to the ArrayList<Transaction>
		Transaction transaction = new Transaction(account.getAccountNumber(), "Deposit Transaction", "credit",
				creditAmount);
		transaction.addTransaction();

	}

	public void debit(Account account, BigDecimal debitAmount) throws SQLException {
		account.debit(debitAmount);
		Transaction transaction = new Transaction(account.getAccountNumber(), "Withdrawal Transaction", "debit",
				debitAmount);
		transaction.addTransaction();
		Notification notification = new Notification(NotificationType.DEBIT,
				String.format("Withdrawal submitted for account %s", account.getName()));
		addNotification(notification);
	}

	// overloaded method does not add a Notification
	public void debit(Account account, BigDecimal debitAmount, boolean isTransfer) throws SQLException {
		// debit the account
		account.debit(debitAmount);
		// add a new transaction
		Transaction transaction = new Transaction(account.getAccountNumber(), "Withdrawal Transaction", "debit",
				debitAmount);
		transaction.addTransaction();

	}

	/**
	 * Debit the amount from the first Account passed to this method, credit the
	 * second.
	 * 
	 * @param fromAccount
	 * @param toAccount
	 * @param amount
	 * @throws SQLException
	 */
	public void transfer(Account fromAccount, Account toAccount, BigDecimal amount) throws SQLException {
		debit(fromAccount, amount, true);
		credit(toAccount, amount, true);

		// create a transfer notification
		Notification notification = new Notification(NotificationType.TRANSFER, String.format(
				"Transfer submitted to account %s from account %s", toAccount.getName(), fromAccount.getName()));
		addNotification(notification);
	}

	/**
	 * This method performs an external transfer of funds from one user to another
	 * @param fromAccount
	 * @param recipient
	 * @param amount
	 * @throws SQLException
	 */
	public void performExternalTransfer(Account fromAccount, User recipient, BigDecimal amount) throws SQLException {
		try {
			debit(fromAccount, amount, false); // debit account; do not create debit notification

			databaseConnection.performExternalTransfer(recipient, amount); // send the money

			Notification notification = new Notification(NotificationType.TRANSFER,
					String.format("You sent money to " + recipient.getUsername() + "!"));
			addNotification(notification);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}

	}

	/**
	 * Update the UserSession to ensure consistency with database
	 */
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
		return String.format("UserSession for user %s:%nnumber of accounts: %d%nnumber of notifications: %d%n%n%s",
				user.getUsername(), accounts.size(), notifications.size(), accountString);
	}
}
