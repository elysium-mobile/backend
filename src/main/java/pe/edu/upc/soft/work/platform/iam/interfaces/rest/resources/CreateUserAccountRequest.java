package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request record for creating a new user account.
 * @param userId the unique identifier of the user account
 * @param email the email address associated with the user account
 * @param password the password for the user account
 * @param anonymousName the anonymous name for the user account
 */
public record CreateUserAccountRequest(

        @NotNull
        @NotBlank
        @JsonProperty("user_id")
        Long userId,

        @NotNull
        @NotBlank
        @JsonProperty("email")
        String email,

        @NotNull
        @NotBlank
        @JsonProperty("password")
        String password,

        @NotNull
        @NotBlank
        @JsonProperty("anonymous_name")
        String anonymousName
) {
}
