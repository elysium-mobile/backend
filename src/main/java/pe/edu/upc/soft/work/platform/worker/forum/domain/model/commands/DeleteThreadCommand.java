package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

/**
 * Command to delete a Thread
 */
public record DeleteThreadCommand(Long threadId) {

    /**
     * Constructor with validation
     */
    public DeleteThreadCommand {
        if (threadId == null || threadId <= 0) {
            throw new IllegalArgumentException("[DeleteThreadCommand] threadId must be a positive number");
        }
    }
}
