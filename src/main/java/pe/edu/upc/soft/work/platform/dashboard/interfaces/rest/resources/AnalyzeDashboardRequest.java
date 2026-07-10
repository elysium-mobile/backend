package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Request payload to ask the Dashboard Assistant to analyze a company's climate metrics.
 *
 * @param companyId the ID of the company to analyze
 * @param question  optional follow-up question from RRHH about the metrics
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AnalyzeDashboardRequest(Long companyId, String question) {
}
