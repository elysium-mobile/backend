package pe.edu.upc.soft.work.platform.notification.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;

/**
 * NotificationCreatedEvent
 * Event triggered when a new Notification is successfully created.
 */
@Getter
public class NotificationCreatedEvent extends ApplicationEvent {
    /** The ID of the created notification. */
    private final Long notificationId;
    /** The ID of the user account associated with the notification. */
    private final Long userAccountId;
    /** The type of the notification. */
    private final NotificationType notificationType;

    /**
     * NotificationCreatedEvent Constructor
     * @param source           the source of the event
     * @param notificationId   the ID of the created notification
     * @param userAccountId    the ID of the target user account
     * @param notificationType the type of the notification
     */
    public NotificationCreatedEvent(Object source, Long notificationId, Long userAccountId, NotificationType notificationType) {
        super(source);
        this.notificationId = notificationId;
        this.userAccountId = userAccountId;
        this.notificationType = notificationType;
    }
}
