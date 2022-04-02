
/**
 * Transaction class represents an account transaction
 * @author owner
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
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
	public Transaction(int account_number, String description, String type, double amount) {
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
	
	/**
	 * Each transaction object has a method addTransaction() that will INSERT a row into the
	 * Transactions heap table of the project database with the information provided during this 
	 * Transaction object's instantiation.
	 * 
	 * @throws SQLException
	 */
	public void addTransaction() throws SQLException {
		Connection connection = DatabaseConnection.getConnection(); // call static method getConnection()
		
		String createString = "INSERT INTO DB2.Transactions "
					+ "(account_number, amount, transaction_type)"
					+ "VALUES (?, ?, ?);";
		try (PreparedStatement createStatement = connection.prepareStatement(createString)) {
			createStatement.setInt(1, getAccount_number());
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

	
	@Override
	public String toString() {
		return String.format("%s\t%s\t%s\t$%.2f%n", getTimestamp(), getDescription(), getType(), getAmount());
	}
}
