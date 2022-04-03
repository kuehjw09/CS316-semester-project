
public enum NotificationType {
	// declare constants of enum type
	CREATE("Create Notification", "rgba(0, 132, 249, 0.54)"),
	UPDATE("Update Notification", "rgba(91, 152, 222, 0.5)"),
	CREDIT("Deposit Notification", "rgba(94, 221, 90, 0.5)"), 
	DEBIT("Withdrawal Notification", "rgba(249, 0, 0, 0.5)"),
	DELETE("Delete Notification", "rgba(245, 88, 39, 0.3)");
	
	// instance fields
	private final String message;
	private final String color;
	
	NotificationType(String message, String color) {
		this.message = message;
		this.color = color;
	}
	
	// accessor for field message
	public String getMessage() {
		return message;
	}
	
	// accessor for field color
	public String getColor() {
		return color;
	}
	
}
