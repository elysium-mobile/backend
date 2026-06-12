package pe.edu.upc.soft.work.platform.dashboard.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllWorkTeamQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWorkTeamByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.infrastructure.persistence.jpa.repositories.WorkTeamRepository;
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
class WorkTeamQueryServiceImplTest {

    @Mock
    private WorkTeamRepository workteamRepository;

    @InjectMocks
    private WorkTeamQueryServiceImpl service;

    private static WorkTeam sample() {
        return new WorkTeam(DashboardCommandFixtures.validCreateWorkTeamCommand());
    }

    @Test
    @DisplayName("handle(GetAllWorkTeamQuery) -> returns list from repository (AAA)")
    void handleAllReturnsList() {
        // Arrange
        List<WorkTeam> teams = List.of(sample());
        when(workteamRepository.findAll()).thenReturn(teams);

        // Act
        List<WorkTeam> result = service.handle(new GetAllWorkTeamQuery());

        // Assert
        assertThat(result).containsExactlyElementsOf(teams);
        verify(workteamRepository, times(1)).findAll();
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(GetAllWorkTeamQuery) -> returns empty list when none exist (AAA)")
    void handleAllReturnsEmpty() {
        // Arrange
        when(workteamRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<WorkTeam> result = service.handle(new GetAllWorkTeamQuery());

        // Assert
        assertThat(result).isEmpty();
        verify(workteamRepository, times(1)).findAll();
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(GetWorkTeamByIdQuery) -> returns Optional with WorkTeam when found (AAA)")
    void handleByIdReturnsPresent() {
        // Arrange
        var team = sample();
        when(workteamRepository.findById(55L)).thenReturn(Optional.of(team));

        // Act
        Optional<WorkTeam> result = service.handle(new GetWorkTeamByIdQuery(55L));

        // Assert
        assertThat(result).isPresent().containsSame(team);
        verify(workteamRepository, times(1)).findById(55L);
        verifyNoMoreInteractions(workteamRepository);
    }

    @Test
    @DisplayName("handle(GetWorkTeamByIdQuery) -> returns Optional.empty when no WorkTeam found (AAA)")
    void handleByIdReturnsEmpty() {
        // Arrange
        when(workteamRepository.findById(55L)).thenReturn(Optional.empty());

        // Act
        Optional<WorkTeam> result = service.handle(new GetWorkTeamByIdQuery(55L));

        // Assert
        assertThat(result).isEmpty();
        verify(workteamRepository, times(1)).findById(55L);
        verifyNoMoreInteractions(workteamRepository);
    }
}
