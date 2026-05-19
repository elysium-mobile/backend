package pe.edu.upc.soft.work.platform.notification.domain.model.commands;

/**
 * Command to delete a notificatio detail
 * @param notificationDetailId the identifier to notification detail
 */
public record DeleteNotificationDetailCommand(Long notificationDetailId) {
}
