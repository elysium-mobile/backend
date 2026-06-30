package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve the number of employees per area within a given company.
 * @param companyId the ID of the company
 */
public record GetEmployeeCountByAreaQuery(Long companyId) {}
