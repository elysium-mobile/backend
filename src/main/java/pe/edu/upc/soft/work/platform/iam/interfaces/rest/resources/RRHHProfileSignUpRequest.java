package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RRHHProfileSignUpRequest(
        @NotNull @NotBlank
        String name,
        @NotNull @NotBlank
        String lastName,
        @NotNull @NotBlank
        String phoneNumber,
        @NotNull @NotBlank
        String dni,
        @NotNull @NotBlank
        String email,
        @NotNull @NotBlank
        String password,
        @NotNull @NotBlank
        String anonymousName,
        @NotNull @NotBlank
        String RRHHDepartment,
        @NotNull @NotBlank
        String statusHierarchy
) {

}
