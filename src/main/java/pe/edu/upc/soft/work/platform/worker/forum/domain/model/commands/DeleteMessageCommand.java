package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

/**
 * Command to delete a Message
 */
public record DeleteMessageCommand(Long messageId) {

    /**
     * Constructor with validation
     */
    public DeleteMessageCommand {
        if (messageId == null || messageId <= 0) {
            throw new IllegalArgumentException("[DeleteMessageCommand] messageId must be a positive number");
        }
    }
}
