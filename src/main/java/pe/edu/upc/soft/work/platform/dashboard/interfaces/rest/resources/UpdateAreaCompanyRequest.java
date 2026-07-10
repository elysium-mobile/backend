package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating an existing AreaCompany.
 * @param name the new name of the AreaCompany
 * @param annualBudget  the new annual budget of the AreaCompany
 * @param companyId     the ID of the company to which the AreaCompany belongs
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateAreaCompanyRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        Integer annualBudget,
        @NotNull
        Long companyId
) {
}
