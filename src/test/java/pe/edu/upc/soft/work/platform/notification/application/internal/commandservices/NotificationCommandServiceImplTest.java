package pe.edu.upc.soft.work.platform.notification.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import pe.edu.upc.soft.work.platform.notification.application.internal.outboundservices.acl.ExternalIamServiceFromNotification;
import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.events.NotificationCreatedEvent;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationRepository;
import pe.edu.upc.soft.work.platform.notification.test.fixtures.NotificationCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.domain.exceptions.NotFoundArgumentException;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationCommandServiceImplTest {

    private static final Long NOTIFICATION_ID = 12L;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private ExternalIamServiceFromNotification externalIamServiceFromNotification;

    @InjectMocks
    private NotificationCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateNotificationCommand) -> creates Notification and returns generated id when user account exists (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = NotificationCommandFixtures.validCreateNotificationCommand();
        when(externalIamServiceFromNotification.existsUserAccountById(NotificationCommandFixtures.VALID_USER_ACCOUNT_ID))
            .thenReturn(true);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> {
            Notification n = inv.getArgument(0);
            ReflectionTestUtils.setId(n, NOTIFICATION_ID);
            return n;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(NOTIFICATION_ID);
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(eventPublisher, times(1)).publishEvent(any(NotificationCreatedEvent.class));
        verify(externalIamServiceFromNotification, times(1))
            .existsUserAccountById(NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);
        verifyNoMoreInteractions(externalIamServiceFromNotification, notificationRepository, eventPublisher);
    }

    @Test
    @DisplayName("handle(CreateNotificationCommand) -> throws NotFoundArgumentException when user account is missing (AAA)")
    void handleCreateMissingUserAccount() {
        // Arrange
        var command = NotificationCommandFixtures.validCreateNotificationCommand();
        when(externalIamServiceFromNotification.existsUserAccountById(NotificationCommandFixtures.VALID_USER_ACCOUNT_ID))
                .thenReturn(false);

        // Act + Assert
        NotFoundArgumentException ex = assertThrows(NotFoundArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("User Account ID: " + NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(externalIamServiceFromNotification, times(1))
                .existsUserAccountById(NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);
        verifyNoMoreInteractions(externalIamServiceFromNotification);
        verifyNoInteractions(notificationRepository);
    }

    @Test
    @DisplayName("handle(CreateNotificationCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = NotificationCommandFixtures.validCreateNotificationCommand();

        when(externalIamServiceFromNotification.existsUserAccountById(NotificationCommandFixtures.VALID_USER_ACCOUNT_ID))
            .thenReturn(true);
        when(notificationRepository.save(any(Notification.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error saving notification").contains("db");
        verify(externalIamServiceFromNotification, times(1)).existsUserAccountById(NotificationCommandFixtures.VALID_USER_ACCOUNT_ID);
        verify(eventPublisher, never()).publishEvent(any(NotificationCreatedEvent.class));
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verifyNoMoreInteractions(externalIamServiceFromNotification, notificationRepository, eventPublisher);
    }

    @Test
    @DisplayName("handle(UpdateNotificationCommand) -> returns Optional with updated Notification when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Notification(NotificationCommandFixtures.validCreateNotificationCommand());
        ReflectionTestUtils.setId(existing, NOTIFICATION_ID);
        var command = NotificationCommandFixtures.updateNotificationCommand(NOTIFICATION_ID);
        when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.of(existing));
        when(notificationRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Notification> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().isSeen()).isTrue();
        assertThat(result.get().getNotificationType()).isEqualTo(NotificationCommandFixtures.VALID_NOTIFICATION_TYPE);
        verify(notificationRepository, times(1)).findById(NOTIFICATION_ID);
        verify(notificationRepository, times(1)).save(existing);
        verifyNoMoreInteractions(notificationRepository);
        verifyNoInteractions(externalIamServiceFromNotification);
    }

    @Test
    @DisplayName("handle(UpdateNotificationCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Notification(NotificationCommandFixtures.validCreateNotificationCommand());
        ReflectionTestUtils.setId(existing, NOTIFICATION_ID);
        var command = NotificationCommandFixtures.updateNotificationCommand(NOTIFICATION_ID);
        when(notificationRepository.findById(NOTIFICATION_ID)).thenReturn(Optional.of(existing));
        when(notificationRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while updating notification").contains("boom");
        verify(notificationRepository, times(1)).findById(NOTIFICATION_ID);
        verify(notificationRepository, times(1)).save(existing);
        verifyNoMoreInteractions(notificationRepository);
        verifyNoInteractions(externalIamServiceFromNotification);
    }

    @Test
    @DisplayName("handle(DeleteNotificationCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteNotificationCommand(NOTIFICATION_ID);
        when(notificationRepository.existsById(NOTIFICATION_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(notificationRepository, times(1)).existsById(NOTIFICATION_ID);
        verify(notificationRepository, times(1)).deleteById(NOTIFICATION_ID);
        verifyNoMoreInteractions(notificationRepository);
        verifyNoInteractions(externalIamServiceFromNotification);
    }

    @Test
    @DisplayName("handle(DeleteNotificationCommand) -> throws IllegalArgumentException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteNotificationCommand(NOTIFICATION_ID);
        when(notificationRepository.existsById(NOTIFICATION_ID)).thenReturn(false);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Notification with id " + NOTIFICATION_ID + " not found");
        verify(notificationRepository, times(1)).existsById(NOTIFICATION_ID);
        verify(notificationRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(notificationRepository);
        verifyNoInteractions(externalIamServiceFromNotification);
    }

    @Test
    @DisplayName("handle(DeleteNotificationCommand) -> wraps deleteById failure in IllegalArgumentException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteNotificationCommand(NOTIFICATION_ID);
        when(notificationRepository.existsById(NOTIFICATION_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(notificationRepository).deleteById(NOTIFICATION_ID);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while deleting notification").contains("fk");
        verify(notificationRepository, times(1)).existsById(NOTIFICATION_ID);
        verify(notificationRepository, times(1)).deleteById(NOTIFICATION_ID);
        verifyNoMoreInteractions(notificationRepository);
        verifyNoInteractions(externalIamServiceFromNotification);
    }
}
