package pe.edu.upc.soft.work.platform.notification.interfaces.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.CreateNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationCommandService;
import pe.edu.upc.soft.work.platform.notification.domain.services.NotificationQueryService;
import pe.edu.upc.soft.work.platform.notification.test.fixtures.NotificationCommandFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationContextFacadeTest {

    @Mock
    private NotificationCommandService notificationCommandService;
    @Mock
    private NotificationQueryService notificationQueryService;

    @InjectMocks
    private NotificationContextFacade facade;

    @Test
    @DisplayName("existsNotificationById(Long) -> returns true when query service returns Optional with value (AAA)")
    void existsNotificationByIdPresent() {
        // Arrange
        var notification = new Notification(NotificationCommandFixtures.validCreateNotificationCommand());
        when(notificationQueryService.handle(any(GetNotificationByIdQuery.class))).thenReturn(Optional.of(notification));

        // Act
        boolean result = facade.existsNotificationById(12L);

        // Assert
        assertThat(result).isTrue();
        verify(notificationQueryService, times(1)).handle(any(GetNotificationByIdQuery.class));
        verifyNoMoreInteractions(notificationQueryService);
        verifyNoInteractions(notificationCommandService);
    }

    @Test
    @DisplayName("existsNotificationById(Long) -> returns false when query service returns Optional.empty (AAA)")
    void existsNotificationByIdAbsent() {
        // Arrange
        when(notificationQueryService.handle(any(GetNotificationByIdQuery.class))).thenReturn(Optional.empty());

        // Act
        boolean result = facade.existsNotificationById(12L);

        // Assert
        assertThat(result).isFalse();
        verify(notificationQueryService, times(1)).handle(any(GetNotificationByIdQuery.class));
        verifyNoMoreInteractions(notificationQueryService);
        verifyNoInteractions(notificationCommandService);
    }

    @Test
    @DisplayName("createNotification(...) -> returns id from command service when not null (AAA)")
    void createNotificationReturnsId() {
        // Arrange
        when(notificationCommandService.handle(any(CreateNotificationCommand.class))).thenReturn(99L);

        // Act
        Long result = facade.createNotification(
                NotificationCommandFixtures.VALID_NOTIFICATION_TYPE,
                NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);

        // Assert
        assertThat(result).isEqualTo(99L);
        verify(notificationCommandService, times(1)).handle(any(CreateNotificationCommand.class));
        verifyNoMoreInteractions(notificationCommandService);
        verifyNoInteractions(notificationQueryService);
    }

    @Test
    @DisplayName("createNotification(...) -> returns 0L when command service returns null (AAA)")
    void createNotificationReturnsZeroOnNull() {
        // Arrange
        when(notificationCommandService.handle(any(CreateNotificationCommand.class))).thenReturn(null);

        // Act
        Long result = facade.createNotification(
                NotificationCommandFixtures.VALID_NOTIFICATION_TYPE,
                NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);

        // Assert
        assertThat(result).isEqualTo(0L);
        verify(notificationCommandService, times(1)).handle(any(CreateNotificationCommand.class));
        verifyNoMoreInteractions(notificationCommandService);
        verifyNoInteractions(notificationQueryService);
    }
}
