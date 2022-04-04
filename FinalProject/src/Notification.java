
public class Notification {
	private NotificationType notificationType;
	private String description;
	
	// constructor
	public Notification(NotificationType notificationType, String description) {
		this.notificationType = notificationType;
		this.description = description;
	}
	
	public NotificationType getNotificationType() {
		return notificationType;
	}
	
	public String getDescription() {
		return description;
	}
}
