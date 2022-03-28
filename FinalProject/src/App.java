import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class App extends Application {
	private Stage stage;
	private Scene scene;
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			DatabaseConnection connection = new DatabaseConnection(); // create a DatabaseConnection object
			boolean authenticated = connection.authenticateUser("kuehjw09", "password");
			if (authenticated == true) {
				System.out.println("User authenticated and verified\n");
				UserSession currentUserSession = connection.getUserSession();
				System.out.printf("%s", currentUserSession);
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("Dashboard.fxml"));
				Parent root = loader.load();
				
				scene = new Scene(root);
				
				// access the controller and call a method
				DashboardController controller = loader.getController();
				controller.initializeData(currentUserSession); // passing current UserSession

				
				primaryStage.setScene(scene);
				primaryStage.show();
			} else {
				System.out.println("Could not verify user");
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}