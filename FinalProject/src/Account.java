/**
 * Account class represents a bank account
 * @author owner
 *
 */
import java.math.BigDecimal;
import java.util.ArrayList;


public class Account {
	private String name;
	private int accountNumber;
//	private enum Type {CHECKING, SAVINGS};
	private BigDecimal availableBalance;
	private BigDecimal totalBalance;
	private ArrayList<Transaction> transactions;
	
	// constructor
	public Account(String name, int accountNumber, BigDecimal availableBalance, BigDecimal totalBalance) {

		this.name = name;
		this.accountNumber = accountNumber;
		this.availableBalance = availableBalance;
		this.totalBalance = totalBalance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public int getTransactionsLength() {
		return transactions.size();
	}
	
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	
	@Override
	public String toString() {
		return String.format("Account Name: %s%nAccount Number: %s%n"
				+ "Available Balance: %.2f%nTotalBalance: %s%nNumber of Transactions: %d%n%n",
				getName(), getAccountNumber(), getAvailableBalance(), getTotalBalance(),
				getTransactionsLength());
	}
	
}
