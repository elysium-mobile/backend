package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWidgetCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.Widget;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateWidgetRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateWidgetRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.WidgetResponse;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class WidgetAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateWidgetRequest) -> maps all fields to CreateWidgetCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateWidgetRequest(
                DashboardCommandFixtures.VALID_WIDGET_TITLE,
                DashboardCommandFixtures.VALID_REFRESH_PERIOD,
            DashboardCommandFixtures.VALID_DASHBOARD_ID);

        // Act
        CreateWidgetCommand command = WidgetAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.title()).isEqualTo(DashboardCommandFixtures.VALID_WIDGET_TITLE);
        assertThat(command.refreshPeriod()).isEqualTo(DashboardCommandFixtures.VALID_REFRESH_PERIOD);
        assertThat(command.dashboardId()).isEqualTo(DashboardCommandFixtures.VALID_DASHBOARD_ID);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateWidgetRequest) -> maps id and all fields to UpdateWidgetCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateWidgetRequest(
                DashboardCommandFixtures.VALID_WIDGET_TITLE,
                DashboardCommandFixtures.VALID_REFRESH_PERIOD,
            DashboardCommandFixtures.VALID_DASHBOARD_ID);

        // Act
        UpdateWidgetCommand command = WidgetAssembler.toCommandFromRequest(44L, request);

        // Assert
        assertThat(command.widgetId()).isEqualTo(44L);
        assertThat(command.title()).isEqualTo(DashboardCommandFixtures.VALID_WIDGET_TITLE);
        assertThat(command.refreshPeriod()).isEqualTo(DashboardCommandFixtures.VALID_REFRESH_PERIOD);
        assertThat(command.dashboardId()).isEqualTo(DashboardCommandFixtures.VALID_DASHBOARD_ID);
    }

    @Test
    @DisplayName("toResponseFromEntity(Widget) -> maps every field to WidgetResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new Widget(DashboardCommandFixtures.validCreateWidgetCommand());
        ReflectionTestUtils.setId(entity, 44L);

        // Act
        WidgetResponse response = WidgetAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.widgetId()).isEqualTo(44L);
        assertThat(response.title()).isEqualTo(DashboardCommandFixtures.VALID_WIDGET_TITLE);
        assertThat(response.refreshPeriod()).isEqualTo(DashboardCommandFixtures.VALID_REFRESH_PERIOD);
        assertThat(response.dashboardId()).isEqualTo(DashboardCommandFixtures.VALID_DASHBOARD_ID);
    }
}
