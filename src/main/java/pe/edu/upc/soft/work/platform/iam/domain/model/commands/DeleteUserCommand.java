package pe.edu.upc.soft.work.platform.iam.domain.model.commands;

/**
 * Command to delete a user.
 * @param userId the identifier of the user to be deleted
 */
public record DeleteUserCommand(Long userId) {
}
