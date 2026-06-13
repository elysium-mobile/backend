package pe.edu.upc.soft.work.platform.worker.forum.domain.model.commands;

/**
 * Command to delete a Attachment
 */
public record DeleteAssetCommand(Long attachmentId) {

    /**
     * Constructor with validation
     */
    public DeleteAssetCommand {
        if (attachmentId == null || attachmentId <= 0) {
            throw new IllegalArgumentException("[DeleteAttachmentCommand] assetId must be a positive number");
        }
    }
}
