package views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import classes.Account;
import classes.Notification;
import classes.NotificationType;
import classes.UserSession;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import resources.AccountListCell;

public class ManageAccountsViewController {
	private static UserSession currentUserSession; // static variable to set the current user session
	private Account selectedAccount; // keep track of selected account

	public static void setCurrentUserSession(UserSession userSession) {
		currentUserSession = userSession;
	}

	public void setSelectedAccount(Account account) {
		this.selectedAccount = account;
	}

	public Account getSelectedAccount() {
		return selectedAccount;
	}

	@FXML
	private ListView<Account> accountsListView;

	@FXML
	private ObservableList<Account> accounts = FXCollections.observableArrayList();

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private TextField renameTextField;

	@FXML
	private Label renameLabel;

	@FXML
	private Button confirmButton;

	@FXML
	void confirmButtonPressed(ActionEvent event) throws SQLException, IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Manage Accounts");
		alert.setHeaderText("Rename Account");
		alert.setContentText("This account's name will be changed. Are you sure you wish to continue?");
		
		Optional<ButtonType> option = alert.showAndWait();
		
		if (option.get() == ButtonType.OK) {
			// rename the account using a method of Account class
			selectedAccount.renameAccount(renameTextField.getText());
			
			// Create and add an UPDATE notification to the current UserSession ArrayList<Notification>
			Notification notification = new Notification(NotificationType.UPDATE, 
					String.format("Renamed Account Number %d to %s", selectedAccount.getAccountNumber(), selectedAccount.getName()));
			currentUserSession.addNotification(notification);
			
			// return to the dashboard
			returnButtonPressed(event);
		} else {
			renameTextField.setText(null);
			renameTextField.setDisable(true); // enable the text field
			confirmButton.setDisable(true);
			renameLabel.setVisible(false);
			alert.close();
		}
	}

	@FXML
	private ListView<Account> selectedAccountListView;

	@FXML
	private final ObservableList<Account> selection = FXCollections.observableArrayList();

	@FXML
	void deleteButtonPressed(ActionEvent event) {
		
		// alert the user if attempting to delete a default account.
		if (selectedAccount.isDefault) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Manage Accounts");
			alert.setHeaderText("Delete Account");
			alert.setContentText(
					"Cannot delete an account marked as default. Please select a new default account before attempting to delete this account.");
			
			Optional<ButtonType> option = alert.showAndWait();
			
			if (option.get() == ButtonType.OK) {
				alert.close();
			}
		} else { // inform the user that funds will be transferred to their default account; send a confirmation Alert to proceed. 
			// confirmation altert; verify option selected (either OK or CANCEL)
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Delete Account");
			alert.setContentText(
					"When an account is deleted, all available funds and all pending deposits will be transferred to your default account."
					+ "Are you sure you wish to continue?");
			
			Optional<ButtonType> option = alert.showAndWait();
			
			if (option.get() == null) {
				alert.close();
			} else if (option.get() == ButtonType.CANCEL) {
				alert.close();
			} else if (option.get() == ButtonType.OK) {
					
				// begin chain of method calls to perform account deletion
				try {
					currentUserSession.deleteAccount(selectedAccount);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
								
				// route the user back to the dashboard
				try {
					returnButtonPressed(event);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				alert.close();
			}
		}
	}

	@FXML
	void editNameButtonPressed(ActionEvent event) {
		renameTextField.setDisable(false); // enable the text field
		confirmButton.setDisable(false);
		renameLabel.setVisible(true);
	}

	@FXML
	void makeDefaultButtonPressed(ActionEvent event) throws SQLException, IOException {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Manage Accounts");
		alert.setHeaderText("Set New Default Account");
		alert.setContentText(
				"This account will become your new default account. Default accounts always appear first and all funds sent to you will be deposited into your default account."
				+ "Are you sure you wish to continue?");
		
		Optional<ButtonType> option = alert.showAndWait();
		
		if (option.get() == null) {
			alert.close();
		} else if (option.get() == ButtonType.OK) {
			// set the default account using a UserSession method
			currentUserSession.getUser().setDefault_Account(selectedAccount.getAccountNumber());
			
			// Create and add an UPDATE notification to the current UserSession ArrayList<Notification>
			Notification notification = new Notification(NotificationType.UPDATE, 
					String.format("Account %s set as default", selectedAccount.getName()));
			currentUserSession.addNotification(notification);
			
			// return to dashboard
			returnButtonPressed(event);
		} else if (option.get() == ButtonType.CANCEL) {
			alert.close();
		} else {
			alert.close();
		}
	}

	@FXML
	void returnButtonPressed(ActionEvent event) throws IOException {
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

	public void initialize() {

		renameLabel.setVisible(false);

		for (Account account : currentUserSession.getAccounts()) {
			accounts.add(account);
		}

		// set ObservableList items
		accountsListView.setItems(accounts);

		// when selection changes
		accountsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
			@Override
			public void changed(ObservableValue<? extends Account> ov, Account oldAccount, Account newAccount) {
				setSelectedAccount(newAccount);
				selection.clear();
				selection.add(getSelectedAccount());
				selectedAccountListView.setItems(selection);
			}
		});

		// custom list cells
		accountsListView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
			@Override
			public ListCell<Account> call(ListView<Account> listView) {
				return new AccountListCell(); // custom list cells
			}
		});

		selectedAccountListView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
			@Override
			public ListCell<Account> call(ListView<Account> listView) {
				return new AccountListCell(); // custom list cells
			}
		});
	}

}
