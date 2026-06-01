package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserCommand;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateUserRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateUserRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserResponse;
import pe.edu.upc.soft.work.platform.iam.test.fixtures.IamCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.fixtures.CommonCommandFixtures;
import pe.edu.upc.soft.work.platform.shared.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class UserAssemblerTest {

    @Test
    @DisplayName("toCommandFromRequest(CreateUserRequest) -> maps all fields to CreateUserCommand (AAA)")
    void toCommandFromCreateRequestMapsAllFields() {
        // Arrange
        var request = new CreateUserRequest(
                CommonCommandFixtures.VALID_NAME,
                CommonCommandFixtures.VALID_LAST_NAME,
                CommonCommandFixtures.VALID_PHONE_NUMBER,
                CommonCommandFixtures.VALID_DNI);

        // Act
        CreateUserCommand command = UserAssembler.toCommandFromRequest(request);

        // Assert
        assertThat(command.name()).isEqualTo(CommonCommandFixtures.VALID_NAME);
        assertThat(command.lastName()).isEqualTo(CommonCommandFixtures.VALID_LAST_NAME);
        assertThat(command.phoneNumber()).isEqualTo(CommonCommandFixtures.VALID_PHONE_NUMBER);
        assertThat(command.dni()).isEqualTo(CommonCommandFixtures.VALID_DNI);
    }

    @Test
    @DisplayName("toCommandFromRequest(Long, UpdateUserRequest) -> maps id and all fields to UpdateUserCommand (AAA)")
    void toCommandFromUpdateRequestMapsAllFields() {
        // Arrange
        var request = new UpdateUserRequest(
                CommonCommandFixtures.VALID_NAME,
                CommonCommandFixtures.VALID_LAST_NAME,
                CommonCommandFixtures.VALID_PHONE_NUMBER,
                CommonCommandFixtures.VALID_DNI);

        // Act
        UpdateUserCommand command = UserAssembler.toCommandFromRequest(7L, request);

        // Assert
        assertThat(command.userId()).isEqualTo(7L);
        assertThat(command.name()).isEqualTo(CommonCommandFixtures.VALID_NAME);
        assertThat(command.lastName()).isEqualTo(CommonCommandFixtures.VALID_LAST_NAME);
        assertThat(command.phoneNumber()).isEqualTo(CommonCommandFixtures.VALID_PHONE_NUMBER);
        assertThat(command.dni()).isEqualTo(CommonCommandFixtures.VALID_DNI);
    }

    @Test
    @DisplayName("toResponseFromEntity(User) -> maps every user field to UserResponse (AAA)")
    void toResponseFromEntityMapsAllFields() {
        // Arrange
        var user = new User(IamCommandFixtures.validCreateUserCommand());
        ReflectionTestUtils.setId(user, 7L);

        // Act
        UserResponse response = UserAssembler.toResponseFromEntity(user);

        // Assert
        assertThat(response.userId()).isEqualTo(7L);
        assertThat(response.name()).isEqualTo(CommonCommandFixtures.VALID_NAME);
        assertThat(response.lastName()).isEqualTo(CommonCommandFixtures.VALID_LAST_NAME);
        assertThat(response.phoneNumber()).isEqualTo(CommonCommandFixtures.VALID_PHONE_NUMBER);
        assertThat(response.dni()).isEqualTo(CommonCommandFixtures.VALID_DNI);
    }
}
