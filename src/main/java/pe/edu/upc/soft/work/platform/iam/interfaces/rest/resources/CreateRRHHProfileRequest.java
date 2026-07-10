package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request record for creating a new RRHH profile.
 * @param RRHHDepartment the department of the RRHH profile
 * @param statusHierarchy the status hierarchy of the RRHH profile
 * @param userAccountId the ID of the associated user account
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateRRHHProfileRequest(
        @NotNull
        @NotBlank
        String RRHHDepartment,

        @NotNull
        @NotBlank
        String statusHierarchy,

        @NotNull
        Long userAccountId
) {
}
