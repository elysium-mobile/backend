package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import java.util.Objects;

/**
 * Command to update an existing user account
 * @param userAccountId the id of the user account to be updated
 * @param email the email of the user account to be updated
 * @param password the password of the user account to be updated
 * @param anonymousName the anonymous name of the user account to be updated
 */
public record UpdateUserAccountCommand(Long userAccountId, String email, String password, String anonymousName) {

    /**
     * Constructor with validation
     * @param userAccountId the id of the user account to be updated
     * @param email the email of the user account to be updated
     * @param password the password of the user account to be updated
     * @param anonymousName the anonymous name of the user account to be updated
     */
        public UpdateUserAccountCommand{
            Objects.requireNonNull(userAccountId, "[UpdateUserAccountCommand] user account id must not be null");
            Objects.requireNonNull(email, "[UpdateUserAccountCommand] email must not be null");
            Objects.requireNonNull(password, "[UpdateUserAccountCommand] password must not be null");
            Objects.requireNonNull(anonymousName, "[UpdateUserAccountCommand] anonymous name must not be null");
        }

}
