package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateRRHHProfileRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.RRHHProfileResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateRRHHProfileRequest;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class RRHHProfileAssemblerTest {

    private static final Long USER_ACCOUNT_ID = 10L;

    @Test
    @DisplayName("toCommandFromRequest(CreateRRHHProfileRequest) -> maps all fields to CreateRRHHProfileCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateRRHHProfileRequest("Recruiting", "Senior", USER_ACCOUNT_ID);

        // Act
        CreateRRHHProfileCommand command = RRHHProfileAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.RRHHDepartment()).isEqualTo("Recruiting");
        assertThat(command.statusHierarchy()).isEqualTo("Senior");
        assertThat(command.userAccountId()).isEqualTo(USER_ACCOUNT_ID);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateRRHHProfileRequest) -> maps id and fields to UpdateRRHHProfileCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateRRHHProfileRequest("Payroll", "Junior", USER_ACCOUNT_ID);

        // Act
        UpdateRRHHProfileCommand command = RRHHProfileAssembler.toCommandFromRequest(21L, request);

        // Assert
        assertThat(command.RRHHProfileId()).isEqualTo(21L);
        assertThat(command.RRHHDepartment()).isEqualTo("Payroll");
        assertThat(command.statusHierarchy()).isEqualTo("Junior");
    }

    @Test
    @DisplayName("toResponseFromEntity(RRHHProfile) -> maps id, department, hierarchy and userAccountId (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new RRHHProfile(IamCommandFixtures.validCreateRRHHProfileCommand(USER_ACCOUNT_ID));
        ReflectionTestUtils.setId(entity, 99L);

        // Act
        RRHHProfileResponse response = RRHHProfileAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.rrhhProfileId()).isEqualTo(99L);
        assertThat(response.RRHHDepartment()).isEqualTo("Recruiting");
        assertThat(response.statusHierarchy()).isEqualTo("Senior");
        assertThat(response.userAccountId()).isEqualTo(USER_ACCOUNT_ID);
    }
}
