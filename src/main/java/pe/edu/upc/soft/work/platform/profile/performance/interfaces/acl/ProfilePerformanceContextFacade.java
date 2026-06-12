package pe.edu.upc.soft.work.platform.profile.performance.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries.GetPerformanceByIdQuery;
import pe.edu.upc.soft.work.platform.profile.performance.domain.services.PerformanceQueryService;

/**
 * Facade for the Profile Performance Bounded Context.
 * Exposes performance record verification operations for other Bounded Contexts.
 */
@Service
public class ProfilePerformanceContextFacade {

    /**
     * Query service for performance records.
     */
    private final PerformanceQueryService performanceQueryService;

    /**
     * Constructor for ProfilePerformanceContextFacade.
     *
     * @param performanceQueryService the performance query service
     */
    public ProfilePerformanceContextFacade(PerformanceQueryService performanceQueryService) {
        this.performanceQueryService = performanceQueryService;
    }

    /**
     * Check if a performance record exists by its ID.
     *
     * @param performanceId the ID of the performance record
     * @return true if the performance record exists, false otherwise
     */
    public boolean existsPerformanceById(Long performanceId) {
        var query = new GetPerformanceByIdQuery(performanceId);
        return this.performanceQueryService.handle(query).isPresent();
    }
}
