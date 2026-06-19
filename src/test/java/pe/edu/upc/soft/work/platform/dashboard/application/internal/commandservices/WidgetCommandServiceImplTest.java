package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.DashboardRepository;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WidgetRepository;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WidgetCommandServiceImplTest {

    private static final Long WIDGET_ID = 44L;

    @Mock
    private WidgetRepository widgetRepository;

    @Mock
    private DashboardRepository dashboardRepository;


    @InjectMocks
    private WidgetCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateWidgetCommand) -> creates Widget and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateWidgetCommand();
        when(dashboardRepository.existsById(command.dashboardId())).thenReturn(true);
        when(widgetRepository.save(any(Widget.class))).thenAnswer(inv -> {
            Widget w = inv.getArgument(0);
            ReflectionTestUtils.setId(w, WIDGET_ID);
            return w;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(WIDGET_ID);
        verify(dashboardRepository, times(1)).existsById(command.dashboardId());
        verify(widgetRepository, times(1)).save(any(Widget.class));
    }

    @Test
    @DisplayName("handle(CreateWidgetCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateWidgetCommand();
        when(dashboardRepository.existsById(command.dashboardId())).thenReturn(true);
        when(widgetRepository.save(any(Widget.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Widget").contains("db");

        // Assert
        verify(dashboardRepository, times(1)).existsById(command.dashboardId());
        verify(widgetRepository, times(1)).save(any(Widget.class));
        verifyNoMoreInteractions(widgetRepository);
    }

    @Test
    @DisplayName("handle(UpdateWidgetCommand) -> returns Optional with updated Widget when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Widget(DashboardCommandFixtures.validCreateWidgetCommand());
        ReflectionTestUtils.setId(existing, WIDGET_ID);
        var command = DashboardCommandFixtures.updateWidgetCommand(WIDGET_ID);
        when(widgetRepository.existsById(WIDGET_ID)).thenReturn(true);
        when(widgetRepository.findById(WIDGET_ID)).thenReturn(Optional.of(existing));
        when(widgetRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Widget> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(DashboardCommandFixtures.VALID_WIDGET_TITLE);
        assertThat(result.get().getRefreshPeriod()).isEqualTo(DashboardCommandFixtures.VALID_REFRESH_PERIOD);
        verify(widgetRepository, times(1)).existsById(WIDGET_ID);
        verify(widgetRepository, times(1)).findById(WIDGET_ID);
        verify(widgetRepository, times(1)).save(existing);
        verifyNoMoreInteractions(widgetRepository);
    }

    @Test
    @DisplayName("handle(UpdateWidgetCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = DashboardCommandFixtures.updateWidgetCommand(WIDGET_ID);
        when(widgetRepository.existsById(WIDGET_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(WIDGET_ID)).contains("does not exist");
        verify(widgetRepository, times(1)).existsById(WIDGET_ID);
        verifyNoMoreInteractions(widgetRepository);
    }

    @Test
    @DisplayName("handle(UpdateWidgetCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Widget(DashboardCommandFixtures.validCreateWidgetCommand());
        ReflectionTestUtils.setId(existing, WIDGET_ID);
        var command = DashboardCommandFixtures.updateWidgetCommand(WIDGET_ID);
        when(widgetRepository.existsById(WIDGET_ID)).thenReturn(true);
        when(widgetRepository.findById(WIDGET_ID)).thenReturn(Optional.of(existing));
        when(widgetRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Widget").contains("boom");
        verify(widgetRepository, times(1)).existsById(WIDGET_ID);
        verify(widgetRepository, times(1)).findById(WIDGET_ID);
        verify(widgetRepository, times(1)).save(existing);
        verifyNoMoreInteractions(widgetRepository);
    }

    @Test
    @DisplayName("handle(DeleteWidgetCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteWidgetCommand(WIDGET_ID);

        var widget = new Widget();
        ReflectionTestUtils.setId(widget, WIDGET_ID);
        widget.setDashboardId(1L);

        var dashboard = new Dashboard();
        dashboard.addWidget(widget);

        when(widgetRepository.findById(WIDGET_ID)).thenReturn(Optional.of(widget));
        when(dashboardRepository.findById(widget.getDashboardId())).thenReturn(Optional.of(dashboard));

        // Act
        service.handle(command);

        // Assert
        verify(dashboardRepository, times(1)).save(any(Dashboard.class));
        verify(widgetRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("handle(DeleteWidgetCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteWidgetCommand(WIDGET_ID);
        when(widgetRepository.findById(WIDGET_ID)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(WIDGET_ID)).contains("does not exist");

        // Assert
        verify(widgetRepository, times(1)).findById(WIDGET_ID);
        verify(dashboardRepository, never()).findById(anyLong());
        verifyNoMoreInteractions(widgetRepository);
    }

    @Test
    @DisplayName("handle(DeleteWidgetCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteWidgetCommand(WIDGET_ID);
        var widget = new Widget();
        ReflectionTestUtils.setId(widget, WIDGET_ID);
        widget.setDashboardId(1L);

        var dashboard = new Dashboard();
        dashboard.addWidget(widget);

        when(widgetRepository.findById(WIDGET_ID)).thenReturn(Optional.of(widget));
        when(dashboardRepository.findById(anyLong())).thenReturn(Optional.of(dashboard));
        when(dashboardRepository.save(any(Dashboard.class))).thenThrow(new RuntimeException("fk"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Widget").contains("fk");
        verify(dashboardRepository, times(1)).save(any(Dashboard.class));
    }
}
