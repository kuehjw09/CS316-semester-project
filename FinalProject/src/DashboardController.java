import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class DashboardController {
	private UserSession currentUserSession;
	private int selectedAccountNumber;

	@FXML
	private ListView<Account> accountsListView;

	@FXML
	private final ObservableList<Account> accounts = FXCollections.observableArrayList();

	@FXML
	void newAccountButtonPressed(ActionEvent event) {
		//  route a user to account creation form
	}

	public void setSelectedAccountNumber(int accountNumber) {
		this.selectedAccountNumber = accountNumber;
		System.out.printf("selectedAccountNumber = %d%n", accountNumber);
	}

	public int getSelectedAccountNumber() {
		return selectedAccountNumber;
	}

	public void setCurrentUserSession(UserSession currentUserSession) {
		this.currentUserSession = currentUserSession;
	}

	public UserSession getCurrentUserSession() {
		return this.currentUserSession;
	}

	/**
	 * Dashboard is initialized with a UserSession object obtained when a validated user signs in
	 * 
	 * @param currentUserSession
	 */
	public void initializeData(UserSession currentUserSession) {
		this.currentUserSession = currentUserSession;
		
		// set static variable in header and footer in order for them have have the current UserSession object
		FooterController.setCurrentUserSession(currentUserSession);
		HeaderController.setCurrentUserSession(currentUserSession);

		for (Account account : getCurrentUserSession().getAccounts()) {
			accounts.add(account);
		}

		accountsListView.setItems(accounts); // set ObservableList items

		// when selection changes
		accountsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
			@Override
			public void changed(ObservableValue<? extends Account> ov, Account oldAccount, Account newAccount) {
				setSelectedAccountNumber(newAccount.getAccountNumber());
			}
		});

		// custom list cells
		accountsListView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
			@Override
			public ListCell<Account> call(ListView<Account> listView) {
				return new AccountListCell();
			}
		});
	}

}
