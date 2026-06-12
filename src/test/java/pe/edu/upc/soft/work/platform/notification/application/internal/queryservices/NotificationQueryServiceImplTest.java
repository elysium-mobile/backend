package pe.edu.upc.soft.work.platform.notification.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.notification.domain.model.aggregates.Notification;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationsQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationByIdQuery;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationRepository;
import pe.edu.upc.soft.work.platform.notification.test.fixtures.NotificationCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationQueryServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationQueryServiceImpl service;

    private static Notification sample() {
        return new Notification(NotificationCommandFixtures.validCreateNotificationCommand());
    }

    @Test
    @DisplayName("handle(GetAllNotificationsQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Notification> notifications = List.of(sample(), sample());
        when(notificationRepository.findAll()).thenReturn(notifications);

        // Act
        List<Notification> result = service.handle(new GetAllNotificationsQuery());

        // Assert
        assertThat(result).hasSize(2).containsExactlyElementsOf(notifications);
        verify(notificationRepository, times(1)).findAll();
        verifyNoMoreInteractions(notificationRepository);
    }

    @Test
    @DisplayName("handle(GetAllNotificationsQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(notificationRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Notification> result = service.handle(new GetAllNotificationsQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(notificationRepository, times(1)).findAll();
        verifyNoMoreInteractions(notificationRepository);
    }

    @Test
    @DisplayName("handle(GetNotificationByIdQuery) -> returns Optional with Notification when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var notification = sample();
        when(notificationRepository.findById(12L)).thenReturn(Optional.of(notification));

        // Act
        Optional<Notification> result = service.handle(new GetNotificationByIdQuery(12L));

        // Assert
        assertThat(result).isPresent().containsSame(notification);
        verify(notificationRepository, times(1)).findById(12L);
        verifyNoMoreInteractions(notificationRepository);
    }

    @Test
    @DisplayName("handle(GetNotificationByIdQuery) -> returns Optional.empty when no Notification found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(notificationRepository.findById(12L)).thenReturn(Optional.empty());

        // Act
        Optional<Notification> result = service.handle(new GetNotificationByIdQuery(12L));

        // Assert
        assertThat(result).isEmpty();
        verify(notificationRepository, times(1)).findById(12L);
        verifyNoMoreInteractions(notificationRepository);
    }
}
