package pe.edu.upc.soft.work.platform.iam.interfaces.rest.assemblers;

import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserCommand;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.CreateUserRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UpdateUserRequest;
import pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources.UserResponse;

public class UserAssembler {

    /**
     * Converts a CreateUserRequest to a CreateUserCommand.
     * @param request the CreateUserRequest to be converted
     * @return a CreateUserCommand containing the data from the request
     */
    public static CreateUserCommand toCommandFromRequest(CreateUserRequest request) {
        return new CreateUserCommand(
            request.name(),
            request.lastName(),
            request.phoneNumber(),
            request.dni()
        );
    }

    /**
     * Converts an UpdateUserRequest to an UpdateUserCommand.
     * @param userId the identifier of the user to be updated
     * @param request the UpdateUserRequest to be converted
     * @return an UpdateUserCommand containing the data from the request and the userId
     */
    public static UpdateUserCommand toCommandFromRequest(Long userId, UpdateUserRequest request) {
        return new UpdateUserCommand(
            userId,
            request.name(),
            request.lastName(),
            request.phoneNumber(),
            request.dni()
        );
    }

    /**
     * Converts a User entity to a UserResponse.
     * @param user the User entity to be converted
     * @return a UserResponse containing the data from the User entity
     */
    public static UserResponse toResponseFromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.getDni()
        );
    }

}
