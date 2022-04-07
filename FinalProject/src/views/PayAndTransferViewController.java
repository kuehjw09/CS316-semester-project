package views;
import classes.UserSession;
import javafx.fxml.FXML;

/**
 * PayAndTransferViewController is initialized with a UserSession object and displays options for pay and transfer features.
 * @author owner
 *
 */

public class PayAndTransferViewController {
	private UserSession currentUserSession;
	
	
	public UserSession getCurrentUserSession() {
		return currentUserSession;
	}
	
	public void setCurrentUserSession(UserSession userSession) {
		this.currentUserSession = userSession;
	}
	
	@FXML
	void sendMoneyButtonPressed() {
		System.out.println("send money button pressed");
		// take user to send money view
	}
	
	@FXML
	void transferFundsButtonPressed() {
		System.out.println("transfer button pressed");
		// take user to transfer view
	}

 	public void initializeData(UserSession userSession) {
		setCurrentUserSession(userSession);
		
		System.out.printf("Current user: $s", userSession.getUser().toString());

	}
}
