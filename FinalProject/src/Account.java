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
	
	
	
}
