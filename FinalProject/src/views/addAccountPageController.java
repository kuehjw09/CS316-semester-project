package views;
import java.io.IOException;

import classes.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class addAccountPageController {
	private static UserSession currentUserSession;
	
	public static void setCurrentUserSession(UserSession userSession) {
		 currentUserSession = userSession;
	}
	
	//observable list to add options to the select account choice box
	ObservableList <String> accountTypeList = FXCollections.observableArrayList("Checking","Savings");
	
	@FXML 
	private AnchorPane anchorPane;

    @FXML
    private TextField accountNumberField;

    @FXML
    private ChoiceBox <String> accountTypeChoicebox;

    @FXML
    private DatePicker birthdateDatePicker;
    
    @FXML
    private TextField pinTextField;

    @FXML
    private Pane selectAccountChoiceBox;
    
    @FXML 
    private void returnButtonPressed() throws IOException {
		// return to AccountView
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountsView.fxml"));
		AnchorPane accountPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(accountPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
    }
    
    //method to initialize 
	public void initialize(){
		//sets value to default savings for choicebox
    	accountTypeChoicebox.setValue("Savings");
    	accountTypeChoicebox.setItems(accountTypeList);
    	
    }   
}

