package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateUserRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String lastName,
        @NotNull
        @NotBlank
        String phoneNumber,
        @NotNull
        @NotBlank
        @Min(0)
        String dni
) {

}
