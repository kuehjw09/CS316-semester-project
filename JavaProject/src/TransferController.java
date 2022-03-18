import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TransferController {
	private int currentAccountNumber;
	private AccountDatabase accountDatabase;
	// fields to be used in scene change methods
	private Stage stage;
	private Scene scene;
	
	/**
	 * This method attaches a new scene graph to the stage to display the Dashboard when called
	 * @param event
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void switchToDashboard(ActionEvent event) throws IOException, SQLException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Dashboard.fxml"));
		Parent root = loader.load();
		
		scene = new Scene(root);
		
		// access the controller and call a method
		DashboardController controller = loader.getController();
		controller.initializeData(currentAccountNumber, accountDatabase); // passing current account number and database

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public void initializeData(int accountNumber, AccountDatabase accountDatabase) throws SQLException {
		this.currentAccountNumber = accountNumber;
		this.accountDatabase = accountDatabase;
		
		String formatted = String.format("%05d", currentAccountNumber);
		System.out.printf("Deposit form loaded with accountNo. %s%n", formatted);
	}
}
