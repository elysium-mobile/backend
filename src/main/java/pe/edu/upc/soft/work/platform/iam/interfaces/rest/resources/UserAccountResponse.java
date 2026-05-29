package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserAccountResponse record to represent user account data in API responses.
 * @param userId the unique identifier of the user account
 * @param email the email address associated with the user account
 * @param password the password associated with the user account
 * @param anonymousName the anonymous name associated with the user account
 */
public record UserAccountResponse(

        Long userAccountId,

        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("email")
        String email,

        @JsonProperty("password")
        String password,

        @JsonProperty("anonymous_name")
        String anonymousName

) {
}
