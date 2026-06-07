package pe.edu.upc.soft.work.platform.notification.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.notification.domain.model.events.NotificationCreatedEvent;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationQueryService;

/**
 * Event handler responsible for reacting to a successful NotificationCreatedEvent.
 */
@Service
public class NotificationCreatedEventHandler {

    private final NotificationQueryService notificationQueryService;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationCreatedEventHandler.class);

    /**
     * Constructor for NotificationCreatedEventHandler.
     * @param notificationQueryService service to query the Notification aggregate
     */
    public NotificationCreatedEventHandler(NotificationQueryService notificationQueryService) {
        this.notificationQueryService = notificationQueryService;
    }

    /**
     * Handles the NotificationCreatedEvent after a new notification has been successfully created.
     * @param event the NotificationCreatedEvent containing notification details
     */
    @EventListener
    public void on(NotificationCreatedEvent event) {
        var getNotificationByIdQuery = new GetNotificationByIdQuery(event.getNotificationId());
        var notification = notificationQueryService.handle(getNotificationByIdQuery);

        if (notification.isPresent()) {
            LOGGER.info("Notification successfully created with ID: {} for UserAccount ID: {} of type: {}",
                    event.getNotificationId(), event.getUserAccountId(), event.getNotificationType());
        } else {
            LOGGER.warn("Error: Notification with ID {} could not be found after creation.", event.getNotificationId());
        }
    }
}
