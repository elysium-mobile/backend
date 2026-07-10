package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import java.util.Objects;

/**
 * Command to complete the sign-up of an RRHH user authenticated through Google.
 * The email is not part of this command: it is derived from the verified Google id_token
 * on the server side, so it cannot be spoofed by the client. Every other field is real data
 * supplied by the user in the completion form.
 *
 * @param idToken the Google id_token to be re-validated to obtain the trusted email and subject
 * @param name the name of the user
 * @param lastName the last name of the user
 * @param phoneNumber the phone number of the user
 * @param dni the dni of the user
 * @param RRHHDepartment the department of the RRHH profile
 * @param statusHierarchy the status hierarchy of the RRHH profile
 */
public record GoogleRRHHSignUpCommand(String idToken,
                                      String name,
                                      String lastName,
                                      String phoneNumber,
                                      String dni,
                                      String RRHHDepartment,
                                      String statusHierarchy) {

    /**
     * Constructor with validation of the security-critical id_token.
     * @param idToken the Google id_token to be validated
     */
    public GoogleRRHHSignUpCommand {
        Objects.requireNonNull(idToken, "[GoogleRRHHSignUpCommand] idToken must not be null");
        if (idToken.isBlank()) {
            throw new IllegalArgumentException("[GoogleRRHHSignUpCommand] idToken must not be blank");
        }
    }
}
