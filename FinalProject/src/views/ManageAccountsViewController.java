package views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import classes.Account;
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
		selectedAccount.renameAccount(renameTextField.getText());
		returnButtonPressed(event);
	}

	@FXML
	private ListView<Account> selectedAccountListView;

	@FXML
	private final ObservableList<Account> selection = FXCollections.observableArrayList();

	@FXML
	void deleteButtonPressed(ActionEvent event) {

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
				"This account will become your new default account.\n Default accounts always appear first and all funds sent to you will be deposited into your default account.");
		
		Optional<ButtonType> option = alert.showAndWait();
		
		if (option.get() == null) {
			alert.close();
		} else if (option.get() == ButtonType.OK) {
			currentUserSession.getUser().setDefault_Account(selectedAccount.getAccountNumber());
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
