package pe.edu.upc.soft.work.platform.notification.domain.model.commands;

/**
 * Command to delete a notification
 * @param notificationId the identifier of the notification to be deleted
 */
public record DeleteNotificationCommand(Long notificationId) {
}
