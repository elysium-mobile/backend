package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Dashboard;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateDashboardCommand;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.DashboardResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateDashboardRequest;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class DashboardAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateDashboardRequest) -> maps ruc to CreateDashboardCommand (AAA)")
    void toCommandFromCreateRequestMapsRuc() {
        // Arrange
        var request = new CreateDashboardRequest(DashboardCommandFixtures.VALID_TITLE,
            DashboardCommandFixtures.VALID_DESCRIPTION,DashboardCommandFixtures.VALID_RUC);

        // Act
        CreateDashboardCommand command = DashboardAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.ruc()).isEqualTo(DashboardCommandFixtures.VALID_RUC);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateDashboardRequest) -> maps id and ruc to UpdateDashboardCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateDashboardRequest(DashboardCommandFixtures.VALID_TITLE,
            DashboardCommandFixtures.VALID_DESCRIPTION, DashboardCommandFixtures.VALID_RUC,
            DashboardCommandFixtures.VALID_COMPANY_ID
            );

        // Act
        UpdateDashboardCommand command = DashboardAssembler.toCommandFromRequest(5L, request);

        // Assert
        assertThat(command.dashboardId()).isEqualTo(5L);
        assertThat(command.companyId()).isEqualTo(DashboardCommandFixtures.VALID_COMPANY_ID);
        assertThat(command.title()).isEqualTo(DashboardCommandFixtures.VALID_TITLE);
        assertThat(command.description()).isEqualTo(DashboardCommandFixtures.VALID_DESCRIPTION);
        assertThat(command.ruc()).isEqualTo(DashboardCommandFixtures.VALID_RUC);
    }

    @Test
    @DisplayName("toResponseFromEntity(Dashboard) -> maps id and ruc to DashboardResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var command = new CreateDashboardCommand(
            DashboardCommandFixtures.VALID_RUC,
            DashboardCommandFixtures.VALID_TITLE,
            DashboardCommandFixtures.VALID_DESCRIPTION,
            0L,
            new ArrayList<>()
        );
        var dashboard = new Dashboard(command);
        ReflectionTestUtils.setId(dashboard, 5L);
        ReflectionTestUtils.setField(dashboard, "widgets", new ArrayList<>());

        // Act
        DashboardResponse response = DashboardAssembler.toResponseFromEntity(dashboard);

        // Assert
        assertThat(response.dashboardId()).isEqualTo(5L);
        assertThat(response.ruc()).isEqualTo(DashboardCommandFixtures.VALID_RUC);
        assertThat(response.title()).isEqualTo(DashboardCommandFixtures.VALID_TITLE);
        assertThat(response.description()).isEqualTo(DashboardCommandFixtures.VALID_DESCRIPTION);
    }
}
