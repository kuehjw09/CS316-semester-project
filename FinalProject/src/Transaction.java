/**
 * Transaction class represents an account transaction
 * @author owner
 *
 */

import java.sql.Timestamp;

public class Transaction {
	private Timestamp timestamp;
	private int account_number;
	private String description;
	private String type;
	private double amount;
}
