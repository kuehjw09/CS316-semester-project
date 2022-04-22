package classes;

public enum NotificationType {
	// declare constants of enum type
	CREATE("Create Notification", "#3d5afe"),
	UPDATE("Update Notification", "#33eaff"),
	CREDIT("Deposit Notification", "#00e676"),
	TRANSFER("Transfer Notification", "#00b0ff"),
	DEBIT("Withdrawal Notification", "#ff5252"),
	DELETE("Delete Notification", "#fb1fa2");
	
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
