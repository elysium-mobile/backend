package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

import pe.edu.upc.soft.work.platform.iam.domain.model.aggregates.User;

import java.util.Objects;

/**
 * Command to create a new user account
 * @param userId the id of the user
 * @param email the email of the user account
 * @param password the password of the user account
 * @param anonymousName the anonymous name of the user account
 */
public record CreateUserAccountCommand(Long userId, String email, String password, String anonymousName) {

    public CreateUserAccountCommand{
        Objects.requireNonNull(userId, "[CreateUserAccountCommand] userId must not be null");
        Objects.requireNonNull(email, "[CreateUserAccountCommand] email must not be null");
        Objects.requireNonNull(password, "[CreateUserAccountCommand] password must not be null");
        Objects.requireNonNull(anonymousName, "[CreateUserAccountCommand] anonymousName must not be null");
    }
}
