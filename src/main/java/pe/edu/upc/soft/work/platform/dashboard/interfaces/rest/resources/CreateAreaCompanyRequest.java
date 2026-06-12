package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *  Request DTO for creating a new AreaCompany.
 */
public record CreateAreaCompanyRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        @JsonProperty("annualBudget")
        Integer annualBudget,

        @NotNull
        @NotBlank
        @JsonProperty("companyId")
        Long companyId

) {
}
