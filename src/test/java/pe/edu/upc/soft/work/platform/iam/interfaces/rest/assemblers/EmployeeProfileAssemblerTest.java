package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateEmployeeProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.EmployeeProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.WorkOfTeamId;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateEmployeeProfileRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.EmployeeProfileResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateEmployeeProfileRequest;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeProfileAssemblerTest {

    private static final Long USER_ACCOUNT_ID = 10L;
    private static final Long WORK_OF_TEAM_ID = 3L;

    @Test
    @DisplayName("toCommandFromRequest(CreateEmployeeProfileRequest) -> maps all fields including WorkOfTeamId VO (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var date = new Date(0L);
        var request = new CreateEmployeeProfileRequest(date, "Engineer", 5000, WORK_OF_TEAM_ID, USER_ACCOUNT_ID);

        // Act
        CreateEmployeeProfileCommand command = EmployeeProfileAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.dateStart()).isEqualTo(date);
        assertThat(command.position()).isEqualTo("Engineer");
        assertThat(command.salary()).isEqualTo(5000);
        assertThat(command.userAccountId()).isEqualTo(USER_ACCOUNT_ID);
        assertThat(command.workOfTeamId()).isEqualTo(new WorkOfTeamId(WORK_OF_TEAM_ID));
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateEmployeeProfileRequest) -> maps id and fields including WorkOfTeamId (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var date = new Date(0L);
        var request = new UpdateEmployeeProfileRequest(date, "Senior", 9000, WORK_OF_TEAM_ID, USER_ACCOUNT_ID);

        // Act
        UpdateEmployeeProfileCommand command = EmployeeProfileAssembler.toCommandFromRequest(55L, request);

        // Assert
        assertThat(command.employeeProfileId()).isEqualTo(55L);
        assertThat(command.dateStart()).isEqualTo(date);
        assertThat(command.position()).isEqualTo("Senior");
        assertThat(command.salary()).isEqualTo(9000);
        assertThat(command.workOfTeamId()).isEqualTo(new WorkOfTeamId(WORK_OF_TEAM_ID));
    }

    /**
     * Reflects the CURRENT behavior of the assembler: it places
     * {@code entity.getUserAccountId()} into the response's 5th positional
     * argument (which is declared as workOfTeamId) and
     * {@code entity.getWorkOfTeamId().workOfTeamId()} into the 6th positional
     * argument (declared as UserAccountId). See risk report for details.
     */
    @Test
    @DisplayName("toResponseFromEntity(EmployeeProfile) -> maps fields per assembler implementation (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var entity = new EmployeeProfile(
                IamCommandFixtures.validCreateEmployeeProfileCommand(USER_ACCOUNT_ID, WORK_OF_TEAM_ID));
        ReflectionTestUtils.setId(entity, 77L);

        // Act
        EmployeeProfileResponse response = EmployeeProfileAssembler.toResponseFromEntity(entity);

        // Assert
        assertThat(response.employeeProfileId()).isEqualTo(77L);
        assertThat(response.starStart()).isEqualTo(entity.getDateStart());
        assertThat(response.position()).isEqualTo("Engineer");
        assertThat(response.salary()).isEqualTo(5000);
        // Existing source-code swap: userAccountId lands in workOfTeamId slot
        assertThat(response.workOfTeamId()).isEqualTo(USER_ACCOUNT_ID);
        // ...and workOfTeamId lands in UserAccountId slot
        assertThat(response.UserAccountId()).isEqualTo(WORK_OF_TEAM_ID);
    }
}
