package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for updating an existing UnitOfWork.
 * @param name  the new name of the UnitOfWork to be updated
 */
public record UpdateUnitOfWorkRequest(
        @NotNull
        @NotBlank
        String name
) {
}
