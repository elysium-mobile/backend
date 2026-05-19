package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRRHHProfileRequest(

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
