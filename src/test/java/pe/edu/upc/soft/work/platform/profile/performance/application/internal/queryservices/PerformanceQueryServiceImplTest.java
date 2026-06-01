package pe.edu.upc.soft.work.platform.profile.performance.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetAllPerformanceQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetPerformanceByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.infrastructure.persistence.jpa.repositories.PerformanceRepository;
import pe.edu.upc.soft.work.platform.profile.performance.test.fixtures.ProfilePerformanceCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerformanceQueryServiceImplTest {

    @Mock
    private PerformanceRepository performanceRepository;

    @InjectMocks
    private PerformanceQueryServiceImpl service;

    private static Performance sample() {
        return new Performance(ProfilePerformanceCommandFixtures.validCreatePerformanceCommand());
    }

    @Test
    @DisplayName("handle(GetAllPerformanceQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Performance> records = List.of(sample());
        when(performanceRepository.findAll()).thenReturn(records);

        // Act
        List<Performance> result = service.handle(new GetAllPerformanceQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(records);
        verify(performanceRepository, times(1)).findAll();
        verifyNoMoreInteractions(performanceRepository);
    }

    @Test
    @DisplayName("handle(GetAllPerformanceQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(performanceRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Performance> result = service.handle(new GetAllPerformanceQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(performanceRepository, times(1)).findAll();
        verifyNoMoreInteractions(performanceRepository);
    }

    @Test
    @DisplayName("handle(GetPerformanceByIdQuery) -> returns Optional with Performance when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var record = sample();
        when(performanceRepository.findById(23L)).thenReturn(Optional.of(record));

        // Act
        Optional<Performance> result = service.handle(new GetPerformanceByIdQuery(23L));

        // Assert
        assertThat(result).isPresent().containsSame(record);
        verify(performanceRepository, times(1)).findById(23L);
        verifyNoMoreInteractions(performanceRepository);
    }

    @Test
    @DisplayName("handle(GetPerformanceByIdQuery) -> returns Optional.empty when no Performance found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(performanceRepository.findById(23L)).thenReturn(Optional.empty());

        // Act
        Optional<Performance> result = service.handle(new GetPerformanceByIdQuery(23L));

        // Assert
        assertThat(result).isEmpty();
        verify(performanceRepository, times(1)).findById(23L);
        verifyNoMoreInteractions(performanceRepository);
    }
}
