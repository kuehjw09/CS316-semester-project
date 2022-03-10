import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller Class
 * 
 * @author jkuehl
 *
 */
public class DashboardController {
	private int currentAccountNumber;
	private AccountDatabase accountDatabase;
	
	// fields to be used in scene change methods
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Label AvailableBalanceLabel;
	
	@FXML
	private Label TotalBalanceLabel;
	
	@FXML
	private Label PendingFundsLabel;
	
	/**
	 * This method will switch back to the login scene when a user selects the log out button.
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void switchToLogin(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * This method accepts an account number and database to initialize the view
	 * 
	 * @param account
	 */
	public void initializeData(int accountNumber, AccountDatabase database) {
		currentAccountNumber = accountNumber;
		accountDatabase = database;
		
		double availableBalance = accountDatabase.getAvailableBalance(currentAccountNumber);
		double totalBalance = accountDatabase.getTotalBalance(currentAccountNumber);
		double pendingFunds = totalBalance - availableBalance;
		
		AvailableBalanceLabel.setText(String.format("$%.2f", availableBalance));
		TotalBalanceLabel.setText(String.format("$%.2f", totalBalance));
		PendingFundsLabel.setText(String.format("-$%.2f", pendingFunds));
	}
}
