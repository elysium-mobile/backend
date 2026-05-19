package pe.edu.upc.soft.work.platform.dashboard.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * Request object for creating a new Company.
 */
public record CreateCompanyRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String RUC,
        @NotNull
        @NotBlank
        String contactEmail,
        @NotNull
        @NotBlank
        String contactPhone
) {}
