package pe.edu.upc.soft.work.platform.dashboard.application.internal.commandservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WorkTeamRepository;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
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
class WorkTeamCommandServiceImplTest {

    private static final Long WORKTEAM_ID = 55L;

    @Mock
    private WorkTeamRepository workteamRepository;

    @InjectMocks
    private WorkTeamCommandServiceImpl service;

    @Test
    @DisplayName("handle(CreateWorkTeamCommand) -> creates WorkTeam and returns generated id (AAA)")
    void handleCreateSuccess() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateWorkTeamCommand();
        when(workteamRepository.save(any(WorkTeam.class))).thenAnswer(inv -> {
            WorkTeam wt = inv.getArgument(0);
            ReflectionTestUtils.setId(wt, WORKTEAM_ID);
            return wt;
        });

        // Act
        Long resultId = service.handle(command);

        // Assert
        assertThat(resultId).isEqualTo(WORKTEAM_ID);
        verify(workteamRepository, times(1)).save(any(WorkTeam.class));
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(CreateWorkTeamCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleCreateSaveFailure() {
        // Arrange
        var command = DashboardCommandFixtures.validCreateWorkTeamCommand();
        when(workteamRepository.save(any(WorkTeam.class))).thenThrow(new RuntimeException("db"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error creating WorkTeam").contains("db");
        verify(workteamRepository, times(1)).save(any(WorkTeam.class));
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(UpdateWorkTeamCommand) -> returns Optional with updated WorkTeam when present (AAA)")
    void handleUpdateSuccess() {
        // Arrange
        var existing = new WorkTeam(DashboardCommandFixtures.validCreateWorkTeamCommand());
        ReflectionTestUtils.setId(existing, WORKTEAM_ID);
        var command = DashboardCommandFixtures.updateWorkTeamCommand(WORKTEAM_ID);
        when(workteamRepository.existsById(WORKTEAM_ID)).thenReturn(true);
        when(workteamRepository.findById(WORKTEAM_ID)).thenReturn(Optional.of(existing));
        when(workteamRepository.save(existing)).thenReturn(existing);

        // Act
        Optional<WorkTeam> result = service.handle(command);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTeamName()).isEqualTo(DashboardCommandFixtures.VALID_TEAM_NAME);
        assertThat(result.get().getLeaderOfTeam()).isEqualTo(DashboardCommandFixtures.VALID_TEAM_LEADER);
        verify(workteamRepository, times(1)).existsById(WORKTEAM_ID);
        verify(workteamRepository, times(1)).findById(WORKTEAM_ID);
        verify(workteamRepository, times(1)).save(existing);
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(UpdateWorkTeamCommand) -> throws RuntimeException when id does not exist (AAA)")
    void handleUpdateMissing() {
        // Arrange
        var command = DashboardCommandFixtures.updateWorkTeamCommand(WORKTEAM_ID);
        when(workteamRepository.existsById(WORKTEAM_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(WORKTEAM_ID)).contains("does not exist");
        verify(workteamRepository, times(1)).existsById(WORKTEAM_ID);
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(UpdateWorkTeamCommand) -> wraps save failure in RuntimeException (AAA)")
    void handleUpdateSaveFailure() {
        // Arrange
        var existing = new WorkTeam(DashboardCommandFixtures.validCreateWorkTeamCommand());
        ReflectionTestUtils.setId(existing, WORKTEAM_ID);
        var command = DashboardCommandFixtures.updateWorkTeamCommand(WORKTEAM_ID);
        when(workteamRepository.existsById(WORKTEAM_ID)).thenReturn(true);
        when(workteamRepository.findById(WORKTEAM_ID)).thenReturn(Optional.of(existing));
        when(workteamRepository.save(existing)).thenThrow(new RuntimeException("boom"));

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error updating WorkTeam").contains("boom");
        verify(workteamRepository, times(1)).existsById(WORKTEAM_ID);
        verify(workteamRepository, times(1)).findById(WORKTEAM_ID);
        verify(workteamRepository, times(1)).save(existing);
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(DeleteWorkTeamCommand) -> deletes when present (AAA)")
    void handleDeleteSuccess() {
        // Arrange
        var command = new DeleteWorkTeamCommand(WORKTEAM_ID);
        when(workteamRepository.existsById(WORKTEAM_ID)).thenReturn(true);

        // Act
        service.handle(command);

        // Assert
        verify(workteamRepository, times(1)).existsById(WORKTEAM_ID);
        verify(workteamRepository, times(1)).deleteById(WORKTEAM_ID);
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(DeleteWorkTeamCommand) -> throws RuntimeException when id is absent (AAA)")
    void handleDeleteMissing() {
        // Arrange
        var command = new DeleteWorkTeamCommand(WORKTEAM_ID);
        when(workteamRepository.existsById(WORKTEAM_ID)).thenReturn(false);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains(String.valueOf(WORKTEAM_ID)).contains("does not exist");
        verify(workteamRepository, times(1)).existsById(WORKTEAM_ID);
        verify(workteamRepository, never()).deleteById(any(Long.class));
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(DeleteWorkTeamCommand) -> wraps deleteById failure in RuntimeException (AAA)")
    void handleDeleteDeleteFailure() {
        // Arrange
        var command = new DeleteWorkTeamCommand(WORKTEAM_ID);
        when(workteamRepository.existsById(WORKTEAM_ID)).thenReturn(true);
        doThrow(new RuntimeException("fk")).when(workteamRepository).deleteById(WORKTEAM_ID);

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.handle(command));
        assertThat(ex.getMessage()).contains("Error deleting WorkTeam").contains("fk");
        verify(workteamRepository, times(1)).existsById(WORKTEAM_ID);
        verify(workteamRepository, times(1)).deleteById(WORKTEAM_ID);
        verifyNoMoreInteractions(workteamRepository);
    }
}
