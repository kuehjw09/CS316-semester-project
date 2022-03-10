// Account class represents an online ATM account

public class Account {
	private int accountNumber; // account number
	private int pin; // PIN for authentication
	private double availableBalance; // funds available for withdrawal
	private double totalBalance; // funds available + pending deposits

	// constructor
	public Account(int accountNumber, int pin, double availableBalance, double totalBalance) {
		this.accountNumber = accountNumber;
		this.pin = pin;
		this.availableBalance = availableBalance;
		this.totalBalance = totalBalance;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public boolean validatePIN(int userPIN) {
		if (userPIN == pin) {
			return true;
		}
		return false;
	}

	public void credit(double amount) {
		totalBalance += amount; // add amount to totalBalance
	}

	public void debit(double amount) {
		availableBalance -= amount; // subtract amount from availableBalance
		totalBalance -= amount; // subtract amount from totalBalance
	}

	@Override
	public String toString() {
		return String.format("Account #%d:%nAvailable Balance: $%.2f%nTotalBalance: $%.2f%n", getAccountNumber(),
				getAvailableBalance(), getTotalBalance());
	}
}

