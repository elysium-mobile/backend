package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateRRHHProfileRequest(

        @NotNull
        @NotBlank
        String RRHHDepartment,

        @NotNull
        @NotBlank
        String statusHierarchy,

        @NotNull
        @NotBlank
        Long userAccountId

) {
}
