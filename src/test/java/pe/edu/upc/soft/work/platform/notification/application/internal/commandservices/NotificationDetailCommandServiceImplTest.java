package pe.edu.upc.soft.work.platform.notification.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.notification.domain.model.commands.DeleteNotificationDetailCommand;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationDetailRepository;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationRepository;
import pe.edu.upc.soft.work.platform.notification.test.fixtures.NotificationCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationDetailCommandServiceImplTest {

    private static final Long DETAIL_ID = 21L;

    @Mock
    private NotificationDetailRepository notificationDetailRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationDetailCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateNotificationDetailCommand) -> creates NotificationDetail and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = NotificationCommandFixtures.validCreateNotificationDetailCommand();
        when(notificationRepository.existsById(command.notificationId())).thenReturn(true);
        when(notificationDetailRepository.save(any(NotificationDetail.class))).thenAnswer(inv -> {
            NotificationDetail nd = inv.getArgument(0);
            ReflectionTestUtils.setId(nd, DETAIL_ID);
            return nd;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(DETAIL_ID);
        verify(notificationRepository).existsById(command.notificationId());
        verify(notificationDetailRepository, times(1)).save(any(NotificationDetail.class));
        verifyNoMoreInteractions(notificationDetailRepository, notificationRepository);
    }

    @Test
    @DisplayName("handle(CreateNotificationDetailCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = NotificationCommandFixtures.validCreateNotificationDetailCommand();

        when(notificationRepository.existsById(command.notificationId())).thenReturn(true);
        when(notificationDetailRepository.save(any(NotificationDetail.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error saving notification detail").contains("db");
        verify(notificationRepository, times(1)).existsById(command.notificationId());
        verify(notificationDetailRepository, times(1)).save(any(NotificationDetail.class));
        verifyNoMoreInteractions(notificationDetailRepository, notificationRepository);
    }

    @Test
    @DisplayName("handle(UpdateNotificationDetailCommand) -> returns Optional with updated NotificationDetail when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new NotificationDetail(NotificationCommandFixtures.validCreateNotificationDetailCommand());
        ReflectionTestUtils.setId(existing, DETAIL_ID);
        var command = NotificationCommandFixtures.updateNotificationDetailCommand(DETAIL_ID);
        when(notificationDetailRepository.findById(DETAIL_ID)).thenReturn(Optional.of(existing));
        when(notificationDetailRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<NotificationDetail> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(NotificationCommandFixtures.VALID_DETAIL_TITLE);
        assertThat(result.get().getContent()).isEqualTo(NotificationCommandFixtures.VALID_DETAIL_CONTENT);
        verify(notificationDetailRepository, times(1)).findById(DETAIL_ID);
        verify(notificationDetailRepository, times(1)).save(existing);
        verifyNoMoreInteractions(notificationDetailRepository);
    }

    @Test
    @DisplayName("handle(UpdateNotificationDetailCommand) -> wraps save failure in IllegalArgumentException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new NotificationDetail(NotificationCommandFixtures.validCreateNotificationDetailCommand());
        ReflectionTestUtils.setId(existing, DETAIL_ID);
        var command = NotificationCommandFixtures.updateNotificationDetailCommand(DETAIL_ID);
        when(notificationDetailRepository.findById(DETAIL_ID)).thenReturn(Optional.of(existing));
        when(notificationDetailRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while updating notification detail").contains("boom");
        verify(notificationDetailRepository, times(1)).findById(DETAIL_ID);
        verify(notificationDetailRepository, times(1)).save(existing);
        verifyNoMoreInteractions(notificationDetailRepository);
    }

    @Test
    @DisplayName("handle(DeleteNotificationDetailCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteNotificationDetailCommand(DETAIL_ID);
        when(notificationDetailRepository.existsById(DETAIL_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(notificationDetailRepository, times(1)).existsById(DETAIL_ID);
        verify(notificationDetailRepository, times(1)).deleteById(DETAIL_ID);
        verifyNoMoreInteractions(notificationDetailRepository);
    }

    @Test
    @DisplayName("handle(DeleteNotificationDetailCommand) -> throws IllegalArgumentException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteNotificationDetailCommand(DETAIL_ID);
        when(notificationDetailRepository.existsById(DETAIL_ID)).thenReturn(false);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Notification detail with id " + DETAIL_ID + " not found");
        verify(notificationDetailRepository, times(1)).existsById(DETAIL_ID);
        verify(notificationDetailRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(notificationDetailRepository);
    }

    @Test
    @DisplayName("handle(DeleteNotificationDetailCommand) -> wraps deleteById failure in IllegalArgumentException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteNotificationDetailCommand(DETAIL_ID);
        when(notificationDetailRepository.existsById(DETAIL_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(notificationDetailRepository).deleteById(DETAIL_ID);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error while deleting notification detail").contains("fk");
        verify(notificationDetailRepository, times(1)).existsById(DETAIL_ID);
        verify(notificationDetailRepository, times(1)).deleteById(DETAIL_ID);
        verifyNoMoreInteractions(notificationDetailRepository);
    }
}
