import java.util.Scanner;

import app.DatabaseConnection;
import classes.UserSession;

public class DriverClass {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Software System Test\n");
		System.out.print("Enter username: ");
		String username = input.nextLine();
		System.out.print("Enter password: ");
		String password = input.nextLine();
				
		System.out.println("Attempting to authenticate user...");
		try {
			DatabaseConnection connection = new DatabaseConnection(); // create a DatabaseConnection object
			boolean authenticated = connection.authenticateUser(username, password);
			if (authenticated == true) {
				System.out.println("User authenticated and verified\n");
				UserSession currentUserSession = connection.getUserSession();
				System.out.printf("%s", currentUserSession);
			} else {
				System.out.println("Could not verify user");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
