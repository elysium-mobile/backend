package pe.edu.upc.soft.work.platform.iam.domain.services;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteUserAccountCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.GoogleSignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateUserAccountCommand;

import java.util.Optional;

/**
 * Service interface for handling commands related to UserAccount aggregate.
 */
public interface UserAccountCommandService {

    /**
     * Handles the creation of a new UserAccount based on the provided command.
     * @param command the command containing the necessary information to create a UserAccount
     * @return the identifier of the newly created UserAccount
     */
    Long handle(CreateUserAccountCommand command);

    /**
     * Handles the update of an existing UserAccount based on the provided command.
     * @param command the command containing the necessary information to update a UserAccount
     * @return an Optional containing the updated UserAccount if the update was successful, or an empty Optional if the UserAccount was not found
     */
    Optional<UserAccount> handle(UpdateUserAccountCommand command);

    /**
     * Handles the deletion of an existing UserAccount based on the provided command.
     * @param command the command containing the identifier of the UserAccount to be deleted
     */
    void handle(DeleteUserAccountCommand command);

    /**
     * Handles the user sign-in process, verifying credentials and generating an access token.
     *
     * @param command the command containing the sign-in credentials (email and password)
     * @return an Optional containing an ImmutablePair with the authenticated UserAccount
     *         and the generated access token, or an empty Optional if authentication fails
     */
    Optional<ImmutablePair<UserAccount, String>> handle(SignInCommand command);

    /**
     * Handles the Google sign-in process, validating the Google id_token, loading or creating
     * the local user account, and generating the application access token.
     *
     * @param command the command containing the Google id_token to be validated
     * @return an Optional containing an ImmutablePair with the authenticated UserAccount
     *         and the generated access token, or an empty Optional if authentication fails
     */
    Optional<ImmutablePair<UserAccount, String>> handle(GoogleSignInCommand command);
}
