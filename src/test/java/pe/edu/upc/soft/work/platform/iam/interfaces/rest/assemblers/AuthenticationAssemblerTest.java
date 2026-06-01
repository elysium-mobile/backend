package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.EmployeeSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.RRHHSignUpCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.AuthenticatedUserAccountResponse;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.EmployeeProfileSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.RRHHProfileSignUpRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.SignInRequest;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.CommonCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.UserInputFixture;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequestSignIn(SignInRequest) -> maps all fields to SignInCommand (AAA)")
    void toCommandFromRequestSignInMapsAllFields() {
        // Arrange
        var request = new SignInRequest(CommonCommandFixtures.VALID_EMAIL, CommonCommandFixtures.VALID_PASSWORD);

        // Act
        SignInCommand command = AuthenticationAssembler.toCommandFromRequestSignIn(request);

        // Assert
        assertThat(command.email()).isEqualTo(CommonCommandFixtures.VALID_EMAIL);
        assertThat(command.password()).isEqualTo(CommonCommandFixtures.VALID_PASSWORD);
    }

    @Test
    @DisplayName("toCommandFromRequestSignUpEmployeeProfile(EmployeeProfileSignUpRequest) -> maps all fields (AAA)")
    void toCommandFromRequestEmployeeSignUpMapsAllFields() {
        // Arrange
        var date = new Date(0L);
        var request = new EmployeeProfileSignUpRequest(
                CommonCommandFixtures.VALID_NAME,
                CommonCommandFixtures.VALID_LAST_NAME,
                CommonCommandFixtures.VALID_PHONE_NUMBER,
                CommonCommandFixtures.VALID_DNI,
                CommonCommandFixtures.VALID_EMAIL,
                CommonCommandFixtures.VALID_PASSWORD,
                CommonCommandFixtures.VALID_ANONYMOUS_NAME,
                date, "Engineer", 5000);

        // Act
        EmployeeSignUpCommand command = AuthenticationAssembler.toCommandFromRequestSignUpEmployeeProfile(request);

        // Assert
        assertThat(command.name()).isEqualTo(CommonCommandFixtures.VALID_NAME);
        assertThat(command.lastName()).isEqualTo(CommonCommandFixtures.VALID_LAST_NAME);
        assertThat(command.phoneNumber()).isEqualTo(CommonCommandFixtures.VALID_PHONE_NUMBER);
        assertThat(command.dni()).isEqualTo(CommonCommandFixtures.VALID_DNI);
        assertThat(command.email()).isEqualTo(CommonCommandFixtures.VALID_EMAIL);
        assertThat(command.password()).isEqualTo(CommonCommandFixtures.VALID_PASSWORD);
        assertThat(command.anonymousName()).isEqualTo(CommonCommandFixtures.VALID_ANONYMOUS_NAME);
        assertThat(command.dateStart()).isEqualTo(date);
        assertThat(command.position()).isEqualTo("Engineer");
        assertThat(command.salary()).isEqualTo(5000);
    }

    @Test
    @DisplayName("toCommandFromRequestSignUpRRHHProfile(RRHHProfileSignUpRequest) -> maps all fields (AAA)")
    void toCommandFromRequestRRHHSignUpMapsAllFields() {
        // Arrange
        var request = new RRHHProfileSignUpRequest(
                CommonCommandFixtures.VALID_NAME,
                CommonCommandFixtures.VALID_LAST_NAME,
                CommonCommandFixtures.VALID_PHONE_NUMBER,
                CommonCommandFixtures.VALID_DNI,
                CommonCommandFixtures.VALID_EMAIL,
                CommonCommandFixtures.VALID_PASSWORD,
                CommonCommandFixtures.VALID_ANONYMOUS_NAME,
                "Recruiting", "Senior");

        // Act
        RRHHSignUpCommand command = AuthenticationAssembler.toCommandFromRequestSignUpRRHHProfile(request);

        // Assert
        assertThat(command.name()).isEqualTo(CommonCommandFixtures.VALID_NAME);
        assertThat(command.lastName()).isEqualTo(CommonCommandFixtures.VALID_LAST_NAME);
        assertThat(command.phoneNumber()).isEqualTo(CommonCommandFixtures.VALID_PHONE_NUMBER);
        assertThat(command.dni()).isEqualTo(CommonCommandFixtures.VALID_DNI);
        assertThat(command.email()).isEqualTo(CommonCommandFixtures.VALID_EMAIL);
        assertThat(command.password()).isEqualTo(CommonCommandFixtures.VALID_PASSWORD);
        assertThat(command.anonymousName()).isEqualTo(CommonCommandFixtures.VALID_ANONYMOUS_NAME);
        assertThat(command.RRHHDepartment()).isEqualTo("Recruiting");
        assertThat(command.statusHierarchy()).isEqualTo("Senior");
    }

    @Test
    @DisplayName("toResponseFromEntityUserAccount(UserAccount, String) -> maps id, email and token to response (AAA)")
    void toResponseFromEntityUserAccountMapsAllFields() {
        // Arrange
        var account = new UserAccount(
                IamCommandFixtures.createUserAccountCommandFrom(1L, UserInputFixture.valid()));
        ReflectionTestUtils.setId(account, 42L);

        // Act
        AuthenticatedUserAccountResponse response =
                AuthenticationAssembler.toResponseFromEntityUserAccount(account, "token-xyz");

        // Assert
        assertThat(response.id()).isEqualTo(42L);
        assertThat(response.gmail()).isEqualTo(CommonCommandFixtures.VALID_EMAIL);
        assertThat(response.token()).isEqualTo("token-xyz");
    }
}
