package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * RRHHProfileResponse record to represent RRHH profile data in API responses.
 * @param RRHHDepartment the department of the RRHH profile
 * @param statusHierarchy the status hierarchy of the RRHH profile
 * @param userAccountId the unique identifier of the user account associated with the RRHH profile
 */
public record RRHHProfileResponse(

        Long rrhhProfileId,

        @JsonProperty("rrhh_department")
        String RRHHDepartment,

        @JsonProperty("status_hierarchy")
        String statusHierarchy,

        @JsonProperty("user_account_id")
        Long userAccountId

) {
}
