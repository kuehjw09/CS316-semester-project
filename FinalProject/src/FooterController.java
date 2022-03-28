import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class FooterController {

    @FXML
    void depositButtonPressed(ActionEvent event) {
    	System.out.println("deposit/withdrawal button pressed");
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
