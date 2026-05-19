package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve a Dashboard by their unique identifier.
 */
public record GetDashboardByIdQuery(Long dashboardId) {

    /**
     * Constructor to validate the dashboardId parameter.
     */
    public GetDashboardByIdQuery {
        if (dashboardId == null || dashboardId <= 0) {
            throw new IllegalArgumentException("Dashboard ID must be a positive number.");
        }
    }
}
