package pe.edu.upc.soft.work.platform.notification.domain.model.queries;


/**
 * Query to retrieve a notification detail by ID identifier
 * @param notificationDetailId the notification detail Identifier
 */
public record GetNotificationDetailByIdQuery(Long notificationDetailId) {
}
