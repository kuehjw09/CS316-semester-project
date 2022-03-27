import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class DashboardController {

    @FXML
    private ListView<Account> accountsListView;
    
    @FXML
    private final ObservableList<Account> accounts = 
    	FXCollections.observableArrayList();

    @FXML
    void newAccountButtonPressed(ActionEvent event) {

    }
    
    public void initializeData(UserSession currentUserSession) {
    	for (Account account : currentUserSession.getAccounts()) {
    		accounts.add(account);
    	}
    	
    	accountsListView.setItems(accounts);
    	
    	accountsListView.setCellFactory(
    			new Callback<ListView<Account>, ListCell<Account>>() {
    				@Override
    				public ListCell<Account> call(ListView<Account> listView) {
    					return new AccountListCell();
    				}
    			}
    		);
    }

}
