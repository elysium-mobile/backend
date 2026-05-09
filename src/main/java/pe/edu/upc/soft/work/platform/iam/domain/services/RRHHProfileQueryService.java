package pe.edu.upc.soft.work.platform.iam.domain.services;

import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllRRHHProfileQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetRRHHProfileByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying RRHH profiles in the system.
 */
public interface RRHHProfileQueryService {

    /**
     * Handles the retrieval of all RRHH profiles in the system.
     * @param query the query object containing any necessary parameters for retrieving the RRHH profiles
     * @return a list of RRHH profiles that match the criteria specified in the query
     */
    List<RRHHProfile> handle(GetAllRRHHProfileQuery query);

    /**
     * Handles the retrieval of a specific RRHH profile by its unique identifier.
     * @param query the query object containing the unique identifier of the RRHH profile to be retrieved
     * @return an Optional containing the RRHH profile if found, or an empty Optional if no profile with the specified identifier exists in the system
     */
    Optional<RRHHProfile> handle(GetRRHHProfileByIdQuery query);
}
