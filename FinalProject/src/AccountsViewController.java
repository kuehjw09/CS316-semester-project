import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * AccountViewController is initialized with an account and shows detailed account information and options.
 * 
 * @author
 *
 */
public class AccountsViewController {
	private static UserSession currentUserSession; // static variable to set the current user session
	private Account selectedAccount; // keep track of selected account
	
	
	public static void setCurrentUserSession(UserSession userSession) {
		currentUserSession = userSession;
	}
	
	public void setSelectedAccount(Account account) {
		this.selectedAccount = account;
		System.out.printf("selectedAccount = %n%s%n", getSelectedAccount());

	}

	public Account getSelectedAccount() {
		return selectedAccount;
	}
	

	@FXML
	private ListView<Account> accountsListView;

	@FXML
	private final ObservableList<Account> accounts = FXCollections.observableArrayList();
	
	@FXML
	void newAccountButtonPressed(ActionEvent event) {
		//  route a user to account creation form
	}
	
	
	public void initialize() {
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
			}
		});

		// custom list cells
		accountsListView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
			@Override
			public ListCell<Account> call(ListView<Account> listView) {
				return new AccountListCell();
			}
		});
		
		// 
	}
}
