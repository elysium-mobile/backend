package pe.edu.upc.soft.work.platform.iam.interfaces.rest.resources;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 * Response resource for the Google sign-in step.
 * Discriminates between an already registered account and one that still needs to complete
 * registration, so the frontend can decide whether to open a session or route the user to the
 * Google sign-up form.
 *
 * @param registered {@code true} when the account already exists and a session token is issued;
 *                   {@code false} when the user must still complete registration
 * @param id the identifier of the authenticated user account, {@code null} when not registered
 * @param email the email of the authenticated user account, {@code null} when not registered
 * @param token the application access token, {@code null} when not registered
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleAuthenticationResponse(boolean registered, Long id, String email, String token) {

    /**
     * Builds a response for an already registered account with an active session token.
     * @param id the user account identifier
     * @param email the user account email
     * @param token the application access token
     * @return a response flagged as registered
     */
    public static GoogleAuthenticationResponse registered(Long id, String email, String token) {
        return new GoogleAuthenticationResponse(true, id, email, token);
    }

    /**
     * Builds a response indicating that registration must still be completed.
     * @return a response flagged as not registered, with no session data
     */
    public static GoogleAuthenticationResponse registrationRequired() {
        return new GoogleAuthenticationResponse(false, null, null, null);
    }
}
