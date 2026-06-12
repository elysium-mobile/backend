package pe.edu.upc.soft.work.platform.notification.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.notification.domain.model.events.NotificationSeenEvent;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationQueryService;

/**
 * Event handler responsible for reacting to a NotificationSeenEvent.
 */
@Service
public class NotificationSeenEventHandler {

    private final NotificationQueryService notificationQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationSeenEventHandler.class);

    /**
     * Constructor for NotificationSeenEventHandler.
     * @param notificationQueryService service to query the Notification aggregate
     */
    public NotificationSeenEventHandler(NotificationQueryService notificationQueryService) {
        this.notificationQueryService = notificationQueryService;
    }

    /**
     * Handles the NotificationSeenEvent when a notification is marked as seen.
     * @param event the NotificationSeenEvent containing the notification and user IDs
     */
    @EventListener
    public void on(NotificationSeenEvent event) {
        var getNotificationByIdQuery = new GetNotificationByIdQuery(event.getNotificationId());
        var notification = notificationQueryService.handle(getNotificationByIdQuery);

        if (notification.isPresent()) {
            LOGGER.info("Notification with ID: {} marked as seen by UserAccount ID: {}",
                    event.getNotificationId(), event.getUserAccountId());
        } else {
            LOGGER.warn("Error: Notification with ID {} not found when marking as seen.", event.getNotificationId());
        }
    }
}
