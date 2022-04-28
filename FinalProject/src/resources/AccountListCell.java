package resources;
import java.text.NumberFormat;

import classes.Account;
import javafx.geometry.Insets;
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
	private Label defaultLabel = new Label();
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
		hbox.getChildren().add(defaultLabel);
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(balanceLabel);
		vbox.getChildren().add(label);
		
		nameLabel.setFont(Font.font("Arial", 15 ));
		numberLabel.setFont(Font.font("Arial", 15 ));
		defaultLabel.setFont(Font.font("Arial", 12));
		balanceLabel.setFont(Font.font("Arial", 24));
		
		defaultLabel.setPadding(new Insets(1, 1, 1, 12));
		vbox.setPadding(new Insets(1, 1, 1, 12));
		vbox.setStyle("-fx-background-radius: 12");


		
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
			numberLabel.setText("..." + String.format("%04d", account.getAccountNumber() % 110000));
			if (account.isDefault) {
				defaultLabel.setText("(Default)");
			} else {
				defaultLabel.setText(null);
			}
			balanceLabel.setText(currency.format(account.getAvailableBalance()));
			label.setText("Available balance");
			setGraphic(vbox);
		}
	}
}
