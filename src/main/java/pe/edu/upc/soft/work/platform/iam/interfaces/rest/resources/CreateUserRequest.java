package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public record CreateUserRequest(
        @NotNull
        @NotBlank
        @JsonProperty("name")
        String name,

        @NotNull
        @NotBlank
        @JsonProperty("last_name")
        String lastName,

        @NotNull
        @NotBlank
        @JsonProperty("phone_number")
        String phoneNumber,

        @NotNull
        @NotBlank
        @Min(0)
        @JsonProperty("dni")
        String dni
){}
