package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllDashboardQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetDashboardByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.DashboardRepository;
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
class DashboardQueryServiceImplTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private DashboardQueryServiceImpl service;

    private static Dashboard sample() {
        return new Dashboard(DashboardCommandFixtures.validCreateDashboardCommand());
    }

    @Test
    @DisplayName("handle(GetAllDashboardQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Dashboard> dashboards = List.of(sample());
        when(dashboardRepository.findAll()).thenReturn(dashboards);

        // Act
        List<Dashboard> result = service.handle(new GetAllDashboardQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(dashboards);
        verify(dashboardRepository, times(1)).findAll();
        verifyNoMoreInteractions(dashboardRepository);
    }

    @Test
    @DisplayName("handle(GetAllDashboardQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(dashboardRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Dashboard> result = service.handle(new GetAllDashboardQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(dashboardRepository, times(1)).findAll();
        verifyNoMoreInteractions(dashboardRepository);
    }

    @Test
    @DisplayName("handle(GetDashboardByIdQuery) -> returns Optional with Dashboard when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var dashboard = sample();
        when(dashboardRepository.findById(5L)).thenReturn(Optional.of(dashboard));

        // Act
        Optional<Dashboard> result = service.handle(new GetDashboardByIdQuery(5L));

        // Assert
        assertThat(result).isPresent().containsSame(dashboard);
        verify(dashboardRepository, times(1)).findById(5L);
        verifyNoMoreInteractions(dashboardRepository);
    }

    @Test
    @DisplayName("handle(GetDashboardByIdQuery) -> returns Optional.empty when no Dashboard found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(dashboardRepository.findById(5L)).thenReturn(Optional.empty());

        // Act
        Optional<Dashboard> result = service.handle(new GetDashboardByIdQuery(5L));

        // Assert
        assertThat(result).isEmpty();
        verify(dashboardRepository, times(1)).findById(5L);
        verifyNoMoreInteractions(dashboardRepository);
    }
}
