package pe.edu.upc.soft.work.platform.notification.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.UpdateNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.CreateNotificationDetailRequest;
import pe.edu.upc.soft.work.platform.notification.interfaces.rest.resources.NotificationDetailResponse;
import pe.edu.upc.soft.work.platform.notification.test.fixtures.NotificationCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationDetailAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateNotificationDetailRequest) -> maps title and content to CreateNotificationDetailCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateNotificationDetailRequest(
                NotificationCommandFixtures.VALID_DETAIL_TITLE,
                NotificationCommandFixtures.VALID_DETAIL_CONTENT,
            NotificationCommandFixtures.VALID_NOTIFICATION_ID);

        // Act
        CreateNotificationDetailCommand command = NotificationDetailAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.title()).isEqualTo(NotificationCommandFixtures.VALID_DETAIL_TITLE);
        assertThat(command.content()).isEqualTo(NotificationCommandFixtures.VALID_DETAIL_CONTENT);
        assertThat(command.notificationId()).isEqualTo(NotificationCommandFixtures.VALID_NOTIFICATION_ID);
    }

    /**
     * Reflects the CURRENT behavior of the assembler: the update overload
     * accepts a {@link CreateNotificationDetailRequest} (not an
     * UpdateNotificationDetailRequest) as its second parameter.
     * See risk report.
     */
    @Test
    @DisplayName("toCommandFromRequest(Long, CreateNotificationDetailRequest) -> maps id, title and content (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new CreateNotificationDetailRequest(
                NotificationCommandFixtures.VALID_DETAIL_TITLE,
                NotificationCommandFixtures.VALID_DETAIL_CONTENT,
            NotificationCommandFixtures.VALID_NOTIFICATION_ID);

        // Act
        UpdateNotificationDetailCommand command = NotificationDetailAssembler.toCommandFromRequest(21L, request);

        // Assert
        assertThat(command.notificationDetailId()).isEqualTo(21L);
        assertThat(command.title()).isEqualTo(NotificationCommandFixtures.VALID_DETAIL_TITLE);
        assertThat(command.content()).isEqualTo(NotificationCommandFixtures.VALID_DETAIL_CONTENT);
        assertThat(command.notificationId()).isEqualTo(NotificationCommandFixtures.VALID_NOTIFICATION_ID);
    }

    @Test
    @DisplayName("toResponseFromEntity(NotificationDetail) -> maps every field to NotificationDetailResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new NotificationDetail(NotificationCommandFixtures.validCreateNotificationDetailCommand());
        ReflectionTestUtils.setId(entity, 21L);

        // Act
        NotificationDetailResponse response = NotificationDetailAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.notificationDetailId()).isEqualTo(21L);
        assertThat(response.title()).isEqualTo(NotificationCommandFixtures.VALID_DETAIL_TITLE);
        assertThat(response.content()).isEqualTo(NotificationCommandFixtures.VALID_DETAIL_CONTENT);
        assertThat(response.notificationId()).isEqualTo(NotificationCommandFixtures.VALID_NOTIFICATION_ID);
    }
}
