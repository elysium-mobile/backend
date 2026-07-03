package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for creating a new UnitOfWork.
 * @param name  the name of the UnitOfWork to be created
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateUnitOfWorkRequest(
        @NotNull
        @NotBlank
        String name
) {
}
