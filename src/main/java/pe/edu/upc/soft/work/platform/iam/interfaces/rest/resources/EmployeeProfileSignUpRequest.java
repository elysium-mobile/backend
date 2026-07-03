package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EmployeeProfileSignUpRequest(
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
        Date dateStart,
        @NotNull @NotBlank
        String position,
        @NotNull @NotBlank
        Integer salary

        ) {
}
