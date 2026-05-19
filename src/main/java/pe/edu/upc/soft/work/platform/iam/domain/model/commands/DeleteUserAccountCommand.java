package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

/**
 * Command to delete a user account.
 * @param userAccountId the identifier of the user account to be deleted
 */
public record DeleteUserAccountCommand(Long userAccountId) {
}
