package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request record for creating a new user account.
 * @param userId the unique identifier of the user account
 * @param email the email address associated with the user account
 * @param password the password for the user account
 * @param anonymousName the anonymous name for the user account
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateUserAccountRequest(

        @NotNull
        @NotBlank
        Long userId,

        @NotNull
        @NotBlank
        String email,

        @NotNull
        @NotBlank
        String password,

        @NotNull
        @NotBlank
        String anonymousName,

        @NotNull
        @NotBlank
        Long membershipId,

        @NotNull
        @NotBlank
        Long companyId


) {
}
