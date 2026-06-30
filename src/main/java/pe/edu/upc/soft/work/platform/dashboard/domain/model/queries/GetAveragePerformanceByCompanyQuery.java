package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve the average performance classification for all employees
 * belonging to a given company.
 * @param companyId the ID of the company
 */
public record GetAveragePerformanceByCompanyQuery(Long companyId) {}
