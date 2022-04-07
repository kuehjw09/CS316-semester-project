package resources;
import classes.Notification;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class NotificationsListCell extends ListCell<Notification> {
	private VBox vbox = new VBox(8.0);
	private Label typeLabel = new Label();
	private Label descriptionLabel = new Label();
	
	public NotificationsListCell() {
		vbox.setAlignment(Pos.CENTER_LEFT);
		vbox.setSpacing(3.0);
		vbox.setPadding(new Insets(3, 3, 3, 3));
		vbox.getChildren().add(typeLabel);
		vbox.getChildren().add(descriptionLabel);
		typeLabel.setFont(Font.font("helvatica", 15));
		descriptionLabel.setFont(Font.font("arial", 12));
		


		
		setPrefWidth(USE_PREF_SIZE);
	}
	
	@Override
	protected void updateItem(Notification notification, boolean empty) {
		super.updateItem(notification, empty);
		
		if (empty || notification == null) {
			setGraphic(null);
		} else {
			typeLabel.setText(notification.getNotificationType().getMessage());
			descriptionLabel.setText(notification.getDescription()); 
			typeLabel.setStyle("-fx-text-fill: " + notification.getNotificationType().getColor());
			
			setGraphic(vbox);
		}
	}
}
