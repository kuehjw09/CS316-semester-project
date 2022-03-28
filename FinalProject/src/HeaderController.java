import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HeaderController {

    @FXML
    void notificationsButtonPressed(ActionEvent event) {
    	System.out.println("Notifications button pressed");
    }

    @FXML
    void userButtonPressed(ActionEvent event) {
    	System.out.println("User button pressed");

    }

}

