package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request record for creating a new RRHH profile.
 * @param RRHHDepartment the department of the RRHH profile
 * @param statusHierarchy the status hierarchy of the RRHH profile
 * @param userAccountId the ID of the associated user account
 */
public record CreateRRHHProfileRequest(
        @NotNull
        @NotBlank
        @JsonProperty("rrhh_department")
        String RRHHDepartment,

        @NotNull
        @NotBlank
        @JsonProperty("status_hierarchy")
        String statusHierarchy,

        @NotNull
        @NotBlank
        @JsonProperty("user_account_id")
        Long userAccountId
) {
}
