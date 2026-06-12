package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.CompanyId;
import pe.edu.upc.soft.work.platform.iam.domain.model.valueobjects.MembershipId;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateUserAccountRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateUserAccountRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserAccountResponse;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.CommonCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.UserInputFixture;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class UserAccountAssemblerTest {

    /**
     * Reflects the CURRENT behavior of the assembler:
     * the request's {@code password} value is passed as the command's
     * {@code email} positional argument and vice versa. See risk report.
     */
    @Test
    @DisplayName("toCommandFromRequest(CreateUserAccountRequest) -> maps fields per assembler implementation (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateUserAccountRequest(
                1L,
                CommonCommandFixtures.VALID_EMAIL,
                CommonCommandFixtures.VALID_PASSWORD,
                CommonCommandFixtures.VALID_ANONYMOUS_NAME,
                0L, 0L);

        // Act
        CreateUserAccountCommand command = UserAccountAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.userId()).isEqualTo(1L);
        // The assembler currently passes request.password() as 'email' positional arg
        assertThat(command.email()).isEqualTo(CommonCommandFixtures.VALID_PASSWORD);
        // ...and request.email() as 'password' positional arg
        assertThat(command.password()).isEqualTo(CommonCommandFixtures.VALID_EMAIL);
        assertThat(command.anonymousName()).isEqualTo(CommonCommandFixtures.VALID_ANONYMOUS_NAME);
        assertThat(command.membershipId()).isEqualTo(new MembershipId(0L));
        assertThat(command.companyId()).isEqualTo(new CompanyId(0L));
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateUserAccountRequest) -> maps id and all fields (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateUserAccountRequest(
                1L,
                CommonCommandFixtures.VALID_EMAIL,
                "newSecret",
                CommonCommandFixtures.VALID_ANONYMOUS_NAME,
                0L, 0L);

        // Act
        UpdateUserAccountCommand command = UserAccountAssembler.toCommandFromRequest(70L, request);

        // Assert
        assertThat(command.userAccountId()).isEqualTo(70L);
        assertThat(command.password()).isEqualTo("newSecret");
        assertThat(command.email()).isEqualTo(CommonCommandFixtures.VALID_EMAIL);
        assertThat(command.anonymousName()).isEqualTo(CommonCommandFixtures.VALID_ANONYMOUS_NAME);
        assertThat(command.membershipId()).isEqualTo(new MembershipId(0L));
        assertThat(command.companyId()).isEqualTo(new CompanyId(0L));
    }

    @Test
    @DisplayName("toResponseFromEntity(UserAccount) -> maps every account field to UserAccountResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var account = new UserAccount(
                IamCommandFixtures.createUserAccountCommandFrom(1L, UserInputFixture.valid()));
        ReflectionTestUtils.setId(account, 70L);

        // Act
        UserAccountResponse response = UserAccountAssembler.toResponseFromEntity(account);

        // Assert
        assertThat(response.userAccountId()).isEqualTo(70L);
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo(CommonCommandFixtures.VALID_EMAIL);
        assertThat(response.password()).isEqualTo(CommonCommandFixtures.VALID_PASSWORD);
        assertThat(response.anonymousName()).isEqualTo(CommonCommandFixtures.VALID_ANONYMOUS_NAME);
        // IamCommandFixtures.createUserAccountCommandFrom uses MembershipId(0L) and CompanyId(0L) defaults
        assertThat(response.membershipId()).isEqualTo(0L);
        assertThat(response.companyId()).isEqualTo(0L);
    }
}
