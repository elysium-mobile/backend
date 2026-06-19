package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 *  Request body for adding an employee to a company.
 * @param employeeId the ID of the employee to be added to the company
 */
public record AddEmployeeToCompanyRequest(
        @NotNull
        @JsonProperty("employeeId")
        Long employeeId
) {
}
