package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * Request object for updating an existing Dashboard.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateDashboardRequest(

        @NotNull
        @NotBlank
        String title,

        @NotNull
        @NotBlank
        String description,

        @NotNull
        @NotBlank
        String ruc,

        @NotNull
        Long companyId
) {}
