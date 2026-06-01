package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllWidgetQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWidgetByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WidgetRepository;
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
class WidgetQueryServiceImplTest {

    @Mock
    private WidgetRepository widgetRepository;

    @InjectMocks
    private WidgetQueryServiceImpl service;

    private static Widget sample() {
        return new Widget(DashboardCommandFixtures.validCreateWidgetCommand());
    }

    @Test
    @DisplayName("handle(GetAllWidgetQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<Widget> widgets = List.of(sample());
        when(widgetRepository.findAll()).thenReturn(widgets);

        // Act
        List<Widget> result = service.handle(new GetAllWidgetQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(widgets);
        verify(widgetRepository, times(1)).findAll();
        verifyNoMoreInteractions(widgetRepository);
    }

    @Test
    @DisplayName("handle(GetAllWidgetQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(widgetRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Widget> result = service.handle(new GetAllWidgetQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(widgetRepository, times(1)).findAll();
        verifyNoMoreInteractions(widgetRepository);
    }

    @Test
    @DisplayName("handle(GetWidgetByIdQuery) -> returns Optional with Widget when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var widget = sample();
        when(widgetRepository.findById(44L)).thenReturn(Optional.of(widget));

        // Act
        Optional<Widget> result = service.handle(new GetWidgetByIdQuery(44L));

        // Assert
        assertThat(result).isPresent().containsSame(widget);
        verify(widgetRepository, times(1)).findById(44L);
        verifyNoMoreInteractions(widgetRepository);
    }

    @Test
    @DisplayName("handle(GetWidgetByIdQuery) -> returns Optional.empty when no Widget found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(widgetRepository.findById(44L)).thenReturn(Optional.empty());

        // Act
        Optional<Widget> result = service.handle(new GetWidgetByIdQuery(44L));

        // Assert
        assertThat(result).isEmpty();
        verify(widgetRepository, times(1)).findById(44L);
        verifyNoMoreInteractions(widgetRepository);
    }
}
