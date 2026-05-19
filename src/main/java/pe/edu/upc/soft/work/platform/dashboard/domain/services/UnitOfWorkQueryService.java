package pe.edu.upc.soft.work.platform.dashboard.domain.services;

import pe.edu.upc.soft.work.platform.dashboard.domain.model.entities.UnitOfWork;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetUnitOfWorkByIdQuery;
import pe.edu.upc.soft.work.platform.dashboard.domain.model.queries.GetAllUnitOfWorkQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying UnitOfWorks in the system.
 */
public interface UnitOfWorkQueryService {

    /**
     * Retrieves a list of all UnitOfWorks in the system.
     */
    List<UnitOfWork> handle(GetAllUnitOfWorkQuery query);

    /**
     * Retrieves a UnitOfWork by their unique identifier.
     */
    Optional<UnitOfWork> handle(GetUnitOfWorkByIdQuery query);
}
