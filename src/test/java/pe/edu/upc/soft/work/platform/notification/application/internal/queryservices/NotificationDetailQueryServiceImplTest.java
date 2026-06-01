package pe.edu.upc.soft.work.platform.notification.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.notification.domain.model.entities.NotificationDetail;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetAllNotificationDetailQuery;
import pe.edu.upc.soft.work.platform.notification.domain.model.queries.GetNotificationDetailByIdQuery;
import pe.edu.upc.soft.work.platform.notification.infrastructure.persistence.jpa.repositories.NotificationDetailRepository;
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
class NotificationDetailQueryServiceImplTest {

    @Mock
    private NotificationDetailRepository notificationDetailRepository;

    @InjectMocks
    private NotificationDetailQueryServiceImpl service;

    private static NotificationDetail sample() {
        return new NotificationDetail(NotificationCommandFixtures.validCreateNotificationDetailCommand());
    }

    @Test
    @DisplayName("handle(GetAllNotificationDetailQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<NotificationDetail> details = List.of(sample());
        when(notificationDetailRepository.findAll()).thenReturn(details);

        // Act
        List<NotificationDetail> result = service.handle(new GetAllNotificationDetailQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(details);
        verify(notificationDetailRepository, times(1)).findAll();
        verifyNoMoreInteractions(notificationDetailRepository);
    }

    @Test
    @DisplayName("handle(GetAllNotificationDetailQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(notificationDetailRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<NotificationDetail> result = service.handle(new GetAllNotificationDetailQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(notificationDetailRepository, times(1)).findAll();
        verifyNoMoreInteractions(notificationDetailRepository);
    }

    @Test
    @DisplayName("handle(GetNotificationDetailByIdQuery) -> returns Optional with NotificationDetail when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var detail = sample();
        when(notificationDetailRepository.findById(21L)).thenReturn(Optional.of(detail));

        // Act
        Optional<NotificationDetail> result = service.handle(new GetNotificationDetailByIdQuery(21L));

        // Assert
        assertThat(result).isPresent().containsSame(detail);
        verify(notificationDetailRepository, times(1)).findById(21L);
        verifyNoMoreInteractions(notificationDetailRepository);
    }

    @Test
    @DisplayName("handle(GetNotificationDetailByIdQuery) -> returns Optional.empty when no NotificationDetail found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(notificationDetailRepository.findById(21L)).thenReturn(Optional.empty());

        // Act
        Optional<NotificationDetail> result = service.handle(new GetNotificationDetailByIdQuery(21L));

        // Assert
        assertThat(result).isEmpty();
        verify(notificationDetailRepository, times(1)).findById(21L);
        verifyNoMoreInteractions(notificationDetailRepository);
    }
}
