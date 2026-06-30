package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve forum activity metrics (threads and messages) per area within a company.
 * @param companyId the ID of the company
 */
public record GetForumActivityByCompanyQuery(Long companyId) {}
