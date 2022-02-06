// Fig. 34.15: Keypad.java
// Represents the keypad of the ATM
import java.util.Scanner;

public class Keypad {
	private Scanner input; // reads data from the command line
	
	// no-argument constructor initializes the Scanner
	public Keypad() {
		input = new Scanner(System.in);
	}
	
	// return an integer value entered by the user
	public int getInput() {
		return input.nextInt(); // we assume that user enters an integer
	}
}
