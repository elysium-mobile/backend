package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record EmployeeProfileSignUpRequest(
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
        @JsonProperty("anonymous_name")
        String anonymousName,
        @NotNull @NotBlank
        Date dateStart,
        @NotNull @NotBlank
        @JsonProperty("position")
        String position,
        @NotNull @NotBlank
        Integer salary

        ) {
}
