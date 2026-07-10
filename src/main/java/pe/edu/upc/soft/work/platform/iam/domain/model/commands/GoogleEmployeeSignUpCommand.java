package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import java.util.Date;
import java.util.Objects;

/**
 * Command to complete the sign-up of an employee authenticated through Google.
 * The email is not part of this command: it is derived from the verified Google id_token
 * on the server side, so it cannot be spoofed by the client. Every other field is real data
 * supplied by the user in the completion form.
 *
 * @param idToken the Google id_token to be re-validated to obtain the trusted email and subject
 * @param name the name of the user
 * @param lastName the last name of the user
 * @param phoneNumber the phone number of the user
 * @param dni the dni of the user
 * @param dateStart the date when the employee started working
 * @param position the position of the employee
 * @param salary the salary of the employee
 */
public record GoogleEmployeeSignUpCommand(String idToken,
                                          String name,
                                          String lastName,
                                          String phoneNumber,
                                          String dni,
                                          Date dateStart,
                                          String position,
                                          Integer salary) {

    /**
     * Constructor with validation of the security-critical id_token.
     * @param idToken the Google id_token to be validated
     */
    public GoogleEmployeeSignUpCommand {
        Objects.requireNonNull(idToken, "[GoogleEmployeeSignUpCommand] idToken must not be null");
        if (idToken.isBlank()) {
            throw new IllegalArgumentException("[GoogleEmployeeSignUpCommand] idToken must not be blank");
        }
    }
}
