package pe.edu.upc.soft.work.platform.worker.forum.interfaces.rest.resources;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Request payload to ask the Employee Assistant.
 *
 * @param companyId optional company ID, used to give the AI some context about the employee's company
 * @param prompt    the employee's question or concern
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AskEmployeeAssistantRequest(Long companyId, String prompt) {
}
