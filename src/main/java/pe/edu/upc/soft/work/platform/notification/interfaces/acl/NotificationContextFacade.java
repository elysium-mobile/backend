package pe.edu.upc.soft.work.platform.notification.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationCommandService;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationQueryService;

import java.util.Objects;

/**
 * Facade for the Notification Bounded Context.
 * Exposes notification creation and verification operations for other Bounded Contexts.
 */
@Service
public class NotificationContextFacade {

    /**
     * Command service for notifications.
     */
    private final NotificationCommandService notificationCommandService;

    /**
     * Query service for notifications.
     */
    private final NotificationQueryService notificationQueryService;

    /**
     * Constructor for NotificationContextFacade.
     *
     * @param notificationCommandService the notification command service
     * @param notificationQueryService   the notification query service
     */
    public NotificationContextFacade(NotificationCommandService notificationCommandService,
                                     NotificationQueryService notificationQueryService) {
        this.notificationCommandService = notificationCommandService;
        this.notificationQueryService = notificationQueryService;
    }

    /**
     * Check if a notification exists by its ID.
     *
     * @param notificationId the ID of the notification
     * @return true if the notification exists, false otherwise
     */
    public boolean existsNotificationById(Long notificationId) {
        var query = new GetNotificationByIdQuery(notificationId);
        return this.notificationQueryService.handle(query).isPresent();
    }

    /**
     * Create a new notification for a user.
     *
     * @param notificationType the type of notification
     * @param userAccountId    the ID of the target user account
     * @return the ID of the created notification, or 0L if creation failed
     */
    public Long createNotification(NotificationType notificationType, Long userAccountId) {
        var command = new CreateNotificationCommand(false,notificationType, userAccountId);
        var notificationId = this.notificationCommandService.handle(command);
        if (Objects.isNull(notificationId)) {
            return 0L;
        }
        return notificationId;
    }
}
