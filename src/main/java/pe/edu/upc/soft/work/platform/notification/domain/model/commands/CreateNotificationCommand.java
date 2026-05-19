package pe.edu.upc.soft.work.platform.notification.domain.model.commands;

import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;

import java.util.Objects;


/**
 * Command to create a new Notification
 * @param seen the seen status of the notification
 * @param notificationType the notification type
 * @param userAccountId the user account id
 */
public record CreateNotificationCommand(boolean seen, NotificationType notificationType, Long userAccountId) {

    /**
     * Constructor with validation
     * @param seen the seen status of the notification
     * @param notificationType the notification type
     * @param userAccountId the user account id
     */
    public CreateNotificationCommand{
        Objects.requireNonNull(notificationType, "Notification type cannot be null");
        Objects.requireNonNull(userAccountId, "User account ID cannot be null");
    }

}
