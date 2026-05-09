package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

/**
 * Command to delete a Forum
 */
public record DeleteForumCommand(Long forumId) {

    /**
     * Constructor with validation
     */
    public DeleteForumCommand {
        if (forumId == null || forumId <= 0) {
            throw new IllegalArgumentException("[DeleteForumCommand] forumId must be a positive number");
        }
    }
}
