package views;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;

import classes.Account;
import classes.Notification;
import classes.UserSession;
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
import resources.AccountListCell;
import resources.NotificationsListCell;

/**
 * AccountViewController has a UserSession and shows detailed account
 * information and options.
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

	@FXML
	private AnchorPane sidebarPane;

	@FXML
	private ListView<Account> accountsListView;

	@FXML
	private final ObservableList<Account> accounts = FXCollections.observableArrayList();

	@FXML
	void newAccountButtonPressed(ActionEvent event) throws IOException {
		// route a user to account creation form
		addAccountPageController.setCurrentUserSession(currentUserSession);

		TransactionViewController.setCurrentUserSession(currentUserSession);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("addAccountPage.fxml"));
		AnchorPane centerPane = (AnchorPane) loader.load();
		try {
			anchorPane.getChildren().clear();
			anchorPane.getChildren().add(centerPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
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
	void notificationsPaneButtonPressed(ActionEvent event) {
		if (notificationsListView.isVisible()) {
			notificationsListView.setVisible(false);
			showHideButton.setText("Show");

		} else {
			notificationsListView.setVisible(true);
			showHideButton.setText("Hide");
		}
	}

	public void initialize() {
		currentUserSession.updateUserSession(); // make sure to update to match database

		welcomeLabel
				.setText(String.format("%s, %s", getWelcomeMessageText(), currentUserSession.getUser().getFirstName()));

		// initialize notifications
		initNotifications();		
		
		// initialize accounts
		initAccounts();
	}
	
	/**
	 * 
	 */
	public void initNotifications() {
		for (Notification notification : currentUserSession.getNotifications()) {
			notifications.add(notification);
		}

		activityLabel.setText(String.format("Session Activity (%d)", notifications.size()));

		if (notifications.size() > 0) {
			notificationsPane.setVisible(true);
			notificationsListView.setVisible(true);
			showHideButton.setText("Hide");
		} else {
			notificationsPane.setVisible(false);
		}

		Collections.reverse(notifications);
		notificationsListView.setItems(notifications);

		notificationsListView.setCellFactory(new Callback<ListView<Notification>, ListCell<Notification>>() {
			@Override
			public ListCell<Notification> call(ListView<Notification> listView) {
				return new NotificationsListCell();
			}
		});
	}
	
	/**
	 * 
	 */
	public void initAccounts() {

		for (Account account : currentUserSession.getAccounts()) {
			if (account.isDefault) {
				accounts.add(0, account); // always show default account first
			} else {
				accounts.add(account);
			}
		}

		// sort the elements past the 0th index position
		Collections.reverse(accounts.subList(1, accounts.size()));

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
	}
	
	/**
	 * 
	 * @return
	 */
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

	/**
	 * 
	 * @throws IOException
	 */
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

}
