import javafx.fxml.FXML;

/**
 * Displays the TransactionView when a user selects the deposit/withdrawal option in the application footer.
 * 
 * @author owner
 *
 */
public class TransactionViewController {
	private UserSession currentUserSession;
	
	
	public UserSession getCurrentUserSession() {
		return currentUserSession;
	}
	
	public void setCurrentUserSession(UserSession userSession) {
		this.currentUserSession = userSession;
	}
	
	@FXML
	void withdrawalButtonPressed() {
		System.out.println("withdrawal button pressed");
		// take user to withdrawal view
	}
	
	@FXML
	void depositButtonPressed() {
		System.out.println("depositButtonPressed");
		// take user to deposit view
	}

 	public void initializeData(UserSession userSession) {
		setCurrentUserSession(userSession);
		
		System.out.printf("Current user: $s", userSession.getUser().toString());
	}
}
