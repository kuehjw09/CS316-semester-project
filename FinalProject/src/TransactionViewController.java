/**
 * Displays the TransactionView when a user selects the deposit/withdrawal option in the application footer.
 * 
 * @author jkuehl
 *
 */
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
public class TransactionViewController {
	private static UserSession currentUserSession;
	
	
	public UserSession getCurrentUserSession() {
		return currentUserSession;
	}
	
	public static void setCurrentUserSession(UserSession userSession) {
		currentUserSession = userSession;
	}
	
	@FXML 
	private AnchorPane anchorPane;
	
	@FXML
	void withdrawalButtonPressed() throws IOException {
		System.out.println("withdrawal button pressed");
		// take the user to the WithdrawalView
		WithdrawalController.setCurrentUserSession(currentUserSession);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WithdrawalView.fxml"));
		AnchorPane accountPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(accountPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@FXML
	void depositButtonPressed() throws IOException {
		System.out.println("depositButtonPressed");
		// take user to deposit view

		FXMLLoader loader = new FXMLLoader(getClass().getResource("DepositView.fxml"));
		AnchorPane accountPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(accountPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

 	public void initializeData(UserSession userSession) {
		setCurrentUserSession(userSession);
		
		System.out.printf("Current user: $s", userSession.getUser().toString());
	}
}
