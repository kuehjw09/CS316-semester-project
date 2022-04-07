package classes;
/**
 * Class User represents an application user
 * 	- Represent relevant user information for an application session
 * @author owner
 *
 */
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	private String firstName;
	private String lastName;
	private String username;
	private Date birthDate;
	private int user_id;
	
	public User(String firstName, String lastName, Date birthDate, String username, int user_id) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.username = username;
		this.user_id = user_id;
	}
	
	public User(ResultSet resultSet) throws SQLException {
		this.firstName = resultSet.getString(2);
		this.lastName = resultSet.getString(3);
		this.birthDate = resultSet.getDate(5);
		this.username = resultSet.getString(6);
		this.user_id = resultSet.getInt(1);
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
	
	@Override
	public String toString() {
		return String.format("First: %s%nLast: %s%nBirth Date: %s%nUsername: %s%nuser_id: %05d%n", getFirstName(),
				getLastName(), getBirthDate(), getUsername(), getUser_id());
	}

}
