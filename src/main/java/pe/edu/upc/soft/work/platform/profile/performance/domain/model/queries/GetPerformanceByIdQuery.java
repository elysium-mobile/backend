package pe.edu.upc.soft.work.platform.profile.performance.domain.model.queries;

/**
 * Query to retrieve a Performance by their unique identifier.
 */
public record GetPerformanceByIdQuery(Long performanceId) {

    /**
     * Constructor to validate the performanceId parameter.
     */
    public GetPerformanceByIdQuery {
        if (performanceId == null || performanceId <= 0) {
            throw new IllegalArgumentException("Performance ID must be a positive number.");
        }
    }
}
