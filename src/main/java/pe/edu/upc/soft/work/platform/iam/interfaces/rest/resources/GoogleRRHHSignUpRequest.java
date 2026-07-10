package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request resource to complete an RRHH sign-up started with Google authentication.
 * Carries the Google id_token (re-validated on the server to obtain the trusted email) plus the
 * real profile data captured in the completion form. Neither email nor password is accepted from
 * the client: the email comes from the token and the account is Google-backed.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleRRHHSignUpRequest(
        @NotNull @NotBlank
        String idToken,
        @NotNull @NotBlank
        String name,
        @NotNull @NotBlank
        String lastName,
        @NotNull @NotBlank
        String phoneNumber,
        @NotNull @NotBlank
        String dni,
        @NotNull @NotBlank
        String RRHHDepartment,
        @NotNull @NotBlank
        String statusHierarchy
) {
}
