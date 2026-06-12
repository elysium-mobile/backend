package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllUnitOfWorkQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetUnitOfWorkByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.UnitOfWorkRepository;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitOfWorkQueryServiceImplTest {

    @Mock
    private UnitOfWorkRepository unitofworkRepository;

    @InjectMocks
    private UnitOfWorkQueryServiceImpl service;

    private static UnitOfWork sample() {
        return new UnitOfWork(DashboardCommandFixtures.validCreateUnitOfWorkCommand());
    }

    @Test
    @DisplayName("handle(GetAllUnitOfWorkQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<UnitOfWork> units = List.of(sample());
        when(unitofworkRepository.findAll()).thenReturn(units);

        // Act
        List<UnitOfWork> result = service.handle(new GetAllUnitOfWorkQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(units);
        verify(unitofworkRepository, times(1)).findAll();
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(GetAllUnitOfWorkQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(unitofworkRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<UnitOfWork> result = service.handle(new GetAllUnitOfWorkQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(unitofworkRepository, times(1)).findAll();
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(GetUnitOfWorkByIdQuery) -> returns Optional with UnitOfWork when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var unit = sample();
        when(unitofworkRepository.findById(33L)).thenReturn(Optional.of(unit));

        // Act
        Optional<UnitOfWork> result = service.handle(new GetUnitOfWorkByIdQuery(33L));

        // Assert
        assertThat(result).isPresent().containsSame(unit);
        verify(unitofworkRepository, times(1)).findById(33L);
        verifyNoMoreInteractions(unitofworkRepository);
    }

    @Test
    @DisplayName("handle(GetUnitOfWorkByIdQuery) -> returns Optional.empty when no UnitOfWork found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(unitofworkRepository.findById(33L)).thenReturn(Optional.empty());

        // Act
        Optional<UnitOfWork> result = service.handle(new GetUnitOfWorkByIdQuery(33L));

        // Assert
        assertThat(result).isEmpty();
        verify(unitofworkRepository, times(1)).findById(33L);
        verifyNoMoreInteractions(unitofworkRepository);
    }
}
