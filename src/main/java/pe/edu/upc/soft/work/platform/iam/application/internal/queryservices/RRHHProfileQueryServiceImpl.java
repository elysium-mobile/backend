package pe.edu.upc.soft.work.platform.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.iam.domain.model.entities.RRHHProfile;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetAllRRHHProfileQuery;
import pe.edu.upc.soft.work.platform.iam.domain.model.queries.GetRRHHProfileByIdQuery;
import pe.edu.upc.soft.work.platform.iam.domain.services.RRHHProfileQueryService;
import pe.edu.upc.soft.work.platform.iam.infrastructure.persistence.jpa.repositories.RRHHProfileRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the RRHHProfileQueryService interface.
 */
@Service
public class RRHHProfileQueryServiceImpl implements RRHHProfileQueryService {

    private final RRHHProfileRepository rrhhProfileRepository;

    /**
     * Constructor of the class.
     * @param rrhhProfileRepository the repository to be used by the class.
     */
    public RRHHProfileQueryServiceImpl(RRHHProfileRepository rrhhProfileRepository) {
        this.rrhhProfileRepository = rrhhProfileRepository;
    }

    /**
     * Handles the GetAllRRHHProfileQuery by retrieving all RRHH profiles from the repository.
     * @param query the query object containing any necessary parameters for retrieving the RRHH profiles
     * @return a list of RRHHProfile objects retrieved from the repository
     */
    @Override
    public List<RRHHProfile> handle(GetAllRRHHProfileQuery query) {
        return rrhhProfileRepository.findAll();
    }

    /**
     * Handles the GetRRHHProfileByIdQuery by retrieving a specific RRHH profile from the repository based on its unique identifier.
     * @param query the query object containing the unique identifier of the RRHH profile to be retrieved
     * @return an Optional containing the RRHHProfile object if found, or an empty Optional if no profile with the specified identifier exists in the repository
     */
    @Override
    public Optional<RRHHProfile> handle(GetRRHHProfileByIdQuery query) {
        return rrhhProfileRepository.findById(query.RRHHProfileId());
    }
}
