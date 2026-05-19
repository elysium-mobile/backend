package pe.edu.upc.soft.work.platform.notification.domain.model.commands;

/**
 * Command to create a new NotificationDetail
 * @param title the title of the notification
 * @param content the content of the notification
 */
public record CreateNotificationDetailCommand(String title, String content) {
}
