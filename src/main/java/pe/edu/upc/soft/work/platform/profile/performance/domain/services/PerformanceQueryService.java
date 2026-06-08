package pe.edu.upc.soft.work.platform.profile.performance.domain.services;

import pe.edu.upc.soft.work.platform.profile.performance.domain.model.aggregates.Performance;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetPerformanceByEmployeeProfileIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetPerformanceByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetAllPerformanceQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for querying Performances in the system.
 */
public interface PerformanceQueryService {

    /**
     * Retrieves a list of all Performances in the system.
     */
    List<Performance> handle(GetAllPerformanceQuery query);

    /**
     * Retrieves a Performance by their unique identifier.
     */
    Optional<Performance> handle(GetPerformanceByIdQuery query);

    Optional<Performance> handle(GetPerformanceByEmployeeProfileIdQuery query);
}
