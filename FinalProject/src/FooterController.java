import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FooterController {
	private static UserSession currentUserSession;
	

    public UserSession getCurrentUserSession() {
		return currentUserSession;
	}

	public static void setCurrentUserSession(UserSession currentUserSession) {
		FooterController.currentUserSession = currentUserSession;
	}

	@FXML
    void depositButtonPressed(ActionEvent event) {
    	System.out.println("deposit/withdrawal button pressed");
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Test Alert");
    	alert.setHeaderText("deposit/withdrawal button pressed");
    	alert.setContentText(currentUserSession.toString());
    	
    	alert.showAndWait();
    }

    @FXML
    void homeButtonPressed(ActionEvent event) {
    	System.out.println("home button pressed");

    }

    @FXML
    void menuButtonPressed(ActionEvent event) {
    	System.out.println("menu button pressed");

    }

    @FXML
    void transferButtonPressed(ActionEvent event) {
    	System.out.println("pay and transfer pressed");

    }


}
