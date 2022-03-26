import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
/**
 * UserSession class represents all data required to accommodate a user session
 * 
 * @author owner
 *
 */

public class UserSession {
	private User user; 
	private ArrayList<Account> accounts;
	
	private DatabaseConnection databaseConnection;
	
	public UserSession(User user) {
		this.user = user;
		
	}
	
	// holds all objects associated with a user's account and interacts with these
	// composed classes
	
	
}
