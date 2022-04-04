import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import javax.management.Notification;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * AccountViewController has a UserSession and shows detailed account information and options.
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
	}

	public Account getSelectedAccount() {
		return selectedAccount;
	}

	@FXML
	private Label welcomeLabel;
	
	@FXML
	private Label activityLabel;

	@FXML
	private AnchorPane anchorPane;
	
	@FXML private AnchorPane sidebarPane;

	@FXML
	private ListView<Account> accountsListView;

	@FXML
	private final ObservableList<Account> accounts = FXCollections.observableArrayList();

	@FXML
	void newAccountButtonPressed(ActionEvent event) {
		// route a user to account creation form
	}
	
    @FXML
    private ListView<Notification> notificationsListView;
    
    @FXML 
    private final ObservableList<Notification> notifications = FXCollections.observableArrayList();

    @FXML
    private AnchorPane notificationsPane;
    
    @FXML
    private Button showHideButton;

    @FXML
    void clearNotificationsButtonPressed(ActionEvent event) {
    	// clear notifications
    	currentUserSession.clearNotifications();
    	notifications.clear();
		notificationsPane.setVisible(false);
		showHideButton.setText("Show");
    	activityLabel.setText(String.format("Session Activity (%d)", notifications.size()));
    }

    @FXML
    void notificationsPaneButtonPressed(ActionEvent event) {
    	if (notificationsPane.isVisible()) {
    		notificationsPane.setVisible(false);
    		showHideButton.setText("Show");
    		
    	} else {
         	notificationsPane.setVisible(true);
    		showHideButton.setText("Hide");
    	}
    }

	public String getWelcomeMessageText() {

		Calendar calendar = Calendar.getInstance();
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		if (hours >= 0 && hours < 12) {
			return ("Good Morning");
		} else if (hours >= 12 && hours < 18) {
			return ("Good Afternoon");
		} else {
			return ("Good Evening");
		}

	}

	void showAccountDetailsView() throws IOException {
		welcomeLabel.setText(null);
		// show the AccountDetailsView
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountDetailsView.fxml"));
		AnchorPane accountPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(accountPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void initialize() {
		currentUserSession.updateUserSession(); // make sure to update the ArrayList to match database
		
		welcomeLabel
				.setText(String.format("%s, %s", getWelcomeMessageText(), currentUserSession.getUser().getFirstName()));
		
    	for (Notification notification : currentUserSession.getNotifications()) {
    		notifications.add(notification);
    	}
    	
    	activityLabel.setText(String.format("Session Activity (%d)", notifications.size()));
		notificationsPane.setVisible(false);
		showHideButton.setText("Show");
		
    	notificationsListView.setItems(notifications);
    	
    	
    	notificationsListView.setCellFactory(new Callback<ListView<Notification>, ListCell<Notification>>() {
    		@Override
    		public ListCell<Notification> call(ListView<Notification> listView) {
    			return new NotificationsListCell();
    		}
    	});
		
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
				AccountDetailsViewController.setCurrentAccount(newAccount); // set static variable to the selected
																			// account

				// call a method that changes the anchorPane node
				try {
					showAccountDetailsView();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// custom list cells
		accountsListView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
			@Override
			public ListCell<Account> call(ListView<Account> listView) {
				return new AccountListCell(); // custom list cells
			}
		});

		//
	}
}
