package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Map;

/**
 * Response payload with the dashboard AI diagnosis.
 *
 * @param status   system-computed diagnosis label: "BUENO", "REGULAR" or "CRITICO"
 * @param analysis the AI-generated explanation and recommendations
 * @param metrics  the raw metrics used to build the analysis
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DashboardInsightResponse(String status, String analysis, Map<String, Object> metrics) {
}
