package pe.edu.upc.soft.work.platform.profile.performance.domain.model.commands;

/**
 * Command to delete a CommentEmployee
 */
public record DeleteCommentEmployeeCommand(Long commentemployeeId) {

    /**
     * Constructor with validation
     */
    public DeleteCommentEmployeeCommand {
        if (commentemployeeId == null || commentemployeeId <= 0) {
            throw new IllegalArgumentException("[DeleteCommentEmployeeCommand] commentemployeeId must be a positive number");
        }
    }
}
