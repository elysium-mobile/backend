package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateWorkTeamRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateWorkTeamRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.WorkTeamResponse;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class WorkTeamAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateWorkTeamRequest) -> maps all fields to CreateWorkTeamCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateWorkTeamRequest(
                DashboardCommandFixtures.VALID_TEAM_NAME,
                DashboardCommandFixtures.VALID_TEAM_LEADER);

        // Act
        CreateWorkTeamCommand command = WorkTeamAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.teamName()).isEqualTo(DashboardCommandFixtures.VALID_TEAM_NAME);
        assertThat(command.leaderOfTeam()).isEqualTo(DashboardCommandFixtures.VALID_TEAM_LEADER);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateWorkTeamRequest) -> maps id and all fields to UpdateWorkTeamCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateWorkTeamRequest(
                DashboardCommandFixtures.VALID_TEAM_NAME,
                DashboardCommandFixtures.VALID_TEAM_LEADER);

        // Act
        UpdateWorkTeamCommand command = WorkTeamAssembler.toCommandFromRequest(55L, request);

        // Assert
        assertThat(command.workteamId()).isEqualTo(55L);
        assertThat(command.teamName()).isEqualTo(DashboardCommandFixtures.VALID_TEAM_NAME);
        assertThat(command.leaderOfTeam()).isEqualTo(DashboardCommandFixtures.VALID_TEAM_LEADER);
    }

    @Test
    @DisplayName("toResponseFromEntity(WorkTeam) -> maps every field to WorkTeamResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new WorkTeam(DashboardCommandFixtures.validCreateWorkTeamCommand());
        ReflectionTestUtils.setId(entity, 55L);

        // Act
        WorkTeamResponse response = WorkTeamAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.workTeamId()).isEqualTo(55L);
        assertThat(response.teamName()).isEqualTo(DashboardCommandFixtures.VALID_TEAM_NAME);
        assertThat(response.leaderOfTeam()).isEqualTo(DashboardCommandFixtures.VALID_TEAM_LEADER);
    }
}
