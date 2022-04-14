package views;

import java.io.IOException;
import java.util.Collections;

import classes.Account;
import classes.UserSession;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
	private final ObservableList<Account> accounts = FXCollections.observableArrayList();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ListView<Account> selectedAccountListView;
	private final ObservableList<Account> selection = FXCollections.observableArrayList();


    @FXML
    void deleteButtonPressed(ActionEvent event) {

    }

    @FXML
    void editNameButtonPressed(ActionEvent event) {

    }

    @FXML
    void makeDefaultButtonPressed(ActionEvent event) {

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
    	for (Account account : currentUserSession.getAccounts()) {
			accounts.add(account);
		}
		
		// reverse the list/ show oldest account first
		Collections.reverse(accounts);
		
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
