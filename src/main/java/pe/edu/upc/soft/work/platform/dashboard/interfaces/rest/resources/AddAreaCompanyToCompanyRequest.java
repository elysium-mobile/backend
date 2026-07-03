package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for adding an area company to a company.
 * @param areaCompanyId the ID of the area company to be added to the company
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddAreaCompanyToCompanyRequest(
        @NotNull
        Long areaCompanyId
) {
}
