package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * Request object for updating an existing Company.
 */
public record UpdateCompanyRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        @JsonProperty("RUC")
        String RUC,
        @NotNull
        @NotBlank
        @JsonProperty("contactEmail")
        String contactEmail,
        @NotNull
        @NotBlank
        @JsonProperty("contactPhone")
        String contactPhone
) {}
