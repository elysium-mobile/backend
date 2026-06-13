package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating an existing AreaCompany.
 * @param name the new name of the AreaCompany
 * @param annualBudget  the new annual budget of the AreaCompany
 * @param companyId     the ID of the company to which the AreaCompany belongs
 */
public record UpdateAreaCompanyRequest(
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
