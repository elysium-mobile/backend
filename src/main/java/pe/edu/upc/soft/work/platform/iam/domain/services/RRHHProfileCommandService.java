package pe.edu.upc.soft.work.platform.iam.domain.services;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.UserAccount;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.*;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;

import java.util.Optional;

public interface RRHHProfileCommandService {

    /**
     * Create a new RRHHProfile based on the provided command.
     * @param command The command containing the details for creating the RRHHProfile.
     * @return The identifier of the newly created RRHHProfile.
     */
    Long handle(CreateRRHHProfileCommand command);

    /**
     * Update an existing RRHHProfile based on the provided command.
     * @param command The command containing the details for updating the RRHHProfile.
     * @return An Optional containing the updated RRHHProfile if the update was successful, or an empty Optional if the RRHHProfile was not found.
     */
    Optional<RRHHProfile> handle(UpdateRRHHProfileCommand command);

    /**
     * Delete an existing RRHHProfile based on the provided command.
     * @param command The command containing the identifier of the RRHHProfile to be deleted.
     */
    void handle(DeleteRRHHProfileCommand command);

    /**
     * Handles the sign-up process for a new RRHH user.
     * This operation orchestrates the creation of a new User,
     * its corresponding UserAccount, and the specific RRHHProfile.
     *
     * @param command The command containing the details required for the RRHH sign-up.
     * @return An Optional containing the created RRHHProfile, or an empty Optional if the process failed.
     */
    Optional<RRHHProfile> handle(RRHHSignUpCommand command);

    /**
     * Handles the sign-up completion for an RRHH user authenticated through Google.
     * This operation re-validates the Google id_token to obtain the trusted email, then
     * orchestrates the creation of the User, its Google-backed UserAccount and the specific
     * RRHHProfile with the real data supplied in the completion form. On success it returns the
     * created UserAccount together with a freshly generated application access token, so the user
     * ends the flow authenticated.
     *
     * @param command the command containing the Google id_token and the RRHH profile data
     * @return an Optional containing a pair of the created UserAccount and the application access
     *         token, or an empty Optional if the process failed
     */
    Optional<ImmutablePair<UserAccount, String>> handle(GoogleRRHHSignUpCommand command);

}
