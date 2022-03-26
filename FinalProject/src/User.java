/**
 * Class User represents an application user
 * 
 * @author owner
 *
 */
public class User {
	private String firstName;
	private String lastName;
	private String username;
	private int user_id;
	
	public User(String firstName, String lastName, String username, int user_id) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.user_id = user_id;
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
		return String.format("First: %s%nLast:%s%nUsername: %s%nuser_id: %05d");
	}

}
