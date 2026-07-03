package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * UserAccountResponse record to represent user account data in API responses.
 * @param userId the unique identifier of the user account
 * @param email the email address associated with the user account
 * @param password the password associated with the user account
 * @param anonymousName the anonymous name associated with the user account
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserAccountResponse(

        Long userAccountId,

        Long userId,

        String email,

        String password,

        String anonymousName,

        Long membershipId,

        Long companyId

) {
}
