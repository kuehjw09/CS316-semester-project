package app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppLoginLauncher extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../views/Login.fxml"));
			primaryStage.setTitle("JavaFX ATM Interface");
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
