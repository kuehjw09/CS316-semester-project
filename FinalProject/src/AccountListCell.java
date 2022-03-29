import java.text.NumberFormat;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AccountListCell extends ListCell<Account> {
	private VBox vbox = new VBox(8.0);
	private HBox hbox = new HBox(3.0);
	private Label nameLabel = new Label();
	private Label numberLabel = new Label();
	private Label balanceLabel = new Label();
	private Label label = new Label();
	
	// formatter for currency
	private static final NumberFormat currency = NumberFormat.getCurrencyInstance();
	
	// constructor configures VBox, HBox, nameLabel, numberLabel, balanceLabel, and label
	public AccountListCell() {
		vbox.setAlignment(Pos.CENTER_LEFT);
		hbox.setSpacing(3.0);
		hbox.getChildren().add(nameLabel); // attach to hbox
		hbox.getChildren().add(numberLabel); // attach to hbox
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(balanceLabel);
		vbox.getChildren().add(label);
		
		nameLabel.setFont(Font.font("arial", 15 ));
		numberLabel.setFont(Font.font("arial", 15 ));
		balanceLabel.setFont(Font.font("helvatica", 24));
		vbox.setStyle("-fx-border-width: 2px 4px 4px 2px");

		
		setPrefWidth(USE_PREF_SIZE);
	}
	
	// called to configure each custom ListView cell
	@Override
	protected void updateItem(Account account, boolean empty) {
		super.updateItem(account, empty);
		
		if (empty || account == null) {
			setGraphic(null); // don't display anything
		} else {
			nameLabel.setText(account.getName());
			numberLabel.setText("..." + String.valueOf(account.getAccountNumber() % 110000));
			balanceLabel.setText(currency.format(account.getAvailableBalance()));
			label.setText("Available balance");
			setGraphic(vbox);
		}
	}
}
