package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RRHHProfileSignUpRequest(
        @NotNull @NotBlank
        String name,
        @NotNull @NotBlank
        String lastname,
        @NotNull @NotBlank
        String phoneNumber,
        @NotNull @NotBlank
        String dni,
        @NotNull @NotBlank
        String email,
        @NotNull @NotBlank
        String password,
        @NotNull @NotBlank
        @JsonProperty("RRHHDepartment")
        String RRHHDepartment,
        @NotNull @NotBlank
        @JsonProperty("statusHierarchy")
        String statusHierarchy
) {

}
