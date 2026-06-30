package pe.edu.upc.soft.work.platform.dashboard.domain.model.queries;

/**
 * Query to retrieve the positive survey response rate for a given company.
 * @param companyId         the ID of the company
 * @param positiveThreshold minimum scoreAnswer considered positive (e.g. 3 on a 1-5 scale)
 */
public record GetPositiveSurveyRateByCompanyQuery(Long companyId, int positiveThreshold) {}
