package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for creating a new user.
 * @param name the name of the user to be created
 * @param lastName the last name of the user to be created
 * @param phoneNumber the phone number of the user to be created
 * @param dni the dni of the user to be created
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateUserRequest(
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
){}
