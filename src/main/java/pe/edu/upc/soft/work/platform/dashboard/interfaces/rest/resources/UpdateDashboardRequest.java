package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * Request object for updating an existing Dashboard.
 */
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
        @NotBlank
        @JsonProperty("companyId")
        Long companyId
) {}
