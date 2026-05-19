package pe.edu.upc.soft.work.platform.iam.domain.services;

import pe.edu.upc.soft.work.platform.iam.domain.model.commands.CreateRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.DeleteRRHHProfileCommand;
import pe.edu.upc.soft.work.platform.iam.domain.model.commands.UpdateRRHHProfileCommand;
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


}
