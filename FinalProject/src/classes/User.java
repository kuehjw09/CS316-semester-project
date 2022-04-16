package classes;
import java.sql.Connection;
/**
 * Class User represents an application user
 * 	- Represent relevant user information for an application session
 * @author owner
 *
 */
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.DatabaseConnection;

public class User {
	private String firstName;
	private String lastName;
	private String username;
	private Date birthDate;
	private int user_id;
	private int default_account;
	
	public User(String firstName, String lastName, Date birthDate, String username, int user_id) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.username = username;
		this.user_id = user_id;
	}
	
	public User(ResultSet resultSet) throws SQLException {
		this.user_id = resultSet.getInt(1);
		this.firstName = resultSet.getString(2);
		this.lastName = resultSet.getString(3);
		this.birthDate = resultSet.getDate(5);
		this.username = resultSet.getString(6);
		this.default_account = resultSet.getInt(8);
	}

	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public int getDefault_Account() {
		return default_account;
	}
	
	public void setDefault_Account(int default_account) throws SQLException {
		this.default_account = default_account;
		updateUsersTable();
	}
	
	public void updateUsersTable() throws SQLException {
		Connection connection = DatabaseConnection.getConnection();
		String createString = "UPDATE DB2.Users SET default_account = ? WHERE user_id = ?";
		
		try (PreparedStatement createStatement = connection.prepareStatement(createString) ) {
			createStatement.setInt(1, default_account);
			createStatement.setInt(2, user_id);
			
			createStatement.executeUpdate();
			System.out.printf("Database updated successfully.%n%n");
			createStatement.close();
		} catch (SQLException e) {
			System.out.printf("Update failed.%n");
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
	@Override
	public String toString() {
		return String.format("First: %s%nLast: %s%nBirth Date: %s%nUsername: %s%nuser_id: %05d%ndefault_account: %08d%n", getFirstName(),
				getLastName(), getBirthDate(), getUsername(), getUser_id(), getDefault_Account());
	}

}
