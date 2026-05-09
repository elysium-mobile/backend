package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.WorkTeam;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetWorkTeamByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllWorkTeamQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying WorkTeams in the system.
 */
public interface WorkTeamQueryService {

    /**
     * Retrieves a list of all WorkTeams in the system.
     */
    List<WorkTeam> handle(GetAllWorkTeamQuery query);

    /**
     * Retrieves a WorkTeam by their unique identifier.
     */
    Optional<WorkTeam> handle(GetWorkTeamByIdQuery query);
}
