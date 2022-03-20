/**
 * Class Transaction represents a credit or debit transaction receipt for all account transactions. 
 * 
 * @author jkuehl
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

	private Timestamp timeStamp;
	private String type;
	private double amount;
	private int accountNumber;

	public Transaction(int accountNumber, double amount, String type) {
		super();
		this.accountNumber = accountNumber;
		this.type = type;
		this.amount = amount;
	}
	
	// Overloaded constructor that accepts a ResultSet
	public Transaction(ResultSet resultSet) throws SQLException {
		this.timeStamp = resultSet.getTimestamp(5);
		this.type = resultSet.getString(4);
		this.amount= resultSet.getDouble(3);
		
	}

	public String getTimeStamp() {
		String formatted = new SimpleDateFormat("MM/dd/yyyy").format(timeStamp);

		return formatted;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
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
	

	/**
	 * Each transaction object has a method addTransaction() that will INSERT a row into the
	 * Transactions heap table of the project database with the information provided during this 
	 * Transaction object's instantiation.
	 * 
	 * @throws SQLException
	 */
	public void addTransaction() throws SQLException {
		Connection connection = AccountDatabase.getConnection(); // call AccountDatabase static method getConnection()
		
		String createString = "INSERT INTO Transactions "
					+ "(accountID, amount, transaction_type) "
					+ "VALUES (?, ?, ?);";
		try (PreparedStatement createStatement = connection.prepareStatement(createString)) {
			createStatement.setInt(1, getAccountNumber());
			createStatement.setDouble(2, getAmount());
			createStatement.setString(3, getType());
			
			createStatement.executeUpdate();
			System.out.printf("Transaction receipt created.%n%n");
			createStatement.close();
			
		} catch (SQLException e) {
			System.out.printf("INSERT Query failed.%n");
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	// return a String representation of a Transaction object
	@Override
	public String toString() {
		return String.format("%s\t%s\t$%.2f%n", getTimeStamp(), getType(), getAmount());
	}
}
