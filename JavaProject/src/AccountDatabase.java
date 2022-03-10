// AccountDatabase class represents ATM account information database
import java.util.ArrayList;

public class AccountDatabase {
	private ArrayList<Account> accounts; // array of Accounts

	// no-argument AccountDatabse constructor initializes accounts
	public AccountDatabase() {
		accounts = new ArrayList<Account>(); // just 2 accounts for testing
		accounts.add(new Account(12345, 54321, 1000.0, 1200.0));
		accounts.add(new Account(98765, 56789, 200.0, 200.0));
	}

	// retrieve Account object containing specified account number
	private Account getAccount(int accountNumber) {
		// loop through accounts searching for matching account number
		for (Account currentAccount : accounts) {
			// return current account if match found
			if (currentAccount.getAccountNumber() == accountNumber) {
				return currentAccount;
			}
		}

		return null; // if no matching account was found, return null
	}
	
	// public account method for testing
	public Account getPublicAccount(int accountNumber) {
		// loop through accounts searching for matching account number
		for (Account currentAccount : accounts) {
			// return current account if match found
			if (currentAccount.getAccountNumber() == accountNumber) {
				return currentAccount;
			}
		}

		return null; // if no matching account was found, return null
	}

	public boolean authenticateUser(int userAccountNumber, int userPIN) {
		// attempt to retrieve the account with the account number
		Account userAccount = getAccount(userAccountNumber);

		// if account exists, return result of Account method validatePIN
		if (userAccount != null) {
			return userAccount.validatePIN(userPIN);
		}

		return false;
	}

	// return available balance of Account with specified account number
	public double getAvailableBalance(int userAccountNumber) {
		return getAccount(userAccountNumber).getAvailableBalance();
	}

	// return total balance of Account with specified account number
	public double getTotalBalance(int userAccountNumber) {
		return getAccount(userAccountNumber).getTotalBalance();
	}

	// credit amount to account having userAccountNumber
	public void credit(int userAccountNumber, double amount) {
		getAccount(userAccountNumber).credit(amount);
	}

	// debit amount from account having userAccountNumber
	public void debit(int userAccountNumber, double amount) {
		getAccount(userAccountNumber).debit(amount);
	}

}
