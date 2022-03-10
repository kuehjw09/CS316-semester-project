import java.util.List;

public class TransactionLog {
	private static List<Transaction> list;
	
	public void add(Transaction transaction) {
		list.add(transaction);
	}
	
	
}
