package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateAreaCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.AreaCompany;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.AreaCompanyResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateAreaCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateAreaCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class AreaCompanyAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateAreaCompanyRequest) -> maps all fields to CreateAreaCompanyCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateAreaCompanyRequest(
                DashboardCommandFixtures.VALID_AREA_NAME,
                DashboardCommandFixtures.VALID_ANNUAL_BUDGET,
            DashboardCommandFixtures.VALID_COMPANY_ID);

        // Act
        CreateAreaCompanyCommand command = AreaCompanyAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.name()).isEqualTo(DashboardCommandFixtures.VALID_AREA_NAME);
        assertThat(command.annualBudget()).isEqualTo(DashboardCommandFixtures.VALID_ANNUAL_BUDGET);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateAreaCompanyRequest) -> maps id and all fields to UpdateAreaCompanyCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateAreaCompanyRequest(
                DashboardCommandFixtures.VALID_AREA_NAME,
                DashboardCommandFixtures.VALID_ANNUAL_BUDGET,
            DashboardCommandFixtures.VALID_COMPANY_ID);

        // Act
        UpdateAreaCompanyCommand command = AreaCompanyAssembler.toCommandFromRequest(22L, request);

        // Assert
        assertThat(command.areaCompanyId()).isEqualTo(22L);
        assertThat(command.name()).isEqualTo(DashboardCommandFixtures.VALID_AREA_NAME);
        assertThat(command.annualBudget()).isEqualTo(DashboardCommandFixtures.VALID_ANNUAL_BUDGET);
        assertThat(command.companyId()).isEqualTo(DashboardCommandFixtures.VALID_COMPANY_ID);
    }

    @Test
    @DisplayName("toResponseFromEntity(AreaCompany) -> maps every field to AreaCompanyResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new AreaCompany(DashboardCommandFixtures.validCreateAreaCompanyCommand());
        ReflectionTestUtils.setId(entity, 22L);

        // Act
        AreaCompanyResponse response = AreaCompanyAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.areaCompanyId()).isEqualTo(22L);
        assertThat(response.name()).isEqualTo(DashboardCommandFixtures.VALID_AREA_NAME);
        assertThat(response.annualBudget()).isEqualTo(DashboardCommandFixtures.VALID_ANNUAL_BUDGET);
        assertThat(response.companyId()).isEqualTo(DashboardCommandFixtures.VALID_COMPANY_ID);
    }
}
