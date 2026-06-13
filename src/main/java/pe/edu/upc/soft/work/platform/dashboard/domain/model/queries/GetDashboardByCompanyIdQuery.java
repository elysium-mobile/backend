package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 *  Query to retrieve a Dashboard by its associated Company ID.
 * @param companyId the ID of the Company for which to retrieve the Dashboard.
 */
public record GetDashboardByCompanyIdQuery(Long companyId) {
}
