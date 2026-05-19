package pe.edu.upc.soft.work.platform.notification.domain.model.commands;

import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;

import java.util.Objects;

/**
 * Command class for updating a notification's seen status and type for a specific user account.
 * @param notificationId the identifier of the notification to be updated
 * @param seen the new seen status of the notification
 * @param notificationType the notification type
 * @param userAccountId the user account id
 */
public record UpdateNotificationCommand(Long notificationId, boolean seen, NotificationType notificationType, Long userAccountId) {

    /**
     * Constructor with validation
     * @param notificationId the identifier of the notification to be updated
     * @param seen the new seen status of the notification
     * @param notificationType the notification type
     * @param userAccountId the user account id
     */
    public UpdateNotificationCommand{
        Objects.requireNonNull(notificationId, "Notification ID cannot be null");
        Objects.requireNonNull(notificationType, "Notification type cannot be null");
        Objects.requireNonNull(userAccountId, "User account ID cannot be null");
    }
}
