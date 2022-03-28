import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HeaderController {
	private static UserSession currentUserSession;
	
    public static UserSession getCurrentUserSession() {
		return currentUserSession;
	}

	public static void setCurrentUserSession(UserSession currentUserSession) {
		HeaderController.currentUserSession = currentUserSession;
	}

	@FXML
    void notificationsButtonPressed(ActionEvent event) {
    	System.out.println("Notifications button pressed");
    }

    @FXML
    void userButtonPressed(ActionEvent event) {
    	System.out.println("User button pressed");

    }

}

