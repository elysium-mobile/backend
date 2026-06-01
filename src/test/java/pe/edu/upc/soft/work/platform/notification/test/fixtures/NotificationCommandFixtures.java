package pe.edu.upc.soft.work.platform.notification.test.fixtures;

import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.valueobjects.NotificationType;

/**
 * Notification-specific command factories. Mirrors the architectural
 * template established by {@code IamCommandFixtures} / {@code DashboardCommandFixtures}
 * / {@code FeedbackCommandFixtures}. Tests MUST NOT instantiate notification
 * commands inline.
 */
public final class NotificationCommandFixtures {

    public static final boolean VALID_SEEN = false;
    public static final NotificationType VALID_NOTIFICATION_TYPE = NotificationType.MESSAGE;
    public static final Long VALID_USER_ACCOUNT_ID = 10L;

    public static final String VALID_DETAIL_TITLE = "Welcome";
    public static final String VALID_DETAIL_CONTENT = "Welcome to the platform.";

    private NotificationCommandFixtures() {
        throw new AssertionError("NotificationCommandFixtures is a utility class and must not be instantiated.");
    }

    // ---------- Notification ----------
    public static CreateNotificationCommand validCreateNotificationCommand() {
        return new CreateNotificationCommand(VALID_SEEN, VALID_NOTIFICATION_TYPE, VALID_USER_ACCOUNT_ID);
    }

    public static UpdateNotificationCommand updateNotificationCommand(Long notificationId) {
        return new UpdateNotificationCommand(notificationId, true, VALID_NOTIFICATION_TYPE, VALID_USER_ACCOUNT_ID);
    }

    // ---------- NotificationDetail ----------
    public static CreateNotificationDetailCommand validCreateNotificationDetailCommand() {
        return new CreateNotificationDetailCommand(VALID_DETAIL_TITLE, VALID_DETAIL_CONTENT);
    }

    public static UpdateNotificationDetailCommand updateNotificationDetailCommand(Long notificationDetailId) {
        return new UpdateNotificationDetailCommand(notificationDetailId, VALID_DETAIL_TITLE, VALID_DETAIL_CONTENT);
    }
}
