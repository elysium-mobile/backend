package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record AddEmployeeToCompanyRequest(
        @NotNull
        @JsonProperty("employeeId")
        Long employeeId
) {
}
