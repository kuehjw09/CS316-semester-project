import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class Transaction {

	private Timestamp timeStamp;
	private String type;
	private double amount;

	public Transaction(Timestamp timeStamp, String type, double amount) {
		super();
		this.timeStamp = timeStamp;
		this.type = type;
		this.amount = amount;
	}
	
	// Overloaded constructor that accepts a ResultSet
	public Transaction(ResultSet resultSet) throws SQLException {
		this.timeStamp = resultSet.getTimestamp(5);
		this.type = resultSet.getString(4);
		this.amount= resultSet.getDouble(3);
		
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
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
		return String.format("%s\t%s\t$%.2f%n", getTimeStamp(), getType(), getAmount());

	}
}
