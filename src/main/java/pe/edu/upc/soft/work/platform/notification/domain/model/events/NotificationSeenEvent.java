package pe.edu.upc.soft.work.platform.notification.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * NotificationSeenEvent
 * Event triggered when a Notification is marked as seen by a user.
 */
@Getter
public class NotificationSeenEvent extends ApplicationEvent {
    /** The ID of the notification marked as seen. */
    private final Long notificationId;
    /** The ID of the user account that saw the notification. */
    private final Long userAccountId;

    /**
     * NotificationSeenEvent Constructor
     * @param source         the source of the event
     * @param notificationId the ID of the seen notification
     * @param userAccountId  the ID of the user account
     */
    public NotificationSeenEvent(Object source, Long notificationId, Long userAccountId) {
        super(source);
        this.notificationId = notificationId;
        this.userAccountId = userAccountId;
    }
}
