package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.CreateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.UpdateWorkTeamCommand;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.commands.DeleteWorkTeamCommand;

import java.util.Optional;

/**
 * Service interface for handling WorkTeam-related commands.
 */
public interface WorkTeamCommandService {

    /**
     * Handles the creation of a new WorkTeam.
     */
    Long handle(CreateWorkTeamCommand command);

    /**
     * Handles the update of an existing WorkTeam.
     */
    Optional<WorkTeam> handle(UpdateWorkTeamCommand command);

    /**
     * Handles the deletion of an existing WorkTeam.
     */
    void handle(DeleteWorkTeamCommand command);
}
