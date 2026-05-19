package pe.edu.upc.soft.work.platform.iam.domain.services;

import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserCommand;

import java.util.Optional;

/**
 * Service interface for handling user-related commands such as creating, updating, and deleting users.
 */
public interface UserCommandService {

    /**
     * Handles the creation of a new user.
     * @param command the command containing the necessary information to create a user
     * @return the identifier of the newly created user
     */
    Long handle(CreateUserCommand command);

    /**
     * Handles the update of an existing user.
     * @param command the command containing the necessary information to update a user
     * @return an Optional containing the updated user if the update was successful, or an empty Optional if the user was not found
     */
    Optional<User> handle(UpdateUserCommand command);

    /**
     * Handles the deletion of an existing user.
     * @param command the command containing the identifier of the user to be deleted
     */
    void handle(DeleteUserCommand command);
}
