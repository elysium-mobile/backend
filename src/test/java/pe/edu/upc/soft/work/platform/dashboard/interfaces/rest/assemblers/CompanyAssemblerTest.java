package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.aggregates.Company;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateCompanyCommand;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CompanyResponse;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.CreateCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources.UpdateCompanyRequest;
import pe.edu.upc.soft.work.platform.dashboard.test.fixtures.DashboardCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class CompanyAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateCompanyRequest) -> maps all fields to CreateCompanyCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateCompanyRequest(
                DashboardCommandFixtures.VALID_COMPANY_NAME,
                DashboardCommandFixtures.VALID_RUC,
                DashboardCommandFixtures.VALID_CONTACT_EMAIL,
                DashboardCommandFixtures.VALID_CONTACT_PHONE);

        // Act
        CreateCompanyCommand command = CompanyAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.name()).isEqualTo(DashboardCommandFixtures.VALID_COMPANY_NAME);
        assertThat(command.RUC()).isEqualTo(DashboardCommandFixtures.VALID_RUC);
        assertThat(command.contactEmail()).isEqualTo(DashboardCommandFixtures.VALID_CONTACT_EMAIL);
        assertThat(command.contactPhone()).isEqualTo(DashboardCommandFixtures.VALID_CONTACT_PHONE);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateCompanyRequest) -> maps id and all fields to UpdateCompanyCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateCompanyRequest(
                DashboardCommandFixtures.VALID_COMPANY_NAME,
                DashboardCommandFixtures.VALID_RUC,
                DashboardCommandFixtures.VALID_CONTACT_EMAIL,
                DashboardCommandFixtures.VALID_CONTACT_PHONE);

        // Act
        UpdateCompanyCommand command = CompanyAssembler.toCommandFromRequest(12L, request);

        // Assert
        assertThat(command.companyId()).isEqualTo(12L);
        assertThat(command.name()).isEqualTo(DashboardCommandFixtures.VALID_COMPANY_NAME);
        assertThat(command.RUC()).isEqualTo(DashboardCommandFixtures.VALID_RUC);
        assertThat(command.contactEmail()).isEqualTo(DashboardCommandFixtures.VALID_CONTACT_EMAIL);
        assertThat(command.contactPhone()).isEqualTo(DashboardCommandFixtures.VALID_CONTACT_PHONE);
    }

    @Test
    @DisplayName("toResponseFromEntity(Company) -> maps every Company field to CompanyResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var company = new Company(DashboardCommandFixtures.validCreateCompanyCommand());
        ReflectionTestUtils.setId(company, 12L);
        ReflectionTestUtils.setField(company, "employees", new ArrayList<>());
        ReflectionTestUtils.setField(company, "areaCompanyList", new ArrayList<>());

        // Act
        CompanyResponse response = CompanyAssembler.toResponseFromEntity(company);

        // Assert
        assertThat(response.companyId()).isEqualTo(12L);
        assertThat(response.name()).isEqualTo(DashboardCommandFixtures.VALID_COMPANY_NAME);
        assertThat(response.RUC()).isEqualTo(DashboardCommandFixtures.VALID_RUC);
        assertThat(response.contactEmail()).isEqualTo(DashboardCommandFixtures.VALID_CONTACT_EMAIL);
        assertThat(response.contactPhone()).isEqualTo(DashboardCommandFixtures.VALID_CONTACT_PHONE);
    }
}
