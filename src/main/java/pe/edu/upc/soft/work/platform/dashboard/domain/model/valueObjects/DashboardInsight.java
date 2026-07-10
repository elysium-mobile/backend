package pe.edu.upc.soft.work.platform.dashboard.domain.model.valueObjects;

import java.util.Map;

/**
 * Value object representing the result of an AI analysis over a company's
 * dashboard/climate metrics.
 *
 * @param status   a system-computed diagnosis label: "BUENO", "REGULAR" or "CRITICO"
 * @param analysis the AI-generated explanation and recommendations
 * @param metrics  the raw metrics used to build the analysis, useful for the client to render charts
 */
public record DashboardInsight(String status, String analysis, Map<String, Object> metrics) {
}
