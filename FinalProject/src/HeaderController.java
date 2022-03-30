import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HeaderController {
	private static UserSession currentUserSession; // keep track of current user session

	
//	private static final String GREEN = ("#69f0ae");
//	private static final String RED = ("#ff80ab");
//	private static final String BLUE = ("#90caf9");
	
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
    

//    
//    // to display a message
//    @FXML
//    private Label messageLabel;
//    
//    @FXML 
//    private Button closeButton;
//    
//  
//    public void closeBannerPane() {
//    	messageLabel.visibleProperty().set(false);
//    }
//    
//    /**
//     * Display the bannerPane with the message and background color provided to this method
//     * @param messageText
//     * @param messageColor
//     */
//    public void showBannerPane(String messageText, String messageColor) {
//    	String bgColor = BLUE;
//    	if (messageColor == "blue") {
//    		bgColor = BLUE;
//    	}
//    	if (messageColor == "red") {
//    		bgColor = RED;
//    	}
//    	if (messageColor == "green") {
//    		bgColor = GREEN;
//    	}
//    	
//    	messageLabel.visibleProperty().set(true);
//    	messageLabel.setText(messageText);
//    	messageLabel.setStyle("-fx-background-color:" + bgColor );
//    }
    

}

