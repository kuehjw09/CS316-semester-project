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
    	alert.setTitle("Info Alert Test");
    	alert.setHeaderText("deposit/withdrawal button pressed");
    	alert.setContentText(currentUserSession.toString());
    	
    	alert.showAndWait();
    }

    @FXML
    void homeButtonPressed(ActionEvent event) {
    	System.out.println("home button pressed");
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Warning Alert Test");
    	alert.setHeaderText("home button pressed");
    	alert.setContentText(currentUserSession.toString());
    	
    	alert.showAndWait();
    }

    @FXML
    void menuButtonPressed(ActionEvent event) {
    	System.out.println("menu button pressed");
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Alert Test");
    	alert.setHeaderText("menu button pressed");
    	alert.setContentText(currentUserSession.toString());
    	
    	alert.showAndWait();
    }

    @FXML
    void transferButtonPressed(ActionEvent event) {
    	System.out.println("pay and transfer pressed");
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("Error Alert Test");
    	alert.setHeaderText("pay and transfer button pressed");
    	alert.setContentText(currentUserSession.toString());
    	
    	alert.showAndWait();

    }


}
