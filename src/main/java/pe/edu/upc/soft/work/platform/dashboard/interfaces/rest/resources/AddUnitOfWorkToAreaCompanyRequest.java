package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for adding a unit of work to an area company.
 * @param unitOfWorkId  the ID of the unit of work to be added to the area company
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddUnitOfWorkToAreaCompanyRequest(
        @NotNull
        Long unitOfWorkId
) {
}
