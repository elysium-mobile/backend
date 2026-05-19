package pe.edu.upc.soft.work.platform.notification.domain.model.queries;

/**
 * Query to retrieve a notification by ID identifier
 * @param notificationId the notification identifier
 */
public record GetNotificationByIdQuery(Long notificationId) {
}
