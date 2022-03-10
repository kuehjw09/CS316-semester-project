import java.util.Date;

public class Transaction {

	private Date date; 
	private String description;
	private String type;
	private double amount;

	// constructor
	public Transaction(Date date, String description,
			String type, double amount) {
		if (type == "credit") {
			this.type = "credit";	
		} else if (type == "debit") {
			this.type = "debit";
		} else {
			throw new IllegalArgumentException("transaction type is invalid.");
		}
		
		this.date = date;
		this.description = description;
		this.amount = amount;
	}
	
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	@Override
	public String toString() {
		return String.format("%s\t%s\t%s\t$%.2f%n", getDate(), getDescription(), getType(), getAmount());
				
	}
}
