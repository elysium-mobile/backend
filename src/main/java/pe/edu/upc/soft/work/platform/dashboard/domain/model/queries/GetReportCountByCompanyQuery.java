package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve the total number of reports per area within a company.
 * @param companyId the ID of the company
 */
public record GetReportCountByCompanyQuery(Long companyId) {}
