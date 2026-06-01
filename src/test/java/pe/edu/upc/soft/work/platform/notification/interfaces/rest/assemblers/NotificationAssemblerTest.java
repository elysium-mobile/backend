package pe.edu.upc.soft.work.platform.notification.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.CreateNotificationRequest;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.NotificationResponse;
import pe.edu.upc.soft.work.platform.notification.test.fixtures.NotificationCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateNotificationRequest) -> maps fields and resolves NotificationType enum (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateNotificationRequest(
                true,
                NotificationCommandFixtures.VALID_NOTIFICATION_TYPE.name(),
                NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);

        // Act
        CreateNotificationCommand command = NotificationAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.seen()).isTrue();
        assertThat(command.notificationType()).isEqualTo(NotificationCommandFixtures.VALID_NOTIFICATION_TYPE);
        assertThat(command.userAccountId()).isEqualTo(NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);
    }

    /**
     * Reflects the CURRENT behavior of the assembler: the update overload
     * accepts a {@link CreateNotificationRequest} (not an UpdateNotificationRequest)
     * as its second parameter. See risk report.
     */
    @Test
    @DisplayName("toCommandFromRequest(Long, CreateNotificationRequest) -> maps id and fields to UpdateNotificationCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new CreateNotificationRequest(
                true,
                NotificationCommandFixtures.VALID_NOTIFICATION_TYPE.name(),
                NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);

        // Act
        UpdateNotificationCommand command = NotificationAssembler.toCommandFromRequest(12L, request);

        // Assert
        assertThat(command.notificationId()).isEqualTo(12L);
        assertThat(command.seen()).isTrue();
        assertThat(command.notificationType()).isEqualTo(NotificationCommandFixtures.VALID_NOTIFICATION_TYPE);
        assertThat(command.userAccountId()).isEqualTo(NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);
    }

    /**
     * Notification's constructor hardcodes {@code seen = false} regardless
     * of the command's seen value. The response therefore reflects the
     * actual entity state (seen == false on freshly created entities).
     * See risk report.
     */
    @Test
    @DisplayName("toResponseFromEntity(Notification) -> maps id, seen, notificationType.name() and userAccountId (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Notification(NotificationCommandFixtures.validCreateNotificationCommand());
        ReflectionTestUtils.setId(entity, 12L);

        // Act
        NotificationResponse response = NotificationAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.notificationId()).isEqualTo(12L);
        // Notification(CreateNotificationCommand) hardcodes seen=false
        assertThat(response.seen()).isFalse();
        assertThat(response.notificationType()).isEqualTo(NotificationCommandFixtures.VALID_NOTIFICATION_TYPE.name());
        assertThat(response.userAccountId()).isEqualTo(NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);
    }
}
