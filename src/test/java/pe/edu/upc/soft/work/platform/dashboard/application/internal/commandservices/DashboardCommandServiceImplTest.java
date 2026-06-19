package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.AddWidgetToDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.CompanyRepository;
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
class DashboardCommandServiceImplTest {

    private static final Long DASHBOARD_ID = 5L;

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private WidgetRepository widgetRepository;

    @InjectMocks
    private DashboardCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateDashboardCommand) -> creates Dashboard and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateDashboardCommand();
        when(companyRepository.existsById(command.companyId())).thenReturn(true);
        when(dashboardRepository.save(any(Dashboard.class))).thenAnswer(inv -> {
            Dashboard d = inv.getArgument(0);
            ReflectionTestUtils.setId(d, DASHBOARD_ID);
            return d;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(DASHBOARD_ID);
        verify(companyRepository, times(1)).existsById(command.companyId());
        verify(dashboardRepository, times(1)).save(any(Dashboard.class));
    }

    @Test
    @DisplayName("handle(CreateDashboardCommand) -> wraps repository save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateDashboardCommand();
        when(companyRepository.existsById(command.companyId())).thenReturn(true);
        when(dashboardRepository.save(any(Dashboard.class))).thenThrow(new RuntimeException("db down"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating Dashboard").contains("db down");

        // Assert
        verify(companyRepository, times(1)).existsById(command.companyId());
        verify(dashboardRepository, times(1)).save(any(Dashboard.class));
        verifyNoMoreInteractions(dashboardRepository);
    }

    @Test
    @DisplayName("handle(UpdateDashboardCommand) -> returns Optional with updated Dashboard when id exists (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new Dashboard(DashboardCommandFixtures.validCreateDashboardCommand());
        ReflectionTestUtils.setId(existing, DASHBOARD_ID);
        var command = DashboardCommandFixtures.updateDashboardCommand(DASHBOARD_ID);

        when(dashboardRepository.existsById(DASHBOARD_ID)).thenReturn(true);
        when(companyRepository.existsById(command.companyId())).thenReturn(true);
        when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(existing));
        when(dashboardRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<Dashboard> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getRuc()).isEqualTo(DashboardCommandFixtures.VALID_RUC);
        verify(dashboardRepository).save(existing);
    }

    @Test
    @DisplayName("handle(UpdateDashboardCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = DashboardCommandFixtures.updateDashboardCommand(DASHBOARD_ID);
        when(dashboardRepository.existsById(DASHBOARD_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(DASHBOARD_ID)).contains("does not exist");
        verify(dashboardRepository, times(1)).existsById(DASHBOARD_ID);
        verifyNoMoreInteractions(dashboardRepository);
    }

    @Test
    @DisplayName("handle(UpdateDashboardCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new Dashboard(DashboardCommandFixtures.validCreateDashboardCommand());
        ReflectionTestUtils.setId(existing, DASHBOARD_ID);
        var command = DashboardCommandFixtures.updateDashboardCommand(DASHBOARD_ID);

        when(dashboardRepository.existsById(DASHBOARD_ID)).thenReturn(true);
        when(companyRepository.existsById(command.companyId())).thenReturn(true);
        when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(existing));
        when(dashboardRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating Dashboard").contains("boom");
        verify(dashboardRepository, times(1)).existsById(DASHBOARD_ID);
        verify(companyRepository, times(1)).existsById(command.companyId());
        verify(dashboardRepository, times(1)).findById(DASHBOARD_ID);
        verify(dashboardRepository, times(1)).save(existing);
        verifyNoMoreInteractions(dashboardRepository);
    }

    @Test
    @DisplayName("handle(DeleteDashboardCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteDashboardCommand(DASHBOARD_ID);
        when(dashboardRepository.existsById(DASHBOARD_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(dashboardRepository, times(1)).existsById(DASHBOARD_ID);
        verify(dashboardRepository, times(1)).deleteById(DASHBOARD_ID);
        verifyNoMoreInteractions(dashboardRepository);
    }

    @Test
    @DisplayName("handle(DeleteDashboardCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteDashboardCommand(DASHBOARD_ID);
        when(dashboardRepository.existsById(DASHBOARD_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(DASHBOARD_ID)).contains("does not exist");
        verify(dashboardRepository, times(1)).existsById(DASHBOARD_ID);
        verify(dashboardRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(dashboardRepository);
    }

    @Test
    @DisplayName("handle(DeleteDashboardCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteDashboardCommand(DASHBOARD_ID);
        when(dashboardRepository.existsById(DASHBOARD_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(dashboardRepository).deleteById(DASHBOARD_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting Dashboard").contains("fk");
        verify(dashboardRepository, times(1)).existsById(DASHBOARD_ID);
        verify(dashboardRepository, times(1)).deleteById(DASHBOARD_ID);
        verifyNoMoreInteractions(dashboardRepository);
    }

    @Test
    @DisplayName("handle(AddWidgetToDashboardCommand) -> adds widget to dashboard successfully (AAA)")
    void handleAddWidgetSuccess() {
        // Arrange
        var command = new AddWidgetToDashboardCommand(1L, 1L); // ID 1
        var dashboard = new Dashboard();
        var widget = new Widget();

        when(widgetRepository.findById(anyLong())).thenReturn(Optional.of(widget));
        when(dashboardRepository.findById(anyLong())).thenReturn(Optional.of(dashboard));

        // Act
        service.handle(command);

        // Assert
        verify(dashboardRepository, times(1)).save(dashboard);
    }
}
