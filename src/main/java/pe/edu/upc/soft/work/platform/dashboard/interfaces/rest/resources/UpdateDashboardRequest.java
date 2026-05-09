package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * Request object for updating an existing Dashboard.
 */
public record UpdateDashboardRequest(
        @NotNull
        @NotBlank
        Long ruc
) {}
