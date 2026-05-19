package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

/**
 * Command to delete a Attachment
 */
public record DeleteAttachmentCommand(Long attachmentId) {

    /**
     * Constructor with validation
     */
    public DeleteAttachmentCommand {
        if (attachmentId == null || attachmentId <= 0) {
            throw new IllegalArgumentException("[DeleteAttachmentCommand] attachmentId must be a positive number");
        }
    }
}
