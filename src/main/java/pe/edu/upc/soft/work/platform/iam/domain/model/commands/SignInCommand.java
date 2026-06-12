package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

/**
 * Command to sign in a user
 * @param email the username of the user
 * @param password the password of the user
 */
public record SignInCommand(String email, String password) {
}
