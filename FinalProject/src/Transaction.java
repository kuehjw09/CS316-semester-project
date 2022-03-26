
/**
 * Transaction class represents an account transaction
 * @author owner
 *
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Transaction {
	private Timestamp timestamp;
	private int account_number;
	private String description;
	private String type;
	private double amount;

	// constructor
	public Transaction(Timestamp timestamp, int account_number, String description, String type, double amount) {
		this.timestamp = timestamp;
		this.account_number = account_number;
		this.description = description;
		this.type = type;
		this.amount = amount;
	}
	
	public Transaction(ResultSet resultSet) throws SQLException {
		this.timestamp = resultSet.getTimestamp(6);
		this.account_number = resultSet.getInt(2);
		this.description = resultSet.getString(5);
		this.type = resultSet.getString(4);
		this.amount = resultSet.getDouble(3);
	}

	public String getTimestamp() {
		String formatted = new SimpleDateFormat("MM/dd/yyyy").format(timestamp);

		return formatted;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getAccount_number() {
		return account_number;
	}

	public void setAccount_number(int account_number) {
		this.account_number = account_number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return String.format("%s\t%s\t%s\t$%.2f%n", getTimestamp(), getDescription(), getType(), getAmount());
	}
}
