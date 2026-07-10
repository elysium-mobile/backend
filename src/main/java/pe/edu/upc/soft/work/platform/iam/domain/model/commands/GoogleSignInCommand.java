package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import java.util.Objects;

/**
 * Command to sign in a user through Google authentication.
 * @param idToken the Google id_token issued by Google Identity Services to be validated
 */
public record GoogleSignInCommand(String idToken) {

    /**
     * Constructor with validation.
     * @param idToken the Google id_token to be validated
     */
    public GoogleSignInCommand {
        Objects.requireNonNull(idToken, "[GoogleSignInCommand] idToken must not be null");
        if (idToken.isBlank()) {
            throw new IllegalArgumentException("[GoogleSignInCommand] idToken must not be blank");
        }
    }
}
