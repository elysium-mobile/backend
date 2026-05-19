package pe.edu.upc.soft.work.platform.notification.domain.model.commands;

/**
 * The command to update a notification detail.
 * @param notificationDetailId the identifier to notification detail
 * @param title the title of the notification
 * @param content the content of the notification
 */
public record UpdateNotificationDetailCommand(Long notificationDetailId, String title, String content) {
}
